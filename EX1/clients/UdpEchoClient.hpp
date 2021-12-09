//
// Created by abous on 02/12/2021.
//

#ifndef EX1_UDPECHOCLIENT_HPP
#define EX1_UDPECHOCLIENT_HPP

#include <iostream>
#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

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

/*enum class IpAddrKind : int {
    V4 = AF_INET,
    V6 = AF_INET6
};*/

class UdpEchoClient {
    const IpAddrKind mIpVersion;
    int mClientFd{};

private:


public:
    sockaddr_storage setupConnection(const Args *_args);

    explicit UdpEchoClient(IpAddrKind _addressType);

    void startRequest(sockaddr_storage *_serverAddress) const;

    ~UdpEchoClient();
};


#endif //EX1_UDPECHOCLIENT_HPP
