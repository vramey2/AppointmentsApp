package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.helper.JDBC;


/**This class creates an app for appointments scheduling.
 * Javadoc folder is included as a separate zipfolder.
 * @author Veronika Ramey
 */
public class Main extends Application {


    /**Method to set up primary stage for the application. The method sets up the main stage, loading the first scene.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 750, 600));
        primaryStage.show();
    }

    /**Main method which is called first when the program starts.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
        JDBC.openConnection();

        JDBC.closeConnection();
    }
}
