package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        primaryStage.setTitle("Signal Flow Graph");
        primaryStage.setScene(new Scene(root, 1200 , 600));


        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
