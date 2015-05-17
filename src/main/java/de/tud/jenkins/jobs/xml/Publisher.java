package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

/**
 *
 * @author svenseemann
 */
@XBDocURL("resource://jenkinsJobConfig/publisherTemplates/_publishersBaseTemplate.xml")
public interface Publisher extends JenkinsJobXml.Element {

    @XBWrite("/publishers/*")
    Publisher setPublisherElements(List<PublisherElement> publisherElements);

    public interface PublisherElement {
    }

    @XBDocURL("resource://jenkinsJobConfig/publisherTemplates/_publishersBuildTriggerTemplate.xml")
    public interface BuildTrigger extends PublisherElement {

        @XBWrite("/hudson.tasks.BuildTrigger/childProjects/*")
        BuildTrigger setChildProject(String childProject);
    }

    @XBDocURL("resource://jenkinsJobConfig/publisherTemplates/_publishersSonarTemplate.xml")
    public interface Sonar extends PublisherElement {

        @XBWrite("/hudson.plugins.sonar.SonarPublisher/jdk/*")
        Sonar setJdkName(String jdkName);

        @XBWrite("/hudson.plugins.sonar.SonarPublisher/mavenInstallationName/*")
        Sonar setMavenName(String mavenName);
    }
}
