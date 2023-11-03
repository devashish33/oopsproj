import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 
// Abstract User class
abstract class User implements Serializable {
    private int userID;
    private String username;
    private String password;

    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public boolean login(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }
}


class Owner extends User 
{
    private ArrayList<String> menu;
    private ArrayList<Integer> customers;
    private int OwnerID;
    public Owner(int OwnerID,String username, String password) 
    {
        super(OwnerID,username, password);
        this.menu = new ArrayList<>();
        this.customers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("customers.txt"))) 
        {
            while (scanner.hasNextLine()) 
            {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                int customerId = Integer.parseInt(parts[0]);
                customers.add(customerId);
            }
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Error loading customer data to owner: " + e.getMessage());
        }
        }
    

   /*public void addCustomer(String customerUsername) 
    {
        customers.add(customerUsername);
    }*/

    public void viewMealsBookedForToday() 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String today = dateFormat.format(currentDate);
        int mealsToday = 0;
        for (int customerid : customers) 
        {
            String customerBookingFile = "customer_" + customerid + "_bookings.txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(customerBookingFile))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    String[] parts = line.split(",");
                    String date = parts[0];
                    String status = parts[1];
                    if (date.equals(today) && status.equals("booked")) 
                    {
                        mealsToday++;
                    }
                }
            }
            catch (IOException e) 
            {
                System.err.println("Error reading booking history: " + e.getMessage());
            }
        }
        System.out.println("Number of Customers Eating Today: "+mealsToday);
    }

    public void viewMealsBookedForUpcomingDates() 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        for (int i = 1; i <= 5; i++) 
        {
            String upcomingDate = dateFormat.format(new Date(currentDate.getTime() + i * 24 * 60 * 60 * 1000));
            for (int customerid : customers) 
            {
                String customerBookingFile = "customer_" + customerid + "_bookings.txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(customerBookingFile))) 
                {
                    String line;
                    while ((line = reader.readLine()) != null) 
                    {
                        String[] parts = line.split(",");
                        String date = parts[0];
                        String status = parts[1];
                        if (date.equals(upcomingDate) && status.equals("booked")) 
                        {
                            System.out.println("Customer " + customerid + " has booked a meal for " + upcomingDate);
                        }
                    }
                } 
                catch (IOException e) 
                {
                    System.err.println("Error reading booking history: " + e.getMessage());
                }
            }
        }
    }

    public void viewMealHistoryForCustomer(int customerid) 
    {
        String customerBookingFile = "customer_" + customerid + "_bookings.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(customerBookingFile))) 
        {
            System.out.println("Meal History for Customer " + customerid + ":");
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                String date = parts[0];
                String status = parts[1];
                System.out.println("Date: " + date + ", Status: " + status);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error reading meal history: " + e.getMessage());
        }
    }

    public void viewPastMealHistoryByDate(String date) 
    {
        for (int customerid : customers) 
        {
            String customerBookingFile = "customer_" + customerid + "_bookings.txt";
            try (BufferedReader reader = new BufferedReader(new FileReader(customerBookingFile))) 
            {
                String line;
                while ((line = reader.readLine()) != null) 
                {
                    String[] parts = line.split(",");
                    String bookingDate = parts[0];
                    String status = parts[1];
                    if (bookingDate.equals(date)) 
                    {
                        System.out.println("Customer " + customerid + " booked a meal on " + date + " with status: " + status);
                    }
                }
            } 
            catch (IOException e) 
            {
                System.err.println("Error reading booking history: " + e.getMessage());
            }
        }
    }
    public void viewMenu() 
    {
        if (menu.isEmpty()) 
        {
            System.out.println("Menu is empty.");
        } 
        else 
        {
            System.out.println("Menu for the week:");
            for (int i = 0; i < menu.size(); i++) 
            {
                System.out.println("Day " + (i + 1) + ": " + menu.get(i));
            }
        }
    }

    public void setMenu(ArrayList<String> menu) 
    {
        this.menu = menu;
    }

    public void logout() 
    {
        // Implement code to handle owner logout
    }
}

