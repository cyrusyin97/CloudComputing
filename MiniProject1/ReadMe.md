# Mapreduce Program Execution in Hadoop
​
The Miniproject 1 contains the installation of Hadoop and cluster setup based on Ubuntu VM. 
Several mapreduce programs are implemented in Java for N-gram counting and access log file analysis.
This file shows how to execute a mapreduce program in a Hadoop cluster.
## Start Hadoop
1. Log into the VM of Master node in cluster and `cd` to the Hadoop root directory.
2. Use `jps` to check if the Hadoop cluster is started.
3. If the cluster is not started, use `sbin start-dfs.sh` and `sbin start-yarn.sh` to start the services.
4. Use `jps` to check if Namenode is avaliable.
## Compile Mapreduce Program and Create Jar
1. Create a project directory under your Hadoop root path and edit your mapreduce code in it.<br>
    For example: `mkdir Ngram`,`nano NGram.java`.
2. After completing your code, you need to compile it by the following commands:<br>
`export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64` (This should be your own Java path)<br>
`export PATH=${JAVA_HOME}/bin:${PATH}`<br>
`export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar`
`bin/hadoop com.sun.tools.javac.Main NGram/NGram.java`
3. Use `ls` and you should see *.class files under Ngram.
4. Now let's create the jar using all class files.<br>
`jar cf NGram.jar *.class`
5. Now you should have an NGram.jar under the directory.
## Execute the Program
1. First you need to create an input folder in HDFS. You probably need to specify the path:`export PATH=$PATH:~/hadoop/bin`<br>
`hdfs dfs -ls /`<br>
`hdfs dfs -mkdir /user`<br>
`hdfs dfs -mkdir /user/student`<br>
`hdfs dfs -mkdir input`<br>
`hdfs dfs -put etc/hadoop/*.xml input` (Use all the .xml files as our input.)<br>
`hdfs dfs -ls /user/student/input`
2. Now use jar to actually run your program.<r>
`bin/hadoop jar /NGram/NGram.jar NGram input/ output/`
3. Check the results in 'output'<br>
`hdfs dfs -cat output/*`
Then remember to delete the output directory:<br>
`hdfs dfs -rm -r output/`
