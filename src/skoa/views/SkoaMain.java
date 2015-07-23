/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.views;

import skoa.helpers.EnviarMail;
import skoa.models.VariableGlobal;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.*; // Necesario para utilizar las variables de tipo Image
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*; // Necesario para utilizar las variables de tipo ImageIcon
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JFileChooser; // Para poder utilizar el JFileChooser (abrir y guardar ficheros)
import javax.swing.filechooser.FileFilter; // Para poder utilizar los filtros con JFileChooser
import java.io.*; // Para el manejo de ficheros

import java.io.IOException;
import java.util.Iterator; // Para poder utilizar iteradores en los ficheros xml
import java.util.List; // Para poder utilizar las listas
import java.io.File;

// Para el uso de la libreía JDOM
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.dom4j.DocumentException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
// Para el uso de PointList()
import tuwien.auto.eibpoints.*;
// Necesarios para el uso de la librería Calimero (puede que no todos)
import tuwien.auto.eibxlator.PointPDUXlator;
import tuwien.auto.eibxlator.PDUXlatorList;
import tuwien.auto.eicl.struct.cemi.CEMI_L_DATA;
import tuwien.auto.eicl.*;
import tuwien.auto.eicl.util.*;
import tuwien.auto.eicl.event.*;
import tuwien.auto.eicl.struct.cemi.EIB_Address;
import tuwien.auto.eicldemo.ui.*;
// Imports utilizados en "Main_Frame.java"
import tuwien.auto.eicl.struct.eibnetip.util.HPAI;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Calendar; // Necesaria para la fecha y hora del día
import java.util.Locale;
import java.util.Vector;
import org.jfree.ui.about.Licences;
import skoa.models.ElemNameFilter;
import skoa.helpers.ExtensionFileFilter;



/**
 * The application's main frame.
 */
public class SkoaMain extends FrameView {
    //ESTEFANÍA
    //Vectores para almacenar cada dirección de grupo, con su estancia y tipo.

