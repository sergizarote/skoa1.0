
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.bd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.sql.DataSource;

public class Consultas extends Thread{
    String fechaFin2,fechaFin,fechaIni;
    String nombre, ruta, ruta_destino;
    Vector<String> dirsg=null;
    int intervalo,tipo;
    String fecha_ini, fecha_fin,rango;
    //static=no sirve para crear constantes, sino para crear miembros que pertenecen a la clase, y no a una instancia de la clase.
    //Estas variables si que son static, xq son comunes a todos los objetos de la clase, e.d, a cada hilo creado,
    //para que se modifiquen bien y se tomen los valores correctos.
    static SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy/MM/dd hh:mm"); //Para convertir de string a date
    static String ruta_original1="",ruta_original2="";
    static int cnum=1;
    static DataSource pool;
	
    //Constructor de la clase.
    public Consultas(ThreadGroup h, int c, int f, String fini, String ffin, String r, Vector<String> dirs, String NombreCarpetaActual, DataSource dataSource) throws IOException, SQLException{
        super(h,"HiloH");
	pool=dataSource;
	//Inicializamos aqui para que no hayan problemas de creacion de carpetas al volver a hacer un NUEVO.
	//Inicializamos los directorios correspondientes a partir de donde se encuentra el .jar
        File dir_inicial = new File ("./");
        String ruta_jar =dir_inicial.getAbsolutePath();
        String os=System.getProperty("os.name");
        //En Windows las barras son \ y en Linux /.
        String busca,reemp;
        if (os.indexOf("Win")>=0) {
            ruta_destino=ruta_jar+"\\Consultas\\";
            busca="\\";
            reemp="\\\\";
        }
        else{
            ruta_destino=ruta_jar+"/Consultas/";
            busca="/";
            reemp="//";  //La doble barra es por MySQL, en Linux no sé si hace falta.
        }
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
        if (os.indexOf("Win")>=0)  ruta=aux2+"Consultas\\\\";
        else  ruta=aux2+"Consultas//";
        //----------------------------
	cnum=1;
	ruta_original1=ruta+NombreCarpetaActual;
	ruta_original2=ruta_destino+NombreCarpetaActual;
        if (os.indexOf("Win")>=0)  {
            ruta=ruta_original1+"\\\\"+cnum+"\\\\";
            ruta_destino=ruta_original2+"\\"+cnum;
        }
        else  {
            ruta=ruta_original1+"//"+cnum+"//";
            ruta_destino=ruta_original2+"/"+cnum;
        }
	cnum++;
	File destino_consulta = new File(ruta_destino);
        destino_consulta.mkdir();
        //Abro todos los permisos de las carpetas para que en Linux no falle.
        //No afecta a Windows.
        destino_consulta.setExecutable(true,false);
        destino_consulta.setReadable(true,false);
        destino_consulta.setWritable(true, false);
	if (f==1) {				//Compara un intervalo
            intervalo=0;
            fecha_ini=fini;
            fecha_fin=ffin;
	}
	else intervalo=1;		//Compara todo	
	tipo=c;					//Para determinar el tipo de consulta
	if (c!=1) {
            rango=r;
	}
	if (f==1) corregirFechas();
	dirsg=new Vector<String>();
	for (int i=0;i<dirs.size();i++) {	 	//En DGs solo copio las DGs para usar con la base de datos.
            if (dirs.elementAt(i).indexOf("C:")>=0){	//Si contiene C: quiere decir que empieza por una ruta
                String aux=dirs.elementAt(i);		//Copiamos este fichero en la ruta_destino directamente.
		aux=aux.substring(aux.indexOf("Consultas")+1); 
		if (aux.indexOf("A")>0) aux=aux.substring(aux.indexOf("A"));
		if (aux.indexOf("B")>0) aux=aux.substring(aux.indexOf("B"));
		if (aux.indexOf("C")>0) aux=aux.substring(aux.indexOf("C"));
		if (aux.indexOf("D")>0) aux=aux.substring(aux.indexOf("D")); 
		if (tipo==1) aux="A-"+aux;	//Le añadimos el tipo de consulta actual que se le va a hacer,
		else if (tipo==2) aux="B-"+aux;	//para en graficos, saber que estamos haciendo.
		else if (tipo==3) aux="C-"+rango+"h-"+aux;
		else if (tipo==5) aux="Dp-"+aux;
		else if (tipo==6) aux="Da-"+aux;
		File origen = new File(dirs.elementAt(i));
                String barra="";
                if (os.indexOf("Win")>=0) barra="\\";
                else barra="/";
		File destino = new File(ruta_destino+barra+aux);
		try {
                    InputStream in = new FileInputStream(origen);
                    OutputStream out = new FileOutputStream(destino);                            
                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }            
                    in.close();
                    out.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
	        //Ajusta el fichero a la consulta actual. Por ejemplo: Si son datos de una consulta A, y ahora
	        //estoy haciendo una consulta B, cojo lo datos deseados, separados por un determinado rango.
                ajustarFicheroAConsulta(ruta_destino+barra+aux);
            }
            else  dirsg.add(dirs.elementAt(i));
	}
    }
	
