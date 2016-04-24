#!/bin/bash

 files_list="files_list.txt"


 for i in `find . -name '*.xml' -type f`; do
    echo `wc -l $i` >> $files_list
 done

 c=0
 for a in `cat $files_list | cut -d ' ' -f 1`; do
    c=`expr $c + $a`
 done

 echo "Sum: "$c" lines" >> $files_list