    static String[] vecDGS;
    static String[] vecDGnombre;
    static String[] vecDGEstancia;
    static String[] vecDGindx;
    static String[] vecDGindy;
    static String[] vecDGancho;
    static String[] vecDGalto;
    static EstanciaGenerica[] vecDGEstanciaGenerica;
    static String[] vecDGOpcion;
    static String DGEstancia = "", DGTipo = "", indx = "", indy = "", DGancho, DGalto;
    Timer inicializacion;
    int siguienteVista = 0;
    //-----------------------------------
    // Vectores que almacenarán los alias de las diferentes plantas, el nombre de las estancias,
    // la ruta de los planos y los actuadores/sensores de las diferentes estancias
    public static String[] vecAliasPlantas;
    static String[] vecNombresEstancias;
    static String[] vecRutasPlanos;
    static String[] vecRegulacion;
    static String[] vecConmutacion;
    static String[] vecPersianas;
    static String[] vecElectrovalvulas;
    static String[] vecPuertas;
    static String[] vecMovimiento;
    static String[] vecTemperatura;
    static String[] vecCombinado;
    static String[] vecInundacion;
    static String[] vecContadores;
    // Los siguientes vectores servirán para controlar el nº de luces, persianas, etc. de cada estancia
    static String[] vecNumPersianas;
    static String[] vecNumLucReg;
    static String[] vecNumLucConm;
    static String[] vecNumElectr;
    static String[] vecNumPuertas;
    static String[] vecNumPresencias;
    // Los siguiente vectores servirán para calcular las coordenadas de los iconos que se mostrarán en los planos
    static String[] vecEje_x;
    static String[] vecEje_y;
    static String[] vecAncho;
    static String[] vecAlto;
    static String[] vecIcono;
    // Este vector indicará cuántas estancias hay en cada planta
    static int[] vecNumEstancias;
    // Para saber cuántos iconos existirán en cada estancia
    static int[] vecNumIconosEstancia;
    // Vector que almacenará la sig. información de un act/sensor: planta,estancia,tipo,numero,nombre
    static String[] vecDispDomoticos;
    // Vector que almacenará la sig. información para cada estancia: planta,estancia,num_elems_luzreg,num_elems_luzconm,....así con todos sus elementos (10)
    static String[] vecDispUsados;
    // Vector que almacenará las diferentes estancias. Sirve de apoyo a la función "deten_timers"
    static EstanciaGenerica[] vecEstanciasTimers;
    // Ruta de la imagen a mostrar en la parte superior de la intefaz
    static String RutaNombreHotel;
    File dir_iniciall = new File("./");
    static String raiz_ficheros;//= dir_iniciall.getAbsolutePath();
    // Contendrá el nombre del fichero con el listado de los contadores
    public static String Fcontadores_MCA_cnf;
    // Otras variables utilizadas para almacenar los parámetros del fichero MCA.cnf
    public String fichero_contadores = "";
    // Fichero de configuración de la aplicación
    public String fichero_MCA_cnf;// = raiz_ficheros + "MCA.cnf";
    public static Integer intervalo_MCA_cnf;
    public static Integer intervalo_largo_MCA_cnf;
    public static String Fmedidas_MCA_cnf;
    public static String Flog_MCA_cnf;
    public static String IPknx_MCA_cnf;
    public static String MCA_MCA_cnf = "";
    public static String IPordenador;
    public static String Hotel_MCA_cnf;
    public static String ficheroLOG = new String();
    private VariableGlobal mainActivado = new VariableGlobal();
    private Discoverer factory;
    //private PointList pointList = new PointList();
    //public static PointList Lcontadores = new PointList();
//    private
    static CEMI_Connection tunnel = null;
    public static int contadores_aparecidos = 0;
    // Actuará a modo de buffer donde se guardarán los contadores con sus medidas
    public static String b = "";
    public String valor_recibido = "";
    // El tiempo medido en milisegundos (3 segundos) para la monitorización automática de los sensores
    public int intervalo = 3000;
    public static String ROOT_ICONS_PATH = "/skoa/views/resources/icons/";
    public static String ROOT_FILES_PATH = "/skoa/views/resources/files/";
   
    
    public SkoaMain(SingleFrameApplication app) {
        super(app);

        this.getFrame().setTitle("SKoA - SCADA Konnex Autoconfigurable");
        // Variables que corresponen a las diferentes plantas de la vivienda
        tpPlanta01 = new javax.swing.JTabbedPane();
        tpPlanta02 = new javax.swing.JTabbedPane();
        tpPlanta03 = new javax.swing.JTabbedPane();
        tpPlanta04 = new javax.swing.JTabbedPane();
        tpPlanta05 = new javax.swing.JTabbedPane();
        vecDispDomoticos = new String[31250]; // 50*5*25*5 (mÃ¡x disp/est. * campos info/disp * mÃ¡x est/planta * max plants/hotel)
        vecDispUsados = new String[1500]; // 25*5*12 (mÃ¡x est/planta * mÃ¡x. plant/hotel * informaciÃ³n de cada estancia


//COGER DIRECCION ACTUAL
//            System.out.println("directorio actual1:"+raiz_ficheros);    //DIRECCION/.
        raiz_ficheros = dir_iniciall.getAbsolutePath();
        //le quito el punto final a la direccion
        int longitud = raiz_ficheros.length();
        longitud = longitud - 1;
        raiz_ficheros = raiz_ficheros.substring(0, longitud);
//         System.out.println("directorio actual2:"+raiz_ficheros);    //okkkkk
        fichero_MCA_cnf = raiz_ficheros + "MCA.cnf";

        // Inicializamos el vector vecDispUsados
        int aux = 0;
        while (aux < vecDispUsados.length) {
            vecDispUsados[aux] = "";
            vecDispUsados[aux + 1] = "";
            vecDispUsados[aux + 2] = Integer.toString(0);
            vecDispUsados[aux + 3] = Integer.toString(0);
            vecDispUsados[aux + 4] = Integer.toString(0);
            vecDispUsados[aux + 5] = Integer.toString(0);
            vecDispUsados[aux + 6] = Integer.toString(0);
            vecDispUsados[aux + 7] = Integer.toString(0);
            vecDispUsados[aux + 8] = Integer.toString(0);
            vecDispUsados[aux + 9] = Integer.toString(0);
            vecDispUsados[aux + 10] = Integer.toString(0);
            vecDispUsados[aux + 11] = Integer.toString(0);
            aux = aux + 12;
        }

        // Y el vector vecDispDomoticos
        for (int d = 0; d < vecDispDomoticos.length; d++) {
            vecDispDomoticos[d] = "";
        }

        // InicializaciÃ³n de variables gestionadas en Netbeans
        initComponents();

        // Iniciamos las direcciones de grupo de los dispositivos
        //iniciarModelo();

        // Mostramos la hora y la fecha en tiempo real a travÃ©s de la interfaz (se actualiza cada segundo)
        Timer tiempo = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Muestra la fecha y hora en tiempo real
                fecha_res.setText(fecha());
                hora_res.setText(hora());
            }
        });
        tiempo.start();

        // InicializaciÃ³n de variables a travÃ©s del fichero XML
        //initComponentsXML();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                //statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        //statusAnimationLabel.setIcon(idleIcon);
        //progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        //statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    //statusAnimationLabel.setIcon(idleIcon);
                    //progressBar.setVisible(false);
                    //progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    //statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    //progressBar.setVisible(true);
                    //progressBar.setIndeterminate(false);
                    //progressBar.setValue(value);
                }
            }
        });
        factory = new Discoverer();

        //  -------------------- Opciones de MCA, ya veremos si las dejamos o no -------------------------------------
        /*
        System.out.println("\n" + fecha_hora() + ": Hola. Por favor espere durante 2 minutos");
        Calendar calendario = Calendar.getInstance();
        long tiempo = calendario.getInstance().getTimeInMillis(); // registra el tiempo en milisegundos
        System.out.println(fecha_hora() + ": Medida de tiempo ACTUAL (minutos)= " + tiempo/(60*1000));
        
        // textArea2 se corresponde con el log de los contadores
        synchronized (textArea2) {
        textArea2.append(fecha_hora() + ": Hola - Por favor espere durante 2 minutos \n");
        textArea2.append(fecha_hora() + ": Medida de tiempo ACTUAL (minutos)= " + tiempo/(60*1000) + " \n");
        }
        
        // (run-time):
        while(calendario.getInstance().getTimeInMillis() < tiempo + 1000*60*2){ } // se espera 2 minutos
        tiempo = calendario.getInstance().getTimeInMillis();
        System.out.println(fecha_hora() + ":Ya ha terminado de esperar - Medida de tiempo DESPUES (minutos)= " + tiempo/(60*1000));
        synchronized (textArea2) {
        textArea2.append(fecha_hora() + ": Ya ha terminado de esperar - Medida de tiempo DESPUES (minutos)= " + tiempo/(60*1000) + " \n");
        } */
        //  -------------------- Opciones de MCA, ya veremos si las dejamos o no -------------------------------------

        try {
            // Lee el fichero de configuraciÃ³n (MCA.cnf)
            LeeMCA_cnf(fichero_MCA_cnf);
        } catch (EICLException ex) {
            Logger.getLogger(SkoaMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        IPordenador = tuwien.auto.eicl.struct.eibnetip.util.HPAI.getLocalIP().getHostAddress();
        //System.out.println("IP local ordenador= " + IPordenador );

        // Fichero LOG donde se guardan los resultados de las medidas de los contadores domÃ³ticos
        fichero_contadores = raiz_ficheros + Fmedidas_MCA_cnf;

        // Establecemos la ruta del fichero LOG de la apliaciÃ³n
        ficheroLOG = raiz_ficheros + Flog_MCA_cnf;

        String error = "";
        // Si en el fichero de configuraciÃ³n MCA.cnf el campo MCA estÃ¡ con el valor "si"
        // se activa el procedimiento de tiempo real MideCuandoArranca (MCAinit)
        if (SkoaMain.MCA_MCA_cnf.contentEquals("si") == true) // Llamada a MCAinit, que se encarga de conectarse al bus, realizar lecturas periÃ³dicas,
        // obtener los valores medidos, guardar informaciÃ³n den los ficheros de LOG, etc
        {
            MCAinit();
        }
    } // Fin de "public MaipezView"

    // Lee un fichero XML y genera el objeto Document
    public static Document readXML(String filePath) throws JDOMException, IOException {

        File f = new File(filePath);
        SAXBuilder b = new SAXBuilder(false);
        return b.build(f);
    }

    // Recorre todos los objetos que forman parte del documento XML y son seleccionados por el filtro
    public static int countElements(Document doc, String name) {
        // Será el contador de los elementos a buscar
        int result = 0;

        // Declaramos una variable iterador
        Iterator it = doc.getDescendants(new ElemNameFilter(name));
        // Y mientras haya elementos, los irá recorriendo
        while (it.hasNext()) {
            Object o = it.next();
            // Y contabilizando
            result++;
        }
        return result;
    }

    // Recorre todos los objetos que forman parte del documento XML y los muestra
    // Se imprimen también los nombres de las clases Java a las que pertenecen los distintos objetos
    public static void showClassesForNodes(Document doc) {

        Iterator it = doc.getDescendants();
        while (it.hasNext()) {
            Object o = it.next();
            //System.out.println("Object: " + o.toString());
            //System.out.println("*** JDOM Class: " + o.getClass().getCanonicalName() + "\n");

            // ¿Es el objeto (java.lang.Object) una instancia de org.jdom.Element?
            if (o instanceof Element) {
                // Llamamos a esta otra función para mostrar los atributos
                showElementAttributes((Element) o);
            }
        }
    }

    // Imprime los atributos de un elemento
    public static void showElementAttributes(Element e) {

        List attributes = e.getAttributes();
        for (Object o : attributes) {
            // TUUUUXA Para ver los elementos del fichero .xml
            //System.out.println("Object: " + o.toString());

            // Llamada al procedimiento encargado de rellenar los vectores con los valores
            // leídos en el fichero XML
            obtenAtributo(o.toString());
        }
    }

    // Dado un documento doc que representa a un fichero XML, cuenta los nodos que se adaptan a la expresión dada por xPath
    public static int cuentaNodosXpath(Document doc, String xPath) throws JDOMException {

        int count = 0;
        XPath xPathProc = XPath.newInstance(xPath);
        List list = xPathProc.selectNodes(doc);
        count = list.size();
        return count;
    }

    /* Procedimiento que asigna la subristra contenida entre " " de la ristra pasada por parámetro al vector que le corresponde
     * @author David Monné Chávez
     * @param original Ristra que contiene uno de los casos que se muestran más abajo
     */
    public static void obtenAtributo(String original) {


        /* La ristra original tiene alguno de los siguientes formatos:
         * original = [Attribute: alias="Planta Baja"]
         * original = [Attribute: nombre="Dormitorio 1"]
         * original = [Attribute: imagen="Ruta imagen 1"]
         * original = [Attribute: ruta="Ruta plano"] 
         * original = [Attribute: regulacion="si"]
         * original = [Attribute: conmutacion="no"]
         * original = [Attribute: persianas="si"]
         * original = [Attribute: electrovalvula="si"]
         * original = [Attribute: puerta="si"]
         * original = [Attribute: movimiento="no"]
         * original = [Attribute: temperatura="si"]
         * original = [Attribute: combinado="si"]
         * original = [Attribute: inundacion="si"]
         * original = [Attribute: contadores="si"]
         * original = [Attribute: blinds="1"]
         * original = [Attribute: lucreg="1"]
         * original = [Attribute: bombs="1"]
         * original = [Attribute: valves="1"]
         * original = [Attribute: doors="1"]
         * original = [Attribute: presencia="1"]
         * original = [Attribute: tipo="presencia"]
         * original = [Attribute: eje_x="130"]
         * original = [Attribute: eje_y="180"]
         * original = [Attribute: ancho="50"]
         * original = [Attribute: largo="60"]
         * original = [Attribute: id="conta1"]
         * original = [Attribute: direccionGrupo="2/1/01"]
         * original = [Attribute: tipoEstructura="0x13"]
         * original = [Attribute: subtipo="5.013"]
         */

//        acciones.obtenerAtributo();
//        for(int iii =VariableGlobal.indVN-1; iii >0;iii--){
//         System.out.println(vecNombresEstancias[iii]);
//
//        }

        /* //comienza MEGA COMENTARIO*/

        //System.out.println(original);
        String subristra;
        int comienzo, fin, eleccion = 0;

        // Lo primero que tenemos que hacer es averiguar el tipo de atributo que es
        // para saber en qué vector lo almacenaremos. tipo_atributo = original.indexOf("alias"), por ej.
        // Si es distinto de -1, es porque se ha encontrado la subristra alias
        if (original.indexOf("alias") != -1) {
            eleccion = 1; // 1: Utilizar el vector de alias para las plantas
        } else if (original.indexOf("nombre") != -1) {
            eleccion = 2; // 2: Utilizar el vector de nombres de estancias
        } else if (original.indexOf("imagen") != -1) {
            eleccion = 3; // 3: Utilizar el vector de rutas de planos
        } else if (original.indexOf("logo") != -1) {
            eleccion = 4; // 4: Utilizar la variable que almacena la imagen del hotel
        } else // Los que tienen el símbolo = añadido al final, son para distinguirlos de la palabra clave "tipo"
        // Por ejemplo, para diferenciar regulacion=... de tipo=regulacion (Si no, se producía un error)
        if (original.indexOf("regulacion=") != -1) {
            eleccion = 5; // 5: Utilizar la variable que almacena el actuador "regulacion"
        } else if (original.indexOf("conmutacion=") != -1) {
            eleccion = 6; // 6: Utilizar la variable que almacena el actuador "conmutacion"
        } else if (original.indexOf("persianas=") != -1) {
            eleccion = 7; // 7: Utilizar la variable que almacena el actuador "persianas"
        } else if (original.indexOf("electrovalvula=") != -1) {
            eleccion = 8; // 8: Utilizar la variable que almacena el actuador "electrovalvula"
        } else if (original.indexOf("puerta=") != -1) {
            eleccion = 9; // 9: Utilizar la variable que almacena el sensor "puerta"
        } else if (original.indexOf("movimiento=") != -1) {
            eleccion = 10; // 10: Utilizar la variable que almacena el sensor "movimiento"
        } else if (original.indexOf("temperatura=") != -1) {
            eleccion = 11; // 11: Utilizar la variable que almacena el sensor "temperatura"
        } else if (original.indexOf("combinado=") != -1) {
            eleccion = 12; // 12: Utilizar la variable que almacena el sensor "combinado"
        } else if (original.indexOf("inundacion=") != -1) {
            eleccion = 13; // 13: Utilizar la variable que almacena el sensor "inundacion"
        } else if (original.indexOf("contadores=") != -1) {
            eleccion = 14; // 14: Utilizar la variable que almacena el sensor "contadores"
        } else if (original.indexOf("blinds") != -1) {
            eleccion = 15; // 15: Utilizar la variable que almacena el nº de persianas
        } else if (original.indexOf("lucreg") != -1) {
            eleccion = 16; // 16: Utilizar la variable que almacena el nº de luces regulables
        } else if (original.indexOf("bombs") != -1) {
            eleccion = 17; // 17: Utilizar la variable que almacena el nº de luces conmutables
        } else if (original.indexOf("valves") != -1) {
            eleccion = 18; // 18: Utilizar la variable que almacena el nº de electroválvulas
        } else if (original.indexOf("doors") != -1) {
            eleccion = 19; // 19: Utilizar la variable que almacena el nº de puertas
        } else if (original.indexOf("presencia=") != -1) {
            eleccion = 20; // 20: Utilizar la variable que almacena el nº de iconos "presencia"
        } else if (original.indexOf("x=") != -1) {
            eleccion = 21; // 21: Utilizar la variable que almacena las coordenadas de los ejes x de los iconos
        } else if (original.indexOf("y=") != -1) {
            eleccion = 22; // 22: Utilizar la variable que almacena las coordenadas de los ejes y de los iconos
        } else if (original.indexOf("ancho") != -1) {
            eleccion = 23; // 23: Utilizar la variable que almacena la anchura de los iconos
        } else if (original.indexOf("largo") != -1) {
            eleccion = 24; // 24: Utilizar la variable que almacena la altura de los iconos
        } else // Evitamos que no entre en este if cuando se encuentre con "subtipo", sólo cuando sea "tipo"
        if (original.indexOf("[Attribute: tipo=") != -1) {
            eleccion = 25; // 25: Utilizar la variable que almacena la imagen del icono
        } else if (original.indexOf("id") != -1) {
            eleccion = 26;
        } else if (original.indexOf("direccionGrupo") != -1) {
            eleccion = 27;
        } else if (original.indexOf("tipoEstructura") != -1) {
            eleccion = 28;
        } else if (original.indexOf("subtipo") != -1) {
            eleccion = 29;
        } else if (original.indexOf("nombred") != -1) {
            eleccion = 30;
        }
        // Obtenemos la posición en la ristra original de la primera aparición de "
        comienzo = original.indexOf('"');
        // Y la posición del corchete de cierre, justo al final
        fin = original.indexOf(']');
        // Subristra tendrá la ristra que estamos buscando, es decir, lo que hay entre ""
        subristra = original.substring(comienzo + 1, fin - 1);

        // Ahora, dependiendo del valor de eleccion, insertaremos la subristra en un vector u otro
        //System.out.println("eleccion:"+eleccion+" subristra="+subristra);
        switch (eleccion) {
            case 1:

                vecAliasPlantas[VariableGlobal.indVA] = subristra;
                VariableGlobal.indVA++;
//                System.out.println("caso 1-vecAliasPlantas[VariableGlobal.indVA]="+vecAliasPlantas[VariableGlobal.indVA]+ " -indice vector- :"+VariableGlobal.indVA);

                if (VariableGlobal.comodin != 0) {
                    // Almacenamos cuantas estancias hay en una determinada planta cuando se haya detectado la siguiente
                    vecNumEstancias[VariableGlobal.indVecNumEst] = VariableGlobal.cont_est;
//                    System.out.println("caso 1-vecNumEstancias[VariableGlobal.indVecNumEst]="+vecNumEstancias[VariableGlobal.indVecNumEst]+ " -indice vector- :"+ VariableGlobal.indVecNumEst );
                    VariableGlobal.indVecNumEst++;
                    VariableGlobal.cont_est = 0;
                }
                VariableGlobal.comodin = 1;
                break;
            case 2:
                if (VariableGlobal.indVN != 0) {
                    vecNumIconosEstancia[VariableGlobal.indVNIE] = VariableGlobal.contador;
//                    System.out.println("caso 2-vecNumIconosEstancia[VariableGlobal.indVNIE]="+vecNumIconosEstancia[VariableGlobal.indVNIE]+ " -indice vector- :"+ VariableGlobal.indVNIE);
                    VariableGlobal.indVNIE++;
                    // Y reiniciamos la variable contadora
                    VariableGlobal.contador = 0;
                }

                //ESTEFANÍA: AL CAMBIAR DE ESTANCIA, SE REESTABLECEN LAS VARIABLES.
                //La VariableGlobal para comenzar a contar los dispositivos y asignar bien las opciones.
                DGEstancia = subristra;
                VariableGlobal.cmov = 1;
                VariableGlobal.cpuertas = 6;
                VariableGlobal.cinun = 11;
                VariableGlobal.ctemp = 16;
                VariableGlobal.ccomb = 17;
                VariableGlobal.ccont = 18;
                VariableGlobal.cluzcon = 19;
                VariableGlobal.cpers = 24;
                //-----------------------

                vecNombresEstancias[VariableGlobal.indVN] = subristra;
//                System.out.println("caso 2-vecNombresEstancias[VariableGlobal.indVN]="+vecNombresEstancias[VariableGlobal.indVN]+ " -indice vector- :"+ VariableGlobal.indVN);
                VariableGlobal.indVN++;

                // Incrementamos el contador de estancias
                VariableGlobal.cont_est = VariableGlobal.cont_est + 1;
                break;
            case 3:

                System.out.println("en subristra(rutas planos?):" + subristra);

                vecRutasPlanos[VariableGlobal.indVI] = subristra;
//                System.out.println("caso 3-vecRutasPlanos[VariableGlobal.indVI] ="+vecRutasPlanos[VariableGlobal.indVI] + " -indice vector- :"+ VariableGlobal.indVI );
                VariableGlobal.indVI++;
                break;
            case 4:
                // AQUI ESTA LO QUE VA EN EL LOGO
//                try{
//                    File aFile = new File(principal.archivo);
//                    SAXReader xmlReader = new SAXReader();
//		  //xmlReader.setEncoding("UTF-8");
//                    xmlReader.setEncoding("f");
//                    Document doc = xmlReader.read(aFile);
//                    Element node = (Element) doc.selectSingleNode( "//vivienda");
//                    RutaNombreHotel = node.valueof("@name")
//                }catch (DocumentException e) {
//		e.printStackTrace();
//                }

                RutaNombreHotel = subristra;

                break;
            case 5:
                vecRegulacion[VariableGlobal.indVReg] = subristra;
//                System.out.println("caso 5:vecRegulacion[VariableGlobal.indVReg]="+vecRegulacion[VariableGlobal.indVReg]+" -indice:"+VariableGlobal.indVReg+"---contenido subristra:"+subristra);
                VariableGlobal.indVReg++;
                break;
            case 6:
                vecConmutacion[VariableGlobal.indVConm] = subristra;
//              System.out.println("caso 6:vecConmutacion[VariableGlobal.indVConm]="+vecConmutacion[VariableGlobal.indVConm]+" -indice:"+VariableGlobal.indVConm);
                VariableGlobal.indVConm++;
                break;
            case 7:
                vecPersianas[VariableGlobal.indVPer] = subristra;
//                System.out.println("caso 7:vecPersianas[VariableGlobal.indVPer]="+vecPersianas[VariableGlobal.indVPer]+" -indice:"+VariableGlobal.indVPer);
                VariableGlobal.indVPer++;
                break;
            case 8:
                vecElectrovalvulas[VariableGlobal.indVElec] = subristra;
                VariableGlobal.indVElec++;
                break;
            case 9:
                vecPuertas[VariableGlobal.indVPuert] = subristra;
                VariableGlobal.indVPuert++;
                break;
            case 10:
                vecMovimiento[VariableGlobal.indVMov] = subristra;
                VariableGlobal.indVMov++;
                break;
            case 11:
                vecTemperatura[VariableGlobal.indVTemp] = subristra;
                VariableGlobal.indVTemp++;
                break;
            case 12:
                vecCombinado[VariableGlobal.indVComb] = subristra;
                VariableGlobal.indVComb++;
                break;
            case 13:
                vecInundacion[VariableGlobal.indVInund] = subristra;
                VariableGlobal.indVInund++;
                break;
            case 14:
                vecContadores[VariableGlobal.indVCont] = subristra;
                VariableGlobal.indVCont++;
                break;
            case 15:
                vecNumPersianas[VariableGlobal.indVNPers] = subristra;
                VariableGlobal.indVNPers++;
                break;
            case 16:
                vecNumLucReg[VariableGlobal.indVNLR] = subristra;
                VariableGlobal.indVNLR++;
                break;
            case 17:
                vecNumLucConm[VariableGlobal.indVNLC] = subristra;
                VariableGlobal.indVNLC++;
                break;
            case 18:
                vecNumElectr[VariableGlobal.indVNE] = subristra;
                VariableGlobal.indVNE++;
                break;
            case 19:
                vecNumPuertas[VariableGlobal.indVNPuer] = subristra;
                VariableGlobal.indVNPuer++;
                break;
            case 20:
                vecNumPresencias[VariableGlobal.indVNPres] = subristra;
                VariableGlobal.indVNPres++;
                break;
            case 21:
                indx = subristra;//ESTEFANIA
                //*********************************************************************************
                //   INMA POR AKI
                //***********************************************************************************
                // Para cuando X pertenezca a un icono de regulación ...
                if (VariableGlobal.tipo_actual.indexOf("regulacion") != -1) {
                    // Si ambos índices se igualan
                    if (VariableGlobal.ind_reg == VariableGlobal.ind_reg_aux) {
                        // Hay que actualizarlos. En principio, hay 45 posiciones de distancia entre dos subgrupos de elementos del mismo tipo,
                        // y 5 de distancia entre un grupo y otro diferente, de ahí esos números
                        VariableGlobal.ind_reg = VariableGlobal.ind_reg + 45;
                        VariableGlobal.ind_reg_aux = VariableGlobal.ind_reg + 5;
                    }
                    // Insertamos en el vector de las coordenadas del eje x, en su posición adecuada, la coordenada que se está tratando
                    vecEje_x[VariableGlobal.ind_reg] = subristra;
//                    System.out.println("vecEje_x = subristra:"+subristra); en subristra estas las coordenadas X
                }
                // Mismo procedimiento para cuando se trata de un elemento de conmutación
                if (VariableGlobal.tipo_actual.indexOf("conmutacion") != -1) {
                    if (VariableGlobal.ind_conm == VariableGlobal.ind_conm_aux) {
                        VariableGlobal.ind_conm = VariableGlobal.ind_conm + 45;
                        VariableGlobal.ind_conm_aux = VariableGlobal.ind_conm + 5;
                    }
                    vecEje_x[VariableGlobal.ind_conm] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de persianas
                if (VariableGlobal.tipo_actual.indexOf("persianas") != -1) {
                    if (VariableGlobal.ind_pers == VariableGlobal.ind_pers_aux) {
                        VariableGlobal.ind_pers = VariableGlobal.ind_pers + 45;
                        VariableGlobal.ind_pers_aux = VariableGlobal.ind_pers + 5;
                    }
                    vecEje_x[VariableGlobal.ind_pers] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de puerta
                if (VariableGlobal.tipo_actual.indexOf("puerta") != -1) {
                    if (VariableGlobal.ind_puert == VariableGlobal.ind_puert_aux) {
                        VariableGlobal.ind_puert = VariableGlobal.ind_puert + 45;
                        VariableGlobal.ind_puert_aux = VariableGlobal.ind_puert + 5;
                    }
                    vecEje_x[VariableGlobal.ind_puert] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de movimiento
                if (VariableGlobal.tipo_actual.indexOf("movimiento") != -1) {
                    if (VariableGlobal.ind_mov == VariableGlobal.ind_mov_aux) {
                        VariableGlobal.ind_mov = VariableGlobal.ind_mov + 45;
                        VariableGlobal.ind_mov_aux = VariableGlobal.ind_mov + 5;
                    }
                    vecEje_x[VariableGlobal.ind_mov] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de temperatura
                if (VariableGlobal.tipo_actual.indexOf("temperatura") != -1) {
                    if (VariableGlobal.ind_temp == VariableGlobal.ind_temp_aux) {
                        VariableGlobal.ind_temp = VariableGlobal.ind_temp + 45;
                        VariableGlobal.ind_temp_aux = VariableGlobal.ind_temp + 5;
                    }
                    vecEje_x[VariableGlobal.ind_temp] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de combinado
                if (VariableGlobal.tipo_actual.indexOf("combinado") != -1) {
                    if (VariableGlobal.ind_comb == VariableGlobal.ind_comb_aux) {
                        VariableGlobal.ind_comb = VariableGlobal.ind_comb + 45;
                        VariableGlobal.ind_comb_aux = VariableGlobal.ind_comb + 5;
                    }
                    vecEje_x[VariableGlobal.ind_comb] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de inundación
                if (VariableGlobal.tipo_actual.indexOf("inundacion") != -1) {
                    if (VariableGlobal.ind_inund == VariableGlobal.ind_inund_aux) {
                        VariableGlobal.ind_inund = VariableGlobal.ind_inund + 45;
                        VariableGlobal.ind_inund_aux = VariableGlobal.ind_inund + 5;
                    }
                    vecEje_x[VariableGlobal.ind_inund] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de contadores
                if (VariableGlobal.tipo_actual.indexOf("contadores") != -1) {
                    if (VariableGlobal.ind_cont == VariableGlobal.ind_cont_aux) {
                        VariableGlobal.ind_cont = VariableGlobal.ind_cont + 45;
                        VariableGlobal.ind_cont_aux = VariableGlobal.ind_cont + 5;
                    }
                    vecEje_x[VariableGlobal.ind_cont] = subristra;
                }
                // Mismo procedimiento para cuando se trata de un elemento de electroválvula
                if (VariableGlobal.tipo_actual.indexOf("electrovalvula") != -1) {
                    if (VariableGlobal.ind_electr == VariableGlobal.ind_electr_aux) {
                        VariableGlobal.ind_electr = VariableGlobal.ind_electr + 45;
                        VariableGlobal.ind_electr_aux = VariableGlobal.ind_electr + 5;
                    }
                    vecEje_x[VariableGlobal.ind_electr] = subristra;
                }
                break;
            case 22:
                indy = subristra;//ESTEFANIA

                // En el caso de las coordenadas del eje y, no es necesario actualizar los índices
                if (VariableGlobal.tipo_actual.indexOf("regulacion") != -1) {
                    vecEje_y[VariableGlobal.ind_reg] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("conmutacion") != -1) {
                    vecEje_y[VariableGlobal.ind_conm] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("persianas") != -1) {
                    vecEje_y[VariableGlobal.ind_pers] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("puerta") != -1) {
                    vecEje_y[VariableGlobal.ind_puert] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("movimiento") != -1) {
                    vecEje_y[VariableGlobal.ind_mov] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("temperatura") != -1) {
                    vecEje_y[VariableGlobal.ind_temp] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("combinado") != -1) {
                    vecEje_y[VariableGlobal.ind_comb] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("inundacion") != -1) {
                    vecEje_y[VariableGlobal.ind_inund] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("contadores") != -1) {
                    vecEje_y[VariableGlobal.ind_cont] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("electrovalvula") != -1) {
                    vecEje_y[VariableGlobal.ind_electr] = subristra;
                }

                break;
            case 23:
                DGancho = subristra;
                // Tampoco para el vector que obtiene el ancho de los iconos
                if (VariableGlobal.tipo_actual.indexOf("regulacion") != -1) {
                    vecAncho[VariableGlobal.ind_reg] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("conmutacion") != -1) {
                    vecAncho[VariableGlobal.ind_conm] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("persianas") != -1) {
                    vecAncho[VariableGlobal.ind_pers] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("puerta") != -1) {
                    vecAncho[VariableGlobal.ind_puert] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("movimiento") != -1) {
                    vecAncho[VariableGlobal.ind_mov] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("temperatura") != -1) {
                    vecAncho[VariableGlobal.ind_temp] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("combinado") != -1) {
                    vecAncho[VariableGlobal.ind_comb] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("inundacion") != -1) {
                    vecAncho[VariableGlobal.ind_inund] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("contadores") != -1) {
                    vecAncho[VariableGlobal.ind_cont] = subristra;
                }
                if (VariableGlobal.tipo_actual.indexOf("electrovalvula") != -1) {
                    vecAncho[VariableGlobal.ind_electr] = subristra;
                }

                break;
            case 24:
                DGalto = subristra;
                // Sin embargo, en el vector que obtiene el largo de los iconos, sí es necesario actualizar los índices
                // Esto es así pq en el fichero .xml es el último elemento (entre eje_x, eje_y, ancho y alto)antes de empezar una nueva línea
                if (VariableGlobal.tipo_actual.indexOf("regulacion") != -1) {
                    vecAlto[VariableGlobal.ind_reg] = subristra;
                    VariableGlobal.ind_reg++;
                }
                if (VariableGlobal.tipo_actual.indexOf("conmutacion") != -1) {
                    vecAlto[VariableGlobal.ind_conm] = subristra;
                    VariableGlobal.ind_conm++;
                }
                if (VariableGlobal.tipo_actual.indexOf("persianas") != -1) {
                    vecAlto[VariableGlobal.ind_pers] = subristra;
                    VariableGlobal.ind_pers++;
                }
                if (VariableGlobal.tipo_actual.indexOf("puerta") != -1) {
                    vecAlto[VariableGlobal.ind_puert] = subristra;
                    VariableGlobal.ind_puert++;
                }
                if (VariableGlobal.tipo_actual.indexOf("movimiento") != -1) {
                    vecAlto[VariableGlobal.ind_mov] = subristra;
                    VariableGlobal.ind_mov++;
                }
                if (VariableGlobal.tipo_actual.indexOf("temperatura") != -1) {
                    vecAlto[VariableGlobal.ind_temp] = subristra;
                    VariableGlobal.ind_temp++;
                }
                if (VariableGlobal.tipo_actual.indexOf("combinado") != -1) {
                    vecAlto[VariableGlobal.ind_comb] = subristra;
                    VariableGlobal.ind_comb++;
                }
                if (VariableGlobal.tipo_actual.indexOf("inundacion") != -1) {
                    vecAlto[VariableGlobal.ind_inund] = subristra;
                    VariableGlobal.ind_inund++;
                }
                if (VariableGlobal.tipo_actual.indexOf("contadores") != -1) {
                    vecAlto[VariableGlobal.ind_cont] = subristra;
                    VariableGlobal.ind_cont++;
                }
                if (VariableGlobal.tipo_actual.indexOf("electrovalvula") != -1) {
                    vecAlto[VariableGlobal.ind_electr] = subristra;
                    VariableGlobal.ind_electr++;
                }
                break;
            case 25:
                DGTipo = subristra;
                // Almacenamos en la variable tipo_actual el tipo que se está tratando (regulacion, conmutacion, persianas, etc)
                VariableGlobal.tipo_actual = original;
                // Y actualizamos la variable que contará el número de iconos por estancia
                VariableGlobal.contador = VariableGlobal.contador + 1;

                // En este caso se trata el tipo de icono que se representará
                // Para ello, dependiendo del valor del tipo, se mostrará una imagen u otra
                if (original.indexOf("regulacion") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"brd.png";
                }
                if (original.indexOf("conmutacion") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"bcd.png";
                }
                if (original.indexOf("persianas") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"pd.png";
                }
                if (original.indexOf("puerta") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"pud.png";
                }
                if (original.indexOf("movimiento") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"prd.png";
                }
                if (original.indexOf("electrovalvula") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"vd.png";
                }
                if (original.indexOf("temperatura") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"term.png";
                }
                if (original.indexOf("combinado") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"comb.png";
                }
                if (original.indexOf("inundacion") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"inund.png";
                }
                if (original.indexOf("contadores") != -1) {
                    vecIcono[VariableGlobal.indVIcono] = SkoaMain.ROOT_ICONS_PATH+"cont.png";
                }
                // Sólo uno de esos if se ejecutará cada vez que entre en el case, así que incrementamos el índice al final    
                VariableGlobal.indVIcono++;
                break;
            //---------------------------------------------
            //ESTEFANÍA
            case 27: //las direcciones de grupo
                System.out.println("DG=" + subristra);
                System.out.println("    Estancia:" + DGEstancia);
                System.out.println("    Tipo:" + DGTipo);
                vecDGS[VariableGlobal.indVDG] = subristra;
                vecDGEstancia[VariableGlobal.indVDG] = DGEstancia;
                vecDGindx[VariableGlobal.indVDG] = indx;
                vecDGindy[VariableGlobal.indVDG] = indy;
                vecDGancho[VariableGlobal.indVDG] = DGancho;
                vecDGalto[VariableGlobal.indVDG] = DGalto;
                vecDGOpcion[VariableGlobal.indVDG] = DGTipo;
                if (DGTipo.indexOf("movimiento") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.cmov;
                    VariableGlobal.cmov++;
                }
                if (DGTipo.indexOf("puerta") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.cpuertas;
                    VariableGlobal.cpuertas++;
                }
                if (DGTipo.indexOf("inundacion") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.cinun;
                    VariableGlobal.cinun++;
                }
                //Sólo puede haber un sensor de temperatura, otro combinado, y otro de contadores.
                if (DGTipo.indexOf("temperatura") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.ctemp;
                }
                if (DGTipo.indexOf("combinado") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.ccomb;
                }
                if (DGTipo.indexOf("contadores") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.ccont;
                }
                if (DGTipo.indexOf("conmutacion") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.cluzcon;
                    VariableGlobal.cluzcon++;
                }
                if (DGTipo.indexOf("persianas") != -1) {
                    vecDGOpcion[VariableGlobal.indVDG] = "" + VariableGlobal.cpers;
                    VariableGlobal.cpers++;
                }
                System.out.println("    Opcion:" + vecDGOpcion[VariableGlobal.indVDG]);
                VariableGlobal.indVDG++;

                break;
        } // fin del switch
    } // fin del procedimiento obtenAtributo

    /* Procedimiento que lee el fichero de configuración de la aplicación 
     * @author Domingo Benítez Díaz
     * @param _DeviceFile el archivo sobre el que se realizará la lectura
     */
    public void LeeMCA_cnf(String _DeviceFile) throws EICLException {
        String str;
        String[] subStr = new String[10];

        try {
            File f = new File(_DeviceFile); // Abre el fichero que se le pasa
            BufferedReader br = new BufferedReader(new FileReader(f)); // Open del fichero

            //System.out.println("FICHERO DE CONFIGURACIÓN MCA.cnf"); 

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[0] = str.substring(6); // Extrae string HOTEL        
            //System.out.println("  HOTEL= " + subStr[0] ); 
            Hotel_MCA_cnf = subStr[0];

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[1] = str.substring(4); // Extrae string MCA = si/no        
            //System.out.println("  MCA= " + subStr[1] ); 
            MCA_MCA_cnf = subStr[1];

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[2] = str.substring(12); // Extrae string Fcontadores        
            //System.out.println("  Fcontadores= " + subStr[2] ); 
            Fcontadores_MCA_cnf = subStr[2];

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[3] = str.substring(9); // Extrae string Fmedidas        
            //System.out.println("  Fmedidas= " + subStr[3] ); 
            Fmedidas_MCA_cnf = subStr[3];

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[4] = str.substring(5); // Extrae string Flog        
            //System.out.println("  Flog= " + subStr[4] ); 
            Flog_MCA_cnf = subStr[4];

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[5] = str.substring(10); // Extrae string Intervalo  
            int sibInt0 = Integer.parseInt(subStr[5]);
            //System.out.println("  Intervalo= " + sibInt0 + " minutos" ); 
            intervalo_MCA_cnf = sibInt0;

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[6] = str.substring(16); // Extrae string Intervalo_largo        
            int sibInt1 = Integer.parseInt(subStr[6]);
            //System.out.println("  Intervalo_largo= " + sibInt1 + " horas" ); 
            intervalo_largo_MCA_cnf = sibInt1;

            str = br.readLine(); // Lee primera linea del fichero: la cabecera del fichero
            subStr[7] = str.substring(6); // Extrae string Intervalo_largo        
            //System.out.println("  IP router KNXnet/IP= " + subStr[7] ); 
            IPknx_MCA_cnf = subStr[7];

            br.close(); // Close del fichero
        } catch (IOException ex) {
            new IOException(ex.getMessage());
        }
    }

    /* Procedimiento principal "MCAinit()" del programa "MideCuandoArranca"
     * Tan pronto como se ejecuta:
     * (1) inicializa los intervalos de muestreo y backup,
     * (2) se conecta con el router IP,  
     * (3) lee el fichero de los contadores,
     * (4) configura los envíos periódicos de lectura de contadores 
     * (5) y lee las medidas enviadas desde dichos contadores
     * @author Domingo Benítez Díaz
     */
    private void MCAinit() {

        // Intervalo (en minutos) de muestreo de los envíos de lectura de los contadores domóticos: 1 minuto por defecto
        Integer intervalo = intervalo_MCA_cnf;
        // Intervalo (en horas) para guardar las medidas y eventos del bus en un fichero backup: 24 horas por defecto
        Integer intervalo_largo = intervalo_largo_MCA_cnf;
        // Nombre del fichero que almacena los contadores insertados en la base de datos
        String fichero_contadores_MCA = raiz_ficheros + Fmedidas_MCA_cnf;
        // System.out.println("Ruta completa fichero medidas= " + fichero_contadores ); 
        String error = "";

        try {
            // Creamos el fichero inicialmente, ya que si no lo hacemos y estuviera creado ya,
            // con la función escribeMensajeLog lo que hace es añadir texto al final
            PrintWriter writer = new PrintWriter(ficheroLOG);
            writer.close();
            // Ahora ya podemos utilizar la función escribeMensajeLog
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Hola, bienvenido a la aplicación SKoA \n");

            // Dirección IP del ordenador conectado a la instalación domótica KNX
            HPAI.setLocalIP(InetAddress.getByName(IPordenador));
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": IP del ordenador = " + IPordenador + "\n");

            InetSocketAddress isa = new InetSocketAddress(IPknx_MCA_cnf, 3671);
            //InetSocketAddress isa = new InetSocketAddress("192.168.1.51", 3671);
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": IP del Router EIBnet/IP = " + IPknx_MCA_cnf + "\n");
            //escribeMensajeLog(ficheroLOG,fecha_hora() + ": IP del Router EIBnet/IP = 192.168.1.51\n");

            // Realiza la conexión con el router EIBnet/IP
            tunnel = new CEMI_Connection(isa, new TunnellingConnectionType());
            if (tunnel != null) {
                escribeMensajeLog(ficheroLOG, fecha_hora() + ": Se ha establecido de manera correcta la conexión con el router EIBnet/IP \n");
                jlEstadoServ_res.setText("Conectado");
            }

            // Escucha el bus KNX
            tunnel.addFrameListener(new My_EICLEventListener(this));

            // Se lee el fichero de contadores *.dat
            VariableGlobal.Lcontadores = new PointList(raiz_ficheros + Fcontadores_MCA_cnf);
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Se ha realizado la lectura del fichero de "
                    + "Base de Datos de los contadores = "
                    + raiz_ficheros + Fcontadores_MCA_cnf + " \n");

            //  Se activa la lectura periódica con los siguientes valores:
            //  Intervalo de muestreo: 1 minuto (defecto), 
            //  Intervalo largo para backup: 6 horas (defecto), 
            //  Fichero de contadores para guardar las medidas: medidas_SKoA.log (defecto) 
            mainActivado.initVariable("on");

            LecturaPeriodica send = new LecturaPeriodica(this.getFrame(), true,
                    tunnel, fecha_hora(), mainActivado,
                    textArea, ficheroLOG, fichero_contadores);
            send.putLOG(fichero_contadores_MCA);
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Fichero donde se "
                    + "almacenan los resultados de los contadores = "
                    + fichero_contadores_MCA + "\n");

            // Mensajes que se almacenan en fichero LOG 
            escribeMensajeLog(ficheroLOG, fecha_hora()
                    + ": Intervalo de muestreo corto= " + intervalo + " (minutos)" + "\n");
            escribeMensajeLog(ficheroLOG, fecha_hora()
                    + ": Intervalo de muestreo largo (backup)= " + intervalo_largo + " (horas)" + "\n");
            /*escribeMensajeLog(ficheroLOG,fecha_hora() +
            ": variable global que activa lectura periódica, VG= " + 
            mainActivado.getVariable() + "\n");*/

            // Activación de Hilos t1 y t3 para la activación periódica de lecturas
            // "send" es la clase principal que mantiene el contenido de la variable global "Activado"
            send.t1 = new Th_ActivaEnvioPeriodico(intervalo, send);
            send.t1.start(); // Lanza el hilo de activación periódico "Th_ActivaEnvioPeriodico"
            //System.out.println(fecha_hora() + ": Thread Th1 para muestreo periódico lanzado"); // mensaje por el terminal
            escribeMensajeLog(ficheroLOG, fecha_hora()
                    + ": Hilo Th1 ejecutado, muestreo periódico activado" + "\n");

            // "this" es la clase principal que mantiene el contenido de la variable global "Activado"
            send.t3 = new Th_ActivaBackUp(intervalo_largo, send, textArea, fichero_contadores);
            send.t3.start(); // Lanza el hilo de activación periódico "Th_ActivaEnvioPeriodico"
            //System.out.println(fecha_hora() + ": Thread Th3 para BackUp lanzado"); // mensaje por el terminal
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Hilo Th3 ejecutado, copia de seguridad (backup) configurada" + "\n");

            // Mensajes que aparecen en la ventana del log de la aplicación (los mismos que para el fichero LOG)
            synchronized (textArea) {
                textArea.append(fecha_hora() + ": Hola, bienvenido a la aplicación SKoA \n");
                textArea.append(fecha_hora() + ": IP del ordenador = " + IPordenador + "\n");
                textArea.append(fecha_hora() + ": IP del Router EIBnet/IP = " + IPknx_MCA_cnf + "\n");
                textArea.append(fecha_hora() + ": Se ha establecido de manera correcta la conexión con el router EIBnet/IP \n");
                //textArea.append(fecha_hora() + ": Se ha realizado la lectura del fichero de contadores = " + raiz_ficheros + Fcontadores_MCA_cnf + " \n");
                textArea.append(fecha_hora() + ": Fichero donde se almacenan los resultados de los contadores = " + fichero_contadores_MCA + "\n");
                textArea.append(fecha_hora() + ": Intervalo de muestreo corto= " + intervalo + " (minutos)\n");
                textArea.append(fecha_hora() + ": Intervalo de muestreo largo (backup)= " + intervalo_largo + " (horas)\n");
                //textArea.append(fecha_hora() + ": VG= " + mainActivado.getVariable() + "\n" );
                textArea.append(fecha_hora() + ": Hilo Th1 ejecutado, muestreo periódico activado\n");
                textArea.append(fecha_hora() + ": Hilo Th3 ejecutado, copia de seguridad (backup) configurada\n");
            }
        } catch (Exception ex) {
            SkoaMain.showException(ex.getMessage());
            new EICLException(error);
        }
    }

    /* Procedimiento similar a "MCAinit()"
     * Tan pronto como se ejecuta:
     * (1) inicializa los intervalos de muestreo y backup,
     * (2) se conecta con el router IP, //Comentado para que el usuario haga la conexión
     * (3) lee el fichero de los contadores guardado en MCA,
     * (4) configura los envíos periódicos de lectura de contadores
     * @author Estefanía Tramunt Rubio.
     */
    private void MCAinit_manual() {
        // Intervalo (en minutos) de muestreo de los envíos de lectura de los contadores domóticos: 1 minuto por defecto
        Integer intervalo = intervalo_MCA_cnf;
        // Intervalo (en horas) para guardar las medidas y eventos del bus en un fichero backup: 24 horas por defecto
        Integer intervalo_largo = intervalo_largo_MCA_cnf;
        // Nombre del fichero que almacena los contadores insertados en la base de datos
        String fichero_contadores_MCA = raiz_ficheros + Fmedidas_MCA_cnf;
        String error = "";
        try {
            // Creamos el fichero inicialmente, ya que si no lo hacemos y estuviera creado ya,
            // con la función escribeMensajeLog lo que hace es añadir texto al final
            PrintWriter writer = new PrintWriter(ficheroLOG);
            writer.close();
            // Ahora ya podemos utilizar la función escribeMensajeLog
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Hola, bienvenido a la aplicación SKoA \n");
            // Dirección IP del ordenador conectado a la instalación domótica KNX
            HPAI.setLocalIP(InetAddress.getByName(IPordenador));
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": IP del ordenador = " + IPordenador + "\n");
            InetSocketAddress isa = new InetSocketAddress(IPknx_MCA_cnf, 3671);
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": IP del Router EIBnet/IP = " + IPknx_MCA_cnf + "\n");
            //escribeMensajeLog(ficheroLOG,fecha_hora() + ": IP del Router EIBnet/IP = 192.168.1.51\n");

            // Realiza la conexión con el router EIBnet/IP
            /*tunnel = new CEMI_Connection(isa, new TunnellingConnectionType());
            if (tunnel != null){
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Se ha establecido de manera correcta la conexión con el router EIBnet/IP \n");
            jlEstadoServ_res.setText("Conectado");
            }
             */
            // Escucha el bus KNX
            tunnel.addFrameListener(new My_EICLEventListener(this));

            // Se lee el fichero de contadores *.dat
            /*VariableGlobal.Lcontadores = new PointList(raiz_ficheros + Fcontadores_MCA_cnf);*/
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Se ha realizado la lectura del fichero del "
                    + "fichero de contadores = "
                    + raiz_ficheros + Fcontadores_MCA_cnf + " \n");

            //  Se activa la lectura periódica con los siguientes valores:
            //  Intervalo de muestreo: 1 minuto (defecto),
            //  Intervalo largo para backup: 6 horas (defecto),
            //  Fichero de contadores para guardar las medidas: medidas_SKoA.log (defecto)
            mainActivado.initVariable("on");
            LecturaPeriodica send = new LecturaPeriodica(this.getFrame(), true,
                    tunnel, fecha_hora(), mainActivado,
                    textArea, ficheroLOG, fichero_contadores);
            send.putLOG(fichero_contadores_MCA);
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Fichero donde se "
                    + "almacenan los resultados de los contadores = "
                    + fichero_contadores_MCA + "\n");

            // Mensajes que se almacenan en fichero LOG
            escribeMensajeLog(ficheroLOG, fecha_hora()
                    + ": Intervalo de muestreo corto= " + intervalo + " (minutos)" + "\n");
            escribeMensajeLog(ficheroLOG, fecha_hora()
                    + ": Intervalo de muestreo largo (backup)= " + intervalo_largo + " (horas)" + "\n");

            // Activación de Hilos t1 y t3 para la activación periódica de lecturas
            // "send" es la clase principal que mantiene el contenido de la variable global "Activado"
            //send.t1 = new Th_ActivaEnvioPeriodico(intervalo, send);
            //send.t1.start(); // Lanza el hilo de activación periódico "Th_ActivaEnvioPeriodico"
            //escribeMensajeLog(ficheroLOG,fecha_hora() +
            //": Hilo Th1 ejecutado, muestreo periódico activado" + "\n");

            // "this" es la clase principal que mantiene el contenido de la variable global "Activado"
            send.t3 = new Th_ActivaBackUp(intervalo_largo, send, textArea, fichero_contadores);
            send.t3.start(); // Lanza el hilo de activación periódico "Th_ActivaEnvioPeriodico"
            escribeMensajeLog(ficheroLOG, fecha_hora() + ": Hilo Th3 ejecutado, copia de seguridad (backup) configurada" + "\n");

            // Mensajes que aparecen en la ventana del log de la aplicación (los mismos que para el fichero LOG)
            synchronized (textArea) {
                textArea.append(fecha_hora() + ": Hola, bienvenido a la aplicación SKoA \n");
                textArea.append(fecha_hora() + ": IP del ordenador = " + IPordenador + "\n");
                textArea.append(fecha_hora() + ": IP del Router EIBnet/IP = " + IPknx_MCA_cnf + "\n");
                textArea.append(fecha_hora() + ": Se ha establecido de manera correcta la conexión con el router EIBnet/IP \n");
                textArea.append(fecha_hora() + ": Se ha realizado la lectura del fichero de contadores = " + raiz_ficheros + Fcontadores_MCA_cnf + " \n");
                //El fichero de contadores se muestra al conectar al bus, por si se ha abierto otro.
                textArea.append(fecha_hora() + ": Fichero donde se almacenan los resultados de los contadores = " + fichero_contadores_MCA + "\n");
                textArea.append(fecha_hora() + ": Intervalo de muestreo corto= " + intervalo + " (minutos)\n");
                textArea.append(fecha_hora() + ": Intervalo de muestreo largo (backup)= " + intervalo_largo + " (horas)\n");
                textArea.append(fecha_hora() + ": Hilo Th1 ejecutado, muestreo periódico activado\n");
                textArea.append(fecha_hora() + ": Hilo Th3 ejecutado, copia de seguridad (backup) configurada\n");
            }
        } catch (Exception ex) {
            SkoaMain.showException(ex.getMessage());
            new EICLException(error);
        }
    }

    /*
     * Rutina que centraliza las escrituras a un fichero LOG
     * @author Domingo Benítez Díaz
     * @param _ficheroLOG El fichero LOG
     * @param _mensajeLOG El mensaje que se escribirá en el fichero
     */
    public static void escribeMensajeLog(String _ficheroLOG, String _mensajeLog) {
        try {
            // Abrimos el FICHERO DE ACCESO ALEATORIO en cada escritura
            RandomAccessFile miRAFile = new RandomAccessFile(_ficheroLOG, "rw");
            // Nos vamos al final del fichero
            miRAFile.seek(miRAFile.length());
            // Incorporamos un mensaje obtenido del bus KNX al fichero miRAFile    
            miRAFile.writeBytes(_mensajeLog);
            // Cerramos el fichero
            miRAFile.close();
        } // Posibles excepciones
        catch (IOException ex) {
            new IOException(ex.getMessage());
        }
    }

    /* 
     * Función que proporciona una string con la fecha y hora actual
     * @author Domingo Benítez Díaz
     */
    public String fecha_hora() {
        Calendar calendario = Calendar.getInstance();
        String _fecha_hora = calendario.getInstance().getTime().toString();
        return _fecha_hora;
    }

    /*
     * Función que proporciona una string con la fecha actual en formato día/mes/año
     * @author David Monné Chávez
     */
    public String fecha() {
        Calendar calendario = Calendar.getInstance();
        String _fecha = calendario.get(calendario.DATE) + "/" + (calendario.get(calendario.MONTH) + 1) + "/" + calendario.get(calendario.YEAR);
        return _fecha;
    }

    /*
     * Función que proporciona una string con la hora actual en formato hora:mins:segs
     * @author David Monné Chávez
     */
    public String hora() {

        Calendar calendario = Calendar.getInstance();
        String _hora = calendario.get(calendario.HOUR_OF_DAY) + ":" + calendario.get(calendario.MINUTE) + ":" + calendario.get(calendario.SECOND);
        return _hora;
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SkoaApp.getApplication().getMainFrame();
            aboutBox = new About(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SkoaApp.getApplication().show(aboutBox);
    }

    // Procedimiento para mostrar la ventana de conexión
    // AHORA MISMO CREO QUE YA NO ES NECESARIO ÉSTE PQ TENEMOS EL ACTIONPERFORMED
    // ASOCIADO AL BOTÓN CONECTAR, PERO DE MOMENTO DEJAREMOS EL CÓDIGO COMENTADO
    @Action
    public void showConectar() {
        /*if (conectar == null) {
        JFrame mainFrame = MaipezApp.getApplication().getMainFrame();
        conectar = new Conectar(mainFrame, "conectado a", true,
        factory, fecha_hora());
        conectar.setLocationRelativeTo(mainFrame);
        }
        MaipezApp.getApplication().show(conectar);*/
    }

    //----------------------- NO LO USAMOS ------------------------------------
    // Procedimiento para mostrar la ventana de la administración de puntos
    /*
    @Action
    public void showAdminPtos() {
    if (adminptos == null) {
    JFrame mainFrame = MaipezApp.getApplication().getMainFrame();
    adminptos = new AdministrarPuntos(mainFrame,true);
    adminptos.setLocationRelativeTo(mainFrame);
    }
    MaipezApp.getApplication().show(adminptos);
    }
     */
    //----------------------- NO LO USAMOS ------------------------------------
    // Procedimiento para mostrar la ventana de la lectura de puntos
    @Action
    public void showLecturaPtos() {
        if (leer == null) {
            JFrame mainFrame = SkoaApp.getApplication().getMainFrame();
            leer = new Leer(mainFrame, true);
            leer.setLocationRelativeTo(mainFrame);
        }
        SkoaApp.getApplication().show(leer);
    }

    /*
    // Procedimiento para mostrar la ventana de la escritura de puntos
    @Action
    public void showEscrituraPtos() {
    if (escribir == null) {
    JFrame mainFrame = MaipezApp.getApplication().getMainFrame();
    escribir = new Escribir(mainFrame,true);
    escribir.setLocationRelativeTo(mainFrame);
    }
    MaipezApp.getApplication().show(escribir);
    }*/
    // Procedimiento para mostrar la ventana de administración de contadores
    @Action
    public void showAdminConts() {
        // En principio ahora ya no es necesario pq lo lanzamos desde su botón asociado
        /*
        if (admincont == null) {
        JFrame mainFrame = MaipezApp.getApplication().getMainFrame();
        admincont = new AdministrarContadores(mainFrame,true);
        admincont.setLocationRelativeTo(mainFrame);
        }
        MaipezApp.getApplication().show(admincont);*/
    }

    // Procedimiento para mostrar la ventana de lectura de contadores
    @Action
    public void showLeerConts() {
        String error = "";
        // Variable auxiliar: nos permitirá elegir para la lectura o bien los contadores insertados
        // por el usuario(0), o los contadores que indique el fichero de contadores(1)
        int aux = 0;

        try {
            if (aux == 1) {
                VariableGlobal.Lcontadores = new PointList(raiz_ficheros + Fcontadores_MCA_cnf);
            }
        } catch (EICLException ex) {
            showException(ex.getMessage());
        }

        // Si se ha establecido una conexión y hay contadores registrados en la base de datos
        if (tunnel != null && VariableGlobal.Lcontadores.getAllPoints().length > 0) {
            try {
                // Lanzamos la ventana donde se permitirá al usuario elegir el contador que desee para consultar su lectura
                if (leercont == null) {
                    JFrame mainFrame = SkoaApp.getApplication().getMainFrame();
                    leercont = new LeerContador(mainFrame, true, tunnel, fecha_hora());
                    leercont.setLocationRelativeTo(mainFrame);
                }
                SkoaApp.getApplication().show(leercont);
            } catch (Exception ex) {
                SkoaMain.showException(ex.getMessage());
                new EICLException(error);
            }
        }
        // LLegados a este punto, la ejecución se detiene hasta que se pulse un botón
        // System.out.println("prueba");
    }

    // Procedimiento para mostrar la ventana de lectura periódica
    @Action
    public void showLecturaPeriodica() {

        // Si ejecutamos la opción "Lectura Periódica" desde la interfaz, tendrá en cuenta los contadores
        // que haya en la variable global Lcontadores, NO los que hay en el fichero de la base de datos
    /* -----------------------------------------------------------------------------------
        try {
        // Definimos el fichero con el listado de contadores
        VariableGlobal.Lcontadores = new PointList(raiz_ficheros + Fcontadores_MCA_cnf);
        } catch (EICLException ex) {
        showException(ex.getMessage());
        }
        ------------------------------------------------------------------------------------- */

        // Si se ha establecido una conexión y hay contadores insertados en la base de datos
        if (tunnel != null && VariableGlobal.Lcontadores.getAllPoints().length > 0) {

            // Preparamos una ventana del tipo "LecturaPeriodica"
            LecturaPeriodica send = new LecturaPeriodica(this.getFrame(), true,
                    tunnel, fecha_hora(), mainActivado, textArea, ficheroLOG, fichero_contadores);

            // Centramos la ventana
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = send.getSize();
            if (frameSize.height > screenSize.height) {
                frameSize.height = screenSize.height;
            }
            if (frameSize.width > screenSize.width) {
                frameSize.width = screenSize.width;
            }
            send.setLocation((screenSize.width - frameSize.width) / 2,
                    (screenSize.height - frameSize.height) / 2);

            // Y la mostramos por pantalla
            send.setVisible(true);
            fichero_contadores = send.getLOG();

            /* --------------------------- NO HACE FALTA ----------------------------
            // Almacenamos en una ristra un mensaje advirtiendo del estado de la lectura periódica
            // Y mostrando el ficheron donde se guardarán las medidas de los contadores
            String mensajeLog2 = fecha_hora() +
            ": La activación de la lectura periódica de contadores está = " + 
            send.getActivado() + "\n" + fecha_hora() +
            ": Fichero donde se guardan las medidas de los contadores = " + 
            send.getLOG() + "\n";
            
            // Mostramos la infomación en el LOG de la aplicación
            synchronized (textArea) {
            textArea.append(mensajeLog2);
            textArea.setCaretPosition(textArea.getText().length());
            // Y también la escribimos en el fichero de Log
            escribeMensajeLog(ficheroLOG, mensajeLog2);
            }
            
            No hace falta pq ya mostramos esa información a través de los hilos en 
            "LecturaPeriodica.java"
            ----------------------------- NO HACE FALTA -----------------------------*/

        }
    }

    // Procedimiento para mostrar la ventana de configuración
    @Action
    public void showConfiguracion() {
        if (config == null) {
            JFrame mainFrame = SkoaApp.getApplication().getMainFrame();
            config = new Config(mainFrame, true);
            config.setLocationRelativeTo(mainFrame);
        }
        SkoaApp.getApplication().show(config);
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jpPanelSup = new javax.swing.JPanel();
        jbConectar = new javax.swing.JButton();
        jbDesconectar = new javax.swing.JButton();
        jlEstadoServ = new javax.swing.JLabel();
        jlMaipez = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        fecha_res = new javax.swing.JLabel();
        hora = new javax.swing.JLabel();
        hora_res = new javax.swing.JLabel();
        jlEstadoServ_res = new javax.swing.JLabel();
        tpPestanyas = new javax.swing.JTabbedPane();
        tpPlantas = new javax.swing.JTabbedPane();
        pContadores = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea2 = new javax.swing.JTextArea();
        pAplicacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        cargarMenuItem = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        cerrarMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        conectarMenuItem = new javax.swing.JMenuItem();
        desconectarMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        jXmlMenu = new javax.swing.JMenuItem();
        jContadoresMenu = new javax.swing.JMenu();
        jAbrirBDItem = new javax.swing.JMenuItem();
        jInsertarItem = new javax.swing.JMenuItem();
        jGuardarBDItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        jLeerContItem = new javax.swing.JMenuItem();
        jLectPeriodItem = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jGrafsMenu = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        verConfigItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jpPanelSup.setName("jpPanelSup"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(SkoaMain.class, this);
        jbConectar.setAction(actionMap.get("showConectar")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(SkoaMain.class);
        jbConectar.setText(resourceMap.getString("jbConectar.text")); // NOI18N
        jbConectar.setName("jbConectar"); // NOI18N
        jbConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConectarActionPerformed(evt);
            }
        });

        jbDesconectar.setText(resourceMap.getString("jbDesconectar.text")); // NOI18N
        jbDesconectar.setToolTipText(resourceMap.getString("jbDesconectar.toolTipText")); // NOI18N
        jbDesconectar.setName("jbDesconectar"); // NOI18N
        jbDesconectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDesconectarActionPerformed(evt);
            }
        });

        jlEstadoServ.setFont(resourceMap.getFont("jlEstadoServ.font")); // NOI18N
        jlEstadoServ.setText(resourceMap.getString("jlEstadoServ.text")); // NOI18N
        jlEstadoServ.setName("jlEstadoServ"); // NOI18N

        jlMaipez.setBackground(resourceMap.getColor("jlMaipez.background")); // NOI18N
        jlMaipez.setFont(resourceMap.getFont("jlMaipez.font")); // NOI18N
        jlMaipez.setForeground(resourceMap.getColor("jlMaipez.foreground")); // NOI18N
        jlMaipez.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMaipez.setText(resourceMap.getString("jlMaipez.text")); // NOI18N
        jlMaipez.setName("jlMaipez"); // NOI18N
        jlMaipez.setOpaque(true);

        fecha.setFont(resourceMap.getFont("fecha.font")); // NOI18N
        fecha.setText(resourceMap.getString("fecha.text")); // NOI18N
        fecha.setName("fecha"); // NOI18N

        fecha_res.setFont(resourceMap.getFont("fecha_res.font")); // NOI18N
        fecha_res.setText(resourceMap.getString("fecha_res.text")); // NOI18N
        fecha_res.setName("fecha_res"); // NOI18N

        hora.setFont(resourceMap.getFont("hora.font")); // NOI18N
        hora.setText(resourceMap.getString("hora.text")); // NOI18N
        hora.setName("hora"); // NOI18N

        hora_res.setFont(resourceMap.getFont("hora_res.font")); // NOI18N
        hora_res.setText(resourceMap.getString("hora_res.text")); // NOI18N
        hora_res.setName("hora_res"); // NOI18N

        jlEstadoServ_res.setFont(resourceMap.getFont("jlEstadoServ_res.font")); // NOI18N
        jlEstadoServ_res.setText(resourceMap.getString("jlEstadoServ_res.text")); // NOI18N
        jlEstadoServ_res.setName("jlEstadoServ_res"); // NOI18N

        javax.swing.GroupLayout jpPanelSupLayout = new javax.swing.GroupLayout(jpPanelSup);
        jpPanelSup.setLayout(jpPanelSupLayout);
        jpPanelSupLayout.setHorizontalGroup(
            jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPanelSupLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbConectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbDesconectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addComponent(jlMaipez, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpPanelSupLayout.createSequentialGroup()
                        .addComponent(hora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hora_res, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE))
                    .addGroup(jpPanelSupLayout.createSequentialGroup()
                        .addComponent(jlEstadoServ)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlEstadoServ_res))
                    .addGroup(jpPanelSupLayout.createSequentialGroup()
                        .addComponent(fecha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fecha_res, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jpPanelSupLayout.setVerticalGroup(
            jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpPanelSupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpPanelSupLayout.createSequentialGroup()
                        .addComponent(jbConectar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDesconectar))
                    .addComponent(jlMaipez, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpPanelSupLayout.createSequentialGroup()
                        .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlEstadoServ)
                            .addComponent(jlEstadoServ_res))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fecha)
                            .addComponent(fecha_res, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hora)
                            .addComponent(hora_res, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tpPestanyas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tpPestanyas.setName("tpPestanyas"); // NOI18N

        tpPlantas.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tpPlantas.setName("tpPlantas"); // NOI18N
        tpPestanyas.addTab(resourceMap.getString("tpPlantas.TabConstraints.tabTitle"), tpPlantas); // NOI18N

        pContadores.setName("pContadores"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        textArea2.setColumns(20);
        textArea2.setRows(5);
        textArea2.setName("textArea2"); // NOI18N
        jScrollPane1.setViewportView(textArea2);

        javax.swing.GroupLayout pContadoresLayout = new javax.swing.GroupLayout(pContadores);
        pContadores.setLayout(pContadoresLayout);
        pContadoresLayout.setHorizontalGroup(
            pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1320, Short.MAX_VALUE)
                    .addGroup(pContadoresLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(1079, Short.MAX_VALUE))))
        );
        pContadoresLayout.setVerticalGroup(
            pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tpPestanyas.addTab(resourceMap.getString("pContadores.TabConstraints.tabTitle"), pContadores); // NOI18N

        pAplicacion.setName("pAplicacion"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setName("textArea"); // NOI18N
        jScrollPane2.setViewportView(textArea);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout pAplicacionLayout = new javax.swing.GroupLayout(pAplicacion);
        pAplicacion.setLayout(pAplicacionLayout);
        pAplicacionLayout.setHorizontalGroup(
            pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAplicacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(340, Short.MAX_VALUE))
        );
        pAplicacionLayout.setVerticalGroup(
            pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pAplicacionLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        tpPestanyas.addTab(resourceMap.getString("pAplicacion.TabConstraints.tabTitle"), pAplicacion); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tpPestanyas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpPanelSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jpPanelSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tpPestanyas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(resourceMap.getIcon("jMenuItem1.icon")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setToolTipText(resourceMap.getString("jMenuItem1.toolTipText")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        cargarMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        cargarMenuItem.setIcon(resourceMap.getIcon("cargarMenuItem.icon")); // NOI18N
        cargarMenuItem.setText(resourceMap.getString("cargarMenuItem.text")); // NOI18N
        cargarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(cargarMenuItem);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(resourceMap.getIcon("jMenuItem2.icon")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem2);

        cerrarMenuItem.setIcon(resourceMap.getIcon("cerrarMenuItem.icon")); // NOI18N
        cerrarMenuItem.setText(resourceMap.getString("cerrarMenuItem.text")); // NOI18N
        cerrarMenuItem.setToolTipText(resourceMap.getString("cerrarMenuItem.toolTipText")); // NOI18N
        cerrarMenuItem.setName("cerrarMenuItem"); // NOI18N
        cerrarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(cerrarMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        conectarMenuItem.setAction(actionMap.get("showConectar")); // NOI18N
        conectarMenuItem.setIcon(resourceMap.getIcon("conectarMenuItem.icon")); // NOI18N
        conectarMenuItem.setText(resourceMap.getString("conectarMenuItem.text")); // NOI18N
        conectarMenuItem.setName("conectarMenuItem"); // NOI18N
        conectarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(conectarMenuItem);

        desconectarMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        desconectarMenuItem.setIcon(resourceMap.getIcon("desconectarMenuItem.icon")); // NOI18N
        desconectarMenuItem.setText(resourceMap.getString("desconectarMenuItem.text")); // NOI18N
        desconectarMenuItem.setToolTipText(resourceMap.getString("desconectarMenuItem.toolTipText")); // NOI18N
        desconectarMenuItem.setName("desconectarMenuItem"); // NOI18N
        desconectarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desconectarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(desconectarMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(resourceMap.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        optionsMenu.setText(resourceMap.getString("optionsMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        jXmlMenu.setIcon(resourceMap.getIcon("jXmlMenu.icon")); // NOI18N
        jXmlMenu.setText(resourceMap.getString("jXmlMenu.text")); // NOI18N
        jXmlMenu.setToolTipText(resourceMap.getString("jXmlMenu.toolTipText")); // NOI18N
        jXmlMenu.setName("jXmlMenu"); // NOI18N
        jXmlMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXmlMenuActionPerformed(evt);
            }
        });
        optionsMenu.add(jXmlMenu);

        jContadoresMenu.setIcon(resourceMap.getIcon("jContadoresMenu.icon")); // NOI18N
        jContadoresMenu.setText(resourceMap.getString("jContadoresMenu.text")); // NOI18N
        jContadoresMenu.setName("jContadoresMenu"); // NOI18N

        jAbrirBDItem.setIcon(resourceMap.getIcon("jAbrirBDItem.icon")); // NOI18N
        jAbrirBDItem.setText(resourceMap.getString("jAbrirBDItem.text")); // NOI18N
        jAbrirBDItem.setToolTipText(resourceMap.getString("jAbrirBDItem.toolTipText")); // NOI18N
        jAbrirBDItem.setName("jAbrirBDItem"); // NOI18N
        jAbrirBDItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAbrirBDItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jAbrirBDItem);

        jInsertarItem.setAction(actionMap.get("showAdminConts")); // NOI18N
        jInsertarItem.setText(resourceMap.getString("jInsertarItem.text")); // NOI18N
        jInsertarItem.setName("jInsertarItem"); // NOI18N
        jInsertarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jInsertarItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jInsertarItem);

        jGuardarBDItem.setIcon(resourceMap.getIcon("jGuardarBDItem.icon")); // NOI18N
        jGuardarBDItem.setText(resourceMap.getString("jGuardarBDItem.text")); // NOI18N
        jGuardarBDItem.setToolTipText(resourceMap.getString("jGuardarBDItem.toolTipText")); // NOI18N
        jGuardarBDItem.setName("jGuardarBDItem"); // NOI18N
        jGuardarBDItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGuardarBDItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jGuardarBDItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jContadoresMenu.add(jSeparator3);

        jLeerContItem.setAction(actionMap.get("showLeerConts")); // NOI18N
        jLeerContItem.setText(resourceMap.getString("jLeerContItem.text")); // NOI18N
        jLeerContItem.setName("jLeerContItem"); // NOI18N
        jLeerContItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jLeerContItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jLeerContItem);

        jLectPeriodItem.setAction(actionMap.get("showLecturaPeriodica")); // NOI18N
        jLectPeriodItem.setText(resourceMap.getString("jLectPeriodItem.text")); // NOI18N
        jLectPeriodItem.setName("jLectPeriodItem"); // NOI18N
        jContadoresMenu.add(jLectPeriodItem);

        jMenuItem3.setIcon(resourceMap.getIcon("jMenuItem3.icon")); // NOI18N
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setToolTipText(resourceMap.getString("jMenuItem3.toolTipText")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jMenuItem3);

        optionsMenu.add(jContadoresMenu);

        jGrafsMenu.setIcon(resourceMap.getIcon("jGrafsMenu.icon")); // NOI18N
        jGrafsMenu.setText(resourceMap.getString("jGrafsMenu.text")); // NOI18N
        jGrafsMenu.setToolTipText(resourceMap.getString("jGrafsMenu.toolTipText")); // NOI18N
        jGrafsMenu.setName("jGrafsMenu"); // NOI18N
        jGrafsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGrafsMenuActionPerformed(evt);
            }
        });
        optionsMenu.add(jGrafsMenu);

        menuBar.add(optionsMenu);

        helpMenu.setAction(actionMap.get("showConfiguracion")); // NOI18N
        helpMenu.setIcon(resourceMap.getIcon("helpMenu.icon")); // NOI18N
        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N
        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });

        verConfigItem.setIcon(resourceMap.getIcon("verConfigItem.icon")); // NOI18N
        verConfigItem.setText(resourceMap.getString("verConfigItem.text")); // NOI18N
        verConfigItem.setToolTipText(resourceMap.getString("verConfigItem.toolTipText")); // NOI18N
        verConfigItem.setName("verConfigItem"); // NOI18N
        verConfigItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verConfigItemActionPerformed(evt);
            }
        });
        helpMenu.add(verConfigItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setToolTipText(resourceMap.getString("aboutMenuItem.toolTipText")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        jMenuItem4.setIcon(resourceMap.getIcon("jMenuItem4.icon")); // NOI18N
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        helpMenu.add(jMenuItem4);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

// Procedimiento encargado de inicializar las variables y de leer el ficheor XML para distribuir de forma adecuada determinados elementos en la GUI     
    private void initComponentsXML() {
        
        mainPanel = new javax.swing.JPanel();
        jpPanelSup = new javax.swing.JPanel();
        jbConectar = new javax.swing.JButton();
        jbDesconectar = new javax.swing.JButton();
        jlEstadoServ = new javax.swing.JLabel();
        fecha = new javax.swing.JLabel();
        tpPestanyas = new javax.swing.JTabbedPane();
        tpPlantas = new javax.swing.JTabbedPane();
        pContadores = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea2 = new javax.swing.JTextArea();
        pAplicacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        cargarMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        conectarMenuItem = new javax.swing.JMenuItem();
        desconectarMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();
        jXmlMenu = new javax.swing.JMenuItem();
        // Parte correspondiente a la opción quitada "Listado de puntos"
        //jListadoMenu = new javax.swing.JMenu();
        //jAbrirItem = new javax.swing.JMenuItem();
        //jAdminItem = new javax.swing.JMenuItem();
        //jGuardarItem = new javax.swing.JMenuItem();
        // ------------------------------------------------------------------
        jContadoresMenu = new javax.swing.JMenu();
        jAbrirBDItem = new javax.swing.JMenuItem();
        jInsertarItem = new javax.swing.JMenuItem();
        jGuardarBDItem = new javax.swing.JMenuItem();
        jLeerContItem = new javax.swing.JMenuItem();
        jLectPeriodItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        verConfigItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        //statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        //statusMessageLabel = new javax.swing.JLabel();
        //statusAnimationLabel = new javax.swing.JLabel();
        //progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jpPanelSup.setName("jpPanelSup"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getActionMap(SkoaMain.class, this);
        jbConectar.setAction(actionMap.get("showConectar")); // NOI18N
        jbConectar.setName("jbConectar"); // NOI18N
        jbConectar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConectarActionPerformed(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(SkoaMain.class);
        jbDesconectar.setText(resourceMap.getString("jbDesconectar.text")); // NOI18N
        jbDesconectar.setName("jbDesconectar"); // NOI18N

        jlEstadoServ.setFont(resourceMap.getFont("jlEstadoServ.font")); // NOI18N
        jlEstadoServ.setText(resourceMap.getString("jlEstadoServ.text")); // NOI18N
        jlEstadoServ.setName("jlEstadoServ"); // NOI18N

        // ------ Código a añadir en caso de futuras modificaciones ----------
        jlMaipez.setBackground(resourceMap.getColor("jlMaipez.background"));
        jlMaipez.setFont(resourceMap.getFont("jlMaipez.font")); // NOI18N
        jlMaipez.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMaipez.setText(resourceMap.getString("jlMaipez.text"));
        jlMaipez.setName("jlMaipez");
        jlMaipez.setOpaque(true);
        // ------------------------------------------------------------------

        fecha.setFont(resourceMap.getFont("fecha.font")); // NOI18N
        fecha.setText(resourceMap.getString("fecha.text")); // NOI18N
        fecha.setName("fecha"); // NOI18N

        javax.swing.GroupLayout jpPanelSupLayout = new javax.swing.GroupLayout(jpPanelSup);
        jpPanelSup.setLayout(jpPanelSupLayout);
        jpPanelSupLayout.setHorizontalGroup(
                jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpPanelSupLayout.createSequentialGroup().addGap(18, 18, 18).addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jbConectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jbDesconectar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(34, 34, 34).addComponent(jlMaipez, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(34, 34, 34).addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jlEstadoServ).addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(274, 274, 274)));
        jpPanelSupLayout.setVerticalGroup(
                jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpPanelSupLayout.createSequentialGroup().addContainerGap().addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpPanelSupLayout.createSequentialGroup().addComponent(jbConectar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbDesconectar)).addGroup(jpPanelSupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpPanelSupLayout.createSequentialGroup().addComponent(jlEstadoServ).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(fecha).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(jlMaipez, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        tpPestanyas.setName("tpPestanyas"); // NOI18N

        tpPlantas.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tpPlantas.setName("tpPlantas"); // NOI18N
        tpPestanyas.addTab(resourceMap.getString("tpPlantas.TabConstraints.tabTitle"), tpPlantas); // NOI18N

        pContadores.setName("pContadores"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        textArea2.setColumns(20);
        textArea2.setRows(5);
        textArea2.setName("taContadores"); // NOI18N
        jScrollPane1.setViewportView(textArea2);

        javax.swing.GroupLayout pContadoresLayout = new javax.swing.GroupLayout(pContadores);
        pContadores.setLayout(pContadoresLayout);
        pContadoresLayout.setHorizontalGroup(
                pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pContadoresLayout.createSequentialGroup().addContainerGap().addGroup(pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1017, Short.MAX_VALUE).addGroup(pContadoresLayout.createSequentialGroup().addComponent(jLabel2).addContainerGap(856, Short.MAX_VALUE)))));
        pContadoresLayout.setVerticalGroup(
                pContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pContadoresLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(25, Short.MAX_VALUE)));

        tpPestanyas.addTab(resourceMap.getString("pContadores.TabConstraints.tabTitle"), pContadores); // NOI18N

        pAplicacion.setName("pAplicacion"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setName("jTextArea1"); // NOI18N
        jScrollPane2.setViewportView(textArea);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout pAplicacionLayout = new javax.swing.GroupLayout(pAplicacion);
        pAplicacion.setLayout(pAplicacionLayout);
        pAplicacionLayout.setHorizontalGroup(
                pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pAplicacionLayout.createSequentialGroup().addContainerGap().addGroup(pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addContainerGap(37, Short.MAX_VALUE)));
        pAplicacionLayout.setVerticalGroup(
                pAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pAplicacionLayout.createSequentialGroup().addGap(12, 12, 12).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(24, Short.MAX_VALUE)));

        tpPestanyas.addTab(resourceMap.getString("pAplicacion.TabConstraints.tabTitle"), pAplicacion); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jpPanelSup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE).addComponent(tpPestanyas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1014, Short.MAX_VALUE)).addContainerGap(46, Short.MAX_VALUE)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addComponent(jpPanelSup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tpPestanyas, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE).addContainerGap()));

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(resourceMap.getIcon("jMenuItem1.icon")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        cargarMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        cargarMenuItem.setIcon(resourceMap.getIcon("cargarMenuItem.icon")); // NOI18N
        cargarMenuItem.setText(resourceMap.getString("cargarMenuItem.text")); // NOI18N
        cargarMenuItem.setName("cargarMenuItem"); // NOI18N
        cargarMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cargarMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(cargarMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        conectarMenuItem.setAction(actionMap.get("showConectar")); // NOI18N
        conectarMenuItem.setIcon(resourceMap.getIcon("conectarMenuItem.icon")); // NOI18N
        conectarMenuItem.setName("conectarMenuItem"); // NOI18N
        fileMenu.add(conectarMenuItem);

        desconectarMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        desconectarMenuItem.setIcon(resourceMap.getIcon("desconectarMenuItem.icon")); // NOI18N
        desconectarMenuItem.setText(resourceMap.getString("desconectarMenuItem.text")); // NOI18N
        desconectarMenuItem.setToolTipText(resourceMap.getString("desconectarMenuItem.toolTipText")); // NOI18N
        desconectarMenuItem.setName("desconectarMenuItem"); // NOI18N
        fileMenu.add(desconectarMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        optionsMenu.setText(resourceMap.getString("optionsMenu.text")); // NOI18N
        optionsMenu.setName("optionsMenu"); // NOI18N

        jXmlMenu.setIcon(resourceMap.getIcon("jXmlMenu.icon")); // NOI18N
        jXmlMenu.setText(resourceMap.getString("jXmlMenu.text")); // NOI18N
        jXmlMenu.setName("jXmlMenu"); // NOI18N
        optionsMenu.add(jXmlMenu);

        // Parte correspondiente a la opción quitada "Listado de puntos"
        /*
        jListadoMenu.setText(resourceMap.getString("jListadoMenu.text")); // NOI18N
        jListadoMenu.setName("jListadoMenu"); // NOI18N
        
        jAbrirItem.setIcon(resourceMap.getIcon("jAbrirItem.icon")); // NOI18N
        jAbrirItem.setText(resourceMap.getString("jAbrirItem.text")); // NOI18N
        jAbrirItem.setToolTipText(resourceMap.getString("jAbrirItem.toolTipText")); // NOI18N
        jAbrirItem.setName("jAbrirItem"); // NOI18N
        jAbrirItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        jAbrirItemActionPerformed(evt);
        }
        });
        jListadoMenu.add(jAbrirItem);
        
        jAdminItem.setAction(actionMap.get("showAdminPtos")); // NOI18N
        jAdminItem.setName("jAdminItem"); // NOI18N
        jListadoMenu.add(jAdminItem);
        
        jGuardarItem.setIcon(resourceMap.getIcon("jGuardarItem.icon")); // NOI18N
        jGuardarItem.setText(resourceMap.getString("jGuardarItem.text")); // NOI18N
        jGuardarItem.setToolTipText(resourceMap.getString("jGuardarItem.toolTipText")); // NOI18N
        jGuardarItem.setName("jGuardarItem"); // NOI18N
        jGuardarItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        jGuardarItemActionPerformed(evt);
        }
        });
        jListadoMenu.add(jGuardarItem);
        
        optionsMenu.add(jListadoMenu);
         */

        jContadoresMenu.setText(resourceMap.getString("jContadoresMenu.text")); // NOI18N
        jContadoresMenu.setName("jContadoresMenu"); // NOI18N

        jAbrirBDItem.setIcon(resourceMap.getIcon("jAbrirBDItem.icon")); // NOI18N
        jAbrirBDItem.setText(resourceMap.getString("jAbrirBDItem.text")); // NOI18N
        jAbrirBDItem.setToolTipText(resourceMap.getString("jAbrirBDItem.toolTipText")); // NOI18N
        jAbrirBDItem.setName("jAbrirBDItem"); // NOI18N
        jAbrirBDItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAbrirBDItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jAbrirBDItem);

        jInsertarItem.setAction(actionMap.get("showAdminConts")); // NOI18N
        jInsertarItem.setText(resourceMap.getString("jInsertarItem.text")); // NOI18N
        jInsertarItem.setName("jInsertarItem"); // NOI18N
        jContadoresMenu.add(jInsertarItem);

        jGuardarBDItem.setIcon(resourceMap.getIcon("jGuardarBDItem.icon")); // NOI18N
        jGuardarBDItem.setText(resourceMap.getString("jGuardarBDItem.text")); // NOI18N
        jGuardarBDItem.setToolTipText(resourceMap.getString("jGuardarBDItem.toolTipText")); // NOI18N
        jGuardarBDItem.setName("jGuardarBDItem"); // NOI18N
        jGuardarBDItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGuardarBDItemActionPerformed(evt);
            }
        });
        jContadoresMenu.add(jGuardarBDItem);

        jLeerContItem.setAction(actionMap.get("showLeerConts")); // NOI18N
        jLeerContItem.setText(resourceMap.getString("jLeerContItem.text")); // NOI18N
        jLeerContItem.setName("jLeerContItem"); // NOI18N
        jContadoresMenu.add(jLeerContItem);

        jLectPeriodItem.setAction(actionMap.get("showLecturaPeriodica")); // NOI18N
        jLectPeriodItem.setText(resourceMap.getString("jLectPeriodItem.text")); // NOI18N
        jLectPeriodItem.setName("jLectPeriodItem"); // NOI18N
        jContadoresMenu.add(jLectPeriodItem);

        optionsMenu.add(jContadoresMenu);

        menuBar.add(optionsMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        verConfigItem.setAction(actionMap.get("showConfiguracion")); // NOI18N
        verConfigItem.setText(resourceMap.getString("verConfigItem.text")); // NOI18N
        verConfigItem.setToolTipText(resourceMap.getString("verConfigItem.toolTipText")); // NOI18N
        verConfigItem.setName("verConfigItem"); // NOI18N
        helpMenu.add(verConfigItem);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setToolTipText(resourceMap.getString("aboutMenuItem.toolTipText")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        //statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        //statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        //statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        //statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        //progressBar.setName("progressBar"); // NOI18N

        //javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        //statusPanel.setLayout(statusPanelLayout);
        /*statusPanelLayout.setHorizontalGroup(
        statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1078, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createSequentialGroup()
        .addContainerGap()
        .addComponent(statusMessageLabel)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 908, Short.MAX_VALUE)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(statusAnimationLabel)
        .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
        statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(statusPanelLayout.createSequentialGroup()
        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
        .addComponent(statusMessageLabel)
        .addComponent(statusAnimationLabel)
        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(3, 3, 3))
        );*/

        setComponent(mainPanel);
        setMenuBar(menuBar);
        //setStatusBar(statusPanel);
    }

    /* Procedimiento que inicia las direcciones de grupo de los dispositivos domóticos a controlar
     * @author David Monné Chávez
     */
    public void iniciarModelo() {
    }

    /* Procedimiento que manda al bus una señal para que se apague la luz conmutable del dispositivo
     * asociado al punto pasado por parámetro
     * @author David Monné Chávez
     * @pto Variable de tipo point, de la cual obtendremos el tipo, subtipo y dirección EIB
     * @nombre Nombre que identifica al pto anterior
     */
    public void ApagarLuzConmutable(tuwien.auto.eibpoints.Point pto, String nombre) {
        String valor = "";
        try {
            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(pto.getMajorType(), pto.getMinorType());
            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
            xlator.setASDUfromString(valor);
            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                    new EIB_Address(), pto.getDeviceAddress()[0], xlator.getAPDUByteArray());

            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
            synchronized (textArea) {
                textArea.append("Mensaje enviado a: " + nombre + ", valor: " + valor + "\n");
            }
        } catch (EICLException ex) {
            showException(ex.getMessage());
        }
    }

    /* Procedimiento que "dibuja" una estancia en la interfaz gráfica
     * @author David Monné Chávez
     * @param estancias_plantaX El índice que hace referencia a la estancia en concreto que estamos tratando
     * @param plantaX La planta en concreto que estamos tratando
     * @param estanciaX El nombre de la estancia en concreto que estamos tratando
     * @param eleccion Nos permitirá saber qué planta estamos tratando (para el primer if de la función)
     */
    public void dibuja_estancias(int estancias_plantaX, javax.swing.JTabbedPane plantaX, EstanciaGenerica estanciaX, int eleccion) {
        int aux = 0;
        // En aux almacenaremos el límite de estancias que no debe ser sobrepasado por cada planta
        for (int i = 0; i <= eleccion; i++) // Es la suma de los dos pq las estancias se guardan en un sólo vector, para no tener problemas con los índices del mismo
        {
            aux = aux + vecNumEstancias[i];
        }
        int num_estancias_plantaX = aux;

        // Variables necesarias para reajustar la imagen de los planos al tamaño adecuado
        ImageIcon icon, newIcon;
        Image img, newimg;

        // Si todavía no se ha sobrepasado el límite de las estancias de una planta en concreto
        if (estancias_plantaX < num_estancias_plantaX) {
            // Añadimos la pestaña de la estancia
            plantaX.addTab(vecNombresEstancias[estancias_plantaX], estanciaX);
            // Si observamos que hay un "no" en el vector que indica la regulación de luces
            if (vecRegulacion[estancias_plantaX].indexOf("no") != -1) {
                // Desactivamos los botones correspondientes
                estanciaX.jCBRegLuc.setEnabled(false);
                estanciaX.jRBApagar.setEnabled(false);
                estanciaX.jRB25.setEnabled(false);
                estanciaX.jRB50.setEnabled(false);
                estanciaX.jRB75.setEnabled(false);
                estanciaX.jRB100.setEnabled(false);
            }
            // Añadiremos más o menos elementos al ComboBox dependiendo del nº de luces regulables que haya
            // Integer.parseInt(variable); --> Para pasar de string a entero, donde variable es una string
            // El vector listado nos servirá para añadir los diferentes elementos
            Vector<String> listado = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumLucReg[estancias_plantaX]) + 1; i++) {
                // Vamos añadiendo tantos elementos al vector como luces haya
                listado.add("Luz regulable " + i);
                //System.out.println(listado);
                // Con setModel establecemos las opciones que mostrará el ComboBox desplegable
                estanciaX.jCBRegLuc.setModel(new javax.swing.DefaultComboBoxModel(listado));
            }

            // Mismo procedimiento para la conmutación de luces
            if (vecConmutacion[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBConmLuc.setEnabled(false);
                estanciaX.jRBEncender.setEnabled(false);
                estanciaX.jRBApagLuc.setEnabled(false);
            }
            Vector<String> list_conm = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumLucConm[estancias_plantaX]) + 1; i++) {
                list_conm.add("Luz conmutable " + i);
                estanciaX.jCBConmLuc.setModel(new javax.swing.DefaultComboBoxModel(list_conm));
            }

            // Mismo procedimiento para las persianas
            if (vecPersianas[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBPersianas.setEnabled(false);
                estanciaX.jBSubir.setEnabled(false);
                estanciaX.jBBajar.setEnabled(false);
            }
            Vector<String> list_pers = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumPersianas[estancias_plantaX]) + 1; i++) {
                list_pers.add("Persiana " + i);
                estanciaX.jCBPersianas.setModel(new javax.swing.DefaultComboBoxModel(list_pers));
            }

            // Mismo procedimiento para las electroválvulas
            if (vecElectrovalvulas[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBValvula.setEnabled(false);
                estanciaX.jRBAbrir.setEnabled(false);
                estanciaX.jRBCerrar.setEnabled(false);
            }
            Vector<String> list_electr = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumElectr[estancias_plantaX]) + 1; i++) {
                list_electr.add("Electroválvula " + i);
                estanciaX.jCBValvula.setModel(new javax.swing.DefaultComboBoxModel(list_electr));
            }

            // Mismo procedimiento para las puertas
            if (vecPuertas[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBApertura.setEnabled(false);
                estanciaX.jLEstApertura.setEnabled(false);
                estanciaX.jLResApertura.setEnabled(false);
            }
            Vector<String> list_puer = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumPuertas[estancias_plantaX]) + 1; i++) {
                list_puer.add("Puerta " + i);
                estanciaX.jCBApertura.setModel(new javax.swing.DefaultComboBoxModel(list_puer));
            }

            // Mismo procedimiento para la presencia
            if (vecMovimiento[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBMovimiento.setEnabled(false);
                estanciaX.jLEstMovimiento.setEnabled(false);
                estanciaX.jLResMovimiento.setEnabled(false);
            }
            Vector<String> list_pres = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumPresencias[estancias_plantaX]) + 1; i++) {
                list_pres.add("Detector " + i);
                estanciaX.jCBMovimiento.setModel(new javax.swing.DefaultComboBoxModel(list_pres));
            }

            // Mismo procedimiento para la temperatura
            if (vecTemperatura[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jLEstTemp.setEnabled(false);
                estanciaX.jLResTemp.setEnabled(false);
            }
            // Mismo procedimiento para el sensor combinado
            if (vecCombinado[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jLEstTempComb.setEnabled(false);
                estanciaX.jLResTempComb.setEnabled(false);
                estanciaX.jLEstLumComb.setEnabled(false);
                estanciaX.jLResLumComb.setEnabled(false);
            }
            // Mismo procedimiento para la inundación
            if (vecInundacion[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jCBInund.setEnabled(false);
                estanciaX.jLEstInundacion.setEnabled(false);
                estanciaX.jLResInundacion.setEnabled(false);
            }
            Vector<String> list_inund = new Vector<String>();
            for (int i = 1; i < Integer.parseInt(vecNumElectr[estancias_plantaX]) + 1; i++) {
                list_inund.add("Sensor " + i);
                estanciaX.jCBInund.setModel(new javax.swing.DefaultComboBoxModel(list_inund));
            }

            // Mismo procedimiento para los contadores
            if (vecContadores[estancias_plantaX].indexOf("no") != -1) {
                estanciaX.jLEstPotencia.setEnabled(false);
                estanciaX.jLResPotencia.setEnabled(false);
                estanciaX.jLEstEnergia.setEnabled(false);
                estanciaX.jLResEnergia.setEnabled(false);
            }

            // Si todavía quedan iconos por mostrar en la interfaz
            if (VariableGlobal.indDibujaIconos < VariableGlobal.numTotalIconos) {
                // La función dibuja_iconos se encargará de mostrarlos en la interfaz gráfica de cada estancia
                int ind_reg_aux = VariableGlobal.ind_reg;
                int ind_conm_aux = VariableGlobal.ind_conm;
                int ind_pers_aux = VariableGlobal.ind_pers;
                int ind_electr_aux = VariableGlobal.ind_electr;
                int ind_puert_aux = VariableGlobal.ind_puert;
                int ind_mov_aux = VariableGlobal.ind_mov;
                int ind_temp_aux = VariableGlobal.ind_temp;
                int ind_comb_aux = VariableGlobal.ind_comb;
                int ind_inund_aux = VariableGlobal.ind_inund;
                int ind_cont_aux = VariableGlobal.ind_cont;

                dibuja_iconos(estanciaX, VariableGlobal.indDibujaIconos, estancias_plantaX);

                // Prepara todos los listeners de la interfaz para actuar ante posibles eventos del usuario
                monitorizacion_por_interrogacion(plantaX, estanciaX, ind_reg_aux, ind_conm_aux, ind_pers_aux, ind_electr_aux,
                        ind_puert_aux, ind_mov_aux, ind_temp_aux, ind_comb_aux, ind_inund_aux, ind_cont_aux);

                //ESTEFANÍA: ya no se usan los timers.
                // Establece una serie de "timers" encargados de llevar a cabo la monitorización continua se los sensores
                    /*monitorizacion_continua(plantaX, estanciaX, ind_reg_aux, ind_conm_aux, ind_pers_aux, ind_electr_aux,
                ind_puert_aux, ind_mov_aux, ind_temp_aux, ind_comb_aux, ind_inund_aux, ind_cont_aux);
                 */
            }
            //ESTEFANÍA:Se coge la ruta donde se encuentra el ejecutable, y se combina con la ruta del plano
            //almacenada en el .xml
            //File dir_iniciall = new File("./");
            //String a = dir_iniciall.getAbsolutePath();
            //a  = a.substring(0, a.length() - 1 );
            System.out.println("PLano=" + vecRutasPlanos[estancias_plantaX]);
            //System.out.println("PLano=" +a+vecRutasPlanos[estancias_plantaX]);
            //String rutaImg = vecRutasPlanos[estancias_plantaX].replaceAll("/",File.separator);
            //rutaImg = vecRutasPlanos[estancias_plantaX].replaceAll("\\",File.separator);

            // Almacenamos en icon la ruta del plano de la estancia que estamos tratando
            icon = new ImageIcon(vecRutasPlanos[estancias_plantaX]);


            //--------------------------------------------------------------------
            // Obtenemos la imagen, su altura y anchura
            img = icon.getImage();
            //int anchura = img.getWidth(menuBar);
            //int altura = img.getHeight(menuBar);
            int anchura = 390;
            int altura = 400;

            // Escalamos la imagen al tamaño adecuado
            newimg = img.getScaledInstance(anchura, altura, java.awt.Image.SCALE_SMOOTH);
            newIcon = new ImageIcon(newimg);
            // Establecemos cual será la imagen que se corresponderá con el jLabel (jLPlano en este caso)
            estanciaX.jLPlano.setIcon(newIcon);
            // Establecemos las medidas del Jlabel que se corresponde con el plano mostrado
            estanciaX.jLPlano.setBounds(5, 82, 390, 400);

            // Establecemos las medidas del JLayeredPane
            estanciaX.jLPEstancia.setSize(400, 564);
            // Y finalmente añadimos dicha imagen al jLayeredPane (jLPEstancia en este caso)
            estanciaX.jLPEstancia.add(estanciaX.jLPlano, javax.swing.JLayeredPane.DEFAULT_LAYER);

            // Añadimos cada estancia al vector vecEstanciasTimers (utilizado por la función deten_timers)
            vecEstanciasTimers[VariableGlobal.indVEstTim] = estanciaX;
            VariableGlobal.indVEstTim++;

        } // fin del "if (estancias_plantaX < num_estancias_plantaX)"
    } // fin del procedimiento dibuja_estancias
     
    
    
    /*
     * Procedimiento que dibuja los diferentes iconos para cada estancia en la interfaz gráfica
     * @author David Monné Chávez
     * @param estanciaX La estancia en concreto que se está tratando
     * @param indice Hace referencia al índice de los vectores para saber qué posición tratar
     * @param ind_estanciaX El índice que hace referencia a la estancia que estamos tratando
     */

    public void dibuja_iconos(EstanciaGenerica estanciaX, int indice, int ind_estanciaX) {
        //System.out.println("en una estancia generica:"+estanciaX+"--indice:"+indice+"---estanciaX:"+ind_estanciaX);
        // Esta variable llevará la cuenta del núm de iconos dibujados en una estancia en concreto
        int icono_dibujado = 0;
        // Los contadores nos servirán para evitar un problema: cuando existían varios act/sen del mismo tipo
        // y no estaban juntos en el fichero .xml, se producía un error "illegal component position"
        int cont_reg = 0;
        int cont_conm = 0;
        int cont_pers = 0;
        int cont_puert = 0;
        int cont_mov = 0;
        int cont_temp = 0;
        int cont_comb = 0;
        int cont_inund = 0;
        int cont_cont = 0;
        int cont_electr = 0;

        // Condición para salir del bucle: que se hayan dibujado todos los iconos de la estancia
        while (icono_dibujado < vecNumIconosEstancia[ind_estanciaX]) {
            // Antes que nada, comprobamos los valores de los índices de los vectores que tendrán las coordeandas de: eje_x, eje_y, ancho y alto
            // Actualizándolos en caso de que sea necesario
            actualizaIndices();
            // Si hemos dibujado el núm. de iconos que indica el vector de la estancia, salimos de la función
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                // Actualizamos la variable glolbal para la próxima vez que se invoque a esta función
                VariableGlobal.indDibujaIconos = indice;
                return;
            }

            // Si el valor es igual a null, no ejecuta las instrucciones del if 
            // Dependiendo del icono que se esté tratando en vecIcono, sabrá si ha de utilizar una variable u otra para almacenar el tipo de icono a mostrar
            if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice].indexOf("brd") != -1 && cont_reg == 0) {
                estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_reg]), Integer.parseInt(vecEje_y[VariableGlobal.ind_reg]), Integer.parseInt(vecAncho[VariableGlobal.ind_reg]), Integer.parseInt(vecAlto[VariableGlobal.ind_reg]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoReg1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_reg++;
                indice++;
                icono_dibujado++;
                cont_reg++;
            }

            // La comprobación de índices es necesaria antes de dibujar cada icono, para que se dibuje correctamente
            actualizaIndices();
            // Procedimiento análogo de ahora en adelante
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
//              String aavv = "brd";
//              System.out.println(cont_reg );//+ "---" + vecIcono[indice].indexOf(aavv) +"---"+cont_reg);
//              if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice].indexOf("brd") != -1 && cont_reg == 1)   //MONNEE
            if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice] != null && cont_reg == 1) {
                estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_reg]), Integer.parseInt(vecEje_y[VariableGlobal.ind_reg]), Integer.parseInt(vecAncho[VariableGlobal.ind_reg]), Integer.parseInt(vecAlto[VariableGlobal.ind_reg]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoReg2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_reg++;
                indice++;
                icono_dibujado++;
                cont_reg++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice].indexOf("brd") != -1 && cont_reg == 2) {
                estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_reg]), Integer.parseInt(vecEje_y[VariableGlobal.ind_reg]), Integer.parseInt(vecAncho[VariableGlobal.ind_reg]), Integer.parseInt(vecAlto[VariableGlobal.ind_reg]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoReg3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_reg++;
                indice++;
                icono_dibujado++;
                cont_reg++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice].indexOf("brd") != -1 && cont_reg == 3) {
                estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_reg]), Integer.parseInt(vecEje_y[VariableGlobal.ind_reg]), Integer.parseInt(vecAncho[VariableGlobal.ind_reg]), Integer.parseInt(vecAlto[VariableGlobal.ind_reg]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoReg4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_reg++;
                indice++;
                icono_dibujado++;
                cont_reg++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_reg] != null && vecIcono[indice].indexOf("brd") != -1 && cont_reg == 4) {
                estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_reg]), Integer.parseInt(vecEje_y[VariableGlobal.ind_reg]), Integer.parseInt(vecAncho[VariableGlobal.ind_reg]), Integer.parseInt(vecAlto[VariableGlobal.ind_reg]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoReg5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_reg++;
                indice++;
                icono_dibujado++;
                cont_reg++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_conm] != null && vecIcono[indice].indexOf("bcd") != -1 && cont_conm == 0) {
                estanciaX.jLIconoConm1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoConm1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_conm]), Integer.parseInt(vecEje_y[VariableGlobal.ind_conm]), Integer.parseInt(vecAncho[VariableGlobal.ind_conm]), Integer.parseInt(vecAlto[VariableGlobal.ind_conm]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoConm1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_conm++;
                indice++;
                icono_dibujado++;
                cont_conm++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_conm] != null && vecIcono[indice].indexOf("bcd") != -1 && cont_conm == 1) {
                estanciaX.jLIconoConm2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoConm2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_conm]), Integer.parseInt(vecEje_y[VariableGlobal.ind_conm]), Integer.parseInt(vecAncho[VariableGlobal.ind_conm]), Integer.parseInt(vecAlto[VariableGlobal.ind_conm]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoConm2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_conm++;
                indice++;
                icono_dibujado++;
                cont_conm++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_conm] != null && vecIcono[indice].indexOf("bcd") != -1 && cont_conm == 2) {
                estanciaX.jLIconoConm3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoConm3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_conm]), Integer.parseInt(vecEje_y[VariableGlobal.ind_conm]), Integer.parseInt(vecAncho[VariableGlobal.ind_conm]), Integer.parseInt(vecAlto[VariableGlobal.ind_conm]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoConm3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_conm++;
                indice++;
                icono_dibujado++;
                cont_conm++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_conm] != null && vecIcono[indice].indexOf("bcd") != -1 && cont_conm == 3) {
                estanciaX.jLIconoConm4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoConm4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_conm]), Integer.parseInt(vecEje_y[VariableGlobal.ind_conm]), Integer.parseInt(vecAncho[VariableGlobal.ind_conm]), Integer.parseInt(vecAlto[VariableGlobal.ind_conm]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoConm4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                indice++;
                VariableGlobal.ind_conm++;
                icono_dibujado++;
                cont_conm++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_conm] != null && vecIcono[indice].indexOf("bcd") != -1 && cont_conm == 4) {
                estanciaX.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoConm5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_conm]), Integer.parseInt(vecEje_y[VariableGlobal.ind_conm]), Integer.parseInt(vecAncho[VariableGlobal.ind_conm]), Integer.parseInt(vecAlto[VariableGlobal.ind_conm]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoConm5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_conm++;
                indice++;
                icono_dibujado++;
                cont_conm++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_pers] != null && vecIcono[indice].indexOf("pd") != -1 && cont_pers == 0) {
                estanciaX.jLIconoPers1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPers1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_pers]), Integer.parseInt(vecEje_y[VariableGlobal.ind_pers]), Integer.parseInt(vecAncho[VariableGlobal.ind_pers]), Integer.parseInt(vecAlto[VariableGlobal.ind_pers]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPers1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_pers++;
                indice++;
                icono_dibujado++;
                cont_pers++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_pers] != null && vecIcono[indice].indexOf("pd") != -1 && cont_pers == 1) {
                estanciaX.jLIconoPers2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPers2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_pers]), Integer.parseInt(vecEje_y[VariableGlobal.ind_pers]), Integer.parseInt(vecAncho[VariableGlobal.ind_pers]), Integer.parseInt(vecAlto[VariableGlobal.ind_pers]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPers2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_pers++;
                indice++;
                icono_dibujado++;
                cont_pers++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_pers] != null && vecIcono[indice].indexOf("pd") != -1 && cont_pers == 2) {
                estanciaX.jLIconoPers3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPers3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_pers]), Integer.parseInt(vecEje_y[VariableGlobal.ind_pers]), Integer.parseInt(vecAncho[VariableGlobal.ind_pers]), Integer.parseInt(vecAlto[VariableGlobal.ind_pers]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPers3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_pers++;
                indice++;
                icono_dibujado++;
                cont_pers++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_pers] != null && vecIcono[indice].indexOf("pd") != -1 && cont_pers == 3) {
                estanciaX.jLIconoPers4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPers4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_pers]), Integer.parseInt(vecEje_y[VariableGlobal.ind_pers]), Integer.parseInt(vecAncho[VariableGlobal.ind_pers]), Integer.parseInt(vecAlto[VariableGlobal.ind_pers]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPers4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_pers++;
                indice++;
                icono_dibujado++;
                cont_pers++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_pers] != null && vecIcono[indice].indexOf("pd") != -1 && cont_pers == 4) {
                estanciaX.jLIconoPers5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPers5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_pers]), Integer.parseInt(vecEje_y[VariableGlobal.ind_pers]), Integer.parseInt(vecAncho[VariableGlobal.ind_pers]), Integer.parseInt(vecAlto[VariableGlobal.ind_pers]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPers5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_pers++;
                indice++;
                icono_dibujado++;
                cont_pers++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_puert] != null && vecIcono[indice].indexOf("pud") != -1 && cont_puert == 0) {
                estanciaX.jLIconoPuerta1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPuerta1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_puert]), Integer.parseInt(vecEje_y[VariableGlobal.ind_puert]), Integer.parseInt(vecAncho[VariableGlobal.ind_puert]), Integer.parseInt(vecAlto[VariableGlobal.ind_puert]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPuerta1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_puert++;
                indice++;
                icono_dibujado++;
                cont_puert++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_puert] != null && vecIcono[indice].indexOf("pud") != -1 && cont_puert == 1) {
                estanciaX.jLIconoPuerta2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPuerta2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_puert]), Integer.parseInt(vecEje_y[VariableGlobal.ind_puert]), Integer.parseInt(vecAncho[VariableGlobal.ind_puert]), Integer.parseInt(vecAlto[VariableGlobal.ind_puert]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPuerta2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_puert++;
                indice++;
                icono_dibujado++;
                cont_puert++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_puert] != null && vecIcono[indice].indexOf("pud") != -1 && cont_puert == 2) {
                estanciaX.jLIconoPuerta3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPuerta3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_puert]), Integer.parseInt(vecEje_y[VariableGlobal.ind_puert]), Integer.parseInt(vecAncho[VariableGlobal.ind_puert]), Integer.parseInt(vecAlto[VariableGlobal.ind_puert]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPuerta3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_puert++;
                indice++;
                icono_dibujado++;
                cont_puert++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_puert] != null && vecIcono[indice].indexOf("pud") != -1 && cont_puert == 3) {
                estanciaX.jLIconoPuerta4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPuerta4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_puert]), Integer.parseInt(vecEje_y[VariableGlobal.ind_puert]), Integer.parseInt(vecAncho[VariableGlobal.ind_puert]), Integer.parseInt(vecAlto[VariableGlobal.ind_puert]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPuerta4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                indice++;
                VariableGlobal.ind_puert++;
                icono_dibujado++;
                cont_puert++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_puert] != null && vecIcono[indice].indexOf("pud") != -1 && cont_puert == 4) {
                estanciaX.jLIconoPuerta5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoPuerta5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_puert]), Integer.parseInt(vecEje_y[VariableGlobal.ind_puert]), Integer.parseInt(vecAncho[VariableGlobal.ind_puert]), Integer.parseInt(vecAlto[VariableGlobal.ind_puert]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoPuerta5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_puert++;
                indice++;
                icono_dibujado++;
                cont_puert++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }


            if (vecEje_x[VariableGlobal.ind_mov] != null && vecIcono[indice].indexOf("prd") != -1 && cont_mov == 0) {

                estanciaX.jLIconoMov1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoMov1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_mov]), Integer.parseInt(vecEje_y[VariableGlobal.ind_mov]), Integer.parseInt(vecAncho[VariableGlobal.ind_mov]), Integer.parseInt(vecAlto[VariableGlobal.ind_mov]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoMov1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_mov++;
                indice++;
                icono_dibujado++;
                cont_mov++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_mov] != null && vecIcono[indice].indexOf("prd") != -1 && cont_mov == 1) {
                estanciaX.jLIconoMov2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoMov2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_mov]), Integer.parseInt(vecEje_y[VariableGlobal.ind_mov]), Integer.parseInt(vecAncho[VariableGlobal.ind_mov]), Integer.parseInt(vecAlto[VariableGlobal.ind_mov]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoMov2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_mov++;
                indice++;
                icono_dibujado++;
                cont_mov++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_mov] != null && vecIcono[indice].indexOf("prd") != -1 && cont_mov == 2) {
                estanciaX.jLIconoMov3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoMov3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_mov]), Integer.parseInt(vecEje_y[VariableGlobal.ind_mov]), Integer.parseInt(vecAncho[VariableGlobal.ind_mov]), Integer.parseInt(vecAlto[VariableGlobal.ind_mov]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoMov3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_mov++;
                indice++;
                icono_dibujado++;
                cont_mov++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_mov] != null && vecIcono[indice].indexOf("prd") != -1 && cont_mov == 3) {
                estanciaX.jLIconoMov4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoMov4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_mov]), Integer.parseInt(vecEje_y[VariableGlobal.ind_mov]), Integer.parseInt(vecAncho[VariableGlobal.ind_mov]), Integer.parseInt(vecAlto[VariableGlobal.ind_mov]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoMov4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_mov++;
                indice++;
                icono_dibujado++;
                cont_mov++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_mov] != null && vecIcono[indice].indexOf("prd") != -1 && cont_mov == 4) {
                estanciaX.jLIconoMov5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoMov5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_mov]), Integer.parseInt(vecEje_y[VariableGlobal.ind_mov]), Integer.parseInt(vecAncho[VariableGlobal.ind_mov]), Integer.parseInt(vecAlto[VariableGlobal.ind_mov]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoMov5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_mov++;
                indice++;
                icono_dibujado++;
                cont_mov++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_temp] != null && vecIcono[indice].indexOf("term") != -1 && cont_temp == 0) {
                estanciaX.jLIconoTemp1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoTemp1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_temp]), Integer.parseInt(vecEje_y[VariableGlobal.ind_temp]), Integer.parseInt(vecAncho[VariableGlobal.ind_temp]), Integer.parseInt(vecAlto[VariableGlobal.ind_temp]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoTemp1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_temp++;
                indice++;
                icono_dibujado++;
                cont_temp++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_temp] != null && vecIcono[indice].indexOf("term") != -1 && cont_temp == 1) {
                estanciaX.jLIconoTemp2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoTemp2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_temp]), Integer.parseInt(vecEje_y[VariableGlobal.ind_temp]), Integer.parseInt(vecAncho[VariableGlobal.ind_temp]), Integer.parseInt(vecAlto[VariableGlobal.ind_temp]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoTemp2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_temp++;
                indice++;
                icono_dibujado++;
                cont_temp++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_temp] != null && vecIcono[indice].indexOf("term") != -1 && cont_temp == 2) {
                estanciaX.jLIconoTemp3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoTemp3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_temp]), Integer.parseInt(vecEje_y[VariableGlobal.ind_temp]), Integer.parseInt(vecAncho[VariableGlobal.ind_temp]), Integer.parseInt(vecAlto[VariableGlobal.ind_temp]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoTemp3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_temp++;
                indice++;
                icono_dibujado++;
                cont_temp++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_temp] != null && vecIcono[indice].indexOf("term") != -1 && cont_temp == 3) {
                estanciaX.jLIconoTemp4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoTemp4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_temp]), Integer.parseInt(vecEje_y[VariableGlobal.ind_temp]), Integer.parseInt(vecAncho[VariableGlobal.ind_temp]), Integer.parseInt(vecAlto[VariableGlobal.ind_temp]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoTemp4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_temp++;
                indice++;
                icono_dibujado++;
                cont_temp++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_temp] != null && vecIcono[indice].indexOf("term") != -1 && cont_temp == 4) {
                estanciaX.jLIconoTemp5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoTemp5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_temp]), Integer.parseInt(vecEje_y[VariableGlobal.ind_temp]), Integer.parseInt(vecAncho[VariableGlobal.ind_temp]), Integer.parseInt(vecAlto[VariableGlobal.ind_temp]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoTemp5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_temp++;
                indice++;
                icono_dibujado++;
                cont_temp++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_comb] != null && vecIcono[indice].indexOf("comb") != -1 && cont_comb == 0) {
                estanciaX.jLIconoComb1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoComb1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_comb]), Integer.parseInt(vecEje_y[VariableGlobal.ind_comb]), Integer.parseInt(vecAncho[VariableGlobal.ind_comb]), Integer.parseInt(vecAlto[VariableGlobal.ind_comb]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoComb1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_comb++;
                indice++;
                icono_dibujado++;
                cont_comb++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_comb] != null && vecIcono[indice].indexOf("comb") != -1 && cont_comb == 1) {
                estanciaX.jLIconoComb2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoComb2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_comb]), Integer.parseInt(vecEje_y[VariableGlobal.ind_comb]), Integer.parseInt(vecAncho[VariableGlobal.ind_comb]), Integer.parseInt(vecAlto[VariableGlobal.ind_comb]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoComb2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_comb++;
                indice++;
                icono_dibujado++;
                cont_comb++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_comb] != null && vecIcono[indice].indexOf("comb") != -1 && cont_comb == 2) {
                estanciaX.jLIconoComb3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoComb3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_comb]), Integer.parseInt(vecEje_y[VariableGlobal.ind_comb]), Integer.parseInt(vecAncho[VariableGlobal.ind_comb]), Integer.parseInt(vecAlto[VariableGlobal.ind_comb]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoComb3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_comb++;
                indice++;
                icono_dibujado++;
                cont_comb++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_comb] != null && vecIcono[indice].indexOf("comb") != -1 && cont_comb == 3) {
                estanciaX.jLIconoComb4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoComb4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_comb]), Integer.parseInt(vecEje_y[VariableGlobal.ind_comb]), Integer.parseInt(vecAncho[VariableGlobal.ind_comb]), Integer.parseInt(vecAlto[VariableGlobal.ind_comb]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoComb4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_comb++;
                indice++;
                icono_dibujado++;
                cont_comb++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_comb] != null && vecIcono[indice].indexOf("comb") != -1 && cont_comb == 4) {
                estanciaX.jLIconoComb5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoComb5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_comb]), Integer.parseInt(vecEje_y[VariableGlobal.ind_comb]), Integer.parseInt(vecAncho[VariableGlobal.ind_comb]), Integer.parseInt(vecAlto[VariableGlobal.ind_comb]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoComb5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_comb++;
                indice++;
                icono_dibujado++;
                cont_comb++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_inund] != null && vecIcono[indice].indexOf("inund") != -1 && cont_inund == 0) {
                estanciaX.jLIconoInund1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoInund1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_inund]), Integer.parseInt(vecEje_y[VariableGlobal.ind_inund]), Integer.parseInt(vecAncho[VariableGlobal.ind_inund]), Integer.parseInt(vecAlto[VariableGlobal.ind_inund]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoInund1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_inund++;
                indice++;
                icono_dibujado++;
                cont_inund++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_inund] != null && vecIcono[indice].indexOf("inund") != -1 && cont_inund == 1) {
                estanciaX.jLIconoInund2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoInund2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_inund]), Integer.parseInt(vecEje_y[VariableGlobal.ind_inund]), Integer.parseInt(vecAncho[VariableGlobal.ind_inund]), Integer.parseInt(vecAlto[VariableGlobal.ind_inund]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoInund2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_inund++;
                indice++;
                icono_dibujado++;
                cont_inund++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_inund] != null && vecIcono[indice].indexOf("inund") != -1 && cont_inund == 2) {
                estanciaX.jLIconoInund3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoInund3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_inund]), Integer.parseInt(vecEje_y[VariableGlobal.ind_inund]), Integer.parseInt(vecAncho[VariableGlobal.ind_inund]), Integer.parseInt(vecAlto[VariableGlobal.ind_inund]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoInund3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_inund++;
                indice++;
                icono_dibujado++;
                cont_inund++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_inund] != null && vecIcono[indice].indexOf("inund") != -1 && cont_inund == 3) {
                estanciaX.jLIconoInund4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoInund4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_inund]), Integer.parseInt(vecEje_y[VariableGlobal.ind_inund]), Integer.parseInt(vecAncho[VariableGlobal.ind_inund]), Integer.parseInt(vecAlto[VariableGlobal.ind_inund]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoInund4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_inund++;
                indice++;
                icono_dibujado++;
                cont_inund++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_inund] != null && vecIcono[indice].indexOf("inund") != -1 && cont_inund == 4) {
                estanciaX.jLIconoInund5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoInund5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_inund]), Integer.parseInt(vecEje_y[VariableGlobal.ind_inund]), Integer.parseInt(vecAncho[VariableGlobal.ind_inund]), Integer.parseInt(vecAlto[VariableGlobal.ind_inund]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoInund5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_inund++;
                indice++;
                icono_dibujado++;
                cont_inund++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_cont] != null && vecIcono[indice].indexOf("cont") != -1 && cont_cont == 0) {
                estanciaX.jLIconoCont1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoCont1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_cont]), Integer.parseInt(vecEje_y[VariableGlobal.ind_cont]), Integer.parseInt(vecAncho[VariableGlobal.ind_cont]), Integer.parseInt(vecAlto[VariableGlobal.ind_cont]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoCont1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_cont++;
                indice++;
                icono_dibujado++;
                cont_cont++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_cont] != null && vecIcono[indice].indexOf("cont") != -1 && cont_cont == 1) {
                estanciaX.jLIconoCont2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoCont2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_cont]), Integer.parseInt(vecEje_y[VariableGlobal.ind_cont]), Integer.parseInt(vecAncho[VariableGlobal.ind_cont]), Integer.parseInt(vecAlto[VariableGlobal.ind_cont]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoCont2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_cont++;
                indice++;
                icono_dibujado++;
                cont_cont++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_cont] != null && vecIcono[indice].indexOf("cont") != -1 && cont_cont == 2) {
                estanciaX.jLIconoCont3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoCont3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_cont]), Integer.parseInt(vecEje_y[VariableGlobal.ind_cont]), Integer.parseInt(vecAncho[VariableGlobal.ind_cont]), Integer.parseInt(vecAlto[VariableGlobal.ind_cont]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoCont3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_cont++;
                indice++;
                icono_dibujado++;
                cont_cont++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_cont] != null && vecIcono[indice].indexOf("cont") != -1 && cont_cont == 3) {
                estanciaX.jLIconoCont4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoCont4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_cont]), Integer.parseInt(vecEje_y[VariableGlobal.ind_cont]), Integer.parseInt(vecAncho[VariableGlobal.ind_cont]), Integer.parseInt(vecAlto[VariableGlobal.ind_cont]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoCont4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_cont++;
                indice++;
                icono_dibujado++;
                cont_cont++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_cont] != null && vecIcono[indice].indexOf("cont") != -1 && cont_cont == 4) {
                estanciaX.jLIconoCont5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoCont5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_cont]), Integer.parseInt(vecEje_y[VariableGlobal.ind_cont]), Integer.parseInt(vecAncho[VariableGlobal.ind_cont]), Integer.parseInt(vecAlto[VariableGlobal.ind_cont]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoCont5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_cont++;
                indice++;
                icono_dibujado++;
                cont_cont++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_electr] != null && vecIcono[indice].indexOf("vd") != -1 && cont_electr == 0) {
                estanciaX.jLIconoElectr1.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoElectr1.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_electr]), Integer.parseInt(vecEje_y[VariableGlobal.ind_electr]), Integer.parseInt(vecAncho[VariableGlobal.ind_electr]), Integer.parseInt(vecAlto[VariableGlobal.ind_electr]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoElectr1, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_electr++;
                indice++;
                icono_dibujado++;
                cont_electr++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_electr] != null && vecIcono[indice].indexOf("vd") != -1 && cont_electr == 1) {
                estanciaX.jLIconoElectr2.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoElectr2.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_electr]), Integer.parseInt(vecEje_y[VariableGlobal.ind_electr]), Integer.parseInt(vecAncho[VariableGlobal.ind_electr]), Integer.parseInt(vecAlto[VariableGlobal.ind_electr]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoElectr2, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_electr++;
                indice++;
                icono_dibujado++;
                cont_electr++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_electr] != null && vecIcono[indice].indexOf("vd") != -1 && cont_electr == 2) {
                estanciaX.jLIconoElectr3.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoElectr3.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_electr]), Integer.parseInt(vecEje_y[VariableGlobal.ind_electr]), Integer.parseInt(vecAncho[VariableGlobal.ind_electr]), Integer.parseInt(vecAlto[VariableGlobal.ind_electr]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoElectr3, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_electr++;
                indice++;
                icono_dibujado++;
                cont_electr++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_electr] != null && vecIcono[indice].indexOf("vd") != -1 && cont_electr == 3) {
                estanciaX.jLIconoElectr4.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoElectr4.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_electr]), Integer.parseInt(vecEje_y[VariableGlobal.ind_electr]), Integer.parseInt(vecAncho[VariableGlobal.ind_electr]), Integer.parseInt(vecAlto[VariableGlobal.ind_electr]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoElectr4, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_electr++;
                indice++;
                icono_dibujado++;
                cont_electr++;
            }

            actualizaIndices();
            if (icono_dibujado == vecNumIconosEstancia[ind_estanciaX]) {
                VariableGlobal.indDibujaIconos = indice;
                return;
            }
            if (vecEje_x[VariableGlobal.ind_electr] != null && vecIcono[indice].indexOf("vd") != -1 && cont_electr == 4) {
                estanciaX.jLIconoElectr5.setIcon(new javax.swing.ImageIcon(getClass().getResource(vecIcono[indice])));
                estanciaX.jLIconoElectr5.setBounds(Integer.parseInt(vecEje_x[VariableGlobal.ind_electr]), Integer.parseInt(vecEje_y[VariableGlobal.ind_electr]), Integer.parseInt(vecAncho[VariableGlobal.ind_electr]), Integer.parseInt(vecAlto[VariableGlobal.ind_electr]));
                estanciaX.jLPEstancia.add(estanciaX.jLIconoElectr5, javax.swing.JLayeredPane.DEFAULT_LAYER);
                VariableGlobal.ind_electr++;
                indice++;
                icono_dibujado++;
                cont_electr++;
            }
            // Actualizamos la variable glolbal para la próxima vez que se invoque a esta función
            VariableGlobal.indDibujaIconos = indice;
        } // fin del bucle while
        // Supondremos que no habrá más de 30 iconos para representarlos gráficamente en una misma estancia (5 del mismo tipo)
    }

    /*
     * Procedimiento que prepara todos los listeners de las diferentes estancias para actuar según correponda si se pulsa algún botón
     * @author David Monné Chávez 
     * @param plantaX La planta en concreto que se está tratando
     * @param estanciaX La estancia en concreto que se está tratando
     * @param ind_reg El índice para los elementos de tipo "regulación"
     * @param ind_conm El índice para los elementos de tipo "conmutación"
     * @param ind_pers El índice para los elementos de tipo "persianas"
     * @param ind_electr El índice para los elementos de tipo "electroválvula"
     * @param ind_puert El índice para los elementos de tipo "puerta"
     * @param ind_mov El índice para los elementos de tipo "movimiento"
     * @param ind_temp El índice para los elementos de tipo "temperatura"
     * @param ind_combinado El índice para los elementos de tipo "combinado"
     * @param ind_inund El índice para los elementos de tipo "inundacion"
     * @param ind_cont El índice para los elementos de tipo "contadores"
     */
    public void monitorizacion_por_interrogacion(final javax.swing.JTabbedPane plantaX, final EstanciaGenerica estanciaX, final int ind_reg, final int ind_conm, final int ind_pers, final int ind_electr,
            final int ind_puert, final int ind_mov, final int ind_temp, final int ind_comb, final int ind_inund, final int ind_cont) {
        ////////////////////// ACTUADORES, LUCES REGULABES /////////////////////////   
        // Código que se ejecuta cuando se pulsa el ComboBox de las luces regulables para cualquier estancia
        estanciaX.jCBRegLuc.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBRegLuc.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
            }
        });

        // Código que se ejecuta cuando se pulsa el botón apagar (luces regulables) para cualquier estancia
        estanciaX.jRBApagar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_reg;
                String nombre = "";

                // Dependiendo del valor de opcion, sabremos sobre qué icono actuar (máx. 5 iconos de un mismo tipo)
                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bra.png")));
                        estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr1 = "bra";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Decrease 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }

                        break;
                    case 2:
                        // Se ha de revisar el índice en cada caso, para asegurarnos que las coordenadas de actualización son las mismas que las del icono original (para que no cambie de posición, vamos)
                        // Salvo en el primer caso, ya que se controla en otra parte 
                        ind_aux = revisaIndice(ind_aux + 1, "reg", 2);
                        estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bra.png")));
                        estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr2 = "bra";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Decrease 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "reg", 3);
                        estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bra.png")));
                        estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr3 = "bra";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Decrease 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "reg", 4);
                        estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bra.png")));
                        estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr4 = "bra";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Decrease 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "reg", 5);
                        estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bra.png")));
                        estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr5 = "bra";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Decrease 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        // Código que se ejecuta cuando se pulsa el botón del 25% (luces regulables) para cualquier estancia
        estanciaX.jRB25.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_reg;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre25.png")));
                        estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr1.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr1.indexOf("bra") != -1) // Se realiza incremento
                            {
                                help = "Increase 2";
                            }
                            // Si estaba al 50% o 75%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre50") != -1 || estanciaX.ultimoEstado_lr1.indexOf("bre75") != -1) // Se realiza un decremento de 3
                            {
                                help = "Decrease 3";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre100") != -1) // Se realiza un decremento de 2
                            {
                                help = "Decrease 2";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr1 = "bre25";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "reg", 2);
                        estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre25.png")));
                        estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr2.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr2.indexOf("bra") != -1) // Se realiza incremento
                            {
                                help = "Increase 2";
                            }
                            // Si estaba al 50% o 75%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre50") != -1 || estanciaX.ultimoEstado_lr2.indexOf("bre75") != -1) // Se realiza un decremento de 3
                            {
                                help = "Decrease 3";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre100") != -1) // Se realiza un decremento de 2
                            {
                                help = "Decrease 2";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr2 = "bre25";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "reg", 3);
                        estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre25.png")));
                        estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr3.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr3.indexOf("bra") != -1) // Se realiza incremento
                            {
                                help = "Increase 2";
                            }
                            // Si estaba al 50% o 75%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre50") != -1 || estanciaX.ultimoEstado_lr3.indexOf("bre75") != -1) // Se realiza un decremento de 3
                            {
                                help = "Decrease 3";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre100") != -1) // Se realiza un decremento de 2
                            {
                                help = "Decrease 2";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr3 = "bre25";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "reg", 4);
                        estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre25.png")));
                        estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr4.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr4.indexOf("bra") != -1) // Se realiza incremento
                            {
                                help = "Increase 2";
                            }
                            // Si estaba al 50% o 75%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre50") != -1 || estanciaX.ultimoEstado_lr4.indexOf("bre75") != -1) // Se realiza un decremento de 3
                            {
                                help = "Decrease 3";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre100") != -1) // Se realiza un decremento de 2
                            {
                                help = "Decrease 2";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr4 = "bre25";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "reg", 5);
                        estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre25.png")));
                        estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr5.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr5.indexOf("bra") != -1) // Se realiza incremento
                            {
                                help = "Increase 2";
                            }
                            // Si estaba al 50% o 75%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre50") != -1 || estanciaX.ultimoEstado_lr5.indexOf("bre75") != -1) // Se realiza un decremento de 3
                            {
                                help = "Decrease 3";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre100") != -1) // Se realiza un decremento de 2
                            {
                                help = "Decrease 2";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr5 = "bre25";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón del 50% (luces regulables) para cualquier estancia
        estanciaX.jRB50.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_reg;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre50.png")));
                        estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "1");
                        int comodin = 0;
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr1.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr1.indexOf("bra") != -1) {
                                // Se realiza incremento
                                help = "Increase 2";
                                // Y se activa la variable comodín (su funcionamiento se verá más adelante)
                                comodin = 1;
                            }
                            // Si estaba al 25%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre25") != -1) {
                                help = "Increase 4";
                            }
                            // Si estaba al 75%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre75") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 3";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr1 = "bre50";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodín está activado, es que hay que realizar otro envío de señal (caso de la secuencia apagado/desc --> 50%)
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 4";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 1, "reg", 2);
                        estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre50.png")));
                        estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr2.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr2.indexOf("bra") != -1) {
                                // Se realiza incremento
                                help = "Increase 2";
                                // Y se activa la variable comodín (su funcionamiento se verá más adelante)
                                comodin = 1;
                            }
                            // Si estaba al 25%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre25") != -1) {
                                help = "Increase 4";
                            }
                            // Si estaba al 75%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre75") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 3";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr2 = "bre50";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodín está activado, es que hay que realizar otro envío de señal (caso de la secuencia apagado/desc --> 50%)
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 4";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 2, "reg", 3);
                        estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre50.png")));
                        estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr3.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr3.indexOf("bra") != -1) {
                                // Se realiza incremento
                                help = "Increase 2";
                                // Y se activa la variable comodín (su funcionamiento se verá más adelante)
                                comodin = 1;
                            }
                            // Si estaba al 25%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre25") != -1) {
                                help = "Increase 4";
                            }
                            // Si estaba al 75%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre75") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 3";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr3 = "bre50";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodín está activado, es que hay que realizar otro envío de señal (caso de la secuencia apagado/desc --> 50%)
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 4";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 3, "reg", 4);
                        estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre50.png")));
                        estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr4.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr4.indexOf("bra") != -1) {
                                // Se realiza incremento
                                help = "Increase 2";
                                // Y se activa la variable comodín (su funcionamiento se verá más adelante)
                                comodin = 1;
                            }
                            // Si estaba al 25%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre25") != -1) {
                                help = "Increase 4";
                            }
                            // Si estaba al 75%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre75") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 3";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr4 = "bre50";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodín está activado, es que hay que realizar otro envío de señal (caso de la secuencia apagado/desc --> 50%)
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 4";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 4, "reg", 5);
                        estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre50.png")));
                        estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si el anterior estado de esa luz es desconocido o apagado
                            if (estanciaX.ultimoEstado_lr5.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr5.indexOf("bra") != -1) {
                                // Se realiza incremento
                                help = "Increase 2";
                                // Y se activa la variable comodín (su funcionamiento se verá más adelante)
                                comodin = 1;
                            }
                            // Si estaba al 25%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre25") != -1) {
                                help = "Increase 4";
                            }
                            // Si estaba al 75%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre75") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 3";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr5 = "bre50";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodín está activado, es que hay que realizar otro envío de señal (caso de la secuencia apagado/desc --> 50%)
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 4";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón del 75% (luces regulables) para cualquier estancia
        estanciaX.jRB75.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_reg;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre75.png")));
                        estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "1");
                        int comodin = 0;
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si la luz se encuentra en modo desconocido o apagado, no se puede ir directamente al 75%, así que primero lo subimos al 25%
                            if (estanciaX.ultimoEstado_lr1.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr1.indexOf("bra") != -1) {
                                help = "Increase 2";
                                comodin = 1;
                            }
                            // Si el anterior estado de esa luz es 25%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre25") != -1) // Se realiza incremento
                            {
                                help = "Increase 3";
                            }
                            // Si el anterior estado de esa luz es 50%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre50") != -1) // Se realiza incremento
                            {
                                help = "Increase 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr1.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr1 = "bre75";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodin está activado es el caso de: apagado/desc --> 75% por lo que volvemos a enviar otra señal
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 3";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 1, "reg", 2);
                        estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre75.png")));
                        estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si la luz se encuentra en modo desconocido o apagado, no se puede ir directamente al 75%, así que primero lo subimos al 25%
                            if (estanciaX.ultimoEstado_lr2.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr2.indexOf("bra") != -1) {
                                help = "Increase 2";
                                comodin = 1;
                            }
                            // Si el anterior estado de esa luz es 25%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre25") != -1) // Se realiza incremento
                            {
                                help = "Increase 3";
                            }
                            // Si el anterior estado de esa luz es 50%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre50") != -1) // Se realiza incremento
                            {
                                help = "Increase 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr2.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr2 = "bre75";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodin está activado es el caso de: apagado/desc --> 75% por lo que volvemos a enviar otra señal
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 3";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 2, "reg", 3);
                        estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre75.png")));
                        estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si la luz se encuentra en modo desconocido o apagado, no se puede ir directamente al 75%, así que primero lo subimos al 25%
                            if (estanciaX.ultimoEstado_lr3.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr3.indexOf("bra") != -1) {
                                help = "Increase 2";
                                comodin = 1;
                            }
                            // Si el anterior estado de esa luz es 25%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre25") != -1) // Se realiza incremento
                            {
                                help = "Increase 3";
                            }
                            // Si el anterior estado de esa luz es 50%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre50") != -1) // Se realiza incremento
                            {
                                help = "Increase 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr3.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr3 = "bre75";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodin está activado es el caso de: apagado/desc --> 75% por lo que volvemos a enviar otra señal
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 3";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 3, "reg", 4);
                        estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre75.png")));
                        estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si la luz se encuentra en modo desconocido o apagado, no se puede ir directamente al 75%, así que primero lo subimos al 25%
                            if (estanciaX.ultimoEstado_lr4.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr4.indexOf("bra") != -1) {
                                help = "Increase 2";
                                comodin = 1;
                            }
                            // Si el anterior estado de esa luz es 25%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre25") != -1) // Se realiza incremento
                            {
                                help = "Increase 3";
                            }
                            // Si el anterior estado de esa luz es 50%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre50") != -1) // Se realiza incremento
                            {
                                help = "Increase 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr4.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr4 = "bre75";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodin está activado es el caso de: apagado/desc --> 75% por lo que volvemos a enviar otra señal
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 3";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        comodin = 0;
                        ind_aux = revisaIndice(ind_aux + 4, "reg", 5);
                        estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre75.png")));
                        estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            // Por defecto no hace nada
                            String help = "Increase 0";
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            // Si la luz se encuentra en modo desconocido o apagado, no se puede ir directamente al 75%, así que primero lo subimos al 25%
                            if (estanciaX.ultimoEstado_lr5.indexOf("brd") != -1 || estanciaX.ultimoEstado_lr5.indexOf("bra") != -1) {
                                help = "Increase 2";
                                comodin = 1;
                            }
                            // Si el anterior estado de esa luz es 25%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre25") != -1) // Se realiza incremento
                            {
                                help = "Increase 3";
                            }
                            // Si el anterior estado de esa luz es 50%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre50") != -1) // Se realiza incremento
                            {
                                help = "Increase 4";
                            }
                            // Si estaba al 100%
                            if (estanciaX.ultimoEstado_lr5.indexOf("bre100") != -1) // Se realiza un decremento
                            {
                                help = "Decrease 4";
                            }
                            // Se actualiza su estado
                            estanciaX.ultimoEstado_lr5 = "bre75";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }

                            // Si el comodin está activado es el caso de: apagado/desc --> 75% por lo que volvemos a enviar otra señal
                            if (comodin == 1) {
                                comodin = 0;
                                help = "Increase 3";
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data_aux = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data_aux, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }

                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón del 100% (luces regulables) para cualquier estancia
        estanciaX.jRB100.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_reg;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoReg1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre100.png")));
                        estanciaX.jLIconoReg1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr1 = "bre100";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Increase 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "reg", 2);
                        estanciaX.jLIconoReg2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre100.png")));
                        estanciaX.jLIconoReg2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr2 = "bre100";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Increase 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "reg", 3);
                        estanciaX.jLIconoReg3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre100.png")));
                        estanciaX.ultimoEstado_lr3 = "bre100";
                        estanciaX.jLIconoReg3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Increase 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "reg", 4);
                        estanciaX.jLIconoReg4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre100.png")));
                        estanciaX.jLIconoReg4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr4 = "bre100";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Increase 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "reg", 5);
                        estanciaX.jLIconoReg5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bre100.png")));
                        estanciaX.jLIconoReg5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        estanciaX.ultimoEstado_lr5 = "bre100";
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_reg", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Increase 1";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

