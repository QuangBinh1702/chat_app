package src.test1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class loginController {
    @FXML
    private Button login;
    @FXML
    private Button btn_register;
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try {
            Properties props = new Properties();
            props.load(loginController.class.getResourceAsStream("/config.properties"));
            DB_URL = props.getProperty("DB_URL");
            DB_USER = props.getProperty("DB_USER");
            DB_PASSWORD = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        login.setOnAction(event -> setLogin(event));
        btn_register.setOnAction(event -> handleBackRegisterButtonAction(event));
    }

    @FXML
    private void setLogin(ActionEvent event) {
        String username = txt_username.getText();
        String password = txt_password.getText();

        if (checkLogin(username, password)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/chat.fxml"));
                Parent chatRoot = fxmlLoader.load();

                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);


                String query = "SELECT UserID from account where UserName  = ? and Password  = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int userID = resultSet.getInt("UserID");
                    Controller controller = fxmlLoader.getController();
                    controller.getUserID(userID);
                    controller.addUsers();
                    controller.header.setVisible(false);
                    controller.footer.setVisible(false);
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(chatRoot);
                stage.setScene(scene);
                stage.setTitle("Chat Room");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Đăng nhập thất bại");
            alert.setHeaderText(null);
            alert.setContentText("Tên đăng nhập hoặc mật khẩu không chính xác!");
            alert.showAndWait();
        }

    }

    private boolean checkLogin(String username, String password) {
        boolean isValid = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select * from account where UserName  = ? and Password  = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isValid = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

    protected void handleBackRegisterButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/RegisterUI.fxml"));
            Parent loginRoot = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
