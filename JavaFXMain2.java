import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXMain2 extends Application {

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        primaryStage.setTitle("Window");

        //Label
        Label label1 = new Label("Test program 101");


        HBox bottomMenu = new HBox();
        Button buttonA = new Button("File");
        Button buttonB = new Button("Edit");
        Button buttonC = new Button("View");

        bottomMenu.getChildren().addAll(buttonA, buttonB, buttonC);

        VBox leftMenu = new VBox();
        Button buttonD = new Button("D");
        Button buttonE = new Button("E");
        Button buttonF = new Button("F");

        leftMenu.getChildren().addAll(buttonD, buttonE, buttonF);


        BorderPane borderPane = new BorderPane();

        borderPane.setBottom(bottomMenu);
        borderPane.setLeft(leftMenu);

        Scene scene = new Scene(borderPane, 300, 250);

/*        //Button 1
        Button button1 = new Button("Access DB");
        button1.setOnAction(e -> window.setScene(scene2));

        //Layout 1
        VBox layout1 = new VBox(10);
        layout1.getChildren().addAll(label1,button1);
        scene1 = new Scene(layout1,700,500);

        //Button 2
        Button button2 = new Button("Scene 1");
        button2.setOnAction(e -> window.setScene(scene1));

        //Layout 2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button2);
        scene2 = new Scene(layout2,700,500);
        window.setScene(scene1);
*/
        window.setScene(scene);
        window.setTitle("Program title");
        window.show();


    }

}
