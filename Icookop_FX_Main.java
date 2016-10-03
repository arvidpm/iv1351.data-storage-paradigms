package Projekt;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Icookop_FX_Main extends Application {

    // DB connection variable
    static protected Connection con;

    // DB access variables
    private String URL = "jdbc:ucanaccess://C:/Users/arvid/workspace/datalagring/src/Icooköp_v5.accdb";
    private String driver = "net.ucanaccess.jdbc.UcanaccessDriver";

    private Stage window;
    private Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {

        Icookop_FX_Main o = new Icookop_FX_Main();
        o.connect();

        window = primaryStage;
        primaryStage.setTitle("Window");

        //Label
        Label label1 = new Label("Welcome to Datalagring laboration!");
        Label label2 = new Label("Please choose which database to connect to:");


        //Button 1
        Button button1 = new Button("Test1");
        button1.setOnAction(e -> window.setScene(scene2));
        Button button2 = new Button("Test2");
        button2.setOnAction(e -> window.setScene(scene2));

        //Layout 1
        VBox layout1 = new VBox(10);
        layout1.getChildren().addAll(label1, label2, button1, button2);
        scene1 = new Scene(layout1, 700, 500);

        //Button 2
        Button button3 = new Button("Scene 1");
        button3.setOnAction(e -> window.setScene(scene1));

        //Layout 2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button3);
        scene2 = new Scene(layout2, 700, 500);

        window.setScene(scene1);
        window.setTitle("Icooköp v1");
        window.show();

    }

    // Method for establishing a DB connection
    public void connect() {
        try {
            // Register the driver with DriverManager
            Class.forName(driver);
            // Create a connection to the database
            con = DriverManager.getConnection(URL);
            // Set the auto commit of the connection to false.
            // An explicit commit will be required in order to accept
            // any changes done to the DB through this connection.
            con.setAutoCommit(false);
            // Some logging
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method displaying all available products.
    public void productSelect() throws Exception {
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
        while (rs.next()) {
            System.out.println(rs.getString("namn"));
        }

        // Close the variable stmt and release all resources bound to it
        // Any ResultSet associated to the Statement will be automatically closed too.
        stmt.close();
    }

    // Method for presenting all available brands of a selected product.
    public void showBrands() throws Exception {
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
        while (rs.next()) {
            System.out.println(rs.getString("namn"));
        }

        // Close the variable stmt and release all resources bound to it
        // Any ResultSet associated to the Statement will be automatically closed too.
        stmt.close();
    }

    // Method for generating and returning a randomized card number in the range of 1000-9999.
    // The range can be edited in the main method.
    protected int randomNumber(int min, int max) throws Exception {
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

        // If result = 1, we call ourself for a new number
        while (result == true) {
            return randomNumber(min, max);
        }

        // If result = 0, our generated number is unique and can be used as a new card number.
        return internalNumber;
    }

    public void insertStamkund(int randomNum) throws Exception {
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
}
