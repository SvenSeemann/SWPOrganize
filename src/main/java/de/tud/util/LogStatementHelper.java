package de.tud.util;

import java.util.Observable;

/**
 * This simple class is a Singleton Helper, which is callable everywhere
 * in this project. Use the addLogStatement(String msg) Function to add Text
 * to the TextArea on the right of the OverviewWindow.
 */
public class LogStatementHelper extends Observable{

    private static LogStatementHelper instance;

    private LogStatementHelper() {}

    public static LogStatementHelper instanceOf() {
        if(instance == null)
            instance = new LogStatementHelper();
        return instance;
    }

    /**
     * notifies Observers with given message.
     * Only Observer listening is OverviewController,
     * which adds the given message to his Statement Box.
     * @param message
     */
    public void addLogStatement(String message) {
        this.setChanged();
        this.notifyObservers(message);
    }

}
