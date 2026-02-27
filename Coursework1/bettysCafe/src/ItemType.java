// This file defines the ItemType enum, which represents the different types of items available at Betty's Cafe. Each item type has a singular and plural name for proper grammar in logging and output.

public enum ItemType {
    CAKE("cake", "cakes"),
    TEA("tea", "teas"),
    COFFEE("coffee", "coffees");

    private final String singular;
    private final String plural;

    ItemType(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String pluralName(int count) {
        return count == 1 ? singular : plural;
    }
}
