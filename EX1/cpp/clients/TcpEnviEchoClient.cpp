//
// Created by abous on 04/12/2021.
//

#include "TcpEnviEchoClient.hpp"

enum RequestType : int {
    GetSensorTypes = 1,
    GetSensorData,
    GetAllSensors,
    Exit,
};

using namespace std;

TcpEnviEchoClient::TcpEnviEchoClient(const Args *const _args, const IpAddrKind _addressType) :
        mIpVersion(_addressType),
        mClientFd(setupConnection(_args)) {}

int TcpEnviEchoClient::setupConnection(const Args *const _args) {
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

void TcpEnviEchoClient::startRequest() const {
    const auto max = 4;
    const auto min = 1;

    int inputClientType = 0;
    while (true) {
        do {
            cout << "Envi Client Menu:" << endl;
            cout << "-----------------------------" << endl;
            cout << "1. Get sensor types" << endl;
            cout << "2. Get sensor data" << endl;
            cout << "3. Get all Sensor " << endl;
            cout << "4. EXIT" << endl;
            cout << "-----------------------------" << endl;
            cout << "Choose server type to start: ";
            cin >> inputClientType; // >> std::skipws
            cin.clear();
            cin.ignore(); //std::numeric_limits<std::streamsize>::max()

            if (inputClientType < min || inputClientType > max) {
                cout << endl;
                cout << "#####################################" << endl;
                cout << "Option " << inputClientType << " not found, please try again" << endl;
                cout << "Input must be between 1 and 4 (inclusive)" << endl;
                cout << "#####################################" << endl;
                cout << endl;
            }

        } while (inputClientType < min || inputClientType > max);

        switch (static_cast<RequestType>(inputClientType)) {
            case GetSensorTypes: {
                getSensortypes();
                break;
            }
            case GetSensorData: {
                string sensortype;
                cout << "Please provide the sensor type: ";
                cin >> sensortype;
                getSensor(sensortype);
                break;
            }
            case GetAllSensors: {
                getAllSensors();
                break;
            }
            case Exit:
                return;
        }
    }
}


void TcpEnviEchoClient::getSensortypes() const {
    constexpr auto endpoint = "getSensortypes()#";

    const int sendStatus = send(mClientFd, endpoint, strlen(endpoint), 0);

    if (sendStatus == -1) {
        errorExit("ERROR FETCHING SENSOR TYPES", FETCH_ERROR, mClientFd);
    }

    string msg(BUFFER_SIZE, ' ');
    const auto status = recv(mClientFd, &msg.front(), msg.length(), 0);

    if (status == -1) {
        errorExit("NO MSG RECEIVED", NO_MSG_ERROR, mClientFd);
    } else if (status == 0) {
        errorExit("SERVER CLOSED", SERVER_ERROR, mClientFd);
    }

    if (msg == "NOT FOUND") {
        cout << "NOT FOUND" << endl;
        return;
    }

    vector<string> sensortypes;
    sensortypes.reserve(BUFFER_SIZE);

    size_t pos;
    const string delimiter = ";";
    while ((pos = msg.find(delimiter)) != string::npos) {
        sensortypes.push_back(msg.substr(0, pos));
        msg.erase(0, pos + delimiter.length());
    }

    for (const auto &str: sensortypes) {
        cout << "Sensor type: " << str << endl;
    }

    cout << endl;
}

/**
 * Gets the respective data of the sensortype from the server and displays it
 *
 * @param _sensortype a string that is either "air", "light" or "noise"
 */
void TcpEnviEchoClient::getSensor(const std::string &_sensortype) const {
    const string endpoint = "getSensor(" + _sensortype + ")#";
    const int sendStatus = send(mClientFd, endpoint.c_str(), endpoint.length(), 0);

    if (sendStatus == -1) {
        errorExit("ERROR FETCHING SENSOR Data", FETCH_ERROR, mClientFd);
    }

    string msg(BUFFER_SIZE, ' ');
    const auto status = recv(mClientFd, &msg.front(), msg.length(), 0);

    if (status == -1) {
        errorExit("NO MSG RECEIVED", NO_MSG_ERROR, mClientFd);
    } else if (status == 0) {
        errorExit("SERVER CLOSED", SERVER_ERROR, mClientFd);
    }

    if (msg == "NOT FOUND") {
        cout << msg << endl;
        return;
    }

    const auto timestampEnd = msg.find('|');
    const auto timestamp = msg.substr(0, timestampEnd);
    msg.erase(0, timestampEnd + 1);

    vector<string> sensortypes;
    sensortypes.reserve(BUFFER_SIZE);

    size_t pos;
    const string delimiter = ";";
    while ((pos = msg.find(delimiter)) != string::npos) {
        sensortypes.push_back(msg.substr(0, pos));
        msg.erase(0, pos + delimiter.length());
    }

    cout << "Time: " << timestamp << endl;

    for (const auto &str: sensortypes) {
        cout << "Value: " << str << endl;
    }

    cout << endl;
}

void TcpEnviEchoClient::getAllSensors() const {
    constexpr auto endpoint = "getAllSensors()#";
    const int sendStatus = send(mClientFd, endpoint, strlen(endpoint), 0);

    if (sendStatus == -1) {
        errorExit("ERROR FETCHING SENSOR Data", FETCH_ERROR, mClientFd);
    }

    string msg(BUFFER_SIZE, ' ');
    const auto status = recv(mClientFd, &msg.front(), msg.length(), 0);

    if (status == -1) {
        errorExit("NO MSG RECEIVED", NO_MSG_ERROR, mClientFd);
    } else if (status == 0) {
        errorExit("SERVER CLOSED", SERVER_ERROR, mClientFd);
    }

    const auto dataDelimiter = '|';
    const auto timestampEnd = msg.find(dataDelimiter);
    const auto timestamp = msg.substr(0, timestampEnd);
    msg.erase(0, timestampEnd + 1);

    vector<string> sensorTable;
    sensorTable.reserve(BUFFER_SIZE / 2);

    size_t pos;
    const auto keyValDelimiter = ';';
    while ((pos = msg.find(dataDelimiter)) != string::npos) {

        auto data = msg.substr(0, pos);

        size_t valPos;
        if ((valPos = data.find(keyValDelimiter)) != string::npos) {
            const auto sensor = data.substr(0, valPos);

            sensorTable.push_back("Sensor: " + sensor );
            data.erase(0, sensor.length() + 1);


            while ((valPos = data.find(keyValDelimiter)) != string::npos) {
                sensorTable.push_back(" - Value: " + data.substr(0, valPos));
                data.erase(0, valPos + 1);
            }

            sensorTable.push_back(" - Value: " + data);
        }

        msg.erase(0, pos + 1);
    }

    cout << "Time: " << timestamp << endl;

    for (const auto &str: sensorTable) {
        cout << str << endl;
    }

    cout << endl;
}

TcpEnviEchoClient::~TcpEnviEchoClient() {
    close(mClientFd);
}