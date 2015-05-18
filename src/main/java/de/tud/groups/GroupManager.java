package de.tud.groups;

import de.tud.github.GitHubManager;
import de.tud.jenkins.JenkinsJobManager;
import de.tud.jenkins.JenkinsServerManager;
import de.tud.jenkins.jobs.xml.JenkinsJobXml;
import org.kohsuke.github.GHRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

/**
 * @author svenseemann
 */
public class GroupManager extends Observable {

    /**
     * The namePrefix is used to generate names for groups and repositories.
     * The pattern for generated names is: namePrefix + groupNumber
     */
    private String namePrefix;

    /**
     * A default description for the repositories added to GitHub.
     */
    private final String repositoryDescription;

    public static final String WORKSPACE_PATH = "/workspace";

    /**
     * The relative path to the defined workspaces from ${JENKINS_HOME}.
     */
    public static final String JOB_PATH = "jobs/";

    /**
     * The absolute number of groups in the software project course.
     */
    private Integer groupCount;

    /**
     * Contains all current existing groups.
     */
    private List<Group> groups;

    private static GroupManager instance;

    public static GroupManager instanceOf() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }

    private GroupManager() {
        this.groupCount = 0;
        this.namePrefix = "swt15w";
        this.repositoryDescription =
                "A groups repository for the TU Dresden software project course";
        this.groups = new ArrayList<>();
    }

    /**
     * This method will generate the number of groups, specified in groupCount.
     *
     * @return true, if all groups are created successfully
     */
    public Boolean createGroups() {
        Boolean success = true;

        for (int i = 1; i <= this.groupCount; i++) {
            success = success && this.createGroup(i);
        }

        return success;
    }

    /**
     * Deletes all existing groups in the groups field.
     */
    public void deleteGroups() {
        this.groups.forEach(group -> this.deleteGroup(group));
        this.groups = new ArrayList<>();
    }

    /**
     * Creates a new group and the depending resources like the GitHub repository
     * and JenkinsResources.
     *
     * @param number -  the number of the group is used for creating
     *               the groups name and repository name
     * @return true, if the group and all depending resources successfully created
     * false, if an error occur (already created repositories and resources will be deleted)
     */
    private boolean createGroup(Integer number) {
        final String groupName = this.createGroupName(number);
        Group group = new Group(groupName, number);
        Optional<GHRepository> repo = this.createGroupRepository(group);
        if (repo.isPresent()) {
            group.setGroupRepository(repo.get());
            this.createGroupJenkinsResource(group);
            group.getJenkinsResource().pushJobs();
            this.groups.add(group);
            this.notifyObservers(false);
            return true;
        } else {
            this.notifyObservers(true);
            return false;
        }
    }

    /**
     * Creates the repository for the given group.
     *
     * @param group the group to create the repository for
     * @return an optional with either the GHRepository object or empty if an error occurred
     */
    private Optional<GHRepository> createGroupRepository(Group group) {
        return GitHubManager.instanceOf()
                .setupGroupRepository(
                        group.getName(),
                        this.repositoryDescription,
                        ""
                );
    }

    /**
     * Triggers creation of the three different job xml's.
     *
     * @param group the group to create the jenkins jobs for
     */
    private void createGroupJenkinsResource(Group group) {
        JenkinsResource groupsResource =
                new JenkinsResource(group, this.createGroupWorkspacePath(group));
        group.setJenkinsResource(groupsResource);
        groupsResource.setBuildJob(this.createGroupsBuildJob(group));
        groupsResource.setAnalyzeJob(this.createGroupsAnalyzeJob(group));
    }

    /**
     * Creates the jenkins jobs which is responsible for building the application and running tests.
     *
     * @param group the group to create the job for
     * @return the object with the wanted xml configurations
     */
    private JenkinsJobXml createGroupsBuildJob(Group group) {
        JenkinsJobXml buildJob = JenkinsJobManager
                .instanceOf()
                .createBuildJob(
                        "Build " + group.getName(),
                        "", //TODO: set default job description
                        group.getGroupRepository().getUrl().toString(),
                        JenkinsServerManager.instanceOf().getGitHubCredentials(
                                JenkinsServerManager.instanceOf().getJenkinsUsername(),
                                JenkinsServerManager.instanceOf().getJenkinsPassword()
                        ),
                        group.getGroupRepository().gitHttpTransportUrl(),
                        "", // BuildJob has no child project
                        group.getJenkinsResource().getWorkspacePath()
                );
        return buildJob;
    }

    /**
     * @param group
     * @return
     */
    private JenkinsJobXml createGroupsAnalyzeJob(Group group) {
        JenkinsJobXml analyzeJob = JenkinsJobManager
                .instanceOf()
                .createAnalyzeJob(
                        "Analyze " + group.getName(),
                        "", //TODO: set default job description
                        group.getJenkinsResource().getWorkspacePath(),
                        "Build_" + group.getName()
                );

        return analyzeJob;
    }

    /**
     * Deletes a group and all depending resources (GitHub Repository and JenkinsResources)
     * Does NOT delete the workspace and job directories on the Jenkins instance.
     *
     * @param toDelete - the group to delete
     */
    private void deleteGroup(Group toDelete) {
        GitHubManager.instanceOf()
                .deleteRepository(toDelete.getGroupRepository());
    }

    /**
     * Generates a group name based on the constant namePrefix.
     *
     * @param groupNumber
     * @return the generated name
     */
    private String createGroupName(Integer groupNumber) {
        return this.namePrefix + String.valueOf(groupNumber);
    }

    /**
     * Generates the (relative from ${JENKINS_HOME}) path to the groups workspace.
     * Schema: WORKSPACE_PATH + WORKSPACE_PREFIX + name of group.
     *
     * @param group the group to create workspace path for
     * @return the path to the workspace as string
     */
    private String createGroupWorkspacePath(Group group) {
        return GroupManager.JOB_PATH
                + "Build_"
                + group.getName()
                + GroupManager.WORKSPACE_PATH;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public List<Group> getGroups() {
        return groups;
    }
}
