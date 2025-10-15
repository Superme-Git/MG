/*=========================================================================*\
* Internet domain functions
* LuaSocket toolkit
*
* RCS ID: $Id: inet.c,v 1.28 2005/10/07 04:40:59 diego Exp $
\*=========================================================================*/
#include <stdio.h>
#include <string.h>

#include "lua.h"
#include "lauxlib.h"

#include "inet.h"

/*=========================================================================*\
* Internal function prototypes.
\*=========================================================================*/
static int inet_global_toip(lua_State *L);
static int inet_global_tohostname(lua_State *L);
static void inet_pushresolved(lua_State *L, struct hostent *hp);
static int inet_global_gethostname(lua_State *L);

/* DNS functions */
static luaL_reg func[] = {
    { "toip", inet_global_toip },
    { "tohostname", inet_global_tohostname },
    { "gethostname", inet_global_gethostname},
    { NULL, NULL}
};

/*=========================================================================*\
* Exported functions
\*=========================================================================*/
/*-------------------------------------------------------------------------*\
* Initializes module
\*-------------------------------------------------------------------------*/
int inet_open(lua_State *L)
{
    lua_pushstring(L, "dns");
    lua_newtable(L);
    luaL_openlib(L, NULL, func, 0);
    lua_settable(L, -3);
    return 0;
}

/*=========================================================================*\
* Global Lua functions
\*=========================================================================*/
/*-------------------------------------------------------------------------*\
* Returns all information provided by the resolver given a host name
* or ip address
\*-------------------------------------------------------------------------*/
static int inet_gethost(const char *address, struct hostent **hp) {
#ifdef OS_IOS
    struct in6_addr addr;
    if (inet_pton(AF_INET, address, &addr))
#else
    struct in_addr addr;
    if (inet_aton(address, &addr))
#endif
        return socket_gethostbyaddr((char *) &addr, sizeof(addr), hp);
    else 
        return socket_gethostbyname(address, hp);
}

/*-------------------------------------------------------------------------*\
* Returns all information provided by the resolver given a host name
* or ip address
\*-------------------------------------------------------------------------*/
static int inet_global_tohostname(lua_State *L) {
    const char *address = luaL_checkstring(L, 1);
    struct hostent *hp = NULL; 
    int err = inet_gethost(address, &hp);
    if (err != IO_DONE) {
        lua_pushnil(L);
        lua_pushstring(L, socket_hoststrerror(err));
        return 2;
    }
    lua_pushstring(L, hp->h_name);
    inet_pushresolved(L, hp);
    return 2;
}

/*-------------------------------------------------------------------------*\
* Returns all information provided by the resolver given a host name
* or ip address
\*-------------------------------------------------------------------------*/
static int inet_global_toip(lua_State *L)
{
    const char *address = luaL_checkstring(L, 1);
    struct hostent *hp = NULL; 
    int err = inet_gethost(address, &hp);
    if (err != IO_DONE) {
        lua_pushnil(L);
        lua_pushstring(L, socket_hoststrerror(err));
        return 2;
    }
#ifdef OS_IOS
    char dstIP[256];
    inet_ntop(AF_INET, (void*)&hp->h_addr, dstIP, sizeof(dstIP));
    lua_pushstring(L, dstIP);
#else
   lua_pushstring(L, inet_ntoa(*((struct in_addr *) hp->h_addr)));
#endif
    inet_pushresolved(L, hp);
    return 2;
}


/*-------------------------------------------------------------------------*\
* Gets the host name
\*-------------------------------------------------------------------------*/
static int inet_global_gethostname(lua_State *L)
{
    char name[257];
    name[256] = '\0';
    if (gethostname(name, 256) < 0) {
        lua_pushnil(L);
        lua_pushstring(L, "gethostname failed");
        return 2;
    } else {
        lua_pushstring(L, name);
        return 1;
    }
}



/*=========================================================================*\
* Lua methods
\*=========================================================================*/
/*-------------------------------------------------------------------------*\
* Retrieves socket peer name
\*-------------------------------------------------------------------------*/
int inet_meth_getpeername(lua_State *L, p_socket ps)
{
#ifdef OS_IOS
    struct sockaddr_in6 peer;
    socklen_t peer_len = sizeof(peer);
    if (getpeername(*ps, (SA *) &peer, &peer_len) < 0) {
        lua_pushnil(L);
        lua_pushstring(L, "getpeername failed");
    } else {
        char dstIP[256];
        inet_ntop(AF_INET, (void*)&peer.sin6_addr, dstIP, sizeof(dstIP));
        //lua_pushstring(L, inet_ntoa(peer.sin_addr));
        lua_pushstring(L, dstIP);
        lua_pushnumber(L, ntohs(peer.sin6_port));
    }
#else
    struct sockaddr_in peer;
    socklen_t peer_len = sizeof(peer);
    if (getpeername(*ps, (SA *) &peer, &peer_len) < 0) {
        lua_pushnil(L);
        lua_pushstring(L, "getpeername failed");
    } else {
        lua_pushstring(L, inet_ntoa(peer.sin_addr));
        lua_pushnumber(L, ntohs(peer.sin_port));
    }
#endif
    return 2;
}

