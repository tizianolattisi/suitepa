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
package com.axiastudio.suite.deliberedetermine.entities;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.DeterminaListener;
import com.axiastudio.suite.finanziaria.entities.Progetto;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.generale.ITimeStamped;
import com.axiastudio.suite.generale.TimeStampedListener;
import com.axiastudio.suite.generale.entities.Costante;
import com.axiastudio.suite.pratiche.IAtto;
import com.axiastudio.suite.pratiche.IDettaglio;
import com.axiastudio.suite.pratiche.entities.DipendenzaPratica;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.pratiche.entities.Visto;
import com.axiastudio.suite.protocollo.IProtocollabile;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.UfficioProtocollo;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@EntityListeners({DeterminaListener.class, TimeStampedListener.class})
@Table(schema="deliberedetermine")
@SequenceGenerator(name="gendetermina", sequenceName="deliberedetermine.determina_id_seq", initialValue=1, allocationSize=1)
@NamedQueries({
        @NamedQuery(name="inAttesaDiVistoDiBilancio",
                query = "SELECT d FROM Determina d JOIN d.pratica p JOIN p.fasePraticaCollection fp " +
                        "WHERE fp.attiva = true AND (fp.fase.id=:idfase)"),
        @NamedQuery(name="inAttesaDiVistoResp",
                query = "SELECT d FROM Determina d JOIN d.pratica p JOIN p.fasePraticaCollection fp " +
                        "JOIN d.servizioDeterminaCollection sd JOIN Delega dg " +
                        "WHERE dg.servizio=sd.servizio AND " +
                            "fp.cariche LIKE '%RESPONSABILE_DI_SERVIZIO%' AND fp.attiva=true AND " +
                            "sd.principale=true AND " +
                            "dg.carica.codiceCarica='RESPONSABILE_DI_SERVIZIO' AND coalesce(dg.fine, current_timestamp) >= current_timestamp AND " +
                            "dg.delegato=false AND dg.utente.id=:autenticato " +
                        "ORDER BY d.codiceinterno"),
        @NamedQuery(name="inAttesaDiVistoRespDelegato",
                query = "SELECT d FROM Determina d JOIN d.pratica p JOIN p.fasePraticaCollection fp " +
                        "JOIN d.servizioDeterminaCollection sd JOIN Delega dg " +
                        "WHERE dg.servizio=sd.servizio AND " +
                        "fp.cariche LIKE '%RESPONSABILE_DI_SERVIZIO%' AND fp.attiva=true AND " +
                        "sd.principale=true AND " +
                        "dg.carica.codiceCarica='RESPONSABILE_DI_SERVIZIO' AND coalesce(dg.fine, current_timestamp) >= current_timestamp AND " +
                        "dg.delegato=true AND dg.utente.id=:autenticato " +
                        "ORDER BY d.codiceinterno")
})

