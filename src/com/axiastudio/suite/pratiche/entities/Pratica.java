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
package com.axiastudio.suite.pratiche.entities;

import com.axiastudio.pypapi.Register;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.pratiche.PraticaListener;
import com.axiastudio.suite.pratiche.PraticaUtil;
import com.axiastudio.suite.protocollo.entities.Fascicolo;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.richieste.entities.RichiestaPratica;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@EntityListeners({PraticaListener.class, TimeStampedListener.class})
@Table(schema="PRATICHE")
@SequenceGenerator(name="genpratica", sequenceName="pratiche.pratica_id_seq", initialValue=1, allocationSize=1)
public class Pratica implements Serializable, ITimeStamped {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genpratica")
    private Long id;
    @Column(name="idpratica", unique=true)
    private String idpratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceinterno;
    @Column(name="codiceaggiuntivo")
    private String codiceaggiuntivo;
    @Column(name="datapratica")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datapratica;
    @Column(name="anno")
    private Integer anno;
    @Column(name="descrizione")
    private String descrizione="";
    @Column(name="note")
    private String note="";
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<PraticaProtocollo> praticaProtocolloCollection;
    @JoinColumn(name = "attribuzione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio attribuzione;
    @JoinColumn(name = "gestione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio gestione;
    @JoinColumn(name = "ubicazione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ubicazione;
    @Column(name="dettaglioubicazione")
    private String dettaglioubicazione;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private TipoPratica tipo;
    @Column(name="riservata")
    private Boolean riservata=false;
    @Column(name="archiviata")
    private Boolean archiviata=false;
    @JoinColumn(name = "fascicolo", referencedColumnName = "id")
    @ManyToOne
    private Fascicolo fascicolo;
    @OneToMany(mappedBy = "praticadominante", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<DipendenzaPratica> dipendenzaPraticaCollection;
    @Column(name="annoinventario")
    private Integer annoinventario;
    @Column(name="numeroinventario")
    private String numeroinventario;
    @Column(name="datachiusura")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datachiusura;
    @JoinColumn(name = "fase", referencedColumnName = "id")
    @ManyToOne
    private Fase fase;
    @Column(name="datatermineistruttoria")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datatermineistruttoria;
    @Column(name="datascadenza")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datascadenza;
    @Column(name="codificaanomala")
    private Boolean codificaanomala=false;
    @Column(name="multiufficio")
    private Boolean multiufficio=false;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    @OrderColumn(name="progressivo")
    private List<FasePratica> fasePraticaCollection;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<Visto> vistoCollection;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UtentePratica> utentePraticaCollection;
    @OneToMany(mappedBy = "pratica", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<RichiestaPratica> richiestaPraticaCollection;


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

    /* transient */
    @Transient
    private Ufficio tGestione;

    public Ufficio gettGestione() {
        return tGestione;
    }

    @PostLoad
    private void saveTransients() {
        tGestione = gestione;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Date getDatapratica() {
        return datapratica;
    }

    public void setDatapratica(Date datapratica) {
        this.datapratica = datapratica;
    }

    public String getIdpratica() {
        return idpratica;
    }

    public void setIdpratica(String idpratica) {
        this.idpratica = idpratica;
    }

    public String getCodiceinterno() {
        return codiceinterno;
    }

    public void setCodiceinterno(String codiceInterno) {
        this.codiceinterno = codiceInterno;
    }

    public String getCodiceaggiuntivo() {
        return codiceaggiuntivo;
    }

    public void setCodiceaggiuntivo(String codiceaggiuntivo) {
        this.codiceaggiuntivo = codiceaggiuntivo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String oggetto) {
        this.descrizione = oggetto;
    }
    
    public String getDescrizioner() {
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        if( this.getRiservata() != null && this.getRiservata() && !PraticaUtil.utenteInGestorePratica(this, autenticato) &&
                !autenticato.getSupervisorepraticheriservate() ){
            return "RISERVATA";
        }
        return this.getDescrizione();
    }

    public void setDescrizioner(String descrizione) {
        /* nulla da fare */
    }
    
    public Collection<PraticaProtocollo> getPraticaProtocolloCollection() {
        return praticaProtocolloCollection;
    }

    public void setPraticaProtocolloCollection(Collection<PraticaProtocollo> praticaProtocolloCollection) {
        this.praticaProtocolloCollection = praticaProtocolloCollection;
    }

    public Ufficio getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Ufficio attribuzione) {
        this.attribuzione = attribuzione;
    }

    public Ufficio getGestione() {
        return gestione;
    }

    public void setGestione(Ufficio gestione) {
        this.gestione = gestione;
    }

    public Ufficio getUbicazione() {
        return ubicazione;
    }

    public void setUbicazione(Ufficio ubicazione) {
        this.ubicazione = ubicazione;
    }

    public String getDettaglioubicazione() {
        return dettaglioubicazione;
    }

    public void setDettaglioubicazione(String dettaglioubicazione) {
        this.dettaglioubicazione = dettaglioubicazione;
    }

    public TipoPratica getTipo() {
        return tipo;
    }

    public void setTipo(TipoPratica tipo) {
        this.tipo = tipo;
    }

    public Boolean getRiservata() {
        return riservata;
    }

    public void setRiservata(Boolean riservata) {
        this.riservata = riservata;
    }

    public Boolean getArchiviata() {
//        return archiviata;
        return this.getDatachiusura()!=null;
    }

    public void setArchiviata(Boolean archiviata) {
        this.archiviata = archiviata;
    }

    public Fascicolo getFascicolo() {
        return fascicolo;
    }

    public void setFascicolo(Fascicolo fascicolo) {
        this.fascicolo = fascicolo;
    }

    public Collection<DipendenzaPratica> getDipendenzaPraticaCollection() {
        return dipendenzaPraticaCollection;
    }

    public void setDipendenzaPraticaCollection(Collection<DipendenzaPratica> dipendenzaPraticaCollection) {
        this.dipendenzaPraticaCollection = dipendenzaPraticaCollection;
    }

    public Integer getAnnoinventario() {
        return annoinventario;
    }

    public void setAnnoinventario(Integer annoinventario) {
        this.annoinventario = annoinventario;
    }

    public String getNumeroinventario() {
        return numeroinventario;
    }

    public void setNumeroinventario(String numeroinventario) {
        this.numeroinventario = numeroinventario;
    }

    public Date getDatachiusura() {
        return datachiusura;
    }

    public void setDatachiusura(Date datachiusura) {
        this.datachiusura = datachiusura;
    }

    public Date getDatatermineistruttoria() {
        return datatermineistruttoria;
    }

    public void setDatatermineistruttoria(Date datatermineistruttoria) {
        this.datatermineistruttoria = datatermineistruttoria;
    }

    public Date getDatascadenza() {
        return datascadenza;
    }

    public void setDatascadenza(Date datascadenza) {
        this.datascadenza = datascadenza;
    }

    public Fase getFase() {
        return fase;
    }

    public void setFase(Fase fase) {
        this.fase = fase;
    }

    public Boolean getCodificaanomala() {
        return codificaanomala;
    }

    public void setCodificaanomala(Boolean codificaanomala) {
        this.codificaanomala = codificaanomala;
    }

    public Boolean getMultiufficio() {
        return multiufficio;
    }

    public void setMultiufficio(Boolean multiufficio) {
        this.multiufficio = multiufficio;
    }

    public List<FasePratica> getFasePraticaCollection() {
        return fasePraticaCollection;
    }

    public void setFasePraticaCollection(List<FasePratica> fasePraticaCollection) {
        this.fasePraticaCollection = fasePraticaCollection;
    }

    public Collection<Visto> getVistoCollection() {
        return vistoCollection;
    }

    public void setVistoCollection(Collection<Visto> vistoCollection) {
        this.vistoCollection = vistoCollection;
    }

    public Collection<RichiestaPratica> getRichiestaPraticaCollection() {
        return richiestaPraticaCollection;
    }

    public void setRichiestaPraticaCollection(Collection<RichiestaPratica> richiestaPraticaCollection) {
        this.richiestaPraticaCollection = richiestaPraticaCollection;
    }

    /*
                 * timestamped
                 */
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

    public Collection<UtentePratica> getUtentePraticaCollection() {
        return utentePraticaCollection;
    }

    public void setUtentePraticaCollection(Collection<UtentePratica> utentePraticaCollection) {
        this.utentePraticaCollection = utentePraticaCollection;
    }

    public String getIstruttorePratica() {
        if ( this.getUtentePraticaCollection()!=null && this.getUtentePraticaCollection().size()>0 ) {
            for ( UtentePratica utente: this.getUtentePraticaCollection()) {
                if ( utente.getIstruttore() && utente.getAl() == null && utente.getUtente() != null ) {
                    return utente.getUtente().getNome();
                }
            }
        }
        return "";
    }

    public void setIstruttorePratica(String istruttorePratica) {
    }

    public String getResponsabilePratica() {
        if ( this.getUtentePraticaCollection()!=null && this.getUtentePraticaCollection().size()>0 ) {
            for ( UtentePratica utente: this.getUtentePraticaCollection()) {
                if ( utente.getResponsabile() && utente.getAl() == null && utente.getUtente() != null ) {
                    return utente.getUtente().getNome();
                }
            }
        }
        return "";
    }

    public void setResponsabilePratica(String istruttorePratica) {
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
        if (!(object instanceof Pratica)) {
            return false;
        }
        Pratica other = (Pratica) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /*
     * Le pratiche riservate non riportano l'oggetto
     */
    @Override
    public String toString() {
        return this.getCodiceinterno() + " - " + this.getDescrizioner();
    }
    
}
