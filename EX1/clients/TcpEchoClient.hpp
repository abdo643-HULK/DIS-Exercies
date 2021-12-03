//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPECHOCLIENT_HPP
#define EX1_TCPECHOCLIENT_HPP

#include <iostream>
#include <cstring>
#include "shared.hpp"

#ifdef _WIN32

#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>

#pragma comment(lib, "Ws2_32.lib")

#elif __unix__

#include <sys/socket.h>
#include <netinet/in.h>

#include <unistd.h>
#include <arpa/inet.h>

#endif


class TcpEchoClient {
    int mClientFd;
public:
    int setupConnection(const Args *const _args);
};


#endif //EX1_TCPECHOCLIENT_HPP
