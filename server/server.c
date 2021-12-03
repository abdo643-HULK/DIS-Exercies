#include "server.h"

void server(int Port, char *content_file) {
    // FILE *file;
    int server_fd;                    /* parent socket */
    int client_fd;                    /* child socket */
    int client_len;                   /* byte size of client's address */
    struct sockaddr_in clientAddress; /* client addr */
    int child_processID;              /* process id from fork */

    int semid;
    union semun semunion;

    /******************* CHECK FILE ******************
    **************************************************/

    if (access(content_file, F_OK) != 0) {
        // file = fopen(content_file, "w");
        int file = open(content_file, O_CREAT, 0777);
        if (file < 0) {
            /* File not created exit */
            fprintf(stderr, "ERROR: Unable to create file.\n");
            exit(1);
        }
        /* Close file */
        close(file);
        /* Success message */
        printf("File created and saved successfully. :) \n");
    }

    /*************** INITIATE SEMAPHORE ****************
    ***************************************************/

    semid = semget(IPC_PRIVATE, 1, IPC_CREAT | IPC_EXCL);
    if (semid == -1) {
        // if semaphore exists get the semaphore id
        semid = semget(IPC_PRIVATE, 1, 0);
        printf("Semaphore created: %d\n", semid);
        if (semid == -1) {
            fprintf(stderr, "semget failed\n");
            exit(1);
        }
    } else {
        // if sempahore was created new initalize it
        semunion.val = 1;
        semctl(semid, 0, SETVAL, semunion);
    }

    /**************************************************/

    server_fd = start_server(Port);

    while (1) {
        client_len = sizeof(clientAddress);
        client_fd = accept(server_fd, (struct sockaddr *) &clientAddress, &client_len);
        if (client_fd < 0) {
            fprintf(stderr, "ERROR: Not accepted\n");
            exit(1);
        }

        child_processID = fork();

        if (child_processID == 0) {
            /* child  process*/
            close(server_fd); // close parent process
            handle_Request(client_fd, content_file, semid);
            exit(0);
        } else {
            close(client_fd);
            fprintf(stderr, "ERROR: Unable to fork\n");
            exit(1);
        }
        /* clean up */
    }
}

void handle_Request(int client_fd, char *file, int semid) {
    char buf[BUFSIZE]; /* message buffer */
    char *method, *uri, *protocol, *rest, *payload, *t;
    int payload_size, saved = dup(1);
    struct sembuf s;
    chat *data;

    s.sem_num = 0;
    s.sem_flg = SEM_UNDO;
    s.sem_op = -1;

    int MsgLen = recv(client_fd, buf, BUFSIZE, 0);
    if (MsgLen == 0) {
        fprintf(stderr, "ERROR receiving message\n");
        exit(1);
    }

    // printf("Client: %s\n", buf);

    method = strtok(buf, " ");
    uri = strtok(NULL, " ");
    protocol = strtok(NULL, " \r\n");

    printf("Client: %s %s %s\n", method, uri, protocol);

    if (strcasecmp(method, "GET") == 0) {
        if (strcasecmp(uri, "/") == 0) {
            //acquire sem for read
            if (semop(semid, &s, 1) == -1) {
                fprintf(stderr, "semop 1\n");
            } else {
                printf("Semaphore acquired: %d\n", getpid());
            }

            puts("critical section entered");

            response(client_fd, file);

            s.sem_op = 1;
            if (semop(semid, &s, 1) == -1) {
                fprintf(stderr, "semop 2\n");
            }
            puts("outside critical section");
        } else {
            Error_404(client_fd, saved);
        }
    } else if (strcasecmp(method, "POST") == 0) {
        if (strcasecmp(uri, "/") == 0) {
            while (1) {
                char *k, *v;
                k = strtok(NULL, "\r\n: \t");
                v = strtok(NULL, "\r\n");
                if (strncmp(k, "Content-Length:", 16)) {
                    payload_size = atoi(v);
                }
                while (*v && *v == ' ')
                    v++;
                // printf("%s: %s\n", k, v);
                t = v + 1 + strlen(v);
                if (t[1] == '\r' && t[2] == '\n')
                    break;
            }
            payload = t += 3;
            printf("------------\n");
            printf("Content-Length: %d\n", payload_size);
            printf("Payload: %s\n", payload);

            data = extract_data(payload);
            if (data == NULL) {
                send(client_fd, "HTTP/1.0 500 Username or Message missing\r\n\r\n", 45, 0);
                exit(1);
            }

            //acquire sem for read and write
            if (semop(semid, &s, 1) == -1) {
                fprintf(stderr, "semop 1\n");
            } else {
                printf("Semaphore acquired: %d\n", getpid());
            }

            puts("critical section entered");

            handle_payload(file, data->username, data->message, payload_size);
            printf("writing finished\n");
            response(client_fd, file);

            s.sem_op = 1;
            if (semop(semid, &s, 1) == -1) {
                fprintf(stderr, "semop 2\n");
            }
            puts("outside critical section");
        } else {
            Error_404(client_fd, saved);
        }
    } else {
        fflush(stdout);
        dup2(client_fd, 1); // redirect STDOUT
        cerror("501", "Not Implemented",
               "Method not supported");
        fflush(stdout);
        shutdown(1, SHUT_WR);
        dup2(saved, 1);
        close(saved);
        exit(1);
    }
}

