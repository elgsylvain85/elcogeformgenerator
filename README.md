# Local Maven Dependance

This project is compile with OpenJDK 1.8 and using Javafx (jfxrt.jar) API so install that locally in maven :
mvn install:install-file -Dfile=/usr/local/maven/lib/jfxrt.jar -DgroupId=local -DartifactId=jfxrt -Dversion=1.8 -Dpackaging=jar -DgeneratePom=false