    //Constructor de la clase.
    public Consultas(ThreadGroup h, int c, int f, String fini, String ffin, String r, Vector<String> dirs, DataSource dataSource) throws IOException, SQLException{
        super(h,"HiloH");
        pool=dataSource;
        String os=System.getProperty("os.name");
        String barra="";
        //En Windows las barras son \ y en Linux /.
        if (os.indexOf("Win")>=0) {
            ruta=ruta_original1+"\\\\"+cnum+"\\\\";
            barra="\\";
            ruta_destino=ruta_original2+barra+cnum;
        }
        else{
            ruta=ruta_original1+"//"+cnum+"//";
            barra="/";
            ruta_destino=ruta_original2+barra+cnum;
        }
        cnum++;
        File destino_consulta = new File(ruta_destino);
        destino_consulta.mkdir();
        //Abro todos los permisos de las carpetas para que en Linux no falle.
        //No afecta a Windows.
        destino_consulta.setExecutable(true,false);
        destino_consulta.setReadable(true,false);
        destino_consulta.setWritable(true, false);
        if (f==1) {			//Compara un intervalo
            intervalo=0;
            fecha_ini=fini;
            fecha_fin=ffin;
        }
        else intervalo=1;		//Compara todo
        tipo=c;				//Para determinar el tipo de consulta
        if (c!=1) {
            rango=r;
        }
        if (f==1) corregirFechas();
        dirsg=new Vector<String>();
        for (int i=0;i<dirs.size();i++) {	 	//En DGs solo copio las DGs para usar con la base de datos.
            if (dirs.elementAt(i).indexOf("C:")>=0){	//Si contiene C: quiere decir que empieza por una ruta
                String 	aux=dirs.elementAt(i);		//Copiamos este fichero en la ruta_destino directamente.
                aux=aux.substring(aux.indexOf("Consultas")+1);
                if (aux.indexOf("A")>0) aux=aux.substring(aux.indexOf("A"));
                if (aux.indexOf("B")>0) aux=aux.substring(aux.indexOf("B"));
                if (aux.indexOf("C")>0) aux=aux.substring(aux.indexOf("C"));
                if (aux.indexOf("D")>0) aux=aux.substring(aux.indexOf("D"));
                if (tipo==1) aux="A-"+aux;	//Le añadimos el tipo de consulta actual que se le va a hacer,
                else if (tipo==2) aux="B-"+aux;	//para en graficos, saber que estamos haciendo.
                else if (tipo==3) aux="C-"+aux;
                else if (tipo==5) aux="Dp-"+aux;
                else if (tipo==6) aux="Da-"+aux;
                File origen = new File(dirs.elementAt(i));
                //String barra="";
                //if (os.indexOf("Win")>=0) barra="\\";
                //else barra="/";
                File destino = new File(ruta_destino+barra+aux);
                try {
                    InputStream in = new FileInputStream(origen);
                    OutputStream out = new FileOutputStream(destino);
                    byte[] buf = new byte[4096];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (IOException ioe){
                    ioe.printStackTrace();
                }
                ajustarFicheroAConsulta(ruta_destino+barra+aux);
            }
            else dirsg.add(dirs.elementAt(i));//Sino, es una DG, y se guarda para llamar a la BD.
        }
    }

    //Procedimiento usado para comprobar que las fechas se escriben correctamente, porque tienen que
    //estar separadas por "-" y hay veces que nos podemos equivocar y separarlas por "/".
    private void corregirFechas(){
        fecha_ini=fecha_ini.replaceAll("-", "/");
        fecha_fin=fecha_fin.replaceAll("-", "/");
        if (fecha_ini.indexOf(":")==-1) fecha_ini=fecha_ini+" 00:00";
        if (fecha_fin.indexOf(":")==-1) fecha_fin=fecha_fin+" 00:00";
    }
	
    //MÉTODO PARA LANZAR EL HILO QUE REALIZA LA CONSULTA A LA B.D
    public void run() {
        if (tipo==2)
            try {
                consultaB(dirsg,fecha_ini,fecha_fin,rango,ruta,intervalo,tipo);	//CONSULTA B
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        else {
            try {
                consultaACD(dirsg,fecha_ini,fecha_fin,rango,ruta,intervalo,tipo);			//CONSULTA A, o C, o D
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
    private void ajustarFicheroAConsulta(String nfich) {
        if (intervalo==1){}			//Se coge todo el fichero tal cual. COMPLETO.
        else tratarPorIntervalo(nfich);		//Se coge el fichero desde las fechas indicadas. INTERVALO.
        if (tipo==2) tratarPorRango(nfich);	//Se trata de una consulta B.Coger fechas separadas por rango.
    }

    private void tratarPorIntervalo(String nfich) {
        //OJO, los del fichero estan separados por '-', por eso declaro otro SimpleDateFormat
        SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        File archivo = new File(nfich);
        FileReader fr = null;
        BufferedReader linea = null;
        String line,f;
        Date d=null,di=null,df=null;
        String ficheroTemporal=ruta_destino+"\\temporal.txt";
        try {
            di=formatoDelTexto.parse(fecha_ini);
            df=formatoDelTexto.parse(fecha_fin);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        int ri,rf;
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(ficheroTemporal,true));
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr);
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                //Sacamos la primera fecha a comparar con fecha_ini
                f=line.substring(0, line.indexOf("\t"));
                d=formatoDelTexto2.parse(f);	//Fecha de la linea actual.
                ri=d.compareTo(di);
                rf=d.compareTo(df);
                //si ri>0 significa que la fecha d es mayor que la inicial, por lo tanto entra dentro del rango
                //si rf<0 significa que la fecha d es menor que la final, por lo tanto entra dentro del rango
                if (ri>0 && rf<0) bw.write(line+"\n"); //GUARDO LA LINEA EN EL FICHERO TEMPORAL.
                //Sino, no la guardo.
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
                archivo.delete();
                File archivo2 = new File(ficheroTemporal);
                File fichfinal= new File(nfich);
                archivo2.renameTo(fichfinal);
            }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
    }
	
    //EL FICHERO YA ESTA TRATADO Y ESTÁN LAS FECHAS DEL INTERVALO TEMPORAL CORRESPONDIENTE. AHORA SÓLO HAY
    //QUE COGER AQUELLAS SEPARADAS POR UN RANGO DETERMINADO.
    private void tratarPorRango(String nfich) {
        //OJO, los del fichero estan separados por '-', por eso declaro otro SimpleDateFormat
        SimpleDateFormat formatoDelTexto2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        File archivo = new File(nfich);
        FileReader fr = null;
        BufferedReader linea = null;
        String line,f;
        Date d=null,dr=null;
        String ficheroTemporal=ruta_destino+"\\temporal.txt",h;
        int r,nh;
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(ficheroTemporal,true));
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr);
            if ((line=linea.readLine())!=null){ //La primera linea siempre se guarda, por ser la 1º fecha
                f=line.substring(0, line.indexOf("\t"));
                d=formatoDelTexto2.parse(f);	//Fecha de la linea actual.
                h=f.substring(f.indexOf(" ")+1);
                h=h.substring(0, h.indexOf(":"));
                if (h.charAt(0)=='0') h=h.substring(1);
                nh=Integer.parseInt(h);
                d.setHours(nh);
                bw.write(line+"\n");
            }
            //calculo la nueva fecha: dr=di+r
            Calendar c=Calendar.getInstance();	//Debemos hacer uso de una variable calendar para modificar
            c.setTime(d);						//las fechas segun el rango pasado, en base a la fecha cogida.
            int ra=Integer.parseInt(rango);
            c.add(Calendar.HOUR_OF_DAY, ra); 	//Incrementamos el nº de horas segun el rango.Hour_of_day es la hora en formato 24h
            dr=c.getTime();						//dr ES LA FECHA di+r
            //A continuacion la buscamos.
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                //Sacamos la primera fecha a comparar con fecha_ini
                f=line.substring(0, line.indexOf("\t"));
                d=formatoDelTexto2.parse(f);	//Fecha de la linea actual.
                h=f.substring(f.indexOf(" ")+1);
                h=h.substring(0, h.indexOf(":"));
                if (h.charAt(0)=='0') h=h.substring(1);
                nh=Integer.parseInt(h);
                d.setHours(nh);
                r=d.compareTo(dr);
                //si r>0 significa que la fecha d es mayor que la inicial, por lo tanto entra dentro del rango
                //si r=0 significa que la fecha d es igual que la final, por lo tanto entra dentro del rango
                if (r>=0) {
                        bw.write(line+"\n"); //GUARDO LA LINEA EN EL FICHERO TEMPORAL.
                        //Vuelvo a buscar la nueva fecha dr, en base a la que acabo de guardar.
                        c.setTime(d);
                        c.add(Calendar.HOUR_OF_DAY, ra);
                        dr=c.getTime();
                }
                //Sino, no la guardo.
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
                archivo.delete();
                File archivo2 = new File(ficheroTemporal);
                File fichfinal= new File(nfich);
                archivo2.renameTo(fichfinal);
            }catch (Exception e2){ 			//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
    }

    //CONSULTA A,C y D comparten el mismo codigo.
    private static void consultaACD(Vector<String> dGs2, String fecha_ini, String fecha_fin, String rango, String ruta, int intervalo, int tipo) throws IOException, SQLException{
        Vector<String> DGs=new Vector<String>();
        DGs.addAll(0, dGs2);
        Connection con=null;
        con=pool.getConnection();
        String nombre="";
        if (intervalo==0){		//Compara un intervalo
            Statement stmt = con.createStatement();
            //select Fecha, Valor from fv, dirs where Dirs_idDirs=idDirs and DG="nombre" and fecha > 'valor_fecha' and fecha < 'valor_fecha';
            //OJO: valor_fecha en formato "aaaa/mm/dd" o "aaaa/mm/dd hh:mm" porque en "hh:mm" no se puede
            while (!DGs.isEmpty()){
                String DG,DF;
                DG=DGs.elementAt(0).substring(0, 6);
                DF=DGs.elementAt(0).substring(9);
                DG=DG.replaceAll("/", "-");//No se aceptan las barras en los nombres de los ficheros.
                if (tipo==1) nombre="A-I-"+DG+" "+DF+".txt";
                else if (tipo==3) nombre="C-I-"+rango+"h-"+DG+" "+DF+".txt";
                else if (tipo==5) nombre="Dp-I-"+rango+"m-"+DG+" "+DF+".txt";
                else if (tipo==6) nombre="Dd-I-"+rango+"m-"+DG+" "+DF+".txt";
                String consulta2="select Fecha, Valor into outfile '"+ruta+nombre+"' from FV, Dirs where Dirs_idDirs=idDirs and DG='"+DG+"' and DF='"+DF+"' and Fecha > '"+fecha_ini+"' and Fecha < '"+fecha_fin+"' order by Fecha ASC;";
                DGs.remove(0); //Eliminamos la primera posicion, que es la que acabamos de leer.
                stmt.executeQuery(consulta2);
            }
            stmt.close();
        }
        else{					//Compara todo
            Statement stmt = con.createStatement();
            //select Fecha, Valor from fv, dirs where Dirs_idDirs=idDirs and DG="nombre";
            while (!DGs.isEmpty()){
                String DG,DF;
                DG=DGs.elementAt(0).substring(0, 6);
                DF=DGs.elementAt(0).substring(9);
                DG=DG.replaceAll("/", "-");
                if (tipo==1) nombre="A-T-"+DG+" "+DF+".txt";
                else if (tipo==3) nombre="C-T-"+rango+"h-"+DG+" "+DF+".txt";
                else if (tipo==5) nombre="Dp-T-"+rango+"m-"+DG+" "+DF+".txt";
                else if (tipo==6) nombre="Dd-T-"+rango+"m-"+DG+" "+DF+".txt";
                String consulta2="select Fecha, Valor into outfile '"+ruta+nombre+"' from FV, Dirs where Dirs_idDirs=idDirs and DG='"+DG+"' and DF='"+DF+"' order by Fecha ASC;";
                DGs.remove(0); //Eliminamos la primera posicion, que es la que acabamos de leer.
                stmt.executeQuery(consulta2);
            }
            stmt.close();
        }
        try {
            if (null != con)  con.close();// En realidad no cierra, solo libera la conexion.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    //NUEVO CóDIGO DE LA CONSULTAB, mas optimizado.
    private static void consultaB(Vector<String> dGs2, String fecha_ini, String fecha_fin, String rango, String ruta, int intervalo, int tipo) throws IOException, SQLException{
        Vector<String> DGs=new Vector<String>();
        DGs.addAll(0, dGs2);
        Connection con=null;
        con=pool.getConnection();
        if (intervalo==0){		//COMPARA UN INTERVALO
            Statement stmt = con.createStatement();
            //select Fecha, Valor from fv, dirs where Dirs_idDirs=idDirs and DG="nombre" and fecha > 'fecha_ini' and fecha < 'fecha_ini + rango' and fecha < 'valor_fecha';
            //OJO: fecha_ini va cambiando, siendo la última de la consulta anterior.
            //OJO: valor_fecha en formato "aaaa/mm/dd" o "aaaa/mm/dd hh:mm" porque en "hh:mm" no se puede
            String fechaIni=fecha_ini, fechaFin=fecha_fin, fechaFin2;
            //El rango ya se inicializó en el constructor.
            String aux1=fechaIni; //Esta variable es para guardar la fecha inicial para usarla con todas las DGs.
            while (!DGs.isEmpty()){
                String DG,DF;
                DG=DGs.elementAt(0).substring(0, 6);
                DF=DGs.elementAt(0).substring(9);
                DG=DG.replaceAll("/", "-");//No se aceptan las barras en los nombres de los ficheros.
                fechaIni=aux1;
                String nombre2=ruta+"B-I-"+rango+"h-"+DG+" "+DF+".txt";
                //Hago varias consultas, por no poder hacerlo directamente. El 1º resultado es el que nos interesa.
                while (true){
                    fechaFin2=nuevas_fechas(rango,fechaIni);
                    String consulta2="select Fecha, Valor from FV, Dirs where Dirs_idDirs=idDirs and DG='"+DG+"' and DF='"+DF+"' and Fecha > '"+fechaIni+"' and Fecha > '"+fechaFin2+"' and Fecha < '"+fechaFin+"' order by Fecha ASC limit 1;";
                    ResultSet rs =stmt.executeQuery(consulta2); //El ResultSet es como una lista donde esta el resultado de la consulta.
                    if (rs.next()){} //Vamos al primer resultado. Con este metodo pasamos de resultado en resultado.
                    else break;	 //Si es false, es que el set es nulo, por lo que la consulta no devuelve nada. Entendemos que ha acabado.
                    String f=rs.getString(1);
                    f=f.substring(0, f.indexOf("."));
                    String L1=f+"\t"+rs.getString(2);
                    guardarResultado(L1,nombre2); //Guardamos el primer resultado del set obtenido, pues es el que buscamos.
                    fechaIni=escogerFecha(L1,fechaIni,fechaFin2);
                }
                DGs.remove(0); //Eliminamos la primera posicion, que es la que acabamos de leer.
            }
            stmt.close();
        }
        else{ //COMPARA TODAS LAS ENTRADAS DE LA BASE DE DATOS.
            Statement stmt = con.createStatement();
            while (!DGs.isEmpty()){
                String DG,DF;
                DG=DGs.elementAt(0).substring(0, 6);
                DF=DGs.elementAt(0).substring(9);
                DG=DG.replaceAll("/", "-");//No se aceptan las barras en los nombres de los ficheros.
                String nombre2=ruta+"B-T-"+rango+"h-"+DG+" "+DF+".txt"; //Para guardar resultados.
                //Hago la primera consulta para coger la fecha inicial (de la primera entrada).
                String consulta2="select Fecha, Valor from FV, Dirs where Dirs_idDirs=idDirs and DG='"+DG+"' and DF='"+DF+"' order by Fecha ASC limit 1;";
                ResultSet rs=stmt.executeQuery(consulta2); //El ResultSet es como una lista donde está el resultado de la consulta.
                if (rs.next()){} //Vamos al primer resultado. Con este metodo pasamos de resultado en resultado.
                else break;		 //Si es false, es que el set es nulo, por lo que la consulta no devuelve nada. Entendemos que ha acabado.
                String f=rs.getString(1);
                f=f.substring(0, f.indexOf("."));
                String L1=f+"\t"+rs.getString(2);
                guardarResultado(L1,nombre2);
                //Modifico '-' por '/'
                String fechaIni=f.replaceAll("-", "/"),fechaFin2;
                //Hago varias consultas, por no poder hacerlo directamente. El 1º resultado del set es el que interesa
                while (true){
                    fechaFin2=nuevas_fechas(rango,fechaIni);
                    //String consulta2="select Fecha, Valor into outfile '"+nombre+"' from fv, dirs where Dirs_idDirs=idDirs and DG='"+DGs.elementAt(0)+"' and fecha > '"+fechaIni+"' and fecha > '"+fechaFin2+"' and fecha < '"+fechaFin+"';";
                    //consulta2="select Fecha, Valor from fv, dirs where Dirs_idDirs=idDirs and DG='"+DGs.elementAt(0)+"' and fecha > '"+fechaIni+"' and fecha > '"+fechaFin2+"' and fecha < '"+fechaFin+"';";
                    consulta2="select Fecha, Valor from FV, Dirs where Dirs_idDirs=idDirs and DG='"+DG+"' and DF='"+DF+"' and Fecha > '"+fechaFin2+"' order by Fecha ASC limit 1;"; //Ponemos Limit 1 para que devuelva sólo el primer valor que encuentre.
                    rs =stmt.executeQuery(consulta2); //El ResultSet es como una lista donde está el resultado de la consulta.
                    if (rs.next()){} //Vamos al primer resultado. Con este metodo pasamos de resultado en resultado.
                    else break;		 //Si es false, es que el set es nulo, por lo que la consulta no devuelve nada. Entendemos que ha acabado.
                    f=rs.getString(1);
                    f=f.substring(0, f.indexOf("."));
                    L1=f+"\t"+rs.getString(2);
                    guardarResultado(L1,nombre2); //Guardamos el primer resultado del set obtenido, pues es el que buscamos.
                    fechaIni=escogerFecha(L1,fechaIni,fechaFin2);
                }
                DGs.remove(0); //Eliminamos la primera posicion, que es la que acabamos de leer.
            }
            stmt.close();
        }
        try {
            if (null != con)  con.close();// En realidad no cierra, solo libera la conexion.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Elige la siguiente fechaIni del intervalo donde realizar la consulta, en base a la ultima fecha escogida.
    private static String escogerFecha(String L1,String fechaIni,String fechaFin2) {
        String f,aux=L1.substring(0, L1.indexOf("\t"));
        f=aux.substring(0, aux.indexOf("-"))+"/";
        aux=aux.substring(aux.indexOf("-")+1);
        f=f+aux.substring(0, aux.indexOf("-"))+"/";
        f=f+aux.substring(aux.indexOf("-")+1);
        if (!compararFechas(fechaIni, f)) fechaIni=f;
        else fechaIni=fechaFin2;
        return fechaIni;
    }

    /****************************************************************
     * Función que calcula la nueva fecha fechaFin2 pasado el rango *
     ****************************************************************/
    @SuppressWarnings("deprecation")
    private static String nuevas_fechas(String rango,String fechaIni) {
        Date fecha1=null;
        try {
            if (fechaIni.charAt(0)==' ') fechaIni=fechaIni.substring(1);
            String a=fechaIni.substring(fechaIni.indexOf(" ")+1, fechaIni.indexOf(":"));
            if (a.charAt(0)=='0') a=a.substring(1);
            int h=Integer.parseInt(a);
            fecha1 = formatoDelTexto.parse(fechaIni);
            fecha1.setHours(h);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c2=Calendar.getInstance();	//Debemos hacer uso de una variable calendar para modificar
        c2.setTime(fecha1);			//las fechas segun el rango pasado, en base a la fechaFin2.
        int r=Integer.parseInt(rango);
        c2.add(Calendar.HOUR_OF_DAY, r); //Incrementamos el nº de horas segun el rango.Hour_of_day es la hora en formato 24h
        fecha1=c2.getTime();
        String fechaFin2=fecha1.toString();
        //Convertir fecha a formato aaaa/mm/dd hh:mm
        String aux=fechaFin2;
        String m="",d,a,h;
        aux=aux.substring(4);
        if(aux.substring(0,3).equals("Jan")) m="01";
        if(aux.substring(0,3).equals("Feb")) m="02";
        if(aux.substring(0,3).equals("Mar")) m="03";
        if(aux.substring(0,3).equals("Apr")) m="04";
        if(aux.substring(0,3).equals("May")) m="05";
        if(aux.substring(0,3).equals("Jun")) m="06";
        if(aux.substring(0,3).equals("Jul")) m="07";
        if(aux.substring(0,3).equals("Agu")) m="08";
        if(aux.substring(0,3).equals("Sep")) m="08";
        if(aux.substring(0,3).equals("Oct")) m="10";
        if(aux.substring(0,3).equals("Nov")) m="11";
        if(aux.substring(0,3).equals("Dec")) m="12";
        aux=aux.substring(4);
        int i1=0;
        while(aux.charAt(i1)!=' ') i1++;
        d=aux.substring(0,i1);
        aux=aux.substring(i1+1);
        i1=0;
        while(aux.charAt(i1)!=' ') i1++;
        h=aux.substring(0,i1);
        a=aux.substring(i1+4);
        fechaFin2=a+"/"+m+"/"+d+" "+h;
        return fechaFin2;
    }

    /**************************************************************
     * Compara dos fechas.                                        *
     * False si fechaIni es menor(anterior) que fechaFin.         *
     * True si fechaIni es mayor(posterior) o igual que fechaFin. *
     **************************************************************/
    private static boolean compararFechas(String fechaIni, String fechaFin) {
        //Formato de las fechas: "aaaa/mm/dd" o "aaaa/mm/dd hh:mm"
        Date fecha1=null;
        Date fecha2=null;
        try {
            fecha1 = formatoDelTexto.parse(fechaIni);
            fecha2 = formatoDelTexto.parse(fechaFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fecha1.equals(fecha2)) return true; //Iguales
        else { //Distintas
            if (fecha1.after(fecha2)) return true;
            else return false;
        }
    }

    private static void guardarResultado(String L1,String nombre2){
        //GUARDAR L1 (solo para el primero) Y LF EN EL FICHERO TENIENDO EN CUENTA QUE LF SERA L1 DEL SIGUIENTE FICHERO TEMPORAL
        File fichero_buscado = new File(nombre2);
        try {
            if (fichero_buscado.exists()){	//Abre fichero existente y añade datos
                BufferedWriter bw=new BufferedWriter(new FileWriter(nombre2,true)); //true, para guardar al final del fichero
                bw.write(L1+"\n");
                bw.close();
            }
            else{	//Sino, crea fichero y a;ade datos
                fichero_buscado.createNewFile(); 									//CREAR FICHERO
                BufferedWriter bw=new BufferedWriter(new FileWriter(nombre2,true));	//GUARDAR DATOS
                bw.write(L1+"\n");
                bw.close();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
}
