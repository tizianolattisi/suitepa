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
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.IForm;
import com.axiastudio.pypapi.ui.IUIFile;
import com.axiastudio.suite.base.entities.CambiaPassword;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.forms.FormTipoPratica;
import com.axiastudio.suite.protocollo.forms.FormScrivania;
import com.axiastudio.suite.protocollo.forms.FormTitolario;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSignalMapper;
import com.trolltech.qt.gui.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Mdi extends QMainWindow {
    
    private static String ICON = "classpath:com/axiastudio/pypapi/ui/resources/pypapi32.png";
    private QMdiArea workspace;
    private QTreeWidget tree;
    private QSystemTrayIcon trayIcon;
    private QMenu menuWindows;
    private QAction actionCloseAll;
    private QAction actionTile;
    private QAction actionCascade;
    private QAction actionNext;
    private QAction actionPrevious;
    private QAction actionSeparator;
    private QAction actionClose;
    private QSignalMapper windowMapper;
    
    public Mdi(){
        this.setWindowIcon(new QIcon(ICON));
        this.createWorkspace();
        this.createTree();
        //this.createSystemTray();
        this.createMenu();
        
    }
    
    private void createMenu(){
        menuWindows = this.menuBar().addMenu("Finestre");
        actionClose = new QAction("Chiudi", this);
        actionClose.triggered.connect(this.workspace, "closeActiveSubWindow()");
        actionCloseAll = new QAction("Chiudi tutte", this);
        actionCloseAll.triggered.connect(this.workspace, "closeAllSubWindows()");
        actionTile = new QAction("Allinea", this);
        actionTile.triggered.connect(this.workspace, "tileSubWindows()");
        actionCascade = new QAction("Disponi a cascata", this);
        actionCascade.triggered.connect(this.workspace, "cascadeSubWindows()");
        actionNext = new QAction("Finestra successiva", this);
        actionNext.triggered.connect(this.workspace, "activateNextSubWindow()");
        actionPrevious = new QAction("Finestra precedente", this);
        actionPrevious.triggered.connect(this.workspace, "activatePreviousSubWindow()");
        actionSeparator = new QAction(this);
        actionSeparator.setSeparator(true);
        
        menuWindows.aboutToShow.connect(this, "refreshMenuWindows()");     
    }
    
    private void refreshMenuWindows(){
        
        menuWindows.clear();
        menuWindows.addAction(actionClose);
        menuWindows.addAction(actionCloseAll);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionTile);
        menuWindows.addAction(actionCascade);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionNext);
        menuWindows.addAction(actionPrevious);
        menuWindows.addAction(actionSeparator);
        menuWindows.addAction(actionCloseAll);
        
        for( QMdiSubWindow subWindow: this.workspace.subWindowList() ){
            
            String title="";
            if( subWindow.widget() instanceof QMainWindow ){
                title = ((QMainWindow) subWindow.widget()).windowTitle();
            }
            if( subWindow.widget() instanceof QDialog ){
                title = ((QDialog) subWindow.widget()).windowTitle();
            }
            
            QAction action = menuWindows.addAction(title);
            action.setCheckable(true);
            action.setChecked(subWindow.equals(this.workspace.activeSubWindow()));
            action.triggered.connect(windowMapper, "map()");
            windowMapper.setMapping(action, subWindow);
        }
    }
    
    private void createSystemTray(){
        QMenu menu = new QMenu(this);
        menu.addAction("prova");
        this.trayIcon = new QSystemTrayIcon(new QIcon(ICON), this);
        this.trayIcon.setContextMenu(menu);
        this.trayIcon.show();
        this.trayIcon.showMessage("PyPaPi Suite", "Applicazione avviata.");
        
    }
    
    private void createWorkspace() {
        QSplitter splitter = new QSplitter();
        this.tree = new QTreeWidget(splitter);
        this.workspace = new QMdiArea(splitter);
        this.setCentralWidget(splitter);        
        this.workspace.subWindowActivated.connect(this, "refreshMenuWindows()");
        windowMapper = new QSignalMapper(this);
        windowMapper.mappedQObject.connect(this, "setActiveSubWindow(QObject)");
    }
    
    private void setActiveSubWindow(QObject obj){
        this.workspace.setActiveSubWindow((QMdiSubWindow) obj);
    }
    
    private void createTree() {
        this.tree.setColumnCount(2);
        this.tree.setHeaderLabel("PyPaPi Suite");
        this.tree.setColumnHidden(1, true);
        
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);

        /* scrivania */
        QTreeWidgetItem itemScrivania = new QTreeWidgetItem(this.tree);
        itemScrivania.setText(0, "Scrivania");
        itemScrivania.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/house.png"));
        itemScrivania.setText(1, "SCRIVANIA");
        itemScrivania.setDisabled(!autenticato.getOperatoreprotocollo());

        /* Protocollo informatico */
        QTreeWidgetItem itemProtocolloInformatico = new QTreeWidgetItem(this.tree);
        itemProtocolloInformatico.setText(0, "Protocollo informatico");
        this.tree.addTopLevelItem(itemProtocolloInformatico);
        itemProtocolloInformatico.setDisabled(!autenticato.getOperatoreprotocollo());
        
        QTreeWidgetItem itemProtocollo = new QTreeWidgetItem(itemProtocolloInformatico);
        itemProtocollo.setText(0, "Protocollo");
        itemProtocollo.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemProtocollo.setText(1, "com.axiastudio.suite.protocollo.entities.Protocollo");
        itemProtocollo.setText(2, "NEW");
        Controller controllerProtocollo = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.protocollo.entities.Protocollo");
        //Store storeProtocollo = controllerProtocollo.createStore(10);
        //itemProtocollo.setData(1, Qt.ItemDataRole.UserRole, storeProtocollo);

        QTreeWidgetItem itemTitolario = new QTreeWidgetItem(itemProtocolloInformatico);
        itemTitolario.setText(0, "Titolario");
        itemTitolario.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTitolario.setText(1, "TITOLARIO");

        QTreeWidgetItem itemPubblicazioni = new QTreeWidgetItem(itemProtocolloInformatico);
        itemPubblicazioni.setText(0, "Pubblicazioni");
        itemPubblicazioni.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemPubblicazioni.setText(1, "com.axiastudio.suite.pubblicazioni.entities.Pubblicazione");
        itemPubblicazioni.setDisabled(true);
        
        /* Anagrafiche */
        QTreeWidgetItem itemAnagrafiche = new QTreeWidgetItem(this.tree);
        itemAnagrafiche.setText(0, "Anagrafiche");
        this.tree.addTopLevelItem(itemAnagrafiche);
        itemAnagrafiche.setDisabled(!autenticato.getOperatoreanagrafiche());

        QTreeWidgetItem itemSoggetti = new QTreeWidgetItem(itemAnagrafiche);
        itemSoggetti.setText(0, "Soggetti");
        itemSoggetti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemSoggetti.setText(1, "com.axiastudio.suite.anagrafiche.entities.Soggetto");
        itemSoggetti.setText(2, "NEW");
        //Controller controllerSoggetto = (Controller) Register.queryUtility(IController.class, "com.axiastudio.suite.anagrafiche.entities.Soggetto");
        //Store storeSoggetto = controllerSoggetto.createStore(10);
        //itemSoggetti.setData(1, Qt.ItemDataRole.UserRole, storeSoggetto);
        QTreeWidgetItem itemGruppi = new QTreeWidgetItem(itemAnagrafiche);
        itemGruppi.setText(0, "Gruppi");
        itemGruppi.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemGruppi.setText(1, "com.axiastudio.suite.anagrafiche.entities.Gruppo");
        QTreeWidgetItem itemRelazioni = new QTreeWidgetItem(itemAnagrafiche);
        itemRelazioni.setText(0, "Relazioni");
        itemRelazioni.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemRelazioni.setText(1, "com.axiastudio.suite.anagrafiche.entities.Relazione");
        
        /* Pratiche */
        QTreeWidgetItem itemPraticheRoot = new QTreeWidgetItem(this.tree);
        itemPraticheRoot.setText(0, "Pratiche...");
        this.tree.addTopLevelItem(itemPraticheRoot);
        itemPraticheRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemPratiche = new QTreeWidgetItem(itemPraticheRoot);
        itemPratiche.setText(0, "Pratiche");
        itemPratiche.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemPratiche.setText(1, "com.axiastudio.suite.pratiche.entities.Pratica");
        itemPratiche.setText(2, "NEW");

        QTreeWidgetItem itemTipoPratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipoPratica.setText(0, "Tipo di pratica");
        itemTipoPratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemTipoPratica.setText(1, "com.axiastudio.suite.pratiche.entities.TipoPratica");
        
        QTreeWidgetItem itemTipiPratica = new QTreeWidgetItem(itemPraticheRoot);
        itemTipiPratica.setText(0, "Struttura tipi di pratica");
        itemTipiPratica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemTipiPratica.setText(1, "TIPIPRATICA");

        /* Richieste */
        QTreeWidgetItem itemRichiesteRoot = new QTreeWidgetItem(this.tree);
        itemRichiesteRoot.setText(0, "Richieste...");
        this.tree.addTopLevelItem(itemRichiesteRoot);
        //itemRichiesteRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemRichieste = new QTreeWidgetItem(itemRichiesteRoot);
        itemRichieste.setText(0, "Richieste");
        itemRichieste.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/email.png"));
        itemRichieste.setText(1, "com.axiastudio.suite.richieste.entities.Richiesta");
        itemRichieste.setText(2, "NEW");

        /* Delibere e determine */
        QTreeWidgetItem itemDelibereDetermineRoot = new QTreeWidgetItem(this.tree);
        itemDelibereDetermineRoot.setText(0, "Delibere e determine");
        this.tree.addTopLevelItem(itemDelibereDetermineRoot);
        itemDelibereDetermineRoot.setDisabled(!autenticato.getOperatorepratiche());

        QTreeWidgetItem itemDelibere = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDelibere.setText(0, "Delibere");
        itemDelibere.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDelibere.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDelibere.setText(2, "NEW");
        itemDelibere.setDisabled(true);

        QTreeWidgetItem itemDetermine = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemDetermine.setText(0, "Determine");
        itemDetermine.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemDetermine.setText(1, "com.axiastudio.suite.deliberedetermine.entities.Determina");
        itemDetermine.setText(2, "NEW");

        QTreeWidgetItem itemSedute = new QTreeWidgetItem(itemDelibereDetermineRoot);
        itemSedute.setText(0, "Sedute");
        itemSedute.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemSedute.setText(1, "com.axiastudio.suite.sedute.entities.Seduta");
        itemSedute.setText(2, "NEW");
        itemSedute.setDisabled(true);

        /* Procedimento */
        QTreeWidgetItem itemProcedimentiRoot = new QTreeWidgetItem(this.tree);
        itemProcedimentiRoot.setText(0, "Procedimenti");
        this.tree.addTopLevelItem(itemProcedimentiRoot);
        itemProcedimentiRoot.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemProcedimento = new QTreeWidgetItem(itemProcedimentiRoot);
        itemProcedimento.setText(0, "Procedimenti");
        itemProcedimento.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemProcedimento.setText(1, "com.axiastudio.suite.procedimenti.entities.Procedimento");
        itemProcedimento.setText(2, "NEW");

        QTreeWidgetItem itemNorma = new QTreeWidgetItem(itemProcedimentiRoot);
        itemNorma.setText(0, "Norme");
        itemNorma.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemNorma.setText(1, "com.axiastudio.suite.procedimenti.entities.Norma");
        itemNorma.setText(2, "NEW");
        
        /* Configurazione sedute */
        QTreeWidgetItem itemConfigurazioneSeduteRoot = new QTreeWidgetItem(this.tree);
        itemConfigurazioneSeduteRoot.setText(0, "Configurazione sedute");
        this.tree.addTopLevelItem(itemConfigurazioneSeduteRoot);
        itemConfigurazioneSeduteRoot.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemCarica = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCarica.setText(0, "Cariche");
        itemCarica.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCarica.setText(1, "com.axiastudio.suite.procedimenti.entities.Carica");

        QTreeWidgetItem itemCommissione = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCommissione.setText(0, "Commissioni");
        itemCommissione.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCommissione.setText(1, "com.axiastudio.suite.sedute.entities.Commissione");

        QTreeWidgetItem itemCaricaCommissione = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemCaricaCommissione.setText(0, "Cariche-commissioni");
        itemCaricaCommissione.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/vcard.png"));
        itemCaricaCommissione.setText(1, "com.axiastudio.suite.sedute.entities.CaricaCommissione");
        
        QTreeWidgetItem itemTipoSeduta = new QTreeWidgetItem(itemConfigurazioneSeduteRoot);
        itemTipoSeduta.setText(0, "Tipi di seduta");
        itemTipoSeduta.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemTipoSeduta.setText(1, "com.axiastudio.suite.sedute.entities.TipoSeduta");
        itemTipoSeduta.setDisabled(!autenticato.getAmministratore());

        /* Amministrazione */
        QTreeWidgetItem itemAmministrazione = new QTreeWidgetItem(this.tree);
        itemAmministrazione.setText(0, "Amministrazione");
        this.tree.addTopLevelItem(itemAmministrazione);

        QTreeWidgetItem itemCostanti = new QTreeWidgetItem(itemAmministrazione);
        itemCostanti.setText(0, "Costanti");
        itemCostanti.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemCostanti.setText(1, "com.axiastudio.suite.generale.entities.Costante");
        itemCostanti.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemEtichette = new QTreeWidgetItem(itemAmministrazione);
        itemEtichette.setText(0, "Etichette");
        itemEtichette.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemEtichette.setText(1, "com.axiastudio.suite.generale.entities.Etichetta");
        itemEtichette.setDisabled(!autenticato.getAmministratore());
        
        QTreeWidgetItem itemUtenti = new QTreeWidgetItem(itemAmministrazione);
        itemUtenti.setText(0, "Utenti");
        itemUtenti.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/user.png"));
        itemUtenti.setText(1, "com.axiastudio.suite.base.entities.Utente");
        itemUtenti.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemUffici = new QTreeWidgetItem(itemAmministrazione);
        itemUffici.setText(0, "Uffici");
        itemUffici.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemUffici.setText(1, "com.axiastudio.suite.base.entities.Ufficio");
        itemUffici.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemModelli = new QTreeWidgetItem(itemAmministrazione);
        itemModelli.setText(0, "Modelli");
        itemModelli.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemModelli.setText(1, "com.axiastudio.suite.modelli.entities.Modello");
        itemModelli.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemGiunte = new QTreeWidgetItem(itemAmministrazione);
        itemGiunte.setText(0, "Giunte");
        itemGiunte.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/cog.png"));
        itemGiunte.setText(1, "com.axiastudio.suite.base.entities.Giunta");
        itemGiunte.setDisabled(!autenticato.getAmministratore());

        QTreeWidgetItem itemDeleghe = new QTreeWidgetItem(itemAmministrazione);
        itemDeleghe.setText(0, "Incarichi e deleghe");
        itemDeleghe.setIcon(0, new QIcon("classpath:com/axiastudio/suite/resources/group.png"));
        itemDeleghe.setText(1, "com.axiastudio.suite.procedimenti.entities.Delega");
        //itemDeleghe.setDisabled(!autenticato.getAmministratore());
        
        QTreeWidgetItem itemPassword = new QTreeWidgetItem(itemAmministrazione);
        itemPassword.setText(0, "Cambio password");
        itemPassword.setIcon(0, new QIcon("classpath:com/axiastudio/pypapi/ui/resources/key.png"));
        itemPassword.setText(1, "PASSWORD");
        
        this.tree.activated.connect(this, "runTask()");
        this.tree.setMinimumWidth(200);

    }
    
    private void runTask() {
        String formName = this.tree.currentItem().text(1);
        if (formName == null || formName.equals("")) {
            return;  // item di raggruppamento
        }

        String mode = this.tree.currentItem().text(2);
        /* cambio password */
        if( "NEW".equals(formName) ){
            
        }
        if( "PASSWORD".equals(formName) ){
            CambiaPassword passDlg = new CambiaPassword(this);
            int exec = passDlg.exec();
        } else if( "TITOLARIO".equals(formName) ){
            FormTitolario titolario = new FormTitolario();
            this.workspace.addSubWindow(titolario);
            int exec = titolario.exec();
        } else if( "TIPIPRATICA".equals(formName) ){
            FormTipoPratica tipipratica = new FormTipoPratica();
            this.workspace.addSubWindow(tipipratica);
            int exec = tipipratica.exec();
        } else if( "SCRIVANIA".equals(formName) ){
            FormScrivania form = new FormScrivania();
            this.workspace.addSubWindow(form);
            form.show();
        } else {
            /* form registrata */
            Window form=null;
            Class<? extends Window> formClass = (Class) Register.queryUtility(IForm.class, formName);
            String uiFile = (String) Register.queryUtility(IUIFile.class, formName);
            Class factory = (Class) Register.queryUtility(IFactory.class, formName);
            try {
                Constructor<? extends Window> constructor = formClass.getConstructor(new Class[]{String.class, Class.class, String.class});
                try {
                    form = constructor.newInstance(new Object[]{uiFile, factory, ""});
                } catch (InstantiationException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Mdi.class.getName()).log(Level.SEVERE, null, ex);
            }
            // A store with a new element
            Store store = null;
            if( "NEW".equals(mode) ){
                Controller controller = (Controller) Register.queryUtility(IController.class, factory.getName());
                store = controller.createNewStore();
            }
            if( store != null ){
                form.init(store);
            } else {
                form.init();
            }
            this.workspace.addSubWindow(form);
            this.showForm(form);
            this.menuWindows.addAction(form.toString());
        }
    }
    
    private void showForm(Window form) {
        if( this.workspace.subWindowList().size()>1 ){
            form.show();
        } else {
            form.showMaximized();
        }
    }

    public QMdiArea getWorkspace() {
        return workspace;
    }
    
    
}
