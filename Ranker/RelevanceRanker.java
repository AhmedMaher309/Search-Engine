import java.util.*;

public class RelevanceRanker {
    private Map<String, Map<String, Integer>> documentTermFrequency;
    private Map<String, Integer> documentFrequency;
    private List<String> documents;
    private Map<String, Map<String, Double>> tfidfScores;

    public RelevanceRanker() {
        documentTermFrequency = new HashMap<>();
        documentFrequency = new HashMap<>();
        documents = new ArrayList<>();
        tfidfScores = new HashMap<>();
    }

    public void addDocument(String documentName, List<String> terms) {
        Map<String, Integer> termFrequency = new HashMap<>();

        // Count term frequency in the document
        for (String term : terms) {
            termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
        }

        // Update document term frequency
        documentTermFrequency.put(documentName, termFrequency);

        // Update document frequency
        for (String term : termFrequency.keySet()) {
            documentFrequency.put(term, documentFrequency.getOrDefault(term, 0) + 1);
        }

        // Add document to the list
        documents.add(documentName);
    }

    public void calculateTFIDF() {
        for (String document : documents) {
            Map<String, Double> tfidfScoresForDocument = new HashMap<>();
            Map<String, Integer> termFrequency = documentTermFrequency.get(document);
            int totalTerms = termFrequency.values().stream().mapToInt(Integer::intValue).sum();

            for (String term : termFrequency.keySet()) {
                int termFreq = termFrequency.get(term);
                int docFreq = documentFrequency.get(term);

                // Calculate term frequency-inverse document frequency (TF-IDF)
                double tfidf = (double) termFreq / totalTerms * Math.log((double) documents.size() / docFreq);
                tfidfScoresForDocument.put(term, tfidf);
            }

            tfidfScores.put(document, tfidfScoresForDocument);
        }
    }

    public List<String> getRankedDocuments(String query, int topK) {
        Map<String, Double> queryTfidfScores = new HashMap<>();
        Map<String, Double> documentScores = new HashMap<>();

        // Calculate TF-IDF scores for query
        String[] queryTerms = query.toLowerCase().split("\\s+");
        int totalTerms = queryTerms.length;

        for (String term : queryTerms) {
            queryTfidfScores.put(term, (double) Collections.frequency(Arrays.asList(queryTerms), term) / totalTerms);
        }

        // Calculate relevance scores for each document
        for (String document : documents) {
            Map<String, Double> documentTfidfScores = tfidfScores.get(document);

            double relevanceScore = 0.0;
            for (String term : queryTfidfScores.keySet()) {
                if (documentTfidfScores.containsKey(term)) {
                    relevanceScore += queryTfidfScores.get(term) * documentTfidfScores.get(term);
                }
            }

            documentScores.put(document, relevanceScore);
        }

        // Sort documents by relevance score in descending order
        List<String> rankedDocuments = new ArrayList<>(documentScores.keySet());
        rankedDocuments.sort(Comparator.comparingDouble(documentScores::get).reversed());

        // Return the top K ranked documents
        return rankedDocuments.subList(0, Math.min(topK, rankedDocuments.size()));
    }

    public static void main(String[] args) {
        RelevanceRanker ranker = new RelevanceRanker();

        // Add sample documents
        ranker.addDocument("document1", Arrays.asList("apple", "banana", "apple", "orange"));
        ranker.addDocument("document2", Arrays.asList("banana", "orange", "orange"));
        ranker.addDocument("document3", Arrays.asList("apple", "apple", "grape"));

        // Calculate TF-IDF scores
        ranker.calculateTFIDF();

        // Perform a query and get the ranked documents
        String query = "apple orange";
        int topK = 2;
        List<String> rankedDocuments = ranker.getRankedDocuments(query, topK);

        // Print the ranked documents
        System.out.println("Ranked documents for query: " + query);
        for (String document : rankedDocuments) {
            System.out.println(document);
        }
    }
}
