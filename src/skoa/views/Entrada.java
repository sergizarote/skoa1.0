/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.*;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.dom4j.DocumentException;

public class Entrada extends JPanel {

    static JFrame frame1 = new JFrame("Entrada");
    static JPanel panel1 = new JPanel();
    static JLabel vivienda = new javax.swing.JLabel();
    static JButton boton1 = new javax.swing.JButton("Mod. fichero");
    private static final long serialVersionUID = 1L;

    public static void main() {

        // Para que no se cierre la aplicación entera, sólo el módulo "Nuevo Proyecto"
        //frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame1.setLocation(150, 50);
        frame1.setSize(1000, 700);
        frame1.setBackground(Color.cyan);

        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();

        JMenu jMenu1 = new javax.swing.JMenu();
        jMenu1.setText("Archivo");
        JMenuItem archivo_item1 = new JMenuItem("Salir");
        jMenu1.add(archivo_item1);
        archivo_item1.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        //System.exit(0);
                        //entrada.frame1.setVisible(false);
                        frame1.dispose();
                    }
                });
        jMenuBar1.add(jMenu1);

        JMenu jMenu2 = new javax.swing.JMenu();
        jMenu2.setText("Editar");
        jMenuBar1.add(jMenu2);


        frame1.setJMenuBar(jMenuBar1);

        // Crear mensajes 

        JLabel nombre_viv = new javax.swing.JLabel();
        nombre_viv.setText("Modificar el nombre de la vivienda: ");

        JLabel mail = new javax.swing.JLabel();
        mail.setText("Modificar la dirección e-mail: ");

        vivienda.setText("Modificar el fichero de configuración de la vivienda: ");

        JLabel mensaje = new JLabel();
        mensaje.setText("Si es la primera vez que accede, tendrá que introducir el nombre de la vivienda");
        JLabel mensaje1 = new JLabel();
        mensaje1.setText("y una dirección de e-mail, donde se enviarán los mensajes de alerta de su vivienda.");

        //Añadimos los botones
        boton1.setSize(40, 20);
        boton1.addActionListener((ActionListener) new ButtonListener2());

        JButton boton2 = new javax.swing.JButton("Mod. nombre");
        boton2.setSize(40, 20);
        boton2.addActionListener((ActionListener) new ButtonListener2());

        JButton boton3 = new javax.swing.JButton("Mod. e-mail");
        boton3.setSize(40, 20);
        boton3.addActionListener((ActionListener) new ButtonListener2());

        //Paneles

        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();

        panel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        File comprobar = new File("config_hotel.xml");

        if (!comprobar.exists()) {
            panel1.setEnabled(false);
            vivienda.setEnabled(false);
            boton1.setEnabled(false);
        }

        // TRABAJAR CON EL FRAME--------------------------------------------

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(nombre_viv).addGap(10, 10, 10).addComponent(boton2).addGap(25, 25, 25)));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(25, 25, 25).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nombre_viv).addComponent(boton2)).addContainerGap(30, Short.MAX_VALUE)));


        //---------

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(panel3);
        panel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(mail).addGap(10, 10, 10).addComponent(boton3).addGap(25, 25, 25)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(25, 25, 25).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(mail).addComponent(boton3)).addContainerGap(30, Short.MAX_VALUE)));

        //--------

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(panel4);
        panel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addComponent(panel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(25, 25, 25)).addGroup(jPanel4Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)))));
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(20, 20, 20).addComponent(panel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(panel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        //----

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(25, 25, 25).addComponent(vivienda).addGap(42, 42, 42).addComponent(boton1).addContainerGap(141, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(22, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(boton1).addComponent(vivienda)).addGap(30, 30, 30)));

        //--------

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(Entrada.frame1.getContentPane());
        Entrada.frame1.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup() //.addContainerGap()
                .addComponent(panel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGap(180, 180, 180).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(mensaje).addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(mensaje1)))).addGap(370, 370, 370)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(80, 80, 80).addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(60, 60, 60).addComponent(mensaje).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(mensaje1).addGap(15, 15, 15).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(33, 33, 33)));
        //--------------------------------------------------------------------------

        frame1.setVisible(true);



    } // end main
} //end class
class ButtonListener2 implements ActionListener {

    ButtonListener2() {
    }

    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().equals("Mod. e-mail")) {
            Entrada.frame1.dispose();

            ModificarEmail.email_envio.setCaretPosition(0);

            ModificarEmail.email_envio.setSize(150, 20);
            ModificarEmail.email_envio.setText("");

            ModificarEmail.main();

        }
        if (e.getActionCommand().equals("Mod. fichero")) {

            Entrada.frame1.dispose();
            try {
                ConfiguracionProyecto.main();


            } catch (DocumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        if (e.getActionCommand().equals("Salir")) {

            // System.exit(0); // Para que no salga de la aplicación, sólo cierre la parte de Inma
            //entrada.frame1.setVisible(false);
            Entrada.frame1.dispose();

        }

        if (e.getActionCommand().equals("Mod. nombre")) {
            Entrada.frame1.dispose();
            //setVisible(false);
            ModificarNombreVivienda.main();

        }

    }
}