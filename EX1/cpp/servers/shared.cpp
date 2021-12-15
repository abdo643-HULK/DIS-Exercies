//
// Created by abous on 09/12/2021.
//

#include "shared.hpp"

using namespace std;

bool printClientInfo(const IpAddrKind _ip, const sockaddr_storage *const _address) {
    char address[48];
    cout << "NEW CLIENT: " << endl;
    cout << "Client IP-Address: ";

    switch (_ip) {
        case IpAddrKind::V4: {
            const auto addr = reinterpret_cast<const sockaddr_in *const>(_address);
            const auto ret = inet_ntop(AF_INET,
                                       &addr->sin_addr,
                                       address,
                                       sizeof(sockaddr_in));
            if (ret == nullptr) {
                printError("ERROR PARSING CLIENT ADDRESS");
                return false;
            }

            cout << address << endl;
            cout << "Client Port: " << ntohs(addr->sin_port) << endl;
            break;
        }
        case IpAddrKind::V6: {
            const auto addr = reinterpret_cast<const sockaddr_in6 *const>(_address);
            const auto ret = inet_ntop(AF_INET6,
                                       &addr->sin6_addr,
                                       address,
                                       sizeof(sockaddr_in6));
            if (ret == nullptr) {
                printError("ERROR PARSING CLIENT ADDRESS");
                return false;
            }

            cout << address << endl;
            cout << "Client Port: " << ntohs(addr->sin6_port) << endl;
            break;
        }
    }

    cout << endl;

    return true;
}
