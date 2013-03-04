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
package com.axiastudio.suite.protocollo;

import com.axiastudio.pypapi.annotations.Adapter;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.RiferimentoProtocollo;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ProtocolloAdapters {
    
    @Adapter
    public static Attribuzione adaptUfficioToAttribuzione(Ufficio ufficio){
        Attribuzione ap = new Attribuzione();
        ap.setUfficio(ufficio);
        ap.setPrincipale(Boolean.TRUE);
        return ap;
    }
    
    @Adapter
    public static RiferimentoProtocollo adaptProtocolloToRiferimentoProtocollo(Protocollo protocollo){
        RiferimentoProtocollo rp = new RiferimentoProtocollo();
        rp.setPrecedente(protocollo);
        return rp;
    }
    
}
