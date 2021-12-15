//
// Created by abous on 15/12/2021.
//

#include "common.hpp"

using namespace std;

void errorExit(const char *const _msg, const int _exitCode, const int _fd1) {
    cerr << endl;
    cerr << "<<< " << _msg << " >>>" << endl;
    _fd1 != -1 && close(_fd1);
    exit(_exitCode);
}

void printError(const char *const _error) {
    cerr << endl;
    cerr << "<<< " << _error << " >>>" << endl;
}
