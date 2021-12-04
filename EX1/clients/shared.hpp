//
// Created by abous on 03/12/2021.
//

#ifndef EX1_SHARED_HPP
#define EX1_SHARED_HPP

#include <iostream>

#ifdef _WIN32

#include <winsock2.h>

#elif __unix__

#include <netinet/in.h>
#include <unistd.h>

#endif


// rust types
typedef uint8_t u8;
typedef uint16_t u16;
typedef uint32_t u32;

struct Args {
    const char* ipAddress;
    const u32 port;
};

constexpr u16 BUFFER_SIZE = 1024;
constexpr auto SHUTDOWN = "shutdown";
constexpr auto QUIT = "quit";

void errorExit(const char *_msg, int _exitCode = -1, int _fd = -1);

#endif //EX1_SHARED_HPP
