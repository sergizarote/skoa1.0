
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.helpers;

import skoa.models.MiFormatoGrafs;
import skoa.models.NuevoContador;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import skoa.views.PrincipalGrafs;

public class Lecturas {

    static List<MiFormatoGrafs> list = new ArrayList<MiFormatoGrafs>();
    static int inicializados = 0;
//---------------------------------------

    public static void inicializar() {
        System.out.println("-----entro en inicializar con fichero:" + PrincipalGrafs.nombreFichero);


        int num = PrincipalGrafs.ComboBoxAnio.getItemCount();
        if (num == 0) {
            PrincipalGrafs.ComboBoxDia.addItem("-Día-");
            PrincipalGrafs.ComboBoxMes.addItem("-Mes-");
            PrincipalGrafs.ComboBoxAnio.addItem("-Año-");
            PrincipalGrafs.ComboBoxContadores.addItem("-Contadores-");
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
        } else {

            int cuenta = PrincipalGrafs.ComboBoxContadores.getItemCount();
            System.out.println("->cuenta:" + cuenta + "-" + PrincipalGrafs.ComboBoxContadores.getItemAt(cuenta - 1));
            while (cuenta != 1) {
                System.out.println("Componente 1" + PrincipalGrafs.ComboBoxContadores.getItemAt(1));

                if (PrincipalGrafs.ComboBoxContadores.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxContadores.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxContadores.getItemCount();
            }
            //------------------------
            cuenta = PrincipalGrafs.ComboBoxAnio.getItemCount();
            while (cuenta != 1) {

                if (PrincipalGrafs.ComboBoxAnio.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxAnio.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxAnio.getItemCount();
            }
            //------------------------
            cuenta = PrincipalGrafs.ComboBoxMes.getItemCount();
            while (cuenta != 1) {

                if (PrincipalGrafs.ComboBoxMes.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxMes.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxMes.getItemCount();
            }
            //------------------------
            cuenta = PrincipalGrafs.ComboBoxDia.getItemCount();
            while (cuenta != 1) {

                if (PrincipalGrafs.ComboBoxDia.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxDia.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxDia.getItemCount();
            }
            //------------------------
            cuenta = PrincipalGrafs.ComboBoxHoraI.getItemCount();
            while (cuenta != 1) {

                if (PrincipalGrafs.ComboBoxHoraI.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxHoraI.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxHoraI.getItemCount();
            }
            //------------------------
            cuenta = PrincipalGrafs.ComboBoxHoraF.getItemCount();
            while (cuenta != 1) {

                if (PrincipalGrafs.ComboBoxHoraF.getItemAt(1) != null) {
                    PrincipalGrafs.ComboBoxHoraF.removeItemAt(1);
                }
                cuenta = PrincipalGrafs.ComboBoxHoraF.getItemCount();
            }

        }


        LeerFichero();
        Lecturas.inicializarComboContadores();
    }

//------------------------------------------------------------------------------------------
    private static void LeerFichero() {
        try {
//		    FileReader fr = new FileReader("medidas_contadores.log"); // NOMBRE FICHERO !! medidas_SKoAa

            FileReader fr = new FileReader(PrincipalGrafs.nombreFichero);//"medidas_SKoA.log");

            //eliminar lo que hay guardado en lecturas.list
            list.removeAll(list);

//                    System.out.println("abriremos el fichero: "+MaipezView.Fmedidas_MCA_cnf);
//		    FileReader fr = new FileReader(MaipezView.Fmedidas_MCA_cnf); // NOMBRE FICHERO !!

            BufferedReader bf = new BufferedReader(fr);

            String dmes = "";
            int ddia = 0;
            int daño = 0;
            String hora;
            Date fecha = new Date();
            String cadena;
            int aux = 0;
//		    Tue Nov 24 11:01:16 CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
            while ((cadena = bf.readLine()) != null) {

                aux = cadena.indexOf(" ");
                cadena = cadena.substring(aux + 1);
//			    	Nov 24 11:01:16 CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(" ");
                dmes = cadena.substring(0, aux);
                cadena = cadena.substring(aux + 1);
//			    	24 11:01:16 CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(" ");
                ddia = Integer.parseInt(cadena.substring(0, aux));
                cadena = cadena.substring(aux + 1);
//			    	11:01:16 CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(":");
                hora = cadena.substring(0, aux); //11
                cadena = cadena.substring(aux + 1);
//			    	01:16 CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(":");
                String minutos = cadena.substring(0, aux); //01
                fecha.setHours(Integer.parseInt(hora));
                fecha.setMinutes(Integer.parseInt(minutos));


                aux = cadena.indexOf(" ");
                cadena = cadena.substring(aux + 1);
//			    	CET 2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(" ");
                cadena = cadena.substring(aux + 1);
//			    	2009:  2/1/01 17341.0 Wh 2/1/04 65.0 W
                aux = cadena.indexOf(":");
                daño = Integer.parseInt(cadena.substring(0, aux));
                fecha.setYear(daño);

//			    	dhora = new Hour(fecha);
                cadena = cadena.substring(aux + 1);
//			    	.  2/1/01 17341.0 Wh 2/1/04 65.0 W
                String auxi = cadena.substring(0, 1);
                while (auxi.equals(" ")) {
                    cadena = cadena.substring(1);
                    auxi = cadena.substring(0, 1);
                }
//			    	2/1/01 17341.0 Wh 2/1/04 65.0 W
//                                System.out.println("cadena: "+cadena);
                MiFormatoGrafs insertar = new MiFormatoGrafs(daño, dmes, ddia, Integer.parseInt(hora), Integer.parseInt(minutos), cadena);
                list.add(insertar);

            }//end mientras la cadena no este vacia


            fr.close();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
            String mostrar = "No se ha podido abrir el fichero de mediciones";
            JOptionPane.showMessageDialog(null, mostrar, "alerta", 1);

        }
    }//end LeerFichero

//-------------------------------------------------------------------------------------
    private static void BuscarContadores(String cadena) {

        String dir_fisica;
        String medicion;


        int espacio = cadena.indexOf(" ");
        String aux = cadena.substring(0, espacio);
        dir_fisica = aux;
        cadena = cadena.substring(espacio + 1);
        espacio = cadena.indexOf(" ");
        aux = cadena.substring(0, espacio);
        medicion = aux;
        int punto = medicion.indexOf(".");
        medicion = medicion.substring(0, punto);
        cadena = cadena.substring(espacio + 1);
//	 NuevoContador contador = new NuevoContador(dir_fisica, (float)Integer.parseInt(medicion));
        espacio = cadena.indexOf(" "); //eliminar de la cadena Wh
        cadena = cadena.substring(espacio + 1);
        while (cadena.length() != 1) {
            espacio = cadena.indexOf(" ");
            aux = cadena.substring(0, espacio);
            dir_fisica = aux;
            cadena = cadena.substring(espacio + 1);
            espacio = cadena.indexOf(" ");
            aux = cadena.substring(0, espacio);
            medicion = aux;
            punto = medicion.indexOf(".");
            medicion = medicion.substring(0, punto);
            cadena = cadena.substring(espacio + 1);
//		 NuevoContador contador2 = new NuevoContador(dir_fisica, (float)Integer.parseInt(medicion));
            espacio = cadena.indexOf(" "); //eliminar de la cadena Wh
            cadena = cadena.substring(espacio + 1);
        }
    }

    //-----------------------------------------------------------------------------------
    public static void inicializarComboContadores() { //AñO


//     principalGrafs.ComboBoxContadores.addItem("-Contadores-");
//		if(inicializados == 0){
        int i = 0;

        for (MiFormatoGrafs miFormato : Lecturas.list) {
            for (NuevoContador NuevoContador : miFormato.contadores) {
//				miFormato.año   miFormato.dia   miFormato.mes   miFormato.fecha.getHours()
//				miFormato.fecha.getMinutes()
                int encontrado = 0;
//				 Coger numero de items 
                int num = PrincipalGrafs.ComboBoxContadores.getItemCount();
//				 coger items 
                for (int ia = 0; ia < num; ia++) {
                    Object item = PrincipalGrafs.ComboBoxContadores.getItemAt(ia);
                    if (item.equals(NuevoContador.direccionFisica)) {
                        encontrado = 1;
                        break;
                    }
                }
                if (encontrado == 0) {
                    PrincipalGrafs.ComboBoxContadores.addItem(NuevoContador.direccionFisica);
                }
            }//end for
        }//end for

//			 inicializados =1;
//		}
    }

    //-----------------------------------------------------------------------------------
    public static void inicializarComboAnio(String contador) { //AñO

//		if(inicializados == 1){
        if (contador.equals("-Contadores-")) {

            int cuantos = PrincipalGrafs.ComboBoxAnio.getItemCount();
            if (cuantos != 1) {
                for (int i = 1; i < cuantos; i++) {
                    PrincipalGrafs.ComboBoxAnio.removeItemAt(i);
                }
            }
            PrincipalGrafs.ComboBoxMes.removeAllItems();
            PrincipalGrafs.ComboBoxMes.addItem("-Mes-");
//                                principalGrafs.ComboBoxMes.addItem("-Month-");
            PrincipalGrafs.ComboBoxDia.removeAllItems();
            PrincipalGrafs.ComboBoxDia.addItem("-Día-");
//                                principalGrafs.ComboBoxDia.addItem("-Day-");
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");
        } else {
            PrincipalGrafs.ComboBoxMes.removeAllItems();
            PrincipalGrafs.ComboBoxMes.addItem("-Mes-");
//                                                                principalGrafs.ComboBoxMes.addItem("-Month-");

            PrincipalGrafs.ComboBoxDia.removeAllItems();
            PrincipalGrafs.ComboBoxDia.addItem("-Día-");
//                                principalGrafs.ComboBoxDia.addItem("-Day-");
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");

            int cuantos = PrincipalGrafs.ComboBoxAnio.getItemCount();
            if (cuantos != 1) {
                for (int i = 1; i < cuantos; i++) {
                    PrincipalGrafs.ComboBoxAnio.removeItemAt(i);
                }
            }



            int i = 0;
            for (MiFormatoGrafs miFormato : Lecturas.list) {
//				miFormato.año   miFormato.dia   miFormato.mes   miFormato.fecha.getHours()
//				miFormato.fecha.getMinutes()
                int encontrado = 0;
                int num = PrincipalGrafs.ComboBoxAnio.getItemCount();

                for (int ia = 0; ia < num; ia++) {
                    Object item = PrincipalGrafs.ComboBoxAnio.getItemAt(ia);
                    if (item.equals(miFormato.año)) {
                        encontrado = 1;
                        break;
                    }
                }
                for (NuevoContador NuevoContador : miFormato.contadores) {
                    if ((encontrado == 0) && (contador.equals(NuevoContador.direccionFisica))) {
                        PrincipalGrafs.ComboBoxAnio.addItem(miFormato.año);
                    }
                }
            }//end for

//			 inicializados =1;
        }
//		}
    }

    //----------------------------------------
    public static void inicializarComboMes(Object año) {

        int i = 0;
        PrincipalGrafs.ComboBoxMes.removeAllItems();
        PrincipalGrafs.ComboBoxMes.addItem("-Mes-");
//                principalGrafs.ComboBoxMes.addItem("-Month-");
        PrincipalGrafs.ComboBoxDia.removeAllItems();
        PrincipalGrafs.ComboBoxDia.addItem("-Día-");
//                principalGrafs.ComboBoxDia.addItem("-Day-");
        PrincipalGrafs.ComboBoxHoraI.removeAllItems();
        PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
        PrincipalGrafs.ComboBoxHoraF.removeAllItems();
        PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");

        if (año.equals("-Año-")) {
//		if(año.equals("-Year-")){
        } else {
            int miAño = ((Integer) año).intValue();

            for (MiFormatoGrafs miFormato : Lecturas.list) {
                int encontrado = 0;
                int num = PrincipalGrafs.ComboBoxMes.getItemCount();
                for (int ia = 0; ia < num; ia++) {
                    Object item = PrincipalGrafs.ComboBoxMes.getItemAt(ia);
                    if (item.equals(miFormato.mes)) {
                        encontrado = 1;
                        break;
                    }

                }
                if ((encontrado == 0) && (miAño == miFormato.año)) {
                    PrincipalGrafs.ComboBoxMes.addItem(miFormato.mes);
                }

            }//end for

        }
    }

//---------------------------------------------------
    public static void inicializarComboDia(int anio, String mes) {

        PrincipalGrafs.ComboBoxDia.removeAllItems();

        if (mes.equals("-Mes-")) {
//            if(mes.equals("-Month-")){
            PrincipalGrafs.ComboBoxDia.removeAllItems();
            PrincipalGrafs.ComboBoxDia.addItem("-Día-");
//                principalGrafs.ComboBoxDia.addItem("-Day-");
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");
        } else {
            PrincipalGrafs.ComboBoxDia.addItem("-Día-");
//		principalGrafs.ComboBoxDia.addItem("-Day-");
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");

            String miMes = (String) mes;
            int encontrado = 0;
            for (MiFormatoGrafs miFormato : Lecturas.list) {
                encontrado = 0;
                int num = PrincipalGrafs.ComboBoxDia.getItemCount();
                for (int ia = 0; ia < num; ia++) {
                    Object item = PrincipalGrafs.ComboBoxDia.getItemAt(ia);
                    if (item.equals(miFormato.dia)) {
//						 System.out.println("encontro en el combobox:"+miFormato.dia);
                        encontrado = 1;
                        break;
                    }

                }
                if ((encontrado == 0) && (miMes.equals(miFormato.mes)) && (anio == miFormato.año)) {
                    PrincipalGrafs.ComboBoxDia.addItem(miFormato.dia);
//					System.out.println("añadir al combobox:"+miFormato.dia);
                }

            }//end for
        }

    }

//--------------------------------------------
    public static void inicializarComboHoraInicio(int añoSelec, String mesSelec, int diaSelec) {
        PrincipalGrafs.ComboBoxHoraI.removeAllItems();
        String miDia = Integer.toString(diaSelec);
        if (diaSelec == 0) {//if(miDia.equals("Día")){
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");
        } else {
            PrincipalGrafs.ComboBoxHoraI.removeAllItems();
            PrincipalGrafs.ComboBoxHoraI.addItem("-Hora Comienzo-");
//                principalGrafs.ComboBoxHoraI.addItem("-Start Hour-");
            PrincipalGrafs.ComboBoxHoraF.removeAllItems();
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");


            for (MiFormatoGrafs miFormato : Lecturas.list) {

                if ((mesSelec.equals(miFormato.mes))
                        && (añoSelec == miFormato.año) && (diaSelec == miFormato.dia)) {
                    int encontrado = 0;
                    int num = PrincipalGrafs.ComboBoxHoraI.getItemCount();
                    for (int ia = 0; ia < num; ia++) {
                        Object item = PrincipalGrafs.ComboBoxHoraI.getItemAt(ia);
                        if (ia != 0) {
                            String aux = (String) item;
                            int i = aux.indexOf(":");
                            aux = aux.substring(0, i);
                            if (Integer.parseInt(aux) == miFormato.fecha.getHours()) {
                                encontrado = 1;
                                break;
                            }
                        }
                    }//end for
//				 }//end if
                    if ((encontrado == 0)) {
                        String insertar = Integer.toString(miFormato.fecha.getHours()) + ":00";
                        PrincipalGrafs.ComboBoxHoraI.addItem(insertar);
                    }
                }
            }//end for

        }//end else
    }

//------------------------------------------------------------------------------------
    public static void inicializarComboHoraFinal(int añoSelec, String mesSelec, int diaSelec, String horaInicioSelec) {
        PrincipalGrafs.ComboBoxHoraF.removeAllItems();
        if (horaInicioSelec.equals("-Hora Comienzo-")) {
//            if(horaInicioSelec.equals("-Start Hour-")){
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");
        } else {
            PrincipalGrafs.ComboBoxHoraF.addItem("-Hora Final-");
//                principalGrafs.ComboBoxHoraF.addItem("-Final Hour-");
            int x = horaInicioSelec.indexOf(":");
            horaInicioSelec = horaInicioSelec.substring(0, x);
            int horai = Integer.parseInt(horaInicioSelec);
            int insertado = 0;
            for (MiFormatoGrafs miFormato : Lecturas.list) {
                if ((mesSelec.equals(miFormato.mes))
                        && (añoSelec == miFormato.año) && (diaSelec == miFormato.dia)) {

                    int encontrado = 0;
                    int num = PrincipalGrafs.ComboBoxHoraF.getItemCount();
                    for (int ia = 0; ia < num; ia++) {
                        Object item = PrincipalGrafs.ComboBoxHoraF.getItemAt(ia);
                        if (ia != 0) {
                            String aux = (String) item;
                            if (aux.equals("-Hora Final-")) {
//							if(aux.equals("-Final Hour-")){
                            } else {
                                int i = aux.indexOf(":");
                                aux = aux.substring(0, i);
                                if (Integer.parseInt(aux) == miFormato.fecha.getHours()) {
                                    encontrado = 1;
                                    break;
                                }
                            }
                        }
                    }//end for
//				 }//end if

                    if ((encontrado == 0) && (horai < miFormato.fecha.getHours())) {
                        String insertar = Integer.toString(miFormato.fecha.getHours()) + ":00";
                        PrincipalGrafs.ComboBoxHoraF.addItem(insertar);
                        insertado = 1;
                    }
                }
            }//end for
            if (insertado == 0) {
                PrincipalGrafs.ComboBoxHoraF.addItem("----");
            }

        }//end else
    }
//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
}//end class

