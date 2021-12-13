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



class TcpEnviEchoClient {
    const IpAddrKind mIpVersion;
    const int mClientFd;

private:
    int setupConnection(const Args *_args);

public:
    TcpEnviEchoClient(const Args *_args, IpAddrKind _addressType);

    void startRequest() const;

    void getSensortypes() const;

    void getSensor(const std::string &_sensortype) const;

    void getAllSensors() const;

    ~TcpEnviEchoClient();
};


#endif //EX1_TCPENVIECHOCLIENT_HPP
