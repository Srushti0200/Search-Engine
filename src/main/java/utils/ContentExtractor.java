package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ContentExtractor {
    private final static String JOB_NAME = "content extractor";

    /**
     * Reads list of N relevant id strings
     *
     * @param N number of documents
     * @return list of ids
     * @throws IOException
     */
    private static ArrayList<String> readNRelevantIds(int N, Configuration conf) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        FileSystem fs = FileSystem.get(conf);
        try (FSDataInputStream fileWithIDRank = fs.open(new Path(Paths.CE_IDS))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileWithIDRank));
            String line = br.readLine();
            int iter = 0;
            while (line != null && iter < N) {
                result.add(line);
                iter++;
                line = br.readLine();
            }
        }
        return result;
    }

    /**
     * Reads URLs associated with document IDs
     *
     * @return mapping of IDs to URLs
     * @throws IOException
     */
    private static HashMap<String, String> readUrls(Configuration conf) throws IOException {
        HashMap<String, String> idUrlMap = new HashMap<>();
        FileSystem fs = FileSystem.get(conf);
        try (FSDataInputStream fileWithUrls = fs.open(new Path(Paths.CE_URLS))) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileWithUrls));
            String line = br.readLine();
            ObjectMapper mapper = new ObjectMapper();
            while (line != null) {
                JsonNode node = mapper.readTree(line);
                String id = node.get("id").asText();
                String url = node.get("url").asText();
                idUrlMap.put(id, url);
                line = br.readLine();
            }
        }
        return idUrlMap;
    }

    /**
     * Runs the MapReduce
     *
     * @param args 1 parameter N, arg 2 number of relevant IDs to be shown
     */
    public static int run(String[] args, Configuration conf) throws Exception {
        int N = Integer.parseInt(args[1]);
        ArrayList<String> relevantIds = readNRelevantIds(N, conf);
        HashMap<String, String> idUrlMap = readUrls(conf);
       
        System.out.println("ID  |   Title   | Rank | URL");
        for (String idRank : relevantIds) {
            String[] parts = idRank.split(" ");
            String id = parts[0];
            String title = parts[1];
            String rank = parts[2];
            String url = idUrlMap.getOrDefault(id, "URL not found");
            System.out.println(id + " | " + title + " | " + rank + " | " + url);
        }
        return 0;
    }
}


