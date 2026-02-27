import java.util.ArrayList;
import java.util.List;

public final class Order {
    // private final - ensures immutability and thread safety, as the order details cannot be changed after creation.
    private final int cakes;
    private final int teas;
    private final int coffees;

    public Order(int cakes, int teas, int coffees) {

        // order validation 
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

    // Provides a human-readable description of the order
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

    // This help to understand the order when logging and debugging
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
