//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPECHOCLIENT_HPP
#define EX1_TCPECHOCLIENT_HPP

#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

class TcpEchoClient {
    const IpAddrKind mIpVersion;
    const int mClientFd;

private:
    int setupConnection(const Args *_args);

public:
    TcpEchoClient(const Args *_args, IpAddrKind _addressType);

    void startRequest() const;

    ~TcpEchoClient();
};


#endif //EX1_TCPECHOCLIENT_HPP
