public class Account {

    String holder;
    double balance;

    public Account(String name) {
        holder = name;
        balance = 0.0;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public double withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return amount;
        } else {
            return 0;
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getHolder() {
        return holder;
    }

}
