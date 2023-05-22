package Indexer;

import org.jsoup.nodes.Document;
import org.tartarus.snowball.ext.porterStemmer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class PreProcessor {

    public Map<String, List<String>> processAllDocuments(Map<String,String> documents) throws IOException {
        Map<String, List<String>> siteAndDocumentWords = new HashMap<String, List<String>>();
        for (Map.Entry<String, String> entry : documents.entrySet()){
            List<String> words = splitStringToWords(entry.getValue());
            List<String> stemWords = stemWords(words);
            List<String> stopWordsRemoved = removeStopWords(stemWords, "stopwords.txt");
            siteAndDocumentWords.put(entry.getKey(), stopWordsRemoved);
        }
        return siteAndDocumentWords;
    }

    public List<String> stemWords(List<String> words){
        porterStemmer stemmer = new porterStemmer();
        List<String> stemmedWords = new ArrayList<>();

        // Stem each word and add it to the list of stemmed words
        for (String word : words) {
            stemmer.setCurrent(word);
            stemmer.stem();
            String stemmedWord = stemmer.getCurrent();
            stemmedWords.add(stemmedWord);
        }
        return stemmedWords;
    }

    public List<String> splitStringToWords(String text) {
        List<String> words = new ArrayList<>();
        String[] tokens = text.toLowerCase().split("\\s+|\\p{Punct}+");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }

    public List<String> removeStopWords(List<String> words, String stopwordsFile) throws IOException {
        List<String> filteredWords = new ArrayList<>();
        List<String> stopWords = readStopWords(stopwordsFile);

        for (String word : words) {
            if (!stopWords.contains(word)) {
                filteredWords.add(word);
            }
        }
        return filteredWords;
    }

    private List<String> readStopWords(String stopwordsFile) throws IOException {
        List<String> stopWords = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(stopwordsFile));
        String line;

        while ((line = reader.readLine()) != null) {
            stopWords.add(line.trim().toLowerCase());
        }

        reader.close();
        return stopWords;
    }

    public void printer(List<String> words){
        for(int i=0; i<words.size(); i++){
            System.out.println(words.get(i));
        }

    }
}
