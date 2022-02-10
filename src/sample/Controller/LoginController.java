package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.helper.JDBC;
import sample.helper.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable  {

    public TextField Userloginname;
    public TextField Password;
    @FXML
    private Button submitButton; 



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println ("Initialized!");
    }

    public void submitButtonClicked(ActionEvent event) throws SQLException, IOException {

        String userName = Userloginname.getText();
        String password = Password.getText();

        if (userName.isEmpty() || password.isEmpty()){

            System.out.println ("Please enter your user name ans password!");
        }

        else
        {
            JDBC.openConnection();
          boolean validate =  Query.select(userName, password);
          if (validate) {
              System.out.println("Success!");
              Stage stage;
              Parent scene;
              stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
              scene = FXMLLoader.load(getClass().getResource("/sample/View/Customers.fxml"));
              stage.setScene(new Scene(scene));
              stage.show();

          }
          else
              System.out.println("Enter valid credentials!");

        }



    }
}
