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
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import org.dom4j.DocumentException;

public class EstanciaEliminar {

    public static JFrame FRAME_ELIMINAR_ESTANCIA = new JFrame();
    public static JComboBox DESPLEGABLE_PLANTA = new javax.swing.JComboBox();
    public static JComboBox DESPLEGABLE_ESTANCIA = new javax.swing.JComboBox();
   
    public static String planta_seleccionada;
    static String estancia_seleccionada;
    public static int llamado = 0;
    
    public static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
     
    static org.jdesktop.application.ResourceMap resourceMap = 
            org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(ConfiguracionProyecto.class);

 
    public static void main() {
        llamado = 1;
        
        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
        
        FRAME_ELIMINAR_ESTANCIA.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_ELIMINAR_ESTANCIA.setTitle(resourceMap.getString("FRAME_ELIMINAR_ESTANCIA.title"));
        FRAME_ELIMINAR_ESTANCIA.setResizable(true);
        FRAME_ELIMINAR_ESTANCIA.setSize(800, 600);
        FRAME_ELIMINAR_ESTANCIA.setLocation(120, 20);
        FRAME_ELIMINAR_ESTANCIA.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_ELIMINAR_ESTANCIA.setVisible(false);//dispose();
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
        

        JLabel etiquetaEliminarEstancia = new JLabel();
        etiquetaEliminarEstancia.setText(resourceMap.getString("etiquetaEliminarEstancia.text") );
        
        JLabel etiquetaSelecioneEstancia = new JLabel();
        etiquetaEliminarEstancia.setText(resourceMap.getString("etiquetaSelecioneEstancia.text"));

       
  
        cargarDesplegablePlantas();
        DESPLEGABLE_PLANTA.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        EstanciaEliminar.planta_seleccionada = (String) EstanciaEliminar.DESPLEGABLE_PLANTA.getSelectedItem();

                        if(DESPLEGABLE_PLANTA.getSelectedIndex() > 0 ){
                             DESPLEGABLE_ESTANCIA.setEnabled(true);
                             cargarEstancias();
                        }else if(DESPLEGABLE_PLANTA.getSelectedIndex() == 0 ){
                             DESPLEGABLE_ESTANCIA.setEnabled(false);
                             DESPLEGABLE_ESTANCIA.setSelectedIndex(0);
                        }
                    }
                });


       
        DESPLEGABLE_ESTANCIA.setEnabled(false);
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        DESPLEGABLE_ESTANCIA.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        EstanciaEliminar.estancia_seleccionada = (String) EstanciaEliminar.DESPLEGABLE_ESTANCIA.getSelectedItem();

                    }
                });


        

        JButton botonEliminarEstancia = new JButton(resourceMap.getString("botonEliminarEstancia.txt"));
        botonEliminarEstancia.setSize(40, 20);
        botonEliminarEstancia.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        if(DESPLEGABLE_ESTANCIA.getSelectedIndex() == 0  || DESPLEGABLE_PLANTA.getSelectedIndex() == 0 ){
                            JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.error.estanciaeliminaada.selecionar.text"), 
                                    resourceMap.getString("dialog.error.title"), 1);
                        }else{
                            confirmar();
                        }

                    } // fin de public void actionPerformed
                }); // fin de la llamada a addActionListener


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
        FRAME_ELIMINAR_ESTANCIA.setJMenuBar(jMenuBar1);


        
       

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_ELIMINAR_ESTANCIA.getContentPane());

        FRAME_ELIMINAR_ESTANCIA.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2)) // .addComponent(scrollbar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(etiquetaEliminarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaSelecioneEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(34, 34, 34).addComponent(botonEliminarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(50, Short.MAX_VALUE).addContainerGap(259, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(35, 35, 35).addComponent(etiquetaArbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) //                  .addGap(5, 5, 5)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(17, 17, 17) 

                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 450, Short.MAX_VALUE).addGap(275, 275, 275).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(134, 134, 134).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(botonEliminarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaEliminarEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaSelecioneEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(190, Short.MAX_VALUE)));

        Arbol.generarArbol();
        FRAME_ELIMINAR_ESTANCIA.setVisible(true);

    }//end main
    
    public static void cargarDesplegablePlantas(){
        DESPLEGABLE_PLANTA.removeAllItems();
        DESPLEGABLE_PLANTA.addItem(resourceMap.getString("DESPLEGABLE_PLANTA.firstItem.txt"));
        for(String planta : Arbol.getPlantas()){
             DESPLEGABLE_PLANTA.addItem(planta);
        }
    }
    
    public static void cargarEstancias(){
         //remueve los items anteriores
        DESPLEGABLE_ESTANCIA.removeAllItems();
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        System.out.println(EstanciaEliminar.planta_seleccionada);
         for(String e : Arbol.getEstancias(EstanciaEliminar.planta_seleccionada)){
              System.out.println(e);
             DESPLEGABLE_ESTANCIA.addItem(e);
        }

    }
    
    private static void confirmar(){
        Object[] options = {
            resourceMap.getString("confirmar.yes.txt"), 
            resourceMap.getString("confirmar.no.txt")
        };
        
        int n = JOptionPane.showOptionDialog(FRAME_ELIMINAR_ESTANCIA,
                        resourceMap.getString("confirmar.pregunta"),
                        resourceMap.getString("confirmar.title") ,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
        
        if (n == JOptionPane.YES_OPTION) {

            // Actualizamos los vectores vecDispUsados y vecDispDomoticos
            SkoaMain.estancia_eliminada(EstanciaEliminar.planta_seleccionada, EstanciaEliminar.estancia_seleccionada);
            
            // Eliminamos la estancia seleccionada, generamos el árbol de nuevo y mostramos las estancias restantes
            Acciones.eliminarEstanciaXML(EstanciaEliminar.planta_seleccionada, EstanciaEliminar.estancia_seleccionada);
             
            MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
       
            DESPLEGABLE_ESTANCIA.setEnabled(false);
            DESPLEGABLE_ESTANCIA.setSelectedIndex(0);
            DESPLEGABLE_PLANTA.setSelectedIndex(0);                 
        }
    }
    
    
}//end class