////////////////////// ACTUADORES, LUCES CONMUTABLES /////////////////////////                    

// Código que se ejecuta cuando se pulsa el ComboBox de las luces conmutables para cualquier estancia
        estanciaX.jCBConmLuc.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBConmLuc.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
                //AÑADIDO POR ESTEFANÍA
                //Mandamos una señal, para saber el estado en que se encuentra el actuador.
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "" + VariableGlobal.opcion);
                manda_senyal(nombre, estanciaX, 0, 0);//Se pone a 0, porque no hacen falta.
            }
        });

// Código que se ejecuta cuando se pulsa el botón de encendido (luces conmutables) para cualquier estancia
        estanciaX.jRBEncender.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_conm;
                String nombre = "";
                System.out.println("Seleccionado");
                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoConm1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                        estanciaX.jLIconoConm1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "1");

                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            if (p != null) {
                                String help = "True";
                                PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                                xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                                xlator.setASDUfromString(help);
                                CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                        new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                                tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                                synchronized (textArea) {
                                    textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                                }
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "conm", 2);
                        estanciaX.jLIconoConm2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                        estanciaX.jLIconoConm2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "conm", 3);
                        estanciaX.jLIconoConm3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                        estanciaX.jLIconoConm3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "conm", 4);
                        estanciaX.jLIconoConm4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                        estanciaX.jLIconoConm4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "conm", 5);
                        estanciaX.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                        estanciaX.jLIconoConm5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón de apagado (luces conmutables) para cualquier estancia
        estanciaX.jRBApagLuc.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int ind_aux = ind_conm;
                String nombre = "";
                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoConm1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                        estanciaX.jLIconoConm1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "conm", 2);
                        estanciaX.jLIconoConm2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                        estanciaX.jLIconoConm2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "conm", 3);
                        estanciaX.jLIconoConm3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                        estanciaX.jLIconoConm3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "conm", 4);
                        estanciaX.jLIconoConm4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                        estanciaX.jLIconoConm4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "conm", 5);
                        estanciaX.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                        estanciaX.jLIconoConm5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

