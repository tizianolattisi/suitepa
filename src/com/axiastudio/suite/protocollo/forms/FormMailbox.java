package com.axiastudio.suite.protocollo.forms;

import com.axiastudio.suite.email.EMail;
import com.axiastudio.suite.email.EmailHelper;
import com.axiastudio.suite.interoperabilita.entities.Segnatura;
import com.axiastudio.suite.interoperabilita.utilities.JAXBHelper;
import com.axiastudio.suite.protocollo.entities.Mailbox;
import com.axiastudio.suite.protocollo.entities.Protocollo;
import com.axiastudio.suite.protocollo.entities.TipoProtocollo;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;

import javax.mail.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tiziano
 * Date: 11/09/13
 * Time: 10:19
 * To change this template use File | Settings | File Templates.
 */
public class FormMailbox extends QDialog {

    private QTableWidget tableWidget;
    private Mailbox mailbox;

    public FormMailbox(QWidget parent, Mailbox mailbox){
        super(parent);
        this.mailbox = mailbox;
        initLayout();
        refreshMailbox();
    }

    private void initLayout(){
        tableWidget = new QTableWidget();
        tableWidget.verticalHeader().hide();
        tableWidget.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        tableWidget.horizontalHeader().setResizeMode(QHeaderView.ResizeMode.Interactive);
        QVBoxLayout layout = new QVBoxLayout();
        QHBoxLayout buttonLayout = new QHBoxLayout();
        QPushButton refreshButton = new QPushButton("Aggiorna");
        refreshButton.clicked.connect(this, "refreshMailbox()");
        buttonLayout.addWidget(refreshButton);
        QPushButton protocollaButton = new QPushButton("Protocolla");
        protocollaButton.clicked.connect(this, "protocolla()");
        buttonLayout.addWidget(protocollaButton);
        buttonLayout.addSpacerItem(new QSpacerItem(10, 10, QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Minimum));
        layout.addLayout(buttonLayout);
        layout.addWidget(this.tableWidget);
        this.setLayout(layout);
    }

    private void refreshMailbox(){
        while( tableWidget.rowCount() > 0 ){
            tableWidget.removeRow(0);
        }
        tableWidget.setColumnCount(3);
        List<String> labels = new ArrayList();
        labels.add("Data");
        labels.add("Mittente");
        labels.add("Oggetto");
        tableWidget.setHorizontalHeaderLabels(labels);
        EmailHelper helper = new EmailHelper(this.mailbox);
        helper.open();
        Folder folder = helper.getFolder();
        Integer i = 0;
        try {
            int messageCount = folder.getMessageCount();

            Message[] messages = folder.getMessages(messageCount-8, messageCount);
            for( Message msg: messages ){
                tableWidget.insertRow(i);
                QTableWidgetItem itemDate = new QTableWidgetItem(msg.getReceivedDate().toString());
                itemDate.setData(Qt.ItemDataRole.UserRole, msg.getMessageNumber());
                itemDate.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
                tableWidget.setItem(i, 0, itemDate);
                QTableWidgetItem itemSender = new QTableWidgetItem(msg.getFrom()[0].toString());
                itemSender.setData(Qt.ItemDataRole.UserRole, mailbox);
                itemSender.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
                tableWidget.setItem(i, 1, itemSender);
                QTableWidgetItem itemSubject = new QTableWidgetItem(msg.getSubject());
                itemSubject.setData(Qt.ItemDataRole.UserRole, mailbox);
                itemSubject.setFlags(Qt.ItemFlag.ItemIsSelectable, Qt.ItemFlag.ItemIsEnabled);
                tableWidget.setItem(i, 2, itemSubject);
                i++;
            }
        } catch (MessagingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            helper.close();
        }
    }

    private void protocolla() {
        QTableWidgetItem item = tableWidget.item(tableWidget.currentRow(), 0);
        Integer number = (Integer) item.data(Qt.ItemDataRole.UserRole);
        EmailHelper helper = new EmailHelper(this.mailbox);

        helper.open();
        EMail email = helper.getEmail(number);

        System.out.println(email.getBody());
        InputStream stream = email.getStream("Segnatura.xml");
        BufferedReader reader = new BufferedReader(new InputStreamReader( stream ));
        /*
        String line = "";
        String xml = "";
        try {
            xml = IOUtils.getStringFromReader(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //FileInputStream fileInputStream = new FileInputStream(filePath);
        StringBuilder builder = new StringBuilder();
        int ch;
        try {
            while((ch = stream.read()) != -1){
                builder.append((char)ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String xml = builder.toString();

        helper.close();

        Segnatura segnatura = JAXBHelper.leggiSegnatura(xml);

        System.out.println(segnatura.getIntestazione().getOggetto());


        /*
        SegnaturaOld segnatura = new SegnaturaOld(xml);

        String pec = segnatura.getIndirizzoTelematico();
        List<Soggetto> soggetti = AnagraficaUtil.trovaSoggettoDaEmail(pec);

        if( soggetti.size() == 0 ){
            String msg = "Non è stato trovato in anagrafica un soggetto con email \"" + pec + "\"";
            Util.warningBox(this, "Attenzione", msg);
            // TODO: proponiamo di crearlo al volo o di aggiungere l'email a un soggetto esistente?
            return;
        }

        if( soggetti.size() > 1 ){
            String msg = "Più soggetti sono registrati con email \"" + pec + "\".";
            // TODO: li mostriamo e invitiamo a sceglierne uno o a sanare la situazione?
            Util.warningBox(this, "Attenzione", msg);
            return;
        }
        */

        // creo il nuovo protocollo
        Protocollo protocollo = new Protocollo();
        //protocollo.setOggetto(segnatura.getOggetto());
        protocollo.setTipo(TipoProtocollo.ENTRATA);

    }

}