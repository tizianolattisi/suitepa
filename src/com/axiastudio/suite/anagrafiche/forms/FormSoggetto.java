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
package com.axiastudio.suite.anagrafiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.ICriteriaFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.Context;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.axiastudio.suite.SuiteUiUtil;
import com.axiastudio.suite.anagrafiche.entities.Gruppo;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.base.entities.IUtente;
import com.axiastudio.suite.base.entities.Utente;
import com.trolltech.qt.gui.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormSoggetto extends Window {
    
    public FormSoggetto(String uiFile, Class entityClass, String title){
        super(uiFile, entityClass, title);
        QComboBox tipoSoggetto = (QComboBox) this.findChild(QComboBox.class, "comboBoxTipoSoggetto");
        tipoSoggetto.currentIndexChanged.connect(this, "aggiornaTipoSoggetto(Integer)");
        this.aggiornaTipoSoggetto(tipoSoggetto.currentIndex());
        QTabWidget tab = (QTabWidget) this.findChild(QTabWidget.class, "tabWidgetBody");
        tab.currentChanged.connect(this, "aggiornaFiltroGruppo(Integer)");

        /* Non permessa modifica di indirizzo direttamente da tabella */
        PyPaPiTableView tabellaIndirizzi = (PyPaPiTableView) this.findChild(PyPaPiTableView.class, "tableViewIndirizzi");
        tabellaIndirizzi.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers);
    }

    @Override
    public void init(Store store) {
        super.init(store);
        /* inserimento nuove anagrafiche permesso solo agli utenti con flag "supervisore anagrafiche" */
        Context dataContext = this.getContext();
        Utente autenticato = (Utente) Register.queryUtility(IUtente.class);
        dataContext.setNoInsert(!autenticato.getSupervisoreanagrafiche());
    }

    @Override
    protected void indexChanged(int row) {
        super.indexChanged(row);
        /* disabilito TipoSoggetto se anagrafica già inserita */
        ((QWidget) this.findChild(QComboBox.class, "comboBoxTipoSoggetto")).
                setEnabled((((Soggetto) this.getContext().getCurrentEntity()).getId() == null));
    }

    /*
     * Abilitazione del tab corretto e filtro sui gruppi
     */
    private void aggiornaTipoSoggetto(Integer idx){
        QTabWidget tab = (QTabWidget) this.findChild(QTabWidget.class, "tabWidgetHeader");
        tab.setCurrentIndex(idx);
        for( int i=0; i<3; i++ ){
            tab.setTabEnabled(i, i==idx);
        }
        QLineEdit lineEdit_pratitaiva = ((QLineEdit) this.findChild(QLineEdit.class, "lineEdit_partitaiva"));
        if( idx == 0 ){
            // Persona, disabilito la partita iva
            lineEdit_pratitaiva.clear();
            lineEdit_pratitaiva.setEnabled(false);
        } else {
            lineEdit_pratitaiva.setEnabled(true);
        }
        aggiornaFiltroGruppo(idx);
    }

    private void aggiornaFiltroGruppo(Integer unused) {
        QTabWidget tab = (QTabWidget) this.findChild(QTabWidget.class, "tabWidgetHeader");
        Integer idx = tab.currentIndex();
        String[] gruppi = {"P", "A", "E"};
        try {
            Method provider = FormSoggetto.class.getMethod("gruppo"+gruppi[idx]+"PredicateProvider", CriteriaBuilder.class, Root.class);
            Register.registerUtility(provider, ICriteriaFactory.class, Gruppo.class.getName());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FormSoggetto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FormSoggetto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void information() {
        SuiteUiUtil.showInfo(this);
    }
    
    /*
     * Un predicato per filtrare i gruppi sulla base della tipologia di soggetto
     */
    public static Predicate gruppoAPredicateProvider(CriteriaBuilder cb, Root from) {
        return cb.equal(from.get("azienda"), Boolean.TRUE);
    }
    public static Predicate gruppoPPredicateProvider(CriteriaBuilder cb, Root from) {
        return cb.equal(from.get("persona"), Boolean.TRUE);
    }
    public static Predicate gruppoEPredicateProvider(CriteriaBuilder cb, Root from) {
        return cb.equal(from.get("ente"), Boolean.TRUE);
    }
    
}