////////////////////// ACTUADORES, PERSIANAS /////////////////////////                    

// Código que se ejecuta cuando se pulsa el ComboBox de las persianas para cualquier estancia
        estanciaX.jCBPersianas.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBPersianas.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón subir (persianas) para cualquier estancia
        estanciaX.jBSubir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Dependiendo del valor de opcion, sabremos sobre qué icono actuar (máx. 5 iconos de un mismo tipo)
                int ind_aux = ind_pers;
                String nombre = "";
                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoPers1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                        estanciaX.jLIconoPers1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Up";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "pers", 2);
                        estanciaX.jLIconoPers2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                        estanciaX.jLIconoPers2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Up";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "pers", 3);
                        estanciaX.jLIconoPers3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                        estanciaX.jLIconoPers3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Up";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "pers", 4);
                        estanciaX.jLIconoPers4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                        estanciaX.jLIconoPers4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Up";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "pers", 5);
                        estanciaX.jLIconoPers5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                        estanciaX.jLIconoPers5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Up";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón bajar (persianas) para cualquier estancia
        estanciaX.jBBajar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Dependiendo del valor de opcion, sabremos sobre qué icono actuar (máx. 5 iconos de un mismo tipo)
                int ind_aux = ind_pers;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoPers1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                        estanciaX.jLIconoPers1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Down";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "pers", 2);
                        estanciaX.jLIconoPers2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                        estanciaX.jLIconoPers2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Down";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "pers", 3);
                        estanciaX.jLIconoPers3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                        estanciaX.jLIconoPers3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Down";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "pers", 4);
                        estanciaX.jLIconoPers4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                        estanciaX.jLIconoPers4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Down";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "pers", 5);
                        estanciaX.jLIconoPers5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                        estanciaX.jLIconoPers5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "Down";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

