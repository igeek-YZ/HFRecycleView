package entitys;

import java.io.Serializable;

/**
 * Created by User on 16/7/3.
 */
public class RandomEntity implements Serializable {

    private String title;
    private int height;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
