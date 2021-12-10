//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPENVIECHOSERVER_HPP
#define EX1_TCPENVIECHOSERVER_HPP

#include "shared.hpp"

class TcpEnviEchoServer {
    struct ClientCommunicationParams {
        int clientFd;
        TcpEnviEchoServer *server;
    };

    int mServerFd;
    pthread_t mThreadPool[THREAD_COUNT];
    const IpAddrKind mIpVersion;
    bool mShutdown;

    sockaddr *setIp(int _port, sockaddr_storage *_address);

    static void *clientCommunication(void *_parameter);

    void shutdownServer();

public:
    explicit TcpEnviEchoServer();

    void initializeSocket(int _port, int _optval = 1);

    void startRequestHandler();

    ~TcpEnviEchoServer();
};


#endif //EX1_TCPENVIECHOSERVER_HPP
