//
// Created by abous on 03/12/2021.
//

#ifndef EX1_SHARED_HPP
#define EX1_SHARED_HPP

#include <iostream>
#include "../common.hpp"

/**
 * The parsed arguments for the clients
 */
struct Args {
    const char* ipAddress;
    const u32 port;
};

/**
 * shutdown code to check from the user
 */
constexpr auto SHUTDOWN = "shutdown";

/**
 * quit code to check from the user
 */
constexpr auto QUIT = "quit";


#endif //EX1_SHARED_HPP
