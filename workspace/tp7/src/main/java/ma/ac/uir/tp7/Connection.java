package ma.ac.uir.tp7;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
	// constructeur vide
	Connection(){		
	}
	
	public static java.sql.Connection connect() throws SQLException{
		java.sql.Connection conn = null;
		try{
                    conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/tp7?" +
                            "user=root&password=");
		} 
		catch (SQLException e) {
		    System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: "     + e.getSQLState());
		    System.out.println("VendorError: "  + e.getErrorCode());
		}
		return conn;
	}
}