////////////////////// ACTUADORES, ELECTROVÁLVULAS /////////////////////////                    

// Código que se ejecuta cuando se pulsa el ComboBox de las electroválvulas para cualquier estancia
        estanciaX.jCBValvula.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBValvula.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón abrir (electroválvulas) para cualquier estancia
        estanciaX.jRBAbrir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Dependiendo del valor de opcion, sabremos sobre qué icono actuar (máx. 5 iconos de un mismo tipo)
                int ind_aux = ind_electr;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoElectr1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"va.png")));
                        estanciaX.jLIconoElectr1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "electr", 2);
                        estanciaX.jLIconoElectr2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"va.png")));
                        estanciaX.jLIconoElectr2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "electr", 3);
                        estanciaX.jLIconoElectr3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"va.png")));
                        estanciaX.jLIconoElectr3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "electr", 4);
                        estanciaX.jLIconoElectr4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"va.png")));
                        estanciaX.jLIconoElectr4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "electr", 5);
                        estanciaX.jLIconoElectr5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"va.png")));
                        estanciaX.jLIconoElectr5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "True";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                }
            }
        });

// Código que se ejecuta cuando se pulsa el botón cerrar (electroválvulas) para cualquier estancia
        estanciaX.jRBCerrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Dependiendo del valor de opcion, sabremos sobre qué icono actuar (máx. 5 iconos de un mismo tipo)
                int ind_aux = ind_electr;
                String nombre = "";

                switch (VariableGlobal.opcion) {
                    case 1:
                        estanciaX.jLIconoElectr1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"vc.png")));
                        estanciaX.jLIconoElectr1.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "1");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "electr", 2);
                        estanciaX.jLIconoElectr2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"vc.png")));
                        estanciaX.jLIconoElectr2.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "2");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "electr", 3);
                        estanciaX.jLIconoElectr3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"vc.png")));
                        estanciaX.jLIconoElectr3.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "3");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "electr", 4);
                        estanciaX.jLIconoElectr4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"vc.png")));
                        estanciaX.jLIconoElectr4.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "4");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "electr", 5);
                        estanciaX.jLIconoElectr5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"vc.png")));
                        estanciaX.jLIconoElectr5.setBounds(Integer.parseInt(vecEje_x[ind_aux]), Integer.parseInt(vecEje_y[ind_aux]), Integer.parseInt(vecAncho[ind_aux]), Integer.parseInt(vecAlto[ind_aux]));
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "electrov", "5");
                        // A continuación obtenemos dicho point y mandamos la señal al panel domótico para que se apague la luz
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                            String help = "False";
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_WRITE);
                            xlator.setASDUfromString(help);
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ,
                                    new EIB_Address(), p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            synchronized (textArea) {
                                textArea.append(fecha_hora() + ": Mensaje enviado a " + nombre + ", valor= " + help + "\n");
                            }
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;
                    default:
                        break;
                } // fin del case
            }
        });

///////////////////////////   SENSORES, MOVIMIENTO    ////////////////////////

// Código que se ejecuta cuando se pulsa el ComboBox del sensor de movimiento para cualquier estancia
        estanciaX.jCBMovimiento.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBMovimiento.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
                //}
                //});

                int ind_aux = ind_mov;
                String nombre = "";
                // Actualización de la variable global: necesaria para la monitorización del sensor 
                VariableGlobal.estancia = estanciaX;

                switch (VariableGlobal.opcion) {
                    //hatsuket
                    case 1:
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "1");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {

                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 1;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            // Hasta aquí, mismo procedimiento que en "Leer contador.java" cuando se le da al botón leer
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "mov", 2);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "2");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 2;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                            // Hasta aquí, mismo procedimiento que en "Leer contador.java" cuando se le da al botón leer
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "mov", 3);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "3");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 3;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "mov", 4);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "4");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 4;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "mov", 5);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "5");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 5;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    default:
                        break;
                }

            }
        });

///////////////////////////   SENSORES, PUERTAS    ////////////////////////

// Código que se ejecuta cuando se pulsa el ComboBox del sensor de la puerta para cualquier estancia
        estanciaX.jCBApertura.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBApertura.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
                //}
                //});

                int ind_aux = ind_puert;
                String nombre = "";
                // Actualización de la variable global: necesaria para la monitorización del sensor 
                VariableGlobal.estancia = estanciaX;
                switch (VariableGlobal.opcion) {
                    case 1:
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "1");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 6;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "puerta", 2);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "2");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 7;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "puerta", 3);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "3");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 8;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "puerta", 4);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "4");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 9;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "puerta", 5);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "5");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 10;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    default:
                        break;
                }

            }
        });

///////////////////////////   SENSORES, INUNDACIÓN    ////////////////////////   

