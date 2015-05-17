package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

/**
 *
 * @author svenseemann
 */
public interface JobInformationElement extends JenkinsJobXml.Element {

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_actionJobInformation.xml")
    public interface Action extends JobInformationElement {

        @XBWrite("/actions")
        Action setAction(String action);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_displayNameJobInformation.xml")
    public interface DisplayName extends JobInformationElement {

        @XBWrite("/displayName")
        DisplayName setDisplayName(String name);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_descriptionJobInformation.xml")
    public interface Description extends JobInformationElement {

        @XBWrite("/description")
        Description setDescription(String description);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_keepDependenciesJobInformation.xml")
    public interface KeepDependencies extends JobInformationElement {

        @XBWrite("/keepDependencies")
        KeepDependencies setKeepDependencies(String value);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_canRoamJobInformation.xml")
    public interface CanRoam extends JobInformationElement {

        @XBWrite("/canRoam")
        CanRoam setCanRoam(String canRoam);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_disableJobInformation.xml")
    public interface Disable extends JobInformationElement {

        @XBWrite("/disable")
        Disable setDisabled(String disabledValue);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_blockBuildDownstreamBuildingJobInformation.xml")
    public interface BlockBuildDownstreamBuilding extends JobInformationElement {

        @XBWrite("/blockBuildWhenDownstreamBuilding")
        BlockBuildDownstreamBuilding setBlockDownstream(String value);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_blockBuildUpstreamBuildingJobInformation.xml")
    public interface BlockBuildUpstreamBuilding extends JobInformationElement {

        @XBWrite("/blockBuildWhenUpstreamBuilding")
        BlockBuildUpstreamBuilding setBlockUpstream(String value);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_concurrentBuildJobInformation.xml")
    public interface ConcurrentBuild extends JobInformationElement {

        @XBWrite("/concurrentBuild")
        ConcurrentBuild setConcurrentBuild(String value);
    }

    @XBDocURL("resource://jenkinsJobConfig/jobInformation/_buildWrappersJobInformation.xml")
    public interface BuildWrapper extends JobInformationElement {

        @XBWrite("/buildWrappers")
        BuildWrapper setBuildWrapper(String buildWrapper);
    }

}
