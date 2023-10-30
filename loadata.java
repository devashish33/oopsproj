import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class loadata 
{
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String OWNERS_FILE = "owners.txt";

    public static void main(String[] args) 
    {
        ArrayList<Customer> customers = loadCustomers();
        ArrayList<Owner> owners = loadOwners();

        // Other parts of your program

        // Example: Printing loaded customers
       for (Customer customer : customers) {
            System.out.println(customer.getCustomerId() + " - " + customer.getUsername());
        }
    }

    // Load customer data from a file and populate the ArrayList
    private static ArrayList<Customer> loadCustomers() 
    {
        ArrayList<Customer> customers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(CUSTOMERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int customerId = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                double balance = Double.parseDouble(parts[3]);
                Customer customer = new Customer(customerId, username, password, balance);
                customers.add(customer);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading customer data: " + e.getMessage());
        }
        return customers;
    }

    // Load owner data from a file and populate the ArrayList
    private static ArrayList<Owner> loadOwners() {
        ArrayList<Owner> owners = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(OWNERS_FILE))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                Owner owner = new Owner(username, password);
                owners.add(owner);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading owner data: " + e.getMessage());
        }
        return owners;
    }
}
