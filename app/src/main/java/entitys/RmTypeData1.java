package entitys;

public class RmTypeData1 {

    private int type;
    private String url;
    private String title;

    public RmTypeData1(int type, String title) {
        this.type=type;
        this.title = title;
    }

    public RmTypeData1(int type, String url, String title) {
        this.type=type;
        this.url = url;
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
