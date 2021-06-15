package sample.frontend.signuplogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.api.ApiHandler;
import sample.backend.signup.SignUpperLogInner;
import sample.backend.signup.User;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class SigningController implements Initializable
{
    @FXML
    public Label passwordAsterisk;
    @FXML
    public Label emailAsterisk;
    @FXML
    public Label usernameAsterisk;
    @FXML
    public ProgressBar progressbar;
    @FXML
    private Button cancelButton;
    @FXML
    private ImageView instagramImageView;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView titleImageView;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField email;
    @FXML
    private Label isNotNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File instagramFile = new File("Photo/instagram.png");
        Image instagramImage = new Image(instagramFile.toURI().toString());
        instagramImageView.setImage(instagramImage);

        File logoFile = new File("Photo/logo.jpg");
        Image logoImage = new Image(logoFile.toURI().toString());
        logoImageView.setImage(logoImage);

        File titleFile = new File("Photo/title.jpg");
        Image titleImage = new Image(titleFile.toURI().toString());
        titleImageView.setImage(titleImage);
    }

    public void okButtonSignupOnAction() throws IOException, InterruptedException
    {
        User newUser = new User(username.getText(),email.getText(),password.getText());
        SignUpperLogInner signUpper =  new SignUpperLogInner(newUser);
        ApiHandler apiHandler = new ApiHandler();
        if (signUpper.checkUserUniqueness())
        {
            if(signUpper.checkPasswordValidation())
            {
                if (signUpper.checkEmailValidation())
                {
                    apiHandler.setRequest(signUpper.makeRequest());
                    apiHandler.sendRequest();
                    email.setPromptText("email");
                    username.setPromptText("username");
                    password.setPromptText("password");
                    emailAsterisk.setText("");
                    username.setText("");
                    passwordAsterisk.setText("");
                    isNotNew.setText("");

                    alreadyHaveAnAccount();
                }
                else
                {
                    email.setPromptText("invalid email address");
                    emailAsterisk.setText("*");
                }
            }
            else
            {
                password.setPromptText("password is too weak");
                passwordAsterisk.setText("*");
            }
        }
        else
        {
            isNotNew.setText("An account with this info already exists\ntry another username or login instead");
            username.setPromptText("user already exists");
            usernameAsterisk.setText("*");
        }
    }

    public void alreadyHaveAnAccount() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 520, 400));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelButtonOnAction(ActionEvent event){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}