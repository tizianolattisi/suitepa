/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.protocollo.entities;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.protocollo.ProfiloUtenteProtocollo;
import com.axiastudio.suite.protocollo.ProtocolloListener;
import com.axiastudio.suite.pubblicazioni.entities.Pubblicazione;
import com.axiastudio.suite.richieste.entities.RichiestaProtocollo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@EntityListeners({ProtocolloListener.class, TimeStampedListener.class})
@Table(schema="PROTOCOLLO")
@SequenceGenerator(name="genprotocollo", sequenceName="protocollo.protocollo_id_seq", initialValue=1, allocationSize=1)
public class Protocollo implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genprotocollo")
    private Long id;
    @Column(name="iddocumento", length=12, unique=true)
    private String iddocumento;
    @Column(name="dataprotocollo", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataprotocollo;
    @Column(name="anno")
    private Integer anno;
    @Column(name="oggetto", length=1024)
    private String oggetto="";
    @Column(name="note", length=1024)
    private String note="";
    @Enumerated(EnumType.STRING)
    private TipoProtocollo tipo=TipoProtocollo.ENTRATA;
    @JoinColumn(name = "sportello", referencedColumnName = "id")
    @ManyToOne
    private Ufficio sportello;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SoggettoProtocollo> soggettoProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SoggettoRiservatoProtocollo> soggettoRiservatoProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioProtocollo> ufficioProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Attribuzione> attribuzioneCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloCollection;
    @OneToMany(mappedBy = "precedente", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RiferimentoProtocollo> riferimentoProtocolloSuccessivoCollection;
    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<AnnullamentoProtocollo> annullamentoProtocolloCollection;
    @Column(name="annullato")
    private Boolean annullato=false;
    @Column(name="annullamentorichiesto")
    private Boolean annullamentorichiesto=false;
    @Column(name="richiederisposta")
    private Boolean richiederisposta=false;
    @Column(name="spedito")
    private Boolean spedito=false;
    @Column(name="dataspedizione")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataspedizione;
    @Column(name="esecutorespedizione", length=40)
    private String esecutorespedizione="";
    @Column(name="riservato")
    private Boolean riservato=false;
    @Column(name="corrispostoostornato")
    private Boolean corrispostoostornato=false;
    @JoinColumn(name = "tiporiferimentomittente", referencedColumnName = "descrizione")
    @ManyToOne
    private TipoRiferimentoMittente tiporiferimentomittente;
    @Column(name="riferimentomittente")
    private String riferimentomittente="";
    @Column(name="datariferimentomittente")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datariferimentomittente;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;
    
    @Column(name="convalidaattribuzioni")
    private Boolean convalidaattribuzioni=false;
    @Column(name="dataconvalidaattribuzioni")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconvalidaattribuzioni;
    @Column(name="esecutoreconvalidaattribuzioni", length=40)
    private String esecutoreconvalidaattribuzioni="";

    @Column(name="convalidaprotocollo")
    private Boolean convalidaprotocollo=false;
    @Column(name="dataconvalidaprotocollo")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconvalidaprotocollo;
    @Column(name="esecutoreconvalidaprotocollo", length=40)
    private String esecutoreconvalidaprotocollo="";
    @Column(name="numeroconvalidaprotocollo", length=10)
    private String numeroconvalidaprotocollo="";
    
    @Column(name="consolidadocumenti")
    private Boolean consolidadocumenti=false;
    @Column(name="dataconsolidadocumenti")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataconsolidadocumenti;
    @Column(name="esecutoreconsolidadocumenti", length=40)
    private String esecutoreconsolidadocumenti="";

    @Column(name="controlloreposta", length=40)
    private String controlloreposta="";

    @Column(name="numeroatto")
    private Integer numeroatto;
    @Column(name="dataatto")
    @Temporal(TemporalType.DATE)
    private Date dataatto;

    @OneToOne(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private PecProtocollo pecProtocollo;

    @OneToOne(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Pubblicazione pubblicazione;

    @OneToMany(mappedBy = "protocollo", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RichiestaProtocollo> richiestaProtocolloCollection;

    /* timestamped */
    @Column(name="rec_creato", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordcreato;
    @Column(name="rec_creato_da")
    private String recordcreatoda;
    @Column(name="rec_modificato")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date recordmodificato;
    @Column(name="rec_modificato_da")
    private String recordmodificatoda;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIddocumento() {
        return iddocumento;
    }

    public void setIddocumento(String iddocumento) {
        this.iddocumento = iddocumento;
    }

    public Date getDataprotocollo() {
        return dataprotocollo;
    }

    public void setDataprotocollo(Date dataprotocollo) {
        this.dataprotocollo = dataprotocollo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getOggettop() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(this, autenticato);
        if( !profilo.inSportelloOAttribuzione() && !autenticato.getSupervisoreprotocollo() && !autenticato.getRicercatoreprotocollo()){
            return "RISERVATO";
        }
        return this.getOggetto();
    }

    public void setOggettop(String oggetto) {
        /* nulla da fare */
    }

    public String getNotep() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        ProfiloUtenteProtocollo profilo = new ProfiloUtenteProtocollo(this, autenticato);
        if( !profilo.inSportelloOAttribuzione() ){
            return "RISERVATO";
        }
        return this.getNote();
    }

    public void setNotep(String note) {
        /* nulla da fare */
    }
    
    public TipoProtocollo getTipo() {
        return tipo;
    }

    public void setTipo(TipoProtocollo tipo) {
        this.tipo = tipo;
    }

    public Ufficio getSportello() {
        return sportello;
    }

    public void setSportello(Ufficio sportello) {
        this.sportello = sportello;
    }

    public Collection<Attribuzione> getAttribuzioneCollection() {
        return attribuzioneCollection;
    }

    public void setAttribuzioneCollection(Collection<Attribuzione> attribuzioneprotocolloCollection) {
        this.attribuzioneCollection = attribuzioneprotocolloCollection;
    }

    public Boolean getAnnullamentorichiesto() {
        return annullamentorichiesto;
    }

    public void setAnnullamentorichiesto(Boolean annullamentorichiesto) {
        this.annullamentorichiesto = annullamentorichiesto;
    }

    public Boolean getAnnullato() {
        return annullato;
    }

    public void setAnnullato(Boolean annullato) {
        this.annullato = annullato;
    }

    public Boolean getCorrispostoostornato() {
        return corrispostoostornato;
    }

    public void setCorrispostoostornato(Boolean corrispostoostornato) {
        this.corrispostoostornato = corrispostoostornato;
    }

    public Boolean getRichiederisposta() {
        return richiederisposta;
    }

    public void setRichiederisposta(Boolean richiederisposta) {
        this.richiederisposta = richiederisposta;
    }

    public Boolean getRiservato() {
        return riservato;
    }

    public void setRiservato(Boolean riservato) {
        this.riservato = riservato;
    }

    public Boolean getSpedito() {
        return spedito;
    }

    public void setSpedito(Boolean spedito) {
        this.spedito = spedito;
    }

    public TipoRiferimentoMittente getTiporiferimentomittente() {
        return tiporiferimentomittente;
    }

    public void setTiporiferimentomittente(TipoRiferimentoMittente tiporiferimentomittente) {
        this.tiporiferimentomittente = tiporiferimentomittente;
    }

    public Collection<SoggettoProtocollo> getSoggettoProtocolloCollection() {
        return soggettoProtocolloCollection;
    }

    public void setSoggettoProtocolloCollection(Collection<SoggettoProtocollo> soggettoProtocolloCollection) {
        this.soggettoProtocolloCollection = soggettoProtocolloCollection;
    }

    public Collection<SoggettoRiservatoProtocollo> getSoggettoRiservatoProtocolloCollection() {
        return soggettoRiservatoProtocolloCollection;
    }

    public void setSoggettoRiservatoProtocolloCollection(Collection<SoggettoRiservatoProtocollo> soggettoRiservatoProtocolloCollection) {
        this.soggettoRiservatoProtocolloCollection = soggettoRiservatoProtocolloCollection;
    }

    public Collection<UfficioProtocollo> getUfficioProtocolloCollection() {
        return ufficioProtocolloCollection;
    }

    public void setUfficioProtocolloCollection(Collection<UfficioProtocollo> ufficioProtocolloCollection) {
        this.ufficioProtocolloCollection = ufficioProtocolloCollection;
    }

    public Collection<PraticaProtocollo> getPraticaProtocolloCollection() {
        return praticaProtocolloCollection;
    }

    public void setPraticaProtocolloCollection(Collection<PraticaProtocollo> praticaProtocolloCollection) {
        this.praticaProtocolloCollection = praticaProtocolloCollection;
    }

    public Collection<RiferimentoProtocollo> getRiferimentoProtocolloCollection() {
        return riferimentoProtocolloCollection;
    }

    public void setRiferimentoProtocolloCollection(Collection<RiferimentoProtocollo> riferimentoProtocolloCollection) {
        this.riferimentoProtocolloCollection = riferimentoProtocolloCollection;
    }

    public Collection<RiferimentoProtocollo> getRiferimentoProtocolloSuccessivoCollection() {
        return riferimentoProtocolloSuccessivoCollection;
    }

    public void setRiferimentoProtocolloSuccessivoCollection(Collection<RiferimentoProtocollo> riferimentoProtocolloSuccessivoCollection) {
        this.riferimentoProtocolloSuccessivoCollection = riferimentoProtocolloSuccessivoCollection;
    }

    public Collection<AnnullamentoProtocollo> getAnnullamentoProtocolloCollection() {
        return annullamentoProtocolloCollection;
    }

    public void setAnnullamentoProtocolloCollection(Collection<AnnullamentoProtocollo> annullamentoProtocolloCollection) {
        this.annullamentoProtocolloCollection = annullamentoProtocolloCollection;
    }

    public Date getDatariferimentomittente() {
        return datariferimentomittente;
    }

    public void setDatariferimentomittente(Date datariferimentomittente) {
        this.datariferimentomittente = datariferimentomittente;
    }

    public String getRiferimentomittente() {
        return riferimentomittente;
    }

    public void setRiferimentomittente(String riferimentomittente) {
        this.riferimentomittente = riferimentomittente;
    }

    public Boolean getConvalidaattribuzioni() {
        return convalidaattribuzioni;
    }

    public void setConvalidaattribuzioni(Boolean convalidaattribuzioni) {
        this.convalidaattribuzioni = convalidaattribuzioni;
    }

    public Boolean getConvalidaprotocollo() {
        return convalidaprotocollo;
    }

    public void setConvalidaprotocollo(Boolean convalidaprotocollo) {
        this.convalidaprotocollo = convalidaprotocollo;
    }

    public Date getDataconvalidaprotocollo() {
        return dataconvalidaprotocollo;
    }

    public void setDataconvalidaprotocollo(Date dataconvalida) {
        this.dataconvalidaprotocollo = dataconvalida;
    }

    public Boolean getConsolidadocumenti() {
        return consolidadocumenti;
    }

    public void setConsolidadocumenti(Boolean consolidadocumenti) {
        this.consolidadocumenti = consolidadocumenti;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }
    
    public Date getDataspedizione() {
        return dataspedizione;
    }

    public void setDataspedizione(Date dataspedizione) {
        this.dataspedizione = dataspedizione;
    }

    public String getEsecutorespedizione() {
        return esecutorespedizione;
    }

    public void setEsecutorespedizione(String esecutorespedizione) {
        this.esecutorespedizione = esecutorespedizione;
    }

    public Date getDataconvalidaattribuzioni() {
        return dataconvalidaattribuzioni;
    }

    public void setDataconvalidaattribuzioni(Date dataconvalidaattribuzioni) {
        this.dataconvalidaattribuzioni = dataconvalidaattribuzioni;
    }

    public String getEsecutoreconvalidaattribuzioni() {
        return esecutoreconvalidaattribuzioni;
    }

    public void setEsecutoreconvalidaattribuzioni(String esecutoreconvalidaattribuzioni) {
        this.esecutoreconvalidaattribuzioni = esecutoreconvalidaattribuzioni;
    }

    public String getEsecutoreconvalidaprotocollo() {
        return esecutoreconvalidaprotocollo;
    }

    public void setEsecutoreconvalidaprotocollo(String esecutoreconvalidaprotocollo) {
        this.esecutoreconvalidaprotocollo = esecutoreconvalidaprotocollo;
    }

    public Date getDataconsolidadocumenti() {
        return dataconsolidadocumenti;
    }

    public void setDataconsolidadocumenti(Date dataconsolidadocumenti) {
        this.dataconsolidadocumenti = dataconsolidadocumenti;
    }

    public String getEsecutoreconsolidadocumenti() {
        return esecutoreconsolidadocumenti;
    }

    public void setEsecutoreconsolidadocumenti(String esecutoreconsolidadocumenti) {
        this.esecutoreconsolidadocumenti = esecutoreconsolidadocumenti;
    }

    public String getNumeroconvalidaprotocollo() {
        return numeroconvalidaprotocollo;
    }

    public void setNumeroconvalidaprotocollo(String numeroconvalidaprotocollo) {
        this.numeroconvalidaprotocollo = numeroconvalidaprotocollo;
    }

    public String getControlloreposta() {
        return controlloreposta;
    }

    public void setControlloreposta(String controlloreposta) {
        this.controlloreposta = controlloreposta;
    }

    public Integer getNumeroatto() {
        return numeroatto;
    }

    public void setNumeroatto(Integer numeroatto) {
        this.numeroatto = numeroatto;
    }

    public Date getDataatto() {
        return dataatto;
    }

    public void setDataatto(Date dataatto) {
        this.dataatto = dataatto;
    }

    public PecProtocollo getPecProtocollo() {
        return pecProtocollo;
    }

    public void setPecProtocollo(PecProtocollo pecProtocollo) {
        this.pecProtocollo = pecProtocollo;
    }

    public String getPecBody() {
        if ( this.getPecProtocollo()!=null) {
            return this.getPecProtocollo().getBody();
        } else {
            return "";
        }
    }

    public void setPecBody(String pecBody) {
        if( getPecProtocollo()==null ){
            PecProtocollo pecProtocollo = new PecProtocollo();
            pecProtocollo.setProtocollo(this);
            setPecProtocollo(pecProtocollo);
        }
        getPecProtocollo().setBody(pecBody);
    }

    public String getSegnatura() {
        if ( this.getPecProtocollo()!=null) {
            return this.getPecProtocollo().getSegnatura();
        } else {
            return "";
        }
    }

    public void setSegnatura(String segnatura) {}

    public String getStatopec() {
        if ( this.getPecProtocollo()!=null && this.getPecProtocollo().getStato()!=null) {
            return this.getPecProtocollo().getStato().toString();
        } else {
            return "";
        }
    }

    public void setStatopec(String statoPec) {}

    public Pubblicazione getPubblicazione() {
        return pubblicazione;
    }

    public void setPubblicazione(Pubblicazione pubblicazione) {
        this.pubblicazione = pubblicazione;
    }

    public Collection<RichiestaProtocollo> getRichiestaProtocolloCollection() {
        return richiestaProtocolloCollection;
    }

    public void setRichiestaProtocolloCollection(Collection<RichiestaProtocollo> richiestaProtocolloCollection) {
        this.richiestaProtocolloCollection = richiestaProtocolloCollection;
    }

    @Override
    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    @Override
    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }
    
    @Override
    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

   @Override
   public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
    }
  

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Protocollo)) {
            return false;
        }
        Protocollo other = (Protocollo) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.iddocumento + "-" + this.tipo.toString().substring(0, 1) + " (" + SuiteUtil.DATETIME_FORMAT.format(this.dataprotocollo) + ") " + this.getOggettop();
    }

}
