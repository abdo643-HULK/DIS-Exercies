//
// Created by abous on 04/12/2021.
//

#ifndef EX1_TCPENVIECHOCLIENT_HPP
#define EX1_TCPENVIECHOCLIENT_HPP

#include <string>
#include <vector>
#include <sstream>
#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"


/**
 * The TcpEnviEchoClient class trys to connect to the server
 * and trys to communicate with the server.
 */
class TcpEnviEchoClient {
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
     * constructor for TcpEnviEchoClient.
     * @param _args contains IP address
     * @param _addressType contains the IP address type
     */
    TcpEnviEchoClient(const Args *_args, IpAddrKind _addressType);

    /**
     * sends messages to the server.
     */
    void startRequest() const;

    /**
	 * sends a request for all available sensor types and prints the received data.
	 */
    void getSensortypes() const;

    /**
     * sends a request for a concret sensor values and prints the received data.
     *
     * @param _sensortype concrete type of sensor
     */
    void getSensor(const std::string &_sensortype) const;

    /**
	 * sends a request for all available sensor values and prints the received data.
	 */
    void getAllSensors() const;

    /**
     * destructor to close client field descriptor.
     */
    ~TcpEnviEchoClient();
};


#endif //EX1_TCPENVIECHOCLIENT_HPP
