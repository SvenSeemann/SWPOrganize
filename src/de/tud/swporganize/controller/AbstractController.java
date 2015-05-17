package de.tud.swporganize.controller;

import de.tud.Main;
import javafx.fxml.Initializable;

/**
 * Created by svenseemann on 15.05.15.
 */
public abstract class AbstractController implements Initializable {

    private Main main;

    public void setMainApp(Main mainApp) {
        this.main = mainApp;
    }

    public Main getMain() {
        return main;
    }
}
