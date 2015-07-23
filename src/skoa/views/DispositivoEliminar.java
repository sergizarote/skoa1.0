/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import java.net.MalformedURLException;
import skoa.helpers.Acciones;
import java.awt.Component;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;


import org.dom4j.DocumentException;

import java.awt.event.MouseListener;


import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;
import skoa.models.Dispositivo;


public abstract class DispositivoEliminar  implements MouseListener, MouseMotionListener {


    public static int llamado = 0;
    //Frame principal	
    public static JFrame FRAME_DISPOSITIVO_ELIMINAR = new JFrame();
    static JPanel panel_global = new javax.swing.JPanel();
    public static JLayeredPane panel_plano = new javax.swing.JLayeredPane();
    static JPanel panel_act = new javax.swing.JPanel();
    static JPanel panel_sen = new javax.swing.JPanel();
    static JPanel panel_luz_reg = new javax.swing.JPanel();
    static JPanel panel_luz_con = new javax.swing.JPanel();
    static JPanel panel_persianas = new javax.swing.JPanel();
    static JPanel panel_electrovalvula = new javax.swing.JPanel();
    // ...
    static JPanel panel_apertura = new javax.swing.JPanel();
    static JPanel panel_movimiento = new javax.swing.JPanel();
    static JPanel panel_temperatura = new javax.swing.JPanel();
    static JPanel panel_combinado = new javax.swing.JPanel();
    static JPanel panel_inundacion = new javax.swing.JPanel();
    static JPanel panel_contadores = new javax.swing.JPanel();
    static JPanel panel_despegables = new javax.swing.JPanel();
    
    
    //Variables Globales
    static String planta_seleccionada;
    static String estancia_seleccionada;
    
    static String dir_imagen = "";
    static ImageIcon iconito = new ImageIcon();

    static Label label1 = new java.awt.Label();
    static int frame1 = 1;
    static JLabel plano = new JLabel();

    static JLabel luzreg = createLabel("luzreg","bra.png");//luces regulables
    static JLabel luzcon= createLabel("luzcon","bca.png"); //label luces conmutables
    static JLabel persianas= createLabel("persianas","pa.png");//label persianas
    static JLabel electrovalvula= createLabel("electrovalvula","elv.png");//label electrovalvula
    static JLabel apertura= createLabel("apertura","puc.png"); //label apertura/cierre
    static JLabel movimiento= createLabel("movimiento","prdetec.png");//label movimiento
    static JLabel temperatura= createLabel("temperatura","term.png"); //label temperatura
    static JLabel combinado= createLabel("combinado","comb.png"); //label combinado
    static JLabel inundacion= createLabel("inundacion","inund.png");//label inundacion
    static JLabel contadores= createLabel("contadores","cont.png"); //label contadores
    

    public static JComboBox DESPLEGABLE_PLANTA = new javax.swing.JComboBox();
    public static JComboBox DESPLEGABLE_ESTANCIA = new javax.swing.JComboBox();
    
   
    private static JScrollPane MOSTRAR_CONFIGURACION = new javax.swing.JScrollPane();
    
