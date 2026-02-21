import java.util.ArrayList;
import java.util.List;

public final class Order {
    private final int cakes;
    private final int teas;
    private final int coffees;

    public Order(int cakes, int teas, int coffees) {
        if (cakes < 0 || teas < 0 || coffees < 0) {
            throw new IllegalArgumentException("Order values cannot be negative.");
        }
        if (cakes == 0 && teas == 0 && coffees == 0) {
            throw new IllegalArgumentException("Order cannot be empty.");
        }
        this.cakes = cakes;
        this.teas = teas;
        this.coffees = coffees;
    }

    public int cakes() {
        return cakes;
    }

    public int teas() {
        return teas;
    }

    public int coffees() {
        return coffees;
    }

    public boolean hasCake() {
        return cakes > 0;
    }

    public String describe() {
        List<String> parts = new ArrayList<>();
        if (teas > 0) {
            parts.add("tea");
        }
        if (coffees > 0) {
            parts.add("coffee");
        }
        if (cakes > 0) {
            parts.add("cake");
        }

        if (parts.size() == 1) {
            return parts.get(0);
        }
        return parts.get(0) + " and " + parts.get(1);
    }

    public static Order coffeeOnly() {
        return new Order(0, 0, 1);
    }

    public static Order teaOnly() {
        return new Order(0, 1, 0);
    }

    public static Order teaAndCake() {
        return new Order(1, 1, 0);
    }

    public static Order coffeeAndCake() {
        return new Order(1, 0, 1);
    }

    public static Order cakeOnly() {
        return new Order(1, 0, 0);
    }
}
