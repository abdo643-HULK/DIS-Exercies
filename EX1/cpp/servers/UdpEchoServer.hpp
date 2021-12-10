//
// Created by abous on 02/12/2021.
//

#ifndef EX1_UDPECHOSERVER_HPP
#define EX1_UDPECHOSERVER_HPP

#include <iostream>
#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

class UdpEchoServer {
private:
    int mServerFd;
    const IpAddrKind mIpVersion;

    sockaddr *setIp(int _port, sockaddr_storage *_address);
public:
    explicit UdpEchoServer(IpAddrKind _addressKind);

    void initializeSocket(int _port, int _optval = 1);

    void startRequestHandler();

    ~UdpEchoServer();
};


#endif //EX1_UDPECHOSERVER_HPP
