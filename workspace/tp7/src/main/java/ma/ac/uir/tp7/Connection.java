package ma.ac.uir.tp7;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
	// constructeur vide
	Connection() {
	}

	public static java.sql.Connection connect() throws SQLException {
		java.sql.Connection conn = null;
		try {
			conn = DriverManager
					.getConnection("jdbc:mysql://127.0.0.1:3306/tp7?" + "user=tp7user&password=tp7password");
			System.out.println("Connexion à la base de données réussie !");
		} catch (SQLException e) {
			System.err.println("Erreur de connexion à la base de données :");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
		return conn;
	}
}
