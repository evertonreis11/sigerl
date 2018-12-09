
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IsAtivoResponse element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="IsAtivoResponse">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="IsAtivoResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "isAtivoResult"
})
@XmlRootElement(name = "IsAtivoResponse")
public class IsAtivoResponse {

    @XmlElement(name = "IsAtivoResult", namespace = "http://tempuri.org/")
    protected boolean isAtivoResult;

    /**
     * Gets the value of the isAtivoResult property.
     * 
     */
    public boolean isIsAtivoResult() {
        return isAtivoResult;
    }

    /**
     * Sets the value of the isAtivoResult property.
     * 
     */
    public void setIsAtivoResult(boolean value) {
        this.isAtivoResult = value;
    }

}
