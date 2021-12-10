//
// Created by abous on 01/12/2021.
//

#ifndef EX1_CLIENT_HPP
#define EX1_CLIENT_HPP

#include <iostream>
#include <cstring>
#include <limits>
#include "clients/TcpEchoClient.hpp"
#include "clients/TcpHttpEchoClient.hpp"
#include "clients/TcpEnviEchoClient.hpp"
#include "clients/UdpEchoClient.hpp"

#ifdef _WIN32

#pragma comment(lib, "Ws2_32.lib")

#endif


#include "errors.hpp"

int setupConnection(const Args *_args);
Args parseArgs(int _argc, const char *const _args[]);

enum class ClientType: int {
    TcpV4Echo = 1,
    TcpV6Echo,
    UdpEcho,
    TcpHttp,
    TcpEnvi,
    Exit,
};


#endif //EX1_CLIENT_HPP
