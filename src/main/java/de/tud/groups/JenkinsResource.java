package de.tud.groups;


import de.tud.jenkins.JenkinsServerManager;
import de.tud.jenkins.jobs.xml.JenkinsJobXml;

/**
 * POJO holding all necessary configuration and references for the
 * Jenkins-Resource.
 *
 * @author svenseemann
 */
public class JenkinsResource {

    /**
     * Reference to Project-Group.
     */
    private Group group;

    /**
     * Jenkins' workspacePath.
     * All builds etc are made inside this workspace path.
     */
    private String workspacePath;

    /**
     * Job-Name for the project building process. (shown in Jenkins)
     */
    private final String buildJobName;

    /**
     * Job-Name for the project analyzing process. (shown in Jenkins)
     */
    private final String analyzeJobName;

    /**
     * Reference to the JenkinsXML Config for the Build-Job.
     */
    private JenkinsJobXml buildJob;

    /**
     * Reference to the JenkinsXML Config for the analyzing Job.
     */
    private JenkinsJobXml analyzeJob;

    /**
     * Build new Jenkins resource with given {@see Group} and workspace Path.
     *
     * @param group
     *          Reference to the SWP-Project Group object.
     * @param workspacePath
     *          Workspace Path in which Jenkins run all Jobs belonging to the given group.
     */
    public JenkinsResource(Group group, String workspacePath) {
        this.group = group;
        this.workspacePath = workspacePath;
        this.buildJobName = "Build_" + this.group.getName();
        this.analyzeJobName = "Analyze_" + this.group.getName();
    }

    /**
     * Push all configured Jobs to the defined Jenkins-Server.
     *
     * @return  false if everything went fine.
     */
    public boolean pushJobs() {
        String username = JenkinsServerManager.instanceOf().getJenkinsUsername();
        char[] password = JenkinsServerManager.instanceOf().getJenkinsPassword();

        JenkinsServerManager
                .instanceOf()
                .createJob(
                        this.buildJobName,
                        this.buildJob.toString(),
                        username,
                        password
                );

        JenkinsServerManager
                .instanceOf()
                .createJob(
                        this.analyzeJobName,
                        this.analyzeJob.toString(),
                        username,
                        password
                );

        return false;
    }

    // GETTER AND SETTER ###############################################################################################

    public JenkinsJobXml getAnalyzeJob() {
        return analyzeJob;
    }

    public void setAnalyzeJob(JenkinsJobXml analyzeJob) {
        this.analyzeJob = analyzeJob;
    }

    public JenkinsJobXml getBuildJob() {
        return buildJob;
    }

    public void setBuildJob(JenkinsJobXml buildJob) {
        this.buildJob = buildJob;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }
}
