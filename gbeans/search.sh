#!/bin/bash

[ -z $1 ] && echo "please input keys"  && exit


for file in $(ls);
  do
   target="$(grep -n ${1} ${file})"
   
   [[ -n ${target} ]] && echo key in  $file  $target
  done
