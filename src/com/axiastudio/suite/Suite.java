/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.axiastudio.suite;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.db.IDatabase;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.suite.anagrafiche.entities.Indirizzo;
import com.axiastudio.suite.anagrafiche.entities.Soggetto;
import com.axiastudio.suite.anagrafiche.forms.FormIndirizzo;
import com.axiastudio.suite.anagrafiche.forms.FormSoggetto;
import com.axiastudio.suite.base.entities.Login;
import com.axiastudio.suite.base.entities.Ufficio;
import com.axiastudio.suite.base.entities.Utente;
import com.axiastudio.suite.pratiche.PraticaValidators;
import com.axiastudio.suite.pratiche.entities.Pratica;
import com.axiastudio.suite.protocollo.ProtocolloAdapters;
import com.axiastudio.suite.protocollo.ProtocolloPrivate;
import com.axiastudio.suite.protocollo.ProtocolloValidators;
import com.axiastudio.suite.protocollo.entities.PraticaProtocollo;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.SoggettoProtocollo;
import com.axiastudio.suite.protocollo.forms.FormProtocollo;
import com.axiastudio.suite.protocollo.forms.FormSoggettoProtocollo;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Suite {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*
        try {
            Connection conn = DriverManager.getConnection("jdbc:h2:~/suite","","");
            Statement st = conn.createStatement();
            st.executeUpdate("DROP SCHEMA IF EXISTS BASE;");
            st.executeUpdate("CREATE SCHEMA BASE;");
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Suite.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        

        
        String jdbcUrl = System.getProperty("jdbc.url");
        String jdbcUser = System.getProperty("jdbc.user");
        String jdbcPassword = System.getProperty("jdbc.password");
        String jdbcDriver = System.getProperty("jdbc.driver");
        Map properties = new HashMap();
        if( jdbcUrl != null ){
            properties.put("javax.persistence.jdbc.url", jdbcUrl);
        }
        if( jdbcUser != null ){
            properties.put("javax.persistence.jdbc.user", jdbcUser);
        }
        if( jdbcPassword != null ){
            properties.put("javax.persistence.jdbc.password", jdbcPassword);
        }
        if( jdbcDriver != null ){
            properties.put("javax.persistence.jdbc.driver", jdbcDriver);
        }

        Database db = new Database();
        if( properties.isEmpty() ){
            db.open("SuitePU");
        } else {
            properties.put("eclipselink.ddl-generation", "");
            db.open("SuitePU", properties);
        }
        Register.registerUtility(db, IDatabase.class);
        EntityManagerFactory emf = db.getEntityManagerFactory();
        
        
        
        // registro adapter, validatori, e privacy
        Register.registerAdapters(Resolver.adaptersFromClass(ProtocolloAdapters.class));
        Register.registerValidators(Resolver.validatorsFromClass(ProtocolloValidators.class));
        Register.registerValidators(Resolver.validatorsFromClass(PraticaValidators.class));
        Register.registerPrivates(Resolver.privatesFromClass(ProtocolloPrivate.class));
        
        // dati di base
        if( properties.isEmpty() ){
            DemoData.initSchema();
            DemoData.initData();
        }
        
        Application app = new Application(args);
        
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/ufficio.ui",
                              Ufficio.class,
                              Window.class,
                              "Uffici");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/base/forms/utente.ui",
                              Utente.class,
                              Window.class,
                              "Utenti");
        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/soggetto.ui",
                              Soggetto.class,
                              FormSoggetto.class,
                              "Soggetti anagrafici");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/anagrafiche/forms/indirizzo.ui",
                              Indirizzo.class,
                              FormIndirizzo.class,
                              "Indirizzo");

        
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/pratiche/forms/pratica.ui",
                              Pratica.class,
                              Window.class,
                              "Prtiche");
                          
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/soggettoprotocollo.ui",
                              SoggettoProtocollo.class,
                              FormSoggettoProtocollo.class,
                              "Soggetto del protocollo");
      
        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/praticaprotocollo.ui",
                              PraticaProtocollo.class,
                              Window.class,
                              "Pratica contenente il protocollo");

        Register.registerForm(db.getEntityManagerFactory(),
                              "classpath:com/axiastudio/suite/protocollo/forms/protocollo.ui",
                              Protocollo.class,
                              FormProtocollo.class, // custom form
                              "Protocolli");

        /* login */
        Login login = new Login();
        int res = login.exec();
        if( res == 1 ){
        
            Mdi mdi = new Mdi();
            //mdi.showMaximized();
            mdi.show();
            
            app.setCustomApplicationName("PyPaPi Suite");
            app.setCustomApplicationCredits("Copyright AXIA Studio 2012<br/>");
            app.exec();
        }
    
    }
    
}
