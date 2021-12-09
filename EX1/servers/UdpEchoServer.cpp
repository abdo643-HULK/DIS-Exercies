//
// Created by abous on 02/12/2021.
//

#include "UdpEchoServer.hpp"

using namespace std;

UdpEchoServer::UdpEchoServer(const IpAddrKind _addressKind) : mServerFd(-1), mIpVersion(_addressKind) {};

sockaddr *UdpEchoServer::setIp(const int _port, sockaddr_storage *const _data) {
    if (mIpVersion == IpAddrKind::V6) {
        const auto addr = reinterpret_cast<sockaddr_in6 *const>(_data);
        memset(addr, 0, sizeof(sockaddr_in6));
        addr->sin6_family = AF_INET6;
        addr->sin6_port = htons(_port);
#ifdef IN6ADDR_ANY_INIT
        addr->sin6_addr = IN6ADDR_ANY_INIT;
#else
        addr->sin6_addr = IN6ADDR_ANY;
#endif
        return reinterpret_cast<sockaddr *>(addr);
    }

    const auto addr = reinterpret_cast<sockaddr_in *const>(_data);
    memset(addr, 0, sizeof(sockaddr_in));
    addr->sin_family = AF_INET;
    addr->sin_port = htons(_port);
    addr->sin_addr.s_addr = htonl(INADDR_ANY);

    return reinterpret_cast<sockaddr *>(addr);
}

void UdpEchoServer::initializeSocket(const int _port, const int _optval) {
    mServerFd = socket(static_cast<int>(mIpVersion), SOCK_DGRAM, 0);

    if (mServerFd < 0) {
        errorExit("SOCKET ERROR", SOCKET_ERROR);
    }

    sockaddr_storage serverAddress;

    const sockaddr *server = setIp(_port, &serverAddress);
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(mServerFd, server, addressSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, mServerFd);
    }

    if (setsockopt(
            mServerFd,
            SOL_SOCKET,
            SO_REUSEADDR,
            reinterpret_cast<const char *>(&_optval),
            sizeof(_optval)) < 0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, mServerFd);
    }
}

void UdpEchoServer::startRequestHandler() {
    sockaddr_storage clientAddress;
    memset(&clientAddress, 0, sizeof(sockaddr_storage));

    const auto msgSize = BUFFER_SIZE - 7;
    char echoMsg[BUFFER_SIZE] = "\0";
    char msg[msgSize] = "\0";

    const socklen_t size = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    while (true) {
        const auto status = recvfrom(mServerFd,
                                     &msg,
                                     msgSize,
                                     0,
                                     reinterpret_cast<sockaddr *>(&clientAddress),
                                     const_cast<socklen_t *>(&size));

        if (status == -1) return printError("NO MSG RECEIVED");

        if (!printClientInfo(mIpVersion, &clientAddress)) return;

        if (strcmp(msg, "shutdown") == 0) return;

        snprintf(echoMsg, sizeof(echoMsg), "%s%s", "ECHO: ", msg);

        const auto sendRet = sendto(mServerFd,
                                    &echoMsg,
                                    strlen(echoMsg),
                                    0,
                                    reinterpret_cast<sockaddr *>(&clientAddress),
                                    size);

        if (sendRet == -1) return printError("SEND ERROR");
        memset(&msg, 0, msgSize);
        memset(&echoMsg, 0, BUFFER_SIZE);
    }
}

UdpEchoServer::~UdpEchoServer() {
    cout << "SERVER SHUTTING DOWN" << endl;
    close(mServerFd);
}
