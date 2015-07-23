/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import skoa.helpers.ConfiguracionGraficas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Calendar;
import java.util.Vector;
import java.awt.Color;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.KeyStroke;
import skoa.bd.ActualizarLogs;
import skoa.bd.Crear;

public class GraficasMain extends JFrame {

    private static final long serialVersionUID = 1L;
    static GraficasMain aplicacion;
    JFrame interfaz;
    JMenuBar menu;
    JMenuItem j1, j2, j3, j4, j5;
    static final int HOR_TAMANO = 1100; //Tamaño de la ventana
    static final int VER_TAMANO = 750;
    static int nDGs, tipo, ngraficas;
    Vector<String> DG = new Vector<String>();
    static ConfiguracionGraficas x = null;
    static String ruta, ruta_jar, busca;
    ActionListener eventHandler = new EventHandler(); //Manejador de eventos
    static Timer timer;
    Thread bd, act;
    
    public boolean isClose = false;

    /***************************
     * INICIO DE LA APLICACIÓN.*
     * @param args             *
     ***************************/
    private static void initDirectory() {
        
        File dir_inicial = new File("./");
        ruta_jar = dir_inicial.getAbsolutePath();
        String x;
        String os = System.getProperty("os.name");
        //En Windows las barras son \ y en Linux /.
        String aux, reemp;
        if (os.indexOf("Win") >= 0) {
            x = ruta_jar + "\\Consultas";
            aux = x + "\\";
            busca = "\\";
            reemp = "\\\\";
        } else {
            x = ruta_jar + "/Consultas";
            aux = x + "/";
            busca = "/";
            reemp = "//";  //La doble barra es por MySQL, en Linux no sé si hace falta.
        }
        File consultas = new File(x);
        if (!consultas.exists()) {
            consultas.mkdir();//Se crea el directorio consultas si no existe
        }
        ruta = "";
        // Cambiamos la barra simple por doble, para usar MYSQL
        while (aux.length() != 0) {
            if (aux.indexOf(busca) >= 0) {
                ruta = ruta + aux.substring(0, aux.indexOf(busca));
                aux = aux.substring(aux.indexOf(busca) + 1);
            } else {
                ruta = ruta + aux.substring(0);
                aux = "";
            }
            ruta = ruta + reemp;
        }
    }
    
    
    
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                initDirectory();
                aplicacion = new GraficasMain();
                aplicacion.inicializarConexiones();
            }
        });
    }
    

    public GraficasMain() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        
        
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(GraficasMain.class);
        
       
        interfaz = new JFrame(resourceMap.getString("title"));
        interfaz.setResizable(true);
        InicializaMenus(resourceMap);
        interfaz.pack();
        interfaz.setSize(HOR_TAMANO, VER_TAMANO);
        interfaz.setVisible(true);
    }

    private void InicializaMenus(org.jdesktop.application.ResourceMap resourceMap) {
        
        menu = new JMenuBar();
        menu.setBackground(new Color(224, 226, 235));
        menu.setForeground(Color.BLACK);
        //OPCION 1 y sus subopciones.
        JMenu m = new JMenu(resourceMap.getString("fileMenu.text"));
        m.setFont(resourceMap.getFont("font"));
        j1 = new JMenuItem(resourceMap.getString("newFileMenuItem.txt"), resourceMap.getIcon("newFileMenuItem.icon"));	
        j1.setBackground(Color.WHITE);
        j1.setFont(resourceMap.getFont("font"));
        j1.setToolTipText(resourceMap.getString("newFileMenuItem.toolTipText"));
        j1.setAccelerator(KeyStroke.getKeyStroke('N', java.awt.event.InputEvent.CTRL_MASK));
        m.add(j1);
        //m.add(new JMenuItem("Abrir"));	//  -Abrir estadísticas guardadas.
        //m.add(new JMenuItem("Guardar"));	//  s-Guardar las estadósticas.
        m.addSeparator();
     
        j2 = new JMenuItem(resourceMap.getString("closeFileMenuItem.txt"), resourceMap.getIcon("closeFileMenuItem.icon"));		//  -Salir de la aplicación
        j2.setBackground(Color.WHITE);
        j2.setFont(resourceMap.getFont("font"));
        j2.setToolTipText(resourceMap.getString("closeFileMenuItem.toolTipText"));
        m.add(j2);
        m.add(j2);
        menu.add(m);
        
        //OPCION 2 y sus subopciones.
        m = new JMenu(resourceMap.getString("dbMenu.txt"));
        m.setFont(resourceMap.getFont("font"));
        //j3=new JMenuItem("Crear base de datos");
        //m.add(j3);

        j4 = new JMenuItem(resourceMap.getString("updateDbItemMenu.txt"), resourceMap.getIcon("updateDbItemMenu.icon"));
        j4.setBackground(resourceMap.getColor("updateDbItemMenu.background"));
        j4.setFont(resourceMap.getFont("font"));
        m.add(j4);
        menu.add(m);
        //OPCION 3 y sus subopciones.
        m = new JMenu(resourceMap.getString("helpMenu.txt"));
        m.setFont(resourceMap.getFont("font"));
        //m.add(new JMenuItem("¡Ayuda!"));	//  -Ayuda
        //m.addSeparator();
       
        j5 = new JMenuItem(resourceMap.getString("aboutHelpItemMenu.text"), resourceMap.getIcon("aboutHelpItemMenu.icon"));
        j5.setBackground(resourceMap.getColor("aboutHelpItemMenu.background") );
        j5.setFont(resourceMap.getFont("font"));
        m.add(j5);				//  -InformaciÓn de la aplicaciÓn.
        menu.add(m);
        interfaz.setJMenuBar(menu);
    }

    public void inicializarConexiones() {
        j1.addActionListener(eventHandler);
        j2.addActionListener(eventHandler);
        //j3.addActionListener(eventHandler);
        j4.addActionListener(eventHandler);
        j5.addActionListener(eventHandler);
        //interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//JDK 1.3+ (Cerrar al apretar la "X")
    }

    
    
    //Manejador de eventos
    class EventHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
             org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(GraficasMain.class);
            
            if (e.getSource() == j1) {	//NUEVO
                System.out.println("Nuevo");
                if (x == null) {
                    x = new ConfiguracionGraficas(interfaz);//Nueva configuracion
                } else {//Antes debo eliminar la carpeta destino de la consulta, si esta vacia.
                    if (!x.vacia) { //Si la carpeta está vacía, la elimino. Sino, no hago nada.
                        String aux = x.ruta_destino;
                        File destino = new File(aux);
                        destino.delete();
                    }
                    x.eliminar();
                    x = new ConfiguracionGraficas(interfaz);
                }
            }
            if (e.getSource() == j2) { 	//Item de la barra de menu que pone Salir.
                if (x == null) {
                } //Si no se ha llegado a crear un Nuevo, no se hace nada, se sale.
                else if (!x.vacia) { //Si la carpeta está vacía, la elimino. Sino, no hago nada.
                    String aux = x.ruta_destino;
                    File destino = new File(aux);
                    destino.delete();
                }
                //System.exit(0);
            }
            /*if (e.getSource()==j3){       //Crear la base de datos a partir del script.
            Thread bd=new Crear();
            bd.run();
            }*/
            if (e.getSource() == j4) {         //Actualizar la base de datos creada.
              
                Object[] options = {
                    resourceMap.getString("questionButton.accept"), 
                    resourceMap.getString("questionButton.cancel")
                }; //titulo de los botones
                int n = JOptionPane.showOptionDialog(interfaz, resourceMap.getString("question.text") 
                        , resourceMap.getString("question.title") , JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, resourceMap.getString("questionButton.accept"));
                if (n == JOptionPane.YES_OPTION) {
                    Thread bd = new Crear();
                    bd.run();
                    while (bd.isAlive()) {
                    }
                    Thread act = new ActualizarLogs();
                    act.run();
                } else if (n == JOptionPane.NO_OPTION) {
                }
            }
            if (e.getSource() == j5) {
                JFrame frame = new JFrame("Acerca de...");
                JOptionPane.showMessageDialog(
                        frame, 
                        resourceMap.getString("about.text"),
                        resourceMap.getString("about.title"), 1
                  );
            }
        }
    ;
};
}
