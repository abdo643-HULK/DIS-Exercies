//
// Created by abous on 03/12/2021.
//

#ifndef EX1_SHARED_HPP
#define EX1_SHARED_HPP

#ifdef _WIN32

#include <winsock2.h>

#elif __unix__

#include <netinet/in.h>

#endif


// rust types
typedef uint32_t u32;

struct Args {
    in_addr ipAddress;
    u32 port;
};


#endif //EX1_SHARED_HPP
