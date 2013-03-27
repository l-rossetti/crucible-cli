//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.15 at 10:57:54 PM MDT 
//


package com.loquatic.crucible.rest.api;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fileType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fileType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="File"/>
 *     &lt;enumeration value="Directory"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fileType")
@XmlEnum
public enum FileType {

    @XmlEnumValue("File")
    FILE("File"),
    @XmlEnumValue("Directory")
    DIRECTORY("Directory"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    FileType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FileType fromValue(String v) {
        for (FileType c: FileType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
