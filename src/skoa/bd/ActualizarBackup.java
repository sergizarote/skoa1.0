/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.bd;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import tuwien.auto.eibpoints.PointList;
import tuwien.auto.eibxlator.PDUXlatorList;
import tuwien.auto.eibxlator.PointPDUXlator;
import tuwien.auto.eicl.util.EICLException;


/**
 * 
 * @author
 * Clase para clasificar todos los ficheros y guardar dicha infromacion en la base de datos
 */
public class ActualizarBackup extends Thread {
	
	/**
	 * Variables de conexion DB
	 */
	
	
	private static Connection conn = null;
	
	/**
	 * Variables para el manejo de archivos
	 */
	
	private static String barra="";   //dependiente del OS
	private String fichAClasificar="";
	
	private static String ruta_destino;
	private static String ruta_origenA;
	static String ruta, ruta2,Dirs_idDir="";
	
	

	/**
	 * Variables de control en lectura de archivo
	 */
	private static int idDirs=0;
	private static int cont_lineas, primera_linea;
	private static boolean ignorar;
	private static String DF,DG,V,F, DG_real;
	
	/**
	 * Variables para los contadores
	 */
	private static PointList Lcontadores = new PointList();
	static Vector<String> DG_TyS=new Vector<String>();
	
	
	/**
	 * variables no usadas
	 */
	//private String nombreCarpetaLogs="Logs"; //"log lab";
	//private String ruta_origenC; varaible nunca utilizada
	
	/**
	 * Constructores
	 */
	public ActualizarBackup(){}
	public ActualizarBackup(String fich){
        fichAClasificar=fich;
    }
	
	/**
	 * Metodo sobrecargado de la clase Thread para la ejecucion del hilo
	 */
	public void run() {
		//Inicializamos los directorios correspondientes a partir de donde se encuentra el .jar
        File dir_inicial = new File ("./");
        String ruta_jar =dir_inicial.getAbsolutePath();
        
        //En Windows las barras son \ y en Linux /.
        String os=System.getProperty("os.name");   
      
        //dependiente del OS construimos ruta_destino
        String busca,reemp;
        if (os.indexOf("Win")>=0) {
            barra="\\";
            ruta_destino=ruta_jar+"\\Clasificados";
            busca="\\";
            reemp="\\\\";
        }else{
            barra="/";
            ruta_destino=ruta_jar+"/Clasificados";
            
            busca="/";
            reemp="//";  //La doble barra es por MySQL, en Linux no sé si hace falta.
        }
        
        //Donde están los Logs
        //ruta_origenC=ruta_jar+barra+nombreCarpetaLogs; 
        
        
        String aux1=ruta_jar,aux2="";
        //Cuando se usa una ruta en la base de datos, debe tener doble barra, es decir \\\\
        while(aux1.length()!=0){ //Busca la \="\\" y se la pone doble \\="\\\\"
            if (aux1.indexOf(busca)>=0) {
                aux2=aux2+aux1.substring(0,aux1.indexOf(busca));
                aux1=aux1.substring(aux1.indexOf(busca)+1);
            }
            else {
                aux2=aux2+aux1.substring(0);
                aux1="";
            }
            aux2=aux2+reemp;
        }
        
        //dependiente del OS construimos la ruta_origenA
        if (os.indexOf("Win")>=0) ruta_origenA = aux2 + "Clasificados\\\\";
        else ruta_origenA = aux2+"Clasificados//";
  
        
        
        //realizamos la conexion a base de datos
        cargarBD();
        
        //En primer lugar, se buscan el último índice insertado en la tabla dirs.
        BuscarIndicesBD();
        try {
            clasificarFicheros();
            actualizarBD();
        } catch (EICLException e) {
            //e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	cerrarBD();
        }
	}
	
	
	
	
	
	
	
	
	
	
	
	

    /*************************************************************************************
     * Metodos para clasificar los ficheros
     *************************************************************************************/
    
