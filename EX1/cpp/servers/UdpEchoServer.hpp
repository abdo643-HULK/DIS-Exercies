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
    /**
     * The selected Ip-Version
     */
    const IpAddrKind mIpVersion;

    /**
     * Holds the Server file descriptor from the socket function
     */
    int mServerFd;

    /**
     * Creates the Sockaddr of the selected Ip Version
     *
     * @param _port the port the struct has to include
     * @param _address the address to convert
     * @return the struct of the selected version cast as an `sockaddr`
     */
    sockaddr *setIp(int _port, sockaddr_storage *_address);
public:
    /**
     * Sets the IP-Version and initializes all the member properties
     */
    explicit UdpEchoServer(IpAddrKind _addressKind);

    /**
     * Binds the socket and starts listening
     *
     * @param _port the port to bind to and run the server on
     * @param _optval the `optval` for `setsockopt`
     */
    void initializeSocket(int _port, int _optval = 1);

    /**
     * Accepts the client and returns the
     * incoming message with a "Echo: " added
     * at the beginning
     */
    void startRequestHandler();

    /**
     * closes the serverFd
     */
    ~UdpEchoServer();
};


#endif //EX1_UDPECHOSERVER_HPP
