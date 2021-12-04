//
// Created by abous on 02/12/2021.
//

#include "TcpEchoServer.hpp"

bool printClientInfo(IpAddrKind _ip, const sockaddr_storage *_address);

void errorExit(const char *_msg, int _fd1, int _exitCode = -1);

void printError(const char *_error);

using namespace std;

TcpEchoServer::TcpEchoServer(const IpAddrKind _addressKind) : mServerFd(-1), mIpVersion(_addressKind) {};

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

    if (setsockopt(mServerFd, SOL_SOCKET, SO_REUSEADDR, reinterpret_cast<const char *>(&_optval), sizeof(_optval)) <
        0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, mServerFd);
    }

    sockaddr_storage serverAddress;

    const sockaddr *server = setIp(_port, &serverAddress);
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(mServerFd, server, addressSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, mServerFd);
    }

    if (listen(mServerFd, 10) < 0) {
        errorExit("SOCKET LISTEN ERROR", SOCKET_LISTEN_ERROR, mServerFd);
    }

    cout << "Listening..." << endl;
}

bool TcpEchoServer::requestHandler(const int _clientFd) const {
    const auto msgSize = BUFFER_SIZE - 7;
    char echoMsg[BUFFER_SIZE] = "\0";
    char msg[msgSize] = "\0";

    while (true) {
        const auto status = recv(_clientFd, msg, BUFFER_SIZE, 0);

        if (status == -1) {
            printError("NO MSG RECEIVED");
            return true;
        } else if (status == 0) {
            cout << "CLIENT CLOSED" << endl;
            return true;
        }

        if (strcmp(msg, "shutdown") == 0) return false;

//        strcat((char *) &echoMsg, (char *) &msg);
        snprintf(echoMsg, sizeof(echoMsg), "%s%s", "ECHO: ", msg);

        send(_clientFd, echoMsg, strlen(echoMsg), 0);

        memset(&msg, 0, msgSize);
        memset(&echoMsg, 0, BUFFER_SIZE);
    }
}

void TcpEchoServer::startRequestHandler() {
    sockaddr_storage clientAddress;
//    memset (&clientAddress, 0, sizeof(sockaddr_storage));

    const socklen_t size = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    bool active = true;
    while (active) {
        const int clientFd = accept(mServerFd,
                                    reinterpret_cast<sockaddr *>(&clientAddress),
                                    const_cast<socklen_t *>(&size));
        const bool ret = printClientInfo(mIpVersion, &clientAddress);

        if (ret) {
            active = requestHandler(clientFd);
        }

        cout << endl;
        close(clientFd);
    }

    cout << "SERVER SHUTTING DOWN" << endl;
}

TcpEchoServer::~TcpEchoServer() {
    close(mServerFd);
}

void printError(const char *const _error) {
    cerr << endl;
    cerr << "<<< " << _error << " >>>" << endl;
}

bool printClientInfo(const IpAddrKind _ip, const sockaddr_storage *const _address) {
    char address[48];
    cout << "Client IP-Address: ";

    switch (_ip) {
        case IpAddrKind::V4: {
            const auto addr = reinterpret_cast<const sockaddr_in *const>(_address);
            const auto ret = inet_ntop(AF_INET,
                                       &addr->sin_addr,
                                       address,
                                       sizeof(sockaddr_in));
            if (ret == nullptr) {
                printError("ERROR PARSING CLIENT ADDRESS");
                return false;
            }

            cout << address << endl;
            cout << "Client Port: " << ntohs(addr->sin_port) << endl;
            break;
        }
        case IpAddrKind::V6: {
            const auto addr = reinterpret_cast<const sockaddr_in6 *const>(_address);
            const auto ret = inet_ntop(AF_INET6,
                                       &addr->sin6_addr,
                                       address,
                                       sizeof(sockaddr_in6));
            if (ret == nullptr) {
                printError("ERROR PARSING CLIENT ADDRESS");
                return false;
            }

            cout << address << endl;
            cout << "Client Port: " << ntohs(addr->sin6_port) << endl;
            break;
        }
    }

    cout << endl;

    return true;
}

void errorExit(const char *const _msg, const int _fd1, const int _exitCode) {
    cerr << endl;
    cerr << "<<< " << _msg << " >>>" << endl;
    close(_fd1);
    exit(_exitCode);
}
