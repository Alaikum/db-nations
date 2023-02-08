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
		String sceltaBonus;
		Scanner s = new Scanner(System.in);

		try (Connection con = DriverManager.getConnection(url, user, password);) {
			System.out.println("Connessione Effettuata con successo");

			// SCRIVO QUERY
			String sql = "SELECT c.name ,c.country_id, r.name , c2.name  FROM countries c \r\n"
					+ "JOIN regions r on c.region_id =r.region_id \r\n"
					+ "JOIN continents c2 on r.continent_id =c2.continent_id \r\n" + "WHERE c.name LIKE ? \r\n"
					+ "ORDER BY c.name ";
			try (PreparedStatement ps = con.prepareStatement(sql)) {
				System.out.println("Fai una ricerca per nome o lettere presenti nel nome della Nazione");
				System.out.println("Lascia il campo vuoto per tutte le nazioni \nAttento agli spazi vuoti: ");
				ps.setString(1, "%" + s.nextLine() + "%");

				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						System.out.println("Nation: " + rs.getString(1) + " Id: " + rs.getString(2) + " Region: "
								+ rs.getString(3) + " Continent: " + rs.getString(4));
					}

				}
			}

			// BONUS
			// Dopo aver stampato a video l’elenco delle country,
			// chiedere all’utente diinserire l’id di una delle
			// country.Sulla base di quell’id eseguire ulteriori
			// ricerche su database, cherestituiscano:● tutte le lingue parlate
			// in quella country● le statistiche più recenti per quella country
			System.out.println("\n\nBONUS");
			String sql2 = "SELECT c.name, l.`language`  from countries c \r\n"
					+ "JOIN country_languages cl on c.country_id =cl.country_id \r\n"
					+ "JOIN languages l on cl.language_id =l.language_id \r\n" + "WHERE c.country_id = ? ";

			String sql3 = "SELECT c.name,cs.`year` ,cs.population ,cs.gdp  FROM countries c \r\n"
					+ "JOIN country_stats cs on c.country_id =cs.country_id \r\n" + "WHERE c.country_id =?\r\n"
					+ "ORDER BY cs.`year` DESC \r\n" + "LIMIT 5";
			
			try (PreparedStatement ps2 = con.prepareStatement(sql2);
					PreparedStatement ps3 = con.prepareStatement(sql3)) {
				System.out.println("Inserisci l ID della nazione che ti interessa: ");
				sceltaBonus=s.nextLine();
				ps2.setString(1, sceltaBonus);
				ps3.setString(1, sceltaBonus);

				try (ResultSet rs2 = ps2.executeQuery(); ResultSet rs3 = ps3.executeQuery()) {

					String nazione = "";
					String linguaggi = "";
					while (rs2.next()) {
						nazione = rs2.getString(1);
						linguaggi += rs2.getString(2) + " ";

					}

					System.out.printf("Nella Nazione %s le lingue più parlate sono: - %s- \n", nazione.toUpperCase(), linguaggi);
					System.out.println("Statistiche degli ultimi anni: ");
					while (rs3.next()) {
						System.out.println(rs3.getString("year") + ":");
						System.out.println("-Popolazione: " + rs3.getString("cs.population"));
						System.out.println("-Prodotto Interno Lordo: " + rs3.getString("cs.gdp") + " $");
					}

				}
			}
		}
		s.close();
	}

}
