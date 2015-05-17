package de.tud.jenkins.jobs;

import de.tud.jenkins.jobs.xml.Builder;
import de.tud.jenkins.jobs.xml.Property;
import de.tud.jenkins.jobs.xml.Scm;
import de.tud.jenkins.jobs.xml.Trigger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author svenseemann
 */
public class JenkinsBuildJob extends JenkinsJob {

    public JenkinsBuildJob() {

    }

    /**
     * Generates the XML part that defines additional information to the project.
     * By now only github depending informations are supported.
     *
     * @param pluginName - the plugins name to use for additional informations
     * @param projectUrl - the URL to the project (e.g. URL to depending GitHub page)
     */
    public void addGitHubProperty(String pluginName, String projectUrl) {
        Property.GitHubProperty property;

        try {
            property = super.getProjector().io().fromURLAnnotation(Property.GitHubProperty.class);
            property.setPlugin(pluginName);
            property.setProjectUrl(projectUrl);

            super.properties.add(property);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Generates the XML part that defines the trigger to start the build job.
     * By now only GitHubPushTrigger is supported.
     *
     * @param pluginName - the plugins name to use for triggering builds
     */
    public void addGitHubTrigger(String pluginName) {
        Trigger.GitHubTrigger trigger;

        try {
            trigger = super.getProjector().io().fromURLAnnotation(Trigger.GitHubTrigger.class);
            trigger.setPlugin(pluginName);

            super.triggers.add(trigger);
        } catch (IOException e) {
            Logger
                    .getLogger(JenkinsBuildJob.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    public void addScmGit(
            String scmClass,
            String scmPlugin,
            String credentialsId,
            String scmUrl) {
        Scm.ScmGit scmToAdd;

        try {
            scmToAdd = super.getProjector().io().fromURLAnnotation(Scm.ScmGit.class);
            scmToAdd.setScmClass(scmClass);
            scmToAdd.setScmPlugin(scmPlugin);
            scmToAdd.setCredentialsId(credentialsId);
            scmToAdd.setScmUrl(scmUrl);

            super.scm = scmToAdd;
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Generates the XML part that manages the build process
     * by executing the passed maven targets.
     *
     * @param target - the target to execute in build process
     */
    public void addMavenBuilder(String target) {
        Builder.MavenBuilder builder;
        try {
            builder = super.getProjector().io().fromURLAnnotation(Builder.MavenBuilder.class);
            builder.setTarget(target);

            super.builders.add(builder);
        } catch (IOException ex) {
            Logger
                    .getLogger(JenkinsBuildJob.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
}
