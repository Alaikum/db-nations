package org.lessons.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws SQLException {

		String url = "jdbc:mysql://localhost:3306/nations";
		String user = "root";
		String password = "root";
		Scanner s = new Scanner(System.in);

		try (Connection con = DriverManager.getConnection(url, user, password);) {
			System.out.println("Connessione Effettuata con successo");

			// SCRIVO QUERY
			String sql = "SELECT c.name ,c.country_id, r.name , c2.name  FROM countries c \r\n"
					+ "JOIN regions r on c.region_id =r.region_id \r\n"
					+ "JOIN continents c2 on r.continent_id =c2.continent_id \r\n" + "WHERE c.name like ? \r\n"
					+ "ORDER BY c.name ";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				System.out.println("Fai una ricerca per nome o lettere presenti nel nome della Nazione: ");
				
				ps.setString(1, "%"+s.nextLine()+"%");

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Nation: " + rs.getString(1) + " Id: " + rs.getString(2) + " Region: "
								+ rs.getString(3) + " Continent: " + rs.getString(4));
					}
				}
			}
		}
		s.close();
	}

}
