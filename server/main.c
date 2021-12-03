#include "server.h"

int main(int argc, char *argv[])
{
    int c, portNum = 0;
    char *fileName = NULL;

    while ((c = getopt(argc, argv, ":p:f:h")) != -1)
    {
        switch (c)
        {
        case 'p':
            portNum = strtoul(optarg, NULL, 10);
            if (portNum < 1024 || portNum > 65353)
            {
                fprintf(stderr, "Portnumber not useable (can only be 1024-65353)\n");
                exit(1);
            }
            break;
        case 'f':
            fileName = optarg;
            if (strstr(fileName, ".html") == NULL)
            {
                fprintf(stderr, "Wrong filetype\n");
                exit(1);
            }
            break;
        case 'h':
            fprintf(stderr, "Required inputs: \n");
            fprintf(stderr, "-p: Portnumber 1024-65353\n");
            fprintf(stderr, "-f: Filepath for serving file (.html) \n");
            exit(1);
            break;
        }
    }
    if (portNum == 0)
    {
        fprintf(stderr, "Please add a Portnumber\n");
        exit(1);
    }
    if (fileName == NULL)
    {
        fprintf(stderr, "Please provide a filename\n");
        exit(1);
    }
    printf("%s\n", fileName);
    server(portNum, fileName);
    return 0;
}