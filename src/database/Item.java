package database;

public class Item {

    private final int modelNumber;
    private final String itemName;
    private String category;
    private String attr1;
    private String attr2;
    private String attr3;

    public Item (int modelNumber, String itemName, String category, String attr1, String attr2, String attr3) {
        this.modelNumber = modelNumber;
        this.itemName = itemName;
        this.category = category;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.attr3 = attr3;
    }

}