#include "server.hpp"
//#include <limits>
//#include <cstddef>

using namespace std;

/**
 * Asks the user which server they want to use and starts it.
 *
 * @param _argc the size of `_args` passed
 * @param _argv the arguments passed to the program
 * @return
 */
int main(int _argc, char *_argv[]) {
    if (_argc < 2) {
        errorExit("Please provide a port as the first argument", PORT_ERROR);
    }

    const int port = atoi(_argv[1]);

    if (port < 1024 || port > 65353) {
        errorExit("Port not usable (can only be 1024-65353)", PORT_ERROR);
    }

    int inputServerType = 0;
    do {
        cout << "C++ Server Menu:" << endl;
        cout << "-----------------------------" << endl;
        cout << "1. Start TCP v4 Echo Server" << endl;
        cout << "2. Start TCP v6 Echo Server" << endl;
        cout << "3. Start UDP    Echo Server" << endl;
        cout << "4. Start TCP    HTTP Server" << endl;
        cout << "5. Start TCP    Envi Server" << endl;
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

    // being explicit about the cast instead of "(ServerType) inputServerType"
    // which the compiler sees as reinterpret_cast and tries to figure out how
    // to cast it
    switch (static_cast<ServerType>(inputServerType)) {
        case ServerType::TcpV4Echo: {
            TcpEchoServer server(IpAddrKind::V4);
            server.initializeSocket(port);
            server.startRequestHandler();
            break;
        }
        case ServerType::TcpV6Echo: {
            TcpEchoServer server(IpAddrKind::V6);
            server.initializeSocket(port);
            server.startRequestHandler();
            break;
        }
        case ServerType::UdpEcho: {
            UdpEchoServer server(IpAddrKind::V4);
            server.initializeSocket(port);
            server.startRequestHandler();
            break;
        }
        case ServerType::TcpHttp: {
            TcpHttpServer server(IpAddrKind::V4, port);
            server.startRequestHandler();
            cout << "Not implemented yet" << endl;
            break;
        }
        case ServerType::TcpEnvi: {
            TcpEnviServer server;
            server.initializeSocket(port);
            server.startRequestHandler();
            break;
        }
        case ServerType::Exit:
            break;
    }

    return 0;
}
//
//void errorExit(const char *const _msg, const int _exitCode) {
//    cerr << "\n" << "<<< " << _msg << " >>>" << endl;
//    exit(_exitCode);
//}