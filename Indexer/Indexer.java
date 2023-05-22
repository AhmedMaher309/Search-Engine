package Indexer;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Indexer {
    public static void main(String[] args) throws IOException {
        DocumentDownloader downloader = new DocumentDownloader();
        Map<String, Document> documents = DocumentDownloader.downloadWebsitesFromTxtFile();
        Map<String,String> docsWithNames = downloader.getTextByTagForAllDocuments(documents);
        System.out.println("\n\n");

        downloader.print(documents);
        PreProcessor processor = new PreProcessor();
        Map<String, List<String>> output = processor.processAllDocuments(docsWithNames);

        DBDriver driver = new DBDriver();
        Map<String, List<DocumentInDB>> DB= driver.makeTable(output);
        driver.printBDMap(DB);
        driver.insertInDb(DB);
    }
}