class Customer extends User 
{
    private int customerId;
    private double balance;
    private String bookingHistoryFile;
    private int days;
    public Customer(int customerId, String username, String password, double balance) 
    {
        super(customerId,username, password);
        this.customerId = customerId;
        this.balance = balance;
        this.bookingHistoryFile = "customer_" + customerId + "_bookings.txt";
        this.days = (int)balance/ 100;
        try 
        {
            File myObj = new File("customer_"+customerId+"_bookings.txt");
            myObj.createNewFile();
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred while loading customer files");
            e.printStackTrace();
        }
        LocalDate dt = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        try(FileWriter fw = new FileWriter("customer_"+customerId+"_bookings.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter out = new PrintWriter(bw))
        {
            for(int i = 0;i < days; i++)
            {
                out.println(String.valueOf(dateFormat.format(dt.plusDays(i))+","+"booked"));
                balance -= 100;
            }
        } 
        catch (IOException e) 
        {
            //exception handling left as an exercise for the reader
            System.err.println("an error occured during writing file");
            e.printStackTrace();
            
        }
    }

    public int getCustomerId() 
    {
        return customerId;
    }

    public double getBalance() 
    {
        return balance;
    }

    public void viewAccountDetails() 
    {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Remaining Balance: Rs" + balance);
    }

    public void cancelMeal(String date) 
    {
        try {
            File inputFile = new File(bookingHistoryFile);
            File tempFile = new File("tempBookings.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, true));

            String line;
            boolean mealCanceled = false;

            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                String bookingDate = parts[0];
                String status = parts[1];

                if (bookingDate.equals(date) && status.equals("booked")) {
                    writer.write(bookingDate + ",canceled");
                    writer.newLine();
                    mealCanceled = true;
                } 
                else 
                {
                    writer.write(line);
                    writer.newLine();
                }
            }

            reader.close();
            writer.close();

            if (mealCanceled) {
                inputFile.delete();
                tempFile.renameTo(new File(bookingHistoryFile));
                System.out.println("Meal for " + date + " has been canceled.");
            } else {
                tempFile.delete();
                System.out.println("No meal found for " + date + " to cancel.");
            }
        } catch (IOException e) {
            System.err.println("Error canceling meal: " + e.getMessage());
        }
    }

    public void viewPastMealsHistory() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(bookingHistoryFile))) 
        {
            System.out.println("Booking History for Customer ID " + customerId + ":");
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] parts = line.split(",");
                String date = parts[0];
                String status = parts[1];
                System.out.println("Date: " + date + ", Status: " + status);
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error reading meal history: " + e.getMessage());
        }
    }

    public void viewMenu() 
    {
        // Implement code to view the menu
    }

    public void bookMeal() 
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String date = dateFormat.format(currentDate);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(bookingHistoryFile, true))) {
            String bookingRecord = date + ",booked";
            writer.write(bookingRecord);
            writer.newLine();
            System.out.println("Meal booked successfully for " + date);
        } catch (IOException e) {
            System.err.println("Error writing to booking history: " + e.getMessage());
        }
    }

    public void logout() 
    {
        // Implement code to handle user logout
    }
}

