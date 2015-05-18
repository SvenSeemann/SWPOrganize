package de.tud.jenkins;

import de.tud.swporganize.controller.OverviewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author svenseemann
 */
public class JenkinsServerManager extends Observable {

    private static JenkinsServerManager instance;

    private String hostname;
    private static final String JENKINS_WEBHOOK_PATH = "/github-webhook/";
    private static final String CREATE_PATH = "/createItem";
    private static final String CREDENTIALS_PATH = "/credential-store/domain";
    private static final String XML_API_PATH = "/api/xml";

    private String jenkinsUsername;
    private char[] jenkinsPasswordEnc;

    private OverviewController overviewController;

    public static JenkinsServerManager instanceOf() {
        if (instance == null) {
            instance = new JenkinsServerManager();
        }

        return instance;
    }

    private JenkinsServerManager() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
            this.overviewController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpURLConnection getCreateJobConnection(
            String jobName,
            String username,
            char[] password) {

        URL createJobUrl = this.createUrlCreateJob(jobName);
        HttpURLConnection connection =
                this.getPostConnection(createJobUrl, username, password);
        connection.setRequestProperty("Content-Type", "application/xml");

        return connection;
    }

    private HttpURLConnection getPostConnection(
            URL url,
            String username,
            char[] password) {
        this.setChanged();

        HttpURLConnection connection = null;
        try {
            connection = this.getBaseAuthConnection(url, username, password);
            connection.setRequestMethod("POST");
        } catch (ProtocolException ex) {
            this.notifyObservers(true);
            Logger.getLogger(JenkinsServerManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return connection;
    }

    private HttpURLConnection getBaseAuthConnection(
            URL url,
            String username,
            char[] password) {

        HttpURLConnection connection = null;
        connection = this.getBasicConnection(url);
        connection.setRequestProperty(
                "Authorization",
                "Basic " + this.createCredentialsString(username, password)
        );

        return connection;
    }

    private HttpURLConnection getBasicConnection(URL url) {
        this.setChanged();

        HttpURLConnection basicConnection = null;

        try {
            basicConnection = (HttpURLConnection) url.openConnection();
            basicConnection.setRequestProperty("charset", "utf-8");
            basicConnection.setDoOutput(true);
        } catch (IOException ex) {
            this.notifyObservers(true);
            Logger.getLogger(JenkinsServerManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return basicConnection;
    }

    /**
     * This method takes username and password and formats them
     * concatenated and base64 encoded for http basic auth.
     *
     * @param username the username to use
     * @param password the password for the user
     * @return the created string in http basic auth format
     */
    private String createCredentialsString(String username, char[] password) {
        String credentialsString = username + ":" + new String(password);
        byte[] credentialsEncoded =
                Base64.encodeBase64URLSafe(credentialsString.getBytes());

        return new String(credentialsEncoded);
    }

    public String createWebhookTarget() {
        return this.hostname + JenkinsServerManager.JENKINS_WEBHOOK_PATH;
    }

    private URL createUrlCreateJob(String jobName) {
        this.setChanged();
        String urlString = this.hostname
                + JenkinsServerManager.CREATE_PATH
                + "?name=" + jobName;
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            this.notifyObservers();
            Logger.getLogger(JenkinsServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return url;
    }

    private URL createUrlCredentials(String domain) {
        this.setChanged();
        String urlString = this.hostname
                + JenkinsServerManager.CREDENTIALS_PATH
                + "/" + domain
                + JenkinsServerManager.XML_API_PATH;

        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            this.notifyObservers(true);
            Logger.getLogger(JenkinsServerManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return url;
    }

    public void createJob(
            String jobName,
            String configXml,
            String username,
            char[] password) {

        this.setChanged();
        OutputStream os;

        try {
            byte[] postDataBytes = configXml.getBytes("UTF-8");
            HttpURLConnection connection =
                    this.getCreateJobConnection(jobName, username, password);

            os = connection.getOutputStream();
            os.write(postDataBytes);
            os.close();

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                Logger.getLogger(JenkinsServerManager.class.getName())
                        .log(Level.INFO, line);
            }

            connection.disconnect();
        } catch (Exception e) {
            this.notifyObservers();
            Logger.getLogger(JenkinsServerManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getGitHubCredentials(String username, char[] password) {
        this.setChanged();
        HttpURLConnection connection =
                this.getBaseAuthConnection(this.createUrlCredentials("GitHub"), username, password);
        String credentialsString = null;
        try {
            Document credentialsDocument = this.readXml(connection.getInputStream());
            credentialsString = credentialsDocument
                    .getElementsByTagName("credentials")
                    .item(0)
                    .getChildNodes()
                    .item(0)
                    .getNodeName();

            /**
             * This is a workaround because when reading credentialsId from xml api
             * a character is added to the credentialsId (maybe id of used domain.
             * For global domain the prefix is '_'.
             */
            credentialsString = "0" + credentialsString;
        } catch (IOException ex) {
            this.notifyObservers();
            Logger.getLogger(JenkinsServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(String.format("The credentials string is: %s", credentialsString));
        return credentialsString;
    }

    private Document readXml(InputStream in) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;

        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(in);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(JenkinsServerManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return document;
    }

    private void displayState(String message) {

    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setJenkinsUsername(String jenkinsUsername) {
        this.jenkinsUsername = jenkinsUsername;
    }

    public String getJenkinsUsername() {
        return jenkinsUsername;
    }

    public void setJenkinsPassword(char[] jenkinsPassword) {
        this.jenkinsPasswordEnc = jenkinsPassword;
    }

    public char[] getJenkinsPassword() {
        return jenkinsPasswordEnc;
    }
}
