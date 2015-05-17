package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

/**
 *
 * @author svenseemann
 */
public abstract interface Scm extends JenkinsJobXml.Element {

    @XBWrite("/scm/@class")
    public Scm setScmClass(String scmClass);

    @XBWrite("/scm/@plugin")
    public Scm setScmPlugin(String scmPluginName);

    @XBDocURL("resource://jenkinsJobConfig/scmTemplates/_scmGitTemplate.xml")
    public interface ScmGit extends Scm {
        @XBWrite("/scm/userRemoteConfigs/hudson.plugins.git.UserRemoteConfig/url/*")
        ScmGit setScmUrl(String scmUrl);

        @XBWrite("/scm/userRemoteConfigs/hudson.plugins.git.UserRemoteConfig/credentialsId/*")
        ScmGit setCredentialsId(String credentialsId);
    }

    @XBDocURL("resource://jenkinsJobConfig/scmTemplates/_scmBlankTemplate.xml")
    public interface ScmNone extends Scm {
    }
}
