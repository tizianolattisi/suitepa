
package com.axiastudio.suite.interoperabilita.entities.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessaggioRicevuto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessaggioRicevuto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Identificatore"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PrimaRegistrazione" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}DescrizioneMessaggio"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessaggioRicevuto", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "identificatore",
    "primaRegistrazione",
    "descrizioneMessaggio"
})
public class MessaggioRicevuto {

    @XmlElement(name = "Identificatore", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Identificatore identificatore;
    @XmlElement(name = "PrimaRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "DescrizioneMessaggio", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected DescrizioneMessaggio descrizioneMessaggio;

    /**
     * Gets the value of the identificatore property.
     * 
     * @return
     *     possible object is
     *     {@link Identificatore }
     *     
     */
    public Identificatore getIdentificatore() {
        return identificatore;
    }

    /**
     * Sets the value of the identificatore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identificatore }
     *     
     */
    public void setIdentificatore(Identificatore value) {
        this.identificatore = value;
    }

    /**
     * Gets the value of the primaRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public PrimaRegistrazione getPrimaRegistrazione() {
        return primaRegistrazione;
    }

    /**
     * Sets the value of the primaRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaRegistrazione }
     *     
     */
    public void setPrimaRegistrazione(PrimaRegistrazione value) {
        this.primaRegistrazione = value;
    }

    /**
     * Gets the value of the descrizioneMessaggio property.
     * 
     * @return
     *     possible object is
     *     {@link DescrizioneMessaggio }
     *     
     */
    public DescrizioneMessaggio getDescrizioneMessaggio() {
        return descrizioneMessaggio;
    }

    /**
     * Sets the value of the descrizioneMessaggio property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescrizioneMessaggio }
     *     
     */
    public void setDescrizioneMessaggio(DescrizioneMessaggio value) {
        this.descrizioneMessaggio = value;
    }

}
