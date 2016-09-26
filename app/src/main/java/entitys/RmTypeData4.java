package entitys;

public class RmTypeData4 {

    private int type;
    private String bookName;
    private int coverImgId;
    private int number;
    private int newstChapter;

    public RmTypeData4(int type, String bookName) {
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

    public int getHotNumber() {
        return number;
    }

    public void setHotNumber(int hotNumber) {
        this.number = hotNumber;
    }

    public int getNewstChapter() {
        return newstChapter;
    }

    public void setNewstChapter(int newstChapter) {
        this.newstChapter = newstChapter;
    }
}
