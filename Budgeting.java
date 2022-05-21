import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Budgeting {
	public static final int MAX_CHANGES = 3;
	
	private static String username;
	private static double balance;
	private static double monthlyBudget;
	private static double monthlySpent;
	private static boolean budget;
	private static int numBudgetChanges;
	
	// limit on changing budget
	
	public static void main(String[] arg) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter your username (case sensitive): ");
		username = scan.next();
		balance = getBalance(username);
		boolean validUsername = balance != Integer.MAX_VALUE ? true : false;
		while (!validUsername) {
			System.out.print("Invalid Username.\nEnter your username (case sensitive): ");
			username = scan.next();
			balance = getBalance(username);
			validUsername = balance != Integer.MAX_VALUE ? true : false;
		}
		System.out.printf("Your current balance is: $%,.2f\n", balance);

		// runs constantly until program stopped
		while (true) {
			String choice = controlPanel(scan);
			if (choice.equals("t")) {
				System.out.println("\nMAKE A TRANSACTION");
				System.out.print("\tEnter transaction amount: ");
				int transaction = scan.nextInt();
				makeTransaction(scan, transaction);
			}
			else if (choice.equals("q")) {
				System.out.println("\n\tThank you for banking with Capital One!\n");
				System.exit(0);
			}
			else if (choice.equals("b")) {
				budget = true;
				System.out.println("\nSET A BUDGET");
				System.out.print("\tEnter desired budget: ");
				monthlyBudget = scan.nextDouble();
				if (monthlyBudget > balance) {
					System.out.println("\tYour desired budget exceeds your balance."
							+ " Setting budget to your current balance instead");
					monthlyBudget = balance;
				}
				System.out.println("\tBudget set to: $" + monthlyBudget);

			}
			else if (choice.equals("cb")) {
				System.out.println("\nCHANGE BUDGET");
				if (numBudgetChanges >= MAX_CHANGES) {
					System.out.println("\tYou've exceeded the maximum number of budget changes.");
				}
				else {
					int newMonthlyBudget = scan.nextInt();
					if (newMonthlyBudget < monthlySpent) {
						System.out.print("\tEnter new budget: ");
						System.out.println("\tYou have already exceeded this budget.");
					}
					else {	
						numBudgetChanges++;
						System.out.print("\tEnter budget for your current balance: ");
						if (monthlyBudget > balance) {
							System.out.println("\tYour desired budget exceeds your balance."
									+ " Setting budget to your current balance instead.");
							monthlyBudget = balance;
						}
						System.out.println("\tBudget set to: $" + monthlyBudget);
						System.out.println("\tYou have changed your budget " + numBudgetChanges +
								"/3 times this month.");
					}
				}
				
			}
			else {
				System.out.println("\nInvalid choice");
			}
		}

	}
	
	private static String controlPanel(Scanner scan) {
		System.out.println("\nLIST OF COMMANDS"); 

		System.out.println("\t\"t\" to make a transaction"); 
		System.out.println("\t\"b\" to set a budget"); 
		System.out.println("\t\"cb\" to change budget (monthly limit of 3 changes)");
		System.out.println("\t\"q\" to quit and log out"); 



		System.out.print("\nChoose an action: "); 
		return scan.next();
	}
	
	private static void makeTransaction(Scanner scan, int transaction) {
		String response = "";
		// If the user is currently budgeting
		if (budget) {
			if (monthlySpent + transaction > monthlyBudget) {
				System.out.print("\tWarning! This purchase will cause your spending to exceed your monthly budget."
						+ " Do you approve this transaction? (y/n) ");
				response = scan.next();
				if (response.equals("y")) {
					if (monthlySpent + transaction > balance) {
						System.out.println("\tInsufficient funds. Transaction failed.");
					}
					else {
						makeTransactionHelp(transaction);
					}					
				}
			}
			else {
				makeTransactionHelp(transaction);
			}
		}
		else {
			if (monthlySpent + transaction > balance) {
				System.out.println("\tInsufficient funds. Transaction failed.");
			}
			else {
				makeTransactionHelp(transaction);
			}
		}
	}
	private static void makeTransactionHelp(int transaction) {
		balance -= transaction;
		monthlySpent += transaction;
		System.out.printf("\tYour new balance is $%,.2f\n", balance);
		System.out.printf("\tYou spent $%,.2f\n", monthlySpent);
		if (budget) {
			if (monthlySpent <= monthlyBudget) {
				double percentSpent = (monthlySpent / monthlyBudget) * 100;
				System.out.printf("\tYou used %,.2f%% of your budget so far\n", percentSpent);
			}
			else {
				System.out.println("\tYou have exceeded your budget of $" + monthlyBudget);
				// ask to change budget, have a budget limit
			}
		}

	}
	
	public static int getBalance(String username) throws FileNotFoundException{
		File file = new File("UserData");
		Scanner scanFile = new Scanner(file);
		int balance = Integer.MAX_VALUE;

		while (scanFile.hasNextLine()) {
			String line = scanFile.nextLine();
			Scanner scanLine = new Scanner(line);
			String currentName = scanLine.next();
			if (currentName.equals(username)) {
				balance = scanLine.nextInt();
			}
			scanLine.close();
		}
		scanFile.close();
		return balance;
	}
}