public class Determina extends Observable implements Serializable, ITimeStamped, IDettaglio, IProtocollabile, IAtto {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gendetermina")
    private Long id;
    @JoinColumn(name = "idpratica", referencedColumnName = "idpratica")
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.REFRESH})
    private Pratica pratica;
    @Column(name="codiceinterno", unique=true)
    private String codiceinterno;
    @Column(name="oggetto", length=2048)
    private String oggetto;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<ServizioDetermina> servizioDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<MovimentoDetermina> movimentoDeterminaCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<SpesaImpegnoEsistente> spesaImpegnoEsistenteCollection;
    @OneToMany(mappedBy = "determina", orphanRemoval = true, cascade=CascadeType.ALL)
    private Collection<UfficioDetermina> ufficioDeterminaCollection;
    @Column(name="dispesa")
    private Boolean dispesa=Boolean.FALSE;
    @Column(name="spesaimpegnoesistente")
    private Boolean spesaimpegnoesistente =Boolean.FALSE;
    @Column(name="diaffidamento")
    private Boolean diaffidamento =Boolean.FALSE;
    @Column(name="dientrata")
    private Boolean dientrata =Boolean.FALSE;
    @Column(name="diregolarizzazione")
    private Boolean diregolarizzazione =Boolean.FALSE;
    @Column(name="diliquidazione")
    private Boolean diliquidazione =Boolean.FALSE;
    @Column(name="diincarico")
    private Boolean diincarico =Boolean.FALSE;
    @Column(name="referentepolitico")
    private String referentePolitico;
    @JoinColumn(name = "responsabile", referencedColumnName = "id")
    @ManyToOne
    private Utente responsabile;
    @Column(name="anno")
    private Integer anno;
    @Column(name="numero")
    private Integer numero;
    @Column(name="data")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;
    /* protocollo */
    @JoinColumn(name = "protocollo", referencedColumnName = "iddocumento")
    @OneToOne
    private Protocollo protocollo;
    @Enumerated(EnumType.STRING)
    private TipoPubblicazione pubblicabile=TipoPubblicazione.PUBBLICABILE;
    @Column(name="pluriennale")
    private Boolean pluriennale=Boolean.FALSE;
    @Column(name="finoadanno")
    private Integer finoAdAnno;
    @JoinColumn(name = "progetto", referencedColumnName = "id")
    @ManyToOne
    private Progetto progetto;
    @JoinColumn(name = "responsabileprocedimento", referencedColumnName = "id")
    @ManyToOne
    private Soggetto responsabileprocedimento;
    /* Incompatibilità */
    @Column(name="impedimentoresponsabile")
    private Boolean impedimentoresponsabile =Boolean.FALSE;
    @JoinColumn(name = "impedimentodelegato", referencedColumnName = "id")
    @ManyToOne
    private Utente impedimentodelegato;

    /* Spending review */
    @Column(name="benioservizi")
    private Boolean benioservizi =Boolean.FALSE;
    @Column(name="convenzioneattiva")
    private Boolean convenzioneattiva=Boolean.FALSE;
    @Column(name="noadesioneconvenzione")
    private Boolean noadesioneconvenzione=Boolean.FALSE;
    @Column(name="cpvpresente")
    private Boolean cpvpresente=Boolean.FALSE;
    @Column(name="cpvnonfruibile")
    private Boolean cpvnonfruibile=Boolean.FALSE;
    @Enumerated(EnumType.STRING)
    private Mercato mercato=Mercato.ME_PAT;
    @Enumerated(EnumType.STRING)
    private Convenzione convenzione=Convenzione.APAC;
    @Column(name="oggettoconvenzione")
    private String oggettoconvenzione;
    @Column(name="bando")
    private String bando;
    @Column(name="cpv")
    private String cpv;
    @Column(name="motivoanomalia")
    private String motivoanomalia;

    /* Gestione associata */
    @Enumerated(EnumType.STRING)
    private Firmatario firmatario=Firmatario.RESP_SERVIZIO_BILANCIO;


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

    @Override
    public Pratica getPratica() {
        return pratica;
    }

    @Override
    public void setPratica(Pratica pratica) {
        this.pratica = pratica;
    }

    @Override
    public String getIdpratica() {
        if( this.pratica != null ){
            return pratica.getIdpratica();
        }
        return null;
    }

    @Override
    public void setIdpratica(String idpratica) {
        // NOP
    }

    @Override
    public String getCodiceinterno() {
        return codiceinterno;
    }

    @Override
    public void setCodiceinterno(String codiceinterno) {
        this.codiceinterno = codiceinterno;
    }

    @Override
    public Servizio getServizio() {
        for( ServizioDetermina servizioDetermina: getServizioDeterminaCollection() ){
            if( servizioDetermina.getPrincipale() ){
                return servizioDetermina.getServizio();
            }
        }
        return null;
    }

    @Override
    public Ufficio getUfficio() {
        Servizio servizio = getServizio();
        if( servizio != null ){
            return servizio.getUfficio();
        }
        return null;
    }

    @Override
    public String getOggetto() {
        return oggetto;
    }

    @Override
    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
        this.setChanged();
    }

    public Date getDatapratica() {
        if( this.pratica != null ){
            return pratica.getDatapratica();
        }
        return null;
    }

    public void setDatapratica(Date datapratica) {
        // NOP
    }

    public Collection<ServizioDetermina> getServizioDeterminaCollection() {
        return servizioDeterminaCollection;
    }

    public void setServizioDeterminaCollection(Collection<ServizioDetermina> servizioDeterminaCollection) {
        this.servizioDeterminaCollection = servizioDeterminaCollection;
    }

    public Collection<MovimentoDetermina> getMovimentoDeterminaCollection() {
        return movimentoDeterminaCollection;
    }

    public void setMovimentoDeterminaCollection(Collection<MovimentoDetermina> movimentoDeterminaCollection) {
        this.movimentoDeterminaCollection = movimentoDeterminaCollection;
    }

    public Collection<SpesaImpegnoEsistente> getSpesaImpegnoEsistenteCollection() {
        return spesaImpegnoEsistenteCollection;
    }

    public void setSpesaImpegnoEsistenteCollection(Collection<SpesaImpegnoEsistente> spesaImpegnoEsistenteCollection) {
        this.spesaImpegnoEsistenteCollection = spesaImpegnoEsistenteCollection;
    }

    public Collection<UfficioDetermina> getUfficioDeterminaCollection() {
        return ufficioDeterminaCollection;
    }

    public void setUfficioDeterminaCollection(Collection<UfficioDetermina> ufficioDeterminaCollection) {
        this.ufficioDeterminaCollection = ufficioDeterminaCollection;
    }

    public Boolean getDispesa() {
        return dispesa;
    }

    public void setDispesa(Boolean dispesa) {
        this.dispesa = dispesa;
    }

    public Boolean getSpesaimpegnoesistente() {
        return spesaimpegnoesistente;
    }

    public void setSpesaimpegnoesistente(Boolean spesaprecedenteimpegno) {
        this.spesaimpegnoesistente = spesaprecedenteimpegno;
    }

    public Boolean getDiaffidamento() {
        return diaffidamento;
    }

    public void setDiaffidamento(Boolean diaffidamento) {
        this.diaffidamento = diaffidamento;
    }

    public Boolean getDientrata() {
        return dientrata;
    }

    public void setDientrata(Boolean diEntrata) {
        this.dientrata = diEntrata;
    }

    public Boolean getDiregolarizzazione() {
        return diregolarizzazione;
    }

    public void setDiregolarizzazione(Boolean diRegolarizzazione) {
        this.diregolarizzazione = diRegolarizzazione;
    }

    public Boolean getDiliquidazione() {
        return diliquidazione;
    }

    public void setDiliquidazione(Boolean diLiquidazione) {
        this.diliquidazione = diLiquidazione;
    }

    public Boolean getDiincarico() {
        return diincarico;
    }

    public void setDiincarico(Boolean diIncarico) {
        this.diincarico = diIncarico;
    }

    public String getReferentePolitico() {
        return referentePolitico;
    }

    public void setReferentePolitico(String referentePolitico) {
        this.referentePolitico = referentePolitico;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Protocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(Protocollo protocollo) {
        this.protocollo = protocollo;
    }

    public Utente getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Utente responsabile) {
        this.responsabile = responsabile;
    }

    public TipoPubblicazione getPubblicabile() {
        return pubblicabile;
    }

    public void setPubblicabile(TipoPubblicazione pubblicabile) {
        this.pubblicabile = pubblicabile;
    }

    public Boolean getPluriennale() {
        return pluriennale;
    }

    public void setPluriennale(Boolean pluriennale) {
        this.pluriennale = pluriennale;
    }

    public Integer getFinoAdAnno() {
        return finoAdAnno;
    }

    public void setFinoAdAnno(Integer finoAdAnno) {
        this.finoAdAnno = finoAdAnno;
    }

    public Progetto getProgetto() {
        return progetto;
    }

    public void setProgetto(Progetto progetto) {
        this.progetto = progetto;
    }

    public Soggetto getResponsabileprocedimento() {
        return responsabileprocedimento;
    }

    public void setResponsabileprocedimento(Soggetto responsabileprocedimento) {
        this.responsabileprocedimento = responsabileprocedimento;
    }

    public String getNumeroprotocollo() {
        if ( getProtocollo() != null) {
            return getProtocollo().getIddocumento();
        }
        return null;
    }

    public void setNumeroprotocollo(String numeroprotocollo) {
        // NOP
    }

    public String getServizioPrincipale() {
        if (getServizio() != null) {
            return getServizio().getDescrizione();
        }
        return null;
    }

    public void setServizioPrincipale(String servizioPrincipale) {
        // NOP
    }

    public String getPraticaprincipale() {
        if ( getPratica()!=null && getPratica().getDipendenzaPraticaCollection()!=null ) {
            for (DipendenzaPratica dp: getPratica().getDipendenzaPraticaCollection()) {
                if ( dp.getDipendenza()!=null && dp.getDipendenza().getId() == -1 && dp.getInvertita() ) {
                    return dp.getPraticadipendente().getCodiceinterno();
                }
            }
        }
        return null;
    }

    public void setPraticaprincipale(String praticaprincipale) {
        // NOP
    }

    public String getFirma() {
        String firma = "";

        Visto vistoResp = this.getVisto("FASE_VISTO_RESPONSABILE");
        if (vistoResp != null) {
            if ( firmatario == Firmatario.RESP_SERVIZIO_BILANCIO ) {
                firma = " RESPONSABILE DEL SERVIZIO\n";
                if (vistoResp.getUtente().equals(vistoResp.getResponsabile())) {
                    firma = "IL" + firma;
                } else {
                    if ("SEGRETARIO".equals(vistoResp.getCodiceCarica())){
                        firma = "PER IL" + firma;
                    } else if (this.getImpedimentoresponsabile()) {
                        firma = "PER IMPEDIMENTO DEL" + firma;
                    } else {
                        firma = "PER TEMPORANEA ASSENZA DEL" + firma;
                    }
                }

                if (this.getProtocollo() != null && this.getProtocollo().getUfficioProtocolloCollection() != null) {
                    UfficioProtocollo ufficio = this.getProtocollo().getUfficioProtocolloCollection().iterator().next();
                    firma += ufficio.getUfficio().getDenominazione() + "\n";
                } else {
                    String ufficio = "[ufficio]\n";
                    for( ServizioDetermina servizioDetermina: this.getServizioDeterminaCollection() ) {
                        if (servizioDetermina.getPrincipale()) {
                            ufficio = servizioDetermina.getServizio().getUfficio().getDenominazione() + "\n";
                            ufficio = ufficio.replace("RESPONSABILE ", "");
                            break;
                        }
                    }
                    firma += ufficio;
                }

                if (vistoResp.getCodiceCarica() != null) {
                    if ("RESPONSABILE_DI_SERVIZIO".equals(vistoResp.getCodiceCarica()) &&
                            !vistoResp.getUtente().equals(vistoResp.getResponsabile())) {
                        firma += "IL FUNZIONARIO INCARICATO\n";
                    } else if ("SEGRETARIO".equals(vistoResp.getCodiceCarica())) {
                        firma += "IL SEGRETARIO GENERALE\n";
                    } else if ("VICE_SEGRETARIO".equals(vistoResp.getCodiceCarica())) {
                        firma += "IL VICESEGRETARIO GENERALE\n";
                    }
                    firma += vistoResp.getUtente();
                }
            } else if ( firmatario == Firmatario.RESP_PROCEDIMENTO ) {
                firma = "IL RESPONSABILE DI PROCEDIMENTO\n";
                if (vistoResp.getCodiceCarica() != null) {
                    if ("SEGRETARIO".equals(vistoResp.getCodiceCarica())) {
                        firma += "IL SEGRETARIO GENERALE\n";
                    } else if ("VICE_SEGRETARIO".equals(vistoResp.getCodiceCarica())) {
                        firma += "IL VICESEGRETARIO GENERALE\n";
                    }
                    firma += vistoResp.getUtente();
                }
            }
        }
        return firma;
    }

    public void setFirma(String firma) {
        // NOP
    }

    private Visto getVisto(String tipoVisto) {
        if( this.getPratica() != null ){
            Database db = (Database) Register.queryUtility(IDatabase.class);
            Costante faseVisto = SuiteUtil.trovaCostante(tipoVisto);
            if ( faseVisto != null ) {
                Long idFaseVisto = Long.parseLong(faseVisto.getValore());
                Controller controller = db.createController(Fase.class);
                Fase fase = (Fase) controller.get(idFaseVisto);
                EntityManager em = db.getEntityManagerFactory().createEntityManager();
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Visto> cq = cb.createQuery(Visto.class);
                Root<Visto> root = cq.from(Visto.class);
                cq.select(root);
                cq.where(cb.and(cb.equal(root.get("pratica"), this.getPratica()),
                        cb.equal(root.get("fase"), fase),
                        cb.equal(root.get("negato"), false)));
                cq.orderBy(cb.desc(root.get("data")));
                TypedQuery<Visto> tq = em.createQuery(cq);
                List<Visto> resultList = tq.getResultList();
                if (resultList.size() > 0) {
                    return resultList.get(0);
                }
            }
        }
        return null;
    }

    public Visto getVistoResponsabile() {
        return getVisto("FASE_VISTO_RESPONSABILE");
    }
    public void setVistoResponsabile(Visto visto){  }

    public Visto getVistoBilancio() {
        return getVisto("FASE_VISTO_BILANCIO");
    }
    public void setVistoBilancio(Visto visto){  }

    public Visto getVistoBilancioNegato() {
        return getVisto("FASE_VISTO_BILANCIO_NEGATO");
    }
    public void setVistoBilancioNegato(Visto visto){  }

    public Visto getVistoLiquidazione() {
        return getVisto("FASE_LIQUIDAZIONE");
    }
    public void setVistoLiquidazione(Visto visto){  }

    public Boolean getBenioservizi() {
        return benioservizi;
    }

    public void setBenioservizi(Boolean nobenioservizi) {
        this.benioservizi = nobenioservizi;
    }

    public Boolean getConvenzioneattiva() {
        return convenzioneattiva;
    }

    public void setConvenzioneattiva(Boolean convenzioneattiva) {
        this.convenzioneattiva = convenzioneattiva;
    }

    public Boolean getNoadesioneconvenzione() {
        return noadesioneconvenzione;
    }

    public void setNoadesioneconvenzione(Boolean noadesioneconvenzione) {
        this.noadesioneconvenzione = noadesioneconvenzione;
    }

    public Boolean getNoconvenzioneattiva() {
        return !convenzioneattiva && !noadesioneconvenzione;
    }

    public void setNoconvenzioneattiva(Boolean noconvenzioneattiva) {

    }

    public Boolean getCpvpresente() {
        return cpvpresente;
    }

    public void setCpvpresente(Boolean cpvpresente) {
        this.cpvpresente = cpvpresente;
    }

    public Boolean getCpvnonfruibile() {
        return cpvnonfruibile;
    }

    public void setCpvnonfruibile(Boolean cpvnonfruibile) {
        this.cpvnonfruibile = cpvnonfruibile;
    }

    public Boolean getNocpvpresente() {
        return !cpvpresente && !cpvnonfruibile;
    }

    public void setNocpvpresente(Boolean cpvpresente) {

    }
    public Mercato getMercato() {
        return mercato;
    }

    public void setMercato(Mercato mercato) {
        this.mercato = mercato;
    }

    public Convenzione getConvenzione() {
        return convenzione;
    }

    public void setConvenzione(Convenzione convenzione) {
        this.convenzione = convenzione;
    }

    public String getOggettoconvenzione() {
        return oggettoconvenzione;
    }

    public void setOggettoconvenzione(String oggettoconvenzione) {
        this.oggettoconvenzione = oggettoconvenzione;
    }

    public String getBando() {
        return bando;
    }

    public void setBando(String bando) {
        this.bando = bando;
    }

    public String getCpv() {
        return cpv;
    }

    public void setCpv(String cpv) {
        this.cpv = cpv;
    }

    public String getMotivoanomalia() {
        return motivoanomalia;
    }

    public void setMotivoanomalia(String motivoanomalia) {
        this.motivoanomalia = motivoanomalia;
    }

    public Firmatario getFirmatario() {
        return firmatario;
    }

    public void setFirmatario(Firmatario firmatario) {
        this.firmatario = firmatario;
    }

    public Boolean getImpedimentoresponsabile() {
        return impedimentoresponsabile;
    }

    public void setImpedimentoresponsabile(Boolean impedimentoresponsabile) {
        this.impedimentoresponsabile = impedimentoresponsabile;
    }

    public Utente getImpedimentodelegato() {
        return impedimentodelegato;
    }

    public void setImpedimentodelegato(Utente impedimentodelegato) {
        this.impedimentodelegato = impedimentodelegato;
    }

    public Date getRecordcreato() {
        return recordcreato;
    }

    public void setRecordcreato(Date recordcreato) {
        this.recordcreato = recordcreato;
    }

    public String getRecordcreatoda() {
        return recordcreatoda;
    }

    public void setRecordcreatoda(String recordcreatoda) {
        this.recordcreatoda = recordcreatoda;
    }

    public Date getRecordmodificato() {
        return recordmodificato;
    }

    public void setRecordmodificato(Date recordmodificato) {
        this.recordmodificato = recordmodificato;
    }

    public String getRecordmodificatoda() {
        return recordmodificatoda;
    }

    public void setRecordmodificatoda(String recordmodificatoda) {
        this.recordmodificatoda = recordmodificatoda;
    }

    public void reset() {
        this.clearChanged();
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
        if (!(object instanceof Determina)) {
            return false;
        }
        Determina other = (Determina) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.deliberedetermine.entities.Determina[ id=" + id + " ]";
    }

}
