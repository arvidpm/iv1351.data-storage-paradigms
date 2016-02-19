package Projekt;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Icookop_console
{
	
    // DB connection variable
    static protected Connection con;
    
    // DB access variables
    private String URL = "jdbc:ucanaccess://C:/Users/arvid/workspace/datalagring/src/Icooköp_v5.accdb";
    private String driver = "net.ucanaccess.jdbc.UcanaccessDriver";
    
    // Method for establishing a DB connection
    public void connect()
    {
        try
        {
            // Register the driver with DriverManager
            Class.forName(driver);
            // Create a connection to the database
            con = DriverManager.getConnection(URL);
            // Set the auto commit of the connection to false.
            // An explicit commit will be required in order to accept
            // any changes done to the DB through this connection.
            con.setAutoCommit(false);
				// Some logging
				System.out.println("Ansluten till " + URL + " genom "+ driver);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Method displaying all available products.
    public void productSelect() throws Exception
    {
        // Local variables
        String query;
        ResultSet rs;
        Statement stmt;

        // Set the SQL statement into the query variable
        query = "SELECT namn FROM Produktgrupp ORDER BY namn ASC";

        // Create a statement associated to the connection con.
        // The new statement is placed in the variable stmt.
        stmt = con.createStatement();

        // Execute the SQL statement that is stored in the variable query
        // and store the result in the variable rs.
        rs = stmt.executeQuery(query);

        System.out.println("Alla produkttyper är:\n");

        // Loop through the result set and print the results.
        // The method next() returns false when there are no more rows.
        while (rs.next())
        {
            System.out.println(rs.getString("namn"));
        }

        // Close the variable stmt and release all resources bound to it
        // Any ResultSet associated to the Statement will be automatically closed too.
        stmt.close();
    }

    // Method for presenting all available brands of a selected product.
    public void showBrands() throws Exception
    {
        // Local variables
        String query;
        ResultSet rs;
        PreparedStatement stmt;
        String produkttyp;

        // Create a Scanner in order to allow the user to provide input.
        Scanner in = new Scanner(System.in, "Cp850");

        // Ask the user to specify a value for Produkttyp.
        System.out.print("Ange produkttyp: ");
        
        // Retrieve the value and place it in the variable produkttyp.
        produkttyp = in.nextLine();

        // Set the SQL statement into the query variable
        query = "SELECT DISTINCT Märke.namn FROM Märke, Märkesprodukt, Produkt, Produktbeskrivning, Produktgrupp WHERE Märke.[märke_id]=Märkesprodukt.[märke_id] AND Märkesprodukt.[produkt_id]=Produkt.[produkt_id] AND Produkt.[pbeskrivning_id]=Produktbeskrivning.[pbeskrivning_id] AND Produktbeskrivning.[pgrupp_id]=Produktgrupp.[pgrupp_id] AND Produktgrupp.[namn]= ?";

        // Create a statement associated to the connection and the query.
        // The new statement is placed in the variable stmt.
        stmt = con.prepareStatement(query);

        // Provide the value for the first ? in the SQL statement.
        // The value of the variable produkttyp will be sent to the database manager
        // through the variables stmt and con.
        stmt.setString(1, produkttyp);

        // Execute the SQL statement that is prepared in the variable stmt
        // and store the result in the variable rs.
        rs = stmt.executeQuery();

        System.out.println("\nFöljande märken finns för " + produkttyp + ":\n");

        // Loop through the result set and print the results.
        // The method next() returns false when there are no more rows.
        while (rs.next())
        {
            System.out.println(rs.getString("namn"));
        }

        // Close the variable stmt and release all resources bound to it
        // Any ResultSet associated to the Statement will be automatically closed too.
        stmt.close();
    }

    // Method for generating and returning a randomized card number in the range of 1000-9999.
    // The range can be edited in the main method.
    protected int randomNumber(int min, int max) throws Exception{

        // Local variables
        String query;
        ResultSet rs;
        PreparedStatement stmt;
        int internalNumber;

        // A random number is generated in the selected range min-max (1000-9999)
        Random rand = new Random();
        internalNumber = rand.nextInt((max - min) + 1) + min;

        // Set the SQL statement into the query variable
        // We check if our generated number exists in the database.
        query = "SELECT kortnummer FROM Stamkund WHERE kortnummer = ?";
        stmt = con.prepareStatement(query);
        stmt.setInt(1, internalNumber);

        // Execute the SQL statement that is prepared in the variable stmt
        // and store the result in the variable rs.
        // If we receive a ResultSet, boolean result is set to 1, and the generated number exists.
        rs = stmt.executeQuery();
        boolean result = rs.next();

        // If result is true, we call ourself for a new number
        if (result){
            return randomNumber(min, max);
        }

        // If result = 0, our generated number is unique and can be used as a new card number.
        return internalNumber;
    }

    public void insertStamkund(int randomNum) throws Exception
    {
        // Local variables
        String query;
        PreparedStatement stmt;
        String pnrparam, fnamnparam, enamnparam, adrparam, epostparam, telparam;
        
        // Stores our randomized number in the variable kortnrparam
        int kortnrparam = randomNum;
                
        
        // Create a Scanner in order to allow user input.
        Scanner in = new Scanner(System.in, "Cp850");

        System.out.println("--Registrera ny stamkund--");
        
        // Ask the user for registration details. Kortnummer is already provided.
        System.out.println("Personnummer (XXXXXX-XXXX): ");
        pnrparam = in.nextLine();
        System.out.print("Förnamn: ");
        fnamnparam = in.nextLine();
        System.out.print("Efternamn: ");
        enamnparam = in.nextLine();
        System.out.print("Adress: ");
        adrparam = in.nextLine();
        System.out.print("E-post: ");
        epostparam = in.nextLine();
        System.out.print("Mobilnummer: ");
        telparam = in.nextLine();

        // Set the SQL statement into the query variable
        query = "INSERT INTO Stamkund VALUES (?,?,?,?,?,?,?)";
        
        // Create a statement associated to the connection and the query.
        // The new statement is placed in the variable stmt.
        stmt = con.prepareStatement(query);

        // Provide the values for the ?'s in the SQL statement, from 1 to 7.
        stmt.setInt(1, kortnrparam);
        stmt.setString(2, pnrparam);
        stmt.setString(3, fnamnparam);
        stmt.setString(4, enamnparam);
        stmt.setString(5, adrparam);
        stmt.setString(6, epostparam);
        stmt.setString(7, telparam);
        
        // Execute the SQL statement that is prepared in the variable stmt
        stmt.executeUpdate();
        
        // Prints card number information to customer
        System.out.println("\nVälkommen som stamkund hos Icooköp!\nDitt kortnummer är: " + kortnrparam);

        // Close the variable stmt and release all resources bound to it
        stmt.close();
        
        // Commit the changes made to the database.
        con.commit();
        
    }

/*
    // This is a method for inserting hard-coded data into the Access DB.
    
    public void testinsert() throws Exception
    {
        // Local variables
        String query;
        PreparedStatement stmt;
        System.out.println("--Hårdkodad registrering av ny stamkund--");

        // Set the SQL statement into the query variable
        query = "INSERT INTO Stamkund (kortnummer,personnummer,förnamn,efternamn,adress,epost,mobilnummer) VALUES (1111, '0101010101', 'TestIgen', 'Testigen2', 'testvägen 555', 'asdasdasd','0704044444')";
        
        // Create a statement associated to the connection and the query.
        // The new statement is placed in the variable stmt.
        stmt = con.prepareStatement(query);
        
        // Execute the SQL statement that is prepared in the variable stmt
        stmt.executeUpdate();

        // Close the variable stmt and release all resources bound to it
        stmt.close();
    }
*/
    
    public static void main(String[] args) throws Exception
    {
    	
        // Create a new object of this class.
        Icookop_console o = new Icookop_console();
        o.connect();
        
        Scanner in = new Scanner(System.in, "Cp850");
        int userChoice;
        boolean quit = false;
        
		do {
			
			// Display menu to user
			// User input and validation
			System.out.println();
			System.out.println("1) Visa alla produkttyper");
			System.out.println("2) Visa alla märken av en vald produkttyp");
			System.out.println("3) Registrera ny stamkund");
			System.out.print("");
			System.out.println("4) testinsert()");
			System.out.print("");
			System.out.println("5) Avsluta");
			System.out.println();
			System.out.print("Gör ett val [1-5]: ");
			userChoice = in.nextInt();
        	switch (userChoice) {
        	
        	case 1: 
            		o.productSelect();
                    break;
                    
            case 2: 
            		o.showBrands();
                    break;
                    
            // Generation and check of a fictional card number.
            // Min/max values can be adjusted for a different range.
            case 3: 
            		int randomNumber = o.randomNumber(1000, 9999);
            		o.insertStamkund(randomNumber);
            		break;
            		
//          case 3: Call testinsert if you would like to insert hard-coded data into the database.
//                	o.testinsert();
                    
            case 5: System.out.println("Välkommen åter!");
            		quit = true;
            		break;
            
            default: System.out.println("\nFelaktigt val");
            }
		}
		while (!quit);

        // Commit the changes made to the database and close the connection.
    }
}