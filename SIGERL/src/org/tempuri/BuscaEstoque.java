
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BuscaEstoque element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="BuscaEstoque">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="Filial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "filial"
})
@XmlRootElement(name = "BuscaEstoque")
public class BuscaEstoque {

    @XmlElement(name = "Filial", namespace = "http://tempuri.org/")
    protected String filial;

    /**
     * Gets the value of the filial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilial() {
        return filial;
    }

    /**
     * Sets the value of the filial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilial(String value) {
        this.filial = value;
    }

}
