package organizesports.models;

import java.util.ArrayList;

public class ElementModel {
    private String key;
    private ArrayList<String> content = new ArrayList<>();
    private int type; // 1->Title, 2->Image, 3->Video, 4->Content, 5->Images, 6->Separator


    public ElementModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
