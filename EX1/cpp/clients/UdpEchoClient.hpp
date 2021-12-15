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

class UdpEchoClient {
    /**
     * contains the IP address type.
     */
    const IpAddrKind mIpVersion;
    /**
     * contains the client file descriptor.
     */
    int mClientFd{};

private:


public:
    /**
     * trys to connect to the server.
     * @param _args contains IP address
     * @return the server address
     */
    sockaddr_storage setupConnection(const Args *_args);


    /**
     * constructor for the UdpEchoClient class.
     * @param _addressType contains the IP address type
     */
    explicit UdpEchoClient(IpAddrKind _addressType);

    /**
     * sends messages to the server.
     * @param _serverAddress contains the server address
     */
    void startRequest(sockaddr_storage *_serverAddress) const;

    /**
     * destructor to close client field descriptor.
     */
    ~UdpEchoClient();
};


#endif //EX1_UDPECHOCLIENT_HPP
