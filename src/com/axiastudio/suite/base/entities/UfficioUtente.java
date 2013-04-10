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
package com.axiastudio.suite.base.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
@Entity
@Table(schema="BASE")
@SequenceGenerator(name="genufficioutente", sequenceName="base.ufficioutente_id_seq", initialValue=1, allocationSize=1)
public class UfficioUtente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genufficioutente")
    private Long id;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "utente", referencedColumnName = "id")
    @ManyToOne
    private Utente utente;
    @Column(name="ricerca")
    private Boolean ricerca=true;
    @Column(name="visualizza")
    private Boolean visualizza=true;
    @Column(name="privato")
    private Boolean privato=false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Boolean getPrivato() {
        return privato;
    }

    public void setPrivato(Boolean privato) {
        this.privato = privato;
    }

    public Boolean getRicerca() {
        return ricerca;
    }

    public void setRicerca(Boolean ricerca) {
        this.ricerca = ricerca;
    }

    public Boolean getVisualizza() {
        return visualizza;
    }

    public void setVisualizza(Boolean visualizza) {
        this.visualizza = visualizza;
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
        if (!(object instanceof UfficioUtente)) {
            return false;
        }
        UfficioUtente other = (UfficioUtente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.axiastudio.suite.base.entities.UfficioUtente[ id=" + id + " ]";
    }
    
}
