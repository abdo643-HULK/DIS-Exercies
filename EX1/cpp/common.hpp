//
// Created by abous on 15/12/2021.
//

#ifndef EX1_COMMON_HPP
#define EX1_COMMON_HPP

#include <iostream>

#ifdef _WIN32
extern "C" {
#include <winsock2.h>
#include <ws2tcpip.h>
#include <stdio.h>
#include <windows.h>
}

#elif __unix__

extern "C" {
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>

#include <unistd.h>
#include <arpa/inet.h>
#include <semaphore.h>
}


#endif

// rust types
typedef uint8_t u8;
typedef uint16_t u16;
typedef uint32_t u32;
typedef uint64_t u64;
// rust types
typedef int8_t i8;
typedef int16_t i16;
typedef int32_t i32;
typedef int64_t i64;


/**
 * enum for the address type
 */
enum class IpAddrKind : int {
    V4 = AF_INET,
    V6 = AF_INET6
};

/**
 * The Buffer size for all the char arrays
 */
constexpr u16 BUFFER_SIZE = 1024;

/**
 * prints error message
 * @param _msg the error message to display
 * @param _exitCode exit code of the error
 * @param _fd file descriptor to close
 */
void errorExit(const char *_msg, int _exitCode = -1, int _fd = -1);

/**
 * prints an error in `std::err` with a custom format
 * @param _error error msg to show
 */
void printError(const char *_error);

#endif //EX1_COMMON_HPP
