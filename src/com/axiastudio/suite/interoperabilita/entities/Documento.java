
package com.axiastudio.suite.interoperabilita.entities;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Classe Java per Documento complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Documento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence minOccurs="0">
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}CollocazioneTelematica"/>
 *           &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Impronta" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}TitoloDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PrimaRegistrazione" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}TipoDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Oggetto" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Classifica" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}NumeroPagine" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}Note" minOccurs="0"/>
 *         &lt;element ref="{http://www.digitPa.gov.it/protocollo/}PiuInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="rife" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="nome" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="tipoMIME" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="tipoRiferimento" default="MIME">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="cartaceo"/>
 *             &lt;enumeration value="telematico"/>
 *             &lt;enumeration value="MIME"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Documento", namespace = "http://www.digitPa.gov.it/protocollo/", propOrder = {
    "collocazioneTelematica",
    "impronta",
    "titoloDocumento",
    "primaRegistrazione",
    "tipoDocumento",
    "oggetto",
    "classifica",
    "numeroPagine",
    "note",
    "piuInfo"
})
public class Documento {

    @XmlElement(name = "CollocazioneTelematica", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected CollocazioneTelematica collocazioneTelematica;
    @XmlElement(name = "Impronta", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Impronta impronta;
    @XmlElement(name = "TitoloDocumento", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected TitoloDocumento titoloDocumento;
    @XmlElement(name = "PrimaRegistrazione", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PrimaRegistrazione primaRegistrazione;
    @XmlElement(name = "TipoDocumento", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected TipoDocumento tipoDocumento;
    @XmlElement(name = "Oggetto", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Oggetto oggetto;
    @XmlElement(name = "Classifica", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected List<Classifica> classifica;
    @XmlElement(name = "NumeroPagine", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected NumeroPagine numeroPagine;
    @XmlElement(name = "Note", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected Note note;
    @XmlElement(name = "PiuInfo", namespace = "http://www.digitPa.gov.it/protocollo/")
    protected PiuInfo piuInfo;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "rife")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object rife;
    @XmlAttribute(name = "nome")
    @XmlSchemaType(name = "anySimpleType")
    protected String nome;
    @XmlAttribute(name = "tipoMIME")
    @XmlSchemaType(name = "anySimpleType")
    protected String tipoMIME;
    @XmlAttribute(name = "tipoRiferimento")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tipoRiferimento;

    /**
     * Recupera il valore della proprietà collocazioneTelematica.
     * 
     * @return
     *     possible object is
     *     {@link CollocazioneTelematica }
     *     
     */
    public CollocazioneTelematica getCollocazioneTelematica() {
        return collocazioneTelematica;
    }

    /**
     * Imposta il valore della proprietà collocazioneTelematica.
     * 
     * @param value
     *     allowed object is
     *     {@link CollocazioneTelematica }
     *     
     */
    public void setCollocazioneTelematica(CollocazioneTelematica value) {
        this.collocazioneTelematica = value;
    }

    /**
     * Recupera il valore della proprietà impronta.
     * 
     * @return
     *     possible object is
     *     {@link Impronta }
     *     
     */
    public Impronta getImpronta() {
        return impronta;
    }

    /**
     * Imposta il valore della proprietà impronta.
     * 
     * @param value
     *     allowed object is
     *     {@link Impronta }
     *     
     */
    public void setImpronta(Impronta value) {
        this.impronta = value;
    }

    /**
     * Recupera il valore della proprietà titoloDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TitoloDocumento }
     *     
     */
    public TitoloDocumento getTitoloDocumento() {
        return titoloDocumento;
    }

    /**
     * Imposta il valore della proprietà titoloDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TitoloDocumento }
     *     
     */
    public void setTitoloDocumento(TitoloDocumento value) {
        this.titoloDocumento = value;
    }

    /**
     * Recupera il valore della proprietà primaRegistrazione.
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
     * Imposta il valore della proprietà primaRegistrazione.
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
     * Recupera il valore della proprietà tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumento }
     *     
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Imposta il valore della proprietà tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumento }
     *     
     */
    public void setTipoDocumento(TipoDocumento value) {
        this.tipoDocumento = value;
    }

    /**
     * Recupera il valore della proprietà oggetto.
     * 
     * @return
     *     possible object is
     *     {@link Oggetto }
     *     
     */
    public Oggetto getOggetto() {
        return oggetto;
    }

    /**
     * Imposta il valore della proprietà oggetto.
     * 
     * @param value
     *     allowed object is
     *     {@link Oggetto }
     *     
     */
    public void setOggetto(Oggetto value) {
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
     * Recupera il valore della proprietà numeroPagine.
     * 
     * @return
     *     possible object is
     *     {@link NumeroPagine }
     *     
     */
    public NumeroPagine getNumeroPagine() {
        return numeroPagine;
    }

    /**
     * Imposta il valore della proprietà numeroPagine.
     * 
     * @param value
     *     allowed object is
     *     {@link NumeroPagine }
     *     
     */
    public void setNumeroPagine(NumeroPagine value) {
        this.numeroPagine = value;
    }

    /**
     * Recupera il valore della proprietà note.
     * 
     * @return
     *     possible object is
     *     {@link Note }
     *     
     */
    public Note getNote() {
        return note;
    }

    /**
     * Imposta il valore della proprietà note.
     * 
     * @param value
     *     allowed object is
     *     {@link Note }
     *     
     */
    public void setNote(Note value) {
        this.note = value;
    }

    /**
     * Recupera il valore della proprietà piuInfo.
     * 
     * @return
     *     possible object is
     *     {@link PiuInfo }
     *     
     */
    public PiuInfo getPiuInfo() {
        return piuInfo;
    }

    /**
     * Imposta il valore della proprietà piuInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link PiuInfo }
     *     
     */
    public void setPiuInfo(PiuInfo value) {
        this.piuInfo = value;
    }

    /**
     * Recupera il valore della proprietà id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta il valore della proprietà id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Recupera il valore della proprietà rife.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRife() {
        return rife;
    }

    /**
     * Imposta il valore della proprietà rife.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRife(Object value) {
        this.rife = value;
    }

    /**
     * Recupera il valore della proprietà nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietà nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietà tipoMIME.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIME() {
        return tipoMIME;
    }

    /**
     * Imposta il valore della proprietà tipoMIME.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIME(String value) {
        this.tipoMIME = value;
    }

    /**
     * Recupera il valore della proprietà tipoRiferimento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRiferimento() {
        if (tipoRiferimento == null) {
            return "MIME";
        } else {
            return tipoRiferimento;
        }
    }

    /**
     * Imposta il valore della proprietà tipoRiferimento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRiferimento(String value) {
        this.tipoRiferimento = value;
    }

}
