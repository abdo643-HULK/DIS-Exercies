//
// Created by abous on 02/12/2021.
//

#include "TcpEchoServer.hpp"

using namespace std;

void printClientInfo(const sockaddr_in &_address);

void errorExit(const char *_msg, int _fd1, int _exitCode = -1);

void printError(const char *_error);


TcpEchoServer::TcpEchoServer() : mServerFd(-1) {};

sockaddr *TcpEchoServer::setIp(const SocketData *const _data) {
    const int port = _data->port;
    if (_data->ipVersion == IpAddrKind::V6) {
        memset(_data->addressV6, 0, sizeof(sockaddr_in6));
        _data->addressV6->sin6_family = AF_INET6;
        _data->addressV6->sin6_port = htons(port);
#ifdef IN6ADDR_ANY_INIT
        _data->addressV6->sin6_addr = IN6ADDR_ANY_INIT;
#else
        _data->addressV6->sin6_addr = IN6ADDR_ANY;
#endif
        return reinterpret_cast<sockaddr *>(_data->addressV6);
    }

    memset(_data->addressV4, 0, sizeof(sockaddr_in));
    _data->addressV4->sin_family = AF_INET;
    _data->addressV4->sin_port = htons(port);
    _data->addressV4->sin_addr.s_addr = htonl(INADDR_ANY);

    return reinterpret_cast<sockaddr *>(_data->addressV4);
}

void TcpEchoServer::initializeSocket(const int _port, const IpAddrKind _ipAddrKind, const int _optval) {
    mServerFd = socket(static_cast<int>(_ipAddrKind), SOCK_STREAM, 0);

    if (mServerFd < 0) {
        errorExit("SOCKET ERROR", SOCKET_ERROR);
    }

    if (setsockopt(mServerFd, SOL_SOCKET, SO_REUSEADDR, (char *) &_optval, sizeof(_optval)) < 0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, mServerFd);
    }

    sockaddr_in serverAddress;
    sockaddr_in6 serverAddressV6;

    SocketData socketData;
    socketData.port = _port;
    if (_ipAddrKind == IpAddrKind::V4) {
        socketData.ipVersion = IpAddrKind::V4;
        socketData.addressV4 = &serverAddress;
    } else {
        socketData.ipVersion = IpAddrKind::V6;
        socketData.addressV6 = &serverAddressV6;
    }

    const sockaddr *server = setIp(&socketData);
    const int serverSize = _ipAddrKind == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(mServerFd, server, serverSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, mServerFd);
    }

    if (listen(mServerFd, 10) < 0) {
        errorExit("SOCKET LISTEN ERROR", SOCKET_LISTEN_ERROR, mServerFd);
    }

    cout << "Listening..." << endl;
}

void TcpEchoServer::startRequestHandler() const {
    sockaddr_in clientAddress;
    socklen_t size = sizeof(sockaddr_in);
    char msg[BUFFER_SIZE];
    bool active = true;

    char echoMsg[BUFFER_SIZE] = "ECHO: ";

    while (active) {
        const int clientFd = accept(mServerFd, (sockaddr *) &clientAddress, (socklen_t *) &size);
        printClientInfo(clientAddress);

        while (active) {
            const auto status = recv(clientFd, msg, BUFFER_SIZE, 0);

            if (status == -1) {
                printError("NO MSG RECEIVED");
                break;
            } else if (status == 0) {
                cout << "CLIENT CLOSED" << endl;
                break;
            }

            cout << "NEW MESSAGE: " << msg << endl;

            if (strcmp(msg, "shutdown") == 0) active = false;

            strcat((char *) &echoMsg, (char *) &msg);

            send(clientFd, echoMsg, strlen(echoMsg), 0);

            memset(&msg, 0, BUFFER_SIZE);
            memset(&echoMsg, 0, BUFFER_SIZE);
        }

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

void printClientInfo(const sockaddr_in &_address) {
    cout << "Client IP-Address: " << inet_ntoa(_address.sin_addr) << endl;
    cout << "Client Port: " << ntohs(_address.sin_port) << endl;
    cout << endl;
}

void errorExit(const char *const _msg, const int _fd1, const int _exitCode) {
    cerr << endl;
    cerr << "<<< " << _msg << " >>>" << endl;
    close(_fd1);
    exit(_exitCode);
}
