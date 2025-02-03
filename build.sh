mkdir bin
javac --release 11 -cp "lib/htsjdk-4.1.3-imp.jar:lib/snappy-java-1.1.7.3.jar" -d bin src/umicollapse/*/*.java src/test/*.java
cd bin
jar -c -m ../Manifest.txt -f ../../umicollapse-new.jar umicollapse/*/*.class test/*.class


echo "Done!"
