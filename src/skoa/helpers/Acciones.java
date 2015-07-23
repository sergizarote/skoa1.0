/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.helpers;

import skoa.models.MiFormato;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import skoa.views.DispositivoEliminar;
import skoa.views.EstanciaEliminar;
import skoa.views.PlantaEliminar;
import skoa.views.Entrada;
import skoa.views.DispositivoNuevo;
import skoa.views.EstanciaGenerica;
import skoa.views.SkoaMain;
import skoa.views.ModificarPlano;
import skoa.views.NuevoProyecto;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 
 * @author David Monné Chávez
 *
 */
public class Acciones {

    /**
     * Metodo que busca dentro del archivo xml la ruta de la imagne
     * 
     * @param planta
     * @param estancia
     * @return
     */
    public static String SeleccionarImagen(String planta, String estancia) {
        String dir_imagen = "";
        try {
            // crea una instancia de la clase File del archivo de configuracion
            // xml
            File aFile = new File(NuevoProyecto.archivo);

            // instancia de SAXReader para leer y recorrer el arbol del xml
            SAXReader xmlReader = new SAXReader();

            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            // Comenzamos a leer el aarbol
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            // recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                // si el nodo en el que estoy es "planta"
                if (node.getName().equals( "planta")
                        && node.valueOf("@alias").equals(planta)) {
                    node2 = node;
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia")) {
                            if (node2.valueOf("@nombre").equals(estancia)) {
                                // EstanciaCaracteristicas.dir_imagen =
                                // node2.valueOf( "@imagen" ) ;
                                dir_imagen = node2.valueOf("@imagen");
                                return dir_imagen;
                            }
                        }
                    }
                }
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return dir_imagen;
    }

    /**
     * Metodo principal
     */
    public static void main(int accion) {
        if (accion == 1) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                     Entrada.main();
                }
            });
        }

    }

    /**
     * inserta iconos sobre el plano de una estancia, en el fichero xml, de 1 en 1
     * @param insertar
     * @param nombreIcono
     */
    public static void insertaIconos(JLabel insertar, String nombreIcono, String planta,String estancia) {
        String x = Integer.toString(insertar.getX());
        String y = Integer.toString(insertar.getY());
        String ancho = Integer.toString(insertar.getWidth());
        String largo = Integer.toString(insertar.getHeight());
        String inombre = "";
        String idirgrup = "";
        String itipo = "";
        String isubtipo = "";

        try {

            // instancia del la clase File para leer el archivo xml
            File aFile = new File(NuevoProyecto.archivo);

            // instancia del la clase xmlReader para recorer el arbol del xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            // leemos un obtenemos el nodo raiz
            Document doc = xmlReader.read(aFile);

            // obtenemos el node vivienda
            Element node = (Element) doc.selectSingleNode("//vivienda");

            Element node2 = null;// (Element) doc.selectSingleNode(//
            // "//vivienda/planta");;
            Element node3 = null;
            //String aux;
            //String auxi;
            org.dom4j.Element modificar;

            // leemos el contador de nodo
            String cont = node.valueOf("@contador");
            int contador = Integer.parseInt(cont);
            contador++;

            String nuevoContador = Integer.toString(contador);
            while (nuevoContador.length() != 6) {
                nuevoContador = "0" + nuevoContador;
            }

            Attribute atributoc = node.attribute("contador");
            atributoc.setText(Integer.toString(contador));

            // recoremos los hijos dle nodo vivienda
            if(nombreIcono != null ){
                for (Iterator i = node.elementIterator()  ; i.hasNext();) {
                    node = (Element) i.next();

                    // si es la planta buscada
                    if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                        node2 = node;
                        for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                            node2 = (Element) ia.next();

                            // estancia
                            if (node2.valueOf("@nombre").equals(estancia)) {
                                node3 = node2;

                                // recorremos el nodo para acceder a
                                // actuadores,sensores,iconos
                                for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                    node3 = (Element) ib.next();

                                    if (node3.getName().equals("actuadores")) {

                                        String[] fieldsToInclude = {"regulacion",
                                            "conmutacion", "persianas",
                                            "electrovalvula"
                                        };

                                        for (String field : fieldsToInclude) {

                                            if (nombreIcono.equals(field)) {
                                                Attribute atributo = node3.attribute(field);
                                                atributo.setText("si");
                                            }
                                        }
                                    }// end if actuadores y sensores

                                    if (node3.getName().equals("iconos")) {
                                        String[] fieldsToInclude = {"regulacion",
                                            "conmutacion", "persianas",
                                            "electrovalvula", "puerta",
                                            "movimiento", "temperatura",
                                            "combinado", "inundacion",
                                            "contadores"};

                                        for (String field : fieldsToInclude) {
                                            if (nombreIcono.equals(field)) {
                                                for (MiFormato miFormato : DispositivoNuevo.parametros) {

                                                    if ((miFormato.x.equals(x)) && (miFormato.y.equals(y))) {
                                                        inombre = miFormato.nombre;
                                                        idirgrup = miFormato.direccionGrupo;
                                                        itipo = miFormato.tipoEstructura;
                                                        isubtipo = miFormato.subtipo;
                                                    }
                                                }


                                                org.dom4j.Element anadir = 
                                                node3.addElement("label")
                                                        .addAttribute("tipo", field)
                                                        .addAttribute("x", x)
                                                        .addAttribute("y", y)
                                                        .addAttribute("ancho", ancho)
                                                        .addAttribute("largo", largo)
                                                        .addAttribute("id", nuevoContador) // Integer.toString(contador));
                                                        .addAttribute("direccionGrupo", idirgrup)
                                                        .addAttribute("tipoEstructura", itipo)
                                                        .addAttribute("subtipo", isubtipo);
                                                // anadir.addAttribute("nombred", inombre)

                                            }

                                        }
                                    }

                                    // iconos++++++++++++++++++++++++++++++++++++++++++++++++

                                    if (node3.getName().equals("cantidad")) {
                                        String[][] fieldsToInclude = {
                                            new String[]{"regulacion", "@lucreg", "lucreg"},
                                            new String[]{"conmutacion", "@bombs", "bombs"},
                                            new String[]{"persianas", "@blinds", "blinds"},
                                            new String[]{"electrovalvula", "@valves", "valves"},
                                            new String[]{"puerta", "@doors", "doors"},
                                            new String[]{"movimiento", "@presencia", "presencia"},};

                                        for (String[] field : fieldsToInclude) {

                                            if (nombreIcono.equals(field[0])) {
                                                String valor = node3.valueOf(field[1]);
                                                int numero = Integer.parseInt(valor) + 1;

                                                Attribute atributo = node3.attribute(field[2]);
                                                atributo.setText(Integer.toString(numero));
                                            }
                                        }
                                    }// end if cantidad

                                }// end for node3
                            }// end if estancia==estancia seleccionada
                        }// end for node2
                    } // end consulta si es la planta que busco
                }// end for
            }


            //String auxiliar = doc.asXML();
            //FileWriter archivo;
            //archivo = new FileWriter(principal.archivo);

            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            // XMLWriter writer = new XMLWriter(new FileWriter(
            // principal.archivo ) );

            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }// end insertaIconosf

    /**
     * Metodo que verifica si es una estancia unica
     * @param planta
     * @param estancia
     * @return
     */
    public static boolean estanciaUnica(String planta, String estancia) {
        try {

            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol del xml
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos el nodo raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el nodo vivienda
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                //si encuentra una planta y es la planta que busco

                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();
                        //dentro de plantas solo hay estancias, si es la que busco:
                        if (node2.valueOf("@nombre").equals(estancia)) {// estancia
                            return true;
                        }
                    }//end for
                }
            }//end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo que verifica si es una plnata unica
     * @param planta
     * @return
     */
    public static boolean plantaUnica(String planta) {

        try {
            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol del xml
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos el nodo raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el nodo vivienda
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                //si encuentra una planta y es la planta que busco
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    return true;

                }
            }//end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return false;
    }//end PlantaUnica

    /**
     * Metodo para inicializar a "no" los actuadores y sensores
     * @param nombrePlanta
     * @param nombreEstancia
     */
    public static void inicializarEstancia(String nombrePlanta, String nombreEstancia) {
        try {
            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol del xml
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");

            //cargamos el nodo raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");


            Element node2 = null;
            Element node3 = null;
            String aux;
            String auxi;

            //recorremos el nodo vivienda
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                if (node.getName().equals("planta")) {
                    aux = node.valueOf("@alias");

                    if (aux.equals(nombrePlanta)) { // si es la planta que busco

                        node2 = node;

                        for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                            node2 = (Element) ia.next();
                            aux = node2.valueOf("@nombre");

                            // estancia_nueva.seleccionado;
                            auxi = nombreEstancia;

                            // si es la estancia que  busco
                            if (aux.equals(auxi)) {

                                org.dom4j.Element anadir2 = node2.addElement("actuadores").addAttribute("regulacion", "no").addAttribute("conmutacion", "no").addAttribute("persianas", "no").addAttribute("electrovalvula", "no");

                                org.dom4j.Element anadir3 = node2.addElement("sensores").addAttribute("puerta", "no").addAttribute("movimiento", "no").addAttribute("temperatura", "no").addAttribute("combinado", "no").addAttribute("inundacion", "no").addAttribute("contadores", "no");

                                org.dom4j.Element anadir4 = node2.addElement("iconos");

                                org.dom4j.Element anadir5 = node2.addElement("cantidad").addAttribute("blinds", "0").addAttribute("lucreg", "0").addAttribute("bombs", "0").addAttribute("valves", "0").addAttribute("doors", "0").addAttribute("presencia", "0");
                            }// end if si es la estancia que quiero
                        }// end for node2
                    }// end if es la planta que busco
                }// end if si es una planta
            }// end for

            //String auxiliar = doc.asXML();
            //FileWriter archivo;
            //archivo = new FileWriter(principal.archivo);

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");

            //actualizamos el archivo
            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }// end inicializarEstancia

   
    
    /**
     * Metodo para eliminar una planta del xml
     * @param planta
     */
    public static void eliminarPlantaXML(String planta) {
        try {

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");
            //xmlReader.setEncoding("iso-8859-1");
       
            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                // si es la planta que busco
                System.out.println(node.valueOf("@alias") + " " +planta );
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node.getParent().remove(node);

                    // ++++++++++++++ MENSAJE ALERTA
                    String mostrar = "Planta Eliminada";

                    //muestro una ventana de notificacion
                    JOptionPane.showMessageDialog(null, mostrar, "Información", 1);


                }// end if
            }// end for

            //String auxiliar = doc.asXML();
            //FileWriter archivo;
            //archivo = new FileWriter(principal.archivo);

            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            //actualziamos el archivo
            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }// end EliminarPlanta

    /**
     * Metodo que elimina una isntancia en el archvio xml
     * @param planta
     * @param estancia
     */
    public static void eliminarEstanciaXML(String planta, String estancia) {
        try {

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
             xmlReader.setEncoding("UTF-8");
            //xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2;

            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                // si es la planta que busco
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        // estoy en la estancia buscada
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node2.getParent().remove(node2);
                            // ++++++++++++++ MENSAJE ALERTA
                            String mostrar = "Estancia Eliminada";
                            JOptionPane.showMessageDialog(null, mostrar, "Información", 1);
                        }
                    }// end for2
                }// end if
            }// end for



            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            //actualziamos el archivo
            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }// end EliminarEstancia

    /**
     * Metodo que carga desde el xml las estancias
     * para mostrarla en la vista ModificarPlano
     */
    public static void mostrarEstanciasVistaModificarPlano() {
        try {
            //remueve los items anteriores
            ModificarPlano.DESPLEGABLE_ESTANCIA.removeAllItems();
            ModificarPlano.DESPLEGABLE_ESTANCIA.addItem("--Estancias--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(ModificarPlano.planta_seleccionada)) {
                    node2 = node;

                    //recorro sus nodos hijos
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals( "estancia") ) {
                            //lo agrego a la vista
                            ModificarPlano.DESPLEGABLE_ESTANCIA.addItem(node2.valueOf("@nombre"));
                        }
                    }

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }// end mostrarEstancias

    /**
     * Metodo que carga desde el xml las estancias
     * para mostrarla en la vista EstanciaCaracteristicas
     */
    public static void mostrarEstanciasVistaEstanciaCaracteristicas_NOT() {
        try {
            //remueve los items anteriores
            DispositivoNuevo.DESPLEGABLE_ESTANCIA.removeAllItems();
            DispositivoNuevo.DESPLEGABLE_ESTANCIA.addItem("--Estancias--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta") 
                        //&& node.valueOf("@alias").equals(DispositivoNuevo.planta_seleccionada)
                        
                        ) {
                    node2 = node;

                    //recorro sus nodos hijos
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia")) {
                            //lo agrego a la vista
                            DispositivoNuevo.DESPLEGABLE_ESTANCIA.addItem(node2.valueOf("@nombre"));
                        }
                    }

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }// end mostrarEstancias

    /**
     * Metodo que carga desde el xml las estancias
     * para mostrarla en la vista EliminarEstancia
     */
    public static void mostrarEstanciasVistaEliminarEstancia_NOT() {
        try {
            //remueve los items anteriores
            EstanciaEliminar.DESPLEGABLE_ESTANCIA.removeAllItems();
            EstanciaEliminar.DESPLEGABLE_ESTANCIA.addItem("--Estancias--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta")  && node.valueOf("@alias").equals(EstanciaEliminar.planta_seleccionada)) {
                    node2 = node;

                    //recorro sus nodos hijos
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia") ) {
                            //lo agrego a la vista
                            EstanciaEliminar.DESPLEGABLE_ESTANCIA.addItem(node2.valueOf("@nombre"));
                        }
                    }

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }// end mostrarEstancias

    /**
     * Metodo que carga desde el xml las estancias
     * para mostrarla en la vista EliminarDispositivos
     */
    public static void mostrarEstanciasVistaEliminarDispositivos_NOT() {
        try {
            //remueve los items anteriores
            DispositivoEliminar.DESPLEGABLE_ESTANCIA.removeAllItems();
            DispositivoEliminar.DESPLEGABLE_ESTANCIA.addItem("--Estancias--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta")
                       // && node.valueOf("@alias").equals(DispositivoEliminar.planta_seleccionada)
                        ) {
                    node2 = node;

                    //recorro sus nodos hijos
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia") ) {
                            //lo agrego a la vista
                            DispositivoEliminar.DESPLEGABLE_ESTANCIA.addItem(node2.valueOf("@nombre"));
                        }
                    }

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }// end mostrarEstancias

    /**
     * Metodo que modifica en el xml 
     * la instacia selecionada en la vista ModificarPlano.planta_seleccionada
     */
    public static void modificarPlanoXML() {
        try {
            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);
          
            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(ModificarPlano.planta_seleccionada)) {
                    node2 = node;
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia")
                                && node2.valueOf("@nombre").equals(ModificarPlano.estancia_seleccionada)) {

                            Attribute atributo1 = node2.attribute("imagen");
                            atributo1.setText(ModificarPlano.direcPlano);

                        }// end if estancia
                    }// end for node2
                }// end if planta
            }// end for node


            //volver a inicializar todas las cantidades a 0 y "no" en actuadores y sensores
            Acciones.inicializarEstancia(ModificarPlano.planta_seleccionada, ModificarPlano.estancia_seleccionada);

            ////String aux = doc.asXML();
            //FileWriter archivo;
            //archivo = new FileWriter(principal.archivo);

            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("iso-8859-1");
            //format.setEncoding("UTF-8");

            //actualziamos el archivo
            //XMLWriter writer = new XMLWriter(new FileWriter(principal.archivo));
            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }// end modificarPlano

    /**
     * Metodo que recorre el xml buscando la planta y la estancia para 
     * luego crear el correspondiente iconoy agregarlo en EliminarDispositivos.panel_plano
     * @param planta
     * @param estancia
     */
    public static void mostrarDispositivosVistaEliminarDispositivos_NOT(String planta, String estancia) {
        try {
            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;
            Element node3 = null;
            Element labels = null;
            ImageIcon iconito = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;

                    //recorro los hijos de planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        //si es la estancia que estoy buscando
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node3 = node2;

                            // recorro los hijos de estancia
                            for (Iterator ii = node3.elementIterator(); ii.hasNext();) {
                                node3 = (Element) ii.next();

                                //si encuento el nodo iconos
                                if (node3.getName().equals("iconos")) {
                                    labels = node3;

                                    // recorro los hijos de iconos
                                    for (Iterator il = labels.elementIterator(); il.hasNext();) {
                                        labels = (Element) il.next();

                                        if (labels.getName().equals("label")) {
                                            String tipo = labels.valueOf("@tipo");
                                            String x = labels.valueOf("@x");
                                            String y = labels.valueOf("@y");
                                            String ancho = labels.valueOf("@ancho");
                                            String largo = labels.valueOf("@largo");
                                            String id = labels.valueOf("@id");


                                            //crea una amigen en memoria dependiendo del nodo
                                            String[][] tipoIcono = {
                                                new String[]{"puerta", "puc.png"},
                                                new String[]{"movimiento", "prdetec.png"},
                                                new String[]{"temperatura", "term.png"},
                                                new String[]{"combinado", "comb.png"},
                                                new String[]{"inundacion", "inund.png"},
                                                new String[]{"contadores", "cont.png"},
                                                new String[]{"regulacion", "luzreg.png"},
                                                new String[]{"conmutacion", "bca.png"},
                                                new String[]{"persianas", "pa.png"},
                                                new String[]{"electrovalvula", "elv.png"},};

                                            for (String[] icono : tipoIcono) {
                                                if (tipo.equals(icono[0])) {
                                                    String path = SkoaMain.ROOT_ICONS_PATH + icono[1];
                                                    iconito = new ImageIcon(Acciones.class.getClass().getResource(path));
                                                    break;
                                                }
                                            }


                                            //crea una etiqeuta y le agrega el icono creado anteriormente
                                            JLabel insertar = new JLabel();
                                            insertar.setName(tipo);

                                            insertar.setIcon(iconito);
                                            insertar.setBounds(Integer.parseInt(x), Integer.parseInt(y), 30, 40);

                                            //lo inserta en la vista principal
                                            DispositivoEliminar.panel_plano.add(insertar, javax.swing.JLayeredPane.DRAG_LAYER);

                                            final JButton boton1 = new JButton();

                                            String nombre = "boton+" + id;
                                            boton1.setName(nombre);


                                            Icon imagen = new ImageIcon(Acciones.class.getClass().getResource(SkoaMain.ROOT_ICONS_PATH + "eliminar.JPG"));



                                            boton1.setIcon(imagen);
                                            // boton1.setBounds(Integer.parseInt(x)+Integer.parseInt(ancho),
                                            // Integer.parseInt(y)+10,
                                            // 20, 20);
                                            // Puse + 30 pq antes el
                                            // ancho valia 30, ahora ya
                                            // no, por eso la sentencia
                                            // de arriba ya no vale
                                            boton1.setBounds(
                                                    Integer.parseInt(x) + 30,
                                                    Integer.parseInt(y) + 10,
                                                    20, 20);

                                            DispositivoEliminar.panel_plano.add(boton1, javax.swing.JLayeredPane.DRAG_LAYER);
                                            boton1.addActionListener(new ActionListener() {
                                                // Acción asociada
                                                // cuando se pulsa en la
                                                // X roja para eliminar
                                                // un dispositivo

                                                @Override
                                                public void actionPerformed(
                                                        ActionEvent evt) {
                                                    // Obtenemos el id.
                                                    // del act/sen que
                                                    // va a ser
                                                    // eliminado
                                                    String identif = boton1.getName().substring(
                                                            6);
                                                    // Obtenemos su tipo
                                                    String tipo = buscaTipo(identif);
                                                    // Y el número de
                                                    // elemento dentro
                                                    // de la estancia
                                                    // (1,2,3,4,5)
                                                   /*int num = buscaNum(
                                                            identif,
                                                            tipo,
                                                            DispositivoEliminar.planta_seleccionada,
                                                            DispositivoEliminar.estancia_seleccionada
                                                            );*/
                                                   
                                                    // Necesitamos
                                                    // actualizar los
                                                    // nombres de
                                                    // "tipo", ya que la
                                                    // sig. función
                                                    // requiere nombres
                                                    // distintos
                                                    if (tipo.indexOf("regulacion") != -1) {
                                                        tipo = "luz_reg";
                                                    }
                                                    if (tipo.indexOf("conmutacion") != -1) {
                                                        tipo = "luz_conm";
                                                    }
                                                    if (tipo.indexOf("persianas") != -1) {
                                                        tipo = "pers";
                                                    }
                                                    if (tipo.indexOf("electrovalvula") != -1) {
                                                        tipo = "electrov";
                                                    }
                                                    if (tipo.indexOf("movimiento") != -1) {
                                                        tipo = "mov";
                                                    }
                                                    if (tipo.indexOf("temperatura") != -1) {
                                                        tipo = "temp";
                                                    }
                                                    if (tipo.indexOf("combinado") != -1) {
                                                        tipo = "comb";
                                                    }
                                                    if (tipo.indexOf("inundacion") != -1) {
                                                        tipo = "inund";
                                                    }
                                                    if (tipo.indexOf("contadores") != -1) {
                                                        tipo = "cont";
                                                    }

                                                    // Actualizamos los
                                                    // vectores
                                                    // vecDispUsados y
                                                    // vecDispDomoticos
                                                   /* SkoaMain.dispositivo_eliminado(
                                                            DispositivoEliminar.planta_seleccionada,
                                                            DispositivoEliminar.estancia_seleccionada,
                                                            tipo,
                                                            String.valueOf(num));*/
                                                    // Por último, se
                                                    // elimina el
                                                    // dispositivo
                                                    // adecuado en el
                                                    // fichero .xml
                                                    Acciones.eliminarDispositivoVistaEliminarDispositivos(identif);
                                                }
                                            });
                                        }


                                    }

                                }

                            }
                        }
                    }
                }

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que recorre el xml buscando la planta y la estancia para 
     * luego crear el correspondiente iconoy agregarlo en EstanciaCaracteristicas.panel_plano
     * @param planta
     * @param estancia
     */
    public static void mostrarDispositivosVistaEstanciaCaracteristicas_NOT(String planta, String estancia) {
        try {
            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2 = null;
            Element node3 = null;
            Element labels = null;
            ImageIcon iconito = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;

                    //recorro los hijos de planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        //si es la estancia que estoy buscando
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node3 = node2;

                            // recorro los hijos de estancia
                            for (Iterator ii = node3.elementIterator(); ii.hasNext();) {
                                node3 = (Element) ii.next();

                                //si encuento el nodo iconos
                                if (node3.getName().equals("iconos")) {
                                    labels = node3;

                                    // recorro los hijos de iconos
                                    for (Iterator il = labels.elementIterator(); il.hasNext();) {
                                        labels = (Element) il.next();

                                        if (labels.getName().equals("label")) {
                                            String tipo = labels.valueOf("@tipo");
                                            String x = labels.valueOf("@x");
                                            String y = labels.valueOf("@y");
                                            String ancho = labels.valueOf("@ancho");
                                            String largo = labels.valueOf("@largo");
                                            String id = labels.valueOf("@id");


                                            //crea una amigen en memoria dependiendo del nodo
                                            String[][] tipoIcono = {
                                                new String[]{"puerta", "puc.png"},
                                                new String[]{"movimiento", "prdetec.png"},
                                                new String[]{"temperatura", "term.png"},
                                                new String[]{"combinado", "comb.png"},
                                                new String[]{"inundacion", "inund.png"},
                                                new String[]{"contadores", "cont.png"},
                                                new String[]{"regulacion", "luzreg.png"},
                                                new String[]{"conmutacion", "bca.png"},
                                                new String[]{"persianas", "pa.png"},
                                                new String[]{"electrovalvula", "elv.png"},};

                                            for (String[] icono : tipoIcono) {
                                                if (tipo.equals(icono[0])) {
                                                    String path = SkoaMain.ROOT_ICONS_PATH + icono[1];
                                                    iconito = new ImageIcon(Acciones.class.getClass().getResource(path));
                                                    break;
                                                }
                                            }


                                            //crea una etiqeuta y le agrega el icono creado anteriormente
                                            JLabel insertar = new JLabel();
                                            insertar.setName(tipo);

                                            insertar.setIcon(iconito);
                                            insertar.setBounds(Integer.parseInt(x), Integer.parseInt(y), 30, 40);

                                            //lo inserta en la vista principal
                                            DispositivoNuevo.panel_plano.add(insertar, javax.swing.JLayeredPane.DRAG_LAYER);

                                        }

                                    }

                                }

                            }
                        }
                    }
                }

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Meotod para eliminar un dispositivo en la vista
     * @param identificador
     */
    public static void eliminarDispositivoVistaEliminarDispositivos(String identificador) {
        try {
            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            //xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2, node3, node4 = null;
            String planta, estancia;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es un nodo planta
                if (node.getName().equals("planta")) {
                    planta = node.valueOf("@alias");
                    node2 = node;

                    //recorremos los hijso de ese nodo planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {

                        node2 = (Element) ia.next();

                        //si es un nodo estancia
                        if (node2.getName().equals("estancia")) {
                            estancia = node2.valueOf("@nombre");
                            node3 = node2;
                            int eliminado = 0;
                            String tipo = "";

                            //recorremos los hijso de ese nodo estancia
                            for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                node3 = (Element) ib.next();

                                if (node3.getName().equals("iconos")) {
                                    node4 = node3;

                                    for (Iterator ic = node4.elementIterator(); ic.hasNext();) {
                                        node4 = (Element) ic.next();

                                        if (node4.getName().equals("label")) {

                                            String ident = node4.valueOf("@id");
                                            if (ident.equals(identificador)) {
                                                //identificador localizado, se borra
                                                tipo = node4.valueOf("@tipo");
                                                //System.out.println("tipo:"+tipo);
                                                node4.getParent().remove(node4);
                                                //doc2= 
                                                inicializar(planta, estancia, tipo, doc);

                                                //eliminado=1;
                                                //break;
                                            }
                                        }
                                    }//end for


                                    if (eliminado == 1) {
                                        if (node3.getName().equals("cantidad")) {
                                            //System.out.println("tipo:"+tipo);
                                            String[][] fieldsToInclude = {
                                                new String[]{"regulacion", "@lucreg", "lucreg"},
                                                new String[]{"conmutacion", "@bombs", "bombs"},
                                                new String[]{"persianas", "@blinds", "blinds"},
                                                new String[]{"electrovalvula", "@valves", "valves"},
                                                new String[]{"puerta", "@doors", "doors"},
                                                new String[]{"movimiento", "@presencia", "presencia"},};

                                            for (String[] field : fieldsToInclude) {
                                                if (tipo.equals(field[0])) {

                                                    //restar una
                                                    int cuantas = Integer.parseInt(node3.valueOf(field[1]));
                                                    cuantas = cuantas - 1;
                                                    //System.out.println("cuantas:"+cuantas);
                                                    Attribute atributo1 = node3.attribute(field[2]);
                                                    atributo1.setText(String.valueOf(cuantas));
                                                }
                                            }
                                        }
                                    }

                                }
                            }//end for	    				 
                        }
                    }//end for
                }
            }//end for


            //++++++++++++++       MENSAJE ALERTA 
            

            /*Component[] componentes = DispositivoEliminar.panel_plano.getComponents();
            for (int indice = 0; indice < componentes.length; indice++) {
                String name = componentes[indice].getName();

                if (name != null) {
                    if (name.equals("regulacion")
                            || name.equals("conmutacion")
                            || name.equals("persianas")
                            || name.equals("electrovalvula")
                            || name.equals("puerta")
                            || name.equals("movimiento")
                            || name.equals("temperatura")
                            || name.equals("combinado")
                            || name.equals("inundacion")
                            || name.equals("contadores")
                            || name.substring(0, 5).equals("boton")) {
                        DispositivoEliminar.panel_plano.remove(componentes[indice]);
                        DispositivoEliminar.panel_plano.moveToBack(componentes[indice]);
                    } //end if
                }
            }//end for*/
            //Acciones.mostrarDispositivosVistaEliminarDispositivos_NOT(DispositivoEliminar.planta_estancia, DispositivoEliminar.nombre_estancia);


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    } // fin del procedimiento Eliminar Dispositivo

    /**
     * Metodo para inicializar los nodos dentro del arbol del xml
     * @param planta
     * @param estancia
     * @param tipo
     * @param doc
     * @throws IOException
     */
    public static void inicializar(String planta, String estancia, String tipo, Document doc) throws IOException {
        try {

            //cargamos el nodo vivienda
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2, node3, node4 = null;

            int cero = 0;

            //recoremos sus hijos
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si  es el nodo que estamos buscando
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        //si es la estancia que estamos buscando
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node3 = node2;

                            //recorremos sus hijos
                            for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                node3 = (Element) ib.next();

                                if (node3.getName().equals("cantidad")) {
                                    String[][] fieldsToInclude = {
                                        new String[]{"regulacion", "@lucreg", "lucreg"},
                                        new String[]{"conmutacion", "@bombs", "bombs"},
                                        new String[]{"persianas", "@blinds", "blinds"},
                                        new String[]{"electrovalvula", "@valves", "valves"},
                                        new String[]{"puerta", "@doors", "doors"},
                                        new String[]{"movimiento", "@presencia", "presencia"},};

                                    for (String[] field : fieldsToInclude) {
                                        if (tipo.equals(field[0])) {
                                            int cuantas = Integer.parseInt(node3.valueOf(field[1])) - 1;


                                            if (cuantas == 0) {
                                                cero = 1;
                                            }


                                            Attribute atributo1 = node3.attribute(field[2]);
                                            atributo1.setText(String.valueOf(cuantas));
                                        }
                                    }
                                }// end if Cantidad

                            }// end for dentro estancias

                        }
                    }
                }
            }



            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

            // si el contador llego a 0 hay que poner NO en actuadores/sensores
            if (cero == 1) {
                inicializar2(planta, estancia, tipo);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }// end inicializar

    /**
     * Inicializa los componente dentro del xml en "no"
     * @param planta
     * @param estancia
     * @param tipo
     */
    public static void inicializar2(String planta, String estancia, String tipo) {

        try {
            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para lerr el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2, node3, node4 = null;


            String buscar = "";
            if (tipo.equals("regulacion") || tipo.equals("conmutacion")
                    || tipo.equals("persianas")
                    || tipo.equals("electrovalvula")) {
                buscar = "actuadores";

            }

            if (tipo.equals("puerta") || tipo.equals("movimiento")
                    || tipo.equals("temperatura") || tipo.equals("combinado")
                    || tipo.equals("inundacion") || tipo.equals("contadores")) {

                buscar = "sensores";

            }

            //recoremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;

                    //recorremos el nodo estancia planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        // dentro de la estancia que busco
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node3 = node2;

                            //recorremos el nodo estancia
                            for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                node3 = (Element) ib.next();

                                // si es lo que estoy buscando (sensores/actuadores)
                                if (node3.getName().equals(buscar)) {
                                    String[] fieldsToInclude = {
                                        "regulacion", "conmutacion", "persianas",
                                        "electrovalvula", "puerta", "movimiento", "temperatura",
                                        "combinado", "inundacion", "contadores"
                                    };

                                    for (String field : fieldsToInclude) {

                                        if (tipo.equals("regulacion") ) {
                                            Attribute atributo = node3.attribute(field);
                                            if(atributo != null )atributo.setText("no");
                                        }
                                    }


                                }// end for dentro estancias
                            }
                        }
                    }
                }
            }// end for



            OutputFormat format = OutputFormat.createPrettyPrint();
            // format.setEncoding("UTF-8");
            format.setEncoding("iso-8859-1");

            //actualizamos el xml
            XMLWriter writer = new XMLWriter(new FileWriter(NuevoProyecto.archivo), format);
            writer.write(doc);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }// end inicializar2

    /**
     * Emtodo que busca el tipo de act/sen gracias a su identificador
     * @param identificador El id. del act/sen del cual se quiere obtener su
     * @return el tipo del identificador
     */
    public static String buscaTipo(String identificador) {
        try {


            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para lerr el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2, node3, node4 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es un nodo planta
                if (node.getName().equals("planta")) {
                    node2 = node;

                    //recorremos el nodo planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        //si es un nodo estancia
                        if (node2.getName().equals("estancia")) {
                            node3 = node2;

                            //recorremos el nodo estancia
                            for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                node3 = (Element) ib.next();

                                //si es un nodo iconos
                                if (node3.getName().equals("iconos")) {
                                    node4 = node3;

                                    //recorremo el nodo iconos
                                    for (Iterator ic = node4.elementIterator(); ic.hasNext();) {
                                        node4 = (Element) ic.next();

                                        //si es un nodo label
                                        if (node4.getName().equals("label")) {

                                            String ident = node4.valueOf("@id");
                                            if (ident.equals(identificador)) {
                                                // identificador localizado, se
                                                // devuelve su tipo
                                                return node4.valueOf("@tipo");
                                            }
                                        }
                                    }// end for
                                }
                            }// end for
                        }
                    }// end for
                }
            }// end for principal

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Si llegado a este punto no ha encontrado el identificador (no
        // debería), se devuelve la ristra vacía ""
        return "";
    } // fin del procedimiento BuscaTipo

    /**
     * Procedimiento que devuelve el num. de dispositivo que va a ser eliminado
     * gracias a su identificador, su tipo, y la planta y estancia en la que se
     * encuentra Es decir, saber si se trata de la 3ª luz, la 2ª, la 4ª, etc
     * (por ej)
     * @param identificador El id. del act/sen del cual se quiere saber el numero
     * @param tipo El tipo del act/sen del cual se quiere saber el numero
     * @param planta La planta en la que se encuentra el act/sen
     * @param estancia La estancia en la que se encuentra el act/sen
     */
    public static int buscaNum(String identificador, String tipo, String planta, String estancia) {
        // contador será el valor devuelto por la función
        int contador = 0;
        try {
            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para lerr el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");
            Element node2, node3, node4 = null;


            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                // si es la plnata buscada
                if (node.getName().equals("planta") && node.valueOf("@alias").equals(planta)) {
                    node2 = node;

                    //recorremos el nodo planta
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        //si es la estancia buscada estancia
                        if (node2.getName().equals("estancia") && node2.valueOf("@nombre").equals(estancia)) {
                            node3 = node2;

                            for (Iterator ib = node3.elementIterator(); ib.hasNext();) {
                                node3 = (Element) ib.next();

                                if (node3.getName().equals("iconos")) {
                                    node4 = node3;

                                    for (Iterator ic = node4.elementIterator(); ic.hasNext();) {
                                        node4 = (Element) ic.next();


                                        if (node4.getName().equals("label")) {
                                            String tipo_aux = node4.valueOf("@tipo");
                                            String ident = node4.valueOf("@id");

                                            // Una vez que estamos situados en la planta y
                                            // estancia correcta Si nos encontramos un
                                            // tipo como el que estamos buscando, incrementamos el contador
                                            if (tipo_aux.equals(tipo)) {
                                                contador++;
                                            }

                                            // Si hemos dado con el identificador, ya no
                                            // necesitamos contar más
                                            if (ident.equals(identificador)) {
                                                // identificador localizado, se devuelve su tipo
                                                return contador;
                                            }
                                        }
                                    }// end for
                                }
                            }// end for
                        }
                    }
                }// end for
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Si llegados a este punto, no ha encontrado el identificador que
        // buscaba (no debería), devuelve 0
        return contador;
    } // fin del procedimiento BuscaTipo

    /**
     * 
     * @param vecEstancias
     * @param vectpPlantas
     * @param numPlantas
     */
    public static void rellenaVect(EstanciaGenerica[] vecEstancias,javax.swing.JTabbedPane[] vectpPlantas, int[] numPlantas) {

        try {

            for (int iii = 0; iii < SkoaMain.vecAliasPlantas.length; iii++) {

                //referencia al archivo
                File aFile = new File(NuevoProyecto.archivo);

                //referencia para lerr el arbol xml
                SAXReader xmlReader = new SAXReader();
                // xmlReader.setEncoding("UTF-8");
                xmlReader.setEncoding("iso-8859-1");

                //cargamos la raiz
                Document doc = xmlReader.read(aFile);
                Element node = (Element) doc.selectSingleNode("//vivienda");
                Element node2, node3, node4 = null;

                int est_tratadas = 0, plantas = 0;

                //recorremos el arbol
                for (Iterator i = node.elementIterator(); i.hasNext();) {
                    node = (Element) i.next();

                    //si es un nodo planta
                    if (node.getName().equals("planta")) {
                        numPlantas[0]++;
                        String nombre_planta = node.valueOf("@alias");

                        //System.out.println("nombre planta encontrado:"+ nombre_planta);

                        vectpPlantas[plantas].setName(nombre_planta);
                        plantas++;

                        node2 = node;
                        int estancias = 0;

                        // recorremos el nodo planta
                        for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                            node2 = (Element) ia.next();

                            //si es un nodo estancia
                            if (node2.getName().equals("estancia")) {
                                String nombre_estancia = node2.valueOf("@nombre");
                                vecEstancias[est_tratadas].setName(nombre_estancia);
                                est_tratadas++;
                                estancias++;
                            }// end if estancias
                        }// end for estancias

                        while (estancias < 25) {
                            vecEstancias[est_tratadas].setName("");

                            est_tratadas++;
                            estancias++;
                        }
                    }// end if planta
                }// end for plantas

            }// end for

        }// end try
        catch (DocumentException e) {
            e.printStackTrace();
        }

        //System.out.println("antes de salir num plantas=" + numPlantas[0]);
    } // end function




    /**
    * Funciones no utilizadas
    */
    public static void mostrarPlantasVistaModificarPlano_NOT() {
        try {
            //remueve los items anteriores
            ModificarPlano.DESPLEGABLE_PLANTA.removeAllItems();
            ModificarPlano.DESPLEGABLE_PLANTA.addItem("--Plantas--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");
            // xmlReader.setEncoding("is-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si encontramos la planta y su alias no esta vacia
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {
                    // estancia_nueva.desplegable.addItem(aux);

                    //llamada a la vista apra msotrar la planta
                    ModificarPlano.DESPLEGABLE_PLANTA.addItem(node.valueOf("@alias"));
                }
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public static void mostrarPlantasVistaEstanciaCaracteristicas_NOT() {
        try {
            //remueve los items anteriores
            DispositivoNuevo.DESPLEGABLE_PLANTA.removeAllItems();
            DispositivoNuevo.DESPLEGABLE_PLANTA.addItem("--Plantas--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            // //************************************
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //verificamos que sea la oanta y que su alias no este vacio
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {

                    //llamamos la vista para mostralo
                    DispositivoNuevo.DESPLEGABLE_PLANTA.addItem(node.valueOf("@alias"));
                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public static void mostrarPlantasVistaEliminarPlanta_NOT() {
        try {

            //remueve los items anteriores
            PlantaEliminar.DESPLEGABLE_PLANTA.removeAllItems();
            PlantaEliminar.DESPLEGABLE_PLANTA.addItem("--Plantas--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //verificamos que sea la oanta y que su alias no este vacio
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {

                    //llamamos la vista para mostralo
                    PlantaEliminar.DESPLEGABLE_PLANTA.addItem(node.valueOf("@alias"));

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public static void mostrarPlantasVistaEliminarEstancia_NOT() {
        try {

            //remueve los items anteriores
            //EliminarEstancia.DESPLEGABLE_PLANTA.removeAllItems();
            //EliminarEstancia.DESPLEGABLE_PLANTA.addItem("--Plantas--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //verificamos que sea la oanta y que su alias no este vacio
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {

                    //llamamos la vista para mostralo
                   // EliminarEstancia.DESPLEGABLE_PLANTA.addItem(node.valueOf("@alias"));

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    public static void mostrarPlantasVistaEliminarDispositivos_NOT() {
        try {

            //remueve los items anteriores
            DispositivoEliminar.DESPLEGABLE_PLANTA.removeAllItems();
            DispositivoEliminar.DESPLEGABLE_PLANTA.addItem("--Plantas--");

            //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            // xmlReader.setEncoding("UTF-8");
            xmlReader.setEncoding("iso-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //verificamos que sea la oanta y que su alias no este vacio
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {

                    //llamamos la vista para mostralo
                    DispositivoEliminar.DESPLEGABLE_PLANTA.addItem(node.valueOf("@alias"));

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
