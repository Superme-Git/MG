
SHARE_HOME = ../../../share
TOP_SRCDIR = $(SHARE_HOME)



SINGLE_THREAD = false
DEBUG_VERSION = false

include $(SHARE_HOME)/mk/gcc.defs.mk

INCLUDES += -I$(SHARE_HOME) 

DEFINES += -DLOG_TO_CONSOLE
LDFLAGS += -static -O3 #-g -ggdb #-pg #-dm

OBJS = logserviceserver.o logservicetcpserver.o state.o stubs.o logservice.o logdispatch.o 

all : logservice

logservice : $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ)

include $(SHARE_HOME)/mk/gcc.rules.mk
