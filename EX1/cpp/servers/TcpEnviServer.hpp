//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPENVISERVER_HPP
#define EX1_TCPENVISERVER_HPP

#include <string>
#include <sstream>
#include <cstring>
#include <chrono>
#include <unordered_map>
#include <functional>
#include <random>
#include "shared.hpp"
#include "../errors.hpp"

/**
 * callback type for the routes
 */
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
class TcpEnviServer {
    /**
     * The struct is only here to provide the
     * thread with the needed parameters
     */
    struct ClientCommunicationParams {
        /**
         * Holds the Client file descriptor from the accept function
         * to be able to send and receive messages
         */
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
     * Creates the Sockaddr of the selected Ip Version
     *
     * @param _port the port the struct has to include
     * @param _address the address to convert
     * @return the struct of the selected version cast as an `sockaddr`
     */
    sockaddr *setIp(int _port, sockaddr_storage *_address);

    /**
     * the function for the thread to handle the client request
     *
     * @param _parameter the parameter the function expects to work
     * @return nothing
     */
    static void *clientCommunication(void *_parameter);

public:
    /**
     * Initializes all the member properties
     */
    explicit TcpEnviServer();

    /**
     * Binds the socket and starts listening
     *
     * @param _port the port to bind to and run the server on
     * @param _optval the `optval` for `setsockopt`
     */
    void initializeSocket(int _port, int _optval = 1);

    /**
     * Accepts the client and puts them into a thread
     */
    [[noreturn]] void startRequestHandler();

    /**
     * closes the serverFd and the threads
     */
    ~TcpEnviServer();
};

/**
 * helper function to create random data for each sensor type
 *
 * @param _sensorType the string that represents the sensor type
 * @param _dataCount the amount of random values to create
 * @return
 */
std::string createSensorData(const std::string &_sensorType, int _dataCount);

#endif //EX1_TCPENVISERVER_HPP
