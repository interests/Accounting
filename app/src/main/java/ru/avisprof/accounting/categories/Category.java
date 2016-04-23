package ru.avisprof.accounting.categories;

/**
 * Created by Leonid on 20.04.2016.
 */
public class Category {

    private int id;
    private String name;
    private int imageId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        if (name == null) {
            return "";
        }
        return name ;
    }
}
