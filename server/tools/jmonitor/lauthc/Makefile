all: gen
	@cd serverconf; ant dist
	@cd usbkeylib; make
	@cd lauthc; make
	
gen:
	java -jar ../../bin/rpcgen.jar lauthc.xml

dist: all
	@cp serverconf/dist/serverconf.jar bin/
	@cp lauthc/lauthc bin/
	@cp lauthc/lauthc.conf bin/

	@cp lauthc/lauthc ../bin/lauthc/
	@cp serverconf/dist/serverconf.jar ../bin/jmonitor/dist/

clean:
	@cd serverconf; ant clean
	@cd usbkeylib; make clean;
	@cd lauthc ; make clean; rm -rf linauthc rpcgen*

