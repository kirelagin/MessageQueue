FILES := $(shell find . -name '*.java')
CLASSES := $(FILES:%.java=%.class)


.PHONY : build clean

build : $(CLASSES)

clean :
	find . -name '*.class' -exec rm -rf {} \;

$(CLASSES) : $(FILES)
	javac $?
