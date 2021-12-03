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

using namespace std;

void errorExit(const char *_msg, int _exitCode);

#endif //EX1_SERVER_HPP
