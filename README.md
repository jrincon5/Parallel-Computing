# Parallel-Computing
This repository keeps the project developed in the course Parallel Computing of the Universidad EAFIT

# Steps to use
* 1 You have to download the wordcount folder.
* 2 You need to to compile the wordcount project inside the folder with SBT (package control for scala): $ sbt package
* 3 You have to run the spark process with yarn if you have your own spark cluster, otherwise you can do it local: $ spark-submit --master yarn --deploy-mode cluster --class "wordcount.DocumentsMatrix" target/scala-2.11/wordcount_2.11-1.1.3.jar
* 4 The dataset is stored by default in the HDFS "/datasets/gutenberg-txt-es/*.txt" and the output will be stored as a textfile in the HDFS "/user/jrincon5/out_matrix_similarity" Note: You can change the paths if you have to.
* 5 After finishing the spark process, you have to run the script with the spark process output as a parameter: $ python script // Note: You have to move the spark output to the same path to the script.
* 6 After runing the python script you will have the database in cassandra like this [doc | {(doc,similarity),...}]
* 7 Finally you have to run the rails app: $ rails s -p 3000
* 8 In this step you will have finished the execution project.
