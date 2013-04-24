FILES := $(shell find . -name '*.java')
CLASSES := $(FILES:%.java=%.class)


.PHONY : build clean

build : $(CLASSES)

%.class : %.java
	javac $?


clean :
	find . -name '*.class' -exec rm -rf {} \;
