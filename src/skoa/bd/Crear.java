/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.bd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 
 * @author
 * Clase para crear la base de datos desde el archivo script.sql y cargar los datos iniciales
 */
public class Crear extends Thread{
	
	

	
	/**
	 * Variables referente al fichero script
	 */
	 private String script;
	 private static String NOMBRE_SCRIPT = "script.sql";	//ruta donde está el script.
	
	
	 /**
	  * Variables con los comando a ajecutar
	  */
	 private String comandoWindows;		//="c:/Archivos de programa/MySQL/MySQL Server 5.1/bin/mysql -u "+usuario+" -p"+password+" "+nombreBD;
	 private String comandoLinux="/usr/bin/mysql -u "+ConfigDB.USER+" -p"+ConfigDB.PASSWORD+" "+ConfigDB.DB_NAME;
	 
	 
	 
	
	/**
	 * Metodo sobrecargado de la clase Thread para la ejecucion del hilo
	 */
	public void run() {
        String ruta_jar =new File ("./").getAbsolutePath();
        String aux2="";
        
        
        //En Windows las barras son \ y en Linux /.
        String os=System.getProperty("os.name");
        String busca;
        if (os.indexOf("Win")>=0)  busca="\\";
        else busca="/";
        
        
        //Cuando se usa una ruta en la base de datos, debe tener doble barra
        while(ruta_jar.length()!=0){
            if (ruta_jar.indexOf(busca)>=0) {
                aux2=aux2+ruta_jar.substring(0,ruta_jar.indexOf(busca));
                ruta_jar=ruta_jar.substring(ruta_jar.indexOf(busca)+1);
            }
            else {
                aux2=aux2+ruta_jar.substring(0);
                ruta_jar="";
            }
            aux2=aux2+"//";//el script, siempre es esto aunque sea Windows o Linux
        }
        
        script= aux2+ NOMBRE_SCRIPT;
        Connection con=null;
        
        //Por defecto la base de datos se llamara Skoa_Bd, y el usuario sera root y la password 'estefania'.
        //Son los parametros puestos en la configuracion de MySQL al instalarse.
        try {
        	
            //Primero establecemos una conexion.
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            //mysql es la base de datos que MySQL crea automáticamente al instalarse. El usuario root y su
            //contraseña tambien se han configurado durante la instalacion de MySQL.
            
            con= DriverManager.getConnection("jdbc:mysql://localhost/mysql",ConfigDB.USER,ConfigDB.PASSWORD);
            Statement st = con.createStatement();
            
            //SE CREA LA BASE DE DATOS SINO EXISTE
            String accion="create database if not exists "+ConfigDB.DB_NAME;
            st.execute(accion);
            st.close();
            con.close();
            
            if (os.indexOf("Win")>=0) {
                //FORMATO DEL COMANDO PARA ABRIR MYSQL EN WINDOWS:
                //"c:/Archivos de programa/MySQL/MySQL Server x.x/bin/mysql -u "+usuario+" -p"+password+" "+nombreBD;
                
            	comandoWindows="c:/Archivos de programa/MySQL/";
                File dir=new File(comandoWindows);
                String versiones[]=dir.list();
                
                for (int i=0;i<versiones.length;i++){
                	//Se selecciona la ultima carpeta, que es la ultima version de MySQL Server.
                    if (versiones[i].indexOf("Server")!= -1)comandoWindows=comandoWindows+versiones[i];
                }
                comandoWindows=comandoWindows+"/bin/mysql -u "+ConfigDB.USER+" -p"+ConfigDB.PASSWORD+" "+ConfigDB.DB_NAME;
                cargarScript(comandoWindows);
                
            }else{
            	cargarScript(comandoLinux);
            }
        } catch (Exception e) {
            e.printStackTrace(); // handle the error
        }
	}
	
	
	/**
	 * Metodo para ejecutar el comando de cargar del fichero script.sql
	 * dependiendo del OS
	 */
	
	private void cargarScript(String comando) {
        try {
            // Ejecucion del cliente mysql
            Process p = Runtime.getRuntime().exec(comando);
            
            // Lectura de la salida de error y se muestra por pantalla.
            InputStream es = p.getErrorStream();
            muestraSalidaDeError(es);
            
            // Lectura del fichero de backup y redireccion a la entrada estandar de mysql.
            OutputStream os = p.getOutputStream();
            FileInputStream fis = new FileInputStream(script);
            byte buffer[] = new byte[1024];
            int leido = fis.read(buffer);
            while (leido > 0) {
                os.write(buffer, 0, leido);
                leido = fis.read(buffer);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/***********************************************************************
     * Se saca por pantalla la salida de errores del comando, por si acaso.*
     * @param es de donde leer los errores del comando mysql.              *
     ***********************************************************************/
    private void muestraSalidaDeError(final InputStream es) {
        Thread hiloError = new Thread() {
            public void run() {
                try {
                    byte[] buffer = new byte[1024];
                    int leido = es.read(buffer);
                    while (leido > 0)  leido = es.read(buffer);
                    es.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        hiloError.start();
    }	
}
