
package com.axiastudio.suite.interoperabilita.entities.dtd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "denominazione",
    "identificativo",
    "unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax"
})
@XmlRootElement(name = "UnitaOrganizzativa")
public class UnitaOrganizzativa {

    @XmlAttribute(name = "tipo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tipo;
    @XmlElement(name = "Denominazione", required = true)
    protected Denominazione denominazione;
    @XmlElement(name = "Identificativo")
    protected String identificativo;
    @XmlElements({
        @XmlElement(name = "UnitaOrganizzativa", required = true, type = UnitaOrganizzativa.class),
        @XmlElement(name = "Ruolo", required = true, type = Ruolo.class),
        @XmlElement(name = "Persona", required = true, type = Persona.class),
        @XmlElement(name = "IndirizzoPostale", required = true, type = IndirizzoPostale.class),
        @XmlElement(name = "IndirizzoTelematico", required = true, type = IndirizzoTelematico.class),
        @XmlElement(name = "Telefono", required = true, type = Telefono.class),
        @XmlElement(name = "Fax", required = true, type = Fax.class)
    })
    protected List<Object> unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax;

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        if (tipo == null) {
            return "permanente";
        } else {
            return tipo;
        }
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the denominazione property.
     * 
     * @return
     *     possible object is
     *     {@link Denominazione }
     *     
     */
    public Denominazione getDenominazione() {
        return denominazione;
    }

    /**
     * Sets the value of the denominazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link Denominazione }
     *     
     */
    public void setDenominazione(Denominazione value) {
        this.denominazione = value;
    }

    /**
     * Gets the value of the identificativo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificativo() {
        return identificativo;
    }

    /**
     * Sets the value of the identificativo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificativo(String value) {
        this.identificativo = value;
    }

    /**
     * Gets the value of the unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UnitaOrganizzativa }
     * {@link Ruolo }
     * {@link Persona }
     * {@link IndirizzoPostale }
     * {@link IndirizzoTelematico }
     * {@link Telefono }
     * {@link Fax }
     * 
     * 
     */
    public List<Object> getUnitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax() {
        if (unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax == null) {
            unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax = new ArrayList<Object>();
        }
        return this.unitaOrganizzativaOrRuoloOrPersonaOrIndirizzoPostaleOrIndirizzoTelematicoOrTelefonoOrFax;
    }

}
