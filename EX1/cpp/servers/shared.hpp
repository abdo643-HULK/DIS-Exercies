//
// Created by abous on 09/12/2021.
//

#ifndef EX1_SHARED_HPP
#define EX1_SHARED_HPP

#include <iostream>
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

constexpr auto THREAD_COUNT = 10;

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

bool printClientInfo(IpAddrKind _ip, const sockaddr_storage *_address);

void errorExit(const char *_msg, int _exitCode = DEFAULT_ERROR, int _fd1 = -1);

void printError(const char *_error);

#endif //EX1_SHARED_HPP
