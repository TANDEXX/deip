#!/bin/sh
# run / build / pack into jar tool for unix like systems writen by TANDEX

mainClass="deip.Root"
mainClassPath="deip/Root.java"
buildOutputDir="out/production/deip/"
normalRunArgs=""
jarRunArgs=""
run=1
build=0
jar=0
old=0
argSet=0
args=""


help() {

 echo -n "deip run / build / pack into jar script in unix sh.
Writen by TANDEX.
usage:
	$0 [OPTIONS] [PROGRAM ARGUMENTS...]
options:
	--help	this help massage
	--build	build project
	--no-run	do not run program
	--jar	build project to jar
	--old	build project for 6 java version (most used version)
"

}

msg() {

	if [ $1 = i ]; then

		echo "[RUN.SH/INFO] $2"

	elif [ $1 = e ]; then

		if tty > /dev/null; then

			echo "[0;31m[RUN.SH/ERROR] $2[0;1;37m"

		else

			echo "[RUN.SH/ERROR] $2"

		fi

	fi

}

for a in "$@"; do

	if [ "`echo $a | cut -c1-2`" = "--" ] && [ $argSet = 0 ]; then

		if [ "$a" = "--help" ]; then

			help
			run=0
			build=0
			jar=0
			break

		elif [ "$a" = "--build" ]; then

			build=1

		elif [ "$a" = "--no-run" ]; then

			run=0

		elif [ "$a" = "--jar" ]; then

			jar=1

		elif [ "$a" = "--old" ]; then

			old=1

		elif [ "$a" = "--" ]; then

			argSet=1

		else

			echo "wrong option: \"$a\""

		fi

	else

		args="$args $a"

	fi

done

if [ $build = 1 ]; then

	msg i "building..."
	cd src
	if [ $old = 1 ]; then

		if ! javac -d "../$buildOutputDir" --release 6 "$mainClassPath"; then

			msg e "build failed. Aborting..."
			run=0
			jar=0

		fi

	else

		if ! javac -d "../$buildOutputDir" "$mainClassPath"; then

			msg e "build failed. Aborting..."
			run=0
			jar=0

		fi

	fi
	cd ..

fi

if [ $jar = 1 ]; then

	msg i "packing into jar file..."
	cd "$buildOutputDir"
	if ! jar --create --file "$OLDPWD/jarOutput.jar" --main-class="$mainClass" *; then

		msg e "failed to pack into jar file. Aborting..."
		run=0

	else

		if [ $run = 1 ]; then
			run=2
		fi

	fi
	cd "$OLDPWD"

fi

if [ $run != 0 ]; then

	msg i "running..."

	if [ $run = 1 ]; then

		command="java -cp $buildOutputDir $mainClass $normalRunArgs"

	else

		command="java -jar jarOutput.jar $jarRunArgs"

	fi

	if $command $args; then

		msg i "program ended successfully."

	else

		msg e "program failed."

	fi

fi
