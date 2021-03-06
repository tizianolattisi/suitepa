package com.axiastudio.suite.pratiche;

import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.finanziaria.entities.Servizio;
import com.axiastudio.suite.pratiche.entities.Pratica;

/**
 * User: tiziano
 * Date: 04/12/13
 * Time: 09:02
 */
public interface IDettaglio {

    Pratica getPratica();
    void setPratica(Pratica pratica);
    String getOggetto();
    void setOggetto(String oggetto);

    // campi proxy (setter in NOP)
    String getIdpratica();
    void setIdpratica(String idpratica);
    String getCodiceinterno();
    void setCodiceinterno(String codiceinterno);

    // per la gestione delle deleghe nei procedimenti
    Servizio getServizio();
    Ufficio getUfficio();

}
