package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

/**
 *
 * @author svenseemann
 */
@XBDocURL("resource://jenkinsJobConfig/_customWorkspaceTemplate.xml")
public interface CustomWorkspace extends JenkinsJobXml.Element {

    @XBWrite("/customWorkspace/*")
    CustomWorkspace setCustomWorkspace(String pathToWorkspace);
}
