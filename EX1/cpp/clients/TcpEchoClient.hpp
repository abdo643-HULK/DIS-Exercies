//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPECHOCLIENT_HPP
#define EX1_TCPECHOCLIENT_HPP

#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

/**
 * The TcpEchoClient class trys to connect to the server
 * and trys to communicate with the server.
 */
class TcpEchoClient {
    /**
     * contains the IP address type.
     */
    const IpAddrKind mIpVersion;
    /**
     * contains the client file descriptor.
     */
    const int mClientFd;

private:
    /**
     * trys to connect to the server.
     * @param _args contains IP address
     * @return the client file descriptor
     */
    int setupConnection(const Args *_args);

public:
    /**
     * constructor for TcpEchoclient.
     * @param _args contains IP address
     * @param _addressType contains the IP address type
     */
    TcpEchoClient(const Args *_args, IpAddrKind _addressType);

    /**
     * sends messages to the server.
     */
    void startRequest() const;

    /**
     * TcpEchoClient destructor to close client field descriptor.
     */
    ~TcpEchoClient();
};


#endif //EX1_TCPECHOCLIENT_HPP
