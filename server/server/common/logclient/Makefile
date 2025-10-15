
SHARE_HOME = ../../../core
TOP_SRCDIR = $(SHARE_HOME)

SINGLE_THREAD = true
DEBUG_VERSION = false

include $(SHARE_HOME)/mk/gcc.defs.mk

DEFINES += -DUSE_LOGCLIENT
INCLUDES += -I$(SHARE_HOME) -I./rpcgen

OBJS = rpcgen.o logclient.o LogClientManager.o LogClientTcpManager.o glog.o log.o

all : myclean logclient

logclient : $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ)

myclean :
	rm -rf $(OBJS)

include $(SHARE_HOME)/mk/gcc.rules.mk