/*-------------------------------------------------------------------------*\
* Retrieves socket local name
\*-------------------------------------------------------------------------*/
int inet_meth_getsockname(lua_State *L, p_socket ps)
{
#ifdef OS_IOS
    struct sockaddr_in6 local;
    socklen_t local_len = sizeof(local);
    if (getsockname(*ps, (SA *) &local, &local_len) < 0) {
        lua_pushnil(L);
        lua_pushstring(L, "getsockname failed");
    } else {
        char dstIP[256];
        inet_ntop(AF_INET, (void*)&local.sin6_addr, dstIP, sizeof(dstIP));
        //lua_pushstring(L, inet_ntoa(local.sin_addr));
        lua_pushstring(L, dstIP);
        lua_pushnumber(L, ntohs(local.sin6_port));
    }
#else
    struct sockaddr_in local;
    socklen_t local_len = sizeof(local);
    if (getsockname(*ps, (SA *) &local, &local_len) < 0) {
        lua_pushnil(L);
        lua_pushstring(L, "getsockname failed");
    } else {
        lua_pushstring(L, inet_ntoa(local.sin_addr));
        lua_pushnumber(L, ntohs(local.sin_port));
    }
#endif
    return 2;
}

/*=========================================================================*\
* Internal functions
\*=========================================================================*/
/*-------------------------------------------------------------------------*\
* Passes all resolver information to Lua as a table
\*-------------------------------------------------------------------------*/
static void inet_pushresolved(lua_State *L, struct hostent *hp)
{
#ifdef OS_IOS
    char **alias;
    struct in6_addr **addr;
    int i, resolved;
    lua_newtable(L); resolved = lua_gettop(L);
    lua_pushstring(L, "name");
    lua_pushstring(L, hp->h_name);
    lua_settable(L, resolved);
    lua_pushstring(L, "ip");
    lua_pushstring(L, "alias");
    i = 1;
    alias = hp->h_aliases;
    lua_newtable(L);
    if (alias) {
        while (*alias) {
            lua_pushnumber(L, i);
            lua_pushstring(L, *alias);
            lua_settable(L, -3);
            i++; alias++;
        }
    }
    lua_settable(L, resolved);
    i = 1;
    lua_newtable(L);
    addr = (struct in6_addr **) hp->h_addr_list;
    if (addr) {
        while (*addr) {
            lua_pushnumber(L, i);
            char dstIP[256];
            inet_ntop(AF_INET, (void*)*addr, dstIP, sizeof(dstIP));
            //lua_pushstring(L, inet_ntoa(**addr));
            lua_pushstring(L, dstIP);
            lua_settable(L, -3);
            i++; addr++;
        }
    }
    lua_settable(L, resolved);
#else
    char **alias;
    struct in_addr **addr;
    int i, resolved;
    lua_newtable(L); resolved = lua_gettop(L);
    lua_pushstring(L, "name");
    lua_pushstring(L, hp->h_name);
    lua_settable(L, resolved);
    lua_pushstring(L, "ip");
    lua_pushstring(L, "alias");
    i = 1;
    alias = hp->h_aliases;
    lua_newtable(L);
    if (alias) {
        while (*alias) {
            lua_pushnumber(L, i);
            lua_pushstring(L, *alias);
            lua_settable(L, -3);
            i++; alias++;
        }
    }
    lua_settable(L, resolved);
    i = 1;
    lua_newtable(L);
    addr = (struct in_addr **) hp->h_addr_list;
    if (addr) {
        while (*addr) {
            lua_pushnumber(L, i);
            lua_pushstring(L, inet_ntoa(**addr));
            lua_settable(L, -3);
            i++; addr++;
        }
    }
    lua_settable(L, resolved);
#endif
}

/*-------------------------------------------------------------------------*\
* Tries to create a new inet socket
\*-------------------------------------------------------------------------*/
const char *inet_trycreate(p_socket ps, int type) {
#ifdef OS_IOS
    return socket_strerror(socket_create(ps, AF_INET6, type, 0));
#else
    return socket_strerror(socket_create(ps, AF_INET, type, 0));
#endif
}

