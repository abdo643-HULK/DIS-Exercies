//
// Created by abous on 01/12/2021.
//

#include "client.hpp"

using namespace std;

int main(int _argc, char *_argv[]) {
    const auto args = parseArgs(_argc, _argv);

    const int clientFd = setupConnection(&args);

    char serverAck[4];
    char msg[BUFFER_SIZE];

    int inputServerType = 0;
    do {
        cout << "C++ Client Menu:" << endl;
        cout << "-----------------------------" << endl;
        cout << "1. Start TCP v4 Echo Client" << endl;
        cout << "2. Start TCP v6 Echo Client" << endl;
        cout << "3. Start UDP    Echo Client" << endl;
        cout << "4. Start TCP    HTTP Client" << endl;
        cout << "5. Start TCP    Envi Client" << endl;
        cout << "6. EXIT" << endl;
        cout << "-----------------------------" << endl;
        cout << "Choose server type to start: ";
        cin >> inputServerType;

        if (inputServerType < 1 || inputServerType > 6) {
            cout << endl;
            cout << "#####################################" << endl;
            cout << "Option " << inputServerType << " not found, please try again" << endl;
            cout << "#####################################" << endl;
            cout << endl;
        }

    } while (inputServerType < 1 || inputServerType > 6);

    while (true) {
        cout << "Enter the data: ";
        cin.getline(msg, BUFFER_SIZE);

        if (strncmp(msg, QUIT, strlen(QUIT)) == 0) break;

        send(clientFd, msg, strlen(msg), 0);

        if (strncmp(msg, SHUTDOWN, strlen(SHUTDOWN)) == 0) break;

//        const auto status = recv(clientFd, serverAck, 4, 0);
        const auto status = recv(clientFd, msg, BUFFER_SIZE, 0);

        if (status == -1) {
            errorExit("NO MSG RECEIVED", NO_MSG_ERROR, clientFd);
        } else if (status == 0) {
            errorExit("SERVER CLOSED", SERVER_ERROR, clientFd);
        }

        cout << msg << endl;
//        if (strncmp(serverAck, "ACK", 4) != 0) errorExit("DIDN'T RECEIVE ACK FROM SERVER", SERVER_ERROR, clientFd);

        memset(msg, 0, BUFFER_SIZE);
        memset(serverAck, 0, 4);
    }

    close(clientFd);

    return 0;
}

int setupConnection(const Args *const _args) {
    sockaddr_in serverAddress;

    const int clientFd = socket(AF_INET, SOCK_STREAM, 0);

    if (clientFd < 0) {
        errorExit("ERROR GETTING SOCKET_FD", SOCKET_ERROR, clientFd);
    }

    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(_args->port);
    serverAddress.sin_addr = _args->ipAddress;
    memset(&(serverAddress.sin_zero), '\0', 8);

    const int connectionStatus = connect(clientFd, (sockaddr *) &serverAddress, sizeof(serverAddress));

    if (connectionStatus == -1) {
        errorExit("CONNECTION FAILED", SOCKET_ERROR, clientFd);
    }

    return clientFd;
}

Args parseArgs(const int _argc, const char *const _args[]) {
    if (_argc != 3) {
        errorExit("Please provide a port as the first argument and IP as the second", 2);
    }

    const u32 port = atoi(_args[1]);

    if (port < 1024 || port > 65353) {
        errorExit("Port not usable (can only be 1024-65353)", PORT_ERROR);
    }

    const auto ip = _args[2];
    in_addr ipAddress;
    const int ret = inet_aton(ip, &ipAddress);

    if (ret < 0) {
        errorExit("Invalid IP-Address", PORT_ERROR);
    }

    return Args{ipAddress, port};
}

void errorExit(const char *const _msg, int _exitCode, const int _fd) {
    cerr << "\n" << "<<< " << _msg << " >>>" << endl;
    _fd != -1 && close(_fd);
    exit(_exitCode);
}
