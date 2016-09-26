package entitys;

public class RmTypeData5 {

    private int type;
    private int coverImgId;
    private String bookName;

    public RmTypeData5(int type, String bookName) {
        this.type = type;
        this.bookName = bookName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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

}
