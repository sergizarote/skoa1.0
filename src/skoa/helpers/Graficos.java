/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.helpers;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;

public class Graficos extends Thread{
    String nombreFichero, ruta;
    String unidad,fechaInicial="",fechaFinal="";
    Vector<String> nombresFicheros=new Vector<String>(),nombresDGs=new Vector<String>();
    int ui;
    String ruta_c; //Se inicializa en el constructor para almacenar la ruta donde se encuentran los resultados de las consultas.
    float max, min, num, sum;
    TimeSeries sMax,sMin,sMed;
    String rangoMinutos;
    Vector<String> consultasDuales;

    //En consultasDuales, cada posicion se corresponde con una consulta, en el mismo orden en que se hizo
    //indicando si no está vacía esa posicion (""), si se aplica un eje Dual a la consulta.
    //public graficos(String ruta_consultas, Vector<String> consultasDuales){
    public Graficos(String ruta_consultas, Vector<String> consultas){
        ruta_c=ruta_consultas;
        consultasDuales=consultas;
    }

    public void run(){
        String os=System.getProperty("os.name");
        //En Windows las barras son \ y en Linux /.
        String barra;
        if (os.indexOf("Win")>=0) barra="\\";
        else barra="/";
        File dir = new File(ruta_c);
        String[] ficheros = dir.list();
        for (int i=0;i<ficheros.length;i++) {
            System.out.println("DIR:"+ficheros[i]);
            ruta=ruta_c+barra+ficheros[i]+barra;
            System.out.println("   R:"+ruta_c+barra+ficheros[i]);
            File dir2 = new File(ruta_c+barra+ficheros[i]);
            String[] ficheros2 = dir2.list();
            unidad=fechaInicial=fechaFinal="";	//Reinicializa para la siguiente consulta
            System.out.println("   L:"+ficheros2.length);
            for (int j=0;j<ficheros2.length;j++) {
                System.out.println("   f:"+ficheros2[j]);
                nombresFicheros.add(ficheros2[j]);
            }
            if (nombresFicheros.size()>0){
                String aux=nombresFicheros.elementAt(0);
                if (aux.charAt(0)=='A') {	//Consulta A
                    if (consultasDuales.elementAt(i).equals("dual")) evolucionDual();
                    else evolucion();
                }
                else if (aux.charAt(0)=='B') {	//Consulta B
                    if (consultasDuales.elementAt(i).equals("dual")) barrasDual2();
                    else barras();
                }
                else if (aux.charAt(0)=='C') maxMinMed();	//Consulta C
                else if (aux.charAt(0)=='D') {			//Consulta D
                    String a;
                    if (aux.indexOf("T")!=-1) a=aux.substring(aux.indexOf("T")+2);
                    else a=aux.substring(aux.indexOf("I")+2);
                    a=a.substring(0, a.indexOf("m"));
                    System.out.println(a+".");
                    rangoMinutos=a;
                    if (aux.charAt(1)=='p')	difPorc();	//   por porcentajes
                    else difAbs();				//   normal
                }
                nombresFicheros.removeAllElements();
                nombresDGs.removeAllElements();
            }
        }
    }

    //Comprueba si el valor traducido es un booleano, y si lo es cúal.
    //Los booleanos son todos los proporcionados por PointPDUXlator_Boolean.java
    //El 2º parámetro nos indica si buscamos positivos o negativos.
    private boolean posiblesBooleanos(String v, int i){
        if (i==0){ //Booleanos negativos.
            if (v.indexOf("NO alarma")>=0 || v.indexOf("False")>=0 || v.indexOf("Down")>=0 ||
                v.indexOf("Off")>=0 || v.indexOf("Disable")>=0 || v.indexOf("No ramp")>=0 ||
                v.indexOf("Low")>=0 || v.indexOf("Decrease")>=0 || v.indexOf("Close")>=0 ||
                v.indexOf("Stop")>=0 || v.indexOf("Inactive")>=0 || v.indexOf("Fixed")>=0 ||
                v.indexOf("Not inverted")>=0 || v.indexOf("Start/Stop")>=0) return true;
        }
        else {      //Booleanos positivos.
            if (v.indexOf("ALARMA")>=0 || v.indexOf("True")>=0 || v.indexOf("Up")>=0 ||
                v.indexOf("On")>=0 || v.indexOf("Enable")>=0 || v.indexOf("Ramp")>=0 ||
                v.indexOf("High")>=0 || v.indexOf("Increase")>=0 || v.indexOf("Open")>=0 ||
                v.indexOf("Start")>=0 || v.indexOf("Active")>=0 || v.indexOf("Calculated")>=0 ||
                v.indexOf("Inverted")>=0 || v.indexOf("Cyclicalli")>=0) return true;
        }
        return false;
    }

