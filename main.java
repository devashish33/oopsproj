import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
class User 
{
    private String username;
    private String password;

    public User(String username, String password) 
    {
        this.username = username;
        this.password = password;
    }
    public boolean login(String username, String password) 
    {
        return this.username.equals(username) && this.password.equals(password);
    }
}
class Customer extends User 
{
    private int customerId;
    private double balance;
    private int mealsTaken;
    public Customer(int customerId, String username, String password, double balance) 
    {  
        super(username, password);
        this.customerId = customerId;
        this.balance = balance;
        this.mealsTaken = 0;
        try 
        {
            File myObj = new File("customer"+customerId+".txt");
            if (myObj.createNewFile()) 
            {

            } 
            else 
            {
                System.out.println("Customer File already exists.");
            }
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void viewAccount() 
    {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Balance: Rs." + balance);
        System.out.println("Meals Taken: " + mealsTaken);
    }

    public void cancelMeal() 
    {
        // Implement meal cancellation logic here
    }
    public int getCustomerId() 
    {
        return customerId;
    }
}
class Owner extends User 
{
    public Owner(String username, String password) 
    {
        super(username, password);
    }

    public void viewCustomerDetails() 
    {
        // Implement code to view customer details
        /*for (Customer customer : customers)
        {
            System.out.println(customer.getCustomerId() + " - " + customer.getUsername());
        }*/
    }
    public void viewBookedMealsForDate(String date) 
    {
        // Implement code to view meals booked for a specific date
    }
}
public class main 
{
    public static void main(String[] args) 
    {
        //String CUSTOMERS_FILE = "customers.txt";
        //String OWNERS_FILE = "owners.txt";
        // Sample code to create and manage users
        ArrayList<Customer> customers = loadCustomers();
        ArrayList<Owner> owners = loadOwners();

        // Load customer and owner data from files and populate the ArrayLists
        // Sample login process
        System.out.print("Enter Userstatus-1.Existing user 2.New user : ");
        Scanner scanner = new Scanner(System.in);
        int status = scanner.nextInt();
        if (status == 1)
        {
            System.out.print("Enter username: ");
            String username = scanner.next();
            System.out.print("Enter password: ");
            String password = scanner.next();
            User currentUser = null;
            for (Customer customer : customers) 
            {
                if (customer.login(username, password)) 
                {
                    currentUser = customer;
                    break;
                }
            }

            for (Owner owner : owners) 
            {
                if (owner.login(username, password)) 
                {
                    currentUser = owner;
                    break;
                }
            }
            if (currentUser instanceof Customer) 
            {
                // Customer specific functionality
                Customer customer = (Customer) currentUser;
                customer.viewAccount();
                System.out.println("you are a customer");
                // Implement other customer operations
            } 
            else if (currentUser instanceof Owner) 
            {
                // Owner specific functionality
                Owner owner = (Owner) currentUser;
                System.out.println("you are a owner");
                owner.viewCustomerDetails();
                // Implement owner operations
            }
            else 
            {
                System.out.println("Invalid login credentials.");
            }
        }
        else if(status == 2)
        {
            System.out.println("Enter details for new user");
            System.out.print("Enter a customer id: ");
            int customer_id = scanner.nextInt();
            System.out.print("Enter a user name: ");
            String name = scanner.next();
            System.out.print("Create a password: ");
            String pass = scanner.next();
            System.out.print("Enter intial balance: ");
            Double bal = scanner.nextDouble();
            Customer n_customer = new Customer(customer_id, name, pass, bal);
            customers.add(n_customer);
            /*try {
                Files.write(Paths.get("customers.txt"), name.getBytes(), StandardOpenOption.APPEND);
            }
            catch (IOException e) 
            {
                //exception handling left as an exercise for the reader
            }*/
        }
        else
        {
            System.out.println("Please choose a valid option");
        }
    }
    private static ArrayList<Customer> loadCustomers() 
    {
        ArrayList<Customer> customers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("customers.txt"))) 
        {
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int customerId = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                double balance = Double.parseDouble(parts[3]);
                Customer customer = new Customer(customerId, username, password, balance);
                customers.add(customer);
            }
            System.out.println("customerdata loaded sucessfylly");
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Error loading customer data: " + e.getMessage());
        }
        return customers;
    }
    private static ArrayList<Owner> loadOwners() 
    {
        ArrayList<Owner> owners = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("owners.txt"))) 
        {
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                Owner owner = new Owner(username, password);
                owners.add(owner);
            }
            System.out.println("Owner data loaded sucessfully");
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Error loading owner data: " + e.getMessage());
        }
        return owners;
    }
}
