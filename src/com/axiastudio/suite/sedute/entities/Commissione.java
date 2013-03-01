/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite.sedute.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 * 
 * Le commissioni sono gli organi che si riuniscono in seduta per discutere e
 * votare l'approvazione di pratiche.
 * 
 * Esempi di commissione sono la giunta, il consiglio, la commissione edilizia.
 * 
 */
@Entity
@Table(schema="SEDUTE")
@SequenceGenerator(name="gencommissione", sequenceName="sedute.commissione_id_seq", initialValue=1, allocationSize=1)
public class Commissione implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="gencommissione")
    private Long id;
    @Column(name="descrizione", length=1024)
    private String descrizione;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Commissione)) {
            return false;
        }
        Commissione other = (Commissione) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDescrizione();
    }

}