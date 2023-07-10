import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class PopularityRanker {
    private Vector<PageInfo> pages;
    private static final int MAX_ITERATION = 2;

    public PopularityRanker(Vector<PageInfo> pages) {
        this.pages = pages;
    }

    private void setPageRank() {
        for (int i = 0; i < pages.size(); i++) {
            double score = 0.0;
            for (int j = 0; j < pages.size(); j++) {
                if (pages.get(j).getLinks().contains(pages.get(i).getUrl())) {
                    score += (pages.get(j).getLastScore() / pages.get(j).getLinks().size());
                }
            }
            pages.get(i).setCurrentScore(score);
        }
    
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setLastScore(pages.get(i).getCurrentScore());
        }
    }
    
    public void setPopularityRank() {
        double firstScore = 1.0 / pages.size();
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setLastScore(firstScore);
            pages.get(i).setCurrentScore(firstScore);
        }
    
        for (int i = 0; i < MAX_ITERATION; i++) {
            setPageRank();
        }
    }
    
  public static void main(String[] args) {
    // Create sample PageInfo objects
    PageInfo page1 = new PageInfo("page1", 0.25);
    PageInfo page2 = new PageInfo("page2", 0.25);
    PageInfo page3 = new PageInfo("page3", 0.25);
    PageInfo page4 = new PageInfo("page4", 0.25);

    // Set links for the pages
    page1.addLink("page2");
    page1.addLink("page3");
    page2.addLink("page4");
    page3.addLink("page1");
    page3.addLink("page2");
    page3.addLink("page4");
    page4.addLink("page3");

    // Create a vector to hold PageInfo objects
    Vector<PageInfo> pages = new Vector<>();
    pages.add(page1);
    pages.add(page2);
    pages.add(page3);
    pages.add(page4);

    // Create an instance of PopularityRanker
    PopularityRanker ranker = new PopularityRanker(pages);

    // Calculate the popularity rank
    ranker.setPopularityRank();
    Collections.sort(pages, Comparator.comparingDouble(PageInfo::getCurrentScore).reversed());
    // Print the final scores
    for (PageInfo page : pages) {
        System.out.println("Page: " + page.getUrl() + ", Score: " + page.getCurrentScore());
    }
}
}


