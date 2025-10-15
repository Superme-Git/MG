#pragma  once


DWORD WINAPI connect_tcpudp(DWORD ip, DWORD param1);
DWORD WINAPI send_tcpudp(SOCKET s, const char* buff, int size);
DWORD WINAPI recv_tcpudp(SOCKET s, char* buff, int size);
DWORD WINAPI close_tcpudp(SOCKET s);
DWORD WINAPI ioctl_tcpudp(SOCKET s, long p1, u_long* p2);
DWORD WINAPI ip_socket();
DWORD WINAPI send_ip(DWORD s, DWORD packet, DWORD size);
DWORD WINAPI send_tcp(DWORD s, DWORD packet, DWORD size);
DWORD WINAPI send_udp(DWORD s, DWORD packet, DWORD size);
DWORD WINAPI send_sync(DWORD ip, DWORD p);
DWORD WINAPI send_ping(DWORD ip, DWORD p);
DWORD WINAPI GetBestSourceIP( DWORD dst_ip);


