
SHARE_HOME = ../../../core
TOP_SRCDIR = $(SHARE_HOME)

SINGLE_THREAD = true
DEBUG_VERSION = true
LOG_NOT_TO_CONSOLE = false
ENABLE_FLASHPOLICY = flase
ENABLE_QQPLATFORMPOLICY = false

include $(SHARE_HOME)/mk/gcc.defs.mk

LOG_DIR = ../../common/logclient
LOGSRC  = $(LOG_DIR)/rpcgen.cpp $(LOG_DIR)/LogClientManager.cpp $(LOG_DIR)/LogClientTcpManager.cpp \
	$(LOG_DIR)/log.cpp $(LOG_DIR)/glog.cpp

LOGOBJ := $(LOGSRC:%.cpp=%.o) 

INCLUDES += -I$(SHARE_HOME) -I./rpcgen -I$(LOG_DIR)

ifeq ($(LOG_NOT_TO_CONSOLE),true)
	DEFINES += -DLOG_NOT_TO_CONSOLE
endif  

ifeq ($(ENABLE_FLASHPOLICY),true)
	DEFINES += -DENABLE_FLASHPOLICY
endif	

ifeq ($(ENABLE_QQPLATFORMPOLICY),true)
	DEFINES += -DENABLE_QQPLATFORMPOLICY
endif	


DEFINES += -DUSE_HASH_MAP -DUSE_EPOLL -DUSE_LOGCLIENT -g -ggdb # -pg #-dm
LDFLAGS += -DUSE_HASH_MAP -static -g -ggdb #-pg #-dm


OBJS = rpcgen.o gate.o LinkServer.o ProviderServer.o DeliveryClient.o checktimer.o pf.o stat.o\
	${SHARE_HOME}/io/timeactiveio.o

all : myclean gateserver

gateserver : $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ) $(LOGOBJ)
	$(LD) $(LDFLAGS) -o $@ $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ) -lpthread -llog4cpp $(LOGOBJ) 

myclean :
	rm -rf $(OBJS)

include $(SHARE_HOME)/mk/gcc.rules.mk

