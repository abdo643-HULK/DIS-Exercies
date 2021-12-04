cmake_minimum_required(VERSION 3.20)
project(EX1)

set(CMAKE_CXX_STANDARD 14)

add_executable(
        server
        server_main.cpp server.hpp
        servers/TcpEchoServer.cpp servers/TcpEchoServer.hpp
        servers/TcpHttpEchoServer.cpp servers/TcpHttpEchoServer.hpp
        servers/TcpEnviEchoServer.cpp servers/TcpEnviEchoServer.hpp
        servers/UdpEchoServer.cpp servers/UdpEchoServer.hpp
)

add_executable(
        client
        client_main.cpp client.hpp
        clients/shared.cpp clients/shared.hpp
        clients/TcpEchoClient.cpp clients/TcpEchoClient.hpp
        clients/TcpHttpEchoClient.cpp clients/TcpHttpEchoClient.hpp
        clients/TcpEnviEchoClient.cpp clients/TcpEnviEchoClient.hpp
        clients/UdpEchoClient.cpp clients/UdpEchoClient.hpp
)
