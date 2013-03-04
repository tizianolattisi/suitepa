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
package com.axiastudio.suite.pratiche.forms;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.suite.pratiche.entities.TipologiaPratica;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormTipologiePratica extends QDialog {
    private QTreeWidget tree;
    
    public FormTipologiePratica(){
        this(null);
    }
        
    public FormTipologiePratica(QWidget parent){
        super(parent);
        tree = new QTreeWidget();
        this.tree.header().hide();
        this.tree.doubleClicked.connect(this, "accept()");
        QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(this.tree);
        QHBoxLayout buttonLayout = new QHBoxLayout();
        buttonLayout.addSpacerItem(new QSpacerItem(10, 10, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum));
        QToolButton cancel = new QToolButton();
        cancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
        cancel.clicked.connect(this, "reject()");
        buttonLayout.addWidget(cancel);
        QToolButton accept = new QToolButton();
        accept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
        accept.clicked.connect(this, "accept()");
        buttonLayout.addWidget(accept);
        layout.addLayout(buttonLayout);
        this.setLayout(layout);
        this.popola(this.tree, null, null);
                
    }
    
    private List<TipologiaPratica> children(EntityManager em, TipologiaPratica parent){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(TipologiaPratica.class);
        Root root = cq.from(TipologiaPratica.class);
        if( parent == null ){
            cq.where(cb.isNull(root.get("tipologiapadre")));
        } else {
            cq.where(cb.equal(root.get("tipologiapadre"), parent));
        }
        Query q = em.createQuery(cq);
        return q.getResultList();
    }
    
     public TipologiaPratica getSelection() {
        QTreeWidgetItem currentItem = this.tree.currentItem();
        TipologiaPratica tipologia = (TipologiaPratica) currentItem.data(0, Qt.ItemDataRole.UserRole);
        return tipologia;
    }
   
    public void popola(QTreeWidget tree, QTreeWidgetItem parentItem, TipologiaPratica parent){
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        List<TipologiaPratica> children = this.children(em, parent);
        for( int i=0; i<children.size(); i++ ){
            TipologiaPratica tipologiaPratica = (TipologiaPratica) children.get(i);
            QTreeWidgetItem item = new QTreeWidgetItem();
            item.setText(0, tipologiaPratica.getCodice());
            item.setToolTip(0, "<FONT COLOR=black>" + tipologiaPratica.getDescrizione() + "</FONT>");
            item.setData(0, Qt.ItemDataRole.UserRole, tipologiaPratica);
            if( parentItem == null ){
                tree.addTopLevelItem(item);
            } else {
                parentItem.addChild(item);
            }
            this.popola(tree, item, tipologiaPratica);
        }
    }
}
