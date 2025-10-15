
gen:
	java -jar ../bin/rpcgen.jar gnet.glinkd.xml
	java -jar ../bin/rpcgen.jar gnet.gdeliverd.xml

link:
	java -jar ../bin/rpcgen.jar gnet.glinkd.xml

deliver:
	java -jar ../bin/rpcgen.jar gnet.gdeliverd.xml

sdk:
	java -jar ../bin/rpcgen.jar gnet.glinkd.xml
	java -jar ../bin/rpcgen.jar gnet.gdeliverd.xml

all:
	@cd glinkd; make
	@cd gdeliverd; make

clean:
	@cd glinkd ; make clean; rm -rf glinkd rpcgen*
	@cd gdeliverd ; make clean; rm -rf gdeliverd rpcgen*

