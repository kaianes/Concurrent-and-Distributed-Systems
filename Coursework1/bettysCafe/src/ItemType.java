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
