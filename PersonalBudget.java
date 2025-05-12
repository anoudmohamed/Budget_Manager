import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


class User {
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Expense> expenses;
    private List<Reminder> reminders;

    public User(String username, String email, String password, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.expenses = new ArrayList<>();
        this.reminders = new ArrayList<>();
    }

    public String toFileString() {
        return username + "," + email + "," + password + "," + phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addExpense(Expense e) {
        expenses.add(e);
    }

    public void addReminder(Reminder r) {
        reminders.add(r);
    }
}


class Expense {
    private double amount;
    private String category;
    private String paymentMethod;
    private String date;

    public Expense(double amount, String category, String paymentMethod, String date) {
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.date = date;
    }

    public String toFileString() {
        return amount + "," + category + "," + paymentMethod + "," + date;
    }

    public void displayExpense() {
        System.out.println("Category: " + category + ", Amount: $" + amount +
                ", Payment Method: " + paymentMethod + ", Date: " + date);
    }
}


class Reminder {
    private String title;
    private String date;
    private String time;

    public Reminder(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public String toFileString() {
        return title + "," + date + "," + time;
    }

    public void displayReminder() {
        System.out.println("Reminder: " + title + " at " + date + " " + time);
    }

    public boolean isValid() {
        return !title.isEmpty() && date.matches("\\d{4}-\\d{2}-\\d{2}") && time.matches("\\d{2}:\\d{2}");
    }
}


class Goal {
    private String title;
    private double targetAmount;
    private double currentAmount;
    private String deadline;

    public Goal(String title, double targetAmount, double currentAmount, String deadline) {
        this.title = title;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.deadline = deadline;
    }

    public String toFileString() {
        return title + "," + targetAmount + "," + currentAmount + "," + deadline;
    }

    public void displayGoal() {
        System.out.println(" Goal: " + title + " | Target: $" + targetAmount +
                " | Saved: $" + currentAmount + " | Deadline: " + deadline);
    }

    public boolean isCompleted() {
        return currentAmount >= targetAmount;
    }
}


interface ReportStrategy {
    void generate(String filename);
}

class SummaryReport implements ReportStrategy {
    public void generate(String filename) {
        File file = new File(filename + "_expenses.txt");
        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        try (Scanner scan = new Scanner(file)) {
            double total = 0;
            int count = 0;

            while (scan.hasNextLine()) {
                String[] parts = scan.nextLine().split(",");
                if (parts.length == 4) {
                    total += Double.parseDouble(parts[0]);
                    count++;
                }
            }

            System.out.println(" Summary Report:");
            System.out.println("- Total Expenses: $" + total);
            System.out.println("- Number of Transactions: " + count);

        } catch (Exception e) {
            System.out.println("Error generating summary: " + e.getMessage());
        }
    }
}

class DetailedReport implements ReportStrategy {
    public void generate(String filename) {
        File file = new File(filename + "_expenses.txt");
        if (!file.exists()) {
            System.out.println("No expenses found.");
            return;
        }

        try (Scanner scan = new Scanner(file)) {
            System.out.println("Detailed Report:");
            while (scan.hasNextLine()) {
                String[] parts = scan.nextLine().split(",");
                if (parts.length == 4) {
                    System.out.println("Category: " + parts[1] +
                            ", Amount: $" + parts[0] +
                            ", Method: " + parts[2] +
                            ", Date: " + parts[3]);
                }
            }
        } catch (Exception e) {
            System.out.println("Error generating details: " + e.getMessage());
        }
    }
}

class Report {
    private ReportStrategy strategy;

    public Report(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void generateReport(String filename) {
        strategy.generate(filename);
    }
}


class UserValidator {
    public boolean isValidEmail(String email) {
        return email != null &&
                email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
    }

    public boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.length() <= 16 &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    }

    public boolean isValidPhone(String phone) {

        return phone != null &&
                phone.matches("^\\+\\d{10,15}$");
    }
}


interface UserStorage {
    boolean isEmailExists(String email) throws IOException;
    void saveUser(User user) throws IOException;
    boolean login(String email, String password) throws IOException;
}

class FileUserStorage implements UserStorage {
    private static final String FILE_PATH = "users.txt";

