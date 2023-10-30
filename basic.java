import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}

class Customer extends User {
    private int customerId;
    private double balance;
    private int mealsTaken;

    public Customer(int customerId, String username, String password, double balance) {
        super(username, password);
        this.customerId = customerId;
        this.balance = balance;
        this.mealsTaken = 0;
    }

    public void viewAccount() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Balance: $" + balance);
        System.out.println("Meals Taken: " + mealsTaken);
    }

    public void cancelMeal() {
        // Implement meal cancellation logic here
    }
}

class Owner extends User {
    public Owner(String username, String password) {
        super(username, password);
    }

    public void viewCustomerDetails() {
        // Implement code to view customer details
    }

    public void viewBookedMealsForDate(String date) {
        // Implement code to view meals booked for a specific date
    }
}

public class basic {
    public static void main(String[] args) {
        // Sample code to create and manage users
        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<Owner> owners = new ArrayList<>();

        // Load customer and owner data from files and populate the ArrayLists

        // Sample login process
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User currentUser = null;

        for (Customer customer : customers) {
            if (customer.login(username, password)) {
                currentUser = customer;
                break;
            }
        }

        for (Owner owner : owners) {
            if (owner.login(username, password)) {
                currentUser = owner;
                break;
            }
        }

        if (currentUser instanceof Customer) {
            // Customer specific functionality
            Customer customer = (Customer) currentUser;
            customer.viewAccount();
            // Implement other customer operations
        } else if (currentUser instanceof Owner) {
            // Owner specific functionality
            Owner owner = (Owner) currentUser;
            // Implement owner operations
        } else {
            System.out.println("Invalid login credentials.");
        }
    }
}
