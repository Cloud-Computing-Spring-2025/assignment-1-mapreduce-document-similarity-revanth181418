package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parts = line.split(" ", 2); // Split into DocumentID and content
       
        if (parts.length < 2) {
            return; // Ignore empty or malformed lines
        }

        String documentId = parts[0];
        String content = parts[1];

        Set<String> words = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(content);
       
        while (tokenizer.hasMoreTokens()) {
            words.add(tokenizer.nextToken().toLowerCase());
        }

        context.write(new Text(documentId), new Text(String.join(",", words)));
    }
}