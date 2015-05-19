package de.tud.groups;

import org.kohsuke.github.GHRepository;

/**
 * Simple POJO, which olds relevant information to each SWP-Group.
 *
 * @author svenseemann
 */
public class Group {

    /**
     * GroupName
     */
    private final String name;

    /**
     * GroupNumber
     */
    private final Integer number;

    /**
     * Reference to the JenkinsResource,
     * holding the configuration for this group.
     */
    private JenkinsResource jenkinsResource;

    /**
     * Reference to the GitHub-Repository
     * for this Group
     */
    private GHRepository groupRepository;

    /**
     * Group-Constructor.
     * Only sets name and number. GHRepository and JenkinsResource are
     * set after the creation of the group-object.
     *
     * @param name
     *              The Group-Name
     * @param number
     *              The Group-Number
     */
    public Group(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    //*****************************************************************************************************************/
    // GETTER AND SETTER

    public GHRepository getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(GHRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setJenkinsResource(JenkinsResource jenkinsResource) {
        this.jenkinsResource = jenkinsResource;
    }

    public JenkinsResource getJenkinsResource() {
        return jenkinsResource;
    }
}
