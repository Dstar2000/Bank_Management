package bankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
	private Connection con;
	private Scanner s;

	public AccountManager(Connection con, Scanner s) {
		this.con = con;
		this.s = s;
	}

	public void credit_money(long account_number) throws SQLException {
		s.nextLine();
		System.out.println("Enter Ammount to be credit: ");
		double amount = s.nextDouble();
		s.nextLine();
		System.out.println("Enter Security Pin: ");
		String Security_pin = s.nextLine();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = con
						.prepareStatement("SELECT * FROM Accounts WHERE Security_pin=?");
				preparedStatement.setString(1, Security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number=?";
					PreparedStatement preparedStatement1 = con.prepareStatement(credit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					int rowAffected = preparedStatement1.executeUpdate();

					if (rowAffected > 0) {
						System.out.println("Rs. " + amount + " credited Successfully");
						con.commit();
						con.setAutoCommit(true);
						return;
					} else {
						System.out.println("Transaction Failed!");
						con.rollback();
						con.setAutoCommit(true);
					}
				} else {
					System.out.println("Invalid Security Pin!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}

	public void debit_money(long account_number) throws SQLException {
		s.nextLine();
		System.out.println("Enter Ammount to be debit: ");
		double amount = s.nextDouble();
		s.nextLine();
		System.out.println("Enter Security Pin: ");
		String Security_pin = s.nextLine();

		try {
			con.setAutoCommit(false);
			if (account_number != 0) {
				PreparedStatement preparedStatement = con
						.prepareStatement("SELECT * FROM Accounts WHERE Security_Pin=?");
				preparedStatement.setString(1, Security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					if (amount <= current_balance) {
						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number=?";
						PreparedStatement preparedStatement1 = con.prepareStatement(debit_query);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int rowAffected = preparedStatement1.executeUpdate();
						if (rowAffected > 0) {
							System.out.println("Rs. " + amount + " debited Successfully");
							con.commit();
							con.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance!");
					}
				} else {
					System.out.println("Invalid Pin!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
	}

	public void transfer_money(long sender_account_number) throws SQLException {
		s.nextLine();
		System.out.println("Enter receiver's account number: ");
		long receivers_account_number = s.nextLong();
		System.out.println("Enter Amount: ");
		double amount = s.nextDouble();
		s.nextLine();
		System.out.println("Enter Security Pin: ");
		String Security_pin = s.nextLine();

		try {
			con.setAutoCommit(false);

			if (sender_account_number != 0 && receivers_account_number != 0) {
				PreparedStatement preparedStatement = con
						.prepareStatement("SELECT * FROM Accounts WHERE account_number=? AND Security_pin=?");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, Security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");

					if (amount <= current_balance) {
						String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number=?";
						PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setLong(2, sender_account_number);

						String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number=?";
						PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setLong(2, receivers_account_number);

						int rowAffected1 = debitPreparedStatement.executeUpdate();
						int rowAffected2 = creditPreparedStatement.executeUpdate();

						if (rowAffected1 > 0 && rowAffected2 > 0) {
							System.out.println("Transaction Successful!");
							System.out.println("Rs. " + amount + " transferred successfully.");
							con.commit();
							con.setAutoCommit(true);
							return;
						} else {
							System.out.println("Transaction Failed!");
							con.rollback();
							con.setAutoCommit(true);
						}
					} else {
						System.out.println("Insufficient Balance");
					}
				} else {
					System.out.println("Invalid Security Pin!");
				}
			} else {
				System.out.println("Invalid account number");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.setAutoCommit(true);
		}
	}

	public void getBalance(long account_number) {
		s.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = s.nextLine();
		try {
			PreparedStatement preparedStatement = con
					.prepareStatement("SELECT balance FROM Accounts WHERE account_number=? AND security_pin=?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println("Balance: " + balance);
			} else {
				System.out.println("Invalid Pin!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
