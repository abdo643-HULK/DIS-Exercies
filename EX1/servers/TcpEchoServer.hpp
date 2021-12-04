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

//union IpAddress {
//    sockaddr_in *addressV4;
//    sockaddr_in6 *addressV6;
//};

//struct SocketData {
//    int port;
//    sockaddr_storage *const address;
////    union {
////        sockaddr_in *addressV4;
////        sockaddr_in6 *addressV6;
////    };
//};

constexpr int BUFFER_SIZE = 1024;

class TcpEchoServer {
private:
    int mServerFd;
    const IpAddrKind mIpVersion;

    sockaddr *setIp(int _port, sockaddr_storage *const _address);

    bool requestHandler(int _clientFd) const;

public:
    explicit TcpEchoServer(IpAddrKind _addressKind);

    void initializeSocket(int _port, int _optval = 1);

    void startRequestHandler();

    ~TcpEchoServer();
};

#endif //EX1_TCPECHOSERVER_HPP
