/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

import javax.swing.*;

import java.awt.Label;
import java.awt.event.*;
import java.io.*;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import java.io.IOException;
import java.io.File;


public class ModificarEmail {

    public static JFrame FRAME_MODIFICAR_EMAIL = new JFrame("Modificar e-mail");
    public static JTextField email_envio = new JTextField(15);
    public static int llamado = 0;
     
     
    static String mail = "";
    static Label nombreviv = new Label();
   
    private static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    
    static org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);

    public static void main() {
        llamado = 1;
        
       MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
        FRAME_MODIFICAR_EMAIL.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_MODIFICAR_EMAIL.setTitle(resourceMap.getString("FRAME_MODIFICAR_PLANO.title"));
        FRAME_MODIFICAR_EMAIL.setResizable(true);
        FRAME_MODIFICAR_EMAIL.setSize(900,600);
        FRAME_MODIFICAR_EMAIL.setLocation(120, 20);
        FRAME_MODIFICAR_EMAIL.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_MODIFICAR_EMAIL.setVisible(false);
                try {
                    if (ConfiguracionProyecto.llamado == 0) {
                        ConfiguracionProyecto.main();
                    } else {
                        ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(true);
                    }

                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });



        ModificarEmail.email_envio.setSize(150, 20);
        ModificarEmail.email_envio.setText("");
        ModificarEmail.email_envio.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {

                String aux = ((JTextField) e.getSource()).getText(); //lee texto escrito
                ModificarEmail.mail = aux;

            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });



        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();

        JMenu jMenuUsando = new javax.swing.JMenu();
        jMenuUsando.setText(resourceMap.getString("jMenuUsando.text") + NuevoProyecto.nombre_archivo);
        jMenuUsando.setEnabled(false);
        jMenuBar1.add(jMenuUsando);

        jMenuBar1.add(jMenuUsando);
        FRAME_MODIFICAR_EMAIL.setJMenuBar(jMenuBar1);

        //Solicitar email
        JLabel etiquetaModificarEmail = new JLabel();
        etiquetaModificarEmail.setText(resourceMap.getString("etiquetaModificarEmail.text"));


        JButton botonModificarEmail = new JButton(resourceMap.getString("botonModificarEmail.text"));
        botonModificarEmail.setSize(40, 20);
        botonModificarEmail.addActionListener(new  ActionListener (){
            public void actionPerformed(ActionEvent e) {

     
                try {
                    File f = new File(NuevoProyecto.archivo);

                    if (!f.exists()) {
                        if (ModificarEmail.mail.length() != 0) {
                            Document documento = DocumentHelper.createDocument();
                            org.dom4j.Element root = documento.addElement("vivienda");

                            
                            org.dom4j.Element email = root.addElement("email").addAttribute("direccion", ModificarEmail.mail);
                            FileWriter archivo = new FileWriter(NuevoProyecto.archivo, true);
                            OutputFormat format = OutputFormat.createPrettyPrint();

                            //format.setEncoding("UTF-8");
                            format.setEncoding("iso-8859-1");

                            XMLWriter writer = new XMLWriter(archivo, format);

                            writer.write(documento);
                            writer.close();

                            String mostrar = "Se ha introducido su email";
                            mostrar += "\n";
                            mostrar += ModificarEmail.mail;
                            mostrar += "\n";
                            mostrar += "en el fichero de configuración";
                            JOptionPane.showMessageDialog(null, mostrar, "Información", 1);

                            ModificarEmail.email_envio.setCaretPosition(0);
                            ModificarEmail.email_envio.setText("");

                            try {

                                File aFile = new File(NuevoProyecto.archivo);
                                SAXReader xmlReader = new SAXReader();
                                //xmlReader.setEncoding("UTF-8");
                                xmlReader.setEncoding("iso-8859-1");
                                Document doc = xmlReader.read(aFile);
                                Element node = (Element) doc.selectSingleNode("//vivienda/email");

                                ModificarEmail.nombreviv.setText(resourceMap.getString("etiquetaEmailActual.text") + ModificarEmail.mail);


                            } catch (DocumentException e1) {
                                e1.printStackTrace();
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.email.text"), 
                            resourceMap.getString("dialog.error.title"), 1); 
                        }
                    } else {
                        navegar();

                    }//end else

                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });



        JSeparator jSeparatorV = new javax.swing.JSeparator();
        JSeparator jSeparatorH = new javax.swing.JSeparator();
        
        jSeparatorV.setOrientation(javax.swing.SwingConstants.VERTICAL);
       
      
        
        Label etiquetaArbol = new java.awt.Label();
        etiquetaArbol.setText(resourceMap.getString("etiquetaArbol.text"));
        
        
        try {

            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda/email");

            nombreviv.setText(resourceMap.getString("etiquetaEmailActual.text") + node.valueOf("@direccion"));
            nombreviv.setSize(450, 30);

        } catch (DocumentException e) {
            e.printStackTrace();
        }



        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(ModificarEmail.FRAME_MODIFICAR_EMAIL.getContentPane());
        ModificarEmail.FRAME_MODIFICAR_EMAIL.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) // .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(5, 5, 5).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(32, 32, 32).addComponent(etiquetaModificarEmail).addGap(18, 18, 18).addComponent(email_envio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(botonModificarEmail)).addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addComponent(nombreviv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(120, 120, 120) //.addComponent(boton2)
                )).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, 
                javax.swing.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, 
                layout.createSequentialGroup() //                  .addContainerGap()
                .addGap(55, 55, 55).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, 
                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE)
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.DEFAULT_SIZE, 450, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(175, 175, 175).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup() //                		 .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nombreviv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(55, 55, 55)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(etiquetaModificarEmail).addComponent(email_envio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(botonModificarEmail)).addGap(42, 42, 42).addGap(475, 475, 475) //.addComponent(boton2)
                ));



        FRAME_MODIFICAR_EMAIL.setVisible(true);



    }
    
    private static  void navegar() {
        try {
            if (ModificarEmail.mail.length() != 0) {
                File aFile = new File(NuevoProyecto.archivo);
                SAXReader xmlReader = new SAXReader();

                //xmlReader.setEncoding("UTF-8");
                xmlReader.setEncoding("iso-8859-1");

                Document doc = xmlReader.read(aFile);

                Element node = (Element) doc.selectSingleNode("//vivienda/email");
                if (node == null) {
                    node = (Element) doc.selectSingleNode("//vivienda");//[@nombre]" );
                    org.dom4j.Element anadir = node.addElement("email");//( "plantas" );

                    node = (Element) doc.selectSingleNode("//vivienda/email");
                    anadir = node.addAttribute("direccion", ModificarEmail.mail);
                    NuevoProyecto.email_fichero = ModificarEmail.mail;
                }//end if
                else {
                    Attribute atributo = node.attribute("direccion");
                    atributo.setText(ModificarEmail.mail);

                }//end else
                String aux = doc.asXML();
                FileWriter archivo;

                archivo = new FileWriter(NuevoProyecto.archivo);
                OutputFormat format = OutputFormat.createPrettyPrint();

                //format.setEncoding("UTF-8");
                format.setEncoding("iso-8859-1");

                XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo));
                writer.write(doc);
                writer.close();

                String mostrar = "Se ha introducido su email";
                mostrar += "\n";
                mostrar += ModificarEmail.mail;
                mostrar += "\n";
                mostrar += "en el fichero de configuración";
                JOptionPane.showMessageDialog(null, mostrar, "Información", 1);


                try {

                    File aFile1 = new File(NuevoProyecto.archivo);
                    SAXReader xmlReader1 = new SAXReader();
                    //xmlReader1.setEncoding("UTF-8");
                    xmlReader1.setEncoding("iso-8859-1");
                    Document doc1 = xmlReader1.read(aFile1);
                    Element node1 = (Element) doc1.selectSingleNode("//vivienda/email");

                    ModificarEmail.nombreviv.setText("E-mail de contacto Actual:       " + ModificarEmail.mail);


                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
            } else {
                String mostrar2 = "Ha de introducir una dirección de email";
                JOptionPane.showMessageDialog(null, mostrar2, "Alerta", 1);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } //end navegar
     
}

