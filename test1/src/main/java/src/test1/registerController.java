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

public class registerController {

    @FXML
    private Button register;

    @FXML
    private Button back_login;
    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private PasswordField txt_confirmPassword;

    @FXML
    private TextField txt_fullname;

    @FXML
    private TextField txt_email;

    private String db_url;
    private String db_user;
    private String db_password;

    {
        try {
            Properties props = new Properties();
            props.load(registerController.class.getResourceAsStream("/config.properties"));
            db_url = props.getProperty("DB_URL");
            db_user = props.getProperty("DB_USER");
            db_password = props.getProperty("DB_PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        register.setOnAction(this::handleRegisterButtonAction);
        back_login.setOnAction(this::handleBackLoginButtonAction);
    }

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) {
        String username = txt_username.getText();
        String password = txt_password.getText();
        String confirmPassword = txt_confirmPassword.getText();
        String fullname = txt_fullname.getText();
        String email = txt_email.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullname.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Đăng ký thất bại", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Đăng ký thất bại", "Mật khẩu xác nhận không khớp!");
            return;
        }

        if (registerUser(username, password, fullname, email)) {
            showAlert(Alert.AlertType.INFORMATION, "Đăng ký thành công", "Tài khoản đã được tạo thành công!");
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/LoginUI.fxml"));
                Parent loginRoot = fxmlLoader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(loginRoot);
                stage.setScene(scene);
                stage.setTitle("Login");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Đăng ký thất bại", "Có lỗi xảy ra, vui lòng thử lại!");
        }
    }

    private boolean registerUser(String username, String password, String fullname, String email) {
        boolean isRegistered = false;
        Connection connection = null;
        PreparedStatement accountStatement = null;
        PreparedStatement userStatement = null;
        PreparedStatement idStatement = null;
        ResultSet resultSet = null;

        try {
            // Kết nối đến cơ sở dữ liệu
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(db_url, db_user, db_password);

            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Lấy UserID tiếp theo
            String getIdQuery = "SELECT MAX(UserID) FROM User"; // Lấy ID lớn nhất trong bảng User
            idStatement = connection.prepareStatement(getIdQuery);
            resultSet = idStatement.executeQuery();

            int nextUserId = 1; // Mặc định ID đầu tiên là 1
            if (resultSet.next()) {
                nextUserId = resultSet.getInt(1) + 1; // Tăng ID lên 1
            }

            System.out.println("Next UserID: " + nextUserId); // Debug UserID

            // Chèn vào bảng User trước
            String userQuery = "INSERT INTO User (UserID, FullName, Email) VALUES (?, ?, ?)";
            userStatement = connection.prepareStatement(userQuery);
            userStatement.setInt(1, nextUserId);
            userStatement.setString(2, fullname);
            userStatement.setString(3, email);
            userStatement.executeUpdate();

            System.out.println("Inserted into User table!"); // Debug User

            // Chèn vào bảng Account sau
            String accountQuery = "INSERT INTO Account (UserName, Password, UserID) VALUES (?, ?, ?)";
            accountStatement = connection.prepareStatement(accountQuery);
            accountStatement.setString(1, username);
            accountStatement.setString(2, password);
            accountStatement.setInt(3, nextUserId); // UserID giống nhau
            accountStatement.executeUpdate();

            System.out.println("Inserted into Account table!"); // Debug Account

            // Commit transaction nếu cả hai thành công
            connection.commit();
            isRegistered = true;

        } catch (SQLException e) {
            System.err.println("SQL Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("SQL Message: " + e.getMessage());
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback(); // Quay lui nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("General Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (idStatement != null) idStatement.close();
                if (accountStatement != null) accountStatement.close();
                if (userStatement != null) userStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
        return isRegistered;
    }





    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void handleBackLoginButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/LoginUI.fxml"));
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