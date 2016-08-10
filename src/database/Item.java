package database;

public class Item {

    public final int modelNumber;
    public final String itemName;
    public String category;
    public String attr1;
    public String attr2;
    public String attr3;

    public Item (int modelNumber, String itemName, String category, String attr1, String attr2, String attr3) {
        this.modelNumber = modelNumber;
        this.itemName = itemName;
        this.category = category;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
    }

}