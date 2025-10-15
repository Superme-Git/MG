#!/bin/bash
ulimit -n 20480
ulimit -n
rm -f trace.log
nohup java -jar robot_r.jar 192.168.32.2 14000 1 500 jqrb &


