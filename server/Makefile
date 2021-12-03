service: main.o server.o
	gcc main.o server.o -o service

main.o: main.c server.h
	gcc -c main.c

server.o: server.c server.h
		gcc -c server.c
clean:
	rm *.o service 