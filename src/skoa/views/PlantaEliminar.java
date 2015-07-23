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
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.dom4j.DocumentException;
import skoa.helpers.Acciones;

public class PlantaEliminar {

    public static JFrame FRAME_PLANTA_ELIMINAR = new JFrame();
    public static JComboBox DESPLEGABLE_PLANTA = new javax.swing.JComboBox();
    static String planta_seleccionada;
    public static int llamado = 0;
    static org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);

    
    public static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    public static void main() {
        llamado = 1;

         MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
        
        
        FRAME_PLANTA_ELIMINAR.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_PLANTA_ELIMINAR.setTitle(resourceMap.getString("FRAME_ELIMINAR_PLANTA.title"));
     
        
        FRAME_PLANTA_ELIMINAR.setResizable(true);
        FRAME_PLANTA_ELIMINAR.setSize(900, 600);
        FRAME_PLANTA_ELIMINAR.setPreferredSize(new Dimension(900,600));
        FRAME_PLANTA_ELIMINAR.setLocation(120, 20);
        
        FRAME_PLANTA_ELIMINAR.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_PLANTA_ELIMINAR.setVisible(false);//dispose();
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


        JLabel etiquetaEliminar = new JLabel();
        etiquetaEliminar.setText(resourceMap.getString("etiquetaEliminarPlanta.text"));

        cargarDesplegablePlantas();
        DESPLEGABLE_PLANTA.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                         PlantaEliminar.planta_seleccionada = (String) DESPLEGABLE_PLANTA.getSelectedItem();

                    }
                });
        
        

        JButton botonEliminarPlanta = new JButton(resourceMap.getString("botonEliminarPlanta.text") );
        botonEliminarPlanta.setSize(40, 20);
        botonEliminarPlanta.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        if ( DESPLEGABLE_PLANTA.getSelectedIndex() == 0 )
                        {

                            JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.seleccionarplanta.text"), 
                            resourceMap.getString("dialog.error.title"), 1);
                        } else{
                            
                            confirmar();
                            
                            //Confirmar.main(1);
                        }
                    }
                }); 

        //Comienza el menu principal
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
        FRAME_PLANTA_ELIMINAR.setJMenuBar(jMenuBar1);
        
        

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_PLANTA_ELIMINAR.getContentPane());

        FRAME_PLANTA_ELIMINAR.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup
                (javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) // .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(5, 5, 5).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(etiquetaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(34, 34, 34).addComponent(botonEliminarPlanta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(50, Short.MAX_VALUE).addContainerGap(259, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(35, 35, 35).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) //                  .addGap(5, 5, 5)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(17, 17, 17) 
                
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 450, Short.MAX_VALUE).addGap(275, 275, 275).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(134, 134, 134).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(botonEliminarPlanta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(190, Short.MAX_VALUE)));

       
        FRAME_PLANTA_ELIMINAR.setVisible(true);

    }
    
    public static void cargarDesplegablePlantas(){
        DESPLEGABLE_PLANTA.removeAllItems();
        DESPLEGABLE_PLANTA.addItem(resourceMap.getString("DESPLEGABLE_PLANTA.firstItem.txt"));
        for(String planta : Arbol.getPlantas()){
             DESPLEGABLE_PLANTA.addItem(planta);
        }
    }
    
    private static void confirmar(){
        Object[] options = {
            resourceMap.getString("confirmar.yes.txt"), 
            resourceMap.getString("confirmar.no.txt")
        };
        
        int n = JOptionPane.showOptionDialog(FRAME_PLANTA_ELIMINAR,
                        resourceMap.getString("confirmar.pregunta"),
                        resourceMap.getString("confirmar.title") ,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
        
        if (n == JOptionPane.YES_OPTION) {

             // Actualizamos los vectores vecDispUsados y vecDispDomoticos
            SkoaMain.planta_eliminada(PlantaEliminar.planta_seleccionada);
            // Eliminamos la planta seleccionada, generamos el árbol de nuevo y mostramos las plantas restantes
            Acciones.eliminarPlantaXML(PlantaEliminar.planta_seleccionada);
            MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
            cargarDesplegablePlantas();
        }
    }
}