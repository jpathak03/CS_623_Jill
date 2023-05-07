import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TransactionExample {

	public static void main(String[] args) throws SQLException, IOException, 
	ClassNotFoundException {
		
		// Load the Postgresql driver
				Class.forName("org.postgresql.Driver");
				
				// Connect to the default database with credentials
				
				Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydatabase","postgres","newroot");
		
				// For atomicity
				conn.setAutoCommit(false);
				
				// For isolation
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				
				Statement stmt = null;
				try {
					// Create statement object
					stmt = conn.createStatement();
					
					//Using CASCADE to maintain integrity and consistency
					//stmt1.executeUpdate("ALTER TABLE stock DROP CONSTRAINT fk_prod_stock,ADD CONSTRAINT fk_prod_stock FOREIGN KEY(prodid)REFERENCES product(prodid)ON DELETE CASCADE ON UPDATE CASCADE;");
					stmt.executeUpdate("ALTER TABLE stock DROP CONSTRAINT fk_depot_stock,ADD CONSTRAINT fk_depot_stock FOREIGN KEY(depid)REFERENCES depot(depid)ON DELETE CASCADE ON UPDATE CASCADE;");
					
					
                    
                    //6.We add a depot (d100, Chicago, 100) in Depot and (p1, d100,100) in Stock

					stmt.executeUpdate("INSERT INTO depot VALUES ('d1','New York',9000)");
					stmt.executeUpdate("INSERT INTO stock VALUES ('p1','d1',1000)");
					
				   stmt.executeUpdate("INSERT INTO depot VALUES ('d100','Chicago',100)");
				   stmt.executeUpdate("INSERT INTO stock VALUES ('p1','d100',100)");
				    
				  //4.The depot d1 changes its name to dd1 in Depot and Stock.
					
					stmt.executeUpdate("UPDATE depot SET depid='dd1' WHERE depid ='d1'");
					
					//2.The depot dd1 is deleted from Depot and Stock.
					
					
				   stmt.executeUpdate("DELETE FROM depot WHERE depid ='dd1'");
				}
				catch(SQLException e) {
					System.out.println("Transaction Failed, PERFORMING ROLLBACK \n"+e);
					
					// for atomicity
					conn.rollback();
					stmt.close();
					conn.close();
					return;
				}
				System.out.println("Transaction Successful, PERFORMING COMMIT \n");
				conn.commit();
				stmt.close();
				conn.close();
				
	}
}