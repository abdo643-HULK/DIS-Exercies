//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPECHOSERVER_HPP
#define EX1_TCPECHOSERVER_HPP

#include <iostream>
#include <cstring>
#include "../errors.hpp"


#ifdef _WIN32

#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
#include <windows.h>

#elif __unix__

#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>

#include <unistd.h>
#include <arpa/inet.h>
#include <semaphore.h>

#endif

enum class IpAddrKind : int {
    V4 = AF_INET,
    V6 = AF_INET6
};

struct SocketData {
    int port;
    IpAddrKind ipVersion;
    union {
        sockaddr_in *addressV4;
        sockaddr_in6 *addressV6;
    };
};

constexpr int BUFFER_SIZE = 1024;

class TcpEchoServer {
private:
    int mServerFd;

    static sockaddr *setIp(const SocketData* _address);

public:
    TcpEchoServer();

    void initializeSocket(int _port, IpAddrKind _ipAddrKind = IpAddrKind::V4, int _optval = 1);

    void startRequestHandler() const;

    ~TcpEchoServer();
};

#endif //EX1_TCPECHOSERVER_HPP
