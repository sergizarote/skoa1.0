/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.helpers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;
import javax.sql.DataSource;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import org.apache.commons.dbcp.BasicDataSource;
import skoa.bd.Consultas;
import skoa.helpers.Graficos;
import skoa.views.ElectorFicheros;


public class ConfiguracionGraficas {
        
    int c=0,cant=0,cd=0,nd=0,g=0,f=0,nd1=0,ng=1,vg=0,nbotongraf=1,control=0; //control, usada al "Generar gráficos"
    public boolean primera=true,vacia=false;
    JPanel principal=new JPanel(),centro=new JPanel(),oeste=new JPanel(),grafica=new JPanel(), obtenidos;
    JPanel p,p2,datos,fichero, panel, opciones, opciones2, tipos2, dirs, dirs2, fechas, fechaI, fechaF, pAdd;
    JComboBox ndirs, listado;
    JButton abrir,boton1,boton2,boton3,anadir,generar,espera,bgraf1=new JButton(),bgraf2=new JButton(),bgraf3=new JButton(),bgraf4=new JButton();
    JCheckBox c1,c2,c3,c4,c4a,c4b,g1,g2,f1,f2,dual;
    JFrame interfaz;	
    JLabel l,l3,l4;
    JComboBox lo,lr;
    Vector<String> seleccionadas = new Vector<String>();
    JTextField fi,ff,r,nfich,rfich;
    String fini, ffin, rango,fselectruta,ruta_graficos,ejeDual="";
    Vector<String> direcciones = new Vector<String>(),consultasDuales = new Vector<String>();
    SimpleDateFormat formatoDelTextoCarpeta = new SimpleDateFormat("yyyyMMdd hhmm");
    public String ruta_jar,ruta_destino; //RECORDAR: En inicial, se reestablece este valor.
    static String NombreCarpetaActual="";
    ThreadGroup H=new ThreadGroup("HiloH"); 	//Grupo de hilos que ejecutan las consultas
    DataSource dataSource;	//Pool de conexiones.
    Thread h1,h2,h3,h4; 	//Como maximo hay 4 consultas. Usaremos los hilos dependiendo del n� de consultas.
    int nh=1; 			//Contador de hilos para saber por cual vamos.
    ElectorFicheros e;
    JProgressBar progressBar;
    Timer timer;
    String user="root", pass="lunes20.",url="jdbc:mysql://localhost/Skoa_Bd";
    //String user="root", pass="estefania",url="jdbc:mysql://localhost/Skoa_Bd";
    String ruta_lista, ruta_lista2;
    Timer barra;
    Graficos graf;

    public ConfiguracionGraficas(JFrame i){
        //Inicializamos los directorios correspondientes a partir de donde se encuentra el .jar
        File dir_inicial = new File ("./");
        ruta_jar =System.getProperty("user.dir");
        String os=System.getProperty("os.name");
        //En Windows las barras son \ y en Linux /.
        String busca,reemp;
        //if (os.indexOf("Win")>=0) {
            ruta_destino=ruta_jar+File.separator+"Consultas"+File.separator;
            ruta_lista2=ruta_jar+File.separator+"lista.txt";
            //busca="\\";
            //reemp="\\\\";
        //}
        //else{
            //ruta_destino=ruta_jar+File.separator+"Consultas"+File.separator;
            //ruta_lista2=ruta_jar+File.separator+"lista.txt";
            //busca="/";
            //reemp="//";  //La doble barra es por MySQL, en Linux no sé si hace falta.
        //}
        //String aux=ruta_jar,aux2="";
        //Cuando se usa una ruta en la base de datos, debe tener doble barra, es decir \\\\
        /*while(aux.length()!=0){
            if (aux.indexOf(busca)>=0) {
                aux2=aux2+aux.substring(0,aux.indexOf(busca));
                aux=aux.substring(aux.indexOf(busca)+1);
            }
            else {
                aux2=aux2+aux.substring(0);
                aux="";
            }
            aux2=aux2+reemp;
        }*/
        ruta_lista = ruta_lista2;
        //if (os.indexOf("Win")>=0) ruta_lista=aux2+"\\\\lista.txt";
        //else ruta_lista=aux2+"//lista.txt";
        
        
        //-----------------------------------
        interfaz=i;
	String g1=" 1 ";	//titulo del boton por defecto
	String g4=" 4 ";
	Object[] options={g1,g4}; //titulo de los botones
	int n=JOptionPane.showOptionDialog(interfaz,"¿Cúantas gráficas desea visualizar?", "Visualización",JOptionPane.YES_NO_OPTION,
					   JOptionPane.QUESTION_MESSAGE, null, options, g1);
	if (n==JOptionPane.YES_OPTION) g=1;
	else if (n==JOptionPane.NO_OPTION) g=4;
	vg=g;//Nº de graficas a visualizar. Variable usada en el procedimiento colocarGraficos()
	inicializarPaneles();
	inicial();
	iniciarConexiones2();
    }
	
    //Procedimiento usado cuando se pulsa de nuevo sobre la opcion <Nuevo>
    public void eliminar(){
        interfaz.remove(centro);
	interfaz.remove(oeste);
    }

