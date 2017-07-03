package edu.utah.nanofab.coralapiserver.core;

/**
 *
 * @author ryant
 */
public class RunDataEntry {
    public String name = "";
    public String description = "";
    public String xmlDefinition = "";
    public String version = "";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getXmlDefinition() {
        return xmlDefinition;
    }

    public void setXmlDefinition(String xmlDefinition) {
        this.xmlDefinition = xmlDefinition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