/*-------------------------------------------------------------------------*\
* Tries to connect to remote address (address, port)
\*-------------------------------------------------------------------------*/
const char *inet_tryconnect(p_socket ps, const char *address, 
        unsigned short port, p_timeout tm)
{
#ifdef OS_IOS
    struct sockaddr_in6 remote;
    int err;
    memset(&remote, 0, sizeof(remote));
    remote.sin6_family = AF_INET;
    remote.sin6_port = htons(port);
	if (strcmp(address, "*")) {
        //if (!inet_aton(address, &remote.sin_addr))
        if (inet_pton(AF_INET, address, &remote.sin6_addr))
        {
            struct hostent *hp = NULL;
            struct in6_addr **addr;
            err = socket_gethostbyname(address, &hp);
            if (err != IO_DONE) return socket_hoststrerror(err);
            addr = (struct in6_addr **) hp->h_addr_list;
            memcpy(&remote.sin6_addr, *addr, sizeof(struct in6_addr));
        }
    } else remote.sin6_family = AF_UNSPEC;
    err = socket_connect(ps, (SA *) &remote, sizeof(remote), tm);
#else
   struct sockaddr_in remote;
    int err;
    memset(&remote, 0, sizeof(remote));
    remote.sin_family = AF_INET;
    remote.sin_port = htons(port);
	if (strcmp(address, "*")) {
        if (!inet_aton(address, &remote.sin_addr)) {
            struct hostent *hp = NULL;
            struct in_addr **addr;
            err = socket_gethostbyname(address, &hp);
            if (err != IO_DONE) return socket_hoststrerror(err);
            addr = (struct in_addr **) hp->h_addr_list;
            memcpy(&remote.sin_addr, *addr, sizeof(struct in_addr));
        }
    } else remote.sin_family = AF_UNSPEC;
    err = socket_connect(ps, (SA *) &remote, sizeof(remote), tm);
#endif
    return socket_strerror(err);
}

/*-------------------------------------------------------------------------*\
* Tries to bind socket to (address, port)
\*-------------------------------------------------------------------------*/
const char *inet_trybind(p_socket ps, const char *address, unsigned short port)
{
#ifdef OS_IOS
    struct sockaddr_in6 local;
    int err;
    memset(&local, 0, sizeof(local));
    /* address is either wildcard or a valid ip address */
    local.sin6_addr = in6addr_any;// htonl(INADDR_ANY);
    local.sin6_port = htons(port);
    local.sin6_family = AF_INET;
    //if (inet_pton(AF_INET6, address, &remote.sin_addr))
    if (strcmp(address, "*") && !inet_pton(AF_INET, address, &local))
    {
        struct hostent *hp = NULL;
        struct in6_addr **addr;
        err = socket_gethostbyname(address, &hp);
        if (err != IO_DONE) return socket_hoststrerror(err);
        addr = (struct in6_addr **) hp->h_addr_list;
        memcpy(&local.sin6_addr, *addr, sizeof(struct in6_addr));
    }
    err = socket_bind(ps, (SA *) &local, sizeof(local));
    if (err != IO_DONE) socket_destroy(ps);
#else
    struct sockaddr_in local;
    int err;
    memset(&local, 0, sizeof(local));
    /* address is either wildcard or a valid ip address */
    local.sin_addr.s_addr = htonl(INADDR_ANY);
    local.sin_port = htons(port);
    local.sin_family = AF_INET;
    if (strcmp(address, "*") && !inet_aton(address, &local.sin_addr)) {
        struct hostent *hp = NULL;
        struct in_addr **addr;
        err = socket_gethostbyname(address, &hp);
        if (err != IO_DONE) return socket_hoststrerror(err);
        addr = (struct in_addr **) hp->h_addr_list;
        memcpy(&local.sin_addr, *addr, sizeof(struct in_addr));
    }
    err = socket_bind(ps, (SA *) &local, sizeof(local));
    if (err != IO_DONE) socket_destroy(ps);
#endif
    return socket_strerror(err); 
}

/*-------------------------------------------------------------------------*\
* Some systems do not provide this so that we provide our own. It's not
* marvelously fast, but it works just fine.
\*-------------------------------------------------------------------------*/
#ifdef INET_ATON
#ifdef OS_IOS
int inet_aton(const char *cp, struct in6_addr *inp)
#else
int inet_aton(const char *cp, struct in_addr *inp)
#endif
{
    unsigned int a = 0, b = 0, c = 0, d = 0;
    int n = 0, r;
    unsigned long int addr = 0;
    r = sscanf(cp, "%u.%u.%u.%u%n", &a, &b, &c, &d, &n);
    if (r == 0 || n == 0) return 0;
    cp += n;
    if (*cp) return 0;
    if (a > 255 || b > 255 || c > 255 || d > 255) return 0;
    if (inp) {
        addr += a; addr <<= 8;
        addr += b; addr <<= 8;
        addr += c; addr <<= 8;
        addr += d;
        inp->s_addr = htonl(addr);
    }
    return 1;
}
#endif


