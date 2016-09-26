package entitys;

public class RmTypeData7 {

    private int type;
    private int coverImgId;
    private int number;
    private String title;
    private String subTitle;

    public RmTypeData7(int type, String title) {
        this.type = type;
        this.title = title;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getCoverImgId() {
        return coverImgId;
    }

    public void setCoverImgId(int coverImgId) {
        this.coverImgId = coverImgId;
    }

    public int getHotNumber() {
        return number;
    }

    public void setHotNumber(int hotNumber) {
        this.number = hotNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

}
