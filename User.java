package bankManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	private Connection con;
	private Scanner s;

	public User(Connection con, Scanner s) {
		this.con = con;
		this.s = s;
	}

	public void register() {
		s.nextLine();
		System.out.print("Please Enter Full Name: ");
		String full_name = s.nextLine();
		System.out.print("Please Enter Email: ");
		String email = s.nextLine();
		System.out.print("Please Enter Password: ");
		String password = s.nextLine();
		if (user_exist(email)) {
			System.out.println("User alredy exist for this email address!! ");
			return;
		}
		String registr_query = "INSERT INTO User(full_name, email, password) VALUES(?, ?, ?)";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(registr_query);
			preparedStatement.setString(1, full_name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows > 0) {
				System.out.println("Registration Successfull! ");
			} else {
				System.out.println("Registration Unsuccessfull!! ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String login() {
		s.nextLine();
		System.out.print("Email: ");
		String email = s.nextLine();
		System.out.println("Password: ");
		String password = s.nextLine();
		String Login_query = "SELECT * FROM User WHERE email=? AND password=?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(Login_query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return email;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean user_exist(String email) {
		String query = "SELECT * FROM user WHERE email = ?";
		try {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