    public boolean isEmailExists(String email) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(",");
            if (data.length >= 2 && data[1].equalsIgnoreCase(email)) {
                scanner.close();
                return true;
            }
        }
        scanner.close();
        return false;
    }

    public void saveUser(User user) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(user.toFileString());
        bufferedWriter.newLine();
        bufferedWriter.close();
    }

    public boolean login(String email, String password) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String[] data = scanner.nextLine().split(",");
            if (data.length >= 3) {
                String storedEmail = data[1];
                String storedPassword = data[2];
                if (storedEmail.equalsIgnoreCase(email)) {
                    if (storedPassword.equals(password)) {
                        scanner.close();
                        return true;
                    } else {
                        scanner.close();
                        System.out.println("Wrong password.");
                        return false;
                    }
                }
            }
        }
        scanner.close();
        System.out.println("Email not found.");
        return false;
    }
}


class OTPGenerator {
    public static String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}

class OTPService {
    public static void sendOTP(String email, String otp) {
        System.out.println("OTP sent to " + email + ": " + otp);
    }
}

class AuthenticationManager {
    private final UserStorage storage;
    private final UserValidator validator;

    public AuthenticationManager(UserStorage storage, UserValidator validator) {
        this.storage = storage;
        this.validator = validator;
    }

    public void signUp(String username, String email, String password, String phone) throws IOException {
        if (!validator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }
        if (!validator.isValidPassword(password)) {
            System.out.println("Password must be 8-16 characters, include uppercase, lowercase, and number.");
            return;
        }
        if (!validator.isValidPhone(phone)) {
            System.out.println("Invalid phone number format.");
            return;
        }
        if (storage.isEmailExists(email)) {
            System.out.println("Email already exists. Please log in.");
            return;
        }

        String otp = OTPGenerator.generateOTP();
        OTPService.sendOTP(email, otp);

        Scanner input = new Scanner(System.in);
        System.out.print("Enter the OTP sent to your email: ");
        String enteredOtp = input.nextLine();

        if (!enteredOtp.equals(otp)) {
            System.out.println("Invalid OTP. Registration canceled.");
            return;
        }

        User user = new User(username, email, password, phone);
        storage.saveUser(user);
        System.out.println("User registered successfully! You are now logged in.");
        redirectToDashboard();
    }

    public boolean login(String email, String password) throws IOException {
        if (storage.login(email, password)) {
            System.out.println("Login successful!");
            redirectToDashboard();
            return true;
        } else {
            System.out.println("Login failed. Check your credentials.");
            return false;
        }
    }

    private void redirectToDashboard() {
        System.out.println("Redirecting to dashboard...");
    }
}

// Anoud Part
class Income {
    private String source;
    private double amount;
    private String date;

    public Income(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    public String toFileString() {
        return source + "," + amount + "," + date;
    }

    public void displayIncome() {
        System.out.println("Source: " + source + ", Amount: $" + amount + ", Date: " + date);
    }

    public static boolean isValidSource(String source) {
        return source != null && source.length() >= 3 && source.length() <= 50;
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidDate(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}") && !date.matches(".*[a-zA-Z].*");
    }
}

class Budget {
    private String category;
    private double amount;

    public Budget(String category, double amount) {
        this.category = category;
        this.amount = amount;
    }

    public String toFileString() {
        return category + "," + amount;
    }

    public void displayBudget() {
        System.out.println("Category: " + category + ", Budget: $" + amount);
    }

    public static boolean isValidCategory(String category) {
        return category != null && category.length() >= 3 && category.length() <= 50;
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
}

class SpendingAnalysis {
    private Map<String, Double> categorySpending;
    private Map<String, Double> budgetComparison;

    public SpendingAnalysis(Map<String, Double> categorySpending, Map<String, Double> budgetComparison) {
        this.categorySpending = categorySpending;
        this.budgetComparison = budgetComparison;
    }

