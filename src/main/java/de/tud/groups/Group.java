package de.tud.groups;

import org.kohsuke.github.GHRepository;

/**
 * @author svenseemann
 */
public class Group {

    private final String name;
    private final Integer number;
    private JenkinsResource jenkinsResource;
    private GHRepository groupRepository;

    public Group(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

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
