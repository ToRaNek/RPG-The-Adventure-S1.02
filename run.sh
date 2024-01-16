#!/bin/bash
export CLASSPATH=`find ./lib -name "*.jar" | tr '\n' ':'`
export MAINCLASS=jeu
java -cp ${CLASSPATH}:classes $MAINCLASS