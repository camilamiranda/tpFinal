package org.miranda;

public class Item {

    public int id;
    public String type;
    public String name;
    public State state;
    public String color;
    public String material;
    public String details;
    public String season;
    public String givenBy;
    public String imagePath;

    public Item(String nameItem) {
        name = nameItem;
        state = State.propre;
    }
}