    private void evolucionDual() {
        TimeSeries serie;
        XYDataset dataset=null;
        ui=0;
        nombreFichero=nombresFicheros.elementAt(0);
        unidad=buscarUnidad(nombreFichero);
        serie=obtenerSerieEvolucion();
        dataset=new TimeSeriesCollection(serie);
        //Para generar el gráfico se usa createTimeSeriesChart para ver la evolución de las fechas.
        JFreeChart grafica = ChartFactory.createTimeSeriesChart("Valores medidos de las direcciones de grupo",//titulo
                                                                "Fechas", 			//titulo eje x
                                                                "Mediciones ("+unidad+")",	//titulo eje y
                                                                dataset,			//dataset
                                                                true, 			//leyenda
                                                                true, 			//tooltips
                                                                false);			//configure chart to generate URLs?
        //Dar color a cada categoria
        grafica.setBackgroundPaint(Color.WHITE);	//Color del fondo del gráfico
        //CREACIóN DEL SEGUNDO EJE CON SU CORRESPONDIENTE DATASET.
        XYDataset dataset2=null;
        ui=0;
        nombreFichero=nombresFicheros.elementAt(1);
        unidad=buscarUnidad(nombreFichero);
        final XYPlot plot = grafica.getXYPlot();
        final NumberAxis axis2 = new NumberAxis("Mediciones ("+unidad+")");
        axis2.setAutoRangeIncludesZero(false);
        plot.setRangeAxis(1, axis2);
        serie=obtenerSerieEvolucion();
        dataset2=new TimeSeriesCollection(serie);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setSeriesPaint(0, Color.blue);
        plot.setRenderer(1, renderer2);
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));//igual color de fondo que el de barrasDual
        plot.setDomainGridlinesVisible(true);	//Ver lineas divisorias.
        plot.setRangeGridlinePaint(Color.BLACK);//Color de las lineas divisorias.
        //---------------------------------------------------------------------
        if (fechaInicial.equals("") & fechaFinal.equals("")){ //Si están vacías es porque no hay resultados para ese intervalo.
            fechaInicial=" ? ";
            fechaFinal=" ? ";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"EvolucionSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"EvolucionBig.jpg"), grafica, 900, 600);
        } catch (IOException e1) {
            System.err.println("Problem occurred creating chart."+e1);
        }
    }

    /****************************************************************************************
     * Funcion evolucion(): dados los ficheros a leer, obtiene la serie de cada uno de ellos*
     * y la añade a la gráfica en cuestion.                                                 *
     ****************************************************************************************/
    private void evolucion() {
        TimeSeries serie;
        TimeSeriesCollection dataset=null;
        for(int i=0;i<nombresFicheros.size();i++)  {
            ui=0;
            nombreFichero=nombresFicheros.elementAt(i);
            unidad=unidad+","+buscarUnidad(nombreFichero);
            serie=obtenerSerieEvolucion();
            if (i==0) dataset=new TimeSeriesCollection(serie);
            else dataset.addSeries(serie);
        }
        unidad=unidad.substring(1); //Para quitar la coma del principio, introducida por la primera unidad.
        //Para generar el gráfico se usa createTimeSeriesChart para ver la evolución de las fechas.
        JFreeChart grafica = ChartFactory.createTimeSeriesChart("Valores medidos de las direcciones de grupo",//titulo
                                                                "Fechas", 		//titulo eje x
                                                                "Mediciones ("+unidad+")",//titulo eje y
                                                                dataset,		//dataset
                                                                true, 			//leyenda
                                                                true, 			//tooltips
                                                                false);			//configure chart to generate URLs?
        //Dar color a cada categoria
        grafica.setBackgroundPaint(Color.WHITE);	//Color del fondo del gráfico
        if (fechaInicial.equals("") & fechaFinal.equals("")){ //Si están vacías es porque no hay resultados para ese intervalo.
            fechaInicial=" ? ";
            fechaFinal=" ? ";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"EvolucionSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"EvolucionBig.jpg"), grafica, 900, 600);
        } catch (IOException e1) {
            System.err.println("Problem occurred creating chart."+e1);
        }
    }

    /******************************************************************
     * Funcion obtenerSerieEvolucion(): obtiene la serie de un fichero*
     ******************************************************************/
    private TimeSeries obtenerSerieEvolucion() {
        String naux="";
        //si la longitud es 20 o 21, se trata de un fichero directo de una consulta A.
        //porque la df puede ser x.x.x o x.x.xx
        if (nombreFichero.length()==20||nombreFichero.length()==21) naux=nombreFichero.substring(nombreFichero.indexOf("-")+3,nombreFichero.indexOf(".txt")); //Saca la DG del nombre del fich.
        //si la longitud es 22, se trata de un fichero de una consulta A previa.
        else if (nombreFichero.length()==22)naux=nombreFichero.substring(nombreFichero.indexOf("-")+5,nombreFichero.indexOf(".txt"));
        //si la longitud es 23, se trata de un fichero (sin unificar) de una consulta D previa.
        else if (nombreFichero.length()==23)naux=nombreFichero.substring(nombreFichero.indexOf("-")+6,nombreFichero.indexOf(".txt"));
        //si se trata de un fichero de una consulta B o C previa.
        else if (nombreFichero.indexOf("h")>=0) naux=nombreFichero.substring(nombreFichero.indexOf("h")+2,nombreFichero.indexOf(".txt"));
        naux=naux.replaceAll("-", "/"); //Para que las DGs sea x/xx/xx en vez de x-xx-xx
        TimeSeries serie = new TimeSeries(naux);
        File archivo = new File(ruta+nombreFichero);
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            int d=0,m=0,a=0,a1=0,m1=0,d1=0,j,h1,h2,h3;
            double e=0;
            String aux,h,minutos,segundos;
            int min_ant=0,sec_ant=0,vez1=0;			//min_prim mira si es el primero, para comparar ant y act.
            Day day1=null;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                int i=line.indexOf("\t");
                String f=line.substring(0,i);
                String valor=line.substring(i+1);
            //Obtencion del dia, mes y año de la fecha.
                j=f.indexOf("-");
                aux=f.substring(0,j);
                a=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf("-");
                aux=f.substring(0,j);
                m=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf(" ");
                aux=f.substring(0,j);
                d=Integer.parseInt(aux);
            //Obtencion de la hora de la fecha.
                f=f.substring(j+1);
                if (fechaInicial.contentEquals("")) fechaInicial=d+"/"+m+"/"+a+" "+f; //Variable para la gráfica
                fechaFinal=d+"/"+m+"/"+a+" "+f;
                j=f.indexOf(":");
                h=f.substring(0,j);
                f=f.substring(j+1);
                j=f.indexOf(":");
                minutos=f.substring(0,j);
                segundos=f.substring(j+1);
                if(a1==0 & m1==0 & d1==0){	//Inicialización: Primera fecha.
                    a1=a;
                    m1=m;
                    d1=d;
                    day1=new Day(d1,m1,a1);
                }
                else{
                    if (a1!=a){
                        a1=a;
                        if (m1!=m) m1=m;
                        if (d1!=d) d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                    else if (m1!=m){
                        m1=m;
                        if (d1!=d) d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                    else if (d1!=d){
                        d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                }
                //Comprueba si es boolean. Si lo es, se le asigna 0 o 1
                //para poder representarlo en la gráfica. Si no, su <<valor>>.
                if (posiblesBooleanos(valor, 1)) e=1;
                else if (posiblesBooleanos(valor, 0)) e=0;
                else{   //NO ES UN BOOLEANO.
                    int u=valor.indexOf(" ");
                    valor=valor.substring(0, u);
                    e=Double.parseDouble(valor);
                }
                //Comprobamos que la hora no coincida, para que si coincide, introducir en la serie sólo
                //la primera aparición de la fecha con su valor, por ser este más representativo según lo visto.
                if (vez1==0){
                    min_ant=h1=Integer.parseInt(minutos);	//minutos
                    h2=Integer.parseInt(h);					//hora
                    sec_ant=h3=Integer.parseInt(segundos);	//segundos
                    serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                    vez1=1;
                }
                else{
                    h1=Integer.parseInt(minutos);	//minutos
                    h2=Integer.parseInt(h);		//hora
                    h3=Integer.parseInt(segundos);	//segundos
                    if (min_ant==h1){			//Si el minuto es =, comprobamos los segundos
                        if (sec_ant==h3){}		//Si los segundos son =, no se introduce nada en la serie.
                        else{				//Si los segundos son !=, se introduce en la serie.
                            serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                            sec_ant=h3;
                        }
                    }
                    else{				//Si el minuto es !=, se introduce en la serie.
                        serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                        min_ant=h1;
                        sec_ant=h3;
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
            return serie;
    }

    private void barras() {
        unificarDatosFicheros();
        Vector<String> vectorOrdenUnidades=new Vector<String>();
        vectorOrdenUnidades=ordenDeUnidades();
        aplicarDiferencia(vectorOrdenUnidades);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset=obtenerSerieBarras(vectorOrdenUnidades);
        String unidad="";
        //for (int i=0;i<vectorOrdenUnidades.size();i++) unidad=unidad+vectorOrdenUnidades.elementAt(i)+", ";
        for (int i=0;i<vectorOrdenUnidades.size();i++) {
            if (vectorOrdenUnidades.elementAt(i).indexOf("W")>=0 || vectorOrdenUnidades.elementAt(i).indexOf("L")>=0 || 
                vectorOrdenUnidades.elementAt(i).indexOf("m")>=0 || 
                vectorOrdenUnidades.elementAt(i).indexOf("B")>=0) unidad = unidad + vectorOrdenUnidades.elementAt(i) + ", ";
            else if (vectorOrdenUnidades.elementAt(i).indexOf("C")>=0) unidad = unidad + "ºC, ";
            else unidad = unidad + "º, ";
        }
        unidad=unidad.substring(0, unidad.length()-2); //Quita el ultimo espacio y la ultima coma.

        JFreeChart grafica = ChartFactory.createBarChart("Valores medidos de las direcciones de grupo",
                                                       "Fechas", 		//titulo eje x
                                                       "Mediciones ("+unidad+")",
                                                       dataset,
                                                       PlotOrientation.VERTICAL,
                                                       true,			//leyenda
                                                       true,
                                                       false);
        if (fechaInicial.isEmpty()){
            fechaInicial=fechaFinal="?";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        CategoryPlot plot = grafica.getCategoryPlot();
        //Modificar eje X
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6.0));
        domainAxis.setTickLabelFont(new Font("Dialog",Font.PLAIN,8)); //Letra de las fechas más pequeña
        //Esconder las sombras de las barras del barchart.
        CategoryPlot categoryPlot = (CategoryPlot) grafica.getPlot();
        BarRenderer renderer = new BarRenderer();
        renderer.setShadowVisible(false);
        categoryPlot.setRenderer(renderer);
        //-------------------------------------------------
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"BarrasSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"BarrasBig.jpg"), grafica, 900, 600);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }

    private void barrasDual2() {
        unificarDatosFicheros();
        Vector<String> vectorOrdenUnidades=new Vector<String>();
        vectorOrdenUnidades=ordenDeUnidades();
        aplicarDiferencia(vectorOrdenUnidades);
        //En este caso, que queremos 2 ejes, MARCAMOS LA DIFERENCIA AL RECOGER EL 2 DATASET.
        CategoryDataset dataset=obtenerSerieBarrasDual(1);
        //String unidad=vectorOrdenUnidades.elementAt(0);
        String unidad;
       if (vectorOrdenUnidades.elementAt(0).indexOf("W")>=0 || vectorOrdenUnidades.elementAt(0).indexOf("L")>=0 ||
           vectorOrdenUnidades.elementAt(0).indexOf("m")>=0 ||
           vectorOrdenUnidades.elementAt(0).indexOf("B")>=0) unidad = vectorOrdenUnidades.elementAt(0);
        else if (vectorOrdenUnidades.elementAt(0).indexOf("C")>=0) unidad = "ºC";
        else unidad = "º";
        final CategoryAxis domainAxis = new CategoryAxis("Fechas");
        final NumberAxis rangeAxis = new NumberAxis("Mediciones ("+unidad+")");
        final BarRenderer renderer1 = new BarRenderer();
        final CategoryPlot plot = new CategoryPlot(dataset, domainAxis, rangeAxis, renderer1) {
            private static final long serialVersionUID = 1L;
            //ESPECIAL. Modificamos la leyenda, para que se vea correcta, cogiendo los 2 items deseados.
            public LegendItemCollection getLegendItems() {
                final LegendItemCollection result = new LegendItemCollection();
                final CategoryDataset data = getDataset();
                if (data != null) {
                    final CategoryItemRenderer r = getRenderer();
                    if (r != null) {
                        final LegendItem item;
                        try{                                //Se recoge la excepcion en caso de haber solo
                            item = r.getLegendItem(0, 0);   //una línea en el fichero unificado, porque
                        }catch(Exception e){                //no habria nada en diferenciaAplicada.
                            System.out.println("MAL "+e);
                            return null;
                        }
                        result.add(item);
                    }
                }
                final CategoryDataset dset2 = getDataset(1);
                if (dset2 != null) {
                    final CategoryItemRenderer renderer2 = getRenderer(1);
                    if (renderer2 != null) {
                        final LegendItem item = renderer2.getLegendItem(1, 1);
                        result.add(item);
                    }
                }
                return result;
            }
        };
        final JFreeChart grafica = new JFreeChart("Valores medidos de las direcciones de grupo", plot);
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        CategoryDataset dataset2=obtenerSerieBarrasDual(2);
        //unidad=vectorOrdenUnidades.elementAt(1);
       if (vectorOrdenUnidades.elementAt(1).indexOf("W")>=0 || vectorOrdenUnidades.elementAt(1).indexOf("L")>=0 ||
           vectorOrdenUnidades.elementAt(1).indexOf("m")>=0 ||
           vectorOrdenUnidades.elementAt(1).indexOf("B")>=0) unidad = vectorOrdenUnidades.elementAt(1);
        else if (vectorOrdenUnidades.elementAt(1).indexOf("C")>=0) unidad = "ºC";
        else unidad = "º";
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        final ValueAxis axis2 = new NumberAxis("Mediciones ("+unidad+")");
        plot.setRangeAxis(1, axis2);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        final BarRenderer renderer2 = new BarRenderer();
        renderer2.setShadowVisible(false); //Esconder las sombras de la barra 1.
        plot.setRenderer(1, renderer2);
        BarRenderer renderer = new BarRenderer();
        renderer.setShadowVisible(false);  //Esconder las sombras de la barra 2.
        plot.setRenderer(0,renderer);
        plot.setRangeGridlinePaint(Color.BLACK);	//Color de las lineas divisorias.
        //Subtítulos
        if (fechaInicial.isEmpty()) {
            fechaInicial= fechaFinal="?";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6.0));
        domainAxis.setTickLabelFont(new Font("Dialog",Font.PLAIN,8)); //Letra de las fechas más pequeña
        grafica.setBackgroundPaint(Color.white);
        //-------------------------------------------------
        //Guarda la imagen:
        BufferedImage i1=grafica.createBufferedImage(400, 300);
        BufferedImage i2=grafica.createBufferedImage(900, 600);
        BufferedImage imag1=convertirTipo(i1, BufferedImage.TYPE_INT_RGB);//!OBLIGATORIO!Para que al guardar la imagen,
        BufferedImage imag2=convertirTipo(i2, BufferedImage.TYPE_INT_RGB);//no se vea transparente naranja.
        try {
           ImageIO.write(imag1, "jpg", new File(ruta+"BarrasSmall.jpg"));
           ImageIO.write(imag2, "jpg", new File(ruta+"BarrasBig.jpg"));
        } catch (IOException e) {
           System.out.println("Error de escritura");
        }
    }

    //Funcion para que al guardar la imagen no se vea anaranjada la transparencia.
    private BufferedImage convertirTipo(BufferedImage i1, int tipo) {
        BufferedImage res=new BufferedImage(i1.getWidth(),i1.getHeight(),tipo);
        Graphics2D g = res.createGraphics();
        g.drawRenderedImage(i1, null);
        g.dispose();
        return res;
    }

    private void aplicarDiferencia(Vector<String> v) {
        File archivo = new File(ruta+"unificado.txt");
        FileReader fr = null;
        BufferedReader linea = null;
        String L1,L2,v1="",v2="",aux1,aux2,line="";
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr);
            L1=linea.readLine();					//Lee la primera linea
            BufferedWriter bw=new BufferedWriter(new FileWriter(ruta+"diferenciaAplicada.txt",true)); //true, para guardar al final del fichero
            while((L2=linea.readLine())!=null){ 	//y despues la segunda, y hace la diferencia
                line=L2.substring(0, L2.indexOf("\t"))+"\t";
                aux1=L1.substring(L1.indexOf("\t")+1);//Quito la fecha
                aux2=L2.substring(L2.indexOf("\t")+1);
                Boolean coger1=true, ultimo1=false, coger2=true, ultimo2=false;
                for (int l=0;l<v.size();l++) {
                    if (coger1 & !ultimo1){			//Cojo el valor de la primera linea
                        int p1=aux1.indexOf("\t");
                        if (p1!=(-1)){
                            v1=aux1.substring(0,p1);
                            aux1=aux1.substring(p1+1);
                        }
                        else {	//Ultimo valor.
                            v1=aux1;
                            aux1="";
                            ultimo1=true;
                        }
                    }
                    if (coger2 & !ultimo2){			//Cojo el valor de la segunda linea
                        int p1=aux2.indexOf("\t");
                        if (p1!=(-1)){
                            v2=aux2.substring(0,p1);
                            aux2=aux2.substring(p1+1);
                        }
                        else {	//Ultimo valor.
                            v2=aux2;
                            aux2="";
                            ultimo2=true;
                        }
                    }
                    String alm1=v1,alm2=v2;
                    String u=v.elementAt(l);				//Unidad a comparar
                    u=u.substring(u.length()-1);			//Ultimo caracter de la unidad a comparar
                    String u1=v1.substring(v1.length()-1);	//Ultimo caracter de la unidad de v1 a comparar
                    String u2=v2.substring(v2.length()-1);	//Ultimo caracter de la unidad de v1 a comparar
                    double x1=0,x2=0;
                    //Tener en cuenta que el caracter � est� escrito distinto (no se xq) por lo que hay que tenerlo en cuenta.
                    //Se distinguen 4 posibles casos: v1-v2, v1-0, 0-v2 y 0-0
                    if(u.compareTo(u1)==0 | (u.hashCode()==186 & u1.hashCode()==176) | (u.compareTo("a")==0 & (u1.compareTo("a")==0 | u1.compareTo("A")==0))){
                        if (posiblesBooleanos(v1,0)) x1=0;      //Comprueba si es booleano negativo
                        else if (posiblesBooleanos(v1,1)) x1=1; //Comprueba si es booleano positivo
                        else{
                            v1=v1.substring(0, v1.indexOf(" "));
                            x1=Double.parseDouble(v1);
                        }
                        if(u.compareTo(u2)==0 | (u.hashCode()==186 & u2.hashCode()==176) | (u.compareTo("a")==0 & (u2.compareTo("a")==0 | u2.compareTo("A")==0))){	//DIFERENCIA v1-v2
                            if (posiblesBooleanos(v2,0)) x2=0;      //Comprueba si es booleano negativo
                            else if (posiblesBooleanos(v2,1)) x2=1; //Comprueba si es booleano positivo
                            else{
                                    v2=v2.substring(0, v2.indexOf(" "));
                                    x2=Double.parseDouble(v2);
                            }
                            x1=Math.abs(x1-x2);	//DIFERENCIA v1- v2
                            coger1=coger2=true;
                        }
                        else{					//DIFERENCIA v1-0
                            x1=Math.abs(x1-0);
                            coger1=true;
                            coger2=false;
                        }
                        if (posiblesBooleanos(v.elementAt(l),0) || posiblesBooleanos(v.elementAt(l),0)) line=line+x1+" B"+"\t"; //No pongo la unidad
                        else line=line+x1+" "+v.elementAt(l)+"\t";
                    }
                    else{	//DIFERENCIA 0-v2
                        if(u.compareTo(u2)==0 | (u.hashCode()==186 & u2.hashCode()==176) | (u.compareTo("a")==0 & (u2.compareTo("a")==0 | u2.compareTo("A")==0))){
                            if (posiblesBooleanos(v2,0)) x2=0;      //Comprueba si es booleano negativo
                            else if (posiblesBooleanos(v2,1)) x2=1; //Comprueba si es booleano positivo
                            else{
                                v2=v2.substring(0, v2.indexOf(" "));
                                x2=Double.parseDouble(v2);
                            }
                            x2=Math.abs(0-x2);
                            coger1=false;
                            coger2=true;
                        }
                        else{coger1=coger2=true;}	//DIFERENCIA 0-0
                        if (posiblesBooleanos(v.elementAt(l),0) || posiblesBooleanos(v.elementAt(l),0)) line=line+x1+" B"+"\t"; //No pongo la unidad
                        else line=line+x2+" "+v.elementAt(l)+"\t";
                    }
                    v1=alm1;
                    v2=alm2;
                }
                bw.write(line+"\n");
                L1=L2;
            }
            bw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null!=fr) fr.close();   	//Se cierra si todo va bien.
            }catch (Exception e2){ 			//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
    }

    /**Segun la DG, se obtiene la unidad, colocada según el orden de lectura de los ficheros**/
    private Vector<String> ordenDeUnidades() {
        Vector<String> v=new Vector<String>();
        String n="";
        for (int i=0;i<nombresFicheros.size();i++){
            n=nombresFicheros.elementAt(i);
            String u=buscarUnidad(n);
            if (n.indexOf("D")>0 && n.indexOf(" ")<0){  //Si es el resultado final de una consulta D.
               n=n.substring(n.indexOf(".txt")-13,n.indexOf(".txt")); //Saca la DG del nombre del fich.
            }
            else{   //Si son resultados de una consulta D.
                //Así, buscando a partir de .txt da igual el tipo de consulta que sea, que se saca igual.
                n=n.substring(n.indexOf(" ")-6,n.indexOf(".txt")); //Saca la DG del nombre del fich.
            }
            n=n.replaceAll("-", "/"); //Para que las DGs sea x/xx/xx en vez de x-xx-xx
            nombresDGs.add(n);
            v.add(u);
        }
        return v;
    }

    /**********************************************************************************************************
     * Busca la unidad, leyendo la 1º línea del fichero.                                                      *
     * @param nombre, no usa la variable global nombreFichero xq funciona para evolucion() y no para barras().*
     * @return devuelve la unidad, y si el fichero est� vac�o null.                                           *
     **********************************************************************************************************/
    private String buscarUnidad(String nombre) {
        File archivo = new File(ruta+nombre);
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); //Se crea para leer las lineas
            line=linea.readLine(); 			//Leo la primera linea del finchero.
            if (line!=null){				//Si no es nula, busco la unidad.
                line=line.substring(line.indexOf("\t")+1);
                if (posiblesBooleanos(line,0) || posiblesBooleanos(line,1)) return "Bool";
                else {
                    line=line.substring(line.indexOf(" ")+1);
                    if (line.indexOf("C")>=0)line="ºC";
                    return line;
                }
            }
        }catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
             }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
        return null;
    }

    private void unificarDatosFicheros() {
        Vector<String> nombresFicheros2=new Vector<String>(nombresFicheros);	//Copiamos los nombres
        String nombreFicheroEscogido;
        String nombreFicheroReferencia=nombresFicheros2.get(0); //Se coge el 1º fichero de referencia, al que se le ir�n a�adiendo los datos.
        nombresFicheros2.remove(0);
        crearFicheroTemporal(nombreFicheroReferencia);
        for(int i=0;i<nombresFicheros2.size();i++)  {
            nombreFicheroEscogido=nombresFicheros2.elementAt(i);
            File archivo = new File(ruta+nombreFicheroEscogido);
            FileReader fr = null;
            BufferedReader linea = null;
            String line;
            try {
                fr = new FileReader (archivo);
                linea = new BufferedReader(fr);
                while((line=linea.readLine())!=null){ 	//Lectura del fichero
                    incluirDatos(line);
                }
            }
            catch(Exception e){
                 e.printStackTrace();
            }finally{
                 try{
                    if (null != fr) fr.close();   	//Se cierra si todo va bien.
                 }catch (Exception e2){ 		//Sino salta una excepcion.
                    e2.printStackTrace();
                 }
            }
        }//fin for
    }

    /**Crea un fichero temporal identico al fichero referencia.**/
    private void crearFicheroTemporal(String nombreFicheroReferencia) {
        File origen = new File(ruta+nombreFicheroReferencia);
        File destino = new File(ruta+"unificado.txt");
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
    }

    /**Funcion que pasa fecha-valor de la linea a introducir en el fichero de referencia**/
    private void incluirDatos(String datos) {
        String fecha="", valor="";
        int i=datos.indexOf("\t");
        fecha=datos.substring(0, i);
        valor=datos.substring(i+1);
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = null;
        try {
            d = formatoDelTexto.parse(fecha);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        File archivo = new File(ruta+"unificado.txt");
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        String ficheroTemporal=ruta+"diferenciaAplicada.txt";
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(ficheroTemporal,true)); //true, para guardar al final del fichero
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            String L;
            Boolean salir=false;
            int r=0;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                L="";
                if (salir) L=line;//break;
                else{
                    String fechaR="";
                    int j=line.indexOf("\t");
                    fechaR=line.substring(0, j);
                    Date dR=null;
                    dR=formatoDelTexto.parse(fechaR);
                    r=d.compareTo(dR);
                    if (r==0) {
                            L=line+"\t"+valor;	//fechas iguales, a�ade el valor del otro fichero.
                            salir=true;
                    }
                    else if(r<0) {
                            L=fecha+"\t"+valor+"\n"+line;
                            salir=true;
                    }
                    else if(r>0) L=line; //si la fecha es mayor, se trata en la siguiente iteracion, si la hay.
                }
                bw.write(L+"\n");
            }//Fin while
            if (r>0) {					//Ultima linea, si es mayor, no se escribio en el bucle.
                L=fecha+"\t"+valor;
                bw.write(L+"\n");
            }
            bw.close();
        }
        catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
                archivo.delete();
                File archivo2 = new File(ruta+"diferenciaAplicada.txt");
                File f= new File(ruta+"unificado.txt");
                archivo2.renameTo(f);
             }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
    }

    private DefaultCategoryDataset obtenerSerieBarras(Vector<String> v) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        File archivo = new File(ruta+"diferenciaAplicada.txt");
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            int d=0,m=0,a=0,a1=0,m1=0,d1=0,j;
            String aux;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                int i=line.indexOf("\t");
                String f=line.substring(0,i);
                String valor=line.substring(i+1);
            //Obtencion del dia, mes y año de la fecha.
                j=f.indexOf("-");
                aux=f.substring(0,j);
                a=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf("-");
                aux=f.substring(0,j);
                m=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf(" ");
                aux=f.substring(0,j);
                d=Integer.parseInt(aux);
            //Obtencion de la hora de la fecha.
                f=f.substring(j+1);
                if (fechaInicial.contentEquals("")) fechaInicial=d+"/"+m+"/"+a+" "+f; //Variable para la gr�fica
                fechaFinal=d+"/"+m+"/"+a+" "+f;
                j=f.indexOf(":");
                if(a1==0 & m1==0 & d1==0){	//Inicialización: Primera fecha.
                    a1=a;
                    m1=m;
                    d1=d;
                }
                else{
                    if (a1!=a){
                        a1=a;
                        if (m1!=m) m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (m1!=m){
                        m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (d1!=d) d1=d;
                }
                String sa="", sv="";
                String fecha=""+d1+"-"+m1+" "+f;
                Double ev;
                for (int l=0;l<v.size();l++) {
                    int p1=valor.indexOf("\t");
                    if (p1!=(-1)){
                        sa=valor.substring(0,p1);
                        valor=valor.substring(p1+1);
                    }
                    else {	//Ultimo valor.
                        sa=valor;
                        valor="";
                    }
                    int pu=sa.indexOf(" ");
                    sv=sa.substring(0, pu-1);
                    ev=Double.parseDouble(sv);		//Valor a guardar
                    dataset.setValue(ev, nombresDGs.elementAt(l), fecha);
                }
            }//fin while leer lineas
        }
        catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
             }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
        return dataset;
    }

    //Col indica la columna desde la que coger los datos. 1=1º columna para el 1º eje. 2= 2º para el 2º
    private DefaultCategoryDataset obtenerSerieBarrasDual(int col) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        File archivo = new File(ruta+"diferenciaAplicada.txt");
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            int d=0,m=0,a=0,a1=0,m1=0,d1=0,j;
            String aux;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                int i=line.indexOf("\t");
                String f=line.substring(0,i);
                String valor=line.substring(i+1);
            //Obtencion del dia, mes y a;o de la fecha.
                j=f.indexOf("-");
                aux=f.substring(0,j);
                a=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf("-");
                aux=f.substring(0,j);
                m=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf(" ");
                aux=f.substring(0,j);
                d=Integer.parseInt(aux);
            //Obtencion de la hora de la fecha.
                f=f.substring(j+1);
                if (fechaInicial.contentEquals("")) fechaInicial=d+"/"+m+"/"+a+" "+f; //Variable para la gr�fica
                fechaFinal=d+"/"+m+"/"+a+" "+f;
                j=f.indexOf(":");
                if(a1==0 & m1==0 & d1==0){	//Inicializaci�n: Primera fecha.
                    a1=a;
                    m1=m;
                    d1=d;
                }
                else{
                    if (a1!=a){
                        a1=a;
                        if (m1!=m) m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (m1!=m){
                        m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (d1!=d) d1=d;
                }
                String sa="", sv="";
                String fecha=""+d1+"-"+m1+" "+f;
                Double ev;
                if (col==1){	//Se cogen los valores de la primera columna
                    int p1=valor.indexOf("\t");
                    sa=valor.substring(0,p1);
                    int pu=sa.indexOf(" ");
                    sv=sa.substring(0, pu-1);
                    ev=Double.parseDouble(sv);		//Valor a guardar
                    dataset.setValue(ev, nombresDGs.elementAt(0), fecha);
                    dataset.setValue(null, "vacia", fecha); //Le ponemos un nombre cualquiera, xq no se ver�
                }
                else{			//Se cogen los valores de la segunda columna
                    int p1=valor.indexOf("\t");
                    sa=valor.substring(p1+1);
                    int pu=sa.indexOf(" ");
                    sv=sa.substring(0, pu-1);
                    ev=Double.parseDouble(sv);		//Valor a guardar
                    dataset.setValue(null, "vacia", fecha);
                    dataset.setValue(ev, nombresDGs.elementAt(1), fecha);
                }
            }//fin while leer lineas
        }
        catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
             }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
        return dataset;
    }

    /*********************************************************************************************************
     * FUNCIONES PARA CREAR LOS GR�FICOS DE LA �CONSULTA C! MANIPULANDO LOS DATOS OBTENIDOS DE LAS CONSULTAS.*
     *********************************************************************************************************/
    private void maxMinMed(){
        //En este tipo de consultas, sacamos la DG del fichero original de datos, para ponerla en la graf.
        String dg=nombresFicheros.get(0);
        //dg=dg.substring(dg.indexOf("-")+7,dg.indexOf(".txt")); //Saca la DG del nombre del fich.
        if (dg.indexOf("D")>0 && dg.indexOf(" ")<0){  //Si es el resultado final de una consulta D.
            dg=dg.substring(dg.indexOf(".txt")-13,dg.indexOf(".txt")); //Saca la DG del nombre del fich.
        }
        else{   //Si son resultados de una consulta D.
            //Así, buscando a partir de .txt da igual el tipo de consulta que sea, que se saca igual.
            dg=dg.substring(dg.indexOf(" ")-6,dg.indexOf(".txt")); //Saca la DG del nombre del fich.
        }
        //---------------------------------------------
        crearEstadisticas(); //Crea el fichero estadisticas.txt donde se encuentra el FECHA MAX MIN y MED en este orden
        Vector<String> vectorOrdenUnidades=new Vector<String>();
        vectorOrdenUnidades.add(unidad);
        nombresDGs.add("Max"); //Reusamos esta variable. Nos referimos al nombre de cada una de las barras que se ven.
        nombresDGs.add("Min");
        nombresDGs.add("Med");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset=obtenerSerieBarras2(vectorOrdenUnidades);
        String unidad="";
        for (int i=0;i<vectorOrdenUnidades.size();i++) unidad=unidad+vectorOrdenUnidades.elementAt(i)+", ";
        unidad=unidad.substring(0, unidad.length()-2); //Quita el ultimo espacio y la ultima coma.
        if (unidad.indexOf("C")>=0) unidad="ºC";
        dg=dg.replaceAll("-","/");
        JFreeChart grafica = ChartFactory.createBarChart("Valores medidos de las direcciones de grupo",
                                                       "Fechas", 			//titulo eje x
                                                       "Mediciones ("+unidad+") de "+dg,
                                                       dataset,
                                                       PlotOrientation.VERTICAL,
                                                       true,				//leyenda
                                                       true,
                                                       false);
        if (fechaInicial.equals("") & fechaFinal.equals("")){ //Si están vacías es porque no hay resultados para ese intervalo.
            fechaInicial=" ? ";
            fechaFinal=" ? ";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        CategoryPlot plot = grafica.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/6.0));
        domainAxis.setTickLabelFont(new Font("Dialog",Font.PLAIN,9)); //Letra de las fechas más pequeña
        //Esconder las sombras de las barras del barchart.
        CategoryPlot categoryPlot = (CategoryPlot) grafica.getPlot();
        BarRenderer renderer = new BarRenderer();
        renderer.setShadowVisible(false);
        categoryPlot.setRenderer(renderer);
        //-------------------------------------------------
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"MaxMinMedSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"MaxMinMedBig.jpg"), grafica, 900, 600);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart."+e);
        }
    }

    private void crearEstadisticas(){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Sacamos el rango del nombre del fichero.
        nombreFichero=nombresFicheros.get(0); //En esta consulta s�lo manejamos 1 fichero.
        int i=nombreFichero.indexOf("h");
        int j=i;
        while (true){
            if (nombreFichero.charAt(j)!='-')j--;
            else break;
        }
        String rango=nombreFichero.substring(j+1,i);
        //Leemos el fichero. Primero cojo la primera l�nea que aparezca, saco la fecha y calculo la siguiente fecha
        //fechaF=fechaI+rango. Y comienzo a trabajar las lineas que se encuentren dentro de este rango.
        //Después fechaI=fechaF, y se volverá a calcular fechaF.
        File archivo = new File(ruta+nombreFichero);
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr);
            line=linea.readLine(); //Leemos la primera línea para sacar la fecha Inicial.
            //Si se trata de una DG con valores booleanos, no se hace nada y se genera una gráfica VACIA
            if (posiblesBooleanos(line,0) || posiblesBooleanos(line,1)){
                //Se crea un archivo vacío.
                String fil=ruta+"estadisticas.txt";	//PARA USARSE CON LOAD DATA INFILE EN LA BD
                File fichero_buscado = new File(fil);
                try{
                    fichero_buscado.createNewFile(); 								//CREAR FICHERO
                    BufferedWriter bw=new BufferedWriter(new FileWriter(fil,true));	//GUARDAR DATOS
                    bw.write("");
                    bw.close(); 			//Cerrar fichero
                }catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                return; //Se sale del procedimiento
            }
            String fechaI="";
            int j1=line.indexOf("\t");
            fechaI=line.substring(0, j1);
            Date fI=null;
            fI=formatoDelTexto.parse(fechaI);	//FECHA INICIAL
            Calendar c2=Calendar.getInstance();	//Debemos hacer uso de una variable calendar para modificar
            c2.setTime(fI);						//las fechas seg�n el rango pasado.
            int r=Integer.parseInt(rango);
            c2.add(Calendar.HOUR_OF_DAY, r);	//Incrementamos el n1 de horas según el rango.Hour_of_day es la hora en formato 24h
            Date fF=c2.getTime();				//FECHAFINAL=FECHAINICIAL+RANGO.
            //Inicializacion de las variables usadas en las estad�sticas, con los valores de la línea leida.
            String aux=line.substring(line.indexOf("\t")+1);
            unidad=aux.substring(0,aux.indexOf(" "));
            max=min=sum=Float.parseFloat(unidad);
            aux=aux.substring(aux.indexOf(" ")+1);
            unidad=aux;
            num=1;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                //Calculamos la siguiente fecha a comparar con la del final del rango.
                j1=line.indexOf("\t");
                fechaI=line.substring(0, j1);
                fI=formatoDelTexto.parse(fechaI);
                if (estadisticas(fI,fF,line.substring(line.indexOf("\t")+1))){	//Si es true, quiere decir que tenemos que volver a calcular fF.
                    guardarEstadisticas(fechaI);
                    //Calculamos la nueva fecha inicial, que será la fecha de la line en la que estamos ahora,
                    //que es la siguiente a la fecha final anterior.
                    j1=line.indexOf("\t");
                    fechaI=line.substring(0, j1);
                    fI=formatoDelTexto.parse(fechaI);
                    c2.setTime(fI);
                    c2.add(Calendar.HOUR_OF_DAY, r);	//Incrementamos el nº de horas según el rango.
                    fF=c2.getTime();
                    //Volvemos a inicializar las variables, a partir de la nueva fecha inicial.
                    aux=line.substring(line.indexOf("\t")+1);
                    aux=aux.substring(0,aux.indexOf(" "));
                    max=min=sum=Float.parseFloat(aux);
                    num=1;
                }
                else{}	//Si es false, seguimos leyendo los valores, y calculando las estadísticas.
            }
            //Debemos guardar lo que quede en estadisticas, porque se puede dar el caso de que no se cojan
            //intervalos exactos, y al terminar de leer el fichero, podemos estar en la mitad de 1 intervalo.
            guardarEstadisticas(fechaI);
        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
            }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
    }

    //Devuelve false, mientras aun se estén leyendo fechas que están dentro del intervalo se�alado.
    private Boolean estadisticas(Date d1, Date d2, String valor){
        int dif=d1.compareTo(d2); //compara 2 fechas.
        float v;
        if (dif<0 || dif==0){ //d1 < d2 o d1=d2, estamos dentro del intervalo.
            valor=valor.substring(0, valor.indexOf(" ")); //Le quitamos la unidad de tiempo
            v=Float.parseFloat(valor);
            if (v>max) max=v;
            if (v<min) min=v;
            sum=sum+v; //suma de la media
            num++;
            return false;
        }
        else  return true;
    }

    //Guardamos en un fichero max, min, y la media se calcula, con la fechaFinal del rango.
    private void guardarEstadisticas(String fechaI) {
        float med=sum/num;	//Se hace el calculo de la media.
        String fil=ruta+"estadisticas.txt";	//PARA USARSE CON LOAD DATA INFILE EN LA BD
        File fichero_buscado = new File(fil);
        if (fichero_buscado.exists()){	//Abre fichero existente y a�ade datos
            try{//GUARDAR DATOS
                BufferedWriter bw=new BufferedWriter(new FileWriter(fil,true)); //true, para guardar al final del fichero
                bw.write(fechaI+"\t"+max+"\t"+min+"\t"+med+"\n");
                bw.close(); 				//Cerrar fichero
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        else{	//Sino, crea fichero y añade datos
            try{
                fichero_buscado.createNewFile(); 								//CREAR FICHERO
                BufferedWriter bw=new BufferedWriter(new FileWriter(fil,true));	//GUARDAR DATOS
                bw.write(fechaI+"\t"+max+"\t"+min+"\t"+med+"\n");
                bw.close(); 			//Cerrar fichero
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private DefaultCategoryDataset obtenerSerieBarras2(Vector<String> v) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        File archivo = new File(ruta+"estadisticas.txt");
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            int d=0,m=0,a=0,a1=0,m1=0,d1=0,j;
            String aux;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                int i=line.indexOf("\t");
                String f=line.substring(0,i);
                String valor=line.substring(i+1);
            //Obtencion del dia, mes y año de la fecha.
                j=f.indexOf("-");
                aux=f.substring(0,j);
                a=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf("-");
                aux=f.substring(0,j);
                m=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf(" ");
                aux=f.substring(0,j);
                d=Integer.parseInt(aux);
            //Obtencion de la hora de la fecha.
                f=f.substring(j+1);
                if (fechaInicial.contentEquals("")) fechaInicial=d+"/"+m+"/"+a+" "+f; //Variable para la gráfica
                fechaFinal=d+"/"+m+"/"+a+" "+f;                
                j=f.indexOf(":");
                if(a1==0 & m1==0 & d1==0){	//Inicialización: Primera fecha.
                    a1=a;
                    m1=m;
                    d1=d;
                }
                else{
                    if (a1!=a){
                        a1=a;
                        if (m1!=m) m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (m1!=m){
                        m1=m;
                        if (d1!=d) d1=d;
                    }
                    else if (d1!=d) d1=d;
                }
                String sv="";
                String fecha=""+d1+"-"+m1+" "+f;
                Double ev;
                for (int l=0;l<3;l++) {	  //Hasta 3, porque en cada linea hay 3 valores: max,min y med.
                    int p1=valor.indexOf("\t");
                    if (p1!=(-1)){
                        sv=valor.substring(0,p1);
                        valor=valor.substring(p1+1);
                    }
                    else {	//Ultimo valor.
                        sv=valor;
                        valor="";
                    }
                    ev=Double.parseDouble(sv);		//Valor a guardar
                    dataset.setValue(ev, nombresDGs.elementAt(l), fecha);
                }
            }//fin while leer lineas
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
        return dataset;
    }

    /*********************************************************************************************************
     * FUNCIONES PARA CREAR LOS GR�FICOS DE LA �CONSULTA D! MANIPULANDO LOS DATOS OBTENIDOS DE LAS CONSULTAS.*
     *********************************************************************************************************/
    private void difPorc(){
        //Aplicarle la fÓrmula a los valores de las dos direcciones, obteniendo un sÓlo fichero.
        aplicarFormula(1); //Se le aplica con porcentajes.
        //En este tipo de consultas, buscamos el tipo que es y la unidad, para que se vean.
        String tipo=unidad.substring(0, unidad.indexOf("\t"));
        unidad=unidad.substring(unidad.indexOf("\t")+1);
        if (unidad.indexOf("C")>=0) unidad="ºC";
        TimeSeries serie=obtenerSerieEvolucion2();			 //Reutilizamos este código.
        TimeSeriesCollection dataset=new TimeSeriesCollection(serie); //Sólo se obtiene una curva.
        JFreeChart grafica = ChartFactory.createTimeSeriesChart("Valores medidos de las direcciones de grupo",//titulo
                                                                "Fechas", 			//titulo eje x
                                                                "Mediciones en "+tipo+" "+"("+unidad+")",	//titulo eje y
                                                                dataset,			//dataset
                                                                true, 				//leyenda
                                                                true, 				//tooltips
                                                                false);				//configure chart to generate URLs?
           
       //Dar color a cada categoria
        grafica.setBackgroundPaint(Color.WHITE);	//Color del fondo del gráfico
        if (fechaInicial.equals("") & fechaFinal.equals("")){ //Si están vacías es porque no hay resultados para ese intervalo.
            fechaInicial=" ? ";
            fechaFinal=" ? ";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"DiferenciasSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"DiferenciasBig.jpg"), grafica, 900, 600);
        } catch (IOException e1) {
            System.err.println("Problem occurred creating chart.");
        }
    }

    private void difAbs(){
        //Aplicarle la fórmula a los valores de las dos direcciones, obteniendo un sólo fichero.
        aplicarFormula(2); //Se le aplica con porcentajes.
        String tipo=unidad.substring(0, unidad.indexOf("\t"));
        if (tipo.contentEquals("ABS")) tipo="DIF";
        unidad=unidad.substring(unidad.indexOf("\t")+1);
        if (unidad.indexOf("C")>=0) unidad="ºC";
        TimeSeries serie=obtenerSerieEvolucion2();			 //Reutilizamos este código.
        TimeSeriesCollection dataset=new TimeSeriesCollection(serie); //Sólo se obtiene una curva.
        JFreeChart grafica = ChartFactory.createTimeSeriesChart("Valores medidos de las direcciones de grupo",//titulo
                                                                "Fechas", 				//titulo eje x
                                                                "Mediciones en "+tipo+" "+"("+unidad+")",//titulo eje y
                                                                dataset,				//dataset
                                                                true, 					//leyenda
                                                                true, 					//tooltips
                                                                false);					//configure chart to generate URLs?
        //Dar color a cada categoria
        grafica.setBackgroundPaint(Color.WHITE);	//Color del fondo del grafico
        if (fechaInicial.equals("") & fechaFinal.equals("")){ //Si estan vacías es porque no hay resultados para ese intervalo.
            fechaInicial=" ? ";
            fechaFinal=" ? ";
        }
        TextTitle t=new TextTitle("desde "+fechaInicial+" hasta "+fechaFinal,new Font("SanSerif", Font.ITALIC, 12));
        grafica.addSubtitle(t);
        try {
            ChartUtilities.saveChartAsJPEG(new File(ruta+"DiferenciasSmall.jpg"), grafica, 400, 300);
            ChartUtilities.saveChartAsJPEG(new File(ruta+"DiferenciasBig.jpg"), grafica, 900, 600);
        } catch (IOException e1) {
            System.err.println("Problem occurred creating chart.");
        }
    }

    /**
     * FUNCIóN ESTADíSTICA PARA HALLAR LA EVOLUCIÓN DE DIFERENCIAS.
     * PORCENTAJE=(v1-v2)/v1*100
     * DIFERENCIA=(v1-v2)
     * En caso de que no se encuentren los dos valores, y sólo haya 1 o ninguno, se obvia esa medicion.
     * Vamos a comparar según un intervalo en minutos. Si en ese intervalo, hay + de 1 medicion, se hace la media.
     * @param i, subopcion: 1=porcentaje y otro=diferencia normal
     */
    private void aplicarFormula(int i) {
        Vector<String> nombresFicheros2=new Vector<String>(nombresFicheros);	//Copiamos los nombres
        String freferencia=nombresFicheros2.get(0); //Se coge el 1º fichero de referencia, al que se le ir�n a�adiendo los datos.
        nombresFicheros2.remove(0);
        String fcomparar=nombresFicheros2.get(0);
        compararFicheros(freferencia,fcomparar);
        //Nombre del fichero resultante: n=tipo-intervalo-x-xx-xx-y-yy-yy
        String d1,d2,n;
        n=freferencia.substring(0,4);	//Hasta el segundo '-', es decir coge el tipo de consulta e intervalo
        //d1=freferencia.substring(freferencia.length()-10, freferencia.length()-4);	//La DG del fich referencia (1º columna de valores)
        //d2=fcomparar.substring(fcomparar.length()-10, fcomparar.length()-4); 		//La DG del otro fichero    (2º columna de valores)
        d1=freferencia.substring(freferencia.indexOf(" ")-6,freferencia.indexOf(" "));
        d2=fcomparar.substring(fcomparar.indexOf(" ")-6,fcomparar.indexOf(" "));
        //System.out.println(d1+"-"+d2+".");

        n=n+"-"+d1+"-"+d2; 	//Fichero usado para obtener la serie. Es el fichero resultante de aplicarFormulaP()
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        //Leemos el fichero unificado.txt con los dos valores y les aplicamos la f�rmula (V1/V2)*100
        File uni = new File(ruta+"unificado.txt");
        try {
            int i1=1;
            fr = new FileReader (uni);
            linea = new BufferedReader(fr);
            unidad="";
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                String v,f,l;
                float v1=0,v2;
                f=line.substring(0,line.indexOf("\t"));
                l=line.substring(line.indexOf("\t")+1); //l tiene hasta los valores.
                if (l.indexOf("0")==0) v1=0;		//El primer valor es un 0.
                else if (l.indexOf(" ")>0){		//El 1º valor es distinto de 0, por lo convertimos a int.
                    v=l.substring(0, l.indexOf(" "));
                    v1=Float.parseFloat(v);
                    if (i1==1){
                        String aux=l.substring(l.indexOf(" ")+1);
                        if (aux.indexOf("\t")>=0) aux=aux.substring(0, aux.indexOf("\t"));
                        unidad=unidad+aux;
                        //i1=0;
                    }
                }
                if (l.indexOf("\t")>=0){	//Si encontramos \t, quiere decir que hay un segundo valor !=0
                    l=l.substring(l.indexOf("\t")+1);
                    v=l.substring(0, l.indexOf(" "));
                    v2=Float.parseFloat(v);
                    /*if (i1==1){
                            //unidad=unidad+l.substring(l.indexOf(" ")+1);
                            i1=0;
                    }*/
                }
                else v2=0;
                //Una vez tenemos los valores en v1 y v2, aplicamos la formula. Si el numerador y/o denominador es 0
                //entonces el resultado será directamente 0. En v1, guardamos el resultado (reusamos esta variable)
                if (v1==0 || v2==0)  v1=0;
                else if (v1==0 && v2==0) v1=0;
                else {
                    if (i==1) { //HALLA EL PORCENTAJE.
                        float r=v1-v2;
                        if (r<0) r=0-r;
                        v1=(r/v1)*100;
                    }
                    else v1=v1-v2; 	   //HALLA EL VALOR de la diferencia, que puede ser negativo.
                }
                guardar(f,v1,n);
                i1=0;
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
        if (i==1) unidad="%"+"\t"+unidad;
        else unidad="ABS"+"\t"+unidad;
    }

    private void compararFicheros(String freferencia, String fcomparar) {
        System.out.println("min="+rangoMinutos+".");
        File fr=new File(ruta+freferencia);	//El primer fichero es el de referencia, y del que se sacan los incrementos.
        File fc=new File(ruta+fcomparar);
        FileReader fr1=null, fr2=null;
        BufferedReader linea1=null, linea2=null;
        String L1=null, L2=null;
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //Vamos a abrir los dos ficheros a la vez, leyendo línea por línea y comparando.
        try {
            File temp = new File(ruta+"temp.txt");
            if (!temp.exists()) temp.createNewFile(); //Crea el fichero
            //LEEMOS EL PRIMER FICHERO
            fr1 = new FileReader (fr);
            linea1 = new BufferedReader(fr1);
            Vector<String> fechas=new Vector<String>(); //vector donde irán todas las fechas
            Date d1=null,dini=null,dfin=null;
            boolean primeravez=true,nuevo=true;
            float media1=0;
            int nelem1=0, segd1=0;
            String fecha="",aux="",lant="";
            while ((L1=linea1.readLine())!=null){
                aux=L1.substring(0, L1.indexOf("\t")); //Fecha de la línea.
                d1=formatoDelTexto.parse(aux);
                String Ssegd1=aux.substring(aux.indexOf(" ")+7,aux.indexOf(" ")+9);
                segd1=Integer.parseInt(Ssegd1);
                d1.setSeconds(segd1);
                if (nuevo){ //Si terminamos el intervalo, pasamos al siguiente.
                    Calendar c=Calendar.getInstance();
                    if (!primeravez){//Si no es la primera iteracion, se guarda antes de inicializar.
                        //System.out.println(media1+"  "+nelem1);
                        media1=media1/nelem1;
                        BufferedWriter bw=new BufferedWriter(new FileWriter(ruta+"temp.txt",true)); //Para escribir.
                        String x1=""+media1;
                        if (!x1.contentEquals("NaN")) bw.write(""+fecha+"\t"+media1+" "+unidad+"\n");
                        bw.close();
                        media1=nelem1=0;
                        String v=lant.substring(lant.indexOf("\t")+1);
                        v=v.substring(0, v.indexOf(" "));
                        media1=Float.parseFloat(v);//Se inicializan con los valores de la anterior linea.
                        nelem1=1;
                    }
                    else{
                       String u=L1.substring(L1.indexOf("\t")+1);
                       unidad=u.substring(u.indexOf(" ")+1);
                       lant=L1;
                        //Si es la 1ª vez, también se inicializa la fecha inicial.
                    }
                    primeravez=false; 
                    fecha=lant.substring(0, lant.indexOf("\t"));
                    fechas.add(fecha);
                     //Inserta la fecha en el vector de fechas, para luego usarlo en el 2º fichero.
                    Date diniaux=formatoDelTexto.parse(fecha);
                    String Ssegd2=fecha.substring(fecha.indexOf(" ")+7,fecha.indexOf(" ")+9);
                    int segd2=Integer.parseInt(Ssegd2);
                    c.setTime(diniaux);
                    dini=c.getTime();
                    dini.setSeconds(segd2);
                    //System.out.println("Ini="+dini);
                    c.add(Calendar.MINUTE, Integer.parseInt(rangoMinutos));
                    dfin=c.getTime();
                    dfin.setSeconds(segd2);
                    //System.out.println("Fin="+dfin);
                    nuevo=false;//Esta variable se usa para definir otra vez un nuevo intervalo
                }
                if (d1.compareTo(dini)==0){ //Fechas Iguales
                    aux=L1.substring(L1.indexOf("\t")+1);
                    aux=aux.substring(0,aux.indexOf(" "));
                    media1=media1+Float.parseFloat(aux);
                    nelem1++;
                }
                else if(d1.compareTo(dini)>0 && d1.compareTo(dfin)<0){ //Está dentro del intervalo
                    aux=L1.substring(L1.indexOf("\t")+1);
                    aux=aux.substring(0,aux.indexOf(" "));
                    media1=media1+Float.parseFloat(aux);
                    nelem1++;
                }
                else{//Si la fecha es menor que la fecha inicial o mayor que la final, se cambia de intervalo.
                    nuevo=true;
                }
                lant=L1;
            }
            //guardo lo ultimo y penultimo si lo hay
            media1=media1/nelem1;
            BufferedWriter bw=new BufferedWriter(new FileWriter(ruta+"temp.txt",true)); //Para escribir.
            String x1=""+media1;
            String auxi=dini.toGMTString(); //d mon yyyy hh:mm:ss
            String d=auxi.substring(0,auxi.indexOf(" "));
            if (Integer.parseInt(d)<10)d="0"+d;
            auxi=auxi.substring(auxi.indexOf(" ")+1);
            String m=auxi.substring(0,auxi.indexOf(" "));
            if (m.contentEquals("Jan")) m="01";
            if (m.contentEquals("Feb")) m="02";
            if (m.contentEquals("Mar")) m="03";
            if (m.contentEquals("Apr")) m="04";
            if (m.contentEquals("May")) m="05";
            if (m.contentEquals("Jun")) m="06";
            if (m.contentEquals("Jul")) m="07";
            if (m.contentEquals("Aug")) m="08";
            if (m.contentEquals("Sep")) m="09";
            if (m.contentEquals("Oct")) m="10";
            if (m.contentEquals("Nov")) m="11";
            if (m.contentEquals("Dec")) m="12";
            auxi=auxi.substring(auxi.indexOf(" ")+1);
            String y=auxi.substring(0,auxi.indexOf(" "));
            auxi=auxi.substring(auxi.indexOf(" ")+1);
            String h=auxi.substring(0,auxi.indexOf(" "));
            //System.out.println(y+"-"+m+"-"+d+" "+h);
            if (!x1.contentEquals("NaN")) bw.write(""+y+"-"+m+"-"+d+" "+h+"\t"+media1+" "+unidad+"\n");
            bw.close();
            fechas.add(y+"-"+m+"-"+d+" "+h);
            fecha=lant.substring(0, lant.indexOf("\t"));
            if (!fecha.isEmpty()){
                String auxr=lant.substring(lant.indexOf("\t")+1);
                auxr=auxr.substring(0,auxr.indexOf(" "));
                media1=Float.parseFloat(auxr);
                bw=new BufferedWriter(new FileWriter(ruta+"temp.txt",true)); //Para escribir.
                x1=""+media1;
                if (!x1.contentEquals("NaN")) bw.write(""+fecha+"\t"+media1+" "+unidad+"\n");
                bw.close();
                fechas.add(fecha);
            }
            fr1.close();
            //for (int i=0;i<fechas.size();i++) System.out.println("*"+fechas.elementAt(i));

            //Leido el primer fichero, leo el segundo.
            File temp2 = new File(ruta+"temp2.txt");
            if (!temp2.exists()) temp2.createNewFile(); //Crea el fichero
            fr2 = new FileReader (fc);
            linea2 = new BufferedReader(fr2);
            int pos=0;
            String fechaf="";
            media1=nelem1=0;
            nuevo=true;
            primeravez=true;
            while ((L2=linea2.readLine())!=null){                
                aux=L2.substring(0, L2.indexOf("\t")); //Fecha de la línea.
                d1=formatoDelTexto.parse(aux);
                String Ssegd1=aux.substring(aux.indexOf(" ")+7,aux.indexOf(" ")+9);
                segd1=Integer.parseInt(Ssegd1);
                d1.setSeconds(segd1);
                if (nuevo){ //Si terminamos el intervalo, pasamos al siguiente.            
                    Calendar c=Calendar.getInstance();
                    if (!primeravez){//Si no es la primera iteracion, se guarda antes de inicializar.
                        media1=media1/nelem1;
                        BufferedWriter bw1=new BufferedWriter(new FileWriter(ruta+"temp2.txt",true)); //Para escribir.
                        x1=""+media1;
                        if (!x1.contentEquals("NaN")) bw1.write(""+/*fechas.elementAt(pos)+"\t"+*/media1+" "+unidad+"\n");
                        bw1.close();
                        pos++;
                        //media1=nelem1=0;
                        String v=lant.substring(lant.indexOf("\t")+1);
                        v=v.substring(0, v.indexOf(" "));
                        media1=Float.parseFloat(v);//Se inicializan con los valores de la anterior linea.
                        nelem1=1;
                    }
                    else{
                       String u=L2.substring(L2.indexOf("\t")+1);
                       unidad=u.substring(u.indexOf(" ")+1);
                       lant=L1;
                       pos=0;
                        //Si es la 1ª vez, también se inicializa la fecha inicial.
                    }
                    //System.out.println(fechas.elementAt(pos));
                    primeravez=false; 
                    fecha=fechas.elementAt(pos);
                    Date diniaux=formatoDelTexto.parse(fecha);
                    String Ssegd2=fecha.substring(fecha.indexOf(" ")+7,fecha.indexOf(" ")+9);
                    int segd2=Integer.parseInt(Ssegd2);                   
                    c.setTime(diniaux);
                    dini=c.getTime();   //FECHA INICIAL.                    
                    dini.setSeconds(segd2);

                    if (pos+1>=fechas.size()) break;
                    fechaf=fechas.elementAt(pos+1);
                    Date dfinaux=formatoDelTexto.parse(fechaf);
                    Ssegd2=fecha.substring(fechaf.indexOf(" ")+7,fechaf.indexOf(" ")+9);
                    segd2=Integer.parseInt(Ssegd2);
                    c.setTime(dfinaux);
                    dfin=c.getTime();   //FECHA FINAL
                    dfin.setSeconds(segd2);
                    //System.out.println("INI="+dini);
                    //System.out.println("FIN="+dfin);
                    nuevo=false;//Esta variable se usa para definir otra vez un nuevo intervalo
                }
                if (d1.compareTo(dini)==0){ //Fechas Iguales
                    aux=L2.substring(L2.indexOf("\t")+1);
                    aux=aux.substring(0,aux.indexOf(" "));
                    media1=media1+Float.parseFloat(aux);
                    nelem1++;
                }
                else if(d1.compareTo(dini)>0 && d1.compareTo(dfin)<0){ //Está dentro del intervalo
                    aux=L2.substring(L2.indexOf("\t")+1);
                    aux=aux.substring(0,aux.indexOf(" "));
                    media1=media1+Float.parseFloat(aux);
                    nelem1++;
                }
                else{//Si la fecha es menor que la fecha inicial o mayor que la final, se cambia de intervalo.
                    nuevo=true;
                }
                lant=L2;
            }
            //guardo lo ultimo si lo hay
            fecha=lant.substring(0, lant.indexOf("\t"));
            if (!fecha.isEmpty()){
                String auxr=lant.substring(lant.indexOf("\t")+1);
                auxr=auxr.substring(0,auxr.indexOf(" "));
                media1=Float.parseFloat(auxr);
                BufferedWriter bw2=new BufferedWriter(new FileWriter(ruta+"temp2.txt",true)); //Para escribir.
                x1=""+media1;
                if (!x1.contentEquals("NaN")) bw2.write(""+/*fechas.elementAt(pos+1)+"\t"+*/media1+" "+unidad+"\n");
                bw2.close();
            }
            fr2.close();
            //CREAMOS EL UNIFICADO
            File unificado = new File(ruta+"unificado.txt");
            if (!unificado.exists()) unificado.createNewFile(); //Crea el fichero
            fr1 = new FileReader (temp);
            linea1 = new BufferedReader(fr1);
            fr2 = new FileReader (temp2);
            linea2 = new BufferedReader(fr2);
            L1=L2="";
            BufferedWriter bwf=new BufferedWriter(new FileWriter(ruta+"unificado.txt",true)); //Para escribir.
            while((L1=linea1.readLine())!=null && (L2=linea2.readLine())!=null){
                bwf.write(L1+"\t"+L2+"\n");
            }
            bwf.close();
            fechas.removeAllElements();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if (null != fr1) fr1.close();   	//cierra el 1º fichero.
                if (null != fr2) fr2.close();   	//cierra el 2º fichero.
            }catch (Exception e2){ 			//Sino salta una excepcion.
                e2.printStackTrace();
            }
        }
        File temp1 = new File(ruta+"temp.txt");
        File temp2 = new File(ruta+"temp2.txt");
        temp1.delete();
        temp2.delete();
     }

    //Guardamos en una nueva linea al final del fichero D[p|a]-[I|T]-d1-d2.txt
    private void guardar(String f, float v1, String n) {
        String fil=ruta+n+".txt";	//PARA USARSE CON LOAD DATA INFILE EN LA BD
        nombreFichero=n+".txt"; 	//Fichero usado para obtener la serie. Es el fichero resultante de aplicarFormulaP()
        File fichero_buscado = new File(fil);
        if (fichero_buscado.exists()){	//Abre fichero existente y a�ade datos
            try{//GUARDAR DATOS
                BufferedWriter bw=new BufferedWriter(new FileWriter(fil,true)); //true, para guardar al final del fichero
                bw.write(f+"\t"+v1+" "+unidad+"\n");
                bw.close(); 				//Cerrar fichero
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        else{	//Sino, crea fichero y añade datos
            try{
                fichero_buscado.createNewFile(); 								//CREAR FICHERO
                BufferedWriter bw=new BufferedWriter(new FileWriter(fil,true));	//GUARDAR DATOS
                bw.write(f+"\t"+v1+" "+unidad+"\n");
                bw.close(); 			//Cerrar fichero
            }catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /*****************************************************************************************
     * Obtiene la serie de un fichero para la consulta D, que es un poco diferente a la de A.*
     *****************************************************************************************/
    private TimeSeries obtenerSerieEvolucion2() {
        String naux1="",naux2;
        //x-x-xx-x-x-xx.txt
        naux1=nombreFichero.substring(nombreFichero.indexOf(".txt")-13,nombreFichero.indexOf(".txt")-7); //Saca la DG del nombre del fich.
        naux1=naux1.replaceAll("-", "/"); //Para que las DGs sea x/xx/xx en vez de x-xx-xx
        naux2=nombreFichero.substring(nombreFichero.indexOf(".txt")-6,nombreFichero.indexOf(".txt")); //Saca la DG del nombre del fich.
        naux2=naux2.replaceAll("-", "/"); //Para que las DGs sea x/xx/xx en vez de x-xx-xx
        naux1=naux1+"-"+naux2;
        TimeSeries serie = new TimeSeries(naux1);
        File archivo = new File(ruta+nombreFichero);
        FileReader fr = null;
        BufferedReader linea = null;
        String line;
        try {
            fr = new FileReader (archivo);
            linea = new BufferedReader(fr); 		//Se crea para leer las lineas
            int d=0,m=0,a=0,a1=0,m1=0,d1=0,j,h1,h2,h3;
            double e=0;
            String aux,h,minutos,segundos;
            int min_ant=0,sec_ant=0,vez1=0;			//min_prim mira si es el primero, para comparar ant y act.
            Day day1=null;
            while((line=linea.readLine())!=null){ 	//Lectura del fichero
                int i=line.indexOf("\t");
                String f=line.substring(0,i);
                String valor=line.substring(i+1);
            //Obtencion del dia, mes y año de la fecha.
                j=f.indexOf("-");
                aux=f.substring(0,j);
                a=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf("-");
                aux=f.substring(0,j);
                m=Integer.parseInt(aux);
                f=f.substring(j+1);
                j=f.indexOf(" ");
                aux=f.substring(0,j);
                d=Integer.parseInt(aux);
            //Obtencion de la hora de la fecha.
                f=f.substring(j+1);
                if (fechaInicial.contentEquals("")) fechaInicial=d+"/"+m+"/"+a+" "+f; //Variable para la gr�fica
                fechaFinal=d+"/"+m+"/"+a+" "+f;
                j=f.indexOf(":");
                h=f.substring(0,j);
                f=f.substring(j+1);
                j=f.indexOf(":");
                minutos=f.substring(0,j);
                segundos=f.substring(j+1);
                if(a1==0 & m1==0 & d1==0){	//Inicializaci�n: Primera fecha.
                    a1=a;
                    m1=m;
                    d1=d;
                    day1=new Day(d1,m1,a1);
                }
                else{
                    if (a1!=a){
                        a1=a;
                        if (m1!=m) m1=m;
                        if (d1!=d) d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                    else if (m1!=m){
                        m1=m;
                        if (d1!=d) d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                    else if (d1!=d){
                        d1=d;
                        day1=new Day(d1,m1,a1);
                    }
                }
                //Comprueba si es boolean. Si lo es, se le asigna 0 � 1
                //para poder representarlo en la gr�fica. Si no, su <<valor>>.
                if (posiblesBooleanos(valor, 1)) e=1;
                else if (posiblesBooleanos(valor, 0)) e=0;
                else{   //NO ES UN BOOLEANO.
                    int u=valor.indexOf(" ");
                        valor=valor.substring(0, u);
                        e=Double.parseDouble(valor);
                }
                //Comprobamos que la hora no coincida, para que si coincide, introducir en la serie s�lo
                //la primera aparición de la fecha con su valor, por ser este m�s representativo seg�n lo visto.
                if (vez1==0){
                    min_ant=h1=Integer.parseInt(minutos);	//minutos
                    h2=Integer.parseInt(h);					//hora
                    sec_ant=h3=Integer.parseInt(segundos);	//segundos
                    serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                    vez1=1;
                }
                else{
                    h1=Integer.parseInt(minutos);	//minutos
                    h2=Integer.parseInt(h);		//hora
                    h3=Integer.parseInt(segundos);	//segundos
                    if (min_ant==h1){			//Si el minuto es =, comprobamos los segundos
                        if (sec_ant==h3){}		//Si los segundos son =, no se introduce nada en la serie.
                        else{				//Si los segundos son !=, se introduce en la serie.
                            serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                            sec_ant=h3;
                        }
                    }
                    else{				//Si el minuto es !=, se introduce en la serie.
                        serie.addOrUpdate(new Second(h3,new Minute(h1,new Hour(h2,day1))),e);//Montamos la serie en base a los segundos, minutos, hora y d�a
                        min_ant=h1;
                        sec_ant=h3;
                    }
                }
            }
        }
        catch(Exception e){
             e.printStackTrace();
        }finally{
             try{
                if (null != fr) fr.close();   	//Se cierra si todo va bien.
             }catch (Exception e2){ 		//Sino salta una excepcion.
                e2.printStackTrace();
             }
        }
        return serie;
    }
}
