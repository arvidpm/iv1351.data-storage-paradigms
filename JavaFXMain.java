package Projekt;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class JavaFXMain extends Application {

    Stage window;
    Scene scene1, scene2;

    public static void main(String[] args) {
        launch(args);

    }

    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        primaryStage.setTitle("Window");

        //Label
        Label label1 = new Label("Welcome to Datalagring laboration");
        Label label2 = new Label("Please choose which database to connect to!");


        //Button 1
        Button button1 = new Button("Access DB");
        button1.setOnAction(e -> window.setScene(scene2));
        Button button2 = new Button("MySQL DB");
        button2.setOnAction(e -> window.setScene(scene2));

        //Layout 1
        VBox layout1 = new VBox(10);
        layout1.getChildren().addAll(label1, button1, button2);
        scene1 = new Scene(layout1, 700, 500);

        //Button 2
        Button button3 = new Button("Scene 1");
        button3.setOnAction(e -> window.setScene(scene1));

        //Layout 2
        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button3);
        scene2 = new Scene(layout2, 700, 500);

        window.setScene(scene1);
        window.setTitle("Program title");
        window.show();


    }

}
