package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

/**
 *
 * @author svenseemann
 */
@XBDocURL("resource://jenkinsJobConfig/triggerTemplates/_triggerTemplate.xml")
public interface Trigger extends JenkinsJobXml.Element {

    @XBWrite("/triggers/*")
    Trigger setTriggerElements(List<TriggerElement> triggerElements);

    public interface TriggerElement {
    }

    @XBDocURL("resource://jenkinsJobConfig/triggerTemplates/_triggerGitHubTemplate.xml")
    public interface GitHubTrigger extends TriggerElement {

        @XBWrite("/com.cloudbees.jenkins.GitHubPushTrigger/@plugin")
        GitHubTrigger setPlugin(String plugin);
    }

    @XBDocURL("resource://jenkinsJobConfig/triggerTemplates/_triggerReverseTrigger.xml")
    public interface ReverseBuildTrigger extends TriggerElement {

        @XBWrite("jenkins.triggers.ReverseBuildTrigger/upstreamProjects/*")
        ReverseBuildTrigger setUpstreamProject(String projectName);
    }
}
