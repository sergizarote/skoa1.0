/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

import skoa.helpers.Acciones;
import skoa.helpers.ImageFilter;
import skoa.helpers.ImagePreview;
import java.awt.Label;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import javax.swing.JSeparator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ModificarPlano {

    public static JFrame FRAME_MODIFICAR_PLANO = new JFrame();
    public static String direcPlano;
    static String planta;
    static String estancia;
    public static JComboBox DESPLEGABLE_PLANTA = new javax.swing.JComboBox();
    public static JComboBox DESPLEGABLE_ESTANCIA = new javax.swing.JComboBox();
    
    public static String planta_seleccionada;
    public static String estancia_seleccionada;
    static JTextField direccionPlano = new JTextField(15);
    public static int llamado = 0;
    private static JFileChooser fc;
    
    
   static org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);

   
   private static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    public static void main() throws DocumentException {
        llamado = 1;

        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
         
        FRAME_MODIFICAR_PLANO.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_MODIFICAR_PLANO.setTitle(resourceMap.getString("FRAME_MODIFICAR_PLANO.title"));
        FRAME_MODIFICAR_PLANO.setResizable(true);
        FRAME_MODIFICAR_PLANO.setSize(900,600);
        FRAME_MODIFICAR_PLANO.setLocation(120, 20);
        FRAME_MODIFICAR_PLANO.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_MODIFICAR_PLANO.setVisible(false);
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



     
        JPanel panelModificarPlano = new javax.swing.JPanel();
        panelModificarPlano.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelModificarPlano.setBorder(javax.swing.BorderFactory.createTitledBorder(null, 
                resourceMap.getString("panelModificarPlano.title") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        JLabel etiquetaNuevoPlano = new JLabel();
        etiquetaNuevoPlano.setText(resourceMap.getString("etiquetaNuevoPlano.text"));

        direccionPlano.setEnabled(false);
        direccionPlano.setSize(250, 20);
        direccionPlano.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {
                String aux = ((JTextField) e.getSource()).getText();
                ModificarPlano.direcPlano = aux;
            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });


        
        cargarDesplegablePlantas();
        DESPLEGABLE_PLANTA.addActionListener( new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                ModificarPlano.planta_seleccionada = (String) ModificarPlano.DESPLEGABLE_PLANTA.getSelectedItem();
                if (DESPLEGABLE_PLANTA.getSelectedIndex() > 0 ) {
                    ModificarPlano.DESPLEGABLE_ESTANCIA.setEnabled(true);
                    cargarEstancias();
                }
                if (DESPLEGABLE_PLANTA.getSelectedIndex() == 0) {
                    ModificarPlano.DESPLEGABLE_ESTANCIA.setEnabled(false);
                    DESPLEGABLE_ESTANCIA.setSelectedIndex(0);
                }

            }
        });

        DESPLEGABLE_ESTANCIA.setEnabled(false);
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        DESPLEGABLE_ESTANCIA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ModificarPlano.estancia_seleccionada = (String) ModificarPlano.DESPLEGABLE_ESTANCIA.getSelectedItem();

            }
        });

        
       
        JButton botonExaminar = new JButton(resourceMap.getString("botonExamninar.text"));
        botonExaminar.setSize(40, 20);
        botonExaminar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                //Set up the file chooser.
                if (fc == null) {
                    fc = new JFileChooser();

                    String curDir = System.getProperty("user.dir");
                    //curDir = curDir+"\\imagenes\\";
                    fc.setCurrentDirectory(new File(curDir));
                    fc.addChoosableFileFilter(new ImageFilter());
                    fc.setAcceptAllFileFilterUsed(false);


                    //Add the preview pane.
                    fc.setAccessory(new ImagePreview(fc));
                }

                //Show it.
                int returnVal = fc.showDialog(new JPanel(),resourceMap.getString("panelSelecionarArchivo.text"));

                //Process the results.
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    ModificarPlano.direccionPlano.setText(file.getAbsolutePath());
                    ModificarPlano.direcPlano = ModificarPlano.direccionPlano.getText();
                   
                } else {
                   
                }
                fc.setSelectedFile(null);
            }
            
        });
        
        
        
       
        
        JButton botonModificarPlano = new JButton(resourceMap.getString("botonModificarPlano.text"));
        botonModificarPlano.setSize(40, 20);
        botonModificarPlano.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ModificarPlano.DESPLEGABLE_PLANTA.getSelectedIndex() > 0) {
                    if (ModificarPlano.DESPLEGABLE_ESTANCIA.getSelectedIndex() > 0) {
                        
                        if (ModificarPlano.direcPlano != null) {

                            String mostrar = String.format(resourceMap.getString("dialog.ok.estanciamodificada.text"), NuevoProyecto.nombre_archivo);
                            JOptionPane.showMessageDialog(null, mostrar, resourceMap.getString("dialog.title"), 1);
                            
                            ModificarPlano.DESPLEGABLE_ESTANCIA.setEnabled(false);
                            Acciones.modificarPlanoXML();
                            direccionPlano.setText("");
                            DESPLEGABLE_PLANTA.setSelectedIndex(0);
                        }else {
                           // Para cuando no se haya elegido una imagen para la estancia
                            JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.imagen.text"), 
                                resourceMap.getString("dialog.error.title"), 1);
 
                        }
                    } else {
                        // Para cuando no se introduzca ningún nombre en la estancia
                        JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.selecionarestancia.text"), 
                                resourceMap.getString("dialog.error.title"), 1); 
                    }
                } else {
                     // Para cuando no se haya seleccionado ninguna planta
                    JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.planta.text"), 
                    resourceMap.getString("dialog.error.title"), 1);  
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
        FRAME_MODIFICAR_PLANO.setJMenuBar(jMenuBar1);
        
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panelModificarPlano);
        panelModificarPlano.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(29, 29, 29)
                .addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(direccionPlano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonExaminar).addGap(52, 52, 52)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(265, Short.MAX_VALUE)
                .addComponent(etiquetaNuevoPlano).addGap(116, 116, 116)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(332, Short.MAX_VALUE)
                .addComponent(botonModificarPlano).addContainerGap()));
        
        
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE)
                .addComponent(etiquetaNuevoPlano).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(direccionPlano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(botonExaminar)).addGap(19, 19, 19)
                .addComponent(botonModificarPlano)));





        JPanel panel3 = new javax.swing.JPanel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(panel3);
        panel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(84, 84, 84) //    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                ).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(panelModificarPlano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(24, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGap(45, 45, 45).addComponent(panelModificarPlano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE) // .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)));
        

       

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_MODIFICAR_PLANO.getContentPane());
        FRAME_MODIFICAR_PLANO.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                .addGroup(layout.createSequentialGroup()
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) 
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15)
                .addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10)
                .addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                
                .addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) .addContainerGap(259, Short.MAX_VALUE)
                
        ));
     
        
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                
                .addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 
                348, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(35, 35, 35)
                .addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, 
                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                 
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(17, 17, 17) //                  
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 450, Short.MAX_VALUE).addGap(275, 275, 275).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(39, 39, 39)
                .addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(300, Short.MAX_VALUE)));

        
        //FRAME_MODIFICAR_PLANO.pack();
        FRAME_MODIFICAR_PLANO.setVisible(true);

    }
    
    
    public static void cargarDesplegablePlantas(){
        DESPLEGABLE_PLANTA.removeAllItems();
        DESPLEGABLE_PLANTA.addItem(resourceMap.getString("DESPLEGABLE_PLANTA.firstItem.txt"));
        for(String p : Arbol.getPlantas()){
             DESPLEGABLE_PLANTA.addItem(p);
        }
    }
     
    private static void cargarEstancias(){
         //remueve los items anteriores
        DESPLEGABLE_ESTANCIA.removeAllItems();
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        
         for(String e : Arbol.getEstancias(ModificarPlano.planta_seleccionada)){
             DESPLEGABLE_ESTANCIA.addItem(e);
        }

    }
    
 

}