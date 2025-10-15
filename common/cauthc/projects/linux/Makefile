CXX = g++
LIBTOOL = libtool
CXXFLAGS = -ggdb -g3 -Wall -std=c++0x -pthread
DESTLIB = cauthc.a 
DESTAPP = test

SRC_SHARE = ../../authc/share
SRC_OS = ../../authc/os/linux
SRC_AUTHC = ../../authc

all: clean $(DESTAPP)

marshal.o : $(SRC_SHARE)/marshal.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

streamcompress.o : $(SRC_SHARE)/streamcompress.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

byteorder.o : $(SRC_SHARE)/byteorder.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

security.o : $(SRC_SHARE)/security.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

octets.o : $(SRC_SHARE)/octets.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

netsession.o : $(SRC_AUTHC)/netsession.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

pollio.o : $(SRC_AUTHC)/pollio.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

ioengine.o : $(SRC_AUTHC)/ioengine.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

rpcgen.o : $(SRC_AUTHC)/rpcgen.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

protocol.o : $(SRC_AUTHC)/protocol.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

authc.o : $(SRC_AUTHC)/authc.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 

timer.o : $(SRC_AUTHC)/timer.cpp
	$(CXX) $(CXXFLAGS) -I$(SRC_OS) -I$(SRC_AUTHC) -c -o $@ $< 
	
$(DESTLIB) : marshal.o streamcompress.o byteorder.o security.o octets.o netsession.o pollio.o ioengine.o rpcgen.o protocol.o authc.o timer.o 
	ar -ru -o $@ $^

test.o : test.cpp
	$(CXX) $(CXXFLAGS) -c -o $@ $<

$(DESTAPP) : test.o $(DESTLIB)
	$(CXX) $(CXXFLAGS) -o $@ test.o $(DESTLIB)

clean : 
	$(RM) *.o *.a  $(DESTAPP)