// Código que se ejecuta cuando se pulsa el ComboBox del sensor de inundación para cualquier estancia
        estanciaX.jCBInund.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // Obtenemos la opción seleccionada por el usuario
                String opcionSelec = estanciaX.jCBInund.getSelectedItem().toString();

                // Dependiendo de qué opción haya escogido, la variable opcion tendrá un valor u otro
                if (opcionSelec.indexOf("1") != -1) {
                    VariableGlobal.opcion = 1;
                }
                if (opcionSelec.indexOf("2") != -1) {
                    VariableGlobal.opcion = 2;
                }
                if (opcionSelec.indexOf("3") != -1) {
                    VariableGlobal.opcion = 3;
                }
                if (opcionSelec.indexOf("4") != -1) {
                    VariableGlobal.opcion = 4;
                }
                if (opcionSelec.indexOf("5") != -1) {
                    VariableGlobal.opcion = 5;
                }
                //}
                //});

                int ind_aux = ind_inund;
                String nombre = "";
                // Actualización de la variable global: necesaria para la monitorización del sensor 
                VariableGlobal.estancia = estanciaX;

                switch (VariableGlobal.opcion) {
                    case 1:
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "1");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 11;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 2:
                        ind_aux = revisaIndice(ind_aux + 1, "inund", 2);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "2");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 12;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 3:
                        ind_aux = revisaIndice(ind_aux + 2, "inund", 3);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "3");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 13;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 4:
                        ind_aux = revisaIndice(ind_aux + 3, "inund", 4);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "4");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 14;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    case 5:
                        ind_aux = revisaIndice(ind_aux + 4, "inund", 5);
                        // Obtenemos el nombre asociado al elemento seleccionado, para después obtener el "point" asociado a dicho nombre
                        nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "5");
                        // A continuación obtenemos dicho point y realizamos una lectura para saber en qué estado se encuentra el sensor
                        try {
                            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);

                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del contador
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                            // Actualización de variables globales: necesaria para la monitorización del sensor 
                            VariableGlobal.coord = ind_aux;
                            VariableGlobal.opc_sens = 15;
                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        } catch (EICLException ex) {
                            showException(ex.getMessage());
                        }
                        break;

                    default:
                        break;
                }

            }
        });

    } // fin procedimiento monitorizacion_por_interrogacion

    /* Procedimiento que envía una señal a través del bus EIB para poder monitorizar
     * de forma automática los sensores
     * @author David Monné Chávez
     * @param nombre El nombre del sensor que nos servirá para obtener el punto adecuado
     * @param estanciaX La estancia en concreto que estamos tratando
     * @param indice El indice que nos servirá para saber las coordenadas de situación del icono
     * @param opcion La opcion que indicará qué tipo de sensor estamos tratando (puerta 2, movimiento 1, etc)
     */
    public void manda_senyal(String nombre, EstanciaGenerica estanciaX, int indice, int opcion) {
        if (tunnel == null) {
            return;// Si NO se ha establecido una conexión,NO SE MANDA LA SEÑAL.
        }
        if (VariableGlobal.Lcontadores.getAllPoints().length == 0) {
            return;// Si NO hay contadores registrados, NO SE MANDA LA SEÑAL.
        }    // Obtenemos el point asociado al nombre pasado por parámetro
        try {
            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
            if (p != null) {
                PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                // Creamos un nuevo dato con la información del dispositivo
                CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                        p.getDeviceAddress()[0], xlator.getAPDUByteArray());
                // Se envía el frame y se espera por su confirmación
                tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
            }
        } catch (EICLException ex) {
            showException(ex.getMessage());
        }
    }

    /*
     * Procedimiento que monitoriza cada X segundos el estado de los sensores
     * @author David Monné Chávez 
     * @param plantaX La planta en concreto que se está tratando
     * @param estanciaX La estancia en concreto que se está tratando
     * @param ind_reg El índice para los elementos de tipo "regulación"
     * @param ind_conm El índice para los elementos de tipo "conmutación"
     * @param ind_pers El índice para los elementos de tipo "persianas"
     * @param ind_electr El índice para los elementos de tipo "electroválvula"
     * @param ind_puert El índice para los elementos de tipo "puerta"
     * @param ind_mov El índice para los elementos de tipo "movimiento"
     * @param ind_temp El índice para los elementos de tipo "temperatura"
     * @param ind_combinado El índice para los elementos de tipo "combinado"
     * @param ind_inund El índice para los elementos de tipo "inundacion"
     * @param ind_cont El índice para los elementos de tipo "contadores"
     */
    public void monitorizacion_continua(final javax.swing.JTabbedPane plantaX, final EstanciaGenerica estanciaX, final int ind_reg, final int ind_conm, final int ind_pers, final int ind_electr,
            final int ind_puert, final int ind_mov, final int ind_temp, final int ind_comb, final int ind_inund, final int ind_cont) {
        // ------------------ SENSORES DE MOVIMIENTO ------------------------------
        // Sensor de movimiento 1
        estanciaX.timer_mov1 = new Timer(intervalo, new ActionListener() // cada "intervalo" segundos (3 está puesto)
        {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_mov;
                String nombre = "";

                // Obtenemos el nombre asociado al elemento seleccionado
                nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "1");
                // En caso de que hayamos obtenido algún nombre
                if (!nombre.equals("")) // Mandamos una señal para ese dispositivo, y saber en qué estado se encuentra
                {
                    manda_senyal(nombre, estanciaX, ind_aux, 1);
                } // En caso contrario, paramos el timer del movimiento 1 y ejecutamos el siguiente (movimiento 2)
                else {
                    estanciaX.timer_mov1.stop();
                    estanciaX.timer_mov2.start();
                }

            } // fin de actionPerformed
        }); // fin del ActionListener del timer_mov1

        estanciaX.timer_mov1.start();

        // Sensor de movimiento 2
        estanciaX.timer_mov2 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_mov;
                String nombre_2 = "";

                // Mismo procedimiento para el sensor de movimiento 2
                ind_aux = revisaIndice(ind_mov + 1, "mov", 2);
                nombre_2 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "2");
                if (!nombre_2.equals("")) {
                    manda_senyal(nombre_2, estanciaX, ind_aux, 2);
                } else {
                    estanciaX.timer_mov2.stop();
                    estanciaX.timer_mov3.start();
                }

            } // fin de actionPerformed
        }); // fin del ActionListener del timer_mov2

        // Sensor de movimiento 3
        estanciaX.timer_mov3 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_mov;
                String nombre_3 = "";

                // Mismo procedimiento para el sensor de movimiento 2
                ind_aux = revisaIndice(ind_mov + 2, "mov", 3);
                nombre_3 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "3");
                if (!nombre_3.equals("")) {
                    manda_senyal(nombre_3, estanciaX, ind_aux, 3);
                } else {
                    estanciaX.timer_mov3.stop();
                    estanciaX.timer_mov4.start();
                }

            } // fin de actionPerformed
        }); // fin del ActionListener del timer_mov3

        // Sensor de movimiento 4
        estanciaX.timer_mov4 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_mov;
                String nombre_4 = "";

                // Mismo procedimiento para el sensor de movimiento 2
                ind_aux = revisaIndice(ind_mov + 3, "mov", 4);
                nombre_4 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "4");
                if (!nombre_4.equals("")) {
                    manda_senyal(nombre_4, estanciaX, ind_aux, 4);
                } else {
                    estanciaX.timer_mov4.stop();
                    estanciaX.timer_mov5.start();
                }

            } // fin de actionPerformed
        }); // fin del ActionListener del timer_mov4

        // Sensor de movimiento 5
        estanciaX.timer_mov5 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_mov;
                String nombre_5 = "";

                // Mismo procedimiento para el sensor de movimiento 2
                ind_aux = revisaIndice(ind_mov + 4, "mov", 5);
                nombre_5 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "mov", "5");
                if (!nombre_5.equals("")) {
                    manda_senyal(nombre_5, estanciaX, ind_aux, 5);
                } else {
                    estanciaX.timer_mov5.stop();
                    estanciaX.timer_puerta1.start();
                }

            } // fin de actionPerformed
        }); // fin del ActionListener del timer_mov5

        // -------------- SENSORES DE LAS PUERTAS ------------------------------

        // Sensor de puerta1
        estanciaX.timer_puerta1 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_puert;
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "1");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 6);
                } else {
                    estanciaX.timer_puerta1.stop();
                    estanciaX.timer_puerta2.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_puerta1

        // Sensor de puerta2
        estanciaX.timer_puerta2 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //int ind_aux = ind_puert;
                int ind_aux = revisaIndice(ind_puert + 1, "puerta", 2);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "2");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 7);
                } else {
                    estanciaX.timer_puerta2.stop();
                    estanciaX.timer_puerta3.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_puerta2

        // Sensor de puerta3
        estanciaX.timer_puerta3 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //int ind_aux = ind_puert;
                int ind_aux = revisaIndice(ind_puert + 2, "puerta", 3);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "3");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 8);
                } else {
                    estanciaX.timer_puerta3.stop();
                    estanciaX.timer_puerta4.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_puerta3

        // Sensor de puerta4
        estanciaX.timer_puerta4 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //int ind_aux = ind_puert;
                int ind_aux = revisaIndice(ind_puert + 3, "puerta", 4);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "4");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 9);
                } else {
                    estanciaX.timer_puerta4.stop();
                    estanciaX.timer_puerta5.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_puerta4

        // Sensor de puerta5
        estanciaX.timer_puerta5 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                //int ind_aux = ind_puert;
                int ind_aux = revisaIndice(ind_puert + 4, "puerta", 5);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "puerta", "5");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 10);
                } else {
                    estanciaX.timer_puerta5.stop();
                    estanciaX.timer_inund1.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_puerta5

        // -------------- SENSORES DE INUNDACIÓN ------------------------------

        // Sensor de inundación1
        estanciaX.timer_inund1 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_inund;
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "1");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 11);
                } else {
                    estanciaX.timer_inund1.stop();
                    estanciaX.timer_inund2.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_inund1

        // Sensor de inundación2
        estanciaX.timer_inund2 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = revisaIndice(ind_inund + 1, "inund", 2);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "2");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 12);
                } else {
                    estanciaX.timer_inund2.stop();
                    estanciaX.timer_inund3.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_inund2

        // Sensor de inundación3
        estanciaX.timer_inund3 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = revisaIndice(ind_inund + 2, "inund", 3);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "3");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 13);
                } else {
                    estanciaX.timer_inund3.stop();
                    estanciaX.timer_inund4.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_inund3

        // Sensor de inundación4
        estanciaX.timer_inund4 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = revisaIndice(ind_inund + 3, "inund", 4);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "4");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 14);
                } else {
                    estanciaX.timer_inund4.stop();
                    estanciaX.timer_inund5.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_inund4

        // Sensor de inundación5
        estanciaX.timer_inund5 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = revisaIndice(ind_inund + 4, "inund", 5);
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "inund", "5");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 15);
                } else {
                    estanciaX.timer_inund5.stop();
                    estanciaX.timer_temp.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_inund5

        // -------------- SENSOR DE TEMPERATURA ------------------------------

        // Sensor de temperatura
        estanciaX.timer_temp = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_temp;
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "temp", "1");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 16);
                } else {
                    estanciaX.timer_temp.stop();
                    estanciaX.timer_comb.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_temp

        // -------------- SENSOR COMBINADO -----------------------------------

        // Sensor combinado de temperatura y luminosidad
        estanciaX.timer_comb = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_comb;
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "comb", "1");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 17);
                } else {
                    estanciaX.timer_comb.stop();
                    estanciaX.timer_cont.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_comb

        // -------------- SENSOR DEL CONTADOR ---------------------------------

        // Sensor del contador (energía, potencia, etc)
        estanciaX.timer_cont = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_cont;
                String nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "cont", "1");
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 18);
                } else {
                    estanciaX.timer_cont.stop();
                    //estanciaX.timer_mov1.start();
                    estanciaX.timer_luzc1.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener del timer_cont

        //AÑADIDO POR ESTEFANÍA
        // ------------------ ACTUADORES: LUCES CONMUTABLES ------------------------------
        // Luz conmutable 1
        estanciaX.timer_luzc1 = new Timer(intervalo, new ActionListener() // cada "intervalo" segundos (3 está puesto)
        {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_conm;
                String nombre = "";
                // Obtenemos el nombre asociado al elemento seleccionado
                nombre = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "1");
                // En caso de que hayamos obtenido algún nombre
                if (!nombre.equals("")) {
                    manda_senyal(nombre, estanciaX, ind_aux, 19);// Mandamos una señal para ese dispositivo, y saber en qué estado se encuentra
                } // En caso contrario, paramos el timer del movimiento 1 y ejecutamos el siguiente (movimiento 2)
                else {
                    estanciaX.timer_luzc1.stop();
                    estanciaX.timer_luzc2.start();
                }
            }
        });

        // Luz conmutable 2
        estanciaX.timer_luzc2 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_conm;
                String nombre_2 = "";
                // Mismo procedimiento para el sensor de movimiento 2
                ind_aux = revisaIndice(ind_conm + 1, "luz_conm", 2);
                nombre_2 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "2");
                if (!nombre_2.equals("")) {
                    manda_senyal(nombre_2, estanciaX, ind_aux, 20);
                } else {
                    estanciaX.timer_luzc2.stop();
                    estanciaX.timer_luzc3.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener

        // Luz conmutable 3
        estanciaX.timer_luzc3 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_conm;
                String nombre_3 = "";
                ind_aux = revisaIndice(ind_conm + 2, "luz_conm", 3);
                nombre_3 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "3");
                if (!nombre_3.equals("")) {
                    manda_senyal(nombre_3, estanciaX, ind_aux, 21);
                } else {
                    estanciaX.timer_luzc3.stop();
                    estanciaX.timer_luzc4.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener

        // Luz conmutable 4
        estanciaX.timer_luzc4 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_conm;
                String nombre_4 = "";
                ind_aux = revisaIndice(ind_conm + 3, "luz_conm", 4);
                nombre_4 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "4");
                if (!nombre_4.equals("")) {
                    manda_senyal(nombre_4, estanciaX, ind_aux, 22);
                } else {
                    estanciaX.timer_luzc4.stop();
                    estanciaX.timer_luzc5.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener

        // Luz conmutable 5
        estanciaX.timer_luzc5 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_conm;
                String nombre_5 = "";
                ind_aux = revisaIndice(ind_conm + 4, "luz_conm", 5);
                nombre_5 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "luz_conm", "5");
                if (!nombre_5.equals("")) {
                    manda_senyal(nombre_5, estanciaX, ind_aux, 23);
                } else {
                    estanciaX.timer_luzc5.stop();
                    //estanciaX.timer_mov1.start();
                    estanciaX.timer_pers1.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener

        // Persiana 1
        estanciaX.timer_pers1 = new Timer(intervalo, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int ind_aux = ind_pers;
                String nombre_1 = "";
                nombre_1 = devuelve_nombre(plantaX.getName(), estanciaX.getName(), "pers", "1");
                if (!nombre_1.equals("")) {
                    manda_senyal(nombre_1, estanciaX, ind_aux, 24);
                } else {
                    estanciaX.timer_pers1.stop();
                    estanciaX.timer_mov1.start();
                }
            } // fin de actionPerformed
        }); // fin del ActionListener

    } // fin del procedimiento monitorizar_siempre

    /* Procedimiento que detiene la ejecución de todos los timers (salvo el que muestra la fecha y hora)
     * @author David Monné Chávez
     */
    public void deten_timers() {
        // Si el vector es distinto de nulo (ya ha sido creado) y tiene algún elemento en él
        if (vecEstanciasTimers != null && vecEstanciasTimers.length > 0) // Detenemos los diferentes timers para cada una de las estancias
        {
            for (int i = 0; i < vecEstanciasTimers.length; i++) {
//            System.out.println("veamos que hay:"+vecEstanciasTimers[i].timer_mov1);
                if (vecEstanciasTimers[i].timer_mov1 != null) {
                    vecEstanciasTimers[i].timer_mov1.stop();
                    vecEstanciasTimers[i].timer_mov2.stop();
                    vecEstanciasTimers[i].timer_mov3.stop();
                    vecEstanciasTimers[i].timer_mov4.stop();
                    vecEstanciasTimers[i].timer_mov5.stop();
                }
                if (vecEstanciasTimers[i].timer_puerta1 != null) {
                    vecEstanciasTimers[i].timer_puerta1.stop();
                    vecEstanciasTimers[i].timer_puerta2.stop();
                    vecEstanciasTimers[i].timer_puerta3.stop();
                    vecEstanciasTimers[i].timer_puerta4.stop();
                    vecEstanciasTimers[i].timer_puerta5.stop();
                }
                if (vecEstanciasTimers[i].timer_inund1 != null) {
                    vecEstanciasTimers[i].timer_inund1.stop();
                    vecEstanciasTimers[i].timer_inund2.stop();
                    vecEstanciasTimers[i].timer_inund3.stop();
                    vecEstanciasTimers[i].timer_inund4.stop();
                    vecEstanciasTimers[i].timer_inund5.stop();
                }
                if (vecEstanciasTimers[i].timer_temp != null) {
                    vecEstanciasTimers[i].timer_temp.stop();
                    vecEstanciasTimers[i].timer_comb.stop();
                    vecEstanciasTimers[i].timer_cont.stop();
                }
                //Añadido por Estefanía para los actuadores
                if (vecEstanciasTimers[i].timer_luzc1 != null) {
                    vecEstanciasTimers[i].timer_luzc1.stop();
                    vecEstanciasTimers[i].timer_luzc2.stop();
                    vecEstanciasTimers[i].timer_luzc3.stop();
                    vecEstanciasTimers[i].timer_luzc4.stop();
                    vecEstanciasTimers[i].timer_luzc5.stop();
                }
                if (vecEstanciasTimers[i].timer_pers1 != null) {
                    vecEstanciasTimers[i].timer_pers1.stop();
                    /*vecEstanciasTimers[i].timer_pers2.stop();
                    vecEstanciasTimers[i].timer_pers3.stop();
                    vecEstanciasTimers[i].timer_pers4.stop();
                    vecEstanciasTimers[i].timer_pers5.stop();*/
                }
            }
        }
    }

    /*
     * Procedimiento que busca en el vector vecDispDomoticos el nombre de un elemento gracias a los 4 parámetros
     * @author David Monné Chávez
     * @param planta El nombre de la planta
     * @param estancia El nombre de la estancia
     * @param tipo El tipo de actuador/sensor
     * @param numero El numero de act/sen (de 1 a 5)
     */
    public String devuelve_nombre(String planta, String estancia, String tipo, String numero) {
        for (int i = 0; i < vecDispDomoticos.length; i++) // Si da con los cuatro parámetros seguidos, OBLIGATORIAMENTE el nombre se encuentra en la sig. posición
        {
            if (vecDispDomoticos[i].indexOf(planta) != -1) {
                if (vecDispDomoticos[i + 1].indexOf(estancia) != -1) {
                    if (vecDispDomoticos[i + 2].indexOf(tipo) != - 1) {
                        if (vecDispDomoticos[i + 3].indexOf(numero) != -1) {
                            return vecDispDomoticos[i + 4];
                        }
                    }
                }
            }
        }

        // En caso de no encontrarlo devuelve la ristra vacía
        return "";
    }

    /*
     * Procedimiento que busca en el vector vecDispUsados una planta y estancia en concreto para devolver el 
     * índice donde comienzan los contadores de dicha estancia
     * @author David Monné Chávez
     * @param planta El nombre de la planta
     * @param estancia El nombre de la estancia
     */
    public static int devuelve_pos(String planta, String estancia) {
        // Si da con el nombre de la planta seguido de la estancia, OBLIGATORIAMENTE el siguiente es el índice del primer contador
        for (int i = 0; i < vecDispUsados.length; i++) {
            if (vecDispUsados[i].indexOf(planta) != -1) {
                if (vecDispUsados[i + 1].indexOf(estancia) != -1) {
                    return i + 2;
                }
            }
        }

        // En caso de no encontrarlo, devuelve -1
        return -1;
    }

    /* Procedimiento que recorre el vector vecDispUsados para saber si una pareja planta-estancia ya ha sido insertada en él o no
     * @author David Monné Chávez
     * @param planta El nombre de la planta
     * @param estancia El nombre de la estancia
     */
    public static int comprueba_planta_estancia(String planta, String estancia) {
        // Si da con el nombre de la planta seguido de la estancia, hemos encontrado la pareja y devolvemos 1
        for (int i = 0; i < vecDispUsados.length; i++) {
            if (vecDispUsados[i].indexOf(planta) != -1) {
                if (vecDispUsados[i + 1].indexOf(estancia) != -1) {
                    return 1;
                }
            }
        }

        // En caso contrario, se devuelve un 0
        return 0;
    }

    /*
     * Procedimiento que actualiza los vectores vecDispUsados y vecDispDomoticos
     * tras la eliminación de una estancia en concreto
     * @author David Monné Chávez
     * @param planta La planta a la que pertenece la estancia
     * @param estancia La estancia que se va a eliminar
     */
    public static void estancia_eliminada(String planta, String estancia) {
        // Bucle que recorre el vector vecDispUsados en busca de la pareja planta-estancia
        for (int i = 0; i < vecDispUsados.length; i++) {
            if (vecDispUsados[i].indexOf(planta) != -1) {
                if (vecDispUsados[i + 1].indexOf(estancia) != -1) {
                    // Si la encuentra, borra sus posiciones 
                    vecDispUsados[i] = "";
                    vecDispUsados[i + 1] = "";
                    // Y pone a 0 las 10 siguientes
                    for (int j = i + 2; j < i + 12; j++) {
                        vecDispUsados[j] = String.valueOf(0);
                    }
                }
            }
        }

        // Bucle que recorre el vector vecDispDomoticos en busca de la pareja planta-estancia
        for (int k = 0; k < vecDispDomoticos.length; k++) {
            if (vecDispDomoticos[k].indexOf(planta) != -1) {
                if (vecDispDomoticos[k + 1].indexOf(estancia) != - 1) // Si la encuentra, borra sus posiciones y las 3 siguientes (planta,estancia,tipo,num,nombre)
                {
                    for (int l = k; l < k + 5; l++) {
                        vecDispDomoticos[l] = "";
                    }
                }
            }
        }
    }

    /*
     * Procedimiento que actualiza los vectores vecDispUsados y vecDispDomoticos
     * tras la eliminación de una planta en concreto
     * @author David Monné Chávez
     * @param planta La planta que se va a eliminar
     */
    public static void planta_eliminada(String planta) {
        // Bucle que recorre el vector vecDispUsados en busca de la planta
        for (int i = 0; i < vecDispUsados.length; i++) {
            if (vecDispUsados[i].indexOf(planta) != -1) {
                // Si la encuentra, borra su posición y la sig (estancia)
                vecDispUsados[i] = "";
                vecDispUsados[i + 1] = "";
                // Y pone a 0 las 10 siguientes (los contadores de los act/sens)
                for (int j = i + 2; j < i + 12; j++) {
                    vecDispUsados[j] = String.valueOf(0);
                }
            }
        }

        // Bucle que recorre el vector vecDispDomoticos en busca de la planta
        for (int k = 0; k < vecDispDomoticos.length; k++) {
            if (vecDispDomoticos[k].indexOf(planta) != -1) // Si la encuentra, borra su posicion y las 4 siguientes (planta,estancia,tipo,num,nombre)
            {
                for (int l = k; l < k + 5; l++) {
                    vecDispDomoticos[l] = "";
                }
            }
        }
    }

    /*
     * Procedimiento que actualiza los vectores vecDispUsados y vecDispDomoticos tras la eliminación de un act/sen
     * @author David Monné Chávez
     * @param planta El nombre de la planta
     * @param estancia El nombre de la estancia
     * @param tipo El tipo de actuador/sensor
     * @param numero El numero de act/sen (de 1 a 5)
     */
    public static void dispositivo_eliminado(String planta, String estancia, String tipo, String numero) {
        int pos = 0;
        // Una vez que se localiza la posición del act/sen que va a ser eliminado
        for (int i = 0; i < vecDispDomoticos.length; i++) {
            if (vecDispDomoticos[i].indexOf(planta) != -1) {
                if (vecDispDomoticos[i + 1].indexOf(estancia) != -1) {
                    if (vecDispDomoticos[i + 2].indexOf(tipo) != - 1) {
                        if (vecDispDomoticos[i + 3].indexOf(numero) != -1) // Borra dicha posicion y las 4 siguientes (planta,estancia,tipo,num,nombre)
                        {
                            for (int j = i; j < i + 5; j++) {
                                vecDispDomoticos[j] = "";
                            }
                        }
                    }
                }
            }
        }

        // Para el otro vector (vecDispUsados) nos hace falta saber qué tipo de elemento va a ser eliminado
        if (tipo.indexOf("luz_reg") != -1) {
            pos = 2;
        }
        if (tipo.indexOf("luz_conm") != -1) {
            pos = 3;
        }
        if (tipo.indexOf("pers") != -1) {
            pos = 4;
        }
        if (tipo.indexOf("electrov") != -1) {
            pos = 5;
        }
        if (tipo.indexOf("puerta") != -1) {
            pos = 6;
        }
        if (tipo.indexOf("mov") != -1) {
            pos = 7;
        }
        if (tipo.indexOf("temp") != -1) {
            pos = 8;
        }
        if (tipo.indexOf("comb") != -1) {
            pos = 9;
        }
        if (tipo.indexOf("inund") != -1) {
            pos = 10;
        }
        if (tipo.indexOf("cont") != -1) {
            pos = 11;
        }

        // Una vez que sepamos su tipo, buscamos la planta y estancia a la que pertenece el dispositivo
        for (int k = 0; k < vecDispUsados.length; k++) {
            if (vecDispUsados[k].indexOf(planta) != -1) {
                if (vecDispUsados[k + 1].indexOf(estancia) != -1) // Y decrementamos en uno el contador correspondiente (según el tipo eliminado)
                {
                    vecDispUsados[k + pos] = String.valueOf(Integer.valueOf(vecDispUsados[k + pos]) - 1);
                }
            }
        }
    }

    /* 
     * Procedimiento que guarda el contenido de los vectores vecDispUsados y vecDispDomoticos, así como
     * sus índices, en un fichero de texto
     * @author David Monné Chávez
     */
    public static void guarda_info() {
        // El nombre del fichero será info.txt
        String fichero = "info.txt";

        try {
            // Esto lo hacemos para que borre el contenido del fichero en caso de haber algo en él
            PrintWriter writer = new PrintWriter(fichero);
            writer.close();
        } catch (Exception ex) {
            showException(ex.getMessage());
        }

        // Se recorre el vector de los dispositivos usados y se escribe el contenido del mismo en el fichero, separado por comas
        for (int i = 0; i < vecDispUsados.length; i++) {
            escribeMensajeLog(fichero, vecDispUsados[i] + ",");
        }
        // En la siguiente línea se añade el índice del vector
        escribeMensajeLog(fichero, "\n" + String.valueOf(VariableGlobal.indVDispUsad + "\n"));

        // Procedimiento análogo para el vector de los dispositivos domóticos
        for (int j = 0; j < vecDispDomoticos.length; j++) {
            escribeMensajeLog(fichero, vecDispDomoticos[j] + ",");
        }
        escribeMensajeLog(fichero, "\n" + String.valueOf(VariableGlobal.indVDispDom));
    }

    /* 
     * Procedimiento que lee el fichero "info.txt" y carga el contenido de dicho fichero
     * en los vectores vecDispUsados y vecDispDomoticos, así como sus índices (variables globales)
     * @author David Monné Chávez
     */
    public void recupera_info() {
        String fichero = "info.txt";
        String linea = "";
        int comienzo = 0, fin = 0;

        try {
            // Si todavía no se ha creado el fichero, se crea aquí vacío
            // Si ya está creado, se abre y se cierra sin hacer nada
            RandomAccessFile miRAFile;
            miRAFile = new RandomAccessFile(fichero, "rw");
            miRAFile.close();
            // Lo de antes se hace para evitar un error en la sig. línea de código cuando el fichero no existe
            FileReader fr = new FileReader(fichero);
            BufferedReader br = new BufferedReader(fr);
            // Obtenemos la primera linea del fichero
            linea = br.readLine();
            // Si el fichero está vacío, es que todavía no hay nada que recuperar, terminamos la función
            if (linea == null) {
                return;
            }

            // fin y comienzo marcarán las posiciones para poder extraer las palabras separadas por coma en el fichero
            fin = linea.indexOf(",", 0);
            // Recorremos el vector y vamos insertando en él las diferentes palabras que hay en la primera línea del fichero
            for (int i = 0; i < vecDispUsados.length; i++) {
                vecDispUsados[i] = linea.substring(comienzo, fin);
                // Actualizamos las variables comienzo y fin
                comienzo = fin + 1;
                fin = linea.indexOf(",", comienzo);
//////////////                System.out.println("vecDispUsados[i]:"+vecDispUsados[i]);
            }

            // Leemos la segunda línea del fichero
            linea = br.readLine();
            // Como sólo es un dato, se lo asginamos directamente a la variable global
            VariableGlobal.indVDispUsad = Integer.valueOf(linea);

            // Leemos la tercera línea del fichero
            linea = br.readLine();
            // Procedimiento análogo al otro vector
            comienzo = 0;
            fin = linea.indexOf(",", 0);
            for (int i = 0; i < vecDispDomoticos.length; i++) {
                vecDispDomoticos[i] = linea.substring(comienzo, fin);
                comienzo = fin + 1;
                fin = linea.indexOf(",", comienzo);
//                System.out.println("vecDispDomoticos[i]:"+vecDispDomoticos[i]);
            }

            // Leemos la cuarta línea del fichero
            linea = br.readLine();
            // Le asignamos el valor a la variable global correspondiente
            VariableGlobal.indVDispDom = Integer.valueOf(linea);

//            System.out.println("VariableGlobal.indVDispDom:"+VariableGlobal.indVDispDom);

            // Cerramos el descriptor de fichero
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Procedimiento que actualiza los índices para los vectores Eje_x, Eje_y, Ancho y Alto. Sirve de ayuda para la función dibuja_iconos
     * @author David Monné Chávez 
     */
    public void actualizaIndices() {
        // En caso de llegar a igualarse los índices
        if (VariableGlobal.ind_reg == VariableGlobal.ind_reg_aux) {
            // Se procede a actualizar ambos
            VariableGlobal.ind_reg = VariableGlobal.ind_reg + 45;
            VariableGlobal.ind_reg_aux = VariableGlobal.ind_reg + 5;
        }
        // Mismo procedimiento para el resto de índices
        if (VariableGlobal.ind_conm == VariableGlobal.ind_conm_aux) {
            VariableGlobal.ind_conm = VariableGlobal.ind_conm + 45;
            VariableGlobal.ind_conm_aux = VariableGlobal.ind_conm + 5;
        }
        if (VariableGlobal.ind_pers == VariableGlobal.ind_pers_aux) {
            VariableGlobal.ind_pers = VariableGlobal.ind_pers + 45;
            VariableGlobal.ind_pers_aux = VariableGlobal.ind_pers + 5;
        }
        if (VariableGlobal.ind_puert == VariableGlobal.ind_puert_aux) {
            VariableGlobal.ind_puert = VariableGlobal.ind_puert + 45;
            VariableGlobal.ind_puert_aux = VariableGlobal.ind_puert + 5;
        }
        if (VariableGlobal.ind_mov == VariableGlobal.ind_mov_aux) {
            VariableGlobal.ind_mov = VariableGlobal.ind_mov + 45;
            VariableGlobal.ind_mov_aux = VariableGlobal.ind_mov + 5;
        }
        if (VariableGlobal.ind_temp == VariableGlobal.ind_temp_aux) {
            VariableGlobal.ind_temp = VariableGlobal.ind_temp + 45;
            VariableGlobal.ind_temp_aux = VariableGlobal.ind_temp + 5;
        }
        if (VariableGlobal.ind_comb == VariableGlobal.ind_comb_aux) {
            VariableGlobal.ind_comb = VariableGlobal.ind_comb + 45;
            VariableGlobal.ind_comb_aux = VariableGlobal.ind_comb + 5;
        }
        if (VariableGlobal.ind_inund == VariableGlobal.ind_inund_aux) {
            VariableGlobal.ind_inund = VariableGlobal.ind_inund + 45;
            VariableGlobal.ind_inund_aux = VariableGlobal.ind_inund + 5;
        }
        if (VariableGlobal.ind_cont == VariableGlobal.ind_cont_aux) {
            VariableGlobal.ind_cont = VariableGlobal.ind_cont + 45;
            VariableGlobal.ind_cont_aux = VariableGlobal.ind_cont + 5;
        }
        if (VariableGlobal.ind_electr == VariableGlobal.ind_electr_aux) {
            VariableGlobal.ind_electr = VariableGlobal.ind_electr + 45;
            VariableGlobal.ind_electr_aux = VariableGlobal.ind_electr + 5;
        }
    }

    /*
     * Procedimiento que reinicia los índices para los vectores Eje_x, Eje_y, Ancho y Alto. 
     * Sirve de apoyo antes de llamar a las funciones dibuja_estancias y monitorizacion_por_interrogacion
     * @author David Monné Chávez 
     */
    public void reiniciaIndices() {
        // Reiniciamos los valores de los índices de algunos vectores
        VariableGlobal.ind_reg = 0;
        VariableGlobal.ind_conm = 5;
        VariableGlobal.ind_pers = 10;
        VariableGlobal.ind_puert = 15;
        VariableGlobal.ind_mov = 20;
        VariableGlobal.ind_temp = 25;
        VariableGlobal.ind_comb = 30;
        VariableGlobal.ind_inund = 35;
        VariableGlobal.ind_cont = 40;
        VariableGlobal.ind_electr = 45;
        VariableGlobal.ind_reg_aux = 5;
        VariableGlobal.ind_conm_aux = 10;
        VariableGlobal.ind_pers_aux = 15;
        VariableGlobal.ind_puert_aux = 20;
        VariableGlobal.ind_mov_aux = 25;
        VariableGlobal.ind_temp_aux = 30;
        VariableGlobal.ind_comb_aux = 35;
        VariableGlobal.ind_inund_aux = 40;
        VariableGlobal.ind_cont_aux = 45;
        VariableGlobal.ind_electr_aux = 50;
    }

    /*
     * Procedimiento que comprueba si el índice pasado por parámetro es correcto para posteriormente
     * actualizar el estado de algún icono en la interfaz mediante las coordenadas adecuadas
     * @author David Monné Chávez 
     * @param indice El indice que se evaluará para saber si es válido o no
     * @param tipo Servirá como flag para proceder de una manera u otra
     * @param pos La posición del índice que se está tratando dentro del subgrupo de 5 elementos
     * pos 2: sólo tiene en cuenta la posición actual
     * pos 3: Tiene en cuenta la posición actual y la anterior
     * pos 4: Tiene en cuenta la posición actual y las dos anteriores
     * pos 5: Tiene en cuenta la posición actual y las tres anteriores
     */
    public int revisaIndice(int indice, String tipo, int pos) {
        int opcion = 0;

        // En primer lugar, veremos cuál es el tipo pasado
        if (tipo.indexOf("reg") != -1) {
            opcion = 1;
        }
        if (tipo.indexOf("conm") != -1) {
            opcion = 2;
        }
        if (tipo.indexOf("pers") != -1) {
            opcion = 3;
        }
        if (tipo.indexOf("electr") != -1) {
            opcion = 4;
        }
        if (tipo.indexOf("puerta") != -1) {
            opcion = 5;
        }
        if (tipo.indexOf("mov") != -1) {
            opcion = 6;
        }
        if (tipo.indexOf("inund") != -1) {
            opcion = 7;
        }


        // Dependiendo el tipo, se procederá de una forma u otra
        switch (opcion) {
            // Luces regulabes
            case 1:
                // Sólo tiene en cuenta su posición
                if (pos == 2) // Si el índice es mayor que 0 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 0 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                // Tiene en cuenta su posición y la anterior
                if (pos == 3) {
                    if (indice > 0 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                // Tiene en cuenta su posición y las dos anterires
                if (pos == 4) {
                    if (indice > 0 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                // Tiene en cuenta su posición y las tres anteriores
                if (pos == 5) {
                    if (indice > 0 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Luces conmutables
            case 2:
                if (pos == 2) // Si el índice es mayor que 5 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 5 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 5 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 5 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 5 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Persianas
            case 3:
                if (pos == 2) // Si el índice es mayor que 10 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 10 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 10 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 10 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 10 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Electroválvulas
            case 4:

                if (pos == 2) // Si el índice es mayor que 45 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 45 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 45 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 45 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 45 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Puertas
            case 5:

                if (pos == 2) // Si el índice es mayor que 15 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 15 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 15 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 15 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 15 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Sensores de movimiento
            case 6:

                if (pos == 2) // Si el índice es mayor que 20 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 20 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 20 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 20 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 20 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
            // Sensores de inundación
            case 7:

                if (pos == 2) // Si el índice es mayor que 35 y múltiplo de 5 (última cifra acaba en 5 ó 0), se actualizará
                // Es decir, se le sumará 45 (debido a la ubicación de las coordenadas en los vectores)
                {
                    if (indice > 35 && (indice % 5) == 0) {
                        return indice + 45;
                    }
                }
                if (pos == 3) {
                    if (indice > 35 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 4) {
                    if (indice > 35 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0)) {
                        return indice + 45;
                    }
                }
                if (pos == 5) {
                    if (indice > 35 && ((indice % 5 == 0) || ((indice - 1) % 5) == 0 || ((indice - 2) % 5) == 0 || ((indice - 3) % 5) == 0)) {
                        return indice + 45;
                    }
                }
        }

        // Si no ha sido necesario realizar modificaciones, se devolverá el índice tal cual
        return indice;
    }

    /* 
     * Procedimiento que se activa cada vez que se observa que existe un mensaje por el bus KNX
     * @author Domingo Benítez Díaz
     * @param e Un Evento
     */
    void packetReceived(FrameEvent e) {
        // Paquete que contiene la información
        CEMI_L_DATA data = (CEMI_L_DATA) e.getPacket();
        String mensajeLog2 = null, mensajeLog3 = null;
        PointPDUXlator ppx;
        String from = "";
        /* --------------------------------------------------------------------------
        // Inicializamos el pointlist a la lista de contadores de la base de datos
        try {
        pointList = new PointList(raiz_ficheros + Fcontadores_MCA_cnf);
        //pointList = VariableGlobal.Lcontadores;
        } catch (EICLException ex) {
        showException(ex.getMessage());
        }
        ---------------------------------------------------------------------------- */

        try {
            // Obtiene el nombre del contador que se está tratando
            from = VariableGlobal.Lcontadores.getPoint(data.getDestinationAddress()).getDeviceName();
        } catch (EICLException ex) {
            from = data.getDestinationAddress().toString();
        }

        // Log de la aplicación
        synchronized (textArea) {
            // Primera parte del mensaje, falta el valor
            String fechaHora = fecha_hora();
            String mensajeLog = fechaHora + ": Paquete recibido desde "
                    + data.getSourceAddress().toString() + " a " + from + ", valor= ";
            // Informamos en el log de la aplicación de la recepción de los diversos paquetes
            textArea.append(mensajeLog);
            // Se sitúa al final del todo
            textArea.setCaretPosition(textArea.getText().length());

            //CAMBIO PARA EL MÓDULO DE PROCESAMIENTO ESTADÍSTICO: NUEVO MENSAJELOG
            from = data.getDestinationAddress().toString();
            mensajeLog = fechaHora + ": Paquete recibido desde "
                    + data.getSourceAddress().toString() + " a " + from + ", valor= ";
            //------------------------------
            // Escribe la primera parte del mensaje en el fichero LOG
            escribeMensajeLog(ficheroLOG, mensajeLog);

            try {
                // Obtiene el punto/contador asociado al parámetro de entrada
                tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(data.getDestinationAddress());
                ppx = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                ppx.setAPDUByteArray(data.getData());

                // Si es nulo, no aparece en la ventana textArea(log de la aplicación) ni se escribe en el fichero
                mensajeLog2 = ppx.getASDUasString();
                //System.out.println("que tendra esto: " + mensajeLog2);
                // Es nulo cuando la dirección de grupo no se reconoce como una de las que está en la lista
                textArea.append(" " + mensajeLog2);
                // Escribe la segunda parte del mensaje cuando se encuentre una dirección de grupo conocida
                //escribeMensajeLog(ficheroLOG, mensajeLog2); //COMENTADO. NO QUEREMOS GUARDAR EL VALOR TRADUCIDO

                //System.out.println("Nombre: " + p.getDeviceName()); 
                //System.out.println("Tipo Major: " + p.getMajorType()); 
                //System.out.println("Tipo Minor: " + p.getMinorType()); 
                //System.out.println("Contador= " + mensajeLog2); 

            } catch (EICLException ex) {
                byte[] d = data.getData();
                for (int i = 0; i < d.length; i++) {
                    short help = d[i]; // Selecciona un short porque no hay unsigned en Java
                    mensajeLog3 = Integer.toHexString(help & 0x00FF) + " ";
                    textArea.append(mensajeLog3);
                    // Escribe la segunda parte del mensaje cuando la dirección de grupo no es conocida
                    //escribeMensajeLog(ficheroLOG, mensajeLog3); //COMENTADO. NO QUEREMOS GUARDAR EL VALOR TRADUCIDO
                    //System.out.println("mensajeLog3 " + mensajeLog3);

                    // System.out.println("dato leido " + i + ": " + 
                    // System.out.println("Byte dentro de " + mensajeLog + 
                    // Integer.toHexString(help & 0x00FF) ); // mensaje por el terminal
                }
            }
            textArea.append("\n");
            //NUEVA SEGUNDA PARTE DEL MENSAJELOG. VALOR SIN TRADUCIR.
            byte[] d = data.getData();
            for (int i = 0; i < d.length; i++) {
                short help = d[i]; // Selecciona un short porque no hay unsigned en Java
                mensajeLog3 = Integer.toHexString(help & 0x00FF) + " ";
                // Escribe la segunda parte del mensaje cuando la dirección de grupo no es conocida
                escribeMensajeLog(ficheroLOG, mensajeLog3);
            }
            //----------------------------------
            escribeMensajeLog(ficheroLOG, "\n");
        }
        // Mira si se correponde con alguno de los contadores y cuando haya recibido
        // las medidas de todos los contadores de un intervalo de muestreo, las
        // almacena en un fichero exclusivo para las medidas de contadores.
        guardaContador(data);
    }

    /*
     * Muestra las medidas de los contadores en el log de la aplicación y guarda diversa información en algunos ficheros LOG
     * Está orientado principalmente a la lectura periódica de los contadores, y va almacenando en el fichero de las medidas los valores ofrecidos por dichos contadores
     * El comportamiento es ligeramente distinto si se trata de una lectura manual solicitada por el usuario
     * @author Domingo Benítez Díaz, David Monné Chávez
     * @param data La información contenida en el paquete CEMI_L_DATA
     */
    private void guardaContador(CEMI_L_DATA data) {
        // Contador tendrá el dato pasado por parámetro
        CEMI_L_DATA contador = data;
        // Obtenemos la dirección de destino (dirección de grupo)
        String from = data.getDestinationAddress().toString();
        // Así como la dirección de origen
        String direccion_fisica = data.getSourceAddress().toString();
        PointPDUXlator ppx;
        // Obtenemos en el vector arrayLocal todos los contadores
        tuwien.auto.eibpoints.Point[] arrayLocal = VariableGlobal.Lcontadores.getAllPoints();
        String aux = "";

        try {
            // Obtenemos el punto asociado a la dirección de destino del dato pasado por parámetro
            tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(contador.getDestinationAddress());

            // Si el punto obtenido es distinto de nulo
            if (p != null) {
                // Se selecciona la dirección de grupo del siguiente contador que espera recibir en el orden de la lista de contadores
                EIB_Address[] dumy = arrayLocal[contadores_aparecidos].getDeviceAddress();

                // Se comprueba que la dirección física no sea ninguna de estas
                if (direccion_fisica.contentEquals("1.1.0") != true
                        && direccion_fisica.contentEquals("1.2.0") != true
                        && direccion_fisica.contentEquals("1.3.0") != true
                        && direccion_fisica.contentEquals("0.0.0") != true
                        // Y que se cumpla una de estas dos condiciones: 
                        //   - Se trata de una lectura periódica y el contador que se está tratando es el siguiente que se espera recibir en la lista de contadores
                        //   - Se trata de una lectura manual
                        && ((VariableGlobal.lectura_manual == 1 && from.contentEquals(dumy[0].toString()) == true) || VariableGlobal.lectura_manual == 0)) {

                    /* Con "p != null" seleccionamos que el dato esté registrado en la 
                     *      lista de contadores "Lcontadores"
                     * Con "direccion_fisica.contentEquals("1.1.0")" NO consideramos
                     *      los mensajes duplicados de los contadores que emite el 
                     *      router IP con dirección 1.1.0
                     * Con "direccion_fisica.contentEquals("0.0.0")" NO consideramos
                     *      los mensajes duplicados de los contadores que emite el 
                     *      router IP con dirección 0.0.0
                     */

                    // Incrementamos el número de contadores tratados si se trata de una lectura periodica
                    if (VariableGlobal.lectura_manual == 1) {
                        contadores_aparecidos++;
                    }

                    ppx = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                    ppx.setAPDUByteArray(data.getData());

                    // Obtiene el valor del contador en mensajeLog2
                    String mensajeLog2 = ppx.getASDUasString();

                    //En from esta la DG del paquete,en base a ella se busca en los vectores vecDGS,vecDGOpcion y vecDGEstanciaGenerica
                    buscaDGeInicializaVars(from);
                    //CÓDIGO PARA COLOCARLO, CUANDO SE MODIFIQUE A SKOA 0.3, EN LA PARTE DE LA ALARMA
                    //Si se activa la ALARMA, se van a apagar todos los dispositivos pero no se reciben paquetes
                    //para actualizar sus vistas, y se pueden quedar las vistas como de encendido cuando no es así.
                /*if (from.contentEquals("4/5/01") && mensajeLog2.contentEquals("True")){
                    inicializacion.stop(); //Para el timer por si se habia activado con un mensaje anterior.
                    siguienteVista=0;  
                    inicializacion.start();//Por eso, volvemos a inicializar las vistas.
                    }*/
                    // La sig. función se encarga de monitorizar el sensor y cambiar su estado si hace falta                
                    monitoriza_sensor(mensajeLog2, VariableGlobal.estancia, VariableGlobal.opc_sens, VariableGlobal.coord);

                    // Se obtiene información acerca del contador que se está tratando
                    aux = fecha_hora() + ": Dirección de grupo: " + from + " - Valor asociado: " + mensajeLog2 + "\n";
                    // Y se muestra esa información en el log de los contadores de la interfaz, además de guardarse en el fichero log de la aplicación
                    synchronized (textArea2) {
                        textArea2.append(aux);
                        escribeMensajeLog(ficheroLOG, aux);
                    }

                    // Cuando el proceso de lectura periódica haya llegado al último contador de la base de datos
                    if (contadores_aparecidos == VariableGlobal.Lcontadores.getAllPoints().length
                            && VariableGlobal.lectura_manual == 1) {

                        // Se guarda en la ristra b la siguiente información: fecha_hora + dirección grupo + valor
                        // de todos los contadores tratados hasta ahora
                        b = fecha_hora() + ": " + b + " " + from + " " + mensajeLog2 + "\n";

                        // Esa misma información se escribe en los ficheros LOG de las medidas de los contadores,
                        // de la aplicación y en el LOG de los contadores (interfaz de la aplicación) si se trata de
                        // una lectura periódica(1), en caso contrario(0) no, pues ya se mostró el valor del contador requerido anteriormente
                        escribeMensajeLog(fichero_contadores, b);
                        escribeMensajeLog(ficheroLOG, b);
                        synchronized (textArea2) {
                            textArea2.append(b);
                        }

                        // Se reinicia la varible hasta la siguiente lectura de contadores
                        contadores_aparecidos = 0;
                        // Se inicializa el buffer donde se guardan los contadores
                        b = "";
                        // Y se vuelve a activar la variable que evita que se escriban 2 espacios
                        VariableGlobal.primera_vez = 1;
                    } // En este caso, todavía no se ha llegado al último contador de la base de datos
                    else {
                        // En el caso de la primera iteración, evitamos que se escriban 2 espacios
                        if (VariableGlobal.primera_vez == 1) {
                            // Se va añadiendo en el buffer la información del contador tratado
                            b = b + from + " " + mensajeLog2;
                            VariableGlobal.primera_vez = 0;
                        } else {
                            // Se va añadiendo en el buffer la información del contador tratado
                            b = b + " " + from + " " + mensajeLog2;
                        }
                    }
                } // Fin del if con las direcciones físicas
            } // Fin del "if (p != null)"
        } catch (EICLException ex) {;
        }
    } // Fin del procedimiento guardaContador

    /*
     * Procedimiento que busca en el vector vecDGs dicha direccion de grupo, para obtener 
     * la posición donde se encuentra y buscar en los vectores vecDGOpcion y vecDgEstanciaGenerica
     * la opcion y la estancia para inicializar correctamente la VariableGlobal.
     * @author Estefanía Tramunt Rubio
     * @param dg Direccion de grupo que estamos buscando en los vectores.
     */
    public void buscaDGeInicializaVars(String dg) {
        int posicion = 0;
        VariableGlobal.opc_sens = 0;//se inicializa a 0 para que funcione bien. Si después encuentra una DG, cambia
        for (int i = 0; i < vecDGS.length; i++) {
            if (vecDGS[i].indexOf(dg) != -1) {
                posicion = i;
                VariableGlobal.estancia = vecDGEstanciaGenerica[posicion];
                VariableGlobal.opc_sens = Integer.parseInt(vecDGOpcion[posicion]);
                VariableGlobal.coord = posicion;
                /*System.out.println("ENCONTRADA:"+dg+" en pos "+posicion);
                System.out.println("            "+VariableGlobal.estancia);
                System.out.println("            "+VariableGlobal.opc_sens);
                System.out.println("            "+vecDGindx[posicion]+" "+vecDGindy[posicion]);*/
                break;
            }
        }
    }

    /*
     * Procedimiento que implementa un timer para que nada más conectarnos al bus, se mande una señal cada segundo
     * por cada dispositivo que tengamos, para actualizar inicialmente las vistas de los dispositivos.
     * Autora: Estefanía Tramunt Rubio
     */
    public void inicializaVistas() {
        //inicializacion = new Timer(intervalo,new ActionListener() // cada "intervalo" segundos (3 está puesto)
        inicializacion = new Timer(1000, new ActionListener() // cada "intervalo" segundos (3 está puesto)
        {

            public void actionPerformed(ActionEvent e) {
                if (siguienteVista >= vecDGnombre.length) {
                    inicializacion.stop();
                } else {
                    //System.out.println("inicializa sig Vista:"+siguienteVista);
                    //Manda una señal por cada dispositivo para inicializar las vistas.
                    if (tunnel == null) {
                        return;// Si NO se ha establecido una conexión,NO SE MANDA LA SEÑAL.
                    }
                    if (VariableGlobal.Lcontadores.getAllPoints().length == 0) {
                        return;// Si NO hay contadores registrados, NO SE MANDA LA SEÑAL.
                    }
                    String nombre = vecDGnombre[siguienteVista];
                    siguienteVista++;
                    // Obtenemos el point asociado al nombre pasado por parámetro
                    try {
                        tuwien.auto.eibpoints.Point p = VariableGlobal.Lcontadores.getPoint(nombre);
                        if (p != null) {
                            PointPDUXlator xlator = PDUXlatorList.getPointPDUXlator(p.getMajorType(), p.getMinorType());
                            xlator.setServiceType(PointPDUXlator.A_GROUPVALUE_READ);
                            // Creamos un nuevo dato con la información del dispositivo
                            CEMI_L_DATA data = new CEMI_L_DATA((byte) CEMI_L_DATA.MC_L_DATAREQ, new EIB_Address(),
                                    p.getDeviceAddress()[0], xlator.getAPDUByteArray());

                            // Se envía el frame y se espera por su confirmación
                            tunnel.sendFrame(data, CEMI_Connection.WAIT_FOR_CONFIRM);
                        }
                    } catch (EICLException ex) {
                        showException(ex.getMessage());
                    }
                }
            }
        });
        inicializacion.start();
    }

    /*
     * Procedimiento que se ejecuta en caso de producirse algún error en la conexión (¿?)
     * @author Domingo Benítez Díaz
     * @param _DisconnectEvent El evento que provoca la desconexión del bus
     */
    void TunnelServerError(DisconnectEvent _DisconnectEvent) {
        // Log de la aplicación
        synchronized (textArea) {
            String mensajeLog2 = fecha_hora() + ": Usuario desconectado: "
                    + _DisconnectEvent.getDisconnectMessage() + "\n";
            textArea.append(mensajeLog2);
            textArea.setCaretPosition(textArea.getText().length());
            // Se guarda el evento en el fichero LOG
            escribeMensajeLog(ficheroLOG, mensajeLog2);
        }
        tunnel = null;
    }

    /*
     * Procedimiento que muestra una excepción producida
     * @author Domingo Benítez Díaz
     * @param _Message El mensaje asociado a la excepción
     */
    public static void showException(String _Message) {
        MessageDialog md = new MessageDialog(_Message, MessageDialog.ERROR_PIC);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = md.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        md.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
        md.setVisible(true);
    }

    /* 
     * Devuelve una string con el nombre del fichero LOG que está en uso
     * @author Domingo Benítez Díaz
     */
    public String getLOG() {
        return ficheroLOG;
    }

    /*
     * Función que pasa un conjunto de bytes a una string
     * @author Domingo Benítez Díaz
     * @param _Buffer El vector de bytes
     */
    public String BytesToString(byte[] _Buffer) {
        String dummy = "";

        for (int i = 2; i < _Buffer.length; i++) {
            short help = _Buffer[i];
            // Cuando el valor del byte es inferior a 16, el byte lo transforma 
            // en un solo dígito. Por eso se le añade un "0" para que aparezcan
            // dos dígitos en los número inferiores a 16.
            if (help < 16 && help > -1) {
                dummy = dummy + "0" + Integer.toHexString(help & 0x00FF);
            } else {
                dummy = dummy + "" + Integer.toHexString(help & 0x00FF);
            }
        }
        return dummy;
    }

    /* 
     * Función que remplaaza dentro de una ristra, una subristra por otra
     * @author Función encontrada en Internet
     * param str La ristra donde se realizará la búsqueda
     * param pattern La subristra a buscar
     * param replace La subristra que se pondrá en su lugar
     */
    static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /*
     * Función que cambia la codificación de un fichero, pasando a ser de UTF-8 a iso-8859-1
     * @author David Monné Chávez
     * param archivo El fichero cuya codificación se quiere cambiar
     */
    public void cambia_codificacion(File archivo) {
        String linea = "";
        String aux = "";
        // El bloque try-catch es necesario para capturar las excepciones
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            // Obtenemos la primera linea del fichero (donde está señalada la codificación)
            linea = br.readLine();
            // Y almacenamos en aux la línea con la codificación cambiada (función replace)
            aux = replace(linea, "UTF-8", "iso-8859-1");

            // El fichero temp contendrá el mismo contenido que archivo salvo la primera línea
            // Es una forma de guardar de manera temporal el contenido de archivo, ya que posteriormente se eliminará
            FileWriter FW = new FileWriter("temp.xml");
            PrintWriter PW = new PrintWriter(FW);
            // Leemos la segunda línea del fichero
            linea = br.readLine();
            // Y mientras no lleguemos al final del mismo
            while (linea != null) {
                // Vamos copiando en temp.xml el contenido del mismo
                PW.println(linea);
                linea = br.readLine();
            }
            // Cerramos los descriptores de los ficheros (archivo y temp)
            PW.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // En esta sección, añadimos la primera linea con la codificación correcta a archivo
        // Pero desaparece el resto del contenido, de ahí que lo copiáramos anteriormente en temp.xml
        try {
            FileWriter fw = new FileWriter(archivo);
            PrintWriter pw = new PrintWriter(fw);
            // Escribe al primera linea, pero el resto del fichero se trunca
            pw.println(aux);
            // Cerramos los descriptores
            pw.flush();
            fw.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        // LLamamos a esta función para insertar en archivo todo el contenido que falta
        anyade_resto(archivo);
    } // fin de la función cambia_codificación

    /*
     * Función que añade en un fichero pasado por parámetro el contenido del fichero temp.xml
     * @author David Monné Chávez
     * param archivo El fichero pasado por parámetro
     */
    public void anyade_resto(File archivo) {
        String linea = "";

        try {
            // Creamos un fichero de acceso aleatorio
            RandomAccessFile miRAFile;
            miRAFile = new RandomAccessFile(archivo, "rw");
            // Nos situamos al final del mismo (en este caso de archivo)
            miRAFile.seek(archivo.length());

            // Y por otro lado abrimos el fichero temp.xml
            FileReader fr = new FileReader("temp.xml");
            BufferedReader br = new BufferedReader(fr);
            // Y vamos leyendo línea por línea
            linea = br.readLine();
            while (linea != null) {
                // Al mismo tiempo que las vamos añadiendo al fichero de acceso aleatorio (archivo)
                miRAFile.writeBytes(linea + "\n");
                linea = br.readLine();
            }
            // Cerramos los descriptores de fichero
            br.close();
            miRAFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* Procedimiento que se encarga de cambiar el estado de los iconos de representación
     * al monitorizar un sensor/actuador según el valor que se haya leído
     * @author David Monné Chávez
     * @author Estefanía Tramunt Rubio --> Se añadió la monitorización de los actuadores
     * @param valor El valor de respuesta que se ha obtenido a través del bus domótico
     * @param estancia La estancia en la que se encuentra el sensor monitorizado
     * @param opcion Nos servirá para saber sobre qué icono actuar
     * @param ind El índice utilizado en los vectores para saber las coordenadas del icono
     */
    public void monitoriza_sensor(String valor, EstanciaGenerica estancia, int opcion, int ind) {
        switch (opcion) {
            case 1: // Movimiento 1
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoMov1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prdetec.png")));
                    //estancia.jLIconoMov1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (1):");
                    estancia.jLResMovimiento.setText("Sí");

                } // Si es False, es porque no se ha detectado ninguna presencia
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoMov1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prnodetec.png")));
                    //estancia.jLIconoMov1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (1):");
                    estancia.jLResMovimiento.setText("No");
                }
                // printtt
                //System.out.println ("timer_mov1: stop, timer_mov2: start");
                //estancia.timer_mov1.stop();
                //estancia.timer_mov2.start();
                break;

            case 2: // Movimiento 2
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoMov2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prdetec.png")));
                    //estancia.jLIconoMov2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (2):");
                    estancia.jLResMovimiento.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna presencia
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoMov2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prnodetec.png")));
                    //estancia.jLIconoMov2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (2):");
                    estancia.jLResMovimiento.setText("No");
                }
                // printtt
                //System.out.println ("timer_mov2: stop, timer_mov3: start");
                //estancia.timer_mov2.stop();
                //estancia.timer_mov3.start();
                break;

            case 3: // Movimiento 3
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoMov3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prdetec.png")));
                    //estancia.jLIconoMov3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (3):");
                    estancia.jLResMovimiento.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna presencia
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoMov3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prnodetec.png")));
                    //estancia.jLIconoMov3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (3):");
                    estancia.jLResMovimiento.setText("No");
                }
                // printtt
                //System.out.println ("timer_mov3: stop, timer_mov4: start");
                //estancia.timer_mov3.stop();
                //estancia.timer_mov4.start();
                break;

            case 4: // Movimiento 4
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoMov4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prdetec.png")));
                    //estancia.jLIconoMov4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (4):");
                    estancia.jLResMovimiento.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna presencia
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoMov4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prnodetec.png")));
                    //estancia.jLIconoMov4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (4):");
                    estancia.jLResMovimiento.setText("No");
                }
                // printtt
                //System.out.println ("timer_mov4: stop, timer_mov5: start");
                //estancia.timer_mov4.stop();
                //estancia.timer_mov5.start();
                break;

            case 5: // Movimiento 5
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoMov5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prdetec.png")));
                    //estancia.jLIconoMov5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (5):");
                    estancia.jLResMovimiento.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna presencia
                else if (valor.indexOf("False") != -1) {
                    System.out.println("presencia NO detectada");
                    estancia.jLIconoMov5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"prnodetec.png")));
                    //estancia.jLIconoMov5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstMovimiento.setText("Presencia detectada (5):");
                    estancia.jLResMovimiento.setText("No");
                }
                // printtt
                //System.out.println ("timer_mov5: stop, timer_puerta1: start");
                //estancia.timer_mov5.stop();
                //estancia.timer_puerta1.start();
                break;

            case 6: // Puerta 1
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoPuerta1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pua.png")));
                    //estancia.jLIconoPuerta1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (1):");
                    estancia.jLResApertura.setText("Abierta");
                } // Si es False, es porque la puerta se encuentra cerrada
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoPuerta1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"puc.png")));
                    //estancia.jLIconoPuerta1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (1):");
                    estancia.jLResApertura.setText("Cerrada");
                }
                // printtt
                //System.out.println ("timer_puerta1: stop, timer_puerta2: start");
                //estancia.timer_puerta1.stop();
                //estancia.timer_puerta2.start();
                break;

            case 7: // Puerta 2
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoPuerta2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pua2.png")));
                    //estancia.jLIconoPuerta2.setIcon(new javax.swing.ImageIcon(SkoaMain.ROOT_ICONS_PATH+"pua.png"));
                    //estancia.jLIconoPuerta2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    //estancia.jLIconoPuerta2.setBounds(Integer.parseInt(vecDGindx[ind]), Integer.parseInt(vecDGindy[ind]), Integer.parseInt(vecDGancho[ind]), Integer.parseInt(vecDGalto[ind]));

                    estancia.jLEstApertura.setText("Estado de la puerta (2):");
                    estancia.jLResApertura.setText("Abierta");
                } // Si es False, es porque la puerta se encuentra cerrada
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoPuerta2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"puc2.png")));
                    //estancia.jLIconoPuerta2.setIcon(new javax.swing.ImageIcon(SkoaMain.ROOT_ICONS_PATH+"puc.png"));
                    //estancia.jLIconoPuerta2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    //estancia.jLIconoPuerta2.setBounds(Integer.parseInt(vecDGindx[ind]), Integer.parseInt(vecDGindy[ind]), Integer.parseInt(vecDGancho[ind]), Integer.parseInt(vecDGalto[ind]));

                    estancia.jLEstApertura.setText("Estado de la puerta (2):");
                    estancia.jLResApertura.setText("Cerrada");
                }
                // printtt
                //System.out.println ("timer_puerta2: stop, timer_puerta3: start");
                //estancia.timer_puerta2.stop();
                //estancia.timer_puerta3.start();
                break;

            case 8: // Puerta 3
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoPuerta3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pua.png")));
                    //estancia.jLIconoPuerta3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (3):");
                    estancia.jLResApertura.setText("Abierta");
                } // Si es False, es porque la puerta se encuentra cerrada
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoPuerta3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"puc.png")));
                    //estancia.jLIconoPuerta3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (3):");
                    estancia.jLResApertura.setText("Cerrada");
                }
                // printtt
                //System.out.println ("timer_puerta3: stop, timer_puerta4: start");
                //estancia.timer_puerta3.stop();
                //estancia.timer_puerta4.start();
                break;

            case 9: // Puerta 4
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoPuerta4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pua.png")));
                    //estancia.jLIconoPuerta4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (4):");
                    estancia.jLResApertura.setText("Abierta");
                } // Si es False, es porque la puerta se encuentra cerrada
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoPuerta4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"puc.png")));
                    //estancia.jLIconoPuerta4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (4):");
                    estancia.jLResApertura.setText("Cerrada");
                }
                // printtt
                //System.out.println ("timer_puerta4: stop, timer_puerta5: start");
                //estancia.timer_puerta4.stop();
                //estancia.timer_puerta5.start();
                break;

            case 10: // Puerta 5
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoPuerta5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pua.png")));
                    //estancia.jLIconoPuerta5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (5):");
                    estancia.jLResApertura.setText("Abierta");
                } // Si es False, es porque la puerta se encuentra cerrada
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoPuerta5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"puc.png")));
                    //estancia.jLIconoPuerta5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstApertura.setText("Estado de la puerta (5):");
                    estancia.jLResApertura.setText("Cerrada");
                }
                //estancia.timer_puerta5.stop();
                //estancia.timer_inund1.start();
                break;

            case 11: // Inundación 1
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoInund1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_si.png")));
                    //estancia.jLIconoInund1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (1):");
                    estancia.jLResInundacion.setText("Sí");

                    System.out.println("FUGA DETECTADA: estancia.jLEstInundacion: " + estancia.jLEstInundacion.getText());

                    if (NuevoProyecto.mail_enviado == 0 && estancia.jLEstInundacion.getText().equals("Fuga detectada (1):") && (estancia.jLResInundacion.getText().equals("Sí"))) {
                        EnviarMail.main();
                        NuevoProyecto.mail_enviado = 1;
                    }
                } // Si es False, es porque no se ha detectado ninguna fuga
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoInund1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_no.png")));
                    //estancia.jLIconoInund1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (1):");
                    estancia.jLResInundacion.setText("No");
                }
                //estancia.timer_inund1.stop();
                //estancia.timer_inund2.start();
                break;

            case 12: // Inundación 2
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoInund2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_si.png")));
                    //estancia.jLIconoInund2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (2):");
                    estancia.jLResInundacion.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna fuga
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoInund2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_no.png")));
                    //estancia.jLIconoInund2.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (2):");
                    estancia.jLResInundacion.setText("No");
                }
                //estancia.timer_inund2.stop();
                //estancia.timer_inund3.start();
                break;

            case 13: // Inundación 3
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoInund3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_si.png")));
                    //estancia.jLIconoInund3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (3):");
                    estancia.jLResInundacion.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna fuga
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoInund3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_no.png")));
                    //estancia.jLIconoInund3.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (3):");
                    estancia.jLResInundacion.setText("No");
                }
                //estancia.timer_inund3.stop();
                //estancia.timer_inund4.start();
                break;

            case 14: // Inundación 4
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoInund4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_si.png")));
                    //estancia.jLIconoInund4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (4):");
                    estancia.jLResInundacion.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna fuga
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoInund4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_no.png")));
                    //estancia.jLIconoInund4.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (4):");
                    estancia.jLResInundacion.setText("No");
                }
                //estancia.timer_inund4.stop();
                //estancia.timer_inund5.start();
                break;

            case 15: // Inundación 5
                if (valor.indexOf("True") != -1) {
                    // Así que actualizamos el icono y el texto informativo
                    estancia.jLIconoInund5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_si.png")));
                    //estancia.jLIconoInund5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (5):");
                    estancia.jLResInundacion.setText("Sí");
                } // Si es False, es porque no se ha detectado ninguna fuga
                else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoInund5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"inund_no.png")));
                    //estancia.jLIconoInund5.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                    estancia.jLEstInundacion.setText("Fuga detectada (5):");
                    estancia.jLResInundacion.setText("No");
                }
                //estancia.timer_inund5.stop();
                //estancia.timer_temp.start();
                break;

            case 16: // Temperatura 1 (sólo hay uno por estancia como mucho)
                // No es necesario cambiar el icono, ya que siempre es el mismo y ya está dibujado
                //estancia.jLIconoTemp1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"term.png")));
                //estancia.jLIconoTemp1.setBounds(Integer.parseInt(vecEje_x[ind]), Integer.parseInt(vecEje_y[ind]), Integer.parseInt(vecAncho[ind]), Integer.parseInt(vecAlto[ind]));
                estancia.jLResTemp.setText(valor);
                //estancia.timer_temp.stop();
                //estancia.timer_comb.start();
                break;

            case 17: // Combinado 1 (sólo hay uno por estancia como mucho)
                // HABLAR CON DOMINGO A VER QUÉ PASA CON ESTO ...
                estancia.jLResTempComb.setText(valor);
                estancia.jLResLumComb.setText(valor);
                //estancia.timer_comb.stop();
                //estancia.timer_cont.start();
                break;

            case 18: // Contador 1 (sólo hay uno por estancia como mucho)
                estancia.jLResPotencia.setText(valor);
                //estancia.timer_cont.stop();
                //estancia.timer_mov1.start(); //Estefania:
                //estancia.timer_luzc1.start(); //Para que continue con los actuadores
                break;
            //----------------------------------------------------------------------
            //ESTEFANÍA. Añadidos para la monitorizacion_continua de los actuadores.
            //Del 19 al 23, luces conmutables.
            //Del 24 al 28, persianas.
            case 19:
                if (valor.indexOf("True") != -1) {
                    estancia.jLIconoConm1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                } else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoConm1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                }
                //estancia.timer_luzc1.stop();
                //estancia.timer_luzc2.start();
                break;
            case 20:
                if (valor.indexOf("True") != -1) {
                    estancia.jLIconoConm2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                } else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoConm2.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                }
                //estancia.timer_luzc2.stop();
                //estancia.timer_luzc3.start();
                break;
            case 21:
                if (valor.indexOf("True") != -1) {
                    estancia.jLIconoConm3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                } else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoConm3.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                }
                //estancia.timer_luzc3.stop();
                //estancia.timer_luzc4.start();
                break;
            case 22:
                if (valor.indexOf("True") != -1) {
                    estancia.jLIconoConm4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                } else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoConm4.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                }
                //estancia.timer_luzc4.stop();
                //estancia.timer_luzc5.start();
                break;
            case 23:
                if (valor.indexOf("True") != -1) {
                    estancia.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bce.png")));
                } else if (valor.indexOf("False") != -1) {
                    estancia.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"bca.png")));
                }
                //estancia.timer_luzc5.stop();
                //estancia.timer_mov1.start();
                //estancia.timer_pers1.start();
                break;
            case 24:
                if (valor.indexOf("Up") != -1) {
                    estancia.jLIconoPers1.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pa.png")));
                } else if (valor.indexOf("Down") != -1) {
                    estancia.jLIconoConm5.setIcon(new javax.swing.ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH+"pc.png")));
                }
                //estancia.timer_pers1.stop();
                //estancia.timer_mov1.start();
                break;
            default:
                break;
        } // fin del switch
    }

    /* Procedimiento que reinicia todas las variables globales, asignándoles su valor inicial
     * @author David Monné Chávez
     */
    public void reinicia_VariablesGlobales() {
        VariableGlobal.indVA = 0;
        VariableGlobal.indVN = 0;
        VariableGlobal.indVI = 0;
        VariableGlobal.indVReg = 0;
        VariableGlobal.indVConm = 0;
        VariableGlobal.indVPer = 0;
        VariableGlobal.indVElec = 0;
        VariableGlobal.indVPuert = 0;
        VariableGlobal.indVMov = 0;
        VariableGlobal.indVTemp = 0;
        VariableGlobal.indVComb = 0;
        VariableGlobal.indVInund = 0;
        VariableGlobal.indVCont = 0;
        VariableGlobal.num_estancias_p0 = 0;
        VariableGlobal.num_estancias_p1 = 0;
        VariableGlobal.indVNLR = 0;
        VariableGlobal.indVNLC = 0;
        VariableGlobal.indVNPers = 0;
        VariableGlobal.indVNPuer = 0;
        VariableGlobal.indVNE = 0;
        VariableGlobal.indVNPres = 0;
        VariableGlobal.indVEje_x = 0;
        VariableGlobal.indVEje_y = 0;
        VariableGlobal.indVAncho = 0;
        VariableGlobal.indVAlto = 0;
        VariableGlobal.indVIcono = 0;
        VariableGlobal.indDibujaIconos = 0;
        VariableGlobal.numTotalIconos = 0;
        VariableGlobal.AuxVIcono = 0;
        VariableGlobal.cont_est = 0;
        VariableGlobal.indVecNumEst = 0;
        VariableGlobal.comodin = 0;
        VariableGlobal.opcion = 0;
        VariableGlobal.tipo_actual = "";
        VariableGlobal.ind_reg = 0;
        VariableGlobal.ind_conm = 5;
        VariableGlobal.ind_pers = 10;
        VariableGlobal.ind_puert = 15;
        VariableGlobal.ind_mov = 20;
        VariableGlobal.ind_temp = 25;
        VariableGlobal.ind_comb = 30;
        VariableGlobal.ind_inund = 35;
        VariableGlobal.ind_cont = 40;
        VariableGlobal.ind_electr = 45;
        VariableGlobal.ind_reg_aux = 5;
        VariableGlobal.ind_conm_aux = 10;
        VariableGlobal.ind_pers_aux = 15;
        VariableGlobal.ind_puert_aux = 20;
        VariableGlobal.ind_mov_aux = 25;
        VariableGlobal.ind_temp_aux = 30;
        VariableGlobal.ind_comb_aux = 35;
        VariableGlobal.ind_inund_aux = 40;
        VariableGlobal.ind_cont_aux = 45;
        VariableGlobal.ind_electr_aux = 50;
        VariableGlobal.indVNIE = 0;
        VariableGlobal.contador = 0;
        VariableGlobal.lectura_manual = 0;
        VariableGlobal.desconexion = 0;
        VariableGlobal.primera_vez = 1;
        VariableGlobal.codificacion = 1;
        // En un vector obtenemos todos los puntos
        tuwien.auto.eibpoints.Point[] p = VariableGlobal.Lcontadores.getAllPoints();
        // Luego recorremos dicho vector y vamos eliminando punto por punto todos ellos
        for (int i = 0; i < p.length; i++) {
            VariableGlobal.Lcontadores.removePoint(p[i]);
        }
        VariableGlobal.indVDispDom = 0;
        VariableGlobal.indVDispUsad = 0;
        // VariableGlobal.estancia. ¿?
        VariableGlobal.coord = 0;
        VariableGlobal.opc_sens = 0;
        VariableGlobal.indVEstTim = 0;
    }

    /* Procedimiento que reinicia otras variables usadas por otros ficheros
     * @author David Monné Chávez
     */
    public void reinicia_OtrasVariables() {
        ///////////////////////// MAIPEZVIEW.JAVA ////////////////////////////////

        // Variables que corresponen a las diferentes plantas de la vivienda
        tpPlanta01 = new javax.swing.JTabbedPane();
        tpPlanta02 = new javax.swing.JTabbedPane();
        tpPlanta03 = new javax.swing.JTabbedPane();
        tpPlanta04 = new javax.swing.JTabbedPane();
        tpPlanta05 = new javax.swing.JTabbedPane();

        // Inicializamos el vector vecDispUsados
        int aux = 0;
        while (aux < vecDispUsados.length) {
            vecDispUsados[aux] = "";
            vecDispUsados[aux + 1] = "";
            vecDispUsados[aux + 2] = Integer.toString(0);
            vecDispUsados[aux + 3] = Integer.toString(0);
            vecDispUsados[aux + 4] = Integer.toString(0);
            vecDispUsados[aux + 5] = Integer.toString(0);
            vecDispUsados[aux + 6] = Integer.toString(0);
            vecDispUsados[aux + 7] = Integer.toString(0);
            vecDispUsados[aux + 8] = Integer.toString(0);
            vecDispUsados[aux + 9] = Integer.toString(0);
            vecDispUsados[aux + 10] = Integer.toString(0);
            vecDispUsados[aux + 11] = Integer.toString(0);
            aux = aux + 12;
        }

        // Y el vector vecDispDomoticos
        for (int d = 0; d < vecDispDomoticos.length; d++) {
            vecDispDomoticos[d] = "";
        }

        // Inicialización de variables gestionadas en Netbeans
        initComponents();

        RutaNombreHotel = "";
        Fcontadores_MCA_cnf = "";
        fichero_contadores = "";
        //-----------------------------------------------------------------------------------------------------------!!!!!!!!!!!!!!!
//        raiz_ficheros = "C:\\David\\Universidad 08-09\\PI\\PFC\\Aplicaciones\\maipez\\maipez\\";
//        fichero_MCA_cnf = raiz_ficheros + "MCA.cnf";


        dir_iniciall = new File("./");
        raiz_ficheros = dir_iniciall.getAbsolutePath(); //direccion/.
        //quitarle el punto final a la direccion actual
        int longitud = raiz_ficheros.length();
        longitud = longitud - 1;
        raiz_ficheros = raiz_ficheros.substring(0, longitud);
//         System.out.println("directorio actual2:"+raiz_ficheros);    //okkkkk

        fichero_MCA_cnf = raiz_ficheros + "MCA.cnf";



        factory = new Discoverer();

        try {
            // Lee el fichero de configuración (MCA.cnf)
            LeeMCA_cnf(fichero_MCA_cnf);
        } catch (EICLException ex) {
            Logger.getLogger(SkoaMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        IPordenador = tuwien.auto.eicl.struct.eibnetip.util.HPAI.getLocalIP().getHostAddress();
        //System.out.println("IP local ordenador= " + IPordenador );

        // Fichero LOG donde se guardan los resultados de las medidas de los contadores domóticos
        fichero_contadores = raiz_ficheros + Fmedidas_MCA_cnf;

        // Establecemos la ruta del fichero LOG de la apliación
        ficheroLOG = raiz_ficheros + Flog_MCA_cnf;

        // Si en el fichero de configuración MCA.cnf el campo MCA está con el valor "si"
        // se activa el procedimiento de tiempo real MideCuandoArranca (MCAinit)
        if (SkoaMain.MCA_MCA_cnf.contentEquals("si") == true) // Llamada a MCAinit, que se encarga de conectarse al bus, realizar lecturas periódicas,
        // obtener los valores medidos, guardar información den los ficheros de LOG, etc
        {
            MCAinit();
        }

        mainActivado = new VariableGlobal();
        tunnel = null;
        contadores_aparecidos = 0;
        b = "";
        valor_recibido = "";

        // Variables necesarias para la creación dinámica de las pestañas que
        // representarán a las diferentes estancias del hotel
        // ---------------- PRIMERA PLANTA -----------------
        est01p01 = new EstanciaGenerica();
        est02p01 = new EstanciaGenerica();
        est03p01 = new EstanciaGenerica();
        est04p01 = new EstanciaGenerica();
        est05p01 = new EstanciaGenerica();
        est06p01 = new EstanciaGenerica();
        est07p01 = new EstanciaGenerica();
        est08p01 = new EstanciaGenerica();
        est09p01 = new EstanciaGenerica();
        est10p01 = new EstanciaGenerica();
        est11p01 = new EstanciaGenerica();
        est12p01 = new EstanciaGenerica();
        est13p01 = new EstanciaGenerica();
        est14p01 = new EstanciaGenerica();
        est15p01 = new EstanciaGenerica();
        est16p01 = new EstanciaGenerica();
        est17p01 = new EstanciaGenerica();
        est18p01 = new EstanciaGenerica();
        est19p01 = new EstanciaGenerica();
        est20p01 = new EstanciaGenerica();
        est21p01 = new EstanciaGenerica();
        est22p01 = new EstanciaGenerica();
        est23p01 = new EstanciaGenerica();
        est24p01 = new EstanciaGenerica();
        est25p01 = new EstanciaGenerica();
        // ---------------- SEGUNDA PLANTA -----------------
        est01p02 = new EstanciaGenerica();
        est02p02 = new EstanciaGenerica();
        est03p02 = new EstanciaGenerica();
        est04p02 = new EstanciaGenerica();
        est05p02 = new EstanciaGenerica();
        est06p02 = new EstanciaGenerica();
        est07p02 = new EstanciaGenerica();
        est08p02 = new EstanciaGenerica();
        est09p02 = new EstanciaGenerica();
        est10p02 = new EstanciaGenerica();
        est11p02 = new EstanciaGenerica();
        est12p02 = new EstanciaGenerica();
        est13p02 = new EstanciaGenerica();
        est14p02 = new EstanciaGenerica();
        est15p02 = new EstanciaGenerica();
        est16p02 = new EstanciaGenerica();
        est17p02 = new EstanciaGenerica();
        est18p02 = new EstanciaGenerica();
        est19p02 = new EstanciaGenerica();
        est20p02 = new EstanciaGenerica();
        est21p02 = new EstanciaGenerica();
        est22p02 = new EstanciaGenerica();
        est23p02 = new EstanciaGenerica();
        est24p02 = new EstanciaGenerica();
        est25p02 = new EstanciaGenerica();
        // ---------------- TERCERA PLANTA -----------------
        est01p03 = new EstanciaGenerica();
        est02p03 = new EstanciaGenerica();
        est03p03 = new EstanciaGenerica();
        est04p03 = new EstanciaGenerica();
        est05p03 = new EstanciaGenerica();
        est06p03 = new EstanciaGenerica();
        est07p03 = new EstanciaGenerica();
        est08p03 = new EstanciaGenerica();
        est09p03 = new EstanciaGenerica();
        est10p03 = new EstanciaGenerica();
        est11p03 = new EstanciaGenerica();
        est12p03 = new EstanciaGenerica();
        est13p03 = new EstanciaGenerica();
        est14p03 = new EstanciaGenerica();
        est15p03 = new EstanciaGenerica();
        est16p03 = new EstanciaGenerica();
        est17p03 = new EstanciaGenerica();
        est18p03 = new EstanciaGenerica();
        est19p03 = new EstanciaGenerica();
        est20p03 = new EstanciaGenerica();
        est21p03 = new EstanciaGenerica();
        est22p03 = new EstanciaGenerica();
        est23p03 = new EstanciaGenerica();
        est24p03 = new EstanciaGenerica();
        est25p03 = new EstanciaGenerica();
        // ---------------- CUARTA PLANTA ------------------
        est01p04 = new EstanciaGenerica();
        est02p04 = new EstanciaGenerica();
        est03p04 = new EstanciaGenerica();
        est04p04 = new EstanciaGenerica();
        est05p04 = new EstanciaGenerica();
        est06p04 = new EstanciaGenerica();
        est07p04 = new EstanciaGenerica();
        est08p04 = new EstanciaGenerica();
        est09p04 = new EstanciaGenerica();
        est10p04 = new EstanciaGenerica();
        est11p04 = new EstanciaGenerica();
        est12p04 = new EstanciaGenerica();
        est13p04 = new EstanciaGenerica();
        est14p04 = new EstanciaGenerica();
        est15p04 = new EstanciaGenerica();
        est16p04 = new EstanciaGenerica();
        est17p04 = new EstanciaGenerica();
        est18p04 = new EstanciaGenerica();
        est19p04 = new EstanciaGenerica();
        est20p04 = new EstanciaGenerica();
        est21p04 = new EstanciaGenerica();
        est22p04 = new EstanciaGenerica();
        est23p04 = new EstanciaGenerica();
        est24p04 = new EstanciaGenerica();
        est25p04 = new EstanciaGenerica();
        // ---------------- QUINTA PLANTA ------------------
        est01p05 = new EstanciaGenerica();
        est02p05 = new EstanciaGenerica();
        est03p05 = new EstanciaGenerica();
        est04p05 = new EstanciaGenerica();
        est05p05 = new EstanciaGenerica();
        est06p05 = new EstanciaGenerica();
        est07p05 = new EstanciaGenerica();
        est08p05 = new EstanciaGenerica();
        est09p05 = new EstanciaGenerica();
        est10p05 = new EstanciaGenerica();
        est11p05 = new EstanciaGenerica();
        est12p05 = new EstanciaGenerica();
        est13p05 = new EstanciaGenerica();
        est14p05 = new EstanciaGenerica();
        est15p05 = new EstanciaGenerica();
        est16p05 = new EstanciaGenerica();
        est17p05 = new EstanciaGenerica();
        est18p05 = new EstanciaGenerica();
        est19p05 = new EstanciaGenerica();
        est20p05 = new EstanciaGenerica();
        est21p05 = new EstanciaGenerica();
        est22p05 = new EstanciaGenerica();
        est23p05 = new EstanciaGenerica();
        est24p05 = new EstanciaGenerica();
        est25p05 = new EstanciaGenerica();

        ///////////////////////// ADMINISTRAR CONTADORES.JAVA /////////////////////

        AdministrarContadores.list_Point = new javax.swing.JList();

        ///////////////////////// PRINCIPAL.JAVA ////////////////////////////////

        NuevoProyecto.archivo = "";
        NuevoProyecto.nombre_archivo = "";
        NuevoProyecto.nombre_vivienda_fichero = "";
        NuevoProyecto.email_fichero = "";
        NuevoProyecto.eshotel = false;

        ///////////////////////// PRINCIPALGRAFS.JAVA /////////////////////////////

        PrincipalGrafs.nombreFichero = "";
    }

private void jAbrirBDItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAbrirBDItemActionPerformed
// Declaramos una variable de tipo JFileChooser (nos mostrará el cuadro de diálogo típico para abrir ficheros)
    JFileChooser fc = new JFileChooser();
    FileFilter filtro = new ExtensionFileFilter("Archivos con extensión .dat", new String[]{"DAT"});
// Establecemos que sólo se puedan seleccionar ficheros
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
// Y no más de uno al mismo tiempo
    fc.setMultiSelectionEnabled(false);
    fc.setFileFilter(filtro);
// Establecemos cual será el directorio inicial por defecto
// El "." establece el directorio actual del proyecto
    File dir_inicial = new File("./");
    fc.setCurrentDirectory(dir_inicial);

// returnVal almacenará el valor al abrirse el diálogo
    int returnVal = fc.showOpenDialog((java.awt.Component) null);

// Si no ha habido problemas 
    if (returnVal == fc.APPROVE_OPTION) {
        // Obtenemos el fichero seleccionado en archivo
        File archivo = fc.getSelectedFile();
//////////////////////////////////////////////////////////////////    principal.archivo = archivo.getName();
//    System.out.println("archivo seleccionado:"+archivo.getName());
        // Y aquí vendría el código encargado de realizar algo con ese fichero
        try {
            // En este caso, Lcontadores contendrá el contenido del fichero dado por el PointList que se indica
            // "\ denota que lo siguiente va a ser un carácter especial, en este caso la barra inclinada \

            VariableGlobal.Lcontadores = new PointList(fc.getCurrentDirectory().getAbsolutePath() + "\\" + archivo.getName());
            // La ruta sería por ej: C:\David\Universidad 08-09\PI\PFC\Aplicaciones\Borrar\maipez\contadores_norcopia.dat

            //ESTEFANÍA: Al abrir el fichero de contadores, inicializo su nombre porque ha cambiado.
            Fcontadores_MCA_cnf = archivo.getName();
            //--------------------------------------
        } catch (EICLException ex) {
            showException(ex.getMessage());
        }
    }
}//GEN-LAST:event_jAbrirBDItemActionPerformed

private void jGuardarBDItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGuardarBDItemActionPerformed
// Declaramos una variable de tipo JFileChooser (nos mostrará el cuadro de diálogo típico para guardar ficheros)
    JFileChooser fc = new JFileChooser();
    FileFilter filtro = new ExtensionFileFilter("Archivos con extensión .dat", new String[]{"DAT"});
// Establecemos cual será el directorio inicial por defecto
// El "." establece el directorio actual del proyecto
    File dir_inicial = new File("./");
    fc.setCurrentDirectory(dir_inicial);
    fc.setFileFilter(filtro);
// returnVal almacenará el valor al abrirse el diálogo
    int returnVal = fc.showSaveDialog((java.awt.Component) null);

// Si no ha habido problemas, obtenemos el fichero seleccionado en archivo
    if (returnVal == fc.APPROVE_OPTION) {
        File archivo = fc.getSelectedFile();
        // Y aquí vendría el código encargado de realizar algo con ese fichero
        try {
            // Pasamos el contenido del ListPoint Lcontadores (los contadores de
            // la base de datos) al archivo donde se grabarán los datos
            VariableGlobal.Lcontadores.toFile(fc.getCurrentDirectory().getAbsolutePath() + "\\" + archivo.getName());
        } catch (EICLException ex) {
            showException(ex.getMessage());
        }
    }
}//GEN-LAST:event_jGuardarBDItemActionPerformed

