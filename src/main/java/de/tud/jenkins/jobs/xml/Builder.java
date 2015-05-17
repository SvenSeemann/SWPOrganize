package de.tud.jenkins.jobs.xml;


import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

/**
 *
 * @author svenseemann
 */

@XBDocURL("resource://jenkinsJobConfig/builderTemplates/_buildersTemplate.xml")
public interface Builder extends JenkinsJobXml.Element {

    @XBWrite("/builders/*")
    Builder setBuilders(List<BuilderElement> builderElements);

    public interface BuilderElement{
    }

    @XBDocURL("resource://jenkinsJobConfig/builderTemplates/_mavenBuilderTemplate.xml")
    public interface MavenBuilder extends BuilderElement {
        @XBWrite("hudson.tasks.Maven/targets/*")
        MavenBuilder setTarget(String target);
    }
}
