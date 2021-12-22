//
// Created by abous on 02/12/2021.
//

#include "TcpHttpServer.hpp"

using namespace std;

TcpHttpServer::TcpHttpServer(const IpAddrKind _addressKind, const int _port, const int _optval) :
        mIpVersion(_addressKind),
        mServerFd(initializeSocket(_port, _optval)),
        mShutdown(false) {
    memset(mThreadPool, -1, sizeof(mThreadPool));
}

sockaddr *TcpHttpServer::setIp(int _port, sockaddr_storage *_address) {
    if (mIpVersion == IpAddrKind::V6) {
        const auto addr = reinterpret_cast<sockaddr_in6 *const>(_address);
        memset(addr, 0, sizeof(sockaddr_in6));
        addr->sin6_family = AF_INET6;
        addr->sin6_port = htons(_port);
#ifdef IN6ADDR_ANY_INIT
        addr->sin6_addr = IN6ADDR_ANY_INIT;
#else
        addr->sin6_addr = IN6ADDR_ANY;
#endif
        return reinterpret_cast<sockaddr *>(addr);
    }

    const auto addr = reinterpret_cast<sockaddr_in *const>(_address);
    memset(addr, 0, sizeof(sockaddr_in));
    addr->sin_family = AF_INET;
    addr->sin_port = htons(_port);
    addr->sin_addr.s_addr = htonl(INADDR_ANY);

    return reinterpret_cast<sockaddr *>(addr);
}


int TcpHttpServer::initializeSocket(int _port, int _optval) {
    const auto serverFd = socket(static_cast<int>(mIpVersion), SOCK_STREAM, 0);

    if (serverFd < 0) {
        errorExit("SOCKET ERROR", SOCKET_ERROR);
    }

    sockaddr_storage serverAddress;

    const sockaddr *server = setIp(_port, &serverAddress);
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(serverFd, server, addressSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, serverFd);
    }

    if (setsockopt(serverFd,
                   SOL_SOCKET,
                   SO_REUSEADDR,
                   reinterpret_cast<const char *>(&_optval),
                   sizeof(_optval)) < 0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, serverFd);
    }

    if (listen(serverFd, 10) < 0) {
        errorExit("SOCKET LISTEN ERROR", SOCKET_LISTEN_ERROR, serverFd);
    }

    cout << "Listening..." << endl;

    return serverFd;
}

void *TcpHttpServer::clientCommunication(void *const _parameter) {
    const auto params = (ClientCommunicationParams *) _parameter;
    const auto clientFd = params->clientFd;
    const auto server = params->server;

    const auto detachRet = pthread_detach(pthread_self());
    if (detachRet != 0) {
        errorExit("ERROR DETACHING THREADS", THREAD_ERROR, clientFd);
    }

    constexpr char echoMsg[] = "BROWSER REQUEST:";
    constexpr char htmlBodyFormat[] = "<h1>%s</h1><br> <p>%s</p>";
    constexpr auto msgSize = BUFFER_SIZE - sizeof(htmlBodyFormat) - sizeof(echoMsg);

    char msg[msgSize] = "\0";
    const auto status = recv(clientFd, msg, BUFFER_SIZE, 0);

    if (status == -1) {
        errorExit("NO MSG RECEIVED", NO_MSG_ERROR, clientFd);
    } else if (status == 0) {
        cout << "CLIENT CLOSED" << endl;
        close(clientFd);
        return nullptr;
    }

    char htmlBody[BUFFER_SIZE] = "\0";
    snprintf(htmlBody, sizeof(htmlBody), htmlBodyFormat, echoMsg, msg);

    const auto htmlResponse = string() + HTML_START + htmlBody + HTML_END;
    const auto contentLength = "Content-Length: " + to_string(htmlResponse.length()) + "\n\n";

    const auto responseHeader = HTTP_HEADER + contentLength;

    const auto sendHeaderRet = send(clientFd, responseHeader.c_str(), responseHeader.length(), 0);
    if (sendHeaderRet == -1) errorExit("ERROR SENDING DATA", THREAD_ERROR, clientFd);

    const auto sendHtmlRet = send(clientFd, htmlResponse.c_str(), htmlResponse.length(), 0);
    if (sendHtmlRet == -1) errorExit("ERROR SENDING DATA", THREAD_ERROR, clientFd);

    memset(&msg, 0, msgSize);
    memset(&htmlBody, 0, BUFFER_SIZE);

    cout << "CLOSING CLIENT" << endl;
    shutdown(clientFd, SHUT_RDWR);
    close(clientFd);

    if (params->counter >= 10) {
        server->shutdownServer();
    }

    delete params;

    return nullptr;
}

void TcpHttpServer::startRequestHandler() {
    sockaddr_storage clientAddress;
    memset(&clientAddress, 0, sizeof(sockaddr_storage));

    const socklen_t size = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    int i = 0;

    while (!mShutdown) {
        const int clientFd = accept(mServerFd,
                                    reinterpret_cast<sockaddr *>(&clientAddress),
                                    const_cast<socklen_t *>(&size));

        if (clientFd == -1) continue;

        const bool ret = printClientInfo(mIpVersion, &clientAddress);
        if (!ret) continue;

        auto parameter = new ClientCommunicationParams();
        parameter->clientFd = clientFd;
        parameter->server = this;
        parameter->counter = i;

        if (pthread_create(&mThreadPool[i++],
                           nullptr,
                           clientCommunication,
                           parameter) != 0) {
            printError("ERROR CREATING THREAD");
        }
    }
}

void TcpHttpServer::shutdownServer() {
    cout << "CLOSING SERVER" << endl;
    shutdown(mServerFd, SHUT_RDWR);
    mShutdown = true;
}

TcpHttpServer::~TcpHttpServer() {
    cout << "SERVER SHUTTING DOWN" << endl;
    for (unsigned long i: mThreadPool) {
        i != -1 && pthread_cancel(i);
    }
    close(mServerFd);
}