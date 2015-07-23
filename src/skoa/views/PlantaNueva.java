
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.views;

import java.awt.Dimension;
import skoa.helpers.Acciones;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class PlantaNueva {

    static JFrame FRAME_PLANTA_NUEVA = new JFrame();
    static String nueva = "";
    static JTextField nombre = new JTextField(15);
    public static int llamado = 0;
    public static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    
    static org.jdesktop.application.ResourceMap resourceMap 
          = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class)
                .getContext().getResourceMap(ConfiguracionProyecto.class);

    
    public static void main() {
        llamado = 1;
        
        
        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
       
        // Para que no se cierre la aplicación entera, sólo el módulo "Nuevo Proyecto"
        FRAME_PLANTA_NUEVA.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_PLANTA_NUEVA.setTitle(resourceMap.getString("FRAME_PLANTA_NUEVA.title"));
        
        FRAME_PLANTA_NUEVA.setResizable(true);
        FRAME_PLANTA_NUEVA.setSize(700, 600);
        FRAME_PLANTA_NUEVA.setPreferredSize(new Dimension(700,600));
        FRAME_PLANTA_NUEVA.setLocation(120, 20);
        
        FRAME_PLANTA_NUEVA.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                PlantaNueva.FRAME_PLANTA_NUEVA.setVisible(false);//dispose();
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
       
        //Recoger nombre planta
        nombre.setSize(150, 20);
        nombre.setText("");
        nombre.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {
                
                String aux = ((JTextField) e.getSource()).getText(); //lee texto escrito
                PlantaNueva.nueva = aux;

            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });

        JLabel etiquetaAgregar = new JLabel();
        etiquetaAgregar.setText(resourceMap.getString("etiquetaAgregar.text"));


        JButton botonAgregar = new JButton(resourceMap.getString("botonAgregar.text"));
        botonAgregar.setSize(40, 20);
        botonAgregar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            if (!PlantaNueva.nueva.equals("")) {
                    if (Acciones.plantaUnica(PlantaNueva.nueva) == false) {

                        navegar();
                        String mostrar = String.format(resourceMap.getString("dialog.ok.text"), PlantaNueva.nueva);
                        JOptionPane.showMessageDialog(null, mostrar, resourceMap.getString("dialog.title"), 1);

                        PlantaNueva.nombre.setCaretPosition(0);
                        PlantaNueva.nombre.setText("");
                        PlantaNueva.nueva = "";


                         MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());

                    } else {

                        JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.existe.text"), 
                                resourceMap.getString("dialog.error.title"), 1);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.vacio.text"), 
                                resourceMap.getString("dialog.error.title"), 1);
                }
            }


        });


        //Comienza el menu principal
        JSeparator jSeparatorH = new javax.swing.JSeparator();

        JSeparator jSeparatorV = new javax.swing.JSeparator();
        jSeparatorV.setOrientation(javax.swing.SwingConstants.VERTICAL);


        Label etiquetaArbol = new java.awt.Label();
        etiquetaArbol.setText(resourceMap.getString("etiquetaArbol.text"));


        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
        JMenu jMenuUsando = new javax.swing.JMenu();
        jMenuUsando.setText(resourceMap.getString("jMenuUsando.text") + NuevoProyecto.nombre_archivo);
        jMenuUsando.setEnabled(false);
        jMenuBar1.add(jMenuUsando);

        FRAME_PLANTA_NUEVA.setJMenuBar(jMenuBar1);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_PLANTA_NUEVA.getContentPane());
        FRAME_PLANTA_NUEVA.getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(etiquetaArbol, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 274,
                javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup()
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 262,
                javax.swing.GroupLayout.PREFERRED_SIZE).addGap(24, 24, 24))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(etiquetaAgregar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(nombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)).addComponent(botonAgregar)).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addGap(62, 62, 62).addComponent(etiquetaAgregar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(botonAgregar))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    
        FRAME_PLANTA_NUEVA.setVisible(true);

    }//end main

   
    private static void navegar() {
        try {
            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();

            xmlReader.setEncoding("UTF-8");
            //xmlReader.setEncoding("iso-8859-1");

            Document doc = xmlReader.read(aFile);

            Element node = (Element) doc.selectSingleNode("//vivienda/planta");

            if (node == null) {
                node = (Element) doc.selectSingleNode("//vivienda");//[@nombre]" );
                org.dom4j.Element anadir = node.addElement("planta");//( "plantas" );

                node = (Element) doc.selectSingleNode("//vivienda/planta");
                anadir = node.addAttribute("alias", PlantaNueva.nueva);// planta_nueva.nueva );
            }//end if
            else {
                node = (Element) doc.selectSingleNode("//vivienda");
                org.dom4j.Element anadir2 = node.addElement("planta").addAttribute("alias", PlantaNueva.nueva);

            }//end else
 
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("iso-8859-1");

            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    } //end navegar
}//end class