void handle_payload(char *file, char *user, char *message, int size) {
    time_t now = time(NULL);
    char *local = asctime(localtime(&now));
    int tmp;

    if (now == ((time_t) - 1)) {
        fprintf(stderr, "ERROR: Failed to obtain the current time.\n");
        exit(1);
    }

    printf("%s\n", local);
    FILE *fpc = fopen(file, "a");
    if (fpc == NULL) {
        fprintf(stderr, "ERROR: Unable to open file\n");
        exit(1);
    }
    tmp = fprintf(fpc, "<tr><td>%s</td><td>%s</td><td>%s</td></tr>\r\n", local, user, message);
    if (tmp < 0) {
        fprintf(stderr, "ERROR: Writing to file unsuccessful\n");
        exit(1);
    }
    fclose(fpc);
}

chat *extract_data(char *payload) {

    chat *p;
    p = (chat *) malloc(sizeof(chat));
    char *user = strtok(payload, "&");
    char *message = strtok(NULL, "&");
    p->username = &user[5];
    p->message = &message[8];
    if (!(p->username)[0] || !(p->message)[0]) {
        fprintf(stderr, "Username or Message missing\n");
        return NULL;
    }
    urldecode2(p->username, p->username);
    urldecode2(p->message, p->message);
    return p;
}

void response(int client_fd, char *file) {
    FILE *fptr;
    int count = 0;
    char *str;
    char buffer[BUFSIZE];
    struct stat sbuf; /* file status */
    int saved = dup(1);

    // Open file
    fptr = fopen(file, "r");
    if (fptr == NULL) {
        fprintf(stderr, "ERROR: Cannot open file \n");
        exit(1);
    }

    //Get File information
    if (stat(file, &sbuf) < 0) {
        fprintf(stderr, "ERROR: Unable to get stats of file\n");
        exit(1);
    }

    count = strlen(POSTFIX) + strlen(PREFIX) + (int) sbuf.st_size;
    // printf("%d\n", count);
    // printf("%s\n", PREFIX);
    // printf("%s\n", POSTFIX);

    //redirect stdout to client
    fflush(stdout);
    dup2(client_fd, 1);

    /************* Response Message ***************/
    printf("HTTP/1.0 200 OK\r\n");
    printf("Server: Mohameds Server\r\n");
    printf("Content-length: %d\r\n", count);
    printf("Content-type: text/html\r\n");
    printf("\r\n");
    printf("%s", PREFIX);
    // Read content from file
    while (fgets(buffer, BUFSIZE, fptr) != NULL) {
        /* Total character read count */
        count = strlen(buffer);
        //Trim new line character from last if exists.
        buffer[count - 1] = buffer[count - 1] == '\n'
                            ? '\0'
                            : buffer[count - 1];
        /* Print line read */
        printf("%s\n", buffer);
    }
    printf("%s", POSTFIX);
    fclose(fptr);
    fflush(stdout);
    dup2(saved, 1);
    close(saved);
}

int start_server(int port) {
    struct sockaddr_in serverAddress;                /* server's addr */
    int server_fd = socket(AF_INET, SOCK_STREAM, 0); /* parent socket */
    int optval = 1;

    if (server_fd < 0) {
        fprintf(stderr, "socket error\n");
        exit(1);
    }

    // setsockopt() free previously used sockets()
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR,
                   &optval, sizeof(optval)) == -1)
        fprintf(stderr, "setsockopt error\n");

    memset(&serverAddress, 0, sizeof serverAddress);
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(port);
    serverAddress.sin_addr.s_addr = htonl(INADDR_ANY);

    if (bind(server_fd, (struct sockaddr *) &serverAddress, sizeof(serverAddress)) < 0) {
        fprintf(stderr, "ERROR binding\n");
        exit(1);
    }

    if (listen(server_fd, 10) == -1) {
        fprintf(stderr, "ERROR listening.\n");
        exit(1);
    }

    printf("Listening....\n");

    return server_fd;
}

void Error_404(int fd, int saved) {
    fflush(stdout);
    dup2(fd, 1); // redirect STDOUT
    cerror("404", "Not found",
           "Couldn't find the requested file");
    fflush(stdout);
    shutdown(1, SHUT_WR);
    dup2(saved, 1);
    close(saved);
}

void cerror(char *errno,
            char *error_msg, char *text) {
    printf("HTTP/1.1 %s %s\r\n", errno, error_msg);
    printf("Content-type: text/html\r\n");
    printf("\r\n");
    printf("<html><title>Error</title>\n");
    printf("<body bgcolor="
           "ffffff"
           ">\n");
    printf("%s: %s\n", errno, error_msg);
    printf("<p>%s\n", text);
}

void urldecode2(char *dst, const char *src) {
    char a, b;
    while (*src) {
        if ((*src == '%') &&
            ((a = src[1]) && (b = src[2])) &&
            (isxdigit(a) && isxdigit(b))) {
            if (a >= 'a')
                a -= 'a' - 'A';
            if (a >= 'A')
                a -= ('A' - 10);
            else
                a -= '0';
            if (b >= 'a')
                b -= 'a' - 'A';
            if (b >= 'A')
                b -= ('A' - 10);
            else
                b -= '0';
            *dst++ = 16 * a + b;
            src += 3;
        } else if (*src == '+') {
            *dst++ = ' ';
            src++;
        } else {
            *dst++ = *src++;
        }
    }
    *dst++ = '\0';
}

void SIGCHLD_handler(int signo) {
    pid_t pid;
    int stat;

    (void) signo;
    while ((pid = waitpid(-1, &stat, WNOHANG)) > 0);
    // optional actions, usually nothing ;
    return;
}

// installer for the SIGCHLD handler
void install_SIGCHLD_handler() {
    struct sigaction act;
    // block all signals during exec of SIGCHLD_handler
    sigfillset(&act.sa_mask);
    act.sa_handler = &SIGCHLD_handler;
    // auto restart interrupted system calls
    act.sa_flags = SA_RESTART;
    sigaction(SIGCHLD, &act, NULL);
}