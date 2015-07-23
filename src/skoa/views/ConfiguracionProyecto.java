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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.dom4j.DocumentException;

/**
 *
 * @author jorge
 */
public class ConfiguracionProyecto {

    public static JFrame FRAME_CONFIGURACION;
    public static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    
    public static int llamado = 0;
    //public static JScrollPane ARBOL_CONFIGURACION = new javax.swing.JScrollPane();

    static org.jdesktop.application.ResourceMap resourceMap 
          = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class)
                .getContext().getResourceMap(ConfiguracionProyecto.class);
    
   
  
    public static void main() throws DocumentException {
        if (FRAME_CONFIGURACION == null ){
            FRAME_CONFIGURACION = new JFrame();
        }
        
        FRAME_CONFIGURACION.setResizable(true);
        
        FRAME_CONFIGURACION.setSize(900, 500);
        FRAME_CONFIGURACION.setPreferredSize(new Dimension(900,500));
        FRAME_CONFIGURACION.setLocation(120, 20);
        
        FRAME_CONFIGURACION.setTitle(resourceMap.getString("frame_configuracion.title"));
        FRAME_CONFIGURACION.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
       
        
        javax.swing.JPanel jPanelPlanta = new javax.swing.JPanel();
        javax.swing.JPanel jPanelEstancia = new javax.swing.JPanel();
        javax.swing.JPanel jPanelActuadores = new javax.swing.JPanel();
        javax.swing.JPanel jPanelGeneral = new javax.swing.JPanel();
        
        
        javax.swing.JButton botonAgregarPlanta = new javax.swing.JButton();
        javax.swing.JButton botonEliminarPlanta = new javax.swing.JButton();
       
        javax.swing.JButton botonAgregarEstancia = new javax.swing.JButton();
        javax.swing.JButton botonEliminarEstancia = new javax.swing.JButton();
       
        javax.swing.JButton botonAgregarActuadores = new javax.swing.JButton();
        javax.swing.JButton botonEliminarActuadores = new javax.swing.JButton();
        
        javax.swing.JButton botonModificarPlano = new javax.swing.JButton();
        javax.swing.JButton botonModificarEmail = new javax.swing.JButton();
        javax.swing.JButton botonModificarVivienda = new javax.swing.JButton();


        jPanelPlanta.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelPlanta.title")));
        jPanelPlanta.setForeground(new java.awt.Color(-20561,true));
        jPanelPlanta.setName("jPanelPlanta"); // NOI18N
        
        
        botonAgregarPlanta.setText(resourceMap.getString("botonAgregarPlanta.text"));
        botonAgregarPlanta.setName("botonAgregarPlanta"); // NOI18N
        botonAgregarPlanta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
               
                if (PlantaNueva.llamado == 0) {
                    PlantaNueva.main();
                } else {
                    PlantaNueva.MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
                    PlantaNueva.FRAME_PLANTA_NUEVA.setVisible(true);
               }

            }
        });

        botonEliminarPlanta.setText(resourceMap.getString("botonEliminarPlanta.text"));
        botonEliminarPlanta.setName("botonEliminarPlanta"); // NOI18N
        botonEliminarPlanta.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
               
                if (PlantaEliminar.llamado == 0) {
                    PlantaEliminar.main();
                } else {
                    PlantaEliminar.cargarDesplegablePlantas();
                    PlantaEliminar.MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());  
                    PlantaEliminar.FRAME_PLANTA_ELIMINAR.setVisible(true);
                }

            }
        });

        javax.swing.GroupLayout jPanelPlantaLayout = new javax.swing.GroupLayout(jPanelPlanta);
        jPanelPlanta.setLayout(jPanelPlantaLayout);
        jPanelPlantaLayout.setHorizontalGroup(
            jPanelPlantaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlantaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonAgregarPlanta)
                .addGap(18, 18, 18)
                .addComponent(botonEliminarPlanta)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanelPlantaLayout.setVerticalGroup(
            jPanelPlantaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPlantaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPlantaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAgregarPlanta)
                    .addComponent(botonEliminarPlanta))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelEstancia.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelEstancia.title")));
        jPanelEstancia.setForeground(new java.awt.Color(-20561,true));
        jPanelEstancia.setName("jPanelEstancia"); // NOI18N
        
        botonAgregarEstancia.setText(resourceMap.getString("botonAgregarEstancia.text"));
        botonAgregarEstancia.setName("botonAgregarEstancia"); // NOI18N
        botonAgregarEstancia.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
               
                
                if (EstanciaNueva.llamado == 0) {
                    EstanciaNueva.main();
                } else {
                    EstanciaNueva.MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
                    EstanciaNueva.cargarDesplegablePlantas();
                    EstanciaNueva.FRAME_ESTANCIA_NUEVA.setVisible(true);
                }

            }
        });

        botonEliminarEstancia.setText(resourceMap.getString("botonEliminarEstancia.text") );
        botonEliminarEstancia.setName("botonEliminarEstancia"); // NOI18N
        botonEliminarEstancia.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
                
               
                if (EstanciaEliminar.llamado == 0) {
                    EstanciaEliminar.main();
                } else {
                    EstanciaEliminar.MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
                    EstanciaEliminar.cargarDesplegablePlantas();
                    EstanciaEliminar.cargarEstancias();
                    EstanciaEliminar.FRAME_ELIMINAR_ESTANCIA.setVisible(true);
                }

            }
        });

        javax.swing.GroupLayout jPanelEstanciaLayout = new javax.swing.GroupLayout(jPanelEstancia);
        jPanelEstancia.setLayout(jPanelEstanciaLayout);
        jPanelEstanciaLayout.setHorizontalGroup(
            jPanelEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonAgregarEstancia)
                .addGap(18, 18, 18)
                .addComponent(botonEliminarEstancia)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanelEstanciaLayout.setVerticalGroup(
            jPanelEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAgregarEstancia)
                    .addComponent(botonEliminarEstancia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelActuadores.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelActuadores.title")));
        jPanelActuadores.setForeground(new java.awt.Color(-20561,true));
        jPanelActuadores.setName("jPanelActuadores"); // NOI18N

        botonAgregarActuadores.setText(resourceMap.getString("botonAgregarActuadores.text") );
        botonAgregarActuadores.setName("botonAgregarActuadores"); // NOI18N
        botonAgregarActuadores.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
                
                
                //if (DispositivoNuevo.llamado == 0) {
                    DispositivoNuevo.main();
                //} else {
                    //Arbol.generarArbol();
                    //DispositivoNuevo.cargarDesplegablePlantas();
                    //DispositivoNuevo.FRAME_DISPOSITIVO_NUEVO.setVisible(true);
                //}

            }
        });

        botonEliminarActuadores.setText(resourceMap.getString("botonEliminarActuadores.text") );
        botonEliminarActuadores.setName("botonEliminarActuadores"); // NOI18N
        botonEliminarActuadores.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
               
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
                
                
                //if (DispositivoEliminar.llamado == 0) {
                    DispositivoEliminar.main();
                //} else {
                   // Arbol.generarArbol();
                    //DispositivoEliminar.cargarDesplegablePlantas();
                    //DispositivoEliminar.FRAME_DISPOSITIVO_ELIMINAR.setVisible(true);
                //}
            }
        });


       javax.swing.GroupLayout jPanelActuadoresLayout = new javax.swing.GroupLayout(jPanelActuadores);
        jPanelActuadores.setLayout(jPanelActuadoresLayout);
        jPanelActuadoresLayout.setHorizontalGroup(
            jPanelActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonAgregarActuadores)
                .addGap(18, 18, 18)
                .addComponent(botonEliminarActuadores)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanelActuadoresLayout.setVerticalGroup(
            jPanelActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonAgregarActuadores)
                    .addComponent(botonEliminarActuadores))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jPanelGeneral.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanelGeneral.title")));
        jPanelGeneral.setForeground(new java.awt.Color(-20561,true));
        jPanelGeneral.setName("jPanelGeneral"); // NOI18N

        botonModificarPlano.setText(resourceMap.getString("botonModificarPlano.text"));
        botonModificarPlano.setName("botonModificarPlano"); // NOI18N
        botonModificarPlano.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evento) {
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);
                try {
                   
                    //if (ModificarPlano.llamado == 0) {
                        ModificarPlano.main();
                    //} else {
                       // Arbol.generarArbol(); 
                      // ModificarPlano.cargarDesplegablePlantas();
                      // ModificarPlano.FRAME_MODIFICAR_PLANO.setVisible(true);
                    //}
                } catch (DocumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });


        botonModificarEmail.setText(resourceMap.getString("botonModificarEmail.text"));
        botonModificarEmail.setName( "botonModificarEmail"); // NOI18N
         botonModificarEmail.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
          		
                        //if (ModificarEmail.llamado == 0) {
                            ModificarEmail.main();
                        //} else {
                            //Arbol.generarArbol();
                           // ModificarEmail.FRAME_MODIFICAR_EMAIL.setVisible(true);
                        //}

                    }
                });
       
        botonModificarVivienda.setText(resourceMap.getString("botonModificarVivienda.text"));
        botonModificarVivienda.setName("botonModificarVivienda" ); // NOI18N
        botonModificarVivienda.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(false);//dispose();
                       
                        //if (ModificarNombreVivienda.llamado == 0) {
                            ModificarNombreVivienda.main();
                        //} else {
                            //Arbol.generarArbol();
                            //ModificarNombreVivienda.nombre_vivienda.setColumns(15);
                        //}
                       

                    }
                });



        javax.swing.GroupLayout jPanelGeneralLayout = new javax.swing.GroupLayout(jPanelGeneral);
        jPanelGeneral.setLayout(jPanelGeneralLayout);
        jPanelGeneralLayout.setHorizontalGroup(
            jPanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonModificarPlano, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonModificarEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(botonModificarVivienda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        jPanelGeneralLayout.setVerticalGroup(
            jPanelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGeneralLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(botonModificarPlano)
                .addGap(30, 30, 30)
                .addComponent(botonModificarVivienda)
                .addGap(18, 18, 18)
                .addComponent(botonModificarEmail)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_CONFIGURACION.getContentPane());
        FRAME_CONFIGURACION.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelActuadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelEstancia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelPlanta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jPanelGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelGeneral, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanelPlanta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanelActuadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );



        FRAME_CONFIGURACION.pack();
        FRAME_CONFIGURACION.setVisible(true);
 
    }
    
    
}
