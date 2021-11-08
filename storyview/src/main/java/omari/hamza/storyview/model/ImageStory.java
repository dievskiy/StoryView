package omari.hamza.storyview.model;

public class ImageStory extends Story {

    private String url;

    public ImageStory(String url, String title, String description) {
        super(title, description);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
