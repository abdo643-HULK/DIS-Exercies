//
// Created by abous on 02/12/2021.
//

#include "TcpEchoServer.hpp"

using namespace std;

TcpEchoServer::TcpEchoServer(const IpAddrKind _addressKind) : mServerFd(-1),
                                                              mIpVersion(_addressKind),
                                                              mShutdown(false) {
    memset(mThreadPool, -1, sizeof(mThreadPool));
}

sockaddr *TcpEchoServer::setIp(const int _port, sockaddr_storage *const _data) {
    if (mIpVersion == IpAddrKind::V6) {
        const auto addr = reinterpret_cast<sockaddr_in6 *const>(_data);
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

    const auto addr = reinterpret_cast<sockaddr_in *const>(_data);
    memset(addr, 0, sizeof(sockaddr_in));
    addr->sin_family = AF_INET;
    addr->sin_port = htons(_port);
    addr->sin_addr.s_addr = htonl(INADDR_ANY);

    return reinterpret_cast<sockaddr *>(addr);
}

void TcpEchoServer::initializeSocket(const int _port, const int _optval) {
    mServerFd = socket(static_cast<int>(mIpVersion), SOCK_STREAM, 0);

    if (mServerFd < 0) {
        errorExit("SOCKET ERROR", SOCKET_ERROR);
    }

    sockaddr_storage serverAddress;

    const sockaddr *server = setIp(_port, &serverAddress);
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(mServerFd, server, addressSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, mServerFd);
    }

    if (setsockopt(mServerFd,
                   SOL_SOCKET,
                   SO_REUSEADDR,
                   reinterpret_cast<const char *>(&_optval),
                   sizeof(_optval)) < 0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, mServerFd);
    }

    if (listen(mServerFd, 10) < 0) {
        errorExit("SOCKET LISTEN ERROR", SOCKET_LISTEN_ERROR, mServerFd);
    }

    cout << "Listening..." << endl;
}

void *TcpEchoServer::clientCommunication(void *const _parameter) {
    const auto params = (ClientCommunicationParams *) _parameter;
    const auto clientFd = params->clientFd;
    const auto server = params->server;

    const auto detachRet = pthread_detach(pthread_self());
    if (detachRet != 0) {
        errorExit("ERROR DETACHING THREADS", THREAD_ERROR, clientFd);
    }

    char echoMsg[BUFFER_SIZE] = "\0";
    const auto msgSize = BUFFER_SIZE - 7;
    char msg[msgSize] = "\0";

    while (true) {
        const auto status = recv(clientFd, msg, BUFFER_SIZE, 0);

        if (status == -1) {
            errorExit("NO MSG RECEIVED", NO_MSG_ERROR, clientFd);
        } else if (status == 0) {
            cout << "CLIENT CLOSED" << endl;
            close(clientFd);
            return nullptr;
        }

        if (strcmp(msg, "shutdown") == 0) break;

        snprintf(echoMsg, sizeof(echoMsg), "%s%s", "ECHO: ", msg);

        const auto sendRet = send(clientFd, echoMsg, strlen(echoMsg), 0);
        if (sendRet == -1) errorExit("ERROR SENDING DATA", THREAD_ERROR, clientFd);

        memset(&msg, 0, msgSize);
        memset(&echoMsg, 0, BUFFER_SIZE);
    }

    delete params;

    cout << "CLOSING CLIENT" << endl;
    shutdown(clientFd, SHUT_RDWR);
    close(clientFd);

    server->shutdownServer();
    return nullptr;
}

void TcpEchoServer::startRequestHandler() {
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

        if (pthread_create(&mThreadPool[i++],
                           nullptr,
                           clientCommunication,
                           parameter) != 0) {
            printError("ERROR CREATING THREAD");
        }
    }
}

void TcpEchoServer::shutdownServer() {
    cout << "CLOSING SERVER" << endl;
    shutdown(mServerFd,SHUT_RDWR);
    mShutdown = true;
}

TcpEchoServer::~TcpEchoServer() {
    cout << "SERVER SHUTTING DOWN" << endl;
    for (unsigned long i: mThreadPool) {
        i != -1 && pthread_cancel(i);
    }
    close(mServerFd);
}