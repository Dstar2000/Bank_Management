package bankManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
	private static final String url = "jdbc:mysql://localhost:3306/DBank";
	private static final String username = "root";
	private static final String password = "pass123";

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection con = DriverManager.getConnection(url, username, password);
			Scanner s = new Scanner(System.in);
			User user = new User(con, s);
			Accounts a = new Accounts(con, s);
			AccountManager am = new AccountManager(con, s);

			String email;
			long account_number;

			while (true) {
				System.out.println("** Welcome to Banking System **");
				System.out.println();
				System.out.println("1. Register");
				System.out.println("2. Login");
				System.out.println("3. Exit");
				System.out.println("Please enter your choice : ");
				int choice1 = s.nextInt();
				switch (choice1) {
				case 1:
					user.register();
					break;
				case 2:
					email = user.login();
					if (email != null) {
						System.out.println();
						System.out.println("Login Successfull!....");
						if (!a.account_exist(email)) {
							System.out.println();
							System.out.println("1. Open a new Bank Account");
							System.out.println("2. Exit");
							if (s.nextInt() == 1) {
								account_number = a.open_account(email);
								System.out.println("Account Created Successfully");
								System.out.println("Your Account Number is: " + account_number);
							} else {
								break;
							}
						}

						account_number = a.getAccount_number(email);
						int choice2 = 0;
						while (choice2 != 5) {
							System.out.println();
							System.out.println("1. Debit Money");
							System.out.println("2. Credit Money");
							System.out.println("3. Transfer Money");
							System.out.println("4. Check Balance");
							System.out.println("5. Logout");
							System.out.println("Enter your choice: ");
							choice2 = s.nextInt();
							switch (choice2) {
							case 1:
								am.debit_money(account_number);
								break;
							case 2:
								am.credit_money(account_number);
								break;
							case 3:
								am.transfer_money(account_number);
								break;
							case 4:
								am.getBalance(account_number);
								break;
							case 5:
								break;
							default:
								System.out.println("Enter Valid Choice");
								break;
							}
						}
					} else {
						System.out.println("Incorect Email or Password!!");
					}
				case 3:
					System.out.println("Thank you for using Banking system!!!");
					System.out.println("Exititng System!!");
					return;
				default:
					System.out.println("Enter Valid Choice");
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
}