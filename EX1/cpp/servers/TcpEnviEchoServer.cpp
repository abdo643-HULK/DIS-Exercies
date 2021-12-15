//
// Created by abous on 02/12/2021.
//

#include "TcpEnviEchoServer.hpp"

using namespace std;

const unordered_map<string, routerCb> ROUTER{
        {"getSensortypes()#", [](const string &_req) {
            string data;
            for (const auto i: SENSORS) {
                data += i.type;
                data += ';';
            }
            return data += '#';
        }},
        {"getAllSensors()#",  [](const string &_req) {
            std::random_device dev;
            std::mt19937 rng(dev());
            std::uniform_int_distribution<u32> value(1, 100);

            string data;
            data.reserve(BUFFER_SIZE);

            const auto timestamp = chrono::seconds(time(nullptr)).count();
            data += to_string(timestamp) + "|";

            constexpr auto dataDelimiter = '|';
            constexpr auto keyValDelimiter = ';';

            for (auto i : SENSORS) {
                data.append(i.type);

                for (int j = 0; j < i.amount; ++j) {
                    data += keyValDelimiter + to_string(value(rng));
                }

                data += dataDelimiter;
            }

            return data += '#';
        }},
        {"getSensor(light)#", [](const string &_req) {
            return createSensorData(_req, 1) += '#';
        }},
        {"getSensor(noise)#", [](const string &_req) {
            return createSensorData(_req, 1) += '#';
        }},
        {"getSensor(air)#",   [](const string &_req) {
            return createSensorData(_req, 3) += '#';
        }},
};

string createSensorData(const string &_sensorType, const int _dataCount) {
    std::random_device dev;
    std::mt19937 rng(dev());
    std::uniform_int_distribution<u32> value(1, 100);

    string data;
    data.reserve(BUFFER_SIZE);

    const auto timestamp = chrono::seconds(time(nullptr)).count();
    data += to_string(timestamp) + "|";

    for (int i = 0; i < _dataCount; ++i) {
        data += to_string(value(rng)) + ';';
    }

    return data;
}

TcpEnviEchoServer::TcpEnviEchoServer() : mServerFd(-1),
                                         mIpVersion(IpAddrKind::V4),
                                         mShutdown(false) {
    memset(mThreadPool, -1, sizeof(mThreadPool));
}

sockaddr *TcpEnviEchoServer::setIp(const int _port, sockaddr_storage *const _data) {
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

void TcpEnviEchoServer::initializeSocket(const int _port, const int _optval) {
    mServerFd = socket(static_cast<int>(mIpVersion), SOCK_STREAM, 0);

    if (mServerFd < 0) {
        errorExit("SOCKET ERROR", SOCKET_ERROR);
    }

    sockaddr_storage serverAddress;

    const sockaddr *server = setIp(_port, &serverAddress);
    const int addressSize = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    if (bind(mServerFd, server, addressSize) < 0) {
        errorExit("SOCKET BINDING ERROR", SOCKET_BIND_ERROR, mServerFd);
    }

    if (setsockopt(mServerFd,
                   SOL_SOCKET,
                   SO_REUSEADDR,
                   reinterpret_cast<const char *>(&_optval),
                   sizeof(_optval)) < 0) {
        errorExit("SETSOCKOPT ERROR", SOCKET_OPT_ERROR, mServerFd);
    }

    if (listen(mServerFd, 10) < 0) {
        errorExit("SOCKET LISTEN ERROR", SOCKET_LISTEN_ERROR, mServerFd);
    }

    cout << "Listening..." << endl;
}

void *TcpEnviEchoServer::clientCommunication(void *const _parameter) {
    const auto params = (ClientCommunicationParams *) _parameter;
    const auto clientFd = params->clientFd;
    const auto server = params->server;

    const auto detachRet = pthread_detach(pthread_self());
    if (detachRet != 0) {
        errorExit("ERROR DETACHING THREADS", THREAD_ERROR, clientFd);
    }

    char route[BUFFER_SIZE] = "\0";

    while (true) {
        const auto status = recv(clientFd, route, BUFFER_SIZE, 0);

        if (status == -1) {
            errorExit("NO MSG RECEIVED", NO_MSG_ERROR, clientFd);
        } else if (status == 0) {
            cout << "CLIENT CLOSED" << endl;
            break;
        }

        route[strcspn(route, "\r\n")] = 0;

        const auto cb = ROUTER.find(route);
        const auto data = cb != ROUTER.end() ? (cb->second(route) += "\n") : "NOT FOUND\n";

        const auto sendRet = send(clientFd, data.c_str(), data.length(), 0);
        if (sendRet == -1) errorExit("ERROR SENDING DATA", THREAD_ERROR, clientFd);

        memset(&route, 0, BUFFER_SIZE);
    }

    cout << "CLOSING CLIENT" << endl;
    shutdown(clientFd, SHUT_RDWR);
    close(clientFd);

    delete params;

    return nullptr;
}

[[noreturn]] void TcpEnviEchoServer::startRequestHandler() {
    sockaddr_storage clientAddress;
    memset(&clientAddress, 0, sizeof(sockaddr_storage));

    const socklen_t size = mIpVersion == IpAddrKind::V4 ? sizeof(sockaddr_in) : sizeof(sockaddr_in6);

    int i = 0;

    while (true) {
        const int clientFd = accept(mServerFd,
                                    reinterpret_cast<sockaddr *>(&clientAddress),
                                    const_cast<socklen_t *>(&size));

        if (clientFd == -1) {
            printError("ERROR ACCEPTING CLIENT");
            continue;
        };

        const bool ret = printClientInfo(mIpVersion, &clientAddress);
        if (!ret) continue;

        auto parameter = new ClientCommunicationParams();
        parameter->clientFd = clientFd;
        parameter->server = this;

        if (pthread_create(&mThreadPool[i++],
                           nullptr,
                           clientCommunication,
                           parameter) != 0) {
            printError("ERROR CREATING THREAD");
        }
    }
}

TcpEnviEchoServer::~TcpEnviEchoServer() {
    cout << "SERVER SHUTTING DOWN" << endl;
    for (unsigned long i: mThreadPool) {
        i != -1 && pthread_cancel(i);
    }
    shutdown(mServerFd, SHUT_RDWR);
    close(mServerFd);
}