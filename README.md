[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-2e0aaae1b6195c2367325f4f02e2d04e9abb55f0b24a779b69b11b9e10269abc.svg)](https://classroom.github.com/online_ide?assignment_repo_id=18028232&assignment_repo_type=AssignmentRepo)
### **📌 Document Similarity Using Hadoop MapReduce**  

#### **Objective**  
The goal of this assignment is to compute the **Jaccard Similarity** between pairs of documents using **MapReduce in Hadoop**. You will implement a MapReduce job that:  
1. Extracts words from multiple text documents.  
2. Identifies which words appear in multiple documents.  
3. Computes the **Jaccard Similarity** between document pairs.  
4. Outputs document pairs with similarity **above 50%**.  

---

### **📥 Example Input**  

You will be given multiple text documents. Each document will contain several words. Your task is to compute the **Jaccard Similarity** between all pairs of documents based on the set of words they contain.  

#### **Example Documents**  

##### **doc1.txt**  
```
hadoop is a distributed system
```

##### **doc2.txt**  
```
hadoop is used for big data processing
```

##### **doc3.txt**  
```
big data is important for analysis
```

---

# 📏 Jaccard Similarity Calculator

## Overview

The Jaccard Similarity is a statistic used to gauge the similarity and diversity of sample sets. It is defined as the size of the intersection divided by the size of the union of two sets.

## Formula

The Jaccard Similarity between two sets A and B is calculated as:

```
Jaccard Similarity = |A ∩ B| / |A ∪ B|
```

Where:
- `|A ∩ B|` is the number of words common to both documents
- `|A ∪ B|` is the total number of unique words in both documents

## Example Calculation

Consider two documents:
 
**doc1.txt words**: `{hadoop, is, a, distributed, system}`
**doc2.txt words**: `{hadoop, is, used, for, big, data, processing}`

- Common words: `{hadoop, is}`
- Total unique words: `{hadoop, is, a, distributed, system, used, for, big, data, processing}`

Jaccard Similarity calculation:
```
|A ∩ B| = 2 (common words)
|A ∪ B| = 10 (total unique words)

Jaccard Similarity = 2/10 = 0.2 or 20%
```

## Use Cases

Jaccard Similarity is commonly used in:
- Document similarity detection
- Plagiarism checking
- Recommendation systems
- Clustering algorithms

## Implementation Notes

When computing similarity for multiple documents:
- Compare each document pair
- Output pairs with similarity > 50%

### **📤 Expected Output**  

The output should show the Jaccard Similarity between document pairs in the following format:  
```
(doc1, doc2) -> 60%  
(doc2, doc3) -> 50%  
```

---

### **🛠 Environment Setup: Running Hadoop in Docker**  

Since we are using **Docker Compose** to run a Hadoop cluster, follow these steps to set up your environment.  

#### **Step 1: Install Docker & Docker Compose**  
- **Windows**: Install **Docker Desktop** and enable WSL 2 backend.  
- **macOS/Linux**: Install Docker using the official guide: [Docker Installation](https://docs.docker.com/get-docker/)  

#### **Step 2: Start the Hadoop Cluster**  
Navigate to the project directory where `docker-compose.yml` is located and run:  
```sh
docker-compose up -d
```  
This will start the Hadoop NameNode, DataNode, and ResourceManager services.  

#### **Step 3: Access the Hadoop Container**  
Once the cluster is running, enter the **Hadoop master node** container:  
```sh
docker exec -it hadoop-master /bin/bash
```

---

### **📦 Building and Running the MapReduce Job with Maven**  

#### **Step 1: Build the JAR File**  
Ensure Maven is installed, then navigate to your project folder and run:  
```sh
mvn clean package
```  
This will generate a JAR file inside the `target` directory.  

#### **Step 2: Copy the JAR File to the Hadoop Container**  
Move the compiled JAR into the running Hadoop container:  
```sh
docker cp target/similarity.jar hadoop-master:/opt/hadoop-3.2.1/share/hadoop/mapreduce/similarity.jar
```

---

### **📂 Uploading Data to HDFS**  

#### **Step 1: Create an Input Directory in HDFS**  
Inside the Hadoop container, create the directory where input files will be stored:  
```sh
hdfs dfs -mkdir -p /input
```

#### **Step 2: Upload Dataset to HDFS**  
Copy your local dataset into the Hadoop cluster’s HDFS:  
```sh
hdfs dfs -put /path/to/local/input/* /input/
```

---

### **🚀 Running the MapReduce Job**  

Run the Hadoop job using the JAR file inside the container:  
```sh
hadoop jar similarity.jar DocumentSimilarityDriver /input /output_similarity /output_final
```

---

### **📊 Retrieving the Output**  

To view the results stored in HDFS:  
```sh
hdfs dfs -cat /output_final/part-r-00000
```

If you want to download the output to your local machine:  
```sh
hdfs dfs -get /output_final /path/to/local/output
```
---
###  Approach and Implementation

This project follows a three-step MapReduce approach:

1. Mapper: Extract Words from Documents

Reads input lines and extracts words, associating them with their respective documents.

Emits key-value pairs (word, document_id).

2. Reducer: Build an Inverted Index

Groups documents by shared words.

Outputs document lists where common words appear.

3. Mapper: Generate Document Pairs

Forms all possible document pairs from shared words.

Emits key-value pairs (doc_pair, 1) for each occurrence.

4. Reducer: Compute Jaccard Similarity

Counts shared words between documents.

Calculates Jaccard Similarity using the formula.

Outputs document pairs with similarity > 50%.

### step-by-step instructions

@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ docker compose up -d
WARN[0000] /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/docker-compose.yml: the attribute `version` is obsolete, it will be ignored, please remove it to avoid potential confusion 
[+] Running 9/0
 ✔ Container nodemanager2     Running                                                                     0.0s 
 ✔ Container datanode1        Running                                                                     0.0s 
 ✔ Container resourcemanager  Running                                                                     0.0s 
 ✔ Container nodemanager3     Running                                                                     0.0s 
 ✔ Container datanode3        Running                                                                     0.0s 
 ✔ Container datanode2        Running                                                                     0.0s 
 ✔ Container historyserver    Running                                                                     0.0s 
 ✔ Container namenode         Running                                                                     0.0s 
 ✔ Container nodemanager1     Running                                                                     0.0s 
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ mvn install
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.example:DocumentSimilarity >-------------------
[INFO] Building DocumentSimilarity 0.0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[WARNING] 2 problems were encountered while building the effective model for org.apache.yetus:audience-annotations:jar:0.5.0 during dependency collection step for project (use -X to see details)
[INFO] 
[INFO] --- resources:3.3.1:resources (default-resources) @ DocumentSimilarity ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/src/main/resources
[INFO] 
[INFO] --- compiler:3.13.0:compile (default-compile) @ DocumentSimilarity ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] 
[INFO] --- resources:3.3.1:testResources (default-testResources) @ DocumentSimilarity ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/src/test/resources
[INFO] 
[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ DocumentSimilarity ---
[INFO] No sources to compile
[INFO] 
[INFO] --- surefire:3.2.5:test (default-test) @ DocumentSimilarity ---
[INFO] 
[INFO] --- jar:3.4.1:jar (default-jar) @ DocumentSimilarity ---
[INFO] Building jar: /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/target/DocumentSimilarity-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- install:3.1.2:install (default-install) @ DocumentSimilarity ---
[INFO] Installing /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/pom.xml to /home/codespace/.m2/repository/com/example/DocumentSimilarity/0.0.1-SNAPSHOT/DocumentSimilarity-0.0.1-SNAPSHOT.pom
[INFO] Installing /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/target/DocumentSimilarity-0.0.1-SNAPSHOT.jar to /home/codespace/.m2/repository/com/example/DocumentSimilarity/0.0.1-SNAPSHOT/DocumentSimilarity-0.0.1-SNAPSHOT.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.891 s
[INFO] Finished at: 2025-02-15T00:29:04Z
[INFO] ------------------------------------------------------------------------
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
Successfully copied 8.19kB to resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ docker cp dataset/documents.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
Successfully copied 2.05kB to resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ docker exec -it resourcemanager /bin/bash
root@bab1392e8fcd:/# cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop fs -mkdir -p /input/dataset
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop fs -put ./documents.txt /input/dataset
put: `/input/dataset/documents.txt': File exists
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input/dataset/documents.txt /output
2025-02-15 00:30:20,168 INFO client.RMProxy: Connecting to ResourceManager at resourcemanager/172.18.0.2:8032
2025-02-15 00:30:20,363 INFO client.AHSProxy: Connecting to Application History server at historyserver/172.18.0.8:10200
Exception in thread "main" org.apache.hadoop.mapred.FileAlreadyExistsException: Output directory hdfs://namenode:9000/output already exists
        at org.apache.hadoop.mapreduce.lib.output.FileOutputFormat.checkOutputSpecs(FileOutputFormat.java:164)
        at org.apache.hadoop.mapreduce.JobSubmitter.checkSpecs(JobSubmitter.java:277)
        at org.apache.hadoop.mapreduce.JobSubmitter.submitJobInternal(JobSubmitter.java:143)
        at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1570)
        at org.apache.hadoop.mapreduce.Job$11.run(Job.java:1567)
        at java.security.AccessController.doPrivileged(Native Method)
        at javax.security.auth.Subject.doAs(Subject.java:422)
        at org.apache.hadoop.security.UserGroupInformation.doAs(UserGroupInformation.java:1730)
        at org.apache.hadoop.mapreduce.Job.submit(Job.java:1567)
        at org.apache.hadoop.mapreduce.Job.waitForCompletion(Job.java:1588)
        at com.example.controller.DocumentSimilarityDriver.main(DocumentSimilarityDriver.java:33)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.lang.reflect.Method.invoke(Method.java:498)
        at org.apache.hadoop.util.RunJar.run(RunJar.java:323)
        at org.apache.hadoop.util.RunJar.main(RunJar.java:236)
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input/dataset/documents.txt /output/output
2025-02-15 00:30:59,576 INFO client.RMProxy: Connecting to ResourceManager at resourcemanager/172.18.0.2:8032
2025-02-15 00:30:59,732 INFO client.AHSProxy: Connecting to Application History server at historyserver/172.18.0.8:10200
2025-02-15 00:30:59,992 WARN mapreduce.JobResourceUploader: Hadoop command-line option parsing not performed. Implement the Tool interface and execute your application with ToolRunner to remedy this.
2025-02-15 00:31:00,026 INFO mapreduce.JobResourceUploader: Disabling Erasure Coding for path: /tmp/hadoop-yarn/staging/root/.staging/job_1739577932302_0002
2025-02-15 00:31:00,233 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
2025-02-15 00:31:00,376 INFO input.FileInputFormat: Total input files to process : 1
2025-02-15 00:31:00,419 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
2025-02-15 00:31:00,444 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
2025-02-15 00:31:00,462 INFO mapreduce.JobSubmitter: number of splits:1
2025-02-15 00:31:00,643 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
2025-02-15 00:31:00,712 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_1739577932302_0002
2025-02-15 00:31:00,712 INFO mapreduce.JobSubmitter: Executing with tokens: []
2025-02-15 00:31:00,891 INFO conf.Configuration: resource-types.xml not found
2025-02-15 00:31:00,892 INFO resource.ResourceUtils: Unable to find 'resource-types.xml'.
2025-02-15 00:31:01,159 INFO impl.YarnClientImpl: Submitted application application_1739577932302_0002
2025-02-15 00:31:01,202 INFO mapreduce.Job: The url to track the job: http://resourcemanager:8088/proxy/application_1739577932302_0002/
2025-02-15 00:31:01,203 INFO mapreduce.Job: Running job: job_1739577932302_0002
2025-02-15 00:31:07,443 INFO mapreduce.Job: Job job_1739577932302_0002 running in uber mode : false
2025-02-15 00:31:07,444 INFO mapreduce.Job:  map 0% reduce 0%
2025-02-15 00:31:13,519 INFO mapreduce.Job:  map 100% reduce 0%
2025-02-15 00:31:19,587 INFO mapreduce.Job:  map 100% reduce 100%
2025-02-15 00:31:19,594 INFO mapreduce.Job: Job job_1739577932302_0002 completed successfully
2025-02-15 00:31:19,670 INFO mapreduce.Job: Counters: 54
        File System Counters
                FILE: Number of bytes read=123
                FILE: Number of bytes written=458019
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
                HDFS: Number of bytes read=255
                HDFS: Number of bytes written=114
                HDFS: Number of read operations=8
                HDFS: Number of large read operations=0
                HDFS: Number of write operations=2
                HDFS: Number of bytes read erasure-coded=0
        Job Counters 
                Launched map tasks=1
                Launched reduce tasks=1
                Rack-local map tasks=1
                Total time spent by all maps in occupied slots (ms)=14300
                Total time spent by all reduces in occupied slots (ms)=18104
                Total time spent by all map tasks (ms)=3575
                Total time spent by all reduce tasks (ms)=2263
                Total vcore-milliseconds taken by all map tasks=3575
                Total vcore-milliseconds taken by all reduce tasks=2263
                Total megabyte-milliseconds taken by all map tasks=14643200
                Total megabyte-milliseconds taken by all reduce tasks=18538496
        Map-Reduce Framework
                Map input records=3
                Map output records=3
                Map output bytes=143
                Map output materialized bytes=115
                Input split bytes=113
                Combine input records=0
                Combine output records=0
                Reduce input groups=3
                Reduce shuffle bytes=115
                Reduce input records=3
                Reduce output records=3
                Spilled Records=6
                Shuffled Maps =1
                Failed Shuffles=0
                Merged Map outputs=1
                GC time elapsed (ms)=96
                CPU time spent (ms)=740
                Physical memory (bytes) snapshot=511049728
                Virtual memory (bytes) snapshot=13499527168
                Total committed heap usage (bytes)=478674944
                Peak Map Physical memory (bytes)=292827136
                Peak Map Virtual memory (bytes)=5076021248
                Peak Reduce Physical memory (bytes)=218222592
                Peak Reduce Virtual memory (bytes)=8423505920
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters 
                Bytes Read=142
        File Output Format Counters 
                Bytes Written=114
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop fs -cat /output/*
cat: `/output/output': Is a directory
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hadoop fs -cat /output/output/*
2025-02-15 00:32:26,030 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
Document3, Document2    Similarity: 0.10
Document3, Document1    Similarity: 0.20
Document2, Document1    Similarity: 0.18
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# hdfhdfs dfs -get /output /opt/hadoop-3.2.1/share/hap/mapreduce/
get: `/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/_SUCCESS': File exists
2025-02-15 00:33:04,625 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
get: `/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/part-r-00000': File exists
root@bab1392e8fcd:/opt/hadoop-3.2.1/share/hadoop/mapreduce# exit 
exit
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
Successfully copied 4.61kB to /workspaces/assignment-1-mapreduce-document-similarity-revanth181418/output/
@revanth181418 ➜ /workspaces/assignment-1-mapreduce-document-similarity-revanth181418 (master) $ 


### Challenges Faced and Solutions
Handling Large Data: Used combiners to optimize data transfer and reduce network overhead.
Duplicate Words in Documents: Used a Set<String> to ensure unique words per document.
Jaccard Similarity Calculation: Ensured correct union computation using unique words across both documents.
Text Formatting Issues: Used regex for word extraction and converted text to lowercase for consistency.
Debugging in Hadoop: Used hadoop logs and printed intermediate outputs for troubleshooting.
Hadoop Setup in Docker: Configured containers properly and resolved network issues.