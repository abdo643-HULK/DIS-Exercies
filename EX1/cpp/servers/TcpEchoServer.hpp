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

/**
 * The Server allows to communications over TCP with
 * either IPv4 or IPv6.
 * The Server also supports multithreading and echos
 * the client message
 */
class TcpEchoServer {
private:
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

        /**
         * A reference to the server to call shutdown
         */
        TcpEchoServer *server;
    };

    /**
     * The selected Ip-Version
     */
    const IpAddrKind mIpVersion;

    /**
     * Holds the Server file descriptor from the socket function
     */
    int mServerFd;

    /**
     * Holds the created Threads
     */
    pthread_t mThreadPool[THREAD_COUNT];

    /**
     * Toggles the loop to shut down
     */
    bool mShutdown;

    /**
     * Creates the Sockaddr of the selected Ip Version
     *
     * @param _port the port the struct has to include
     * @param _address the address to convert
     * @return the struct of the selected version cast as an `sockaddr`
     */
    sockaddr *setIp(int _port, sockaddr_storage *_address);

    /**
     *
     * @param _parameter
     * @return
     */
    static void *clientCommunication(void *_parameter);

    /**
     * Shuts down the server descriptor and
     * toggles mShutdown
     */
    void shutdownServer();

public:
    /**
     * Sets the IP-Version and initializes all the member properties
     */
    explicit TcpEchoServer(IpAddrKind _addressKind);

    /**
     * Binds the socket and starts listening
     *
     * @param _port the port to bind to and run the server on
     * @param _optval the `optval` for `setsockopt`
     */
    void initializeSocket(int _port, int _optval = 1);

    /**
     * Accepts the client and puts them into a thread.
     * The request loop uses the `mShutdown` to kill it or
     * keep it alive
     */
    void startRequestHandler();

    /**
     * closes the serverFd and the threads
     */
    ~TcpEchoServer();
};

#endif //EX1_TCPECHOSERVER_HPP
