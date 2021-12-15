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

/**
 * A struct for to represent the available sensors
 */
struct Sensor {
    const char *const type;
    u8 amount;
};

/**
 * All available sensors and the amount of values they provide
 */
constexpr Sensor SENSORS[3] = {
        {"air",   1},
        {"light", 1},
        {"noise", 3}
};

/**
 * The Server allows to communications over TCP with
 * either IPv4 or IPv6.
 * The Server also supports multithreading and provides
 * the client with Information about the Sensors
 */
class TcpEnviEchoServer {
    /**
     * The struct is only here to provide the
     * thread with the needed parameters
     */
    struct ClientCommunicationParams {
        int clientFd;
    };

    /**
     * Holds the Server file descriptor from the socket function
     */
    int mServerFd;

    /**
     * Holds the created Threads
     */
    pthread_t mThreadPool[THREAD_COUNT];

    /**
     * The selected Ip-Version
     */
    const IpAddrKind mIpVersion;

    /**
     *
     *
     * @param _port
     * @param _address
     * @return
     */
    sockaddr *setIp(int _port, sockaddr_storage *_address);

    static void *clientCommunication(void *_parameter);

public:
    explicit TcpEnviEchoServer();

    void initializeSocket(int _port, int _optval = 1);

    [[noreturn]] void startRequestHandler();

    ~TcpEnviEchoServer();
};

std::string createSensorData(const std::string &_sensorType, int _dataCount);

#endif //EX1_TCPENVIECHOSERVER_HPP