	/**
	 * Metodo principal para la calisificacion de archivos
	 */
	private void clasificarFicheros() throws EICLException {
        //Si hay un fichero backup, se analiza sólo ese, el cual ya tiene la ruta de Logs, y el nombre.
        //En ruta_destino, esta la ruta de clasificados.
    	
        File directorio_destino = new File(ruta_destino);
        
        //Con mkdir se asume que la ruta existe, excepto el ultimo que sera el directorio generado
        //Si hay ficheros en el directorio Clasificados, se procede a su lectura.
        directorio_destino.mkdir(); 
        
        //LEER FICHEROS .DAT (CON TIPOS Y SUBTIPOS)
        leer_contadores();
        analiza_fichero(fichAClasificar);
        
        return;
    }

	
	/**
	 * Metodo para leer los archivos de los contadores
	 */
    private static void leer_contadores() throws EICLException{
    	//crea un objecto File desde la ruta en la que se ejcuta el jar y 
    	//lista los archivo dentro de esa carpeta
    	
    	String ruta_contadores = new File ("./").getAbsolutePath();
        File dir_contadores=new File(new File ("./").getAbsolutePath());
        String[] archivos=dir_contadores.list();
        
       
        if (archivos==null){
        	System.out.println("No hay ficheros en el directorio");
        }
        
        
        ruta_contadores = ruta_contadores + barra;
       
        
        //recorre la lista de archivos apra leerlos
        for (int x=0;x<archivos.length;x++) {
            
        	//leemos el archivos si su extension es  .dat
        	//solo los archivos de contadores
            if (archivos[x].indexOf(".dat")>=0){ 
            	
            	//creamos la ruta y se lee el fichero de contadores *.dat
            	String ruta=ruta_contadores+archivos[x];
                Lcontadores = new PointList(ruta);		
                
                //Obtiene un vector con todos los elementos del archivo
                String aux[]=Lcontadores.getPointDescriptions();
                int i=0;
                while (i<aux.length) {
                	//Obtengo las DG, los Tipos y Subtipos.
                    DG_TyS.addElement(aux[i].substring(9));	
                    i++;
                }
            }
        }
    }
    
    
	
