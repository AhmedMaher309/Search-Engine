package Indexer;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.util.*;

public class DBDriver {

    public void insertInDb(Map<String, List<DocumentInDB>> table) {
        MongoClientURI uri = new MongoClientURI(System.getenv("INDEXER_DB"));
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase database = mongoClient.getDatabase("Indexer");
        MongoCollection<Document> collection = database.getCollection("indeces");

        for (Map.Entry<String, List<DocumentInDB>> entry : table.entrySet()) {
            String key = entry.getKey();
            BasicDBList documentList = new BasicDBList(); // Create a new BasicDBList
            Document doc = new Document()
                    .append("key", key)
                    .append("DF", entry.getValue().size());
            for (DocumentInDB document : entry.getValue()) {
                BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
                builder.add("name", document.name);
                builder.add("TF", document.TF);
                // Add more fields as needed
                documentList.add(builder.get()); // Add the BasicDBObject to the BasicDBList
            }
            doc.append("documents", documentList); // Add the BasicDBList to the main Document

            collection.insertOne(doc);
        }
        mongoClient.close();
    }


    public Map<String, List<DocumentInDB>> makeTable(Map<String, List<String>> DocumentNameWithFinalWords) {
        Map<String, List<DocumentInDB>> DBMap = new HashMap<String, List<DocumentInDB>>();
        for (Map.Entry<String, List<String>> entry : DocumentNameWithFinalWords.entrySet()) {
            for (String word : entry.getValue()) {
                List<DocumentInDB> documents = DBMap.get(word);
                documents = getDocumentsListForWord(DocumentNameWithFinalWords, word);
                DBMap.put(word, documents);
            }
        }
        return DBMap;
    }

    public List<DocumentInDB> getDocumentsListForWord(Map<String, List<String>> DocumentNameWithFinalWords, String keyword) {
        List<DocumentInDB> documentListPerWord = new ArrayList<DocumentInDB>();
        for (Map.Entry<String, List<String>> entry : DocumentNameWithFinalWords.entrySet()) {
            DocumentInDB d = createDocumentEntryForWord(entry.getKey(),entry.getValue(), keyword);
            if(d.TF != 0){
                 documentListPerWord.add(d);
            }
        }
        return documentListPerWord;
    }
    public DocumentInDB createDocumentEntryForWord (String name, List<String> document, String keyword){
        DocumentInDB D = new DocumentInDB();
        for (int i = 0; i < document.size(); i++){
            if (Objects.equals(document.get(i), keyword)){
                D.TF++;
            }
        }
        D.name = name;
        return D;
    }
    public void printBDMap(Map<String, List<DocumentInDB>> DBMap){
        for (Map.Entry<String, List<DocumentInDB>> entry : DBMap.entrySet()){
            System.out.println(entry.getKey());
            for(int i=0; i<entry.getValue().size(); i++){
                System.out.println(entry.getValue().get(i).name + " ---> " + entry.getValue().get(i).TF);
            }
            System.out.println("\n\n");
        }

    }

}
