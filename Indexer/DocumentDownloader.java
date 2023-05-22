package Indexer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DocumentDownloader {

    public static Map<String, Document> downloadWebsitesFromTxtFile() {
        Map<String, Document> documents = new HashMap<>();
        try (Scanner scanner = new Scanner(new File("sites.txt"))) {
            while (scanner.hasNextLine()) {
                String url = scanner.nextLine();
                if (url == null || url.isEmpty()) {
                    continue; // Skip empty or null URLs
                }
                Document doc = Jsoup.connect(url).get();
                String hash = generateHash(url);
                String documentName = url + "   " + hash + ".html";
                documents.put(documentName, doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documents;
    }

    private static String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            String hash = Base64.getEncoder().encodeToString(digest);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public Map<String, String> getTextByTagForAllDocuments(Map<String, Document> documents){
        Map<String, String> documentWithText = new HashMap<>();
        for(Map.Entry<String, Document> doc : documents.entrySet()){
            documentWithText.put(doc.getKey(), getTextByTagsForOneDocument(doc.getValue(), doc.getKey()));
        }
        return documentWithText;
    }

    public String getTextByTagsForOneDocument(Document document, String documentName) {
        Elements paragraphs = document.getElementsByTag("p");
        Elements spans = document.getElementsByTag("span");
        Elements h1s = document.getElementsByTag("h1");
        Elements h2s = document.getElementsByTag("h2");
        Elements h3s = document.getElementsByTag("h3");
        Elements h4s = document.getElementsByTag("h4");
        Elements titles = document.getElementsByTag("title");

        String documentText = "";
        for (Element paragraph : paragraphs) {
            String text = paragraph.text();
            documentText += text + " ";
        }
        for (Element span : spans) {
            String text = span.text();
            documentText += text + " ";
        }
        for (Element h1 : h1s) {
            String text = h1.text();
            documentText += text + " ";
        }
        for (Element h2 : h2s) {
            String text = h2.text();
            documentText += text + " ";
        }
        for (Element h3 : h3s) {
            String text = h3.text();
            documentText += text + " ";
        }
        for (Element h4 : h4s) {
            String text = h4.text();
            documentText += text + " ";
        }
        for (Element title : titles) {
            String text = title.text();
            documentText += text + " ";
        }
        return documentText;
    }

    public void print(Map<String, Document> doc) {
        for (Map.Entry<String, Document> entry : doc.entrySet()) {
            String documentName = entry.getKey();
            Document documentObject = entry.getValue();
            System.out.println("Document name: " + documentName);
        }
    }
}

