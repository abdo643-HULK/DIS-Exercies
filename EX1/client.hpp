//
// Created by abous on 01/12/2021.
//

#ifndef EX1_CLIENT_HPP
#define EX1_CLIENT_HPP

#include <iostream>
#include <cstring>
#include "clients/TcpEchoClient.hpp"
#include "clients/UdpEchoClient.hpp"

#ifdef _WIN32

#pragma comment(lib, "Ws2_32.lib")

#endif


#include "errors.hpp"

int setupConnection(const Args *_args);

void errorExit(const char *_msg, int _exitCode = -1, int _fd = -1);

Args parseArgs(int _argc, const char *const _args[]);

constexpr auto BUFFER_SIZE = 1024;
constexpr auto SHUTDOWN = "shutdown";
constexpr auto QUIT = "quit";

#endif //EX1_CLIENT_HPP
