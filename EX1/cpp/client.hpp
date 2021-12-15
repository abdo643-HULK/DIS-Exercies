//
// Created by abous on 01/12/2021.
//

#ifndef EX1_CLIENT_HPP
#define EX1_CLIENT_HPP

#include <iostream>
#include <cstring>
#include <limits>
#include "clients/TcpEchoClient.hpp"
#include "clients/TcpHttpClient.hpp"
#include "clients/TcpEnviClient.hpp"
#include "clients/UdpEchoClient.hpp"

#ifdef _WIN32

#pragma comment(lib, "Ws2_32.lib")

#endif


#include "errors.hpp"

/**
 * Parses the args to get the Port and Ip-Address of the Server
 *
 * @param _argc the size of `_args` passed
 * @param _args the arguments passed to the program
 * @return The parsed port and address in a struct
 */
Args parseArgs(int _argc, const char *const _args[]);

/**
 * The supported Clients types to select from
 */
enum class ClientType: int {
    TcpV4Echo = 1,
    TcpV6Echo,
    UdpEcho,
    TcpHttp,
    TcpEnvi,
    Exit,
};


#endif //EX1_CLIENT_HPP
