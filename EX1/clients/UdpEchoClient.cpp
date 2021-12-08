//
// Created by abous on 02/12/2021.
//

#include "UdpEchoClient.hpp"

using namespace std;

UdpEchoClient::UdpEchoClient(const IpAddrKind _addressType) :
        mIpVersion(_addressType){};

sockaddr_storage UdpEchoClient::setupConnection(const Args *const _args) {
    const auto ip = _args->ipAddress;
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    int ret;
    sockaddr_storage serverAddress;
    if (mIpVersion == IpAddrKind::V4) {
        auto *const addr = reinterpret_cast<sockaddr_in *const>(&serverAddress);
        memset(&(serverAddress), '\0', addressSize);
        addr->sin_family = AF_INET;
        addr->sin_port = htons(_args->port);
        ret = inet_pton(static_cast<int>(mIpVersion), ip, &addr->sin_addr);
    } else {
        auto *const addr = reinterpret_cast<sockaddr_in6 *const>(&serverAddress);
        memset(&(serverAddress), '\0', addressSize);
        addr->sin6_family = AF_INET6;
        addr->sin6_port = htons(_args->port);
        ret = inet_pton(static_cast<int>(mIpVersion), ip, &addr->sin6_addr);
    }

    if (ret <= 0) {
        errorExit("INVALID IP-ADDRESS", PORT_ERROR);
    }

    const int clientFd = socket(static_cast<int>(mIpVersion), SOCK_DGRAM, 0);

    if (clientFd < 0) {
        errorExit("ERROR GETTING SOCKET_FD", SOCKET_ERROR, clientFd);
    }

    return serverAddress;
}


void UdpEchoClient::startRequest(sockaddr_storage *_serverAddress) const {
    char msg[BUFFER_SIZE] = "\n";

    const socklen_t size = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    while (true) {
        cout << "Enter the data: ";
        cin.getline(msg, BUFFER_SIZE);
        cout << endl;

        if (strncmp(msg, QUIT, strlen(QUIT)) == 0) break;

        sendto(mClientFd, &msg, strlen(msg), 0, reinterpret_cast<sockaddr *>(_serverAddress), size);

        if (strncmp(msg, SHUTDOWN, strlen(SHUTDOWN)) == 0) break;

        const auto status = recvfrom(mClientFd, &msg, strlen(msg), 0, reinterpret_cast<sockaddr *>(_serverAddress),
                                     const_cast<socklen_t *>(&size));

        if (status == -1) {
            errorExit("NO MSG RECEIVED", NO_MSG_ERROR, mClientFd);
        } else if (status == 0) {
            errorExit("SERVER CLOSED", SERVER_ERROR, mClientFd);
        }

        cout << msg << endl;
        memset(msg, 0, BUFFER_SIZE);
    }
}

UdpEchoClient::~UdpEchoClient() {
    close(mClientFd);
}