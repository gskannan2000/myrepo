package com.cg.prestmt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginDAO {

	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/demo";
	private final String username = "root";
	private final String password = "root";

	private final String listQuery = "SELECT * FROM login";

	private Connection conn = null;
	private PreparedStatement stmt = null;

	public List<Login> findAll() {
		try {
			stmt = getConnection().prepareStatement(listQuery);
//			System.out.println("Statement created...");
		} catch (SQLException e) {
			System.out.println("Unable to create Statement " + e.getErrorCode());
		}

		ResultSet result = null;
		try {
			result = stmt.executeQuery(listQuery);
		} catch (SQLException e) {
			System.out.println("Unable to execute Query " + e.getErrorCode());
		}
		List<Login> list = new ArrayList<>();
		Login login = null;
		try {
			while (result.next()) {
				login = new Login();
				login.setId(result.getInt(1));
				login.setUsername(result.getString(2));
				login.setPassword(result.getString(3));
				login.setRole(result.getString(4));
				list.add(login);
			}
		} catch (SQLException e) {
			System.out.println("Unable to iterate the reult set " + e.getErrorCode());
		}
		return list;
	}

	public Login findById(int id) {
		String query = "SELECT * FROM login WHERE id = ?";
		try {
			stmt = getConnection().prepareStatement(query);
			stmt.setInt(1, id);
//			System.out.println("Statement created...");
		} catch (SQLException e) {
			System.out.println("Unable to create Statement " + e.getErrorCode());
		}

		ResultSet result = null;
		try {
			result = stmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("Unable to execute Query " + e.getErrorCode());
		}
		Login login = null;
		try {
			if (result.next()) {
				login = new Login();
				login.setId(result.getInt(1));
				login.setUsername(result.getString(2));
				login.setPassword(result.getString(3));
				login.setRole(result.getString(4));
			} else {
				System.out.println(id + " does not exixts.");
			}

		} catch (SQLException e) {
			System.out.println("Unable to read result " + e.getErrorCode());
		}
		return login;
	}

	public Login save(Login login) {
		String query = "insert into login values(?, ?, ?, ? )";
		try {
			stmt = getConnection().prepareStatement(query);
			stmt.setInt(1, 0);
			stmt.setString(2, login.getUsername());
			stmt.setString(3, login.getPassword());
			stmt.setString(4, login.getRole());

//			System.out.println("Statement created...");
		} catch (SQLException e) {
			System.out.println("Unable to create Statement " + e.getErrorCode());
		}

		int newId = 0;
		int flag = 0;
		try {
			flag = stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Unable to execute Query " + e.getErrorCode());
		}
		if (flag == 1) {
			login.setId(newId);
		}
		return login;
	}

	public Login saveOrUpdate(Login login) {
		Login oldLogin = null;
		if (login != null) {
//			System.out.println("Update :: " + login);
			oldLogin = findById(login.getId());
		}
		if (oldLogin == null) {
			System.out.println("oldLogin is NULL");
		}
//			System.out.println(oldLogin);

		String query = "UPDATE login SET username =?, password =?, role =? WHERE id = ?";
		try {
			stmt = getConnection().prepareStatement(query);
			stmt.setString(1, login.getUsername());
			stmt.setString(2, login.getPassword());
			stmt.setString(3, login.getRole());
			stmt.setInt(4, login.getId());
			System.out.println("Update Statement created...");
		} catch (SQLException e) {
			System.out.println("Unable to create Statement " + e.getErrorCode());
		}
		try {
			int flag = stmt.executeUpdate();
			if (flag == 1) {
				System.out.println("Successfully Updated");
			} else {
				System.out.println("unable to Updated");
			}
		} catch (SQLException e) {
//			System.out.println("Unable to execute Query " + e.getErrorCode());
			if (e.getErrorCode() == 1062) {
				System.out.println("username '" + login.getUsername() + "' already exists.");
			}
		}

		return login;
	}

	public void deleteById(int id) {
		String query = "DELETE FROM login WHERE id =?";
		Login oldLogin = findById(id);
		if (oldLogin != null) {
			try {
				stmt = getConnection().prepareStatement(query);
				stmt.setInt(1, id);
//			System.out.println("Statement created...");
			} catch (SQLException e) {
				System.out.println("Unable to create Statement " + e.getErrorCode());
			}

			try {
				int flag = stmt.executeUpdate();
				if (flag == 1) {
					System.out.println("Successfully Deleted");
				} else {
					System.out.println("Unable to delete ID +" + id);
				}
			} catch (SQLException e) {
				System.out.println("Unable to execute Query " + e.getErrorCode());
			}
		}
	}

	private Connection getConnection() {
		try {
			Class.forName(driver);
//			System.out.println("Database Driver registered...");
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			System.out.println("Driver NOT available");
		}
		try {
			conn = DriverManager.getConnection(url, username, password);
//			System.out.println("Connection Established...");
		} catch (SQLException e) {
//			e.printStackTrace();
			System.out.println("Unable to connect with Database...");
		}
		return conn;
	}
}
