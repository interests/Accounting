package ru.avisprof.accounting.records;

import java.util.Date;

import ru.avisprof.accounting.categories.Category;

/**
 * Created by Leonid on 20.04.2016.
 */
public class Record {

    private int id;
    private Category category;
    private Date date;
    private double sum;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public int getCategoryId() {
        if (category == null) {
            return -1;
        }
        return category.getId();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
