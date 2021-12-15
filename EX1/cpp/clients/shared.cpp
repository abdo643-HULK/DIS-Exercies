//
// Created by abous on 03/12/2021.
//

#include "shared.hpp"

using namespace std;


void errorExit(const char *const _msg, int _exitCode, const int _fd) {
    cerr << "\n" << "<<< " << _msg << " >>>" << endl;
    _fd != -1 && close(_fd);
    exit(_exitCode);
}