# Environment variables
export MQ_HOME=./mq
export IMQ_JAVAHOME=/usr/lib/jvm/java-8-openjdk-amd64/

# Run broker
cd $MQ_HOME/bin
./imqbrokerd -tty 