private void jbConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConectarActionPerformed

    Conectar conni = new Conectar(this.getFrame(), "Conectado a", true,
            factory, fecha_hora());

    // Para centrar la ventana
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = conni.getSize();
    if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
    }
    conni.setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
    // System.out.println(conni.tunnel);
    conni.setVisible(true);
    // En este momento, se detiene la ejecución del procedimiento 
    // Continúa cuando se pulsa el botón "Conectar"
    // Además, conni.tunnel vale "null", pero luego cuando llega a la
    // instrucción if, vale distinto de "null" siempre y cuando la conexión se realice correctamente

    // En este momento vale distinto de null si se ha realizado correctamente la conexión
    if (conni.tunnel != null) {
        // System.out.println(conni.tunnel);
        tunnel = conni.tunnel;
        tunnel.addFrameListener(new My_EICLEventListener(this));

        // Mensajes a mostrar por la ventana correspondiente al log de la aplicación
        synchronized (textArea) {
            // getLOG devuelve la String del fichero LOG desde la clase conni (Conectar) donde se define esta String
            ficheroLOG = new String(conni.getLOG());
            textArea.setCaretPosition(textArea.getText().length());
        }
    }
    inicializaVecDGnombre();
    //ESTEFANÍA-->DESPÚES DE CONECTAR,PUEDO HACER EL MCAinit_manual() modificado para que solo lanze el backup
    MCAinit_manual();
    //e inicializo las vistas de los dispositivos que tengo.
    inicializaVistas();
}//GEN-LAST:event_jbConectarActionPerformed

    /*
     * Procedimiento que inicializa vecDGnombre a partir del fichero de contadores .dat.
     * Este fichero es creado por SKoA, y el orden de los dispositivos es el mismo orden en que
     * se introdujeron en el fichero .xml.
     * @author Estefanía Tramunt Rubio
     */
    public void inicializaVecDGnombre() {
        for (int i = 0; i < vecDGS.length; i++) {
            vecDGnombre[i] = buscaNombrePorDG(vecDGS[i]);
            System.out.println(vecDGS[i] + " " + vecDGnombre[i] + ".");//La longitud del nombre de la DG es siempre 6.
        }
        System.out.println("Fin inicializa vecDGnombre");
    }

    /*
     * Procedimiento que busca una DG por su nº en el fichero de contadores .dat, y extrae el
     * nombre de la DG.
     * @author Estefanía Tramunt Rubio
     */
    public String buscaNombrePorDG(String dg) {
        // Se lee el fichero de contadores *.dat
        File archivo = new File(raiz_ficheros + Fcontadores_MCA_cnf);
        FileReader fr = null;
        BufferedReader linea = null;
        String line, nombre = "";
        //ABRIR FICHERO Y LEER
        try {
            fr = new FileReader(archivo);
            linea = new BufferedReader(fr);
            line = linea.readLine();//Leemos la primera línea del .dat que no sirve para nada.
            //LEE las lineas en busca del nombre de la DG.
            while ((line = linea.readLine()) != null) {
                //System.out.println(line.substring(9, 15));
                if (line.substring(9, 15).contentEquals(dg)) {
                    nombre = line.substring(0, 6);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();   	//Se cierra si todo va bien.
                }
            } catch (Exception e2) { 			//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
        return nombre;
    }

private void cargarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cargarMenuItemActionPerformed

// En primer lugar, cargaremos el fichero .xml para poder mostrar la representación visual por pantalla
// Declaramos una variable de tipo JFileChooser (nos mostrará el cuadro de diálogo típico para abrir ficheros)
    JFileChooser fc = new JFileChooser();
// Establecemos cual será el filtro para la búsqueda de archivos, ya que sólo nos interesan aquellos con extensión .xml
    FileFilter filtro = new ExtensionFileFilter("Archivos con extension .xml", new String[]{"XML"});

// Establecemos que sólo se puedan seleccionar ficheros
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
// Y no más de uno al mismo tiempo
    fc.setMultiSelectionEnabled(false);
// Aplicamos el filtro
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
//    System.out.println("¿¿¿¿es aki????-"+archivo.getName());
        NuevoProyecto.archivo = archivo.getAbsolutePath();
        NuevoProyecto.nombre_archivo = archivo.getName();

        if (VariableGlobal.codificacion == 1) {
            // Esta función se encarga de cambiar la codificación del fichero, pasando de UTF-8 a iso-8859-1
            // Es necesario ya que cuando colocas actuadores/sensores en un plano y se guardan los cambios,
            // se cambia automáticamente la codificación de iso-8859-1 a UTF-8, por lo que si no se vuelve 
            // a poner en iso-8859-1, fallaría al intentar abrir el fichero
            cambia_codificacion(archivo);
            // Desactivamos la variable global porque ya que se ha cambiado la codificación del archivo
            VariableGlobal.codificacion = 0;
            try {
                // Eliminamos el fichero temporal una vez que lo hemos utilizado
                p = r.exec("cmd /c del \"temp.xml\"");
            } catch (IOException ex) {
                showException(ex.getMessage());
            }
        }

// Nos declaramos una variable con la ruta del archivo de configuración XML
        String xml_in = archivo.getAbsolutePath();

// Ristras que nos ayudarán a saber cuántas plantas y estancias tiene el hotel
        String busca_plantas = "planta";
        String busca_estancias = "estancia";
// Esta otra nos ayudará a saber el número total de iconos que se representarán en la interfaz
        String busca_icono = "label";

// Nos indicará el número de plantas, estancias e iconos que tiene el hotel
        int num_plantas, num_estancias, num_iconos;

        try {
            /* Declaramos una variable de tipo Document y almacenamos en ella el 
             * objeto generado a partir del fichero XML */
            Document input = SkoaMain.readXML(xml_in);

            // Almacenamos el número de plantas y estancias que tendrá el hotel
            num_plantas = SkoaMain.countElements(input, busca_plantas);
            num_estancias = SkoaMain.countElements(input, busca_estancias);
            num_iconos = SkoaMain.countElements(input, busca_icono);
            // La variable global numTotalIconos la utilizaremos más adelante (en la función dibuja_estancias)
            VariableGlobal.numTotalIconos = num_iconos;
            // De momeno no lo borraremos, pero antes estaba así por la marca "fin" que ya no utilizamos
            //VariableGlobal.numTotalIconos = num_iconos + num_estancias;

            //ESTEFANIA
            //Habran tantas DGs como iconos.
            System.out.println("NUM ICONOS=" + num_iconos);
            vecDGS = new String[num_iconos];
            vecDGEstancia = new String[num_iconos];
            vecDGindx = new String[num_iconos];
            vecDGindy = new String[num_iconos];
            vecDGancho = new String[num_iconos];
            vecDGalto = new String[num_iconos];
            vecDGEstanciaGenerica = new EstanciaGenerica[num_iconos];
            vecDGOpcion = new String[num_iconos];
            vecDGnombre = new String[num_iconos];
            //----------------------------------------
            // Establecemos el tamaño de los vectores
            vecAliasPlantas = new String[num_plantas];
            vecNombresEstancias = new String[num_estancias];
            vecRutasPlanos = new String[num_estancias];
            vecRegulacion = new String[num_estancias];
            vecConmutacion = new String[num_estancias];
            vecPersianas = new String[num_estancias];
            vecElectrovalvulas = new String[num_estancias];
            vecPuertas = new String[num_estancias];
            vecMovimiento = new String[num_estancias];
            vecTemperatura = new String[num_estancias];
            vecCombinado = new String[num_estancias];
            vecInundacion = new String[num_estancias];
            vecContadores = new String[num_estancias];
            vecNumPersianas = new String[num_estancias];
            vecNumLucReg = new String[num_estancias];
            vecNumLucConm = new String[num_estancias];
            vecNumElectr = new String[num_estancias];
            vecNumPuertas = new String[num_estancias];
            vecNumPresencias = new String[num_estancias];
            // Suponiendo que habrá un máx. de 5 iconos por tipo (luz, persiana, puerta, etc), teniendo en cuenta que son 10 iconos distintos, serían 50 en total
            vecEje_x = new String[num_estancias * 50];
            vecEje_y = new String[num_estancias * 50];
            vecAncho = new String[num_estancias * 50];
            vecAlto = new String[num_estancias * 50];
            vecIcono = new String[num_iconos];
            vecNumEstancias = new int[num_plantas];
            vecNumIconosEstancia = new int[num_estancias];

            // Obtenemos las clases de los diferentes nodos
            SkoaMain.showClassesForNodes(input);
            // El tamaño de vecEstanciasTimers vendrá determinado por el número de estancias
            vecEstanciasTimers = new EstanciaGenerica[vecNombresEstancias.length];

            // Almacenamos cuantas estancias hay en la última planta (las anteriores se han tratado en la llamada a "showClassesForNodes"
           
            vecNumEstancias[VariableGlobal.indVecNumEst] = VariableGlobal.cont_est;
            VariableGlobal.cont_est = 0;

            // Almacenamos cuantos iconos hay en la última estancia (las anteriores se han tratado en la llamada a "obtenAtributo"
            vecNumIconosEstancia[VariableGlobal.indVNIE] = VariableGlobal.contador;
            VariableGlobal.contador = 0;

            /* Llegados a este punto, los vectores contendrán los nombres de las
             * diferentes plantas, estancias, rutas de imágenes, actuadores/sensores
             * y cantidad de elementos en cada estancia para saber qué mostrar en la GUI */

            // MONNE
    /*for (int i=0; i<vecIcono.length; i++)
            System.out.println(vecIcono[i]);*/

            // Reiniciamos los valores de los índices de algunos vectores para la posterior utilización de la función dibuja_iconos
            reiniciaIndices();

            // Capturamos las posibles excepciones
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Declaramos e inicializamos el vector de las plantas
        javax.swing.JTabbedPane[] vectpPlantas = {tpPlanta01, tpPlanta02, tpPlanta03, tpPlanta04, tpPlanta05};
        // Y el de las estancias genéricas
        EstanciaGenerica[] vecEstancias = {est01p01, est02p01, est03p01, est04p01, est05p01, est06p01, est07p01, est08p01, est09p01, est10p01, est11p01, est12p01, est13p01, est14p01, est15p01, est16p01, est17p01, est18p01, est19p01, est20p01, est21p01, est22p01, est23p01, est24p01, est25p01, est01p02, est02p02, est03p02, est04p02, est05p02, est06p02, est07p02, est08p02, est09p02, est10p02, est11p02, est12p02, est13p02, est14p02, est15p02, est16p02, est17p02, est18p02, est19p02, est20p02, est21p02, est22p02, est23p02, est24p02, est25p02, est01p03, est02p03, est03p03, est04p03, est05p03, est06p03, est07p03, est08p03, est09p03, est10p03, est11p03, est12p03, est13p03, est14p03, est15p03, est16p03, est17p03, est18p03, est19p03, est20p03, est21p03, est22p03, est23p03, est24p03, est25p03, est01p04, est02p04, est03p04, est04p04, est05p04, est06p04, est07p04, est08p04, est09p04, est10p04, est11p04, est12p04, est13p04, est14p04, est15p04, est16p04, est17p04, est18p04, est19p04, est20p04, est21p04, est22p04, est23p04, est24p04, est25p04, est01p05, est02p05, est03p05, est04p05, est05p05, est06p05, est07p05, est08p05, est09p05, est10p05, est11p05, est12p05, est13p05, est14p05, est15p05, est16p05, est17p05, est18p05, est19p05, est20p05, est21p05, est22p05, est23p05, est24p05, est25p05};

        // Asignamos los diferentes nombres a las plantas
        // Nos será de gran utilidad para saber el nombre de la planta con la que interactuará el usuario a través de la interfaz
        // Gracias a este nombre y a otros parámetros, será posible obtener el "point" adecuado a la hora de interactuar con un act/sen
        for (int i = 0; i < vecAliasPlantas.length; i++) {
            vectpPlantas[i].setName(vecAliasPlantas[i]);
        }



        // Mismo procedimiento para las estancias
        // Con la salvedad de que tenemos que tener cuidado cuando terminamos de establecer los nombres de las estancias de una planta
        // Ya que de ahí hasta 25 (máx. estancias por planta) le pondremos el nombre ""
        // Y luego seguimos estaleciendo los nombres adecuados para las estancias de la sig. planta
        int i = 0, planta = 0, est_tratadas = 0, nombres_validos = 0;
        /**/
        // Condición de parada del bucle: que hayamos establecido todos los nombres de las estancias
        while (nombres_validos < vecNombresEstancias.length) {
            // Este bucle establece los nombres de todas las estancias de una planta
            while (i < vecNumEstancias[planta]) {
                vecEstancias[est_tratadas].setName(vecNombresEstancias[nombres_validos]);

                i++;
                est_tratadas++;
                nombres_validos++;

            }


            // Y en caso de que dicha planta tenga menos de 25 estancias, al resto les asigna el nombre ""
            while (i < 25) {
                vecEstancias[est_tratadas].setName("");
                i++;
                est_tratadas++;
            }
            // Pasamos a la siguiente planta y reiniciamos la variable que lleva la cuenta de las estancias de una planta
            planta++;
            i = 0;
        }
        //ESTEFANIA:
        //En base a los nombres de las estancias de cada DG, sacamos su estanciaGenerica.
        for (int k = 0; k < vecDGEstancia.length; k++) {
            for (int l = 0; l < vecEstancias.length; l++) {
                String a = vecEstancias[l].getName();
                if (a.indexOf(vecDGEstancia[k]) != -1) {
                    System.out.println(k + "  COINCIDEN " + a + " " + vecDGEstancia[k]);
                    vecDGEstanciaGenerica[k] = vecEstancias[l];
                }
            }
        }

        // Añadimos las pestañas verticales que representarán las diferentes plantas del hotel
        int contador = 0;


//        System.out.println("num plantas:"+numPlantas[0]);
        for (int ii = 0; ii < vectpPlantas.length; ii++) {
            // Mientras haya elementos en el vector vecAliasPlantas, se mostrarán en la interfaz
            tpPlantas.addTab(vecAliasPlantas[contador], vectpPlantas[ii]);
            contador++;
            // Cuando ya no haya elementos en dicho vector, se saldrá del bucle
            if (contador >= vecAliasPlantas.length) {
                break;
            }
        }

        // Una vez añadidas las pestañas verticales de las diferentes plantas del hotel
        // Añadiremos las diferentes estancias (pestañas horizontales) para cada planta
        int aux_plantas = 0; // Para saber cuántas plantas del hotel tratar
        int aux_estancias = 0; // Para saber cuántas estancias de la planta tratar
        int aux = 0; // Nos servirá para utilizarlo como ayuda para el índice del vector vecEstancias

        // Mientras no se sobrepase el número total de plantas, trataremos cada una de ellas por separado
        // Para añadir las diferentes estancias de dicha planta

//        System.out.println("longitud plantas:"+vecAliasPlantas.length);

        while (aux_plantas < vecAliasPlantas.length) {
            // Dentro de cada planta, trataremos las diferentes estancias
            for (int ind_est = 0; ind_est < 25; ind_est++) {
                // LLamaremos a la función encargada de dibujar las diferentes estancias
                dibuja_estancias(aux_estancias, vectpPlantas[aux_plantas], vecEstancias[ind_est + aux], aux_plantas);
                aux_estancias++;
            }
            // Llegados a este punto, asignamos a la variable aux_estancias el número total
            // de estancias tratadas en las diferentes plantas (estancias reales, pq no tienen que ser necesariamente 25 por planta)
            int auxiliar = 0;
            for (int i2 = 0; i2 <= aux_plantas; i2++) {
                auxiliar = auxiliar + vecNumEstancias[i2];
            }
            aux_estancias = auxiliar;

            // Se incrementa el núm de plantas (ahora pasaremos a tratar la siguiente planta)
            aux_plantas++;
            // Actualizamos la variable aux que sirve de apoyo para calcular el índice del vector vecEstancias
            aux = aux + 25;
        }
        // Ahora mismo, está preparado para admitir un máx. de 5 plantas y 25 estancias por planta 

        // Establecemos las características del logo de la aplicación
        jlMaipez.setBackground(new java.awt.Color(255, 255, 153));
        jlMaipez.setFont(new Font("Bookman Old Style", Font.BOLD, 32));
        jlMaipez.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        // El texto a mostrar será el que se indique en el fichero .xml
        jlMaipez.setText(RutaNombreHotel);
        jlMaipez.setOpaque(true);
        jlMaipez.setName("jlMaipez");
        // Cargamos los vectores vecDispDomoticos y vecDispUsados así como sus índices a través del fichero info.txt
        recupera_info();
    }
    //Estefanía-->Para conectar automaticamente y lanzar los backups.
    //Se ha comentado, para que el usuario pueda cargar los contadores él mismo, y conectarse al bus.
    //MCAinit_manual();//Similar a MCAinit(), pero no realiza las lecturas periodicas de los contadores.
}//GEN-LAST:event_cargarMenuItemActionPerformed

private void verConfigItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verConfigItemActionPerformed
    Config.main();
}//GEN-LAST:event_verConfigItemActionPerformed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
// En caso de seleccionar un nuevo proyecto, se ejecutará uno de los módulos implementados por Inma
    NuevoProyecto nuevo_proyecto = new NuevoProyecto(this.getFrame(), true);
    nuevo_proyecto.setVisible(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jbDesconectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDesconectarActionPerformed
    try {
        if (tunnel != null) {
            tunnel.disconnect("solicitud realizada por el usuario");
        }
    } catch (EICLException ex) {
        showException(ex.getMessage());
    }
    // Activamos (valor a 8) la variable global que nos servirá para detener las lecturas periódicas en caso
    // de que estén activadas y el usuario se haya olvidado de desactivarlas
    VariableGlobal.desconexion = 8;
    jlEstadoServ_res.setText("No conectado");
    // Y paramos los diferentes "timers" que controlan la monitorización continua de los sensores
    deten_timers();
}//GEN-LAST:event_jbDesconectarActionPerformed

