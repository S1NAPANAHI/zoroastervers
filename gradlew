#!/usr/bin/env sh

##############################################################################
##
##  Gradle startup script for UN*X
##
##############################################################################

# Determine the Java command to use to start the JVM.
if [ -n "" ] ; then
    if [ -x "" ] ; then
        JAVACMD=""
    elif [ -x "${JAVA_HOME}/bin/java" ] ; then
        JAVACMD="${JAVA_HOME}/bin/java"
    else
        echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." 1>&2
        exit 1
    fi
else
    JAVACMD="java"
fi

# Determine the script directory.
PRG="$0"

# Need to follow symlinks for this to work.
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done

APP_HOME=`dirname "$PRG"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

# Set the classpath for the wrapper.
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Execute Gradle.
exec "$JAVACMD" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