    private void inicializarPaneles() {
        espera=new JButton();
	inicializaDataSource(); //Inicializa el pool de conexiones.		
	nfich=new JTextField();
	nfich.setEditable(false);
	rfich=new JTextField();
	fichero=new JPanel();		//Volvemos a crear y a configurar "panel" para no tener problemas al cambiar de vista.
	fichero.setLayout(new GridLayout(1,3));
	e=new ElectorFicheros();
	abrir=e.openButton;
	abrir.setEnabled(false); //Al principio, se desactiva, hasta que no se elija un tipo de consulta.
	boton3= new JButton("Seleccionar");	//Boton3 de seleccion de un fichero escogido.
	boton3.setFont(new Font("Tahoma",Font.PLAIN,12));
	boton3.setEnabled(false);
	fichero.add(abrir);
	fichero.add(nfich);
	fichero.add(boton3);
	//Paneles principales:
	principal.setLayout(new BorderLayout());
	principal.add(new JPanel(),"West");	//añadido para que se vea este panel centrado
	principal.add(new JPanel(),"East");	//y no pegado a los bordes de la ventana
	principal.add(new JPanel(),"North");	//...
	principal.add(new JPanel(),"South");	//...		
	centro.setLayout(new BorderLayout());
	oeste.setLayout(new BorderLayout()); 
	//Panel centro
	grafica.setBackground(Color.lightGray);
	JPanel p=new JPanel();
	p.setBorder(BorderFactory.createEtchedBorder());
	if (g==1) {
            grafica.setLayout(new GridLayout(1,1));//1 grafica
            grafica.add(p);
	}
	else {
            grafica.setLayout(new GridLayout(2,2));//4 graficas
            grafica.add(p);
            for (int i=0;i<3;i++){
                p=new JPanel();
		p.setBorder(BorderFactory.createEtchedBorder());
		grafica.add(p);
            }
	}
	centro.add(new JPanel(),"West");	//a�adido para que se vea este panel centrado
	centro.add(new JPanel(),"East");	//y no pegado a los bordes de la ventana
	centro.add(new JPanel(),"North");	//...
	centro.add(new JPanel(),"South");	//...
	centro.add(grafica,"Center");
	//Panel de datos:
	datos=new JPanel();
	datos.setLayout(new GridLayout(2,1));
	//Panel de obtencion de datos
	panel=new JPanel();
	panel.setLayout(new GridLayout(2,1));
	panel.setBorder(BorderFactory.createEtchedBorder());
	//Panel obtenidos (datos obtenidos)
	obtenidos=new JPanel();
	//obtenidos.setLayout(new GridLayout(5,1));
	obtenidos.setLayout(new GridLayout(6,1));
	obtenidos.setBorder(BorderFactory.createEtchedBorder());
	//Panel dirs: num de DGs y DGs
	dirs=new JPanel();	
	l3=new JLabel("  Número de DGs: ");
        l3.setFont(new Font("Tahoma",Font.PLAIN,12));
	String n[]={"0","1","2","3","4","5"};
	ndirs=new JComboBox(n);
	ndirs.setEditable(false);				
	ndirs.setSelectedIndex(0);
	boton2= new JButton("Seleccionar");
        boton2.setFont(new Font("Tahoma",Font.PLAIN,12));
        dirs.setLayout(new GridLayout(1,3));
        dirs.add(l3);
        dirs.add(ndirs);		//nº direcciones de grupo
        dirs.add(boton2);
        l4=new JLabel("  Dir. de grupo:");
        l4.setFont(new Font("Tahoma",Font.PLAIN,12));
	boton1= new JButton("Seleccionar");
        boton1.setFont(new Font("Tahoma",Font.PLAIN,12));
        listarDGs();
        dirs2=new JPanel();
        dirs2.setLayout(new GridLayout(1,3));
        dirs2.add(l4);
        dirs2.add(listado);		//lista de direcciones de grupo
        dirs2.add(boton1);
	//Panel2: DGs seleccionadas y tipos de consulta
	c1 = new JCheckBox("Evolución temporal"); 			//Consulta A.
        c1.setFont(new Font("Tahoma",Font.PLAIN,12));
	c1.setSelected(false);
	c2 = new JCheckBox("Acumulación por intervalos temporales"); 	//Consulta B.
        c2.setFont(new Font("Tahoma",Font.PLAIN,12));
	c2.setSelected(false);
	c3 = new JCheckBox("Máx-Mín-Med por intervalos temporales"); 	//Consulta C.
        c3.setFont(new Font("Tahoma",Font.PLAIN,12));
	c3.setSelected(false);
	c4 = new JCheckBox("Evolución de diferencias"); 		//Consulta D.
        c4.setFont(new Font("Tahoma",Font.PLAIN,12));
	c4.setSelected(false);
	tipos2=new JPanel();	
	tipos2.setLayout(new GridLayout(1,4));
	tipos2.add(new JPanel());
	c4a = new JCheckBox("%"); 	//Consulta D -> %.
	c4a.setSelected(false);
	c4b = new JCheckBox("Diferencia"); 	//Consulta D -> Diferencia.
        c4b.setFont(new Font("Dialog",Font.PLAIN,12));
	c4b.setSelected(false);
	tipos2.add(c4a);
	tipos2.add(c4b);
	tipos2.add(new JPanel());
	//CheckBox para ver si se muestra la gráfica con doble eje
	dual=new JCheckBox("Dual"); 	
	dual.setSelected(false);
	dual.setFont(new Font("Dialog",Font.ITALIC,12));
	JPanel dualA=new JPanel();
	dualA.setLayout(new GridLayout(1,3));
	JLabel aux=new JLabel("Opción de visualización: ");
	aux.setFont(new Font("Dialog",Font.ITALIC,12));
	dualA.add(aux);
	dualA.add(dual);
	dualA.add(new JPanel());
	//Panel opciones: tipos de consulta y seleccion de DGs.
	opciones=new JPanel();
	opciones.setLayout(new GridLayout(8,1));
	opciones.add(dualA);
	opciones.add(c1);
	opciones.add(c2);
	opciones.add(c3);
	opciones.add(c4);
	opciones.add(new JPanel()); //Lugar en que van las subopciones de la consulta D
	opciones.add(dirs);
	opciones.add(dirs2);
	//Panel opciones2: DGs seleccionadas, tipo de fecha, fechas, añadir datos.
	opciones2=new JPanel();
	opciones2.setLayout(new GridLayout(7,1));
	opciones2.add(fichero);	//Lugar en que va la seleccion de los resultados de una consulta ya hecha.
	//Panel3: en principio solo elegir fechas. Se inicializa fecha inicial
	fechas=new JPanel();	//Fecha inicial y final o todo
	fechas.setLayout(new GridLayout(1,3));
	JLabel l2=new JLabel("  Fechas: ");
        l2.setFont(new Font("Tahoma",Font.BOLD,12));
	f1 = new JCheckBox("Intervalo");
        f1.setFont(new Font("Tahoma",Font.PLAIN,12));
	f1.setSelected(false);
	f2 = new JCheckBox("Completo");
        f2.setFont(new Font("Tahoma",Font.PLAIN,12));
	f2.setSelected(false);
	fechas.add(l2);
	fechas.add(f1);
	fechas.add(f2);	
	fechaI=new JPanel();
	JLabel l5=new JLabel("Fecha inicial:");
        l5.setFont(new Font("Tahoma",Font.PLAIN,12));
	fi=new JTextField(16);
	JLabel laux=new JLabel("<aaaa-mm-dd> ó        ");
	laux.setFont(new Font("Tahoma", Font.ITALIC, 10));
	fechaI.add(l5);
	fechaI.add(fi);
	fechaI.add(laux);
	//Panel4: fecha final y [rango | nada]
	fechaF=new JPanel();
	JLabel l6=new JLabel("Fecha final:  ");
        l6.setFont(new Font("Tahoma",Font.PLAIN,12));
	ff=new JTextField(16);
	laux=new JLabel("<aaaa-mm-dd hh:mm>");
	laux.setFont(new Font("Tahoma", Font.ITALIC, 10));
	fechaF.add(l6);	
	fechaF.add(ff);
	fechaF.add(laux);		
	//Panel 5: boton de añadir
	pAdd=new JPanel();
	pAdd.setLayout(new GridLayout(1,4));
	pAdd.add(new JPanel());
	pAdd.add(new JPanel());
	anadir=new JButton("AÑADIR DATOS");
        anadir.setFont(new Font("Tahoma",Font.BOLD,12));
	pAdd.add(anadir);
	//Panel obtenidos (datos obtenidos)
	r=new JTextField(16);
	//Otras inicializaciones:
	generar=new JButton("GENERAR GRÁFICAS");
        generar.setFont(new Font("Tahoma",Font.BOLD,12));
    }
	
