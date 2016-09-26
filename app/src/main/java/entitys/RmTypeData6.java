package entitys;

import java.util.List;

public class RmTypeData6 {

    private int type;
    private List<Integer> coverImgIds;

    public RmTypeData6(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public List<Integer> getCoverImgIds() {
        return coverImgIds;
    }

    public void setCoverImgIds(List<Integer> coverImgIds) {
        this.coverImgIds = coverImgIds;
    }

}
