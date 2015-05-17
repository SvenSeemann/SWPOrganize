package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

/**
 *
 * @author svenseemann
 */
@XBDocURL("resource://jenkinsJobConfig/propertyTemplates/_propertiesTemplate.xml")
public interface Property extends JenkinsJobXml.Element {

    @XBWrite("/properties/*")
    Property setProperties(List<PropertyElement> propertyElements);

    public interface PropertyElement {
    }

    @XBDocURL("resource://jenkinsJobConfig/propertyTemplates/_propertyGitHubPlugin.xml")
    public interface GitHubProperty extends PropertyElement {

        @XBWrite("/com.coravy.hudson.plugins.github.GithubProjectProperty/@plugin")
        GitHubProperty setPlugin(String plugin);

        @XBWrite("/com.coravy.hudson.plugins.github.GithubProjectProperty/projectUrl/*")
        GitHubProperty setProjectUrl(String projectUrl);
    }
}
