import java.sql.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author Narayan
 */

public class DynamicTable extends Application{

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;
    private TableView tableview;

    // DB connection variable
    static protected Connection con;

    // DB access variables
    private String URL = "jdbc:ucanaccess://C:/Users/arvid/workspace/datalagring/src/Icooköp_v5.accdb";
    private String driver = "net.ucanaccess.jdbc.UcanaccessDriver";

    //MAIN EXECUTOR
    public static void main(String[] args) {
        launch(args);
    }

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //CONNECTION DATABASE
    public void buildData(){
        data = FXCollections.observableArrayList();
        try{
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * FROM Produktgrupp";
            //ResultSet
            ResultSet rs = con.createStatement().executeQuery(SQL);

            /**********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             **********************************/
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableview.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableview.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }


    @Override
    public void start(Stage stage) throws Exception {

        DynamicTable o = new DynamicTable();

        // Connecting to Access DB
        o.connect();

        //TableView
        tableview = new TableView();
        buildData();
        // Creating object of this class



        //Main Scene
        Scene scene = new Scene(tableview);

        stage.setScene(scene);
        stage.show();
    }
}