    private void reestablecerPaneles() {
        datos.removeAll();
	panel.removeAll();	//Borra todo lo del panel para inicializarlo de nuevo
	panel=new JPanel();	//NECESARIO CREARLO DE NUEVO.SINO, LA VISTA SE VE MAL Y NO CAMBIA HASTA CAMBIAR EL TAMA�O DE LA VENTANA 
	panel.setLayout(new GridLayout(2,1)); 
	panel.setBorder(BorderFactory.createEtchedBorder());
	//Seleccion de ficheros
	nfich=new JTextField();
	nfich.setEditable(false);
	rfich=new JTextField();
	fichero.removeAll();
	e=new ElectorFicheros();
	abrir=e.openButton;
	abrir.setEnabled(false); //Al principio, se desactiva, hasta que no se elija un tipo de consulta.
	boton3.setEnabled(false);
	fichero.add(abrir);
	fichero.add(nfich);
	fichero.add(boton3);
	//Panel opciones: tipos de consulta y seleccion de DGs.
	dual.setSelected(false);//CheckBox para ver si se muestra la grafica con doble eje
	JPanel dualA=new JPanel();
	dualA.setLayout(new GridLayout(1,3));
	JLabel aux=new JLabel("Opción de visualización: ");
	aux.setFont(new Font("Dialog",Font.ITALIC,12));
	dualA.add(aux);
	dualA.add(dual);
	dualA.add(new JPanel());
	opciones.removeAll();
	opciones2.removeAll();
	c1.setSelected(false);
	c2.setSelected(false);
	c3.setSelected(false);
	c4.setSelected(false);
	opciones.add(dualA);
	opciones.add(c1);
	opciones.add(c2);
	opciones.add(c3);
	opciones.add(c4);
	opciones.add(new JPanel()); //Lugar en que van las subopciones de la consulta D
	ndirs.setEditable(false);				
	ndirs.setSelectedIndex(0);
	dirs.removeAll();
        dirs.add(l3);
        dirs.add(ndirs);		//nº direcciones de grupo
        dirs.add(boton2);
	opciones.add(dirs);
	listado.setEditable(true);				
        listado.setSelectedIndex(0);
        dirs2.removeAll();
        dirs2.add(l4);
        dirs2.add(listado);		
        dirs2.add(boton1);
	opciones.add(dirs2);
	opciones2.add(fichero);	//Lugar en que va la seleccion de los resultados de una consulta ya hecha.
	tipos2.removeAll();
	c4a.setSelected(false);
	c4b.setSelected(false);
	tipos2.add(c4a);
	tipos2.add(c4b);
	tipos2.add(new JPanel());
	//Inicializaciones de los paneles de fechas.
	fechas.removeAll();
	JLabel l2=new JLabel("  Fechas: ");
        l2.setFont(new Font("Tahoma",Font.BOLD,12));
	f1.setSelected(false);
	f2.setSelected(false);
	fechas.add(l2);
	fechas.add(f1);
	fechas.add(f2);	
	fechaI.removeAll();
	JLabel l5=new JLabel("Fecha inicial:");
        l5.setFont(new Font("Tahoma",Font.PLAIN,12));
	fi=new JTextField(16);
	JLabel laux=new JLabel("<aaaa-mm-dd> ó        ");
	laux.setFont(new Font("Tahoma", Font.ITALIC, 10));
	fechaI.add(l5);
	fechaI.add(fi);
	fechaI.add(laux);
	//fecha final y [rango | nada]
	fechaF.removeAll();
	JLabel l6=new JLabel("Fecha final:  ");
        l6.setFont(new Font("Tahoma",Font.PLAIN,12));
	ff=new JTextField(16);
	laux=new JLabel("<aaaa-mm-dd hh:mm>");
	laux.setFont(new Font("Tahoma", Font.ITALIC, 10));
	fechaF.add(l6);	
	fechaF.add(ff);
	fechaF.add(laux);		
	//Otras inicializaciones:
	r=new JTextField(16);
	c=cd=nd=nd1=f=0;
	direcciones.removeAllElements();
	direcciones = new Vector<String>();
	boton3.setEnabled(false);
	//CARGA LA VISTA REESTABLECIDA
	panel.add(opciones);
	panel.add(opciones2);
    }
	
    private void inicial() {
        Date fechaActual = new Date();
	NombreCarpetaActual=formatoDelTextoCarpeta.format(fechaActual);
        //Para cada consulta, se crea una carpeta con la fecha y hora de realizacion de la consulta,
	//para almacenar en ella todos los resultados y graficas de esa consulta.
	String os=System.getProperty("os.name");
        //En Windows las barras son \ y en Linux /.
        if (os.indexOf("Win")>=0) ruta_destino=ruta_jar+"\\Consultas\\";
        else ruta_destino=ruta_jar+"/Consultas/";
	ruta_destino=ruta_destino+NombreCarpetaActual;
	File destino_consulta = new File(ruta_destino);
        destino_consulta.setExecutable(true,false);
        destino_consulta.setReadable(true,false);
        destino_consulta.setWritable(true, false);
	//HACEMOS LA CREACION DE LA CARPETA DESTINO PARA LA NUEVA CONSULTA POSTERIOR.
	if (destino_consulta.mkdir()) {} //System.out.println("**SE CREA DIR.   "+ruta_destino);
	else {	//En caso de existir ya el directorio, porque hemos hecho las consultas muy rapido
            //y estas coinciden en su nombre, ya que el nombre es la hora en que se consulta
	    //le vamos a añadir un numero indicando que es una consulta a la misma hora.
	    String aux=NombreCarpetaActual.substring(NombreCarpetaActual.indexOf(" ")+1); //Contamos desde el primer espacio
	    if (aux.indexOf(" ")!=-1) {
                aux=aux.substring(aux.length()-1); 	//Cogemos el ultimo caracter.
	    	int naux=Integer.parseInt(aux);		//Lo pasamos a entero
	    	naux++;								//Lo incrementamos en 1
	    	NombreCarpetaActual=NombreCarpetaActual+" "+naux;	
	    }
	    else NombreCarpetaActual=NombreCarpetaActual+" 1";
	    if (os.indexOf("Win")>=0) ruta_destino=ruta_jar+"\\Consultas\\";
            else ruta_destino=ruta_jar+"/Consultas/";
            //ruta_destino=ruta_jar+"\\Consultas\\";
            ruta_destino=ruta_destino+NombreCarpetaActual;
	    destino_consulta = new File(ruta_destino);
            destino_consulta.setExecutable(true,false);
            destino_consulta.setReadable(true,false);
            destino_consulta.setWritable(true, false);
	    destino_consulta.mkdir();
        }
        interfaz.getContentPane().setLayout(new BorderLayout());
	panel.add(opciones);	//No se añade esta opcion en cargarVista() para generalizarla y usarla siempre.
	panel.add(opciones2);	//No se añade esta opcion en cargarVista() para generalizarla y usarla siempre.
	cargarVista(); 
	interfaz.getContentPane().add(oeste,"West");
	interfaz.getContentPane().add(centro,"Center");
	interfaz.setVisible(true);
    }
	
    private void seleccionFichero(){
        fichero.removeAll();
	fichero=new JPanel();		//Volvemos a crear y a configurar "panel" para no tener problemas al cambiar de vista.
	fichero.setLayout(new GridLayout(1,3));
	abrir.setEnabled(true);
	fichero.add(abrir);
	nfich=e.log;
	rfich=e.logruta;
	fichero.add(nfich);	//Introducimos el JTextField con la seleccion realizada.
	boton3.setEnabled(true);
	fichero.add(boton3);	
    }
	
