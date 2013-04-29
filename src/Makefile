FILES := $(shell find . -name '*.java')
CLASSES := $(FILES:%.java=%.class)


.PHONY : build clean run

build : $(CLASSES)

%.class : %.java
	javac $?


clean :
	find . -name '*.class' -exec rm -rf {} \;

run : build
	java itmo.dreamq.DreamQueuePublisher
