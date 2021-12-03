#ifndef SERVER_H
#define SERVER_H

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>
#include <signal.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <netinet/in.h>
#include <time.h>
#include <fcntl.h>
#include <ctype.h>
#include <getopt.h>

#define BUFSIZE 1024

#define PREFIX "<html>\n<head>\n<title>Message Board</title>\n</head>\n<body>\n<h1>Message Board</h1>\n<table border=\"1\" width=\"100%\">\n<tr><th>Time</th><th>User</th><th>Message</th></tr>\n"

#define POSTFIX "​</table>\n<hr>\n​<form action=\"/\" method=\"POST\">\n​<label for=\"user\">User:</label><br/>\n​<input type=\"text\" id=\"user\" name=\"user\"><br/>\n​<label for=\"message\">Message:</label><br/>\n​<input type=\"text\" id=\"message\" name=\"message\"><br/>\n​<input type=\"submit\">\n​</form>\n</body>\n</html>\n"

typedef struct
{
    char *username;
    char *message;
} chat;

union semun
{
    int val;
    struct semid_ds *buf;
    unsigned short int *array;
    struct seminfo *__buf;
};

void server(int Port, char *content_file);
int start_server(int port);
void Error_404(int fd, int saved);
void handle_Request(int client_fd, char *file, int semid);
void handle_payload(char *file, char *user, char *message, int size);
void response(int client_fd, char *file);
void cerror(char *errno,
            char *error_msg, char *text);
void SIGCHLD_handler(int);
void install_SIGCHLD_handler(void);
chat *extract_data(char *payload);
void urldecode2(char *dst, const char *src);

#endif
