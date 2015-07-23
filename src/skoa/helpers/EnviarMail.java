/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.helpers;

import javax.mail.*;
import javax.mail.internet.*;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Properties;
import skoa.views.NuevoProyecto;


/**
 * Clase para enviar email
 * @author
 */
public class EnviarMail {

    public static void main() {
        System.out.println("entrado enviar_mail.main()");
        SendAuthentication.Send();

    }
}


class SendAuthentication {

    
    
    public static void Send() //
    {

        String host = "smtp.gmail.com";//127.0.0.1";//Suponiendo que el servidor SMTPsea la propia m�quina
        String from = "alarmas.vivienda@gmail.com";
        String to = "";
//        String to = "";// = "direccion destino email";  //COGER DEL XML


        try { //COGIENDO DIRECCION DE ENVIO DE EMAIL DEL XML

//			File aFile = new File(principal.archivo); //cargar ARCHIVO XML----------------
            System.out.println("fichero: " + NuevoProyecto.archivo);
            File aFile = new File(NuevoProyecto.archivo);//"C:\\Users\\Inma\\aplicacion skoa\\NuevaPrueba\\Maipez\\ChaletFamiliar.xml");
            SAXReader xmlReader = new SAXReader();
//			xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda/email");

            to = node.valueOf("@direccion");

            System.out.println("enviar correo a: " + to);
        } catch (DocumentException e) {
            e.printStackTrace();
        }



        System.out.println("Prueba para enviar un mail..." + new java.util.Date());

        Properties prop = new Properties();

        // Puerto de gmail para envio de correos
        prop.setProperty("mail.smtp.port", "587");

        prop.put("mail.smtp.host", host);

        /*Esta l�nea es la que indica al API que debe autenticarse*/
        prop.put("mail.smtp.auth", "true");

        /*A�adir esta linea si queremos ver una salida detallada del programa*/
        // prop.put("mail.debug", "true");

        prop.put("mail.smtp.starttls.enable", "true");


        try {

            SMTPAuthentication auth = new SMTPAuthentication();
            Session session = Session.getInstance(prop, auth);
            Message msg = getMessage(session, from, to);
            System.out.println("Enviando ...");

            msg.setSubject("Alerta en su vivienda");

            Transport.send(msg);

            System.out.println("Mensaje enviado!");

        } catch (Exception e) {

            ExceptionManager.ManageException(e);

        }

    }

    private static MimeMessage getMessage(Session session, String from, String to) {

        try {

            MimeMessage msg = new MimeMessage(session);

            //MENSAJE DE LA ALERTA QUE SE ENVIARA EN EL EMAIL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            msg.setText("Se ha producido una Alerta por INUNDACIÓN en su vivienda");


            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setFrom(new InternetAddress(from, "Sistema de Alertas"));
            return msg;

        } catch (java.io.UnsupportedEncodingException ex) {

            ExceptionManager.ManageException(ex);
            return null;

        } catch (MessagingException ex) {

            ExceptionManager.ManageException(ex);
            return null;

        }

    }
}

class SMTPAuthentication extends javax.mail.Authenticator {

    public PasswordAuthentication getPasswordAuthentication() {

        String username = "alarmas.vivienda@gmail.com";

        String password = "alarmas123";

        return new PasswordAuthentication(username, password);

    }
}

class ExceptionManager {

    public static void ManageException(Exception e) {

        System.out.println("Se ha producido una exception");

        System.out.println(e.getMessage());

        e.printStackTrace(System.out);

    }
}
