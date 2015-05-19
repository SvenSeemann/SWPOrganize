package de.tud.github;

import de.tud.util.LogStatementHelper;

import org.kohsuke.github.GHEvent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the Connection to GitHub, the creation of repositories,
 * the creation of WebHook and the creation of Milestones.
 *
 * It's implemented as Singleton, so to retrieve the current
 * instance of it call the static instanceOf() method.
 *
 * @author svenseemann
 */
public class GitHubManager extends Observable {

    /** holds the singleton instance */
    private static GitHubManager instance;

    /**
     * holds the GitHub-Username.
     * {@see OverviewController} sets the Username in
     * startSetup() method
     */
    private String username;

    /**
     * oauthToken used to authenticate with GitHub.
     * {@see OverviewController} sets the Token in
     * startSetup() method
     */
    private String oauthToken;

    /**
     * The github instance with the connection to the gh api.
     */
    private GitHub github;

    /**
     * Private Constructor (singleton-pattern)
     */
    private GitHubManager() {}

    /**
     * Get the single running instance of GitHubManager.
     * @return GitHubManager Object
     */
    public static GitHubManager instanceOf() {
        return instance == null ? instance = new GitHubManager() : instance;
    }

    /**
     * Establishes connection to GitHub, via given username and oauthToken.
     * If connection fails warning statement is send to {@see OverviewController}
     * via {@see LogStatementHelper}.
     */
    public void connectToGitHub() {
        LogStatementHelper.instanceOf().addLogStatement("Connecting to GitHub...");
        try {
            System.out.println("Using as credentials:");
            System.out.println(this.username);
            System.out.println(this.oauthToken);
            github = GitHub.connect(this.username, this.oauthToken);
            LogStatementHelper.instanceOf().addLogStatement("Connection to GitHub established");
        } catch (IOException ex) {

            LogStatementHelper.instanceOf().addLogStatement("Could not establish connection to GitHub");
            Logger.getLogger(GitHubManager.class.getSimpleName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes a new repository for a group and creates depending milestones and the webhook to jenkins.
     *
     * @param name        the name of the repository
     * @param description the description text of this repository
     * @param homepage    the project url (e.g. URL to github pages)
     * @return an optional with the repository if everything is setup successfully or empty if an error occurred during repo creation
     */
    public Optional<GHRepository> setupGroupRepository(
            String name,
            String description,
            String homepage) {
        Optional<GHRepository> groupRepository = null;

        groupRepository = this.createPublicRepository(name, description, homepage);

        if (groupRepository.isPresent()) {
            groupRepository = this.addWebhook(
                    groupRepository.get(),
                    "" //TODO: input url for jenkins
            );
        } else {
            Logger.getLogger(GitHubManager.class.getSimpleName())
                    .warning(String.format("Couldn't add webhook to repository %s", name));
        }

        if (groupRepository.isPresent()) {
            groupRepository = this.createMilestones(groupRepository.get());
        } else {
            Logger.getLogger(GitHubManager.class.getSimpleName())
                    .warning(String.format("Couldn't add milestones to repository %s", name));
        }

        return groupRepository;
    }

    /**
     * Deletes the given repository from GitHub.
     *
     * @param repository the repository to delete
     */
    public void deleteRepository(GHRepository repository) {
        try {
            repository.delete();
        } catch (IOException ex) {
            Logger
                    .getLogger(GitHubManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates the repository on github with given attributes for name, description and homepage
     *
     * @param name        the name for the repository
     * @param description the description text of the repository
     * @param homepage    the URL to the homepage of the project (e.g. to GitHub pages)
     * @return
     */
    private Optional<GHRepository> createPublicRepository(String name, String description, String homepage) {
        try {
            GHRepository ghRepository = github
                    .createRepository(name, description, homepage, true);

            return Optional.of(ghRepository);
        } catch (IOException e) {
            e.printStackTrace();

            return Optional.empty();
        }
    }

    /**
     * Adds a webhook to the repository with the given endpoint.
     *
     * @param repository the repository to create the webhook for.
     * @param hookURL    the url of the endpoint of the needed webhook
     * @return an optional with the @class GHRepository on success or empty when an error occurred.
     */
    private Optional<GHRepository> addWebhook(GHRepository repository, String hookURL) {
        try {
            // Create events for webhook
            GHEvent pushEvent = GHEvent.PUSH;
            List<GHEvent> ghEvents = new ArrayList<>();
            ghEvents.add(pushEvent);


            repository.createWebHook(new URL(hookURL), ghEvents);
            return Optional.of(repository);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Creates the three milestones for the given @code GHRepository.
     *
     * @param repository the repository to create the milestones for
     * @return an optional with the @class GHRepository on success or empty when an error occurred.
     */
    private Optional<GHRepository> createMilestones(GHRepository repository) {
        try {
            repository.createMilestone("1st Milestone", "A short description of the first milestone");
            repository.createMilestone("2nd Milestone", "A short description of the second milestone");
            repository.createMilestone("3rd Milestone", "A short description of the third milestone");

            return Optional.of(repository);
        } catch (IOException e) {
            e.printStackTrace();

            return Optional.empty();
        }

    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