public class mess 
{
    public static void main(String[] args) 
    {
        ArrayList<Customer> customers = loadCustomers();
        Owner owner = new Owner(007,"Bond", "mi7");
        owner.setMenu(getDefaultMenu());

        // Simulate some customer accounts
        Customer customer1 = new Customer(1, "customer1", "pass1", 100.0);
        Customer customer2 = new Customer(2, "customer2", "pass2", 75.0);
        customers.add(customer1);
        customers.add(customer2);

        Scanner scanner = new Scanner(System.in);

        while (true) 
        {
            System.out.println("\n*** Mess Management System ***");
            System.out.println("1. Customer Login");
            System.out.println("2. Owner Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            if (choice == 1)
            {
                // Customer Login
                System.out.print("Enter customer username: ");
                String customerUsername = scanner.next();
                System.out.print("Enter customer password: ");
                String customerPassword = scanner.next();

                Customer loggedInCustomer = null;
                for (Customer customer : customers) 
                {
                    if (customer.login(customerUsername, customerPassword)) 
                    {
                        loggedInCustomer = customer;
                        break;
                    }
                }

                if (loggedInCustomer != null) 
                {
                    customerMenu(loggedInCustomer, owner);
                } 
                else 
                {
                    System.out.println("Invalid customer credentials. Please try again.");
                }
            } 
            else if (choice == 2) 
            {
                // Owner Login
                System.out.print("Enter owner username: ");
                String ownerUsername = scanner.next();
                System.out.print("Enter owner password: ");
                String ownerPassword = scanner.next();
                if (owner.login(ownerUsername, ownerPassword)) 
                {
                    ownerMenu(owner, customers);
                } 
                else 
                {
                    System.out.println("Invalid owner credentials. Please try again.");
                }
            }
            else if (choice == 3) 
            {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } 
            else 
            {
                System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // Customer Menu
    private static void customerMenu(Customer customer, Owner owner) 
    {
        Scanner scanner = new Scanner(System.in);

        while (true) 
        {
            System.out.println("\n*** Customer Menu ***");
            System.out.println("1. View Account Details");
            System.out.println("2. Cancel Meal");
            System.out.println("3. View Past Meals History");
            System.out.println("4. View Menu");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    customer.viewAccountDetails();
                    break;
                case 2:
                    System.out.print("Enter date for cancellation");
                    String date = scanner.next();
                    customer.cancelMeal(date);
                    break;
                case 3:
                    customer.viewPastMealsHistory();
                    break;
                case 4:
                    owner.viewMenu();
                    break;
                case 5:
                    System.out.println("Logging out as customer: " + customer.getUsername());
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }
    // Owner Menu
    private static void ownerMenu(Owner owner, ArrayList<Customer> customers) 
    {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n*** Owner Menu ***");
            System.out.println("1. View Meals Booked for Today");
            System.out.println("2. View Meals Booked for Upcoming Dates");
            System.out.println("3. View Meal History for a Customer");
            System.out.println("4. View Past Meal History by Date");
            System.out.println("5. View Menu");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    owner.viewMealsBookedForToday();
                    break;
                case 2:
                    owner.viewMealsBookedForUpcomingDates();
                    break;
                case 3:
                    System.out.print("Enter customer id: ");
                    int customerid = scanner.nextInt();
                    owner.viewMealHistoryForCustomer(customerid);
                    break;
                case 4:
                    System.out.print("Enter date (yyyy-MM-dd): ");
                    String date = scanner.next();
                    owner.viewPastMealHistoryByDate(date);
                    break;
                case 5:
                    owner.viewMenu();
                    break;
                case 6:
                    System.out.println("Logging out as owner: " + owner.getUsername());
                    return;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    // Method to get the default menu (can be customized)
    
    // Method to get the default menu (can be customized)
    private static ArrayList<String> getDefaultMenu() 
    {
        ArrayList<String> defaultMenu = new ArrayList<>();
        defaultMenu.add("Monday: Meal 1");
        defaultMenu.add("Tuesday: Meal 2");
        defaultMenu.add("Wednesday: Meal 3");
        defaultMenu.add("Thursday: Meal 4");
        defaultMenu.add("Friday: Meal 5");
        defaultMenu.add("Saturday: Meal 6");
        defaultMenu.add("Sunday: Meal 7");
        return defaultMenu;
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
                int ownerid = Integer.parseInt(parts[0]);
                String username = parts[1];
                String password = parts[2];
                Owner owner = new Owner(ownerid,username, password);
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