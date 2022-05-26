package organizesports.models;

import java.util.ArrayList;

public class DataModel {
    private String key;
    private String title;
    private String description;
    private String icon;
    private int type; // 1->Exercise, 2->Supplements, 3->Recipes
    private String exerciseKey = "";
    private ArrayList<ElementModel> elements = new ArrayList<>();

    public DataModel() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<ElementModel> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ElementModel> elements) {
        this.elements = elements;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExerciseKey() {
        return exerciseKey;
    }

    public void setExerciseKey(String exerciseKey) {
        this.exerciseKey = exerciseKey;
    }
}