private void conectarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectarMenuItemActionPerformed
    Conectar conni = new Conectar(this.getFrame(), "Conectado a", true,
            factory, fecha_hora());

    // Para centrar la ventana
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = conni.getSize();
    if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
    }
    conni.setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
    // System.out.println(conni.tunnel);
    conni.setVisible(true);
    // En este momento, se detiene la ejecución del procedimiento 
    // Continúa cuando se pulsa el botón "Conectar"
    // Además, conni.tunnel vale "null", pero luego cuando llega a la
    // instrucción if, vale distinto de "null" siempre y cuando la conexión se realice correctamente

    // En este momento vale distinto de null si se ha realizado correctamente la conexión
    if (conni.tunnel != null) {
        // System.out.println(conni.tunnel);
        tunnel = conni.tunnel;
        tunnel.addFrameListener(new My_EICLEventListener(this));

        // Mensajes a mostrar por la ventana correspondiente al log de la aplicación
        synchronized (textArea) {
            // getLOG devuelve la String del fichero LOG desde la clase conni (Conectar) donde se define esta String
            ficheroLOG = new String(conni.getLOG());
            textArea.setCaretPosition(textArea.getText().length());
            //Muestra el fichero de contadores que realmente se va a utilizar cuando se conecta
            textArea.append(fecha_hora() + ": Se ha realizado la lectura del fichero de contadores = " + raiz_ficheros + Fcontadores_MCA_cnf + " \n");
        }
    }
}//GEN-LAST:event_conectarMenuItemActionPerformed

private void desconectarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desconectarMenuItemActionPerformed
    try {
        if (tunnel != null) {
            tunnel.disconnect("solicitud realizada por el usuario");
        }
    } catch (EICLException ex) {
        showException(ex.getMessage());
    }
    // Activamos (valor a 8) la variable global que nos servirá para detener las lecturas periódicas en caso
    // de que estén activadas y el usuario se haya olvidado de desactivarlas
    VariableGlobal.desconexion = 8;
    jlEstadoServ_res.setText("No conectado");
    // Y paramos los diferentes "timers" que controlan la monitorización continua de los sensores
    deten_timers();
}//GEN-LAST:event_desconectarMenuItemActionPerformed

private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
    try {
        if (tunnel != null) {
            tunnel.disconnect("solicitud realizada por el usuario");
        }
    } catch (EICLException ex) {
        showException(ex.getMessage());
    }

//    System.out.println("quiero salir");

    //************************************************************
    //    DESCONECTAR BUS EIB!!!
//	try {
//            if (tunnel != null)
//                tunnel.disconnect("solicitud realizada por el usuario");
//        } catch (EICLException ex) {
//            showException(ex.getMessage());
//        }
//    // Activamos (valor a 8) la variable global que nos servirá para detener las lecturas periódicas en caso
//    // de que estén activadas y el usuario se haya olvidado de desactivarlas
//    VariableGlobal.desconexion = 8;
//    jlEstadoServ_res.setText("No conectado");
//    // Y paramos los diferentes "timers" que controlan la monitorización continua de los sensores
//    deten_timers();
    //************************************************************

    System.out.println("voi a saliiiiiiiiiii");

    factory.terminate();
    System.exit(0);
}//GEN-LAST:event_exitMenuItemActionPerformed

private void jLeerContItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jLeerContItemActionPerformed
}//GEN-LAST:event_jLeerContItemActionPerformed


public boolean isOpenGraficasMain = false;

private void jGrafsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGrafsMenuActionPerformed
    // Iniciamos el módulo encargado de realizar las gráficas

    /*                                     if(principalGrafs.llamado ==0)
    principalGrafs.main();
    else
    principalGrafs.framePrincipal.setVisible(true);
     */


    //LLAMADA A LA APLICACION DE ESTEFANIA
    //Runtime r = Runtime.getRuntime();
    //try {

        //if(Locale.getDefault().toString().equals("es") ){
            //Process p = r.exec("java -jar ./graficas_esp.jar");
        //}else{
            //Process p = r.exec("java -jar ./graficas_eng.jar");
        //}

    //} catch (IOException ex) {
       // Logger.getLogger(SkoaMain.class.getName()).log(Level.SEVERE, null, ex);
    //}
    
   
     GraficasMain.main(new String[]{});
    

}//GEN-LAST:event_jGrafsMenuActionPerformed

private void jXmlMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXmlMenuActionPerformed
// En primer lugar, cargaremos el fichero .xml
// Declaramos una variable de tipo JFileChooser (nos mostrará el cuadro de diálogo típico para abrir ficheros)
    JFileChooser fc = new JFileChooser();
// Establecemos cual será el filtro para la búsqueda de archivos, ya que sólo nos interesan aquellos con extensión .xml
    FileFilter filtro = new ExtensionFileFilter("Archivos con extensión .xml", new String[]{"XML"});

// Establecemos que sólo se puedan seleccionar ficheros
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
// Y no más de uno al mismo tiempo
    fc.setMultiSelectionEnabled(false);
// Aplicamos el filtro
    fc.setFileFilter(filtro);
// Establecemos cual será el directorio inicial por defecto
// El "." establece el directorio actual del proyecto
    File dir_inicial = new File("./");
    fc.setCurrentDirectory(dir_inicial);

    File archivo = null;

// returnVal almacenará el valor al abrirse el diálogo
    int returnVal = fc.showOpenDialog((java.awt.Component) null);

// Si no ha habido problemas, y el usuario le ha dado al botón "abrir"
    if (returnVal == fc.APPROVE_OPTION) {
        // obtenemos el fichero seleccionado en archivo
        archivo = fc.getSelectedFile();

        NuevoProyecto.archivo = archivo.getAbsolutePath();
        NuevoProyecto.nombre_archivo = NuevoProyecto.archivo;

        while (NuevoProyecto.nombre_archivo.contains("\\")) {
            NuevoProyecto.nombre_archivo = NuevoProyecto.nombre_archivo.substring(1);
        }

// Reseteamos el "file chooser" para la próxima vez que se muestre
        fc.setSelectedFile(null);


        try {
            // Cargamos los vectores vecDispUsados y vecDispDomoticos, así como sus índices por medio del fichero info.txt
            recupera_info();
            // Y ejecutamos el archivo que se encarga de configurar la vivienda
            ConfiguracionProyecto.main();
            if (ConfiguracionProyecto.llamado == 0) {
                ConfiguracionProyecto.main();
            } else {
                Arbol.generarArbol();
                ConfiguracionProyecto.FRAME_CONFIGURACION.setVisible(true);
                
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}//GEN-LAST:event_jXmlMenuActionPerformed

private void helpMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpMenuActionPerformed

    Config.main();
}//GEN-LAST:event_helpMenuActionPerformed

private void jInsertarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jInsertarItemActionPerformed
    AdministrarContadores admin = new AdministrarContadores(this.getFrame(), true);

    // Para centrar la ventana
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = admin.getSize();
    if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
    }
    admin.setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
    admin.setVisible(true);

    // En caso de haber contadores insertados, se actualiza la lista
    if (VariableGlobal.Lcontadores.getAllPoints().length >= 1) {
        AdministrarContadores.repaintList();
    }
}//GEN-LAST:event_jInsertarItemActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    // Se produce una llamada al fichero Guardando.main()
    Guardando.main();
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    Escribir esc = new Escribir(this.getFrame(), true, tunnel, fecha_hora());

    // Para centrar la ventana
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = esc.getSize();
    if (frameSize.height > screenSize.height) {
        frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
        frameSize.width = screenSize.width;
    }
    esc.setLocation((screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
    esc.setVisible(true);
}//GEN-LAST:event_jMenuItem3ActionPerformed

private void cerrarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarMenuItemActionPerformed
    // Al cerrar el proyecto, se eliminan todos los recursos utilizados
    try {
        this.finalize();
        // Se reinician las variables globales
        reinicia_VariablesGlobales();
        // Y otras variables usadas por otros ficheros
        reinicia_OtrasVariables();
        // Paramos los diferentes "timers" que controlan la monitorización continua de los sensores
        deten_timers();
    } catch (Throwable ex) {
        showException(ex.getMessage());
    }
}//GEN-LAST:event_cerrarMenuItemActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
// TODO add your handling code here:
    
    
    JFrame mainFrame = SkoaApp.getApplication().getMainFrame();
    Lincencia aboutBox = new Lincencia(mainFrame,true);
    aboutBox.setLocationRelativeTo(mainFrame);
    SkoaApp.getApplication().show(aboutBox);
    
    
}//GEN-LAST:event_jMenuItem4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem cargarMenuItem;
    private javax.swing.JMenuItem cerrarMenuItem;
    private javax.swing.JMenuItem conectarMenuItem;
    private javax.swing.JMenuItem desconectarMenuItem;
    private javax.swing.JLabel fecha;
    public static javax.swing.JLabel fecha_res;
    private javax.swing.JLabel hora;
    private javax.swing.JLabel hora_res;
    private javax.swing.JMenuItem jAbrirBDItem;
    private javax.swing.JMenu jContadoresMenu;
    private javax.swing.JMenuItem jGrafsMenu;
    private javax.swing.JMenuItem jGuardarBDItem;
    private javax.swing.JMenuItem jInsertarItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jLectPeriodItem;
    private javax.swing.JMenuItem jLeerContItem;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JMenuItem jXmlMenu;
    private javax.swing.JButton jbConectar;
    private javax.swing.JButton jbDesconectar;
    private javax.swing.JLabel jlEstadoServ;
    public static javax.swing.JLabel jlEstadoServ_res;
    public javax.swing.JLabel jlMaipez;
    private javax.swing.JPanel jpPanelSup;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JPanel pAplicacion;
    private javax.swing.JPanel pContadores;
    public static javax.swing.JTextArea textArea;
    public static javax.swing.JTextArea textArea2;
    private javax.swing.JTabbedPane tpPestanyas;
    private javax.swing.JTabbedPane tpPlantas;
    private javax.swing.JMenuItem verConfigItem;
    // End of variables declaration//GEN-END:variables
    // Variables que deberíamos añadir para la lectura del XML
    // Si el hotel cuenta con más plantas, se deberán añadir más variables
    private javax.swing.JTabbedPane tpPlanta01;
    private javax.swing.JTabbedPane tpPlanta02;
    private javax.swing.JTabbedPane tpPlanta03;
    private javax.swing.JTabbedPane tpPlanta04;
    private javax.swing.JTabbedPane tpPlanta05;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    // Elementos JDialog para cada una de las GUI (graphic user interface)
    private JDialog aboutBox;
    private JDialog conectar;
    private JDialog adminptos;
    private JDialog leer;
    private JDialog escribir;
    private JDialog admincont;
    private JDialog leercont;
    private JDialog lectperiodica;
    private JDialog config;
    // Esta declaración ya no hace falta, antes sí (cuando utilizábamos "initComponentsXML")
    //private javax.swing.JLabel jlMaipez; // Para el logo de la aplicación
    // Variables necesarias para la creación dinámica de las pestañas que
    // representarán a las diferentes estancias del hotel
    // ---------------- PRIMERA PLANTA -----------------
    EstanciaGenerica est01p01 = new EstanciaGenerica();
    EstanciaGenerica est02p01 = new EstanciaGenerica();
    EstanciaGenerica est03p01 = new EstanciaGenerica();
    EstanciaGenerica est04p01 = new EstanciaGenerica();
    EstanciaGenerica est05p01 = new EstanciaGenerica();
    EstanciaGenerica est06p01 = new EstanciaGenerica();
    EstanciaGenerica est07p01 = new EstanciaGenerica();
    EstanciaGenerica est08p01 = new EstanciaGenerica();
    EstanciaGenerica est09p01 = new EstanciaGenerica();
    EstanciaGenerica est10p01 = new EstanciaGenerica();
    EstanciaGenerica est11p01 = new EstanciaGenerica();
    EstanciaGenerica est12p01 = new EstanciaGenerica();
    EstanciaGenerica est13p01 = new EstanciaGenerica();
    EstanciaGenerica est14p01 = new EstanciaGenerica();
    EstanciaGenerica est15p01 = new EstanciaGenerica();
    EstanciaGenerica est16p01 = new EstanciaGenerica();
    EstanciaGenerica est17p01 = new EstanciaGenerica();
    EstanciaGenerica est18p01 = new EstanciaGenerica();
    EstanciaGenerica est19p01 = new EstanciaGenerica();
    EstanciaGenerica est20p01 = new EstanciaGenerica();
    EstanciaGenerica est21p01 = new EstanciaGenerica();
    EstanciaGenerica est22p01 = new EstanciaGenerica();
    EstanciaGenerica est23p01 = new EstanciaGenerica();
    EstanciaGenerica est24p01 = new EstanciaGenerica();
    EstanciaGenerica est25p01 = new EstanciaGenerica();
    // ---------------- SEGUNDA PLANTA -----------------
    EstanciaGenerica est01p02 = new EstanciaGenerica();
    EstanciaGenerica est02p02 = new EstanciaGenerica();
    EstanciaGenerica est03p02 = new EstanciaGenerica();
    EstanciaGenerica est04p02 = new EstanciaGenerica();
    EstanciaGenerica est05p02 = new EstanciaGenerica();
    EstanciaGenerica est06p02 = new EstanciaGenerica();
    EstanciaGenerica est07p02 = new EstanciaGenerica();
    EstanciaGenerica est08p02 = new EstanciaGenerica();
    EstanciaGenerica est09p02 = new EstanciaGenerica();
    EstanciaGenerica est10p02 = new EstanciaGenerica();
    EstanciaGenerica est11p02 = new EstanciaGenerica();
    EstanciaGenerica est12p02 = new EstanciaGenerica();
    EstanciaGenerica est13p02 = new EstanciaGenerica();
    EstanciaGenerica est14p02 = new EstanciaGenerica();
    EstanciaGenerica est15p02 = new EstanciaGenerica();
    EstanciaGenerica est16p02 = new EstanciaGenerica();
    EstanciaGenerica est17p02 = new EstanciaGenerica();
    EstanciaGenerica est18p02 = new EstanciaGenerica();
    EstanciaGenerica est19p02 = new EstanciaGenerica();
    EstanciaGenerica est20p02 = new EstanciaGenerica();
    EstanciaGenerica est21p02 = new EstanciaGenerica();
    EstanciaGenerica est22p02 = new EstanciaGenerica();
    EstanciaGenerica est23p02 = new EstanciaGenerica();
    EstanciaGenerica est24p02 = new EstanciaGenerica();
    EstanciaGenerica est25p02 = new EstanciaGenerica();
    // ---------------- TERCERA PLANTA -----------------
    EstanciaGenerica est01p03 = new EstanciaGenerica();
    EstanciaGenerica est02p03 = new EstanciaGenerica();
    EstanciaGenerica est03p03 = new EstanciaGenerica();
    EstanciaGenerica est04p03 = new EstanciaGenerica();
    EstanciaGenerica est05p03 = new EstanciaGenerica();
    EstanciaGenerica est06p03 = new EstanciaGenerica();
    EstanciaGenerica est07p03 = new EstanciaGenerica();
    EstanciaGenerica est08p03 = new EstanciaGenerica();
    EstanciaGenerica est09p03 = new EstanciaGenerica();
    EstanciaGenerica est10p03 = new EstanciaGenerica();
    EstanciaGenerica est11p03 = new EstanciaGenerica();
    EstanciaGenerica est12p03 = new EstanciaGenerica();
    EstanciaGenerica est13p03 = new EstanciaGenerica();
    EstanciaGenerica est14p03 = new EstanciaGenerica();
    EstanciaGenerica est15p03 = new EstanciaGenerica();
    EstanciaGenerica est16p03 = new EstanciaGenerica();
    EstanciaGenerica est17p03 = new EstanciaGenerica();
    EstanciaGenerica est18p03 = new EstanciaGenerica();
    EstanciaGenerica est19p03 = new EstanciaGenerica();
    EstanciaGenerica est20p03 = new EstanciaGenerica();
    EstanciaGenerica est21p03 = new EstanciaGenerica();
    EstanciaGenerica est22p03 = new EstanciaGenerica();
    EstanciaGenerica est23p03 = new EstanciaGenerica();
    EstanciaGenerica est24p03 = new EstanciaGenerica();
    EstanciaGenerica est25p03 = new EstanciaGenerica();
    // ---------------- CUARTA PLANTA ------------------
    EstanciaGenerica est01p04 = new EstanciaGenerica();
    EstanciaGenerica est02p04 = new EstanciaGenerica();
    EstanciaGenerica est03p04 = new EstanciaGenerica();
    EstanciaGenerica est04p04 = new EstanciaGenerica();
    EstanciaGenerica est05p04 = new EstanciaGenerica();
    EstanciaGenerica est06p04 = new EstanciaGenerica();
    EstanciaGenerica est07p04 = new EstanciaGenerica();
    EstanciaGenerica est08p04 = new EstanciaGenerica();
    EstanciaGenerica est09p04 = new EstanciaGenerica();
    EstanciaGenerica est10p04 = new EstanciaGenerica();
    EstanciaGenerica est11p04 = new EstanciaGenerica();
    EstanciaGenerica est12p04 = new EstanciaGenerica();
    EstanciaGenerica est13p04 = new EstanciaGenerica();
    EstanciaGenerica est14p04 = new EstanciaGenerica();
    EstanciaGenerica est15p04 = new EstanciaGenerica();
    EstanciaGenerica est16p04 = new EstanciaGenerica();
    EstanciaGenerica est17p04 = new EstanciaGenerica();
    EstanciaGenerica est18p04 = new EstanciaGenerica();
    EstanciaGenerica est19p04 = new EstanciaGenerica();
    EstanciaGenerica est20p04 = new EstanciaGenerica();
    EstanciaGenerica est21p04 = new EstanciaGenerica();
    EstanciaGenerica est22p04 = new EstanciaGenerica();
    EstanciaGenerica est23p04 = new EstanciaGenerica();
    EstanciaGenerica est24p04 = new EstanciaGenerica();
    EstanciaGenerica est25p04 = new EstanciaGenerica();
    // ---------------- QUINTA PLANTA ------------------
    EstanciaGenerica est01p05 = new EstanciaGenerica();
    EstanciaGenerica est02p05 = new EstanciaGenerica();
    EstanciaGenerica est03p05 = new EstanciaGenerica();
    EstanciaGenerica est04p05 = new EstanciaGenerica();
    EstanciaGenerica est05p05 = new EstanciaGenerica();
    EstanciaGenerica est06p05 = new EstanciaGenerica();
    EstanciaGenerica est07p05 = new EstanciaGenerica();
    EstanciaGenerica est08p05 = new EstanciaGenerica();
    EstanciaGenerica est09p05 = new EstanciaGenerica();
    EstanciaGenerica est10p05 = new EstanciaGenerica();
    EstanciaGenerica est11p05 = new EstanciaGenerica();
    EstanciaGenerica est12p05 = new EstanciaGenerica();
    EstanciaGenerica est13p05 = new EstanciaGenerica();
    EstanciaGenerica est14p05 = new EstanciaGenerica();
    EstanciaGenerica est15p05 = new EstanciaGenerica();
    EstanciaGenerica est16p05 = new EstanciaGenerica();
    EstanciaGenerica est17p05 = new EstanciaGenerica();
    EstanciaGenerica est18p05 = new EstanciaGenerica();
    EstanciaGenerica est19p05 = new EstanciaGenerica();
    EstanciaGenerica est20p05 = new EstanciaGenerica();
    EstanciaGenerica est21p05 = new EstanciaGenerica();
    EstanciaGenerica est22p05 = new EstanciaGenerica();
    EstanciaGenerica est23p05 = new EstanciaGenerica();
    EstanciaGenerica est24p05 = new EstanciaGenerica();
    EstanciaGenerica est25p05 = new EstanciaGenerica();
    // Si se quisiera poder manejar más estancias o más plantas,
    // se deberían añadir más variables
}
class My_EICLEventListener implements EICLEventListener {

    SkoaMain maipview;

    My_EICLEventListener(SkoaMain adaptee) {
        maipview = adaptee;
    }

    public void newFrameReceived(FrameEvent e) {
        maipview.packetReceived(e);
    }

    public void serverDisconnected(DisconnectEvent e) {
        maipview.TunnelServerError(e);
    }
}