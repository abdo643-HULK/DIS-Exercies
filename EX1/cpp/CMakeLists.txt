cmake_minimum_required(VERSION 3.20)
project(EX1)

set(CMAKE_CXX_STANDARD 17)


add_executable(
        server
        server_main.cpp server.hpp
        servers/shared.cpp servers/shared.hpp
        servers/TcpEchoServer.cpp servers/TcpEchoServer.hpp
        servers/TcpHttpEchoServer.cpp servers/TcpHttpEchoServer.hpp
        servers/TcpEnviEchoServer.cpp servers/TcpEnviEchoServer.hpp
        servers/UdpEchoServer.cpp servers/UdpEchoServer.hpp
)

target_link_libraries(server pthread)

add_executable(
        client
        client_main.cpp client.hpp
        clients/shared.cpp clients/shared.hpp
        clients/TcpEchoClient.cpp clients/TcpEchoClient.hpp
        clients/TcpHttpEchoClient.cpp clients/TcpHttpEchoClient.hpp
        clients/TcpEnviEchoClient.cpp clients/TcpEnviEchoClient.hpp
        clients/UdpEchoClient.cpp clients/UdpEchoClient.hpp
)
