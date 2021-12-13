//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPENVIECHOSERVER_HPP
#define EX1_TCPENVIECHOSERVER_HPP

#include <string>
#include <sstream>
#include <cstring>
#include <chrono>
#include <unordered_map>
#include <functional>
#include <random>
#include "shared.hpp"
#include "../errors.hpp"

typedef std::function<std::string(const std::string &)> routerCb;

constexpr std::string_view SENSOR_TYPES[] = {"air", "light", "noise"};

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

public:
    explicit TcpEnviEchoServer();

    void initializeSocket(int _port, int _optval = 1);

    void startRequestHandler();

    ~TcpEnviEchoServer();
};

std::string createSensorData(const std::string &_sensorType, int _dataCount);

#endif //EX1_TCPENVIECHOSERVER_HPP