
package org.tempuri;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Produto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Produto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProdutoID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Estoque" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Produto", propOrder = {
    "produtoID",
    "estoque"
})
public class EstoqueProduto {

    @XmlElement(name = "ProdutoID", namespace = "http://tempuri.org/")
    protected String produtoID;
    @XmlElement(name = "Estoque", namespace = "http://tempuri.org/", required = true)
    protected BigDecimal estoque;

    /**
     * Gets the value of the produtoID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProdutoID() {
        return produtoID;
    }

    /**
     * Sets the value of the produtoID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProdutoID(String value) {
        this.produtoID = value;
    }

    /**
     * Gets the value of the estoque property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEstoque() {
        return estoque;
    }

    /**
     * Sets the value of the estoque property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEstoque(BigDecimal value) {
        this.estoque = value;
    }

}
