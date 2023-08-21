package wwf.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce la connessione al database.
 */
public class DBAccess {

	/** URL del server mysql. */
	private static final String SERVER_URL = "137.116.205.176";
	
	/** porta del server in ascolto per la connessione. */
	private static final int PORT = 3306;
	
	/** nome utente del database. */
	private static final String DB_USER = "wwf";
	
	/** password per l'utente del database. */
	private static final String DB_PASSWORD = "Airone72100";
	
	/** nome del database. */
	private static final String DB_NAME = "wwf";
	
	/** nome del driver mysql. */
	private static final String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	
	/** nome DBMS. */
	private static final String DBMS = "jdbc:mysql";

	/** connessione. */
	private static Connection conn;
	

	/**
	 * Inizializza la connessione sql.
	 *
	 * @throws SQLException the SQL exception
	 */
	private static void initConnection() throws SQLException {
		if (conn == null) {
			String connectionString = DBMS + "://" + SERVER_URL 
			        + ":" + PORT + "/" + DB_NAME;
			try {
				Class.forName(DRIVER_CLASS_NAME).newInstance();
			} catch (Exception ex) {
				System.out.println("Impossibile trovare il Driver: " 
			+ DRIVER_CLASS_NAME);
			}

			try {
				conn = DriverManager.getConnection(connectionString,
				        DB_USER, DB_PASSWORD);
			} catch (SQLException e) {
				System.out.println("Impossibile connettersi al DB");
				throw e;
			}
		}
	}

	/**
	 * restituisce la connessione al db.
	 *
	 * @return connessione
	 * @throws SQLException the SQL exception
	 */
	public static Connection getConnection() throws SQLException {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			throw e;
		}
		conn = null;
		initConnection();
		return conn;
	}

}
