
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identificatore",
    "primaRegistrazione",
    "oraRegistrazione",
    "origine",
    "destinazione",
    "perConoscenza",
    "risposta",
    "riservato",
    "interventoOperatore",
    "riferimentoDocumentiCartacei",
    "riferimentiTelematici",
    "oggetto",
    "classifica",
    "note"
})
@XmlRootElement(name = "Intestazione")
public class Intestazione {

    @XmlElement(name = "Identificatore", required = true)
    protected Identificatore identificatore;
    @XmlElement(name = "PrimaRegistrazione")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "OraRegistrazione")
    protected OraRegistrazione oraRegistrazione;
    @XmlElement(name = "Origine", required = true)
    protected Origine origine;
    @XmlElement(name = "Destinazione", required = true)
    protected List<Destinazione> destinazione;
    @XmlElement(name = "PerConoscenza")
    protected List<PerConoscenza> perConoscenza;
    @XmlElement(name = "Risposta")
    protected Risposta risposta;
    @XmlElement(name = "Riservato")
    protected String riservato;
    @XmlElement(name = "InterventoOperatore")
    protected String interventoOperatore;
    @XmlElement(name = "RiferimentoDocumentiCartacei")
    protected RiferimentoDocumentiCartacei riferimentoDocumentiCartacei;
    @XmlElement(name = "RiferimentiTelematici")
    protected RiferimentiTelematici riferimentiTelematici;
    @XmlElement(name = "Oggetto", required = true)
    protected String oggetto;
    @XmlElement(name = "Classifica")
    protected List<Classifica> classifica;
    @XmlElement(name = "Note")
    protected String note;

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
     * Gets the value of the oraRegistrazione property.
     * 
     * @return
     *     possible object is
     *     {@link OraRegistrazione }
     *     
     */
    public OraRegistrazione getOraRegistrazione() {
        return oraRegistrazione;
    }

    /**
     * Sets the value of the oraRegistrazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link OraRegistrazione }
     *     
     */
    public void setOraRegistrazione(OraRegistrazione value) {
        this.oraRegistrazione = value;
    }

    /**
     * Gets the value of the origine property.
     * 
     * @return
     *     possible object is
     *     {@link Origine }
     *     
     */
    public Origine getOrigine() {
        return origine;
    }

    /**
     * Sets the value of the origine property.
     * 
     * @param value
     *     allowed object is
     *     {@link Origine }
     *     
     */
    public void setOrigine(Origine value) {
        this.origine = value;
    }

    /**
     * Gets the value of the destinazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the destinazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDestinazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Destinazione }
     * 
     * 
     */
    public List<Destinazione> getDestinazione() {
        if (destinazione == null) {
            destinazione = new ArrayList<Destinazione>();
        }
        return this.destinazione;
    }

    /**
     * Gets the value of the perConoscenza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the perConoscenza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerConoscenza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerConoscenza }
     * 
     * 
     */
    public List<PerConoscenza> getPerConoscenza() {
        if (perConoscenza == null) {
            perConoscenza = new ArrayList<PerConoscenza>();
        }
        return this.perConoscenza;
    }

    /**
     * Gets the value of the risposta property.
     * 
     * @return
     *     possible object is
     *     {@link Risposta }
     *     
     */
    public Risposta getRisposta() {
        return risposta;
    }

    /**
     * Sets the value of the risposta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Risposta }
     *     
     */
    public void setRisposta(Risposta value) {
        this.risposta = value;
    }

    /**
     * Gets the value of the riservato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiservato() {
        return riservato;
    }

    /**
     * Sets the value of the riservato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiservato(String value) {
        this.riservato = value;
    }

    /**
     * Gets the value of the interventoOperatore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterventoOperatore() {
        return interventoOperatore;
    }

    /**
     * Sets the value of the interventoOperatore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterventoOperatore(String value) {
        this.interventoOperatore = value;
    }

    /**
     * Gets the value of the riferimentoDocumentiCartacei property.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentoDocumentiCartacei }
     *     
     */
    public RiferimentoDocumentiCartacei getRiferimentoDocumentiCartacei() {
        return riferimentoDocumentiCartacei;
    }

    /**
     * Sets the value of the riferimentoDocumentiCartacei property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentoDocumentiCartacei }
     *     
     */
    public void setRiferimentoDocumentiCartacei(RiferimentoDocumentiCartacei value) {
        this.riferimentoDocumentiCartacei = value;
    }

    /**
     * Gets the value of the riferimentiTelematici property.
     * 
     * @return
     *     possible object is
     *     {@link RiferimentiTelematici }
     *     
     */
    public RiferimentiTelematici getRiferimentiTelematici() {
        return riferimentiTelematici;
    }

    /**
     * Sets the value of the riferimentiTelematici property.
     * 
     * @param value
     *     allowed object is
     *     {@link RiferimentiTelematici }
     *     
     */
    public void setRiferimentiTelematici(RiferimentiTelematici value) {
        this.riferimentiTelematici = value;
    }

    /**
     * Gets the value of the oggetto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOggetto() {
        return oggetto;
    }

    /**
     * Sets the value of the oggetto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOggetto(String value) {
        this.oggetto = value;
    }

    /**
     * Gets the value of the classifica property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classifica property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassifica().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classifica }
     * 
     * 
     */
    public List<Classifica> getClassifica() {
        if (classifica == null) {
            classifica = new ArrayList<Classifica>();
        }
        return this.classifica;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

}
