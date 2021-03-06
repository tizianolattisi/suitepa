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
package com.axiastudio.suite.deliberedetermine.forms;

import com.axiastudio.pypapi.db.*;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.suite.SuiteUiUtil;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.menjazo.AlfrescoHelper;
import com.axiastudio.pypapi.IStreamProvider;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.plugins.IPlugin;
import com.axiastudio.pypapi.ui.Delegate;
import com.axiastudio.pypapi.ui.ITableModel;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.SuiteUtil;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.deliberedetermine.entities.Determina;
import com.axiastudio.suite.deliberedetermine.entities.ServizioDetermina;
import com.axiastudio.suite.plugins.cmis.CmisPlugin;
import com.axiastudio.suite.plugins.ooops.IDocumentFolder;
import com.axiastudio.suite.plugins.ooops.OoopsPlugin;
import com.axiastudio.suite.plugins.ooops.Template;
import com.axiastudio.suite.pratiche.entities.Fase;
import com.axiastudio.suite.pratiche.entities.FasePratica;
import com.axiastudio.suite.pratiche.entities.Visto;
import com.axiastudio.suite.pratiche.forms.FormDettaglio;
import com.axiastudio.suite.procedimenti.SimpleWorkFlow;
import com.axiastudio.suite.procedimenti.SimpleWorkflowDialog;
import com.axiastudio.suite.procedimenti.entities.Delega;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class FormDetermina extends FormDettaglio implements IDocumentFolder {

    protected DeterminaToolbar determinaToolbar;
    private Determina determina;
    private Visto vistoLiquidazione;

    public FormDetermina(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);

        determinaToolbar = new DeterminaToolbar("Dettaglio", this);
        addToolBar(determinaToolbar);
        this.determinaToolbar.actionByName("vistoLiquidazione").setEnabled(Boolean.FALSE);

        QListWidget procedimento = (QListWidget) findChild(QListWidget.class, "procedimento");
        procedimento.itemDoubleClicked.connect(this, "completaFase(QListWidgetItem)");

        PyPaPiTableView tableViewServizi = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_servizi");
        tableViewServizi.entityInserted.connect(this, "servizioInserito(Object)");
        tableViewServizi.entityRemoved.connect(this, "servizioRimosso(Object)");
        tableViewServizi.entityUpdated.connect(this, "servizioModificato(Object)");

        PyPaPiTableView tableView_impegni = (PyPaPiTableView) findChild(PyPaPiTableView.class, "tableView_impegni");
        tableView_impegni.setItemDelegate(new Delegate(tableView_impegni));
        PyPaPiTableView tableView_spese = (PyPaPiTableView) findChild(PyPaPiTableView.class, "tableView_spese");
        tableView_spese.setItemDelegate(new Delegate(tableView_spese));

        ((QCheckBox) findChild(QCheckBox.class, "benioservizi")).clicked.connect(this, "beniOServizi(Boolean)");
        ((QCheckBox) findChild(QCheckBox.class, "checkBox_impedimento")).clicked.connect(this, "impedimentoResponsabile(Boolean)");
        ((QCheckBox) findChild(QCheckBox.class, "checkBox_spesa")).clicked.connect(this, "determinaSpesa(Boolean)");

        ((QRadioButton) findChild(QRadioButton.class, "convenzioneattiva")).toggled.connect(this, "convenzioneAttiva(Boolean)");

        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesaImpegnoEsistente")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_affidamento")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked());

        try {
            Method storeFactory = this.getClass().getMethod("storeResponsabileProcedimento");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Responsabileprocedimento");
            storeFactory= this.getClass().getMethod("storeImpedimentoDelegato");
            Register.registerUtility(storeFactory, IStoreFactory.class, "Impedimentodelegato");
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormDetermina.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormDetermina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void beniOServizi(Boolean b){
        ((QGroupBox) findChild(QGroupBox.class, "groupBox_A")).setEnabled(b);
        ((QGroupBox) findChild(QGroupBox.class, "groupBox_B")).setEnabled(b && ! ((QRadioButton) findChild(QRadioButton.class, "convenzioneattiva")).isChecked());
    }

    private void convenzioneAttiva(Boolean b){
        ((QGroupBox) findChild(QGroupBox.class, "groupBox_B")).setEnabled(!b);
    }

    private void impedimentoResponsabile(Boolean b){
        PyPaPiComboBox cmbImpedimentoDelegato=((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_ImpedimentoDelegato"));
        cmbImpedimentoDelegato.setEnabled(b);
        if ( !b ) {
            determina.setImpedimentodelegato(null);
            cmbImpedimentoDelegato.select(PyPaPiComboBox.ND);
        }
    }

    private void determinaSpesa(Boolean b){
        QCheckBox spesaIE=((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesaImpegnoEsistente"));
        QCheckBox affidamento=((QCheckBox) this.findChild(QCheckBox.class, "checkBox_affidamento"));

        spesaIE.setEnabled(b);
        affidamento.setEnabled(b);
        if (!b) {
            spesaIE.setChecked(false);
            affidamento.setChecked(false);
        }
    }

    @Override
    protected void indexChanged(int row) {
        determina = (Determina) this.getContext().getCurrentEntity();

        super.indexChanged(row);
        popolaProcedimento();
        popolaVisti();

        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesaImpegnoEsistente")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_affidamento")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked());
        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_pluriennale")).setEnabled(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_spesa")).isChecked() ||
                        ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_entrata")).isChecked());
        ((QSpinBox) this.findChild(QSpinBox.class, "spinBox_finoAl")).setVisible(
                ((QCheckBox) this.findChild(QCheckBox.class, "checkBox_pluriennale")).isChecked());

        this.determinaToolbar.actionByName("vistoLiquidazione").
                setEnabled(this.determinaToolbar.actionByName("vistoLiquidazione").isEnabled() &&
                        determina.getDiliquidazione() && vistoLiquidazione == null);

        Store store = storeResponsabileProcedimento();
        PyPaPiComboBox cmbResponsabileProcedimento=
                ((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_RespProcedimento"));
        cmbResponsabileProcedimento.setLookupStore(store);
        this.getColumn("Responsabileprocedimento").setLookupStore(store);
        cmbResponsabileProcedimento.select(determina.getResponsabileprocedimento());

        Store store1 = storeImpedimentoDelegato();
        PyPaPiComboBox cmbImpedimentoDelegato=
                ((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_ImpedimentoDelegato"));
        cmbImpedimentoDelegato.setLookupStore(store1);
        this.getColumn("Impedimentodelegato").setLookupStore(store1);
        cmbImpedimentoDelegato.select(determina.getImpedimentodelegato());

        Boolean modificaBloccata=Boolean.FALSE;
        if ( (!(determina.getDispesa() || determina.getDientrata() || determina.getSpesaimpegnoesistente()) &&
                determina.getVistoResponsabile()!=null ) ||
                determina.getVistoBilancio()!=null || determina.getVistoBilancioNegato()!=null ) {
            modificaBloccata=Boolean.TRUE;
        }
        this.getContext().setReadOnly(modificaBloccata);

        beniOServizi(((QCheckBox) findChild(QCheckBox.class, "benioservizi")).checkState().equals(Qt.CheckState.Checked));
    }

    private void popolaVisti() {
        Visto vistoResponsabile = determina.getVistoResponsabile();
        String testoResponsabile = "-";
        if( vistoResponsabile != null ){
            testoResponsabile = SuiteUtil.DATE_FORMAT.format(vistoResponsabile.getData()) + ", " + vistoResponsabile.getUtente();
            if( vistoResponsabile.getResponsabile() != null && !vistoResponsabile.getResponsabile().equals(vistoResponsabile.getUtente()) ){
                testoResponsabile += " (resp. " + vistoResponsabile.getResponsabile() + ")";
            }
        }
        QLabel responsabile = (QLabel) findChild(QLabel.class, "label_vistoResponsabile");
        responsabile.setText(testoResponsabile);

        Visto vistoBilancio = determina.getVistoBilancio();
        String testoBilancio = "-";
        if( vistoBilancio != null ){
            testoBilancio = SuiteUtil.DATE_FORMAT.format(vistoBilancio.getData()) + ", " + vistoBilancio.getUtente();
            if( vistoBilancio.getResponsabile() != null && !vistoBilancio.getResponsabile().equals(vistoBilancio.getUtente()) ){
                testoBilancio += " (resp. " + vistoBilancio.getResponsabile() + ")";
            }
        }
        QLabel bilancio = (QLabel) findChild(QLabel.class, "label_vistoBilancio");
        bilancio.setText(testoBilancio);

        Visto vistoBilancioNegato = determina.getVistoBilancioNegato();
        String testoBilancioNegato = "-";
        if( vistoBilancioNegato != null ){
            testoBilancioNegato = SuiteUtil.DATE_FORMAT.format(vistoBilancioNegato.getData()) + ", " + vistoBilancioNegato.getUtente();
            if( vistoBilancioNegato.getResponsabile() != null && !vistoBilancioNegato.getResponsabile().equals(vistoBilancioNegato.getUtente()) ){
                testoBilancioNegato += " (resp. " + vistoBilancioNegato.getResponsabile() + ")";
            }
        }
        QLabel bilancioNegato = (QLabel) findChild(QLabel.class, "label_vistoBilancioNegato");
        bilancioNegato.setText(testoBilancioNegato);

        vistoLiquidazione = determina.getVistoLiquidazione();
        String testoVistoLiquidazione = "";
        if( vistoLiquidazione != null ){
            testoVistoLiquidazione="(liquidata)";
        }
        QLabel liquidazione = (QLabel) findChild(QLabel.class, "label_vistoLiquidazione");
        liquidazione.setText(testoVistoLiquidazione);
    }

    private void popolaProcedimento() {
        QListWidget listWidget = (QListWidget) findChild(QListWidget.class, "procedimento");
        if( determina.getId() == null ){
            return;
        }
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        listWidget.clear();
        Integer i=0;
        for( FasePratica fp: wf.getFasi() ){
            Fase fase = fp.getFase();
            QIcon icon=null;
            if ( fp.equals(wf.getFaseAttiva()) ){
                icon = new QIcon("classpath:com/axiastudio/suite/resources/star.png");
            } else if ( fp.getCompletata() ){
            icon = new QIcon("classpath:com/axiastudio/suite/resources/tick.png");
            } else if ( fp.getNegata() ){
                icon = new QIcon("classpath:com/axiastudio/suite/resources/cross.png");
            }
            QListWidgetItem item = new QListWidgetItem(icon, fase.getDescrizione());
            item.setData(Qt.ItemDataRole.UserRole, i);
            listWidget.addItem(item);
            i++;
        }
    }

    private void completaFase(QListWidgetItem item){

        if( getContext().getIsDirty() ){
            QMessageBox.warning(this, "Attenzione", "Per completare una fase la determina deve essere prima salvata.");
            return;
        }
        Integer i = (Integer) item.data(Qt.ItemDataRole.UserRole);

        // XXX: se ci sono eventuali modifiche nelle condizioni?
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        FasePratica fasePratica = wf.getFase(i);

        // posso completare solo la fase attiva
        if( !wf.getFaseAttiva().equals(fasePratica) ){
            return;
        }

        SimpleWorkflowDialog swd = new SimpleWorkflowDialog(this, wf , fasePratica);
        int res = swd.exec();

        if( res == 1 ) {
            if (determina.getProtocollo() != null) {
                EntityManager em = this.getContext().getController().getEntityManager();
                if (em.contains(determina.getProtocollo())) {
                    em.detach(determina.getProtocollo());
                }
            }
            this.getContext().commitChanges();
        }
    }

/*
* Il primo servizio diventa principale, e non può più essere rimosso
* Se non indicato il referente politico, viene inserito quello di default x il servizio
*/
    private void servizioInserito(Object obj){
        ServizioDetermina inserita = (ServizioDetermina) obj;
        if( determina.getServizioDeterminaCollection().size() == 1 ){
            inserita.setPrincipale(Boolean.TRUE);
        }
        if ( determina.getReferentePolitico() == null || determina.getReferentePolitico().equals("")) {
            determina.setReferentePolitico(inserita.getServizio().getReferentepolitico());
            ((QLineEdit) findChild(QLineEdit.class, "lineEdit_RefPolitico")).setText(determina.getReferentePolitico());
        }
    }
    private void servizioRimosso(Object obj){
        ServizioDetermina rimossa = (ServizioDetermina) obj;
        if( rimossa.getPrincipale() ){
            QMessageBox.warning(this, "Attenzione", "Il servizio principale non può venir rimosso.");
            PyPaPiTableView tableViewServizio = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableView_servizi");
            ((ITableModel) tableViewServizio.model()).getContextHandle().insertElement(rimossa);
        }
    }

    private void servizioModificato(Object obj) {
        Store store = storeResponsabileProcedimento();
        PyPaPiComboBox cmbResponsabileProcedimento=
                ((PyPaPiComboBox) this.findChild(PyPaPiComboBox.class, "comboBox_RespProcedimento"));
        cmbResponsabileProcedimento.setLookupStore(store);
        this.getColumn("Responsabileprocedimento").setLookupStore(store);
        if ( cmbResponsabileProcedimento.findData(determina.getResponsabileprocedimento()) > -1 ) {
            cmbResponsabileProcedimento.select(determina.getResponsabileprocedimento());
        } else {
            determina.setResponsabileprocedimento(null);
            cmbResponsabileProcedimento.select(null);
        }
    }

    /*
     * Uno store contenente i resp procedimento per il servizio principale ordinati x descrizione
     */
    public Store storeResponsabileProcedimento(){
        List<Soggetto> respProcedimento = new ArrayList<Soggetto>();

        if (this.getContext() == null || this.getContext().getCurrentEntity() == null) {
            return new Store(respProcedimento);
        }

        Determina determina = (Determina) this.getContext().getCurrentEntity();
        if ( determina.getId() == null || determina.getServizioDeterminaCollection().isEmpty() ) {
            return new Store(respProcedimento);
        }

        for (ServizioDetermina servizio: determina.getServizioDeterminaCollection()) {
            if ( servizio.getPrincipale() ) {
                Database db = (Database) Register.queryUtility(IDatabase.class);
                Controller controller = db.createController(Delega.class);
                EntityManager em = controller.getEntityManager();

                String query = "SELECT u FROM Delega d JOIN d.utente u JOIN d.carica c JOIN d.servizio s ";
                query = query + "WHERE s.id = " + servizio.getServizio().getId().toString() +
                        " AND (d.fine IS NULL OR d.fine>current_timestamp) " +
                        " AND c.codiceCarica='RESPONSABILE_BENEFICI_DENARO'";
                for (Utente resp: em.createQuery(query, Utente.class).getResultList()) {
                    respProcedimento.add(resp.getSoggetto());
                }
            }
        }
        return new Store(respProcedimento);
    }

    /*
     * Uno store contenente i delegati in caso di impedimento del responsabile
     */
    public Store storeImpedimentoDelegato(){
        List<Utente> delegato = new ArrayList<Utente>();

        if (this.getContext() == null || this.getContext().getCurrentEntity() == null) {
            return new Store(delegato);
        }

        Determina determina = (Determina) this.getContext().getCurrentEntity();
        if ( determina.getId() == null || determina.getServizioDeterminaCollection().isEmpty() ) {
            return new Store(delegato);
        }

        for (ServizioDetermina servizio: determina.getServizioDeterminaCollection()) {
            if ( servizio.getPrincipale() ) {
                Database db = (Database) Register.queryUtility(IDatabase.class);
                Controller controller = db.createController(Delega.class);
                EntityManager em = controller.getEntityManager();

                String query = "SELECT u FROM Delega d JOIN d.utente u JOIN d.servizio s ";
                query = query + "WHERE s.id = " + servizio.getServizio().getId().toString() +
                        " AND (d.fine IS NULL OR d.fine>current_timestamp) " +
                        " AND d.impedimento=TRUE AND d.delegante.id = " + determina.getResponsabile().getId().toString();
                for (Utente resp: em.createQuery(query, Utente.class).getResultList()) {
                    delegato.add(resp);
                }
            }
        }
        return new Store(delegato);
    }

    /* XXX: codice simile a FormPratica */
    @Override
    public List<Template> getTemplates() {
        List<Template> templates = new ArrayList<Template>();
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormDetermina.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(determina);
        helper.children("protocollo"); // XXX: per creare il subpath "protocollo"
        helper.children("da_non_protocollare"); // XXX: per creare il subpath "protocollo"
        List<HashMap> children = helper.children();
        for( HashMap map: children ){
            String name = (String) map.get("name");
            if( name.toLowerCase().endsWith(".odt") || name.toLowerCase().endsWith(".doc") ){
                String title = (String) map.get("title");
                String description = (String) map.get("description");
                IStreamProvider streamProvider = cmisPlugin.createCmisStreamProvider((String) map.get("objectId"));
                //RuleSet rulesSet = new RuleSet(new HashMap()); // XXX: da pescare
                Template template = new Template(streamProvider, name, title, description);
                templates.add(template);
            }
        }
        return templates;
    }

    /* XXX: codice simile a FormPratica */
    @Override
    public void createDocument(String subpath, String name, String title, String description, byte[] content, String mimeType) {
        //Pratica pratica = SuiteUtil.findPratica(pratica.getIdpratica());
        CmisPlugin cmisPlugin = (CmisPlugin) Register.queryPlugin(FormDetermina.class, "CMIS");
        AlfrescoHelper helper = cmisPlugin.createAlfrescoHelper(determina);

        // extension
        String extension = "";
        if( mimeType.equals("application/pdf") ){
            extension = ".pdf";
        } else if( mimeType.equals("application/vnd.oasis.opendocument.text") ){
            extension = ".odt";
        } else if( mimeType.equals("application/msword") ){
            extension = ".doc";
        }
        String documentName;
        if( name.endsWith(".odt") || name.endsWith(".doc") ){
            documentName = name.substring(0, name.length()-4).concat(extension);
        } else {
            documentName = name.concat("_").concat(determina.getPratica().getIdpratica()).concat(extension);
        }
        helper.createDocument(subpath, documentName, content, mimeType, title, description);
        cmisPlugin.showForm(determina);
    }

    // XXX: codice simile a FormScrivania
    private void apriDocumenti(){
        if( determina == null || determina.getId() == null ){
            return;
        }
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        List<IPlugin> plugins = (List) Register.queryPlugins(this.getClass());
        for(IPlugin plugin: plugins){
            if( "CMIS".equals(plugin.getName()) ){
                Boolean view = true;
                Boolean delete = true;
                Boolean download = true;
                Boolean parent = false;
                Boolean upload = true;
                Boolean version = true;
                if( view ){
                    ((CmisPlugin) plugin).showForm(determina, delete, download, parent, upload, version);
                } else {
                    QMessageBox.warning(this, "Attenzione", "Non disponi dei permessi per visualizzare i documenti");
                    return;
                }
            }
        }
    }

    private void apriOoops(){
        if( determina == null || determina.getId() == null ){
            return;
        }
        List<IPlugin> plugins = (List) Register.queryPlugins(this.getClass());
        for(IPlugin plugin: plugins){
            if( "Ooops".equals(plugin.getName()) ){
                plugin.install(this, false);
                ((OoopsPlugin) plugin).showForm();
            }
        }
    }

    private void vistoLiquidazione(){
        SimpleWorkFlow wf = new SimpleWorkFlow(determina);
        Fase fase = new Fase();
        FasePratica fp = new FasePratica();

        fase.setId(Long.parseLong(SuiteUtil.trovaCostante("FASE_LIQUIDAZIONE").getValore()));
        fp.setFase(fase);
        fp.setPratica(determina.getPratica());
        wf.creaVisto(fp, false, "", false, true);

        this.getContext().refreshElement();
    }

    private void information() {
        SuiteUiUtil.showInfo(this);
    }

}