    //Carga la vista con la configuracion realizada. La primera vez, sera la configuracion por defecto
    //proveniente de inicializarPaneles.
    private void cargarVista() {
        datos.add(panel);	//CELDA DE OBTENCION DE LOS DATOS
	datos.add(obtenidos);	//CELDAS DE DATOS INTRODUCIDOS (AUN VACíO)
	principal.add(datos,"Center");			
	oeste.add(principal);
	interfaz.setVisible(true);
    }
	
    //Procedimiento que realiza las mismas inicializaciones para todas las vistas iniciales.
    private void comun(){
        ndirs.setEditable(true);
        ndirs.setSelectedIndex(0);
        boton3.setEnabled(true);
	f1.setSelected(false);
	f2.setSelected(false);
	nd1=0;		//En caso de haber elegido ya DGs, y cambiar de consulta, reestablecemos la seleccion de DGS.
	direcciones.removeAllElements();
	datos.removeAll();
	panel.removeAll();
	panel=new JPanel();		//Volvemos a crear y a configurar "panel" para no tener problemas al cambiar de vista.
	panel.setLayout(new GridLayout(2,1));
	panel.setBorder(BorderFactory.createEtchedBorder());
	opciones.removeAll();
	opciones2.removeAll();
	JPanel dualA=new JPanel();
	dualA.setLayout(new GridLayout(1,3));
	JLabel aux=new JLabel("Opción de visualización: ");
	aux.setFont(new Font("Dialog",Font.ITALIC,12));
	dualA.add(aux);
	dualA.add(dual);
	dualA.add(new JPanel());
	opciones.add(dualA);
	opciones.add(c1);
	opciones.add(c2);
	opciones.add(c3);
	opciones.add(c4);
	dirs.removeAll();
	dirs.add(l3);
	dirs2.add(l4);
	fichero=new JPanel();		//Volvemos a crear y a configurar "panel" para no tener problemas al cambiar de vista.
	fichero.setLayout(new GridLayout(1,2));
	fichero.setBackground(Color.WHITE);
    }
	
    //Igual que la vistaInicial. Usada para las consultas A y B.
    private void vistaInicial2() {
        comun();
	c4a.setSelected(false);
	c4a.setSelected(false);
	opciones.add(new JPanel()); //Lugar en que van las subopciones de la consulta D
        dirs.add(ndirs);	    //nº direcciones de grupo
        dirs.add(boton2);
	opciones.add(dirs);
	listado.setSelectedIndex(0);	//Se pone sin seleccionar nada.
        dirs2.add(listado);		//lista de direcciones de grupo
        dirs2.add(boton1);
	opciones.add(dirs2);
	seleccionFichero();
	opciones2.add(fichero);
	panel.add(opciones);
	panel.add(opciones2);
	ndirs.setEnabled(true);	//se activan, por si es el caso de ya haber rellenado casi una consulta, y cambiar de opinion,
	boton2.setEnabled(true);
	cargarVista();
    }

    //Usada en el caso de la consulta C, en la que sólo dejamos elegir 1 DG.
    private void vistaInicial3() {
        comun();
	c4a.setSelected(false);
	c4a.setSelected(false);
	opciones.add(new JPanel()); 	//Lugar en que van las subopciones de la consulta D
	//Cambiamos el número de direcciones a escoger. Como es la consulta C sólo es posible elegir 1.
	ndirs.setEditable(false);				
	ndirs.setSelectedIndex(1);
	ndirs.setEnabled(false);
	boton2.setEnabled(false);
        dirs.add(ndirs);				//n� direcciones de grupo
        dirs.add(boton2);
	//Sigue cargando en el panel opciones
        opciones.add(dirs);
	listado.setSelectedIndex(0);	//Se pone sin seleccionar nada.
        dirs2.add(listado);				//lista de direcciones de grupo
        dirs2.add(boton1);
	opciones.add(dirs2);
	seleccionFichero();
	opciones2.add(fichero);
	panel.add(opciones);
	panel.add(opciones2);
	cargarVista();
    }
	
    //Usada en el caso de la consulta D, en la que sólo dejamos elegir 2 DGs.
    private void vistaInicial4() {
        comun();
	//Borramos, para poder poner los check c4a y c4b en el estado en que est�n.
	tipos2.removeAll();
	tipos2.add(new JPanel());
	tipos2.add(c4a);
	tipos2.add(c4b);
	tipos2.add(new JPanel());
	opciones.add(tipos2); //Subopciones de la consulta D
	//Cambiamos el número de direcciones a escoger. Como es la consulta D s�lo es posible elegir 2.
	//Da igual si hay eje dual o no.
	ndirs.setEditable(false);				
	ndirs.setSelectedIndex(2);
	ndirs.setEnabled(false);
	boton2.setEnabled(false);
        dirs.add(ndirs);		//nº direcciones de grupo
        dirs.add(boton2);
	//Sigue cargando en el panel opciones
        opciones.add(dirs);
	listado.setSelectedIndex(0);	//Se pone sin seleccionar nada.
        dirs2.add(listado);		//lista de direcciones de grupo
        dirs2.add(boton1);
	opciones.add(dirs2);
	seleccionFichero();
	opciones2.add(fichero);
	panel.add(opciones);
	panel.add(opciones2);
	cargarVista();
    }
	
    //Se van introduciendo todas las DGs.
    private void vistaIntermedia1() {
        datos.removeAll();
	panel.removeAll();
	panel=new JPanel();
	panel.setLayout(new GridLayout(2,1));
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.add(opciones);
	opciones2.removeAll();
	opciones2.add(fichero);
	//seleccionadas="";
        String DG="";
        seleccionadas.removeAllElements();
        seleccionadas.add("Desplegar para ver");
	for (int i=0;i<direcciones.size();i++) {
            if (direcciones.elementAt(i).charAt(0)=='C') {	//Es un fichero
                //La ruta es: ruta_del_ejecutable\Consultas\...
		//TENGO QUE LLEGAR consultas y despues buscar el fichero Y PONER LO QUE SIGUE EN SELECCIONADAS.
		String aux=direcciones.elementAt(i).substring(direcciones.elementAt(i).indexOf("Consultas")+1);
		if (aux.indexOf("A")>0) aux=aux.substring(aux.indexOf("A"));
		if (aux.indexOf("B")>0) aux=aux.substring(aux.indexOf("B"));
		if (aux.indexOf("C")>0) aux=aux.substring(aux.indexOf("C"));
		if (aux.indexOf("D")>0) aux=aux.substring(aux.indexOf("D"));
                aux=aux.substring(0, aux.indexOf(".txt"));//quito el .txt al nombre
                //if (i==0)seleccionadas=seleccionadas+aux;
                //else seleccionadas = seleccionadas + ", " + aux;
                DG=aux;
            }
            else {//Es una DG
                //if (i==0) seleccionadas = seleccionadas + direcciones.elementAt(i);
                //else seleccionadas = seleccionadas + ", " + direcciones.elementAt(i);
                DG=direcciones.elementAt(i);
            }
            seleccionadas.add(DG);
            //if(nd1==0) lo.append("Seleccionadas "+"("+(nd1+1)+" de "+nd+")"+": "+DG);
            //else lo.append("\n                                             "+DG);
	}
        p=new JPanel();
        p.setLayout(new GridLayout(1,3));
        lo=new JComboBox(seleccionadas);
        lo.setEditable(false);
        lo.setSelectedIndex(0);
        lo.setFont(new Font("Tahoma", Font.ITALIC, 12));
	//l=new JLabel("Seleccionadas "+"("+(nd1+1)+" de "+nd+")"+": "+seleccionadas);
        l=new JLabel("Seleccionadas "+"("+(nd1+1)+" de "+nd+")"+": ");
        l.setFont(new Font("Tahoma", Font.ITALIC, 12));
        p.add(l);
        p.add(lo);
        p.add(new JPanel());
        //lo = new javax.swing.JTextArea(1,1);
        //jScrollPane = new javax.swing.JScrollPane(lo);
        //jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //lo.append("Seleccionadas "+"("+(nd1+1)+" de "+nd+")"+": "+seleccionadas);
	//lo.setFont(new Font("Tahoma", Font.ITALIC, 12));
	//opciones2.add(l);
        opciones2.add(p);
        //opciones2.add(jScrollPane);
	panel.add(opciones2);
	ndirs.setEnabled(false);	//Una vez elegida al menos 1 DG, desactivamos la posibilidad de poder
	boton2.setEnabled(false);	//cambiar el número de DGs a seleccionar.
	cargarVista();
    }
	
