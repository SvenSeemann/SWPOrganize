package de.tud.jenkins.jobs;


import de.tud.jenkins.jobs.xml.*;
import de.tud.jenkins.jobs.xml.Builder.BuilderElement;
import de.tud.jenkins.jobs.xml.JobInformationElement.DisplayName;
import de.tud.jenkins.jobs.xml.Publisher.BuildTrigger;
import org.xmlbeam.XBProjector;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author svenseemann
 */
public abstract class JenkinsJob {

    private final XBProjector projector;
    private JenkinsJobXml jenkinsJobXml;

    public Scm scm;
    public CustomWorkspace customWorkspace;
    public List<JobInformationElement> jobInformations;
    public List<BuilderElement> builders;
    public List<Trigger.TriggerElement> triggers;
    public List<Property.PropertyElement> properties;
    public List<Publisher.PublisherElement> publishers;

    public JenkinsJob() {
        this.jobInformations = new LinkedList<>();
        this.builders = new LinkedList<>();
        this.triggers = new LinkedList<>();
        this.properties = new LinkedList<>();
        this.publishers = new LinkedList<>();

        projector = new XBProjector(XBProjector.Flags.TO_STRING_RENDERS_XML);
        try {
            jenkinsJobXml = projector.io().fromURLAnnotation(JenkinsJobXml.class);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public XBProjector getProjector() {
        return projector;
    }

    public JenkinsJobXml getJenkinsJobXml() {
        return jenkinsJobXml;
    }

    public JenkinsJobXml generateJob() {
        List<JenkinsJobXml.Element> elementsForJob = new LinkedList<>();

        elementsForJob.addAll(this.jobInformations);
        elementsForJob.add(this.scm);
        elementsForJob.add(this.customWorkspace);
        elementsForJob.add(this.generateBuilder(this.builders));
        elementsForJob.add(this.generateProperty(this.properties));
        elementsForJob.add(this.generatePublisher(this.publishers));
        elementsForJob.add(this.generateTrigger(this.triggers));

        return this.jenkinsJobXml.setElements(elementsForJob);
    }

    /**
     * Genrates the XML Part for given builders within <builders></builders>.
     *
     * @param builderElements - list of trigger elements to put in trigger tag
     * @return - the complete builders tag
     */
    public Builder generateBuilder(List<BuilderElement> builderElements) {
        Builder builder = null;

        try {
            builder = this.projector.io().fromURLAnnotation(Builder.class);
            builder.setBuilders(builderElements);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }

        return builder;
    }

    /**
     * Genrates the XML Part for given publishers within <properties></properties>.
     *
     * @param propertyElements - list of trigger elements to put in trigger tag
     * @return - the complete properties tag
     */
    private Property generateProperty(List<Property.PropertyElement> propertyElements) {
        Property property = null;

        try {
            property = this.projector.io().fromURLAnnotation(Property.class);
            property.setProperties(propertyElements);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }

        return property;
    }

    /**
     * Genrates the XML Part for given publishers within <publishers></publishers>.
     *
     * @param publisherElements - list of trigger elements to put in trigger tag
     * @return - the complete publisher tag
     */
    private Publisher generatePublisher(List<Publisher.PublisherElement> publisherElements) {
        Publisher publisher = null;

        try {
            publisher = this.projector.io().fromURLAnnotation(Publisher.class);
            publisher.setPublisherElements(publisherElements);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }

        return publisher;
    }

    /**
     * Genrates the XML Part for given triggers within <trigger></triggers>.
     *
     * @param triggerElements - list of trigger elements to put in trigger tag
     * @return - the complete trigger tag
     */
    private Trigger generateTrigger(List<Trigger.TriggerElement> triggerElements) {
        Trigger trigger = null;

        try {
            trigger = this.projector.io().fromURLAnnotation(Trigger.class);
            trigger.setTriggerElements(triggerElements);

        } catch (IOException ex) {
            Logger.getLogger(JenkinsBuildJob.class.getName()).log(Level.SEVERE, null, ex);
        }

        return trigger;
    }

    public void addCustomWorkspace(String pathToWorkspace) {
        CustomWorkspace workspacePath = null;

        try {
            workspacePath = this
                    .projector
                    .io()
                    .fromURLAnnotation(CustomWorkspace.class);
            workspacePath.setCustomWorkspace(pathToWorkspace);

            this.customWorkspace = workspacePath;
        } catch (IOException e) {
            Logger.getLogger(CustomWorkspace.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Generates the XML part that defines (on success only by now) following jobs
     * to start after finishing.
     *
     * @param childProjectName - the name of the job to trigger after success
     */
    public void addBuildTrigger(String childProjectName) {
        BuildTrigger buildTrigger;

        try {
            buildTrigger = this.projector.io().fromURLAnnotation(BuildTrigger.class);
            buildTrigger.setChildProject(childProjectName);

            this.publishers.add(buildTrigger);
        } catch (IOException ex) {
            Logger
                    .getLogger(JenkinsBuildJob.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    public void addDisplayName(String name) {
        DisplayName nameXml;

        try {
            nameXml = this.projector.io().fromURLAnnotation(DisplayName.class);
            nameXml.setDisplayName(name);

            this.jobInformations.add(nameXml);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addDescription(String description) {
        JobInformationElement.Description descXml;

        try {
            descXml = this.projector.io().fromURLAnnotation(JobInformationElement.Description.class);
            descXml.setDescription(description);

            this.jobInformations.add(descXml);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addActions(String actions) {
        JobInformationElement.Action actionXml;

        try {
            actionXml = this.projector.io().fromURLAnnotation(JobInformationElement.Action.class);
            actionXml.setAction(actions);

            this.jobInformations.add(actionXml);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addKeepDependencies(String value) {
        JobInformationElement.KeepDependencies keepDependencies;

        try {
            keepDependencies = this.projector.io().fromURLAnnotation(JobInformationElement.KeepDependencies.class);
            keepDependencies.setKeepDependencies(value);

            this.jobInformations.add(keepDependencies);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addCanRoam(String value) {
        JobInformationElement.CanRoam canRoam;

        try {
            canRoam = this.projector.io().fromURLAnnotation(JobInformationElement.CanRoam.class);
            canRoam.setCanRoam(value);

            this.jobInformations.add(canRoam);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addDisable(String value) {
        JobInformationElement.Disable disable;

        try {
            disable = this.projector.io().fromURLAnnotation(JobInformationElement.Disable.class);
            disable.setDisabled(value);

            this.jobInformations.add(disable);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBlockBuildDownstream(String value) {
        JobInformationElement.BlockBuildDownstreamBuilding bbdb;

        try {
            bbdb = this.projector.io().fromURLAnnotation(JobInformationElement.BlockBuildDownstreamBuilding.class);
            bbdb.setBlockDownstream(value);

            this.jobInformations.add(bbdb);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBlockBuildUpstream(String value) {
        JobInformationElement.BlockBuildUpstreamBuilding bbub;

        try {
            bbub = this.projector.io().fromURLAnnotation(JobInformationElement.BlockBuildUpstreamBuilding.class);
            bbub.setBlockUpstream(value);

            this.jobInformations.add(bbub);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addConcurrentBuild(String value) {
        JobInformationElement.ConcurrentBuild concurrentBuild;

        try {
            concurrentBuild = this.projector.io().fromURLAnnotation(JobInformationElement.ConcurrentBuild.class);
            concurrentBuild.setConcurrentBuild(value);

            this.jobInformations.add(concurrentBuild);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addBuildWrapper(String buildWrapper) {
        JobInformationElement.BuildWrapper bw;

        try {
            bw = this.projector.io().fromURLAnnotation(JobInformationElement.BuildWrapper.class);
            bw.setBuildWrapper(buildWrapper);

            this.jobInformations.add(bw);
        } catch (IOException ex) {
            Logger.getLogger(JenkinsJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