    public void displayAnalysis() {
        System.out.println("\n--- Spending Analysis ---");
        System.out.println("Category\tSpent\tBudget\tDifference");
        System.out.println("----------------------------------");
        for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
            String category = entry.getKey();
            double spent = entry.getValue();
            double budget = budgetComparison.getOrDefault(category, 0.0);
            System.out.printf("%-10s\t$%.2f\t$%.2f\t$%.2f%n",
                    category, spent, budget, (budget - spent));
        }
    }
}

public class PersonalBudget {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        UserStorage storage = new FileUserStorage();
        UserValidator validator = new UserValidator();
        AuthenticationManager auth = new AuthenticationManager(storage, validator);

        while (true) {
            System.out.println("************* Welcome *************");
            System.out.println("1. Sign Up\n2. Login \n3. Exit");
            int choice = input.nextInt();
            input.nextLine();

            if (choice == 1) {

                String username;
                do {
                    System.out.print("Username: ");
                    username = input.nextLine().trim();
                    if (username.isEmpty()) {
                        System.out.println("Username cannot be empty. Please try again.");
                    }
                } while (username.isEmpty());


                String email;
                do {
                    System.out.print("Email: ");
                    email = input.nextLine().trim();
                    if (!validator.isValidEmail(email)) {
                        System.out.println("Invalid email format. Must be like user@example.com");
                        System.out.println("Example: john@gmail.com, user123@company.co.uk");
                    }
                } while (!validator.isValidEmail(email));


                String password;
                do {
                    System.out.print("Password: ");
                    password = input.nextLine().trim();
                    if (!validator.isValidPassword(password)) {
                        System.out.println("Invalid password. Must be:");
                        System.out.println("- Between 8-16 characters long");
                        System.out.println("- Contain at least one uppercase letter");
                        System.out.println("- Contain at least one lowercase letter");
                        System.out.println("- Contain at least one number");
                    }
                } while (!validator.isValidPassword(password));


                String phone;
                do {
                    System.out.print("Phone Number (e.g., +201234567890): ");
                    phone = input.nextLine().trim();
                    if (!validator.isValidPhone(phone)) {
                        System.out.println("Invalid phone number. Must:");
                        System.out.println("- Start with '+' sign");
                        System.out.println("- Contain 10-15 digits after the '+'");
                        System.out.println("Example: +201234567890, +12345678901");
                    }
                } while (!validator.isValidPhone(phone));


                auth.signUp(username, email, password, phone);

            } else if (choice == 2) {

                String email;
                do {
                    System.out.print("Email: ");
                    email = input.nextLine().trim();
                    if (!validator.isValidEmail(email)) {
                        System.out.println("Invalid email format. Must be like user@example.com");
                        System.out.println("Example: john@gmail.com, user123@company.co.uk");
                    }
                } while (!validator.isValidEmail(email));

                String password;
                do {
                    System.out.print("Password: ");
                    password = input.nextLine().trim();
                    if (!validator.isValidPassword(password)) {
                        System.out.println("Invalid password. Must be:");
                        System.out.println("- Between 8-16 characters long");
                        System.out.println("- Contain at least one uppercase letter");
                        System.out.println("- Contain at least one lowercase letter");
                        System.out.println("- Contain at least one number");
                    }
                } while (!validator.isValidPassword(password));


                if (auth.login(email, password)) {

                    String filename = email.replaceAll("[^a-zA-Z0-9]", "_");
                    while (true) {
                        System.out.println("\n--- Dashboard ---");
                        System.out.println("1. Add Expense");
                        System.out.println("2. Display Expenses");
                        System.out.println("3. Add Reminder");
                        System.out.println("4. Add Goal");
                        System.out.println("5. Display Goals");
                        System.out.println("6. View Financial Report");
                        System.out.println("7. Track My Income");
                        System.out.println("8. Budgeting & Analysing");
                        System.out.println("9. Logout");

                        int action = input.nextInt();
                        input.nextLine();

                        if (action == 1) {
                            System.out.print("Amount: ");
                            double amount = input.nextDouble();
                            input.nextLine();

                            System.out.print("Category: ");
                            String category = input.nextLine();

                            System.out.print("Payment Method: ");
                            String method = input.nextLine();

                            System.out.print("Date (YYYY-MM-DD): ");
                            String date = input.nextLine();

                            Expense e = new Expense(amount, category, method, date);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_expenses.txt", true));
                            writer.write(e.toFileString());
                            writer.newLine();
                            writer.close();
                            System.out.println("  Expense added successfully!");

                        } else if (action == 2) {

                            File expenseFile = new File(filename + "_expenses.txt");

                            if (!expenseFile.exists()) {
                                System.out.println("No expenses found.");
                            } else {
                                Scanner scan = new Scanner(expenseFile);
                                System.out.println("----- Your Expenses -----");
                                while (scan.hasNextLine()) {
                                    String[] parts = scan.nextLine().split(",");
                                    if (parts.length == 4) {
                                        System.out.println("Category: " + parts[1] +
                                                ", Amount: $" + parts[0] +
                                                ", Method: " + parts[2] +
                                                ", Date: " + parts[3]);
                                    }
                                }
                                scan.close();
                            }
                        } else if (action == 3) {
                            System.out.print("Reminder Title: ");
                            String title = input.nextLine();
                            System.out.print("Date (YYYY-MM-DD): ");
                            String rDate = input.nextLine();
                            System.out.print("Time (HH:MM): ");
                            String rTime = input.nextLine();
                            Reminder r = new Reminder(title, rDate, rTime);
                            if (!r.isValid()) {
                                System.out.println(" Invalid reminder data.");
                            } else {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_reminders.txt", true));
                                writer.write(r.toFileString());
                                writer.newLine();
                                writer.close();
                                System.out.println(" Reminder saved successfully!");
                                System.out.println(" Notification will be sent at: " + rDate + " " + rTime);
                            }
                        } else if (action == 4) {
                            System.out.print("Goal Title: ");
                            String gTitle = input.nextLine();

                            System.out.print("Target Amount: ");
                            double gTarget = input.nextDouble();
                            input.nextLine();

                            System.out.print("Current Amount: ");
                            double gCurrent = input.nextDouble();
                            input.nextLine();

                            System.out.print("Deadline (YYYY-MM-DD): ");
                            String gDeadline = input.nextLine();

                            Goal g = new Goal(gTitle, gTarget, gCurrent, gDeadline);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_goals.txt", true));
                            writer.write(g.toFileString());
                            writer.newLine();
                            writer.close();
                            System.out.println(" Goal saved successfully!");
                        } else if (action == 5) {
                            File goalFile = new File(filename + "_goals.txt");
                            if (!goalFile.exists()) {
                                System.out.println("No goals found.");
                            } else {
                                Scanner scan = new Scanner(goalFile);
                                while (scan.hasNextLine()) {
                                    String[] parts = scan.nextLine().split(",");
                                    if (parts.length == 4) {
                                        System.out.println("Goal: " + parts[0] +
                                                " | Target: $" + parts[1] +
                                                " | Saved: $" + parts[2] +
                                                " | Deadline: " + parts[3]);
                                    }
                                }
                                scan.close();
                            }
                        } else if (action == 6) {
                            System.out.println("Choose report type:");
                            System.out.println("1. Summary Report");
                            System.out.println("2. Detailed Report");
                            int reportChoice = input.nextInt();
                            input.nextLine();

                            Report report;
                            if (reportChoice == 1) {
                                report = new Report(new SummaryReport());
                            } else {
                                report = new Report(new DetailedReport());
                            }
                            report.generateReport(filename);
                        } else if (action == 7) {
                            // Track Income
                            System.out.print("Income Source (e.g., salary, freelance): ");
                            String source = input.nextLine();
                            while (!Income.isValidSource(source)) {
                                System.out.print("Invalid source (3-50 chars). Enter again: ");
                                source = input.nextLine();
                            }

                            System.out.print("Amount: ");
                            double amount = input.nextDouble();
                            input.nextLine();
                            while (!Income.isValidAmount(amount)) {
                                System.out.print("Amount must be positive. Enter again: ");
                                amount = input.nextDouble();
                                input.nextLine();
                            }

                            System.out.print("Date (YYYY-MM-DD): ");
                            String date = input.nextLine();
                            while (!Income.isValidDate(date)) {
                                System.out.print("Invalid date format. Enter again (YYYY-MM-DD): ");
                                date = input.nextLine();
                            }

                            Income income = new Income(source, amount, date);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_incomes.txt", true));
                            writer.write(income.toFileString());
                            writer.newLine();
                            writer.close();
                            System.out.println("Income added successfully!");

                        } else if (action == 8) {
                            // Budgeting & Analysis
                            System.out.println("\nBudgeting Options:");
                            System.out.println("1. Set Budget");
                            System.out.println("2. View Budgets");
                            System.out.println("3. Spending Analysis");
                            System.out.print("Choose an option: ");
                            int budgetChoice = input.nextInt();
                            input.nextLine();

                            if (budgetChoice == 1) {
                                // Set Budget
                                System.out.print("Category (e.g., groceries, rent): ");
                                String category = input.nextLine();
                                while (!Budget.isValidCategory(category)) {
                                    System.out.print("Invalid category (3-50 chars). Enter again: ");
                                    category = input.nextLine();
                                }

                                System.out.print("Budget Amount: ");
                                double amount = input.nextDouble();
                                input.nextLine();
                                while (!Budget.isValidAmount(amount)) {
                                    System.out.print("Amount must be positive. Enter again: ");
                                    amount = input.nextDouble();
                                    input.nextLine();
                                }

                                Budget budget = new Budget(category, amount);
                                BufferedWriter writer = new BufferedWriter(new FileWriter(filename + "_budgets.txt", true));
                                writer.write(budget.toFileString());
                                writer.newLine();
                                writer.close();
                                System.out.println("Budget set successfully!");

                            } else if (budgetChoice == 2) {
                                // View Budgets
                                File budgetFile = new File(filename + "_budgets.txt");
                                if (!budgetFile.exists()) {
                                    System.out.println("No budgets set yet.");
                                } else {
                                    Scanner scan = new Scanner(budgetFile);
                                    System.out.println("\n--- Your Budgets ---");
                                    while (scan.hasNextLine()) {
                                        String[] parts = scan.nextLine().split(",");
                                        if (parts.length == 2) {
                                            System.out.println("Category: " + parts[0] +
                                                    ", Budget: $" + parts[1]);
                                        }
                                    }
                                    scan.close();
                                }

                            } else if (budgetChoice == 3) {
                                // Spending Analysis
                                Map<String, Double> categorySpending = new HashMap<>();
                                Map<String, Double> budgets = new HashMap<>();

                                // Read expenses
                                File expenseFile = new File(filename + "_expenses.txt");
                                if (expenseFile.exists()) {
                                    Scanner scan = new Scanner(expenseFile);
                                    while (scan.hasNextLine()) {
                                        String[] parts = scan.nextLine().split(",");
                                        if (parts.length == 4) {
                                            String category = parts[1];
                                            double amount = Double.parseDouble(parts[0]);
                                            categorySpending.put(category,
                                                    categorySpending.getOrDefault(category, 0.0) + amount);
                                        }
                                    }
                                    scan.close();
                                }

                                // Read budgets
                                File budgetFile = new File(filename + "_budgets.txt");
                                if (budgetFile.exists()) {
                                    Scanner scan = new Scanner(budgetFile);
                                    while (scan.hasNextLine()) {
                                        String[] parts = scan.nextLine().split(",");
                                        if (parts.length == 2) {
                                            String category = parts[0];
                                            double amount = Double.parseDouble(parts[1]);
                                            budgets.put(category, amount);
                                        }
                                    }
                                    scan.close();
                                }

                                if (categorySpending.isEmpty() && budgets.isEmpty()) {
                                    System.out.println("No data available for analysis.");
                                } else {
                                    SpendingAnalysis analysis = new SpendingAnalysis(categorySpending, budgets);
                                    analysis.displayAnalysis();
                                }
                            } else {
                                System.out.println("Invalid option.");
                            }

                        } else if (action == 9) {
                            System.out.println("Logging out...");
                            break; // Exit dashboard loop
                        } else {
                            System.out.println("Invalid option.");
                        }
                    }
                } else {
                    System.out.println("Login failed. Returning to main menu.");
                }
            } else if (choice == 3) {
                System.out.println("Exiting program...");
                input.close();
                break;
            } else {
                System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
}