   static org.jdesktop.application.ResourceMap resourceMap 
          = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class)
                .getContext().getResourceMap(ConfiguracionProyecto.class);
    
   
   
    
    public static void main() {
        llamado = 1;
        
        MOSTRAR_CONFIGURACION.setViewportView( Arbol.generarArbol());
         
        FRAME_DISPOSITIVO_ELIMINAR.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        FRAME_DISPOSITIVO_ELIMINAR.setTitle(resourceMap.getString("FRAME_DISPOSITIVO_ELIMINAR.title"));
        FRAME_DISPOSITIVO_ELIMINAR.setResizable(true);
        FRAME_DISPOSITIVO_ELIMINAR.setSize(1200, 500);
        FRAME_DISPOSITIVO_ELIMINAR.setLocation(120, 20);
        FRAME_DISPOSITIVO_ELIMINAR.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                FRAME_DISPOSITIVO_ELIMINAR.setVisible(false);//dispose();
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
        
        panel_global.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_global.setSize(1000, 5000);
        panel_global.setMinimumSize(panel_global.getPreferredSize());

        panel_plano.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_plano.setSize(400, 564);

        //JLabel plano
        plano.setSize(485, 310);
        plano.setBounds(40, 0, 400, 580);

        panel_act.setSize(485, 310);
        panel_sen.setSize(485, 310);
        
        
        cargarDesplegablePlantas();
        DESPLEGABLE_PLANTA.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        DispositivoEliminar.planta_seleccionada = (String) DispositivoEliminar.DESPLEGABLE_PLANTA.getSelectedItem();

                        if (DESPLEGABLE_PLANTA.getSelectedIndex() > 0 ) {
                            planta_seleccionada = DispositivoEliminar.planta_seleccionada;
                            DispositivoEliminar.DESPLEGABLE_ESTANCIA.setEnabled(true);
                            cargarEstancias();
                        }
                        if (DESPLEGABLE_PLANTA.getSelectedIndex() == 0) {
                            DispositivoEliminar.DESPLEGABLE_ESTANCIA.setEnabled(false);
                            DispositivoEliminar.DESPLEGABLE_ESTANCIA.setSelectedIndex(0);  
                        }
                        
                        eliminarDispositivos();
                        

                    }
                });

        DESPLEGABLE_ESTANCIA.setEnabled(false);
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        DESPLEGABLE_ESTANCIA.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        DispositivoEliminar.estancia_seleccionada = (String) DispositivoEliminar.DESPLEGABLE_ESTANCIA.getSelectedItem();

                        if (DESPLEGABLE_ESTANCIA.getSelectedIndex() > 0 ) {
                            System.out.println("DESPLEGABLE_ESTANCIA eliminarDispositivos");
                            eliminarDispositivos();


                            DispositivoEliminar.dir_imagen = Acciones.SeleccionarImagen( 
                                    DispositivoEliminar.planta_seleccionada,  
                                    DispositivoEliminar.estancia_seleccionada
                            );
                            
                           



                            ImageIcon icon = new ImageIcon(DispositivoEliminar.dir_imagen);
                            Image img = icon.getImage();
                            Image newimg = img.getScaledInstance(390, 400, java.awt.Image.SCALE_SMOOTH);
                            ImageIcon newIcon = new ImageIcon(newimg);
                            plano.setIcon(newIcon);
                            plano.setBounds(5, 82, 390, 400);


                            cargarDispositivos();
                           

                        }
                    }
                });



        panel_global.setBorder(new javax.swing.border.MatteBorder(null));

        //grupo despegables
        javax.swing.GroupLayout grupoDespegables = new javax.swing.GroupLayout(panel_despegables);
        panel_despegables.setLayout(grupoDespegables);
        grupoDespegables.setHorizontalGroup(
                grupoDespegables.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grupoDespegables.createSequentialGroup().addGap(29, 29, 29).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGap(52, 52, 52)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grupoDespegables.createSequentialGroup().addContainerGap(265, Short.MAX_VALUE).addGap(116, 116, 116)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grupoDespegables.createSequentialGroup().addContainerGap(332, Short.MAX_VALUE).addContainerGap()));
        grupoDespegables.setVerticalGroup(
                grupoDespegables.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grupoDespegables.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(grupoDespegables.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(DESPLEGABLE_PLANTA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(DESPLEGABLE_ESTANCIA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(19, 19, 19)));



        //Panel actuadores
        panel_act.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_act.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("panel_act.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N


        // luz regulable, luces conmutadas, persianas, electrovalvula 
        //Panel luces regulables
        panel_luz_reg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaLuzRegulble = new JLabel(resourceMap.getString("etiquetaLuzRegulble.text"));

        javax.swing.GroupLayout grupolucesReg = new javax.swing.GroupLayout(panel_luz_reg);
        panel_luz_reg.setLayout(grupolucesReg);
        grupolucesReg.setHorizontalGroup(
                grupolucesReg.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupolucesReg.createSequentialGroup().addComponent(luzreg, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaLuzRegulble, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupolucesReg.setVerticalGroup(
                grupolucesReg.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupolucesReg.createSequentialGroup().addContainerGap().addGroup(grupolucesReg.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(luzreg).addComponent(etiquetaLuzRegulble)).addContainerGap(17, Short.MAX_VALUE)));

        //-Panel luces comnutables
        panel_luz_con.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaLucesCommutables = new JLabel(resourceMap.getString("etiquetaLucesCommutables.text"));
        
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(panel_luz_con);
        panel_luz_con.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(luzcon, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaLucesCommutables, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(luzcon).addComponent(etiquetaLucesCommutables)).addContainerGap(17, Short.MAX_VALUE)));


        //Panel Persianas
        panel_persianas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaPersianas = new JLabel(resourceMap.getString("etiquetaPersianas.text") );


        javax.swing.GroupLayout grupoPersianas = new javax.swing.GroupLayout(panel_persianas);
        panel_persianas.setLayout(grupoPersianas);
        grupoPersianas.setHorizontalGroup(
                grupoPersianas.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoPersianas.createSequentialGroup().addComponent(persianas, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoPersianas.setVerticalGroup(
                grupoPersianas.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoPersianas.createSequentialGroup().addContainerGap().addGroup(grupoPersianas.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(persianas).addComponent(etiquetaPersianas)).addContainerGap(17, Short.MAX_VALUE)));


        //Panel electrovalvulas
        panel_electrovalvula.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaEletroValvula = new JLabel(resourceMap.getString("etiquetaLucesCommutables.text") );

        javax.swing.GroupLayout grupoElectrovalvula = new javax.swing.GroupLayout(panel_electrovalvula);
        panel_electrovalvula.setLayout(grupoElectrovalvula);
        grupoElectrovalvula.setHorizontalGroup(
                grupoElectrovalvula.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoElectrovalvula.createSequentialGroup().addComponent(electrovalvula, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaEletroValvula, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoElectrovalvula.setVerticalGroup(
                grupoElectrovalvula.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoElectrovalvula.createSequentialGroup().addContainerGap().addGroup(grupoElectrovalvula.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(electrovalvula).addComponent(etiquetaEletroValvula)).addContainerGap(17, Short.MAX_VALUE)));



        //Grupo del panel de ACTUADORES- la izquierda
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panel_act);
        panel_act.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel_luz_reg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_luz_con, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_persianas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_electrovalvula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(100, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(panel_luz_reg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_luz_con, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_persianas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_electrovalvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        //---

        //Panel SENSORES
        /* mecanismo apertura/cierre, detector movimiento, detector temperatura, sensor combinado, 
        sensor inundacion, contadores */

        panel_sen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel_sen.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("panel_sen.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        panel_sen.setSize(300, 200);

        //Panel apertura/cierre puertas
        panel_apertura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
       JLabel etiquetaAperturaCierre = new JLabel(resourceMap.getString("etiquetaAperturaCierre.text") );
       
        javax.swing.GroupLayout grupoApertura = new javax.swing.GroupLayout(panel_apertura);
        panel_apertura.setLayout(grupoApertura);
        grupoApertura.setHorizontalGroup(
                grupoApertura.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoApertura.createSequentialGroup().addComponent(apertura, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaAperturaCierre, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoApertura.setVerticalGroup(
                grupoApertura.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoApertura.createSequentialGroup().addContainerGap().addGroup(grupoApertura.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(apertura).addComponent(etiquetaAperturaCierre)).addContainerGap(17, Short.MAX_VALUE)));

        //Panel sensor movimiento
        panel_movimiento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaMovimiento = new JLabel(resourceMap.getString("etiquetaMovimiento.text") );
      
        javax.swing.GroupLayout grupoMovimiento = new javax.swing.GroupLayout(panel_movimiento);
        panel_movimiento.setLayout(grupoMovimiento);
        grupoMovimiento.setHorizontalGroup(
                grupoMovimiento.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoMovimiento.createSequentialGroup().addComponent(movimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoMovimiento.setVerticalGroup(
                grupoMovimiento.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoMovimiento.createSequentialGroup().addContainerGap().addGroup(grupoMovimiento.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(movimiento).addComponent(etiquetaMovimiento)).addContainerGap(17, Short.MAX_VALUE)));


        //Panel sensor temperatura
        panel_temperatura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaTemperatura = new JLabel(resourceMap.getString("etiquetaTemperatura.text")  );

        javax.swing.GroupLayout grupoTemperatura = new javax.swing.GroupLayout(panel_temperatura);
        panel_temperatura.setLayout(grupoTemperatura);
        grupoTemperatura.setHorizontalGroup(
                grupoTemperatura.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoTemperatura.createSequentialGroup().addComponent(temperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaTemperatura, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoTemperatura.setVerticalGroup(
                grupoTemperatura.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoTemperatura.createSequentialGroup().addContainerGap().addGroup(grupoTemperatura.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(temperatura).addComponent(etiquetaTemperatura)).addContainerGap(17, Short.MAX_VALUE)));

        //Panel sensor Combinado
        panel_combinado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaCombinado = new JLabel(resourceMap.getString("etiquetaCombinado.text") );

        javax.swing.GroupLayout grupoCombinado = new javax.swing.GroupLayout(panel_combinado);
        panel_combinado.setLayout(grupoCombinado);
        grupoCombinado.setHorizontalGroup(
                grupoCombinado.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoCombinado.createSequentialGroup().addComponent(combinado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaCombinado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoCombinado.setVerticalGroup(
                grupoCombinado.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoCombinado.createSequentialGroup().addContainerGap().addGroup(grupoCombinado.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(combinado).addComponent(etiquetaCombinado)).addContainerGap(17, Short.MAX_VALUE)));



        //Panel sensor Inundacion
        panel_inundacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaInundacion = new JLabel(resourceMap.getString("etiquetaInundacion.text") );
       
        javax.swing.GroupLayout grupoInundacion = new javax.swing.GroupLayout(panel_inundacion);
        panel_inundacion.setLayout(grupoInundacion);
        grupoInundacion.setHorizontalGroup(
                grupoInundacion.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoInundacion.createSequentialGroup().addComponent(inundacion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaInundacion, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoInundacion.setVerticalGroup(
                grupoInundacion.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoInundacion.createSequentialGroup().addContainerGap().addGroup(grupoInundacion.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(inundacion).addComponent(etiquetaInundacion)).addContainerGap(17, Short.MAX_VALUE)));




        //Panel Contadores
        panel_contadores.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        JLabel etiquetaContadores = new JLabel(resourceMap.getString("etiquetaContadores.text"));

        javax.swing.GroupLayout grupoContadores = new javax.swing.GroupLayout(panel_contadores);
        panel_contadores.setLayout(grupoContadores);
        grupoContadores.setHorizontalGroup(
                grupoContadores.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoContadores.createSequentialGroup().addComponent(contadores, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(etiquetaContadores, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)));
        grupoContadores.setVerticalGroup(
                grupoContadores.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(grupoContadores.createSequentialGroup().addContainerGap().addGroup(grupoContadores.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(contadores).addComponent(etiquetaContadores)).addContainerGap(17, Short.MAX_VALUE)));

        //Grupo panel SENSORES
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(panel_sen);
        panel_sen.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_movimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_temperatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_combinado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_inundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(panel_contadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(100, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(panel_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_movimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_temperatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_combinado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_inundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_contadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        //-----------------------------------------


        //Panel central layered pane. Plano de la estancia
        panel_plano.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_plano.setSize(400, 564);
        //plano.setTransferHandler(new TransferHandler("icon"));

        javax.swing.GroupLayout plano_central = new javax.swing.GroupLayout(panel_plano);
        panel_plano.setLayout(plano_central);
        plano_central.setHorizontalGroup(
                plano_central.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addGap(0, 64, Short.MAX_VALUE).addComponent(plano, javax.swing.GroupLayout.Alignment.CENTER));
        plano_central.setVerticalGroup(
                plano_central.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(plano, javax.swing.GroupLayout.Alignment.CENTER).addGap(0, 51, Short.MAX_VALUE));

        JLabel etiquetaAclaracion = new javax.swing.JLabel(resourceMap.getString("etiquetaAclaracionEliminar.text"));
        etiquetaAclaracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaAclaracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        
        
        panel_apertura.setEnabled(false);
        panel_contadores.setEnabled(false);
        panel_electrovalvula.setEnabled(false);
        panel_inundacion.setEnabled(false);
        panel_luz_con.setEnabled(false);
        panel_luz_reg.setEnabled(false);
        panel_movimiento.setEnabled(false);
        panel_persianas.setEnabled(false);
        panel_temperatura.setEnabled(false);
        panel_combinado.setEnabled(false);

        luzreg.setEnabled(false);
        luzcon.setEnabled(false);
        persianas.setEnabled(false);
        electrovalvula.setEnabled(false);
        apertura.setEnabled(false);
        movimiento.setEnabled(false);
        temperatura.setEnabled(false);
        combinado.setEnabled(false);
        inundacion.setEnabled(false);
        contadores.setEnabled(false);

        etiquetaLuzRegulble.setEnabled(false);
        etiquetaLucesCommutables.setEnabled(false);
        etiquetaPersianas.setEnabled(false);
        etiquetaEletroValvula.setEnabled(false);
        etiquetaAperturaCierre.setEnabled(false);
        etiquetaMovimiento.setEnabled(false);
        etiquetaTemperatura.setEnabled(false);
        etiquetaCombinado.setEnabled(false);
        etiquetaInundacion.setEnabled(false);
        etiquetaContadores.setEnabled(false);

        //PANEL GLOBAL ++++++++
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(panel_global);
        panel_global.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(panel_act, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel_plano, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE).addGap(18, 18, 18) 

                .addComponent(panel_sen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(jPanel7Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(etiquetaAclaracion, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(40, 40, 40) // .addComponent(resetear)
                .addGap(20, 20, 20) 
                ).addGroup(jPanel7Layout.createSequentialGroup().addGap(18, 18, 18)
                .addComponent(panel_despegables, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(panel_despegables, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE).addGap(10, 10, 10)).addContainerGap().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(etiquetaAclaracion, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE) //                    .addComponent(resetear)
               
                .addGap(30, 30, 30)).addContainerGap().addGap(20, 20, 20).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel_plano, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addComponent(panel_sen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(panel_act, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));


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
        FRAME_DISPOSITIVO_ELIMINAR.setJMenuBar(jMenuBar1);


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(FRAME_DISPOSITIVO_ELIMINAR.getContentPane());
        FRAME_DISPOSITIVO_ELIMINAR.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(29, 29, 29).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup()
                .addContainerGap().addComponent(jSeparatorH, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))) 
                .addComponent(jSeparatorV, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panel_global, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparatorV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(35, 35, 35).addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) //                  .addGap(5, 5, 5)
                .addComponent(jSeparatorH, javax.swing.GroupLayout.DEFAULT_SIZE, 7, javax.swing.GroupLayout.DEFAULT_SIZE).addGap(17, 17, 17) //                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                //.addComponent(scrollbar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(MOSTRAR_CONFIGURACION, javax.swing.GroupLayout.PREFERRED_SIZE, 450, Short.MAX_VALUE).addGap(275, 275, 275).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(panel_global, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        FRAME_DISPOSITIVO_ELIMINAR.pack();
        FRAME_DISPOSITIVO_ELIMINAR.setVisible(true);


    }
    
    
     public static void cargarDesplegablePlantas(){
        DESPLEGABLE_PLANTA.removeAllItems();
        DESPLEGABLE_PLANTA.addItem(resourceMap.getString("DESPLEGABLE_PLANTA.firstItem.txt"));
        for(String planta : Arbol.getPlantas()){
             DESPLEGABLE_PLANTA.addItem(planta);
        }
    }
    
    private static void eliminarDispositivos(){
        Component[] componentes = DispositivoEliminar.panel_plano.getComponents();
        for (int indice = 0; indice < componentes.length; indice++) {
            String name = componentes[indice].getName();
             System.out.println("eliminarDispositivos:"+name);
            if (name != null) {
                
                if (name.equals("regulacion")
                        || name.equals("conmutacion")
                        || name.equals("persianas")
                        || name.equals("electrovalvula")
                        || name.equals("puerta")
                        || name.equals("movimiento")
                        || name.equals("temperatura")
                        || name.equals("combinado")
                        || name.equals("inundacion")
                        || name.equals("contadores")
                        || name.indexOf("boton") >  -1 ) {
                    
                    DispositivoEliminar.panel_plano.remove(componentes[indice]);// 
                    DispositivoEliminar.panel_plano.moveToBack(componentes[indice]);
                }//end if
            }
        }//end for
    } 
     
     
    private static void cargarEstancias(){
         //remueve los items anteriores
        DESPLEGABLE_ESTANCIA.removeAllItems();
        DESPLEGABLE_ESTANCIA.addItem(resourceMap.getString("DESPLEGABLE_ESTANCIA.firstItem.txt"));
        
         for(String planta : Arbol.getEstancias(DispositivoEliminar.planta_seleccionada)){
             DESPLEGABLE_ESTANCIA.addItem(planta);
        }

    }
    
    private static void cargarDispositivos(){
        ArrayList<Dispositivo> list = Arbol.getDispositivos(DispositivoEliminar.planta_seleccionada, DispositivoEliminar.estancia_seleccionada);
        for(final Dispositivo d:list){
            JLabel insertar = new JLabel();
            insertar.setName(d.tipo);
            
            insertar.setIcon(new ImageIcon(DispositivoNuevo.class.getClass().getResource(SkoaMain.ROOT_ICONS_PATH +d.icon)));
            insertar.setBounds(d.X,d.Y, 30, 40);

            
            JButton botonEliminar = new JButton();
            botonEliminar.setName("boton+" + d.id);
            botonEliminar.setIcon(new ImageIcon(DispositivoNuevo.class.getClass().getResource(SkoaMain.ROOT_ICONS_PATH + "cp.png")));
            botonEliminar.setBounds(d.X + 30, d.Y + 10, 10, 10);
            botonEliminar.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    int num = Acciones.buscaNum( 
                            d.id,
                            d.tipo,
                            DispositivoEliminar.planta_seleccionada,
                            DispositivoEliminar.estancia_seleccionada
                    );
                    String tipo = "";
                    
                    if (d.tipo.indexOf("regulacion") != -1) {
                        tipo = "luz_reg";
                    }
                    if (d.tipo.indexOf("conmutacion") != -1) {
                        tipo = "luz_conm";
                    }
                    if (d.tipo.indexOf("persianas") != -1) {
                        tipo = "pers";
                    }
                    if (d.tipo.indexOf("electrovalvula") != -1) {
                        tipo = "electrov";
                    }
                    if (d.tipo.indexOf("movimiento") != -1) {
                        tipo = "mov";
                    }
                    if (d.tipo.indexOf("temperatura") != -1) {
                        tipo = "temp";
                    }
                    if (d.tipo.indexOf("combinado") != -1) {
                        tipo = "comb";
                    }
                    if (d.tipo.indexOf("inundacion") != -1) {
                        tipo = "inund";
                    }
                    if (d.tipo.indexOf("contadores") != -1) {
                        tipo = "cont";
                    }
                   
                    try{
                      SkoaMain.dispositivo_eliminado(
                            DispositivoEliminar.planta_seleccionada,
                            DispositivoEliminar.estancia_seleccionada,
                            tipo,
                            String.valueOf(num)
                       );  
                      
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                                        
                    Acciones.eliminarDispositivoVistaEliminarDispositivos(d.id);
                  
                   
                    eliminarDispositivos();
                   
                    
                    JOptionPane.showMessageDialog(null, resourceMap.getString("dialog.ok.eliminarDispositivo.text"), 
                            resourceMap.getString("dialog.ok.title"), 1);
                    
                   
                    cargarDispositivos();

                }
            });
            
            

            //lo inserta en la vista principal
            DispositivoEliminar.panel_plano.add(insertar, javax.swing.JLayeredPane.DRAG_LAYER);
            DispositivoEliminar.panel_plano.add(botonEliminar, javax.swing.JLayeredPane.DRAG_LAYER);
            
        }                                   
    }
    
    private static JLabel createLabel(String name, String icon){
        URL u  = DispositivoNuevo.class.getClass().getResource(SkoaMain.ROOT_ICONS_PATH + icon);
        JLabel label = new JLabel(new ImageIcon(u ), JLabel.CENTER);
        label.setName(name);

        return label;
 
    }
    
    
}
