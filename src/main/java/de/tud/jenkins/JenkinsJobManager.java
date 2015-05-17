package de.tud.jenkins;


import de.tud.jenkins.jobs.JenkinsAnalyzeJob;
import de.tud.jenkins.jobs.JenkinsBuildJob;
import de.tud.jenkins.jobs.xml.JenkinsJobXml;

/**
 * @author svenseemann
 */
public class JenkinsJobManager {

    private JobPropertiesManager jpm;

    private static JenkinsJobManager instance;

    public static JenkinsJobManager instanceOf() {
        if (instance == null) {
            instance = new JenkinsJobManager();
        }

        return instance;
    }

    private JenkinsJobManager() {
        this.jpm = JobPropertiesManager.instanceOf();
    }

    public JenkinsJobXml createBuildJob(
            String displayName,
            String description,
            String projectUrl,
            String credentialsId,
            String scmUrl,
            String childProjectName,
            String workspacePath) {

        return this.createBuildJob(
                displayName,
                description,
                projectUrl,
                this.jpm.getScmClass(true),
                this.jpm.getScmPlugin(),
                scmUrl,
                credentialsId,
                this.jpm.getPluginGitHub(),
                this.jpm.getMavenTarget(),
                childProjectName,
                workspacePath
        );
    }


    public JenkinsJobXml createAnalyzeJob(
            String displayName,
            String description,
            String customWorkspace,
            String upstreamProject) {
        return this.createAnalyzeJob(
                displayName,
                description,
                "", // actions not supported yet
                this.jpm.getKeppDependencies(),
                this.jpm.getCanRoam(),
                this.jpm.getDisabled(),
                this.jpm.getBlockBuildWhenDownstreamBuilding(),
                this.jpm.getBlockBuildWhenUpstreamBuilding(),
                this.jpm.getConcurrentBuilds(),
                "", // buildWrappers not supported yet
                this.jpm.getScmClass(false),
                this.jpm.getScmPlugin(),
                this.jpm.getJdkName(),
                this.jpm.getMavenName(),
                customWorkspace,
                upstreamProject
        );
    }

    private JenkinsJobXml createBuildJob(
            String displayName,
            String description,
            String projectUrl,
            String scmClass,
            String scmPlugin,
            String scmUrl,
            String credentialsId,
            String gitHubPlugin,
            String buildTarget,
            String childProjectName,
            String workspacePath
    ) {
        JenkinsBuildJob job = new JenkinsBuildJob();

        job.addDisplayName(displayName);
        job.addDescription(description);
        job.addActions(""); //FIXME: unsupported in current version
        job.addKeepDependencies("false"); //FIXME: unsupported in current version
        job.addCanRoam("true"); //FIXME: unsupported in current version
        job.addDisable("false"); //FIXME: unsupported in current version
        job.addBlockBuildDownstream("false"); //FIXME: unsupported in current version
        job.addBlockBuildUpstream("false"); //FIXME: unsupported in current version
        job.addConcurrentBuild("false"); //FIXME: unsupported in current version
        job.addBuildWrapper(""); //FIXME: unsupported in current version
        job.addGitHubProperty(gitHubPlugin, projectUrl);
        job.addGitHubTrigger(gitHubPlugin);
        job.addScmGit(scmClass, scmPlugin, credentialsId, scmUrl);
        job.addMavenBuilder(buildTarget);
        job.addBuildTrigger(childProjectName);
        job.addCustomWorkspace(workspacePath);

        return job.generateJob();
    }

    private JenkinsJobXml createAnalyzeJob(
            String displayName,
            String description,
            String actions,
            String keepDependencies,
            String canRoam,
            String disable,
            String blockBuildWhenDownstream,
            String blockBuildWhenUpstream,
            String concurrentBuild,
            String buildWrapper,
            String scmClass,
            String scmPlugin,
            String jdk,
            String maven,
            String customWorkspace,
            String upstreamProject) {
        JenkinsAnalyzeJob job = new JenkinsAnalyzeJob();

        job.addDisplayName(displayName);
        job.addDescription(description);
        job.addActions(actions);
        job.addKeepDependencies(keepDependencies);
        job.addCanRoam(canRoam);
        job.addDisable(disable);
        job.addBlockBuildDownstream(blockBuildWhenDownstream);
        job.addBlockBuildUpstream(blockBuildWhenUpstream);
        job.addConcurrentBuild(concurrentBuild);
        job.addBuildWrapper(buildWrapper);
        job.addScmNone(scmClass, scmPlugin);
        job.addSonar(jdk, maven);
        job.addCustomWorkspace(customWorkspace);
        job.addReverseBuildTrigger(upstreamProject);

        return job.generateJob();
    }
}
