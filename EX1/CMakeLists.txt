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
        clients/TcpEchoClient.cpp clients/TcpEchoClient.hpp
        clients/UdpEchoClient.cpp clients/UdpEchoClient.hpp
        clients/shared.hpp)
