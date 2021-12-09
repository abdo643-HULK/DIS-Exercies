//
// Created by abous on 02/12/2021.
//

#include "TcpEchoClient.hpp"

using namespace std;

TcpEchoClient::TcpEchoClient(const Args *const _args, const IpAddrKind _addressType) :
        mIpVersion(_addressType),
        mClientFd(setupConnection(_args)) {}

int TcpEchoClient::setupConnection(const Args *const _args) {
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

    const int clientFd = socket(static_cast<int>(mIpVersion), SOCK_STREAM, 0);

    if (clientFd < 0) {
        errorExit("ERROR GETTING SOCKET_FD", SOCKET_ERROR, clientFd);
    }

    const int connectionStatus = connect(clientFd, (sockaddr *) &serverAddress, addressSize);

    if (connectionStatus == -1) {
        errorExit("CONNECTION FAILED", SOCKET_ERROR, clientFd);
    }

    return clientFd;
}


void TcpEchoClient::startRequest() const {
    char msg[BUFFER_SIZE] = "\n";

    while (true) {
        cout << "Enter the data: ";
        cin.getline(msg, BUFFER_SIZE);
        cout << endl;

        if (strncmp(msg, QUIT, strlen(QUIT)) == 0) break;

        send(mClientFd, msg, strlen(msg), 0);

        if (strncmp(msg, SHUTDOWN, strlen(SHUTDOWN)) == 0) break;

        const auto status = recv(mClientFd, msg, BUFFER_SIZE, 0);


        if (status == -1) {
            errorExit("NO MSG RECEIVED", NO_MSG_ERROR, mClientFd);
        } else if (status == 0) {
            errorExit("SERVER CLOSED", SERVER_ERROR, mClientFd);
        }

        cout << msg << endl;
        memset(msg, 0, BUFFER_SIZE);
    }
}

TcpEchoClient::~TcpEchoClient() {
    close(mClientFd);
}