    //Ya se han introducido todas las direcciones. Por lo que se elige el tipo de fecha.
    private void vistaIntermedia2() {
        datos.removeAll();
	panel.removeAll();
	panel=new JPanel();
	panel.setLayout(new GridLayout(2,1));
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.add(opciones);
	opciones2.removeAll();
	opciones2.add(fichero);
	//opciones2.add(l);
        opciones2.add(p);
	opciones2.add(fechas);
	panel.add(opciones2);
	cargarVista();
    }
	
    private void vistaIntermedia3() {
        panel.removeAll();
	panel=new JPanel();
	panel.setLayout(new GridLayout(2,1));
	panel.setBorder(BorderFactory.createEtchedBorder());
	panel.add(opciones);
	opciones2.removeAll();
	opciones2.add(fichero);
	//opciones2.add(l);
        opciones2.add(p);
	opciones2.add(fechas);
	if (c==1){		//Consulta A 
            if (f==1){		//  Intervalo
                opciones2.add(fechaI);
		opciones2.add(fechaF); // fecha final
            }
            else  {					//   Completa -->panel4 vacio
                opciones2.add(new JPanel());
		opciones2.add(new JPanel());
            }
            opciones2.add(new JPanel());
	}
        else if(c>4){//Consulta D, Mayor que 4 por 5 y 6, son las subopciones % y Diferencia de 4.
            if (f==1){			//  Intervalo
                opciones2.add(fechaI);
		opciones2.add(fechaF); // fecha final
            }
            else  {					//   Completa -->panel4 vacio
                opciones2.add(new JPanel());
		opciones2.add(new JPanel());
            }
            JPanel paux=new JPanel();
            JLabel l=new JLabel("Rango:        ");
            l.setFont(new Font("Tahoma",Font.PLAIN,12));
            paux.add(l);
            paux.add(r);
            l=new JLabel("               en minutos     ");
            l.setFont(new Font("Tahoma", Font.ITALIC, 10));
            paux.add(l);
            opciones2.add(paux);
        }
	else{						//Consulta B o C.
            JPanel paux=new JPanel();
            JLabel l=new JLabel("Rango:        ");
            l.setFont(new Font("Tahoma",Font.PLAIN,12));
            paux.add(l);
            paux.add(r);
            l=new JLabel("                 en horas     ");
            l.setFont(new Font("Tahoma", Font.ITALIC, 10));
            paux.add(l);
            if (f==1){					//Consulta B, intervalo
                opciones2.add(fechaI);
		opciones2.add(fechaF); // fecha final
		opciones2.add(paux);
            }
            else {					//Consulta B, completo
                opciones2.add(new JPanel());
		opciones2.add(new JPanel()); 
		opciones2.add(paux);
            }
	}
	opciones2.add(pAdd);	//Boton de añadir
	panel.add(opciones2);
	datos.removeAll();
	cargarVista();
    }
	
    private void vistaIntermedia4() {
        vacia=true; //Una vez llegado aquí, ya se empieza a llenar la carpeta destino.
	for (int i=0;i<datos.getComponentCount();i++) datos.remove(i);  //Borra 
	datos.removeAll();
	JPanel p=new JPanel();
	p.setLayout(new GridLayout(3,1));
	//JLabel l=new JLabel(" Gráfica "+ng+":  "+"Direcciones: "+seleccionadas);
        //l.setFont(new Font("Tahoma",Font.BOLD,12));
        p2=new JPanel();
        p2.setLayout(new GridLayout(1,3));
        l=new JLabel(" Gráfica "+ng+":  "+"Direcciones: ");
        l.setFont(new Font("Tahoma", Font.BOLD, 12));
        p2.add(l);
        Vector<String> AuxSeleccionadas = new Vector<String>();
        AuxSeleccionadas.addAll(seleccionadas);
        JComboBox laux=new JComboBox(AuxSeleccionadas);
        laux.setEditable(false);
        laux.setSelectedIndex(0);
        laux.setFont(new Font("Tahoma", Font.ITALIC, 12));
        //p2.add(lo);
        p2.add(laux);//para que se mantengan las anteriores al añadir nuevas.
        p2.add(new JPanel());
	ng++;
	//p.add(l);
        p.add(p2);
	String l2="                  ";
	if (c==1)      {
            if (ejeDual.contentEquals("dual")) l2 = l2 + "Consulta: Evolución temporal dual, ";
            else l2 = l2 + "Consulta: Evolución temporal, ";
        }
	else if (c==2) {
            if (ejeDual.contentEquals("dual")) l2 = l2 + "Consulta: Acumulación por intervalos temporales dual, ";
            else l2 = l2 + "Consulta: Acumulación por intervalos temporales, ";
        }
	else if (c==3) l2=l2+"Consulta: Máx-Mín-Med por intervalos temporales, ";
	else if (c==5) l2=l2+"Consulta: Evolución de diferencias en %, ";
	else if (c==6) l2=l2+"Consulta: Evolución de diferencias, ";
	if (f==1) l2=l2+"de un intervalo.";
	else if (f==2) l2=l2+"completa.";	
	l=new JLabel(l2);
        l.setFont(new Font("Tahoma",Font.PLAIN,12));
	p.add(l);
	l2="                  ";
	if (f==1){			
            l2=l2+"Desde "+fini+" hasta "+ffin;
            if(c==2 || c==3) l2=l2+" con rango de "+rango+" horas.";
            if(c > 4) l2 = l2 + " con rango de " + rango + " minutos.";
	}	
	if (f==2){		
            if(c==2 || c==3) l2=l2+"Con rango de "+rango+" horas.";
            if(c > 4) l2 = l2 + "Con rango de " + rango + " minutos.";
	}	
	l=new JLabel(l2);
        l.setFont(new Font("Tahoma",Font.PLAIN,12));
	p.add(l);
	mantenerVista(p);
	//EN ESTE PUNTO DEL CÓDIGO, ANTES DE RESTABLECERVALORES, INVOCAR A LAS CONSULTAS (al constructor)
	//PARA GENERAR LA CONSULTA CORRESPONDIENTE ANTES DE OBTENER LOS PAR�METROS DE LA SIGUIENTE CONSULTA.
	//nh indica el hilo que se tiene que coger para la consulta actual.
	try {					
            if (nh==1){	//Los hilos en su constructor llaman start.
                h1=new Consultas(H,c,f,fini,ffin,rango,direcciones,NombreCarpetaActual,dataSource);
		h1.start();
            }
            else if(nh==2){
                h2=new Consultas(H,c,f,fini,ffin,rango,direcciones,dataSource);
                h2.start();
            }
            else if(nh==3){
                h3=new Consultas(H,c,f,fini,ffin,rango,direcciones,dataSource);
		h3.start();
            }
            else if(nh==4){
                h4=new Consultas(H,c,f,fini,ffin,rango,direcciones,dataSource);
		h4.start();
            }
            nh++;
	} catch (IOException e) {
            e.printStackTrace();
	} catch (SQLException e) {
            e.printStackTrace();
	}
	consultasDuales.add(ejeDual);
        JPanel gen=new JPanel();
        gen.setLayout(new GridLayout(2,1));
        gen.add(new JPanel());
        gen.add(generar);
	obtenidos.add(gen);
	reestablecerPaneles();
	cargarVista();
    }
	
