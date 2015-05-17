package de.tud.jenkins.jobs.xml;

import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBWrite;

import java.util.List;

/**
 *
 * @author svenseemann
 */
@XBDocURL("resource://jenkinsJobConfig/jenkinsJobTemplate.xml")
public interface JenkinsJobXml {

    @XBWrite("/project/*")
    JenkinsJobXml setJobInformations(List<JobInformationElement> jobInfoElements);

    @XBWrite("/project/builders/*")
    JenkinsJobXml setBuilders(Builder builders);

    @XBWrite("/project/properties/*")
    JenkinsJobXml setProperties(Property properties);

    @XBWrite("/project/publishers/*")
    JenkinsJobXml setPublishers(Publisher publishers);

    @XBWrite("/project/triggers/*")
    JenkinsJobXml setTrigger(Trigger trigger);

    @XBWrite("/project/*")
    JenkinsJobXml setElements(List<Element> elements);

    public abstract interface Element {
    }
}
