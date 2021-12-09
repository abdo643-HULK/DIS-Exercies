//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPECHOSERVER_HPP
#define EX1_TCPECHOSERVER_HPP

#include <iostream>
#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

//#include <thread>
//const auto PROCESSOR_COUNT = std::thread::hardware_concurrency();
constexpr auto THREAD_COUNT = 10;

class TcpEchoServer {
private:
    struct ClientCommunicationParams {
        int clientFd;
        TcpEchoServer *server;
    };

    int mServerFd;
    pthread_t mThreadPool[THREAD_COUNT];
    const IpAddrKind mIpVersion;
    bool mShutdown;

    sockaddr *setIp(int _port, sockaddr_storage *_address);

//    static bool requestHandler(int _clientFd);
    static void *clientCommunication(void *_parameter);

    void shutdownServer();

public:
    explicit TcpEchoServer(IpAddrKind _addressKind);

    void initializeSocket(int _port, int _optval = 1);

    void startRequestHandler();

    ~TcpEchoServer();
};

#endif //EX1_TCPECHOSERVER_HPP
