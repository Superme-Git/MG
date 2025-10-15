#!/bin/bash
ulimit -n 20480
ulimit -n
java -Xmx4096m -Xms1024m -DJava.net.preferIPv4Stack=true -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=39000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.password.file=jmxremote.password -cp dist/robot.jar:lib/xdb.jar:lib/jio.jar:lib/jauthc.jar robot.Main 172.16.0.79 30000 1 1000 &


