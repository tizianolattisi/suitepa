/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.protocollo.entities.Attribuzione;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class SuiteUtil {
    
    public static String digest(String s){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte byteData[] = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SuiteUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Store attribuzioni(Utente autenticato){
        Store store;
        Database db = (Database) Register.queryUtility(IDatabase.class);
        EntityManager em = db.getEntityManagerFactory().createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Attribuzione> cq = cb.createQuery(Attribuzione.class);
        Root<Attribuzione> root = cq.from(Attribuzione.class);
        cq.select(root);
        //cq.where(cb.equal(root.get(Attribuzione_.ufficio), year));
        TypedQuery<Attribuzione> tq = em.createQuery(cq);
        List<Attribuzione> resultList = tq.getResultList();
        store = new Store(resultList);
        return store;
    }
    
}
