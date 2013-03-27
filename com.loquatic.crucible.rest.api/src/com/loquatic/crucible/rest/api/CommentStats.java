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
 * <p>Java class for commentStats complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="commentStats">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="published" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="drafts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="defects" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="unread" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="leaveUnread" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="read" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commentStats", propOrder = {
    "user",
    "published",
    "drafts",
    "defects",
    "unread",
    "leaveUnread",
    "read"
})
public class CommentStats {

    protected String user;
    protected int published;
    protected int drafts;
    protected int defects;
    protected int unread;
    protected int leaveUnread;
    protected int read;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Gets the value of the published property.
     * 
     */
    public int getPublished() {
        return published;
    }

    /**
     * Sets the value of the published property.
     * 
     */
    public void setPublished(int value) {
        this.published = value;
    }

    /**
     * Gets the value of the drafts property.
     * 
     */
    public int getDrafts() {
        return drafts;
    }

    /**
     * Sets the value of the drafts property.
     * 
     */
    public void setDrafts(int value) {
        this.drafts = value;
    }

    /**
     * Gets the value of the defects property.
     * 
     */
    public int getDefects() {
        return defects;
    }

    /**
     * Sets the value of the defects property.
     * 
     */
    public void setDefects(int value) {
        this.defects = value;
    }

    /**
     * Gets the value of the unread property.
     * 
     */
    public int getUnread() {
        return unread;
    }

    /**
     * Sets the value of the unread property.
     * 
     */
    public void setUnread(int value) {
        this.unread = value;
    }

    /**
     * Gets the value of the leaveUnread property.
     * 
     */
    public int getLeaveUnread() {
        return leaveUnread;
    }

    /**
     * Sets the value of the leaveUnread property.
     * 
     */
    public void setLeaveUnread(int value) {
        this.leaveUnread = value;
    }

    /**
     * Gets the value of the read property.
     * 
     */
    public int getRead() {
        return read;
    }

    /**
     * Sets the value of the read property.
     * 
     */
    public void setRead(int value) {
        this.read = value;
    }

}
