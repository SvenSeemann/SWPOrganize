package de.tud.swporganize.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class OverviewController extends AbstractController {

    @FXML
    private TextField txtNamePrefix;

    @FXML
    private TextField txtJenkinsName;

    @FXML
    private TextField txtGhName;

    @FXML
    private TextField txtGhOauth;

    @FXML
    private PasswordField txtJenkinsPwd;

    @FXML
    private TextField txtGrpCount;

    @FXML
    private void getInputFields() {
        String namePrefix = this.txtNamePrefix.getText();
        String jenkinsUsername= this.txtJenkinsName.getText();
        char[]jenkinsPassword = this.txtJenkinsPwd.getText().toCharArray();
        String ghUsername = this.txtGhName.getText();
        String ghOauth = this.txtGhOauth.getText();

        Optional<Integer> grpCount = this.evaluateGrpCount();
        if(!grpCount.isPresent()) {
            showNoNumberErrorDialog();
            return;
        }

        System.out.println(namePrefix);
    }

    private Optional<Integer> evaluateGrpCount() {
        try {
            Integer grpCount = Integer.parseInt(this.txtGrpCount.getText());
            return Optional.of(grpCount);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private void showNoNumberErrorDialog() {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText("Falsche Eingabe");
        error.setContentText("Bitte geben Sie als Gruppenanzahl nur einen numerischen Wert ein.");
        error.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
