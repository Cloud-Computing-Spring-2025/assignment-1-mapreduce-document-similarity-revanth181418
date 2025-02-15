package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {
    private final Map<String, Set<String>> documentWords = new HashMap<>();

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text val : values) {
            Set<String> wordSet = new HashSet<>(Arrays.asList(val.toString().split(",")));
            documentWords.put(key.toString(), wordSet);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        List<String> documentIds = new ArrayList<>(documentWords.keySet());

        for (int i = 0; i < documentIds.size(); i++) {
            for (int j = i + 1; j < documentIds.size(); j++) {
                String doc1 = documentIds.get(i);
                String doc2 = documentIds.get(j);

                Set<String> words1 = documentWords.get(doc1);
                Set<String> words2 = documentWords.get(doc2);

                Set<String> intersection = new HashSet<>(words1);
                intersection.retainAll(words2);

                Set<String> union = new HashSet<>(words1);
                union.addAll(words2);

                double similarity = (double) intersection.size() / union.size();
                String result = String.format("Similarity: %.2f", similarity);

                context.write(new Text(doc1 + ", " + doc2), new Text(result));
            }
        }
    }
}