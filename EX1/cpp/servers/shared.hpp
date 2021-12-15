//
// Created by abous on 09/12/2021.
//

#ifndef EX1_SHARED_HPP
#define EX1_SHARED_HPP

#include <iostream>
#include "../common.hpp"
#include "../errors.hpp"

/**
 * The amount of available Threads
 */
constexpr auto THREAD_COUNT = 10;

/**
 * Prints the address and port of the passed in sockaddr
 * Support V6 and V4
 *
 * @param _ip either V4 or V6
 * @param _address the struct holding the client info
 * @return if the print was successful returns true else false
 */
bool printClientInfo(IpAddrKind _ip, const sockaddr_storage *_address);

#endif //EX1_SHARED_HPP
