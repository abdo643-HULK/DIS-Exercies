//
// Created by abous on 01/12/2021.
//

#include "client.hpp"

using namespace std;

/**
 * Asks the user which client they want to use and starts it.
 *
 * @param _argc the size of `_args` passed
 * @param _argv the arguments passed to the program
 * @return
 */
int main(int _argc, char *_argv[]) {
    int inputClientType = 0;
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
        cin >> inputClientType; // >> std::skipws
        cin.clear();
        cin.ignore(); //std::numeric_limits<std::streamsize>::max()

        if (inputClientType < 1 || inputClientType > 6) {
            cout << endl;
            cout << "#####################################" << endl;
            cout << "Option " << inputClientType << " not found, please try again" << endl;
            cout << "#####################################" << endl;
            cout << endl;
        }

    } while (inputClientType < 1 || inputClientType > 6);

    const auto args = parseArgs(_argc, _argv);

    switch (static_cast<ClientType>(inputClientType)) {
        case ClientType::TcpV4Echo: {
            TcpEchoClient client(&args, IpAddrKind::V4);
            client.startRequest();
            break;
        }
        case ClientType::TcpV6Echo: {
            TcpEchoClient client(&args, IpAddrKind::V6);
            client.startRequest();
            break;
        }
        case ClientType::UdpEcho: {
            UdpEchoClient client(IpAddrKind::V4);
            sockaddr_storage serverAddress = client.setupConnection(&args);
            client.startRequest(&serverAddress);
            break;
        }
        case ClientType::TcpHttp: {
//            TcpHttpClient client;
            cout << "Not implemented yet" << endl;
            break;
        }
        case ClientType::TcpEnvi: {
            TcpEnviClient client(&args, IpAddrKind::V4);
            client.startRequest();
            break;
        }
        case ClientType::Exit:
            break;
    }

    return 0;
}

Args parseArgs(const int _argc, const char *const _args[]) {
    if (_argc != 3) {
        errorExit("Please provide a port as the first argument and IP as the second", 2);
    }

    const u32 port = atoi(_args[1]);

    if (port < 1024 || port > 65353) {
        errorExit("Port not usable (can only be 1024-65353)", PORT_ERROR);
    }

    const auto ipAddress = _args[2];

    return Args{ipAddress, port};
}