import java.util.ArrayList;
import java.util.List;

public class PageInfo {
    private String url;
    private double currentScore;
    private double lastScore;
    private List<String> links;

    public PageInfo(String url, double initialScore) {
        this.url = url;
        this.currentScore = initialScore;
        this.lastScore = initialScore;
        this.links = new ArrayList<>();
    }

    public String getUrl() {
        return url;
    }

    public double getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(double score) {
        this.currentScore = score;
    }

    public double getLastScore() {
        return lastScore;
    }

    public void setLastScore(double score) {
        this.lastScore = score;
    }

    public List<String> getLinks() {
        return links;
    }

    public void addLink(String link) {
        links.add(link);
    }
}
