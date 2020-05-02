# Spark Program Execution

The Miniproject 1 contains the installation and of Spark based on Ubuntu VM. 
Several Spark programs are implemented for listening counts of `user_artists.dat` and simple linear regression of `access_log`.
This file shows how to execute a Spark program based on Hadoop configuration.
## Start Hadoop
1. Log into the VM of Master node in cluster.
2. Use `jps` to check if the Hadoop cluster is started.
3. If the cluster is not started, use `sbin start-dfs.sh` and `sbin start-yarn.sh` to start the services.
4. Use `jps` to check if Namenode is avaliable.
## Compile Spark Program
1. Create a project directory and edit your spark program in it.<br>
    directory structure: <br>
    
    |-pom.xml<br>
    |<br>
    |-src/main/java/program.java<br>
    
2. After completing your code, go back to your project root directory and use `mvn package` to compile.<br>
    Now your directory should be like:<br>
    
    |-pom.xml<br>
    |<br>
    |-src/main/java/program.java<br>
    |<br>
    |-target
    
3. You can use `find .` to check out the compiled files.
4. Now you can find executable jar under `target`.<br>

## Execute the Program
1. `~/spark/bin/spark-submit --class "ClassName" --master local --driver-memory 1g --executor-memory 1g --executor-cores 1 --queue default target/*.jar` <br>
Use this command to run the spark program.<br>

2. Check the output files of your program on HDFS:
`hdfs dfs -cat output/*`
