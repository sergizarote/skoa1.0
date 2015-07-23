
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
import javax.swing.JScrollPane;

import skoa.views.PrincipalGrafs;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;



public class GeneraGraficoSkoa {

    static int insertados = 0;
    static float ultimo = 0;
    static String horas = "", minutos = "";

    @SuppressWarnings("deprecation")
    public static void GenerarGrafica(String contador, int anio, String mes, int dia, String horai, String horaf) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart1;
        JFreeChart chart2;
        int horainicio;
        int horafin;
        String medida = "";
        int dp = horai.indexOf(":");
        horainicio = Integer.parseInt(horai.substring(0, dp));
        if (!horaf.equals("----")) {
            dp = horaf.indexOf(":");
            horafin = Integer.parseInt(horaf.substring(0, dp));
        } else {
            horafin = 99;
        }

        insertados = 0;

        for (MiFormatoGrafs miFormato : Lecturas.list) {
            for (NuevoContador NuevoContador : miFormato.contadores) {
                if (NuevoContador.direccionFisica.equalsIgnoreCase(contador)) {
                    if ((miFormato.año == anio) && (miFormato.mes.equals(mes)) && (miFormato.dia == dia)) {
                        if ((miFormato.fecha.getHours() >= horainicio) && (miFormato.fecha.getHours() < horafin)) {
                            String hora = miFormato.fecha.toString();
                            dp = hora.indexOf(":");
                            hora = hora.substring(dp - 2, dp + 3);
                            horas = hora.substring(0, 2);
                            minutos = hora.substring(3);
                            //Primera inserción en la gráfica?
                            if (insertados == 0) {
                                dataset.addValue(NuevoContador.medicion - (NuevoContador.medicion * 10 / 100), "medicion", "--");
                                insertados++;
                            }//fin caso primera insercion
                            dataset.addValue(NuevoContador.medicion, "medicion", hora);
                            ultimo = NuevoContador.medicion;
                            medida = NuevoContador.medida;
                        }

                    }//end if buscamos valores seleccionados en el combobox
                }//end if
            }//end for2
        }//end for1

        //++++++ ULTIMO AÑADIDO A LA GRAFICA

        dataset.addValue(ultimo + (ultimo * 10 / 100), "medicion", "---");
       

        if (horaf.equals("----")) {
            chart1 = ChartFactory.createLineChart("Valores Medidos por el Contador " + contador + "   (Fecha: " + dia + "/" + mes + "/" + anio + ")", "Intervalo de Horas Medido (" + horai + " - Fin Mediciones)", "Mediciones    ( " + medida + " )", dataset, PlotOrientation.VERTICAL, false, false, false);
//        	

        } else {
            chart1 = ChartFactory.createLineChart("Valores Medidos por el Contador " + contador + "   (Fecha: " + dia + "/" + mes + "/" + anio + ")", "Intervalo de Horas Medido (" + horai + " - " + horaf + ")", "Mediciones   ( " + medida + " )", dataset, PlotOrientation.VERTICAL, false, false, false);
//			chart1 = ChartFactory.createLineChart("Values of the Counter "+contador+"   (Date: "+dia+"/"+mes+"/"+anio+")", "Hours Interval Taken ("+horai+" - "+horaf+")", "Values   ( "+medida+" )", dataset, PlotOrientation.VERTICAL, false, false, false);

            //			chart2 = ChartFactory.createBarChart("Valores Medidos por el Contador "+contador, "Intervalo de Horas Medido ("+horai+" - "+horaf+")", "Mediciones", dataset, PlotOrientation.VERTICAL, false, false, false);

        }


        PrincipalGrafs.jPanel2.removeAll();

        JScrollPane jsp = new JScrollPane();
        jsp.setBounds(30, 30, 700, 900); // le doy tamaño

        jsp.setViewportView(new ChartPanel(chart1));
        PrincipalGrafs.jPanel2.add(jsp);
        PrincipalGrafs.jPanel2.updateUI();
        jsp.setVisible(true);


    }//end GenerarGrafica
}//end class