   //INICIALIZARDOR PARA EL POOL DE CONEXIONES. SOLO SE HACE EN EL inicializaPaneles XQ CON 1 VEZ VALE.
    private void inicializaDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(pass);
        basicDataSource.setUrl(url);
        basicDataSource.setMaxActive(4);
        // Opcional. Sentencia SQL que sirve a BasicDataSource para comprobar que la conexion es correcta.
        basicDataSource.setValidationQuery("select 1");
        dataSource = basicDataSource;
    }
	
    /**
     * Añade paneles vacíos para mantener la misma vista en todas las vistas, y poder generar cuando queramos.
     * @param p ,panel nuevo a a�adir.
     */
    private void mantenerVista(JPanel p){
        if (g==1) {
            JPanel aux1=(JPanel) obtenidos.getComponent(0);
            JPanel aux2=(JPanel) obtenidos.getComponent(1);
            obtenidos.removeAll();
            obtenidos.add(aux1);
            obtenidos.add(aux2);
            obtenidos.add(p);
            obtenidos.add(new JPanel());
        }
	else if (g==2){	
            JPanel aux1=(JPanel) obtenidos.getComponent(0);
            obtenidos.removeAll();
            obtenidos.add(aux1);
            obtenidos.add(p);
            obtenidos.add(new JPanel());
            obtenidos.add(new JPanel());
	}
	else if (g==3){
            obtenidos.add(p);
            obtenidos.add(new JPanel());
            obtenidos.add(new JPanel());
            obtenidos.add(new JPanel());
        }
	else if (g==0 && nh==1){
            obtenidos.add(p);
            obtenidos.add(new JPanel());
            obtenidos.add(new JPanel());
            obtenidos.add(new JPanel());
        }
	else if (g==0 && nh!=1){
            JPanel aux1=(JPanel) obtenidos.getComponent(0);
            JPanel aux2=(JPanel) obtenidos.getComponent(1);
            JPanel aux3=(JPanel) obtenidos.getComponent(2);
            obtenidos.removeAll();
            obtenidos.add(aux1);
            obtenidos.add(aux2);
            obtenidos.add(aux3);
            obtenidos.add(p);
        }
    }
	
    //SE CREA UNA BARRA DE PROGRESO INDEFINIDA
    private void insertarBarraProgreso(){
        progressBar = new JProgressBar();//Barra de progresión indefinida.
	progressBar.setIndeterminate(true);
	progressBar.setVisible(true);
	progressBar.setString("Procesando...");
	progressBar.setStringPainted(true);
	obtenidos.add(progressBar);
	datos.removeAll();
        ruta_graficos=ruta_jar+"\\Consultas\\"+NombreCarpetaActual;
        graf = new Graficos(ruta_graficos,consultasDuales);
        graf.start();
        barra=new Timer(100,new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                cargarVista();
            }
        });
        barra.start();
        //cargarVista();
    }
	
    private void vistaGraficos() {
        //GENERA LOS GRáFICOS UNA VEZ TENEMOS TODOS LOS DATOS
	ruta_graficos=ruta_jar+"\\Consultas\\"+NombreCarpetaActual;
	//COLOCA LOS GRAFICOS RESULTANTES EN SUS PANELES.
	grafica.removeAll();
	centro.remove(grafica);
	//CUIDADO!!! SOLO FUNCIONA CON IMAGENES CON EXTENSIóN JPG. SIEMPRE AñADIRLE ESTA EXTENSIóN.
	String nombreImagen="";
	File dir = new File(ruta_graficos);
	String[] ficheros = dir.list();
	String ruta_aux;
	for (int i=0;i<ficheros.length;i++) {
            ruta_aux=ruta_graficos+"\\"+ficheros[i]+"\\";
            File dir2 = new File(ruta_graficos+"\\"+ficheros[i]);
            String[] ficheros2 = dir2.list();
            //Según la ventana, si se visualiza 1 grafica o 4, busco el gráfico más adecuado.
            for (int j=0;j<ficheros2.length;j++) {
                if (ficheros2[j].indexOf("Small")>0) {
                    nombreImagen=ruta_aux+ficheros2[j];
                    break;
		}
            }
            JPanel p=new JPanel();
            p.setLayout(new BorderLayout());	//Para que el boton ocupe todo el panel.
            p.setBorder(BorderFactory.createEtchedBorder());
            ImageIcon icon = new ImageIcon(nombreImagen);
            if (nbotongraf==1) {
                bgraf1.setIcon(icon);
		bgraf1.setBackground(Color.lightGray);
		p.add(bgraf1);
            }
            if (nbotongraf==2)  {
                bgraf2.setIcon(icon);
		bgraf2.setBackground(Color.lightGray);
		p.add(bgraf2);
            }
            if (nbotongraf==3)  {
                bgraf3.setIcon(icon);
		bgraf3.setBackground(Color.lightGray);
		p.add(bgraf3);
            }
            if (nbotongraf==4)  {
                bgraf4.setIcon(icon);
		bgraf4.setBackground(Color.lightGray);
		p.add(bgraf4);
            }
            nbotongraf++;
            grafica.add(p);
        }
        
	centro.add(grafica, "Center");
	interfaz.getContentPane().add(centro);
	interfaz.setVisible(true);
        progressBar.setVisible(false);
        barra.stop();
    }

    private void listarDGs() {
        File archivo = new File(ruta_lista2); //lista de Dgs.
	if (archivo.exists()){
            //Si el archivo ya existe, se borra para pedir a la BD la lista actual.
            archivo.delete();
        }else{
            try {
                archivo.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	
        
        //En vez de sacarla de un archivo, se debería sacar de la base de datos.
	Connection con;
	try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con=DriverManager.getConnection(url,user,pass);
            Statement st = con.createStatement();
            String accion="select DG,DF into outfile '"+ruta_lista+"' from Dirs;";
            System.out.println(accion);
            
            st.executeQuery(accion);
            st.close();
            con.close();
	} catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
	} catch (ClassNotFoundException e1) {
            e1.printStackTrace();
	} catch (SQLException e) {
            e.printStackTrace();
            //Warning en caso de que la BD no existiera.
            JOptionPane.showMessageDialog(new JFrame("AVISO"),"No existe la base de datos.", "AVISO", 2);
	}
	//Volvemos a abrir el fichero porque se ha creado nuevo.
	archivo = new File(ruta_lista2); //lista de Dgs.
	FileReader fr = null;
	BufferedReader linea = null;
        String line;
        try{
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr);
            Vector<String> lista = new Vector<String>();
            lista.add("");
            while((line=linea.readLine())!=null) lista.add(line);  	//Ordenar las DGs alfabeticamente
            Collections.sort(lista); 					//...
            //Como están las DGs con sus correspondientes DFs, se ven juntas. Añadimos un espacio.
            Vector<String> lista2 = new Vector<String>();
            lista2.add(""); //Cabecera de la lista desplegable
            String aux="";
            for (int i=1;i<lista.size();i++){//empieza desde 1, porque en la posicion 0 esta "".
                aux=lista.elementAt(i).substring(0, 6)+"  "+lista.elementAt(i).substring(6);
                aux=aux.replaceAll("-", "/");
                lista2.add(aux);
            }
            //---------------------------
            listado=new JComboBox(lista2);
            listado.setEditable(true);				
            listado.setSelectedIndex(0);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
	    }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
        archivo.delete();
    }
	
    //El nº pasado por parámetro, nos indicará la carpeta en donde buscar, dependiendo del nº de consultas
    //realizadas a la vez (1 ó 4). 0=1�, 1=2�, 2=3� y 3=4� carpeta, respectivamente.
    private void verGraficaEnGrande(int i){
        File dir = new File(ruta_graficos);
	String[] ficheros = dir.list();
	String ruta_aux=ruta_graficos+"\\"+ficheros[i]+"\\";
	String nombreImagen=ruta_aux+"EvolucionBig.jpg";
	File aux = new File(nombreImagen);			//Creamos un fichero auxiliar con el nombre anterior.
	if (!aux.exists()){							//Comprobamos si existe. Sino existe, era una consulta B.
            nombreImagen=ruta_aux+"BarrasBig.jpg";	//Y cambiamos el nombre.
            aux = new File(nombreImagen);
            if (!aux.exists()){
                nombreImagen=ruta_aux+"MaxMinMedBig.jpg";	//Y cambiamos el nombre.
		aux = new File(nombreImagen);
		if (!aux.exists()) nombreImagen=ruta_aux+"DiferenciasBig.jpg";	//Cambia al ultimo nombre posible.
            }
	}
	ImageIcon icon = new ImageIcon(nombreImagen);
	//JOptionPane.showMessageDialog(interfaz, icon, "Statistics", JOptionPane.CLOSED_OPTION);
	JOptionPane.showMessageDialog(interfaz, icon, "Estadísticas", JOptionPane.DEFAULT_OPTION);
	/*String string1 = "Acept"; //PARA INGLéS
	Object[] options = {string1};
	JOptionPane.showOptionDialog(interfaz,
	 			     "",
                                     "Statistics",	//titulo de la ventana
                                     JOptionPane.YES_OPTION,
                                     JOptionPane.DEFAULT_OPTION,
                                     icon, 		//use a custom Icon
                                     options, 	//the titles of buttons
                                     string1); 	//the title of the default button
        */
    }

    /**************************
     * MANEJADORES DE EVENTOS *
     **************************/
    class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            Object source = e.getItemSelectable();
            int estado=e.getStateChange();
            if (source==dual & estado==1) {			//Definir para las consultas A,B y D eje dual.
                ejeDual="dual";
		//Si es eje dual, sólo pueden tener 2 DGs.
		if (c==3) nd=1;
		else {
                    nd=2;
                    ndirs.setSelectedIndex(2);
                    ndirs.setEditable(false);
                    ndirs.setEnabled(false);
                    boton2.setEnabled(false);
		}
            }
            if (source==dual & e.getStateChange()==ItemEvent.DESELECTED){
                ejeDual="";
		nd=0;				//Se deseleccion� el eje dual
		if (c==1 || c==2){			
                    ndirs.setSelectedIndex(0);
                    ndirs.setEditable(true);
                    ndirs.setEnabled(true);
                    boton2.setEnabled(true);
		}
            }
            if (source==c1 & estado==1) {			//consulta A
                c=1;
		vistaInicial2();					//Vuelve a la vista inicial, quita las subopciones de D
		if (c==1) {							//Desactiva los dem�s
                    c2.setSelected(false);
                    c3.setSelected(false);
                    c4.setSelected(false);
                    dual.setSelected(false);
                }
            }
            if (source==c2 & estado==1){ 			//consulta B
                c=2;
		vistaInicial2();					//Vuelve a la vista inicial, quita las subopciones de D
		if (c==2) {							//Desactiva los dem�s
                    c1.setSelected(false);
                    c3.setSelected(false);
                    c4.setSelected(false);
                    dual.setSelected(false);
                }
            }
            if (source==c3 & estado==1){ 	//consulta C
                c=3;
                nd=1;				//Inicializamos ya el n�mero de DGs, x defecto
                vistaInicial3();			//Vuelve a la vista inicial, quita las subopciones de D
                if (c==3) {				//Desactiva los dem�s
                    c1.setSelected(false);
                    c2.setSelected(false);
                    c4.setSelected(false);
                    dual.setSelected(false);
                }
            }
            if (source==c4 & estado==1){ 			//consulta D
                c=4;
                nd=2;					//Inicializamos ya el n�mero de DGs, x defecto
                vistaInicial4();				//Mostrar las 2 subopciones
                if (c==4) {					//Desactiva los dem�s
                    c1.setSelected(false);
                    c2.setSelected(false);
                    c3.setSelected(false);
                    dual.setSelected(false);
                }
            }
            if (source==c4a & estado==1){ 			//consulta D, %
                c=5;
                cd=1;
                if (f==0) vistaInicial4();			//Mostrar las 2 subopciones
                else vistaIntermedia3();
                if (c==5) c4b.setSelected(false);	//Desactiva los dem�s
            }
            if (source==c4b & estado==1){ 			//consulta D, Abs
                c=6;
                cd=2;
                if (f==0) vistaInicial4();			//Mostrar las 2 subopciones
                else vistaIntermedia3();
                if (c==6) c4a.setSelected(false);	//Desactiva los dem�s
            }
            if (source==f1 & estado==1) {			//Intervalo
                f=1;
                f2.setSelected(false);
                if (c==4 && cd==0) JOptionPane.showMessageDialog(interfaz, "Pulse algunas de las subopciones de la cuarta consulta",
             						         "Aviso", JOptionPane.WARNING_MESSAGE);
                else if (c!=0) vistaIntermedia3();
            }
            if (source==f2 & estado==1){ 			//Completo
                f=2;
                f1.setSelected(false);
                if (c==4 && cd==0) JOptionPane.showMessageDialog(interfaz, "Pulse algunas de las subopciones de la cuarta consulta",
							         "Aviso", JOptionPane.WARNING_MESSAGE);
                else if (c!=0) vistaIntermedia3();
            }
        }
    }
	
    class EventHandler2 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source==boton2) {		//Seleccionar nº de DGs
                if (control!=0) JOptionPane.showMessageDialog(interfaz, "Ya ha creado gráficas, si desea crear más\n pulse File->Nuevo",
							      "Aviso", JOptionPane.WARNING_MESSAGE);
				
		else if (g==0) JOptionPane.showMessageDialog(interfaz, "Ya ha seleccionado los datos de todas las gráficas.\n             PULSE <<GENERAR GRÁFICAS>>",
							    "Aviso", JOptionPane.WARNING_MESSAGE);
		else if (c==0) JOptionPane.showMessageDialog(interfaz, "Seleccione un tipo de consulta.",
							     "Aviso", JOptionPane.WARNING_MESSAGE);
		else{
                    String s = (String)ndirs.getSelectedItem();
                    nd=Integer.parseInt(s);
		}
            }
            if (source==boton1) {		//Seleccionar DGs
                if (control!=0) JOptionPane.showMessageDialog(interfaz, "Ya ha creado gráficas, si desea crear más\n pulse File->Nuevo",
							      "Aviso", JOptionPane.WARNING_MESSAGE);

		else if (g==0) JOptionPane.showMessageDialog(interfaz, "Ya ha seleccionado los datos de todas las gráficas.\n             PULSE <<GENERAR GRÁFICAS>>",
							     "Aviso", JOptionPane.WARNING_MESSAGE);
				
		else if (nd==0) JOptionPane.showMessageDialog(interfaz, "Primero elija el número de direcciones a procesar\n   y a continuación pulse 'Seleccionar'",
						 	      "Aviso", JOptionPane.WARNING_MESSAGE);
		else{
                    if (nd1<nd){
                        String s = (String)listado.getSelectedItem();
			if (s=="") JOptionPane.showMessageDialog(interfaz, "Seleccione alguna dirección de grupo.",
								 "Aviso", JOptionPane.WARNING_MESSAGE);
			else{
                            direcciones.add(s);
                            vistaIntermedia1();
                            nd1++;
                            if (nd1==nd) vistaIntermedia2();
			}
                    }
                    else JOptionPane.showMessageDialog(interfaz, "Ya ha introducido todas las direcciones",
                        			       "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            }
            if (source==boton3) {		//Seleccionar un fichero ya elegido para tratar en la consulta.
                if (control!=0) JOptionPane.showMessageDialog(interfaz, "Ya ha creado gráficas, si desea crear más\n pulse File->Nuevo",
							      "Aviso", JOptionPane.WARNING_MESSAGE);

    		else if (g==0) JOptionPane.showMessageDialog(interfaz, "Ya ha seleccionado los datos de todas las gráficas.\n             PULSE <<GENERAR GRÁFICAS>>",
		 					     "Aviso", JOptionPane.WARNING_MESSAGE);
				
		else if (nd==0) JOptionPane.showMessageDialog(interfaz, "Primero elija el número de direcciones a procesar\n   y a continuación pulse 'Seleccionar'",
		  					      "Aviso", JOptionPane.WARNING_MESSAGE);
		else{
                    String s = nfich.getText();
                    if (s=="") JOptionPane.showMessageDialog(interfaz, "Seleccione algún fichero o dirección .",
                     					     "Aviso", JOptionPane.WARNING_MESSAGE);
                    else{
                        direcciones.add(rfich.getText()); 	//guardamos la ruta del fichero seleccionado
			vistaIntermedia1();
			nd1++;
			if (nd1==nd) vistaIntermedia2();
                    }
		}
            }
            if (source==abrir) {	//Boton abrir un fichero como DG
                if (c==1 || c==2) vistaInicial2();
		else if (c==3) vistaInicial3();
		else if (c>=4) vistaInicial4();
            }
            if (source==anadir) {	//Boton añadir
                if (g!=0) {
                    g--;
                    fini=fi.getText();	//Por si no se da al enter, y que se cojan
                    ffin=ff.getText();
                    rango=r.getText();
                    vistaIntermedia4();
		}
		else JOptionPane.showMessageDialog(interfaz, "Ya ha introducido los datos \n de todas las gráficas",
						   "Aviso", JOptionPane.WARNING_MESSAGE);
            }
            if (source==generar) {	//Boton GENERAR
                if (control==0){
                    insertarBarraProgreso();
                    timer=new Timer(2000,new TimerListener()); //Cada 2seg salta el timer y comprueba.
                    timer.start();
                    control++;
		}
		else JOptionPane.showMessageDialog(interfaz, "Ya ha creado gráficas, si desea crear más\n pulse Archivo->Nuevo",
						   "Aviso", JOptionPane.WARNING_MESSAGE); 	
            }
            if (source==espera) vistaGraficos();	//BOTON ESPERA, ES INVISIBLE PARA EL USUARIO.
            if (source==bgraf1) verGraficaEnGrande(0); 	//Bot�n que visualiza la GR�FICA1 en otra ventana
            if (source==bgraf2) verGraficaEnGrande(1); 	//Bot�n que visualiza la GR�FICA2 en otra ventana
            if (source==bgraf3) verGraficaEnGrande(2); 	//Bot�n que visualiza la GR�FICA3 en otra ventana
            if (source==bgraf4) verGraficaEnGrande(3); 	//Bot�n que visualiza la GR�FICA4 en otra ventana
	}
    };
		
    ActionListener manejador1 = new EventHandler2();
    ItemListener manejador2 = new CheckBoxListener();
	
    public void iniciarConexiones2() {
        ndirs.addActionListener(manejador1);
	boton1.addActionListener(manejador1);
	boton2.addActionListener(manejador1);
	boton3.addActionListener(manejador1);
	bgraf1.addActionListener(manejador1);
	bgraf2.addActionListener(manejador1);
	bgraf3.addActionListener(manejador1);
	bgraf4.addActionListener(manejador1);
	fi.addActionListener(manejador1);
	ff.addActionListener(manejador1);
	r.addActionListener(manejador1);
	anadir.addActionListener(manejador1);
	generar.addActionListener(manejador1);
	espera.addActionListener(manejador1);
	c1.addItemListener(manejador2);		
	c2.addItemListener(manejador2);	
	c3.addItemListener(manejador2);		
	c4.addItemListener(manejador2);	
	c4a.addItemListener(manejador2);	
	c4b.addItemListener(manejador2);	
	dual.addItemListener(manejador2);
	f1.addItemListener(manejador2);		
	f2.addItemListener(manejador2);	
    }
	
    //Clase creada para implementar un timer que compruebe si los hilos han finalizado
    //y asi poder parar la barra de progreso y mostrar las gr�ficas resultantes.
    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            if (H.activeCount()==0 && !graf.isAlive()) {
                timer.stop();
                espera.doClick();
            }
	}
    }
	
}
