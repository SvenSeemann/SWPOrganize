package de.tud.jenkins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author svenseemann
 */
public class JobPropertiesManager {

    private static final String JOB_PROPERTIES =
            "/jenkinsJobConfig/jobConfig.properties";

    private final Properties properties;

    private static JobPropertiesManager instance;

    public static JobPropertiesManager instanceOf() {
        if (instance == null) {
            instance = new JobPropertiesManager();
        }
        return instance;
    }

    private JobPropertiesManager() {
        this.properties = this.readProperties();
    }

    private Properties readProperties() {
        Properties prop = new Properties();
        InputStream input;

        try {
            input = getClass().getResourceAsStream(JobPropertiesManager.JOB_PROPERTIES);

            prop.load(input);
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(JobPropertiesManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        return prop;
    }

    /**
     * Returns the value of the scm.class.* property,
     * which can be used in config files for jenkins jobs.
     * Depending on usingGit it uses property scm.class.git or scm.class.none.
     * Other scm classes are not supported.
     *
     * @param usingGit - set true when using git as scm
     * @return value of the scm.class.* property
     */
    public String getScmClass(Boolean usingGit) {
        if (usingGit) {
            return this.properties.getProperty("scm.class.git");
        } else {
            return this.properties.getProperty("scm.class.none");
        }
    }

    /**
     * Returns the plugin value, which can be used to configure scm
     * usage in jenkins jobs.
     * Currently only git is supported as plugin.
     *
     * @return git plugin string value
     */
    public String getScmPlugin() {
        return this.properties.getProperty("scm.plugin.git");
    }

    /**
     * Returns the plugin value, which can be used to
     * configure GitHub in jenkins jobs.
     *
     * @return GitHub plugin string value
     */
    public String getPluginGitHub() {
        return this.properties.getProperty("github.plugin");
    }

    public String getJdkName() {
        return this.properties.getProperty("builders.jdk.name");
    }

    /**
     * Returns the name of the maven installation, which is (ddefault) configured
     * in jenkins.
     *
     * @return
     */
    public String getMavenName() {
        return this.properties.getProperty("builders.mvn.name");
    }

    /**
     * Currently only 'install' is supported.
     *
     * @return
     */
    public String getMavenTarget() {
        return this.properties.getProperty("builders.mvn.target");
    }

    public String getCredentialsId() {
        return this.properties.getProperty("scm.credentials");
    }

    public String getCanRoam() {
        return this.properties.getProperty("canRoam");
    }

    public String getConcurrentBuilds() {
        return this.properties.getProperty("concurrentBuild");
    }

    public String getDisabled() {
        return this.properties.getProperty("disabled");
    }

    public String getBlockBuildWhenDownstreamBuilding() {
        return this.properties.getProperty("blockBuildWhenDownstreamBuilding");
    }

    public String getBlockBuildWhenUpstreamBuilding() {
        return this.properties.getProperty("blockBuildWhenUpstreamBuilding");
    }

    public String getKeppDependencies() {
        return this.properties.getProperty("keepDependencies");
    }
}









