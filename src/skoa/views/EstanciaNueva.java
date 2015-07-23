/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import skoa.views.Arbol;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class EstanciaNueva {

    public static JFrame FRAME_ESTANCIA_NUEVA = new JFrame();
    public static JTextField NOMBRE_ESTANCIA = new JTextField(15);
    public static JTextField IMAGEN_ESTANCIA = new JTextField(15);
    public static JComboBox DESPLEGABLE_PLANTA = new javax.swing.JComboBox();
    
    public static String seleccionado = "";
    public static int llamado = 0;
    static String nombre_e = "";
    static String imagen_e = "";
    static JFileChooser panelSelecionarArchivo;
    
    public static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    
    static org.jdesktop.application.ResourceMap resourceMap = 
            org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);

    public static void main() {
        llamado = 1;
        
        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  

        FRAME_ESTANCIA_NUEVA.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_ESTANCIA_NUEVA.setTitle(resourceMap.getString("FRAME_ESTANCIA_NUEVA.title"));
        FRAME_ESTANCIA_NUEVA.setResizable(false);
        FRAME_ESTANCIA_NUEVA.setSize(800, 600);
        FRAME_ESTANCIA_NUEVA.setLocation(120, 20);
        FRAME_ESTANCIA_NUEVA.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_ESTANCIA_NUEVA.setVisible(false);//dispose();
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

        JPanel panelAgregarEstancia = new javax.swing.JPanel();
        panelAgregarEstancia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelAgregarEstancia.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("panelAgregarEstancia.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        JLabel etiquetaNombreEstancia = new JLabel();
        etiquetaNombreEstancia.setText(resourceMap.getString("etiquetaNombreEstancia.text"));

        NOMBRE_ESTANCIA.setText("");
        NOMBRE_ESTANCIA.setSize(250, 20);
        NOMBRE_ESTANCIA.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {
                String aux = ((JTextField) e.getSource()).getText(); //lee texto escrito
                EstanciaNueva.nombre_e = aux;

            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });

        JLabel etiquetaImagenEstancia = new JLabel();
        etiquetaImagenEstancia.setText(resourceMap.getString("etiquetaImagenEstancia.text"));

        IMAGEN_ESTANCIA.setText("");
        IMAGEN_ESTANCIA.setSize(250, 20);
        IMAGEN_ESTANCIA.setEnabled(false);
        IMAGEN_ESTANCIA.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {
                String aux = ((JTextField) e.getSource()).getText(); //lee texto escrito
                EstanciaNueva.imagen_e = aux;
            }//end focuslost

            public void focusGained(FocusEvent e) {
                // No hacemos nada
            }
        });

        JButton botonExamninar = new JButton(resourceMap.getString("botonExamninar.text"));
        botonExamninar.setSize(40, 20);
        botonExamninar.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        if (panelSelecionarArchivo == null) {
                            panelSelecionarArchivo = new JFileChooser();
                            String curDir = System.getProperty("user.dir");

                            panelSelecionarArchivo.setCurrentDirectory(new File(curDir));
                            panelSelecionarArchivo.addChoosableFileFilter(new ImageFilter());
                            panelSelecionarArchivo.setAcceptAllFileFilterUsed(false);
                            panelSelecionarArchivo.setAccessory(new ImagePreview(panelSelecionarArchivo));
                        } 
                        int returnVal = panelSelecionarArchivo.showDialog(panelSelecionarArchivo,resourceMap.getString("panelSelecionarArchivo.text"));
                        //Process the results.
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = panelSelecionarArchivo.getSelectedFile();
                            EstanciaNueva.IMAGEN_ESTANCIA.setText(file.getAbsolutePath());
                            EstanciaNueva.imagen_e = EstanciaNueva.IMAGEN_ESTANCIA.getText();
                        }
                        //Reset the file chooser for the next time it's shown.
                        panelSelecionarArchivo.setSelectedFile(null);

                    }
                });



        JButton botonAgregarEstancia = new JButton(resourceMap.getString("botonAgregarEstancia.text"));
        botonAgregarEstancia.setSize(40, 20);
        botonAgregarEstancia.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {

                        if (DESPLEGABLE_PLANTA.getSelectedIndex() > 0) {
                            if (!EstanciaNueva.nombre_e.equals("")) {
                                if (!EstanciaNueva.imagen_e.equals("")) {
                                    
                                    
                                    if (Acciones.estanciaUnica(EstanciaNueva.seleccionado, EstanciaNueva.nombre_e) == false) {
                                        navegar();
                                        
                                     
                                        String mostrar = String.format(resourceMap.getString("dialog.ok.estanciagregada.text"), NuevoProyecto.nombre_archivo);
                                        JOptionPane.showMessageDialog(null, mostrar, resourceMap.getString("dialog.title"), 1);
                                        
                                        
                                        EstanciaNueva.NOMBRE_ESTANCIA.setCaretPosition(0);
                                        EstanciaNueva.NOMBRE_ESTANCIA.setText("");

                                        EstanciaNueva.IMAGEN_ESTANCIA.setCaretPosition(0);
                                        EstanciaNueva.IMAGEN_ESTANCIA.setText("");

                                        // Reinciamos las variables para que no falle al seguir insertando estancias
                                        EstanciaNueva.nombre_e = "";
                                        EstanciaNueva.imagen_e = "";

                                         MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
                                        cargarDesplegablePlantas();
                                        
                                        
                                    }else {
                                        // Para cuando haya una estancia con el mismo nombre en la misma planta
                                        JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.existe.text"), 
                                        resourceMap.getString("dialog.error.title"), 1);
                                        
                                    }
                                }else {
                                    // Para cuando no se haya elegido una imagen para la estancia
                                    JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.imagen.text"), 
                                        resourceMap.getString("dialog.error.title"), 1);
                                }
                            } else {
                                // Para cuando no se introduzca ningún nombre en la estancia
                                JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.nombre.text"), 
                                        resourceMap.getString("dialog.error.title"), 1);  
                            }
                        }  else {
                            // Para cuando no se haya seleccionado ninguna planta
                            JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciagregada.planta.text"), 
                            resourceMap.getString("dialog.error.title"), 1);  
                          
                        }


                    }
                });


        JLabel etiquetaSelecionePlanta = new JLabel();
        etiquetaSelecionePlanta.setText(resourceMap.getString("etiquetaSelecionePlanta.text"));


        cargarDesplegablePlantas();
        DESPLEGABLE_PLANTA.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        EstanciaNueva.seleccionado = (String) EstanciaNueva.DESPLEGABLE_PLANTA.getSelectedItem();
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
        FRAME_ESTANCIA_NUEVA.setJMenuBar(jMenuBar1);





        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panelAgregarEstancia);
        panelAgregarEstancia.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(etiquetaSelecionePlanta).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(etiquetaNombreEstancia).addGap(18, 18, 18).addComponent(NOMBRE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(etiquetaImagenEstancia).addGap(18, 18, 18).addComponent(IMAGEN_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10, 10, 10).addComponent(botonExamninar)).addGroup(jPanel1Layout.createSequentialGroup().addGap(61, 61, 61).addComponent(botonAgregarEstancia)).addGroup(jPanel1Layout.createSequentialGroup().addGap(61, 61, 61) // .addComponent(boton3)
                )).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));


        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaSelecionePlanta)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(etiquetaNombreEstancia).addComponent(NOMBRE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(etiquetaImagenEstancia).addComponent(IMAGEN_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(botonExamninar)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(botonAgregarEstancia).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE) // .addComponent(boton3)
                .addContainerGap()));

        Arbol.generarArbol();
      
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_ESTANCIA_NUEVA.getContentPane());
        FRAME_ESTANCIA_NUEVA.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) //.addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(5, 5, 5).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelAgregarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(259, Short.MAX_VALUE)));


        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(35, 35, 35).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(17, 17, 17)
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 450, Short.MAX_VALUE).addGap(275, 275, 275).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(116, 116, 116).addComponent(panelAgregarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(115, Short.MAX_VALUE)));

        //********************************************************************************

        EstanciaNueva.FRAME_ESTANCIA_NUEVA.setVisible(true);


    }
    
    public static void cargarDesplegablePlantas(){
        DESPLEGABLE_PLANTA.removeAllItems();
        DESPLEGABLE_PLANTA.addItem(resourceMap.getString("DESPLEGABLE_PLANTA.firstItem.txt"));
        for(String planta : Arbol.getPlantas()){
             DESPLEGABLE_PLANTA.addItem(planta);
        }
    }

    
    
    private static void navegar() {
        try {
            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");


            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                if (!node.getName().equals("email")) {


                    if (node.valueOf("@alias").equals(EstanciaNueva.seleccionado)) {
                        //ESTEFANÍA: Añadido para que se coja la imagen del plano desde la carpeta
                        //donde está el ejecutable. Esto se hace para que no hayan rutas absolutas
                        //y si se cambia la carpeta de ejecutables de directorio, siga funcionando.
                        //SIEMPRE Y CUANDO, SE MANTENGA EL MISMO NOMBRE DE LA CARPETA DONDE SE SACARON
                        //LAS IMÁGENES DE LOS PLANOS AL CREAR EL PROYECTO, Y EL MISMO NOMBRE DE LA
                        //IMAGEN.
                        File dir_iniciall = new File("./");
                        String a = dir_iniciall.getAbsolutePath();
                        System.out.println("raiz=" + a);
                        int ind = EstanciaNueva.imagen_e.indexOf(a);
                        int lon = a.length();
                        String b = EstanciaNueva.imagen_e.substring(ind + lon);
                        System.out.println("ruta relativa=" + b);
                        System.out.println("ruta absoluta=" + EstanciaNueva.imagen_e);
                        org.dom4j.Element anadir = node.addElement("estancia").addAttribute("nombre", EstanciaNueva.nombre_e) //.addAttribute("imagen", estancia_nueva.imagen_e)
                                .addAttribute("imagen", b);
                        break;
                    }//end if auxi
                }//end if
            }//end for


            OutputFormat format = OutputFormat.createPrettyPrint();

            //format.setEncoding("UTF-8"); 
            format.setEncoding("iso-8859-1");

            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();
            
            Acciones.inicializarEstancia(EstanciaNueva.seleccionado, EstanciaNueva.nombre_e);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }
}