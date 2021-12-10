//
// Created by abous on 01/12/2021.
//

#ifndef EX1_SERVER_HPP
#define EX1_SERVER_HPP

#include <iostream>
#include <cstring>
#include "errors.hpp"
#include "servers/TcpEchoServer.hpp"
#include "servers/TcpHttpEchoServer.hpp"
#include "servers/TcpEnviEchoServer.hpp"
#include "servers/UdpEchoServer.hpp"

//void errorExit(const char *_msg, int _exitCode);

// Subsequent enumerators, if they are not given an explicit value,
// receive the value of the previous enumerator plus one.
enum class ServerType: int {
    TcpV4Echo = 1,
    TcpV6Echo,
    UdpEcho,
    TcpHttp,
    TcpEnvi,
    Exit,
};

#endif //EX1_SERVER_HPP
