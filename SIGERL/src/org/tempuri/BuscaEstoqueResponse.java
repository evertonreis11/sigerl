
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BuscaEstoqueResponse element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="BuscaEstoqueResponse">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="BuscaEstoqueResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "buscaEstoqueResult"
})
@XmlRootElement(name = "BuscaEstoqueResponse")
public class BuscaEstoqueResponse {

    @XmlElement(name = "BuscaEstoqueResult", namespace = "http://tempuri.org/")
    protected ConsultaEstoque buscaEstoqueResult;

    /**
     * Gets the value of the buscaEstoqueResult property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaEstoque }
     *     
     */
    public ConsultaEstoque getBuscaEstoqueResult() {
        return buscaEstoqueResult;
    }

    /**
     * Sets the value of the buscaEstoqueResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaEstoque }
     *     
     */
    public void setBuscaEstoqueResult(ConsultaEstoque value) {
        this.buscaEstoqueResult = value;
    }

}
