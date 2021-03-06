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
package com.axiastudio.suite.finanziaria.entities;

import com.axiastudio.suite.base.entities.Ufficio;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
@Entity
@Table(schema="FINANZIARIA")
@SequenceGenerator(name="genservizio", sequenceName="finanziaria.servizio_id_seq", initialValue=1, allocationSize=1)
public class Servizio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="genservizio")
    private Long id;
    @Column(name="descrizione", length=1024)
    private String descrizione;
    @JoinColumn(name = "ufficio", referencedColumnName = "id")
    @ManyToOne
    private Ufficio ufficio;
    @JoinColumn(name = "attribuzione", referencedColumnName = "id")
    @ManyToOne
    private Ufficio attribuzione;
    @Column(name="referentepolitico")
    private String referentepolitico;
    @Column(name="responsabileprocedura")
    private String responsabileprocedura;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Ufficio getUfficio() {
        return ufficio;
    }

    public void setUfficio(Ufficio ufficio) {
        this.ufficio = ufficio;
    }

    public Ufficio getAttribuzione() {
        return attribuzione;
    }

    public void setAttribuzione(Ufficio attribuzione) {
        this.attribuzione = attribuzione;
    }

    public String getReferentepolitico() {
        return referentepolitico;
    }

    public void setReferentepolitico(String referentepolitico) {
        this.referentepolitico = referentepolitico;
    }

    public String getResponsabileprocedura() {
        return responsabileprocedura;
    }

    public void setResponsabileprocedura(String responsabileprocedura) {
        this.responsabileprocedura = responsabileprocedura;
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
        if (!(object instanceof Servizio)) {
            return false;
        }
        Servizio other = (Servizio) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return this.getDescrizione();
    }
    
}
