/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

import skoa.helpers.GeneraGraficoSkoa;
import skoa.helpers.Lecturas;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import org.jfree.data.category.DefaultCategoryDataset;
import skoa.helpers.ExtensionFileFilter;

public class PrincipalGrafs extends JFrame {

    public static String nombreFichero = "medidas_SKoA.log";
    static private javax.swing.JInternalFrame jInternalFrame1 = new javax.swing.JInternalFrame();
    static JPanel jPanel1 = new JPanel();
    public static JPanel jPanel2 = new JPanel();
    static JFrame framePrincipal = new JFrame("Visualización de Gráficas");
    static int llamado = 0;
//        static JFrame framePrincipal = new JFrame("Graphics Visualization");
    static int añoSelec;
    static int diaSelec;
    static String mesSelec;
    static String horaiSelec;
    static String horafSelec;
    static String contadorSelec;
    public static JComboBox ComboBoxContadores = new javax.swing.JComboBox();
    public static JComboBox ComboBoxAnio = new javax.swing.JComboBox();
    public static JComboBox ComboBoxMes = new javax.swing.JComboBox();
    public static JComboBox ComboBoxDia = new javax.swing.JComboBox();
    public static JComboBox ComboBoxHoraI = new javax.swing.JComboBox();
    public static JComboBox ComboBoxHoraF = new javax.swing.JComboBox();

//---------------------------------------------------------------------------------------------------
    public static <Miformato> void main() {

        llamado = 1;
//     System.out.println("entro en principalGrafs");

        //framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePrincipal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        framePrincipal.setLocation(150, 50);
        framePrincipal.setSize(830, 700);

        jInternalFrame1.setSize(600, 100);

        JLabel mensaje1 = new javax.swing.JLabel();

        // ------------------ Añadido el botón de Salir ---------------------- //
        JMenuBar jMenuBar1 = new javax.swing.JMenuBar();

        JMenu jMenu1 = new javax.swing.JMenu();
        jMenu1.setText("Archivo");
//                  jMenu1.setText("File");

        JMenuItem archivo_item2 = new JMenuItem("Cargar Fichero");
//                  JMenuItem archivo_item1 = new JMenuItem( "Exit" );

        jMenu1.add(archivo_item2);
        archivo_item2.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        JFileChooser fc = new JFileChooser();
                        // Establecemos cual será el filtro para la búsqueda de archivos, ya que sólo nos interesan aquellos con extensión .xml
                        FileFilter filtro = new ExtensionFileFilter("Archivos con extensión .log", new String[]{"LOG"});

                        // Establecemos que sólo se puedan seleccionar ficheros
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        fc.setMultiSelectionEnabled(false);
                        fc.setFileFilter(filtro);
                        // Establecemos cual será el directorio inicial por defecto
                        // El "." establece el directorio actual del proyecto
                        File dir_inicial = new File("./");
                        fc.setCurrentDirectory(dir_inicial);

                        File archivo = null;
                        Runtime r = Runtime.getRuntime();
                        Process p = null;

                        // returnVal almacenará el valor al abrirse el diálogo
                        int returnVal = fc.showOpenDialog((java.awt.Component) null);

                        // Si no ha habido problemas, y el usuario le ha dado al botón "abrir"
                        if (returnVal == fc.APPROVE_OPTION) {
                            // obtenemos el fichero seleccionado en archivo
                            archivo = fc.getSelectedFile();
//                                            principal.archivo = archivo.getName();

                            PrincipalGrafs.nombreFichero = archivo.getName();

//                                            System.out.println(principalGrafs.nombreFichero);
                            Lecturas.inicializar();
                        }
//                                        principalGrafs.ComboBoxContadores.removeAllItems();
//                                        principalGrafs.ComboBoxContadores.addItem("-Contadores-");
                        //carga un fichero nuevo para mostrar su grafica
//                                        System.out.println(principalGrafs.nombreFichero);
//                                        lecturas.inicializar();



                    }
                }); // fin de la llamada a addActionListener
        jMenuBar1.add(jMenu1);
        framePrincipal.setJMenuBar(jMenuBar1);


        JMenuItem archivo_item1 = new JMenuItem("Salir");
