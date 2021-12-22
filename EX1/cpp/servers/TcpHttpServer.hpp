//
// Created by abous on 02/12/2021.
//

#ifndef EX1_TCPHTTPSERVER_HPP
#define EX1_TCPHTTPSERVER_HPP

#include <cstring>
#include "shared.hpp"
#include "../errors.hpp"

constexpr auto HTTP_HEADER = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=utf-8\n";

constexpr auto HTML_START = "<!DOCTYPE html>\n"
                            "<html lang=\"en\">\n"
                            "\t<head>\n"
                            "\t\t<meta charset=\"utf-8\" />\n"
                            "\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\" />\n"
                            "\t\t<title>BROWSER REQUEST</title>\n"
                            "\t</head>\n"
                            "\t<body>\n";

constexpr auto HTML_END = "\t</body>\n"
                          "</html>";

/**
 * This HTTP-Server supports either IPv4 or IPv6.
 * The Server also supports multithreading and returns
 * the client header
 */
class TcpHttpServer {
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
        TcpHttpServer *server;

        int counter;
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
    * Toggles the loop to shut down
    */
    bool mShutdown;

    /**
     * Holds the created Threads
     */
    pthread_t mThreadPool[THREAD_COUNT];

    /**
     * Creates the Sockaddr of the selected Ip Version
     *
     * @param _port the port the struct has to include
     * @param _address the address to convert
     * @return the struct of the selected version cast as an `sockaddr`
     */
    sockaddr *setIp(int _port, sockaddr_storage *_address);

    /**
     * Binds the socket and starts listening
     *
     * @param _port the port to bind to and run the server on
     * @param _optval the `optval` for `setsockopt`
     */
    int initializeSocket(int _port, int _optval = 1);

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
    explicit TcpHttpServer(IpAddrKind _addressKind, int _port, int _optval = 1);

    /**
     * Accepts the client and puts them into a thread.
     * The request loop uses the `mShutdown` to kill it or
     * keep it alive
     */
    void startRequestHandler();

    /**
     * closes the serverFd and the threads
     */
    ~TcpHttpServer();
};


#endif //EX1_TCPHTTPSERVER_HPP
