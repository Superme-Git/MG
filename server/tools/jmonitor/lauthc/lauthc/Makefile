
SHARE_HOME = ../../../../share
TOP_SRCDIR = $(SHARE_HOME)

SINGLE_THREAD = true
DEBUG_VERSION = true

include $(SHARE_HOME)/mk/gcc.defs.mk

DEFINES += -g3 -ggdb -static
INCLUDES += -I$(SHARE_HOME) -I./rpcgen

OBJS = rpcgen.o lauthc.o LinkClient.o protocolprocess.o InfoServer.o pfio.o pf.o

all : myclean lauthc

lauthc : $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ)
	$(LD) $(LDFLAGS) -o $@ $(OBJS) $(SHAREOBJ) $(SHARE_SOBJ) ../usbkeylib/usbkeylib.a -lrt 

myclean :
	rm -rf $(OBJS)

include $(SHARE_HOME)/mk/gcc.rules.mk

