//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.15 at 10:57:54 PM MDT 
//


package com.loquatic.crucible.rest.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for generalCommentData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="generalCommentData">
 *   &lt;complexContent>
 *     &lt;extension base="{}commentDataImpl">
 *       &lt;sequence>
 *         &lt;element name="permId" type="{}permId" minOccurs="0"/>
 *         &lt;element name="permaId" type="{}permId" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generalCommentData", propOrder = {
    "permId",
    "permaId"
})
public class GeneralCommentData
    extends CommentDataImpl
{

    protected PermId permId;
    protected PermId permaId;

    /**
     * Gets the value of the permId property.
     * 
     * @return
     *     possible object is
     *     {@link PermId }
     *     
     */
    public PermId getPermId() {
        return permId;
    }

    /**
     * Sets the value of the permId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PermId }
     *     
     */
    public void setPermId(PermId value) {
        this.permId = value;
    }

    /**
     * Gets the value of the permaId property.
     * 
     * @return
     *     possible object is
     *     {@link PermId }
     *     
     */
    public PermId getPermaId() {
        return permaId;
    }

    /**
     * Sets the value of the permaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PermId }
     *     
     */
    public void setPermaId(PermId value) {
        this.permaId = value;
    }

}