package wwf.ClassificationGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import wwf.ClassificationGame.data.SerializeObjects;
import wwf.ClassificationGame.view.Pippo;

public class ConnectionDatabase {
	private static final String SERVER_URL = "137.116.205.176";
	private static final int PORT = 3306;
	private static final String DB_USER = "wwf";
	private static final String DB_PASSWORD = "Airone72100";
	private static final String DB_NAME = "wwf";
	private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	private static final String DBMS = "jdbc:mysql";

	private static Connection conn;

	private static void initConnection() throws SQLException {
		if (conn == null) {
			String connectionString = DBMS + "://" + SERVER_URL + ":" + PORT + "/" + DB_NAME;
			try {
				Class.forName(DRIVER_CLASS_NAME).newInstance();
			} catch (Exception ex) {
				System.out.println("Impossibile trovare il Driver: " + DRIVER_CLASS_NAME);
			}

			try {
				conn = DriverManager.getConnection(connectionString, DB_USER, DB_PASSWORD);
			} catch (SQLException e) {
				System.out.println("Impossibile connettersi al DB. Tentativo di recupero dei dati da file.");
//				Pippo pippo = new Pippo();
//				pippo.run();
			}
		}
	}

	public static Connection getConnection() throws SQLException {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			throw e;
		}
		conn = null;
		initConnection();
		return conn;
	}

}
