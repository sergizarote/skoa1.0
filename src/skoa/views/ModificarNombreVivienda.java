
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

import javax.swing.JButton;

import javax.swing.JPanel;

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

public class ModificarNombreVivienda extends JPanel {

    public static JFrame FRAME_MODIFICAR_VIVIENDA = new JFrame();
    static String nombre = "";
    public static JTextField nombre_vivienda = new JTextField(15);
    static String imagen_viv = "";
    static Label nombreviv = new Label();
    public static int llamado = 0;
    private static final long serialVersionUID = 1L;
    static org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);
   private static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    public static void main() {
        llamado = 1;
        
        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
        
        FRAME_MODIFICAR_VIVIENDA.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_MODIFICAR_VIVIENDA.setTitle(resourceMap.getString("FRAME_MODIFICAR_VIVIENDA.title"));
        FRAME_MODIFICAR_VIVIENDA.setResizable(true);
        FRAME_MODIFICAR_VIVIENDA.setSize(900, 600);
        FRAME_MODIFICAR_VIVIENDA.setLocation(120, 20);
        FRAME_MODIFICAR_VIVIENDA.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_MODIFICAR_VIVIENDA.setVisible(false);
                try {
                    if (ConfiguracionProyecto.llamado == 0) {
                        ConfiguracionProyecto.main();
                    } else {
                        ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(true);
                        //Arbol.generarArbol();
                        //ConfiguracionProyecto.ARBOL_CONFIGURACION = Arbol.MOSTRAR_CONFIGURACION;
                    }

                } catch (DocumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });


        //Solicitar email
        JLabel etiquetaModificarVivienda = new JLabel();
        etiquetaModificarVivienda.setText(resourceMap.getString("etiquetaModificarVivienda.text"));


        //Recoger email
        ModificarNombreVivienda.nombre_vivienda.setColumns(15);
        ModificarNombreVivienda.nombre_vivienda.setCaretPosition(0);
        ModificarNombreVivienda.nombre_vivienda.setText("");
        ModificarNombreVivienda.nombre_vivienda.setSelectionEnd(-1);
        ModificarNombreVivienda.nombre_vivienda.setSelectionStart(-1);
        


        nombre_vivienda.setText("");
        nombre_vivienda.setSize(250, 20);

        nombre_vivienda.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {

                String aux = ((JTextField) e.getSource()).getText(); //lee texto escrito
                ModificarNombreVivienda.nombre = aux;

            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });


        JButton botonModidicarVivienda = new JButton(resourceMap.getString("botonModidicarVivienda.text"));
        botonModidicarVivienda.setSize(40, 20);
        botonModidicarVivienda.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File f = new File(NuevoProyecto.archivo);

                    if (!f.exists()) {
                        if (!ModificarNombreVivienda.nombre.equals("")) {
                            if (!ModificarNombreVivienda.imagen_viv.equals("")) {
                                Document documento = DocumentHelper.createDocument();
                                org.dom4j.Element root = documento.addElement("vivienda");
                                root.addAttribute("name", ModificarNombreVivienda.nombre);
                                root.addAttribute("logo", ModificarNombreVivienda.imagen_viv);
                                root.addAttribute("contador", "1");

                                FileWriter archivo = new FileWriter(NuevoProyecto.archivo, true);
                                OutputFormat format = OutputFormat.createPrettyPrint();

                                //format.setEncoding("UTF-8");
                                format.setEncoding("iso-8859-1");

                                XMLWriter writer = new XMLWriter(archivo, format);

                                writer.write(documento);
                                writer.close();
                                String mostrar = "Se ha modificado el nombre e imagen de la vivienda";
                                mostrar += "\n";
                                mostrar += "en el fichero de configuración";
                                JOptionPane.showMessageDialog(null, mostrar, "Información", 1);
       
                                try {

                                    File aFile = new File(NuevoProyecto.archivo);
                                    SAXReader xmlReader = new SAXReader();
                                    //xmlReader.setEncoding("UTF-8");
                                    xmlReader.setEncoding("iso-8859-1");
                                    Document doc = xmlReader.read(aFile);
                                    Element node = (Element) doc.selectSingleNode("//vivienda/email");

                                    ModificarNombreVivienda.nombreviv.setText("Nombre Actual de la vivienda:       " + ModificarNombreVivienda.nombre);

                                    MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
                                    
                                    ModificarNombreVivienda.nombre_vivienda.setColumns(0);
                                    ModificarNombreVivienda.nombre_vivienda.setCaretPosition(0);
                                    ModificarNombreVivienda.nombre_vivienda.setText("");
                                    ModificarNombreVivienda.nombre_vivienda.setSelectionEnd(-1);
                                    ModificarNombreVivienda.nombre_vivienda.setSelectionStart(-1);
                                    
                                } catch (DocumentException e1) {
                                    e1.printStackTrace();
                                }

                            } else {
                                String mostrar = "Se esta creando el fichero.";
                                mostrar += "\n";
                                mostrar += "Debe selecciona una imagen para la vivienda";
                                JOptionPane.showMessageDialog(null, mostrar, "Alerta", 1);
                            }
                        } else {
                            String mostrar = "Debe introducir un nombre para la vivienda";
                            JOptionPane.showMessageDialog(null, mostrar, "Alerta", 1);
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
        jSeparatorV.setOrientation(javax.swing.SwingConstants.VERTICAL);

        JSeparator jSeparatorH = new javax.swing.JSeparator();

        Label etiquetaArbol = new java.awt.Label();
        etiquetaArbol.setText(resourceMap.getString("etiquetaArbol.text"));

        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();

        JMenu jMenuUsando = new javax.swing.JMenu();
        jMenuUsando.setText(resourceMap.getString("jMenuUsando.text") + NuevoProyecto.nombre_archivo);
        jMenuUsando.setEnabled(false);
        jMenuBar1.add(jMenuUsando);

        jMenuBar1.add(jMenuUsando);
        FRAME_MODIFICAR_VIVIENDA.setJMenuBar(jMenuBar1);


        try {

            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            nombreviv.setText(resourceMap.getString("etiquetaViviendaActual.text") + node.valueOf("@name"));
            nombreviv.setSize(150, 30);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(ModificarNombreVivienda.FRAME_MODIFICAR_VIVIENDA.getContentPane());
        ModificarNombreVivienda.FRAME_MODIFICAR_VIVIENDA.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) // .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(5, 5, 5).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup() //                                		.addComponent(mensaje2)
                .addGap(400, 400, 400).addComponent(botonModidicarVivienda)).addGroup(layout.createSequentialGroup().addComponent(etiquetaModificarVivienda).addGap(18, 18, 18).addComponent(nombre_vivienda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(50, 50, 50).addComponent(nombreviv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)) //                                .addGroup(layout.createSequentialGroup()
                )))).addGroup(layout.createSequentialGroup().addContainerGap(145, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, 
                javax.swing.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, 
                layout.createSequentialGroup() //                  .addContainerGap()
                .addGap(55, 55, 55).addComponent(etiquetaArbol, javax.swing.GroupLayout
                .PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE)
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.DEFAULT_SIZE, 450, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(175, 175, 175).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(100, 100, 100).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nombreviv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(55, 55, 55)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(etiquetaModificarVivienda).addComponent(nombre_vivienda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(botonModidicarVivienda)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING) //                        .addComponent(imagen_vivienda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                ).addContainerGap().addGap(50, 50, 50).addContainerGap()));


        FRAME_MODIFICAR_VIVIENDA.setVisible(true);

    }

    private static void navegar() {

        try {

            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");
            Document doc = xmlReader.read(aFile);

            if (!ModificarNombreVivienda.nombre.equals("")) {
                Element node = (Element) doc.selectSingleNode("//vivienda");//[@nombre]" );
                Attribute atributo = node.attribute("name");
                atributo.setText(ModificarNombreVivienda.nombre);

                if (!ModificarNombreVivienda.imagen_viv.equals("")) {
                    Attribute atributo2 = node.attribute("logo");
                    atributo2.setText(ModificarNombreVivienda.imagen_viv);

                    String mostrar = "Se ha modificado el nombre e imagen de la vivienda";
                    mostrar += "\n";
                    mostrar += "en el fichero de configuración";
                    JOptionPane.showMessageDialog(null, mostrar, "Información", 1);
                    Arbol.generarArbol();
                } else {
                    String mostrar = "Se ha modificado el nombre de la vivienda";
                    mostrar += "\n";
                    mostrar += "en el fichero de configuración";
                    JOptionPane.showMessageDialog(null, mostrar, "Información", 1);
                    Arbol.generarArbol();
                }

                try {

                    File aFile1 = new File(NuevoProyecto.archivo);
                    SAXReader xmlReader1 = new SAXReader();
                    //xmlReader1.setEncoding("UTF-8");
                    xmlReader1.setEncoding("iso-8859-1");
                    Document doc1 = xmlReader1.read(aFile1);
                    Element node1 = (Element) doc1.selectSingleNode("//vivienda/email");

                    ModificarNombreVivienda.nombreviv.setText(resourceMap.getString("etiquetaViviendaActual.text") + ModificarNombreVivienda.nombre);


                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }

            } else {
                String mostrar = "Ha de introducir un nombre para la vivienda";
                JOptionPane.showMessageDialog(null, mostrar, "Alerta", 1);
            }

            String aux = doc.asXML();
            FileWriter archivo;

            archivo = new FileWriter(NuevoProyecto.archivo);
            OutputFormat format = OutputFormat.createPrettyPrint();

            //format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo));
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    } //end navegar
}
