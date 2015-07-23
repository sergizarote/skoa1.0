
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MiFormatoGrafs {

    public int año;
    public String mes;
    public int dia;
    public Date fecha;
    public List<NuevoContador> contadores = new ArrayList<NuevoContador>();

    @SuppressWarnings("deprecation")
    public MiFormatoGrafs(int iaño, String imes, int idia, int hora, int minutos, String ristra) {
        this.año = iaño;
        this.mes = imes;
        this.dia = idia;
        this.fecha = new Date();
        this.fecha.setDate(idia);
        this.fecha.setYear(iaño);
//    	this.fecha.setMonth(imes);
        this.fecha.setHours(hora);
        this.fecha.setMinutes(minutos);


        if (imes.equals("Jan")) {
            this.fecha.setMonth(01);
        }
        if (imes.equals("Feb")) {
            this.fecha.setMonth(02);
        }
        if (imes.equals("Mar")) {
            this.fecha.setMonth(03);
        }
        if (imes.equals("Apr")) {
            this.fecha.setMonth(04);
        }
        if (imes.equals("May")) {
            this.fecha.setMonth(05);
        }
        if (imes.equals("Jun")) {
            this.fecha.setMonth(06);
        }
        if (imes.equals("Jul")) {
            this.fecha.setMonth(07);
        }
        if (imes.equals("Aug")) {
            this.fecha.setMonth(8);
        }
        if (imes.equals("Sep")) {
            this.fecha.setMonth(9);
        }
        if (imes.equals("Oct")) {
            this.fecha.setMonth(10);
        }
        if (imes.equals("Nov")) {
            this.fecha.setMonth(11);
        }
        if (imes.equals("Dec")) {
            this.fecha.setMonth(12);
        }



        InicializarContadores(ristra);
    }

    /**
     * 
     * @param ristra
     * @return 
     */
    public NuevoContador[] InicializarContadores(String ristra) {
        String direccion, medicion, medida = "";
        float valor;
        int aux, salir = 0;
        int no = 0;

        while (ristra.length() > 5) {
            if (no == 1) {
                no = 0;
            }

            salir = 0;
            //Coger direccion
            aux = ristra.indexOf(" ");
            direccion = ristra.substring(0, aux);
            ristra = ristra.substring(aux + 1);
//                        System.out.println("111111111111direccion:"+direccion);
//                        System.out.println("ristra:"+ristra);

            if (!(direccion.contains("/"))) {
                continue;
            }
            String trozo = "";
            if (ristra.length() < 6) {
                trozo = ristra;
            } else {
                aux = ristra.indexOf(" ");
                trozo = ristra.substring(0, aux);
//                            System.out.println("ristra1:"+ristra);
//                            System.out.println("trozo:"+trozo);
            }


            if (trozo.equals("Up") || trozo.equals("Down")) { // Up 1/1/12 Decrease 1 steps 1/1/15 True 3/0/01 False
                medida = "Up/Down";
                medicion = trozo;
                valor = 0;
                if (trozo.equals("Up")) {
                    valor = 50;
                }
                if (trozo.equals("Down")) {
                    valor = 0 - 50;
                }
                NuevoContador insertar = new NuevoContador(direccion, valor, medida);
                this.contadores.add(insertar);
                //Coger medicion contador
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                ristra = ristra.substring(aux + 1); //quito el up   // 1/1/12 Decrease 1 steps 1/1/15 True 3/0/01 False

//                            System.out.println("ristra up:"+ristra); // 1/1/12 Decrease 1 steps 1/1/15 True 3/0/01 False
                continue;
            }

            if (trozo.equals("break")) {// || ristra.equals("False") ){
                medida = "break";
                medicion = trozo;
                valor = 40;
//                             if(ristra.equals("True"))
//                                valor = 50;
//                             if(ristra.equals("False"))
//                                valor = 0-50;
                NuevoContador insertar = new NuevoContador(direccion, valor, medida);
                this.contadores.add(insertar);
            }

            if (trozo.equals("Decrease") || trozo.equals("Increase")) {// || ristra.equals("False") ){
                medida = "Decrease";
                medicion = trozo;
                valor = 0;
                if (trozo.equals("Increase")) {
                    valor = 50;
                }
                if (trozo.equals("Decrease")) {
                    valor = 0 - 50;
                }
                NuevoContador insertar = new NuevoContador(direccion, valor, medida);
                this.contadores.add(insertar);

                //Coger medicion contador
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                ristra = ristra.substring(aux + 1); //quito el decrease
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                ristra = ristra.substring(aux + 1); //quito el numero
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                ristra = ristra.substring(aux + 1); //quito el steps


//                            System.out.println("ristra decrease-"+ristra); //1/1/15 True 3/0/01 False
                continue;
            }



            if (trozo.equals("True") || trozo.equals("False")) {
//                            System.out.println("entro-");
                medida = "True/False";
                medicion = trozo;
                valor = 0;
                if (trozo.equals("True")) {
                    valor = 50;
                }
                if (trozo.equals("False")) {
                    valor = 0 - 50;
                }
                NuevoContador insertar = new NuevoContador(direccion, valor, medida);
                this.contadores.add(insertar);
                //Coger medicion contador
//                            System.out.println("ristra false/true-"+ristra);
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                ristra = ristra.substring(aux + 1); //le quita el true
//                            System.out.println("ristra false/true-"+ristra);
//                            if(ristra.length()<5)
//                                continue;

            } else {

                //Coger medicion contador
                aux = ristra.indexOf(" "); //ristra = No alarma direccion
                medicion = ristra.substring(0, aux);


                //no = valor=0 no llueve
                if ((medicion.contains("NO")) || (medicion.contains("no"))) {
                    ristra = ristra.substring(aux + 1); //ristra = alarma direccion
                    aux = ristra.indexOf(" ");
                    ristra = ristra.substring(aux + 1); //ristra = direccion
                    medida = "Lluvia/No lluvia";
                    medicion = ristra.substring(0, aux);
                    salir = 2;
                    no++;
                }

                if (((medicion.contains("alarma")) || (medicion.contains("ALARMA"))) && (no == 0)) {
                    ristra = ristra.substring(aux + 1);
                    aux = ristra.indexOf(" ");

                    medida = "Lluvia/No lluvia";
                    medicion = ristra.substring(0, aux);
                    salir = 1;
                }

                if (salir == 1) { //si ha encontrado una alarma
                    valor = 0 - 50;
                    NuevoContador insertar = new NuevoContador(direccion, valor, medida);

                    this.contadores.add(insertar);


                } else {  //si no ha encontrado alarma
                    if (salir == 2) {
                        valor = 0;
                        NuevoContador insertar = new NuevoContador(direccion, valor, medida);

                        this.contadores.add(insertar);
                    } else {
                        aux = medicion.indexOf(".");

                        if (aux > 0) {
                            valor = (float) Integer.parseInt(medicion.substring(0, aux));
                        } else {
                            valor = (float) Integer.parseInt(medicion);
                        }

                        aux = ristra.indexOf(" ");
                        ristra = ristra.substring(aux + 1); //ristra = medida contador medicion
                        aux = ristra.indexOf(" ");

                        if (aux == -1) {
                            medida = ristra;
                        } else {
                            medida = ristra.substring(0, aux);
                            ristra = ristra.substring(aux + 1);
                        }
                        NuevoContador insertar = new NuevoContador(direccion, valor, medida);
                        this.contadores.add(insertar);


                    }
                }//end else
            }//end else
        }//end while

        return null;

    }
	
}
