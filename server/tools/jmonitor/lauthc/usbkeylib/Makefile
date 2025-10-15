CFLAGS = -ggdb -g3 -Wall 

all: clean usbkeylib

core.o: core.c
	gcc $(CFLAGS) -c -o core.o core.c

descriptor.o: descriptor.c
	gcc $(CFLAGS) -c -o descriptor.o descriptor.c

hid-libusb.o: hid-libusb.c
	gcc $(CFLAGS) -c -o hid-libusb.o hid-libusb.c

io.o: io.c
	gcc $(CFLAGS) -c -o io.o io.c

linux_usbfs.o: linux_usbfs.c
	gcc $(CFLAGS) -c -o linux_usbfs.o linux_usbfs.c

sync.o: sync.c
	gcc $(CFLAGS) -c -o sync.o sync.c

usbkey.o: usbkey.cpp
	g++ $(CFLAGS) -c -o usbkey.o usbkey.cpp

usbkeylib: core.o descriptor.o hid-libusb.o io.o linux_usbfs.o sync.o usbkey.o
	ar crv usbkeylib.a core.o descriptor.o hid-libusb.o io.o linux_usbfs.o sync.o usbkey.o

clean :
	rm -rf *.o usbkeylib.a


