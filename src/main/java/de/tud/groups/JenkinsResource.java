package de.tud.groups;


import de.tud.jenkins.JenkinsServerManager;
import de.tud.jenkins.jobs.xml.JenkinsJobXml;

/**
 * @author svenseemann
 */
public class JenkinsResource {

    private Group group;
    private String workspacePath;
    private final String buildJobName;
    private final String analyzeJobName;
    private JenkinsJobXml buildJob;
    private JenkinsJobXml analyzeJob;

    public JenkinsResource(Group group, String workspacePath) {
        this.group = group;
        this.workspacePath = workspacePath;
        this.buildJobName = "Build_" + this.group.getName();
        this.analyzeJobName = "Analyze_" + this.group.getName();
    }

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
