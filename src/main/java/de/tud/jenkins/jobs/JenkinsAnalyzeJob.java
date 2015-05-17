package de.tud.jenkins.jobs;

import de.tud.jenkins.jobs.xml.Publisher;
import de.tud.jenkins.jobs.xml.Scm;
import de.tud.jenkins.jobs.xml.Trigger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author svenseemann
 */
public class JenkinsAnalyzeJob extends JenkinsJob {

    public JenkinsAnalyzeJob() {

    }

    public void addSonar(String jdkName, String mvnName) {
        Publisher.Sonar sonar = null;

        try {
            sonar = super.getProjector().io().fromURLAnnotation(Publisher.Sonar.class);
            sonar.setJdkName(jdkName);
            sonar.setMavenName(mvnName);
            super.publishers.add(sonar);
        } catch (IOException e) {
            Logger.getLogger(JenkinsAnalyzeJob.class.getName())
                    .log(Level.SEVERE, null, e);
        }
    }

    public void addScmNone(String scmClass, String scmPlugin) {
        Scm.ScmNone scm;

        try {
            scm = super.getProjector().io().fromURLAnnotation(Scm.ScmNone.class);
            scm.setScmClass(scmClass);

            super.scm = scm;
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void addReverseBuildTrigger(String projectName) {
        Trigger.ReverseBuildTrigger buildTrigger = null;

        try {
            buildTrigger = super.getProjector().io().fromURLAnnotation(Trigger.ReverseBuildTrigger.class);
            buildTrigger.setUpstreamProject(projectName);

            super.triggers.add(buildTrigger);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