    private static void borrarDirectorioClasificar() {
        //En ruta_destino esta la ruta de la carpeta clasificados.
        File d=new File(ruta_destino);
        if (d.exists()){
            File[] ficheros = d.listFiles();
            for (int x=0;x<ficheros.length;x++) ficheros[x].delete(); //Borra los ficheros.
            d.delete();
        }
    }
	
    
    
	
	
	
	
	
    
    
    /**
	 * Metodo analizar los ficheros
	 */
    private static void analiza_fichero(String nombre){
        //crea un objecto File desde la ruta pasada como parametro 
    	File archivo = new File(nombre);
        
    	//declara las variables de lectura de buffer para leer el archivo
    	FileReader fr = null;
        BufferedReader linea = null;
        
        String line;
        //ABRIR FICHERO Y LEER
        try {
        	//Se crea para leer las lineas
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 	
            
            primera_linea=0;
            cont_lineas=1;
            ignorar=false;
            //LEE la 1º linea,  busca la informacion, y se guarda al comienzo de la sig linea.
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                if (line.length()!=0){				//Se comprueba porque hay algunas lineas vacias.
                    if (line.indexOf("Wh")==-1) {	//Para evitar la salida con las medidas
                        mandar_a_guardar(line);
                    }
                }
            }
        }
        catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
             }catch (Exception e2){ 			//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
    }
    
    /**
   	 * Metodo para comprobar si la linea puede ser guardada
   	 */
    private static void mandar_a_guardar(String line) throws EICLException{
        //GUARDAR DATOS DE LA ITERACION ANTERIOR
        if (primera_linea!=0){				//Si no es la primera linea
            if (falta_valor(line)){			//Si falta informacion de VALOR
                V=V+line.substring(0,line.length()-1); 	//se añade lo que faltaba
                //Después de encontrar el valor, comprobar si la DF=0.0.0 y valor= 0 0 0... entonces ignorar
                //Si valor es otra cosa, entonces se acepta.
                if (DF.equals("0.0.0")) {
                    if (compruebaValorCeros()) ignorar=true;
                }
                //------------------------------------------
            }
            else {
                if (!ignorar) guardar_datos();		//No ignorar (false). Acepta y guarda.
                else ignorar=false;			//Ignorar (true). Se inicializa para las sig.
                busca_datos(line);
            }
        }
        else { 			//Si es la 1ª linea
            busca_datos(line); 	//Se busca la informacion, y se guarda al comienzo de la sig linea.
            primera_linea=1;
        }
    }
    
    /**
   	 * Metodo para guardador los daros en el fichro que sera 
   	 * cargado en la BD
   	 */
    private static void guardar_datos() throws EICLException{
        Dirs_idDir="";
        buscarPareja();
        
        //PARA USARSE CON LOAD DATA INFILE EN LA BD
        ruta=ruta_destino+barra+DG+".txt";	
        
        //Sólo guardamos en el fichero las DGs que estén en contadores.dat.
        //Del mismo modo, sólo se crea el ficheros por la misma razón.
        if (buscar_en_DG_TyS()==1) {
        	
        	//Para guardar indice-fecha-Valor-DG para usar load data infile en la BD
            String datos=F+'\t'+V+'\t'+Dirs_idDir; 
            File fichero_buscado = new File(ruta);
            
            //Abre fichero existente y añade datos
            if (fichero_buscado.exists()){	
                try{
                	//guardamos los datos en el fichero
                    BufferedWriter bw=new BufferedWriter(new FileWriter(ruta,true)); //true, para guardar al final del fichero
                    bw.write("\n"+datos);
                    bw.close(); 				//Cerrar fichero
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            
            //Sino, crea fichero y añade datos
            }else{	
                try{
                	//crea el fichero y lo guarda
                    fichero_buscado.createNewFile(); 									
                    BufferedWriter bw=new BufferedWriter(new FileWriter(ruta,true));
                    bw.write(datos);
                    bw.close(); 			//Cerrar fichero
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    
    /**
   	 * Metodo para guardador los daros en el fichro que sera 
   	 * cargado en la BD
   	 */
    private static void busca_datos(String linea){
    	
        int espacios=0, k=0, contador_puntos=0;
        if (!comprobar_si_empieza_por_fecha(linea)) ignorar=true;
        else{
            while (contador_puntos<3) {				//Se busca la fecha completa.
                if (linea.charAt(k)==':')contador_puntos++;
                k++;
            }
            F=linea.substring(0, k-1);			//Se copia la fecha a su variable.
            convertir_fecha();
            if (linea.charAt(k+1)!='P')ignorar=true;	//Si NO es informacion util (fin fichero, contadores, threads...).
            else {					//Sino, ES informacion util.
                int i=k;				//Comienza la DF.
                while(espacios<4){			//Se busca la dir. fisica.
                    if (linea.charAt(i)==' ')espacios++;
                    i++;
                }
                int l=i+1,aux;
                while (linea.charAt(l)!=' ') l++;
                DF=linea.substring(i, l);		//Se copia la dir. fisica a su variable.
                //SE COMENTÓ PORQUE EN EL LAB HAY DG QUE SU DF ES 0.0.0
                //if (DF.equals("0.0.0")) ignorar=true;	//Ignora al router (0.0.0)
                //else{
                    l=l+3; 		  		//Comienza la DG.
                    aux=l;
                    while (linea.charAt(l)!=',') l++;
                    DG=linea.substring(aux, l);		//Se copia la dir. de grupo a su variable.
                    //LAS DG QUE NO ESTÉN EN CONTADORES, SE IGNORAN Y NO SE GUARDAN NI EN DIRS.TXT NI EN SU FICH.
                    DG_real=DG;
                    modifica_dg();
                    if (!estaEnContadores()) ignorar=true;
                    //AñADIDO PARA IGNORAR ESTAS DG PORQUE NO SE TRADUCEN POR SER MARCAS DE TIEMPO.
                    /*if ((DG.equals("2/3/01"))|(DG.equals("2/3/02"))|(DG.equals("2/4/01"))|(DG.equals("2/4/02"))|
                        (DG.equals("2/5/08"))|(DG.equals("2/5/09"))) ignorar=true;*/
                    else{
                        l=l+8; 		  		//Comienza el V.
                        busca_valor(linea.substring(l, linea.length()-1));
                    }
                    //Después de encontrar el valor, comprobar si la DF=0.0.0 y valor= 0 0 0... entonces ignorar
                    //Si valor es otra cosa, entonces se acepta.
                    if (DF.equals("0.0.0")) {
                        if (compruebaValorCeros()) ignorar=true;
                    }
                    //------------------------------------------
                //}
            }
        }
    }

    
    
    /*************************************************************************************
     * Metodos Auxiliares
     *************************************************************************************/
    private static boolean falta_valor(String linea){
        if (linea.charAt(0)=='0') return true;
        if (linea.charAt(0)=='1') return true;
        if (linea.charAt(0)=='2') return true;
        if (linea.charAt(0)=='3') return true;
        if (linea.charAt(0)=='4') return true;
        if (linea.charAt(0)=='5') return true;
        if (linea.charAt(0)=='6') return true;
        if (linea.charAt(0)=='7') return true;
        if (linea.charAt(0)=='8') return true;
        if (linea.charAt(0)=='9') return true;
        if (linea.charAt(0)=='a') return true;
        if (linea.charAt(0)=='b') return true;
        if (linea.charAt(0)=='c') return true;
        if (linea.charAt(0)=='d') return true;
        if (linea.charAt(0)=='e') return true;
        if (linea.charAt(0)=='f') return true;
        return false;
    }
    
    private static boolean compruebaValorCeros(){
        //Comprobamos si V es 0 0 0 0...
        for (int i=0;i<V.length();i++){
            if (V.charAt(i)=='0' || V.charAt(i)==' '){} //Si es 0 o espacio, sigue analizando
            else return false;  //Sino, ya sabemos que no es de la forma que buscamos.
        }
        return true;//Si sale del bucle es que valor es de la forma 0 0 0...
    }

    private static int buscar_en_DG_TyS() throws EICLException{
        String dirG,elemento;
        for (int i=0;i<DG_TyS.size();i++){
            elemento=DG_TyS.elementAt(i);
            dirG=elemento.substring(0, 6);
            if (DG_real.equals(dirG)){
                V=traduccion(elemento.substring(9, 13),elemento.substring(16));
                return 1;
            }
        }
        return 0;
    }
    
    private static String traduccion(String t, String s) throws EICLException{
        //OBTENEMOS LOS DATOS EN FORMATOS BYTE[]
        byte[] d= conversion(V);
        //TRADUCCION
        PointPDUXlator ppx;
        ppx = PDUXlatorList.getPointPDUXlator(t, s);
        ppx.setAPDUByteArray(d);
        String msj = ppx.getASDUasString();
        return msj;
    }
    
    private static byte[] conversion(String valor){
        int inicio=0, fin=0;
        Vector<String> elementos=new Vector<String>();
        for (int i=0; i<valor.length();i++){
            if (valor.charAt(i)==' ') {
                if (valor.charAt(inicio)==' ') elementos.addElement(valor.substring(inicio+1,i));
                else elementos.addElement(valor.substring(inicio,i));
                inicio=i;
            }
            fin=i;
        }
        elementos.addElement(valor.substring(inicio+1,fin+1)); //añade el ultimo elemento.
        //Declaracion del vector de bytes a devolver
        byte[] d = new byte[elementos.size()];
        //CONVERSION haciendo el proceso inverso porque primero se hizo Integer.toHexString(...)
        //por lo que un int se hizo hex y despues string
        for (int j=0;j<elementos.size();j++){
            int a1=Integer.parseInt(elementos.elementAt(j), 16);
            short s1=(short) a1;
            d[j]=(byte) s1;
        }
        return d;
    }
    
    private static boolean comprobar_si_empieza_por_fecha(String linea){
        if ((linea.charAt(0)=='M') || (linea.charAt(0)=='T') || (linea.charAt(0)=='W') ||
            (linea.charAt(0)=='F') || (linea.charAt(0)=='S')) return true;
        return false;
    }
    
    private static void convertir_fecha(){
        String fecha=F.substring(4);
        String mes=fecha.substring(0,3);
        String m="";
        if (mes.equals("Jan")) m="01";	//mes
        if (mes.equals("Feb")) m="02";	//...
        if (mes.equals("Mar")) m="03";
        if (mes.equals("Apr")) m="04";
        if (mes.equals("May")) m="05";
        if (mes.equals("Jun")) m="06";
        if (mes.equals("Jul")) m="07";
        if (mes.equals("Agu")) m="08";
        if (mes.equals("Sep")) m="09";
        if (mes.equals("Oct")) m="10";
        if (mes.equals("Nov")) m="11";
        if (mes.equals("Dec")) m="12";
        String d=fecha.substring(4);
        fecha=fecha.substring(7);
        int i=0;
        while (d.charAt(i)!=' ')i++;
        d=d.substring(0,i);				//dia
        if (d.length()==1) d="0"+d;		//para que el formato de dia sea de dos digitos.
        String h=fecha.substring(0);
        i=0;
        while (h.charAt(i)!=' ')i++;
        h=h.substring(0,i);				//hora
        String a=fecha.substring(i+5);	//año
        F=a+"/"+m+"/"+d+" "+h;	//formato para MySQL 0000-00-00 00:00:00
    }
    
    private static void modifica_dg(){
        String aux="";
        for (int i=0;i<DG.length();i++){
            if (DG.charAt(i)=='/') aux=aux + '-'; 	//Modifica el nombre de DG para poder guardar el
            else aux=aux + DG.charAt(i);		//fichero con este nombre
        }
        DG=aux;
    }
    
    private static boolean estaEnContadores(){// throws EICLException{
        String dirG,elemento;
        for (int i=0;i<DG_TyS.size();i++){
            elemento=DG_TyS.elementAt(i);
            dirG=elemento.substring(0, 6);
            if (DG_real.equals(dirG)) return true;
        }
        return false;
    }
    
    private static void busca_valor(String linea){
        linea=linea.substring(1);
        V="";
        /*************************************************************************
         * Los valores válidos están formados por nº hexadecimales que comienzan *
         * por 0, seguidos por elementos de 2 cifras separadas por espacios.     *
         *************************************************************************/
        int i=0;
        while(i<linea.length()){
            if (valor_hexadecimal(linea.substring(i,i+1))){
                V=V+linea.substring(i,i+1);
                i++;
            }
            else{
                if (linea.charAt(i)==' '){
                    V=V+" ";
                    i++;
                }
                else {
                    ignorar=true;
                    break; //Es la interrupcion.
                }
            }
        }
    }
    
    private static boolean valor_hexadecimal(String caracter){
        if (caracter.equals("0")) return true;
        if (caracter.equals("1")) return true;
        if (caracter.equals("2")) return true;
        if (caracter.equals("3")) return true;
        if (caracter.equals("4")) return true;
        if (caracter.equals("5")) return true;
        if (caracter.equals("6")) return true;
        if (caracter.equals("7")) return true;
        if (caracter.equals("8")) return true;
        if (caracter.equals("9")) return true;
        if (caracter.equals("a")) return true;
        if (caracter.equals("b")) return true;
        if (caracter.equals("c")) return true;
        if (caracter.equals("d")) return true;
        if (caracter.equals("e")) return true;
        if (caracter.equals("f")) return true;
        return false;
    }

    
    
    
    
    
    
    /*************************************************************************************
     * Metodos BD
     *************************************************************************************/

	
    
    /**
	 * Metodo que realiza la conexion con la BD MYSQL
	 */
    private static void cargarBD() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn=DriverManager.getConnection(ConfigDB.URL, ConfigDB.USER, ConfigDB.PASSWORD);  //MI USUARIO Y CONTRASEÑA
        } catch (Exception e) {
            e.printStackTrace(); // handle the error
        }
    }

    /**
	 * Metodo que cierra la conexion con la BD MYSQL
	 */
    private static void cerrarBD() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // handle the error
        }
    }
    
    
    /**
   	 * Metodo para actaulzia la base de datos con los ficheros clasificados
   	 * NOTA: Usa el comando LOAD DATA para aumentar la velocidad 20 veces   
   	 */
    private static void actualizarBD() throws SQLException, IOException{
    	//crea un descritor de archvio para el directorio de origin
        File directorio_origen=new File(ruta_origenA);
        
        //lista todos los archivos de la ruta de origen
        String[] ficheros=directorio_origen.list();
     
        //crea un Statement para poder realizar una consulata DB
        Statement st = conn.createStatement();
        String consulta;
        if (ficheros!=null){
        	//recorre los ficheros encontrados
            for(int i=0;i<ficheros.length;i++){
                String nombre=ruta_origenA+ficheros[i];
                System.out.println(nombre);
                
                //crea la consulata de carga y la ejecuta
                consulta="load data infile "+"\""+nombre+"\""+" into table FV (Fecha, Valor,Dirs_idDirs);";
                st.executeUpdate(consulta);
            }
            System.out.println("BD actualizada");
        }
        else System.out.println("No habían ficheros para actualizar la BD");
        st.close();
        
        
        borrarDirectorioClasificar();
    }

    
    
    /**
	 * Metodo Busca el ultimo indice insertado, para asi cuando vayamos a añadir una nueva DF_DG,
	 * saber que indice se supondría que le pone la base de datos.
	 */
    private static void BuscarIndicesBD() {
        try {
            Statement st = conn.createStatement();
            String consulta="select count(*) from Dirs"; 		//Busca el ultimo indice de dirs
            ResultSet rs = st.executeQuery(consulta);
            rs.next();
            idDirs = rs.getInt(1);//idDirs DEL ULTIMO REGISTRO INSERTADO.
            idDirs++;
        } catch (Exception e) {
            e.printStackTrace(); // handle the error
        }
    }
    
    
    /**
	 * Metodo guarda una NUEVA pareja de DG-DF en la BD, 
	 * sino está. Actualiza Dirs_idDirs con el índice correcto
	 */
    private static void buscarPareja() {
        try {
            //Primero, busca si esta en la base de datos.
            Statement st = conn.createStatement();
            String consulta="select idDirs from Dirs where DF='"+DF+"' and DG='"+DG+"';";
            
            ResultSet rs=st.executeQuery(consulta);
            
            //ESTÁ EN LA BD.
            if (rs.next()) { 				
                String dev=""+rs.getInt(1);
                st.close();
                Dirs_idDir=dev;
            
            //NO ESTÁ EN LA BD.
            }else {
                //Inserto la pareja en la B.D
                consulta="insert into Dirs values (null,'"+DF+"','"+DG+"');";
                st.executeUpdate(consulta);
                
                //Vuelvo a hacer la consulta, para extraer el indice.
                consulta="select idDirs from Dirs where DF='"+DF+"' and DG='"+DG+"';";
                rs=st.executeQuery(consulta);
                
                //AHORA YA ESTÁ EN LA BD.
                if (rs.next()) { 		
                    String dev=""+rs.getInt(1);
                    st.close();
                    Dirs_idDir=dev;
                }else{
                	System.out.println("No se inserto bien");
                }
                st.close();
            }
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }
    
    
}