//                  JMenuItem archivo_item1 = new JMenuItem( "Exit" );

        jMenu1.add(archivo_item1);
        archivo_item1.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evento) {
                        //ACCION 
                        //System.exit(0);
                        framePrincipal.setVisible(false);

                    }
                }); // fin de la llamada a addActionListener
        jMenuBar1.add(jMenu1);
        framePrincipal.setJMenuBar(jMenuBar1);
        // ------------------ Añadido el botón de Salir ---------------------- //





        Lecturas.inicializar();
        ComboBoxContadores.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel.equals("-Contadores-")) {
//		      			  if(sel.equals("-Counters-")){

                            PrincipalGrafs.contadorSelec = "-Contadores-";
//                                              principalGrafs.contadorSelec = "-Counters-";

                            Lecturas.inicializarComboAnio(PrincipalGrafs.contadorSelec);
                        } else {
                            PrincipalGrafs.contadorSelec = sel.toString();
                            Lecturas.inicializarComboAnio(PrincipalGrafs.contadorSelec);
                        }
                    }
                });
        //-------------
        ComboBoxAnio.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel.equals("-Año-")) {
//		      			  if(sel.equals("-Year-")){

                            PrincipalGrafs.añoSelec = 0;
                            Lecturas.inicializarComboMes(sel);
                        } else {
                            PrincipalGrafs.añoSelec = ((Integer) sel).intValue();
                            Lecturas.inicializarComboMes(sel);
                        }
                    }
                });
        //--------------
        ComboBoxMes.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel != null) {
                            if (sel.equals("-Mes-")) {
//			        		  if(sel.equals("-Month-")){

                                PrincipalGrafs.mesSelec = (String) sel;
                                Lecturas.inicializarComboDia(añoSelec, mesSelec);
                            } else {
                                PrincipalGrafs.mesSelec = (String) sel;
                                Lecturas.inicializarComboDia(añoSelec, mesSelec);
                            }
                        }
                    }
                });
        //--------------
        ComboBoxDia.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel != null) {
                            if (sel.equals("-Día-")) {
//			        		  if(sel.equals("-Day-")){

                                PrincipalGrafs.diaSelec = 0;
                                Lecturas.inicializarComboHoraInicio(añoSelec, mesSelec, diaSelec);
                            } else {
                                PrincipalGrafs.diaSelec = ((Integer) sel).intValue();
                                Lecturas.inicializarComboHoraInicio(añoSelec, mesSelec, diaSelec);
                            }
                        }
                    }
                });
        //---------------
        ComboBoxHoraI.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel != null) {
                            if (sel.equals("-Hora Comienzo-")) {
//                                              	   if(sel.equals("-Start Hour-")){

                                PrincipalGrafs.horaiSelec = (String) sel;
                            } else {
                                PrincipalGrafs.horaiSelec = (String) sel;
                                Lecturas.inicializarComboHoraFinal(PrincipalGrafs.añoSelec, PrincipalGrafs.mesSelec,
                                        PrincipalGrafs.diaSelec, PrincipalGrafs.horaiSelec);
                            }
                        }
                    }
                });
        //----------------
        ComboBoxHoraF.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        JComboBox combo = (JComboBox) evt.getSource();
                        Object sel = combo.getSelectedItem();

                        if (sel != null) {
                            if (sel.equals("-Hora Final-")) {
//                                              if(sel.equals("-Final Hour-")){
                                PrincipalGrafs.horafSelec = (String) sel;
                            } else {
                                PrincipalGrafs.horafSelec = (String) sel;


                                //*************AQUI CARGA EL GRAFICO*************************
                                GeneraGraficoSkoa.GenerarGrafica(PrincipalGrafs.contadorSelec, PrincipalGrafs.añoSelec,
                                        PrincipalGrafs.mesSelec, PrincipalGrafs.diaSelec, PrincipalGrafs.horaiSelec,
                                        PrincipalGrafs.horafSelec);

                            }
                        }
                    }
                });
        //-----------------

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart1;

        dataset.addValue(0.0, "Cosa", "1");
        dataset.addValue(0.0, "Cosa", "2");
        dataset.addValue(0.0, "Cosa", "3");



//     chart1 = ChartFactory.createLineChart("Seleccione los parámetros de la gráfica a mostrar", "Eje X", "Eje Y", dataset, PlotOrientation.VERTICAL, false, false, false);

        chart1 = ChartFactory.createLineChart("Seleccione los parámetros de la gráfica a mostrar",
                //       chart1 = ChartFactory.createLineChart("Select the parameters you would like to show",
                //                 "X Axis", "Y Axis",
                "Eje X", "Eje Y",
                dataset,
                PlotOrientation.VERTICAL, false, false, false);

        JScrollPane jsp = new JScrollPane();
        jsp.setBounds(30, 30, 700, 400); // le doy tamaño


        jsp.setViewportView(new ChartPanel(chart1));
        jsp.setSize(500, 700);
        jPanel2.setSize(500, 700);
        jPanel2.add(jsp);
        jsp.setVisible(true);


//---------------------------------------------

        mensaje1.setText("--- Seleccione el Contador, la Fecha y Hora para la que desea ver la gráfica ---");
//     mensaje1.setText("--- Select the Counter, Date and Hour to see the graphic---");


        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setName("jPanel1");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setName("jPanel2");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(40, 40, 40).addComponent(mensaje1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(50, 50, 50).addComponent(ComboBoxContadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(ComboBoxAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(ComboBoxMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(ComboBoxDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(ComboBoxHoraI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(ComboBoxHoraF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(130, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(mensaje1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(ComboBoxMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ComboBoxDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ComboBoxHoraI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ComboBoxHoraF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ComboBoxContadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(ComboBoxAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(44, Short.MAX_VALUE)));




//---------------

        jInternalFrame1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jInternalFrame1.setEnabled(false);
        jInternalFrame1.setName("jInternalFrame1");
        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
                jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 305, Short.MAX_VALUE));
        jInternalFrame1Layout.setVerticalGroup(
                jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 74, Short.MAX_VALUE));


        //------------

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(framePrincipal.getContentPane());
        framePrincipal.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(60, 60, 60).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGap(30, 30, 30) //                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(37, 37, 37).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED) //                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(57, 57, 57).addContainerGap(53, Short.MAX_VALUE)));



        framePrincipal.setVisible(true);

    }//end main
}//end class

