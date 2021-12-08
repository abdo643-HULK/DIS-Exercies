//
// Created by abous on 02/12/2021.
//

#ifndef EX1_UDPECHOSERVER_HPP
#define EX1_UDPECHOSERVER_HPP

#include <iostream>
#include <cstring>
#include "../errors.hpp"
#include "TcpEchoServer.hpp"


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

/*enum class IpAddrKind : int {
    V4 = AF_INET,
    V6 = AF_INET6
};*/


//constexpr int BUFFER_SIZE = 1024;


class UdpEchoServer {
private:
    int mServerFd;
    const IpAddrKind mIpVersion;

    sockaddr *setIp(int _port, sockaddr_storage *const _address);

    bool requestHandler(int _clientFd) const;

public:
    explicit UdpEchoServer(IpAddrKind _addressKind);

    sockaddr_storage initializeSocket(int _port, int _optval = 1);

    void startRequestHandler(sockaddr_storage *_serverAddress);

    ~UdpEchoServer();
};


#endif //EX1_UDPECHOSERVER_HPP
