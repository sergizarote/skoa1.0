/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import skoa.models.Dispositivo;



public class Arbol {

 
    /**
     * Metodo para generar el arbol xml
     */
    public static JTree generarArbol() {
        try {

            ConfiguracionProyecto.MOSTRAR_CONFIGURACION.removeAll();
            
            //referencia al archivo
            File aFile = new File(NuevoProyecto.archivo);
            
            //referencia para leer el arbol
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");
            //xmlReader.setEncoding("iso-8859-1");
            
            //referencia apra recorrer el arbol
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");



            Attribute atributo = node.attribute("name");
            String datos = atributo.getText();
            DefaultMutableTreeNode root = new DefaultMutableTreeNode(datos);
            JTree arbol = new javax.swing.JTree(root);

            //--------
            arbol.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            DefaultMutableTreeNode estancia, planta;

            Element node2;
            
            //recorremos el node principal
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                
                //si es una nodo tipo planta
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("") ) {
                   
                    planta = new DefaultMutableTreeNode(node.valueOf("@alias"));
                    root.add(planta);


                    node2 = node;
                    for (Iterator i2 = node2.elementIterator(); i2.hasNext();) {
                        node2 = (Element) i2.next();
                        if (node2.getName().equals("estancia")) {
                        

                            estancia = new DefaultMutableTreeNode(node2.valueOf("@nombre"));
                            planta.add(estancia);

                        }
                    }//end for
                }//end if


            }//end for


            expandAll(arbol, true);
            
            //ConfiguracionProyecto.MOSTRAR_CONFIGURACION = new  JScrollPane();
            
            
           
            ConfiguracionProyecto.MOSTRAR_CONFIGURACION.setViewportView(arbol);
            ConfiguracionProyecto.MOSTRAR_CONFIGURACION.invalidate();
            ConfiguracionProyecto.MOSTRAR_CONFIGURACION.repaint();
            return arbol;
        } catch (DocumentException e) {
            e.printStackTrace();
             
        }
        
        return null;



    }
    
     

  
    public static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }


    public static void generarDesplegable_NOT(String firstItem) {
        try {
            EstanciaNueva.DESPLEGABLE_PLANTA.removeAllItems();
            EstanciaNueva.DESPLEGABLE_PLANTA.addItem(firstItem );

            // estancia_nueva.nombre_estancia.setColumns(0);
            EstanciaNueva.NOMBRE_ESTANCIA.setCaretPosition(0);
            EstanciaNueva.NOMBRE_ESTANCIA.setText("");
            // estancia_nueva.nombre_estancia.setSelectionEnd(-1);
            // estancia_nueva.nombre_estancia.setSelectionStart(-1);

            EstanciaNueva.IMAGEN_ESTANCIA.setCaretPosition(0);
            EstanciaNueva.IMAGEN_ESTANCIA.setText("");

            File aFile = new File(NuevoProyecto.archivo);
            SAXReader xmlReader = new SAXReader();

            
            xmlReader.setEncoding("iso-8859-1");

            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            Attribute atributo = node.attribute("name");

            String datos = atributo.getText();//node.getName();

            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();
                if (node.getName().equals("planta") &&  !node.valueOf("@alias").equals("") ) {
             
                    
                    EstanciaNueva.DESPLEGABLE_PLANTA.addItem( node.valueOf("@alias"));
                    
                }//end if
            }//end for




        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }//end generar desplegable
    
    
     /**
     * Metodo que carga desde el xml las plantas
     * y las retorna en una Arralist
     */
     public static ArrayList<String> getPlantas() {
        ArrayList<String> listPlantas = new ArrayList<>();
         
         try {
             //referencia para leer el archivo
            File aFile = new File(NuevoProyecto.archivo);

            //referencia para leer el arbol xml
            SAXReader xmlReader = new SAXReader();
            xmlReader.setEncoding("UTF-8");
             //xmlReader.setEncoding("is-8859-1");

            //cargamos la raiz
            Document doc = xmlReader.read(aFile);
            Element node = (Element) doc.selectSingleNode("//vivienda");

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si encontramos la planta y su alias no esta vacia
                if (node.getName().equals("planta") && !node.valueOf("@alias").equals("")) {
                    listPlantas.add(node.valueOf("@alias"));

                }
            }// end for
         } catch (DocumentException e) {
            e.printStackTrace();
        }
        
        return listPlantas;
         
     }
    
     
    /**
      * Buscas las estancias dentro de una planta 
      * @param planta
      * @return 
      */
    public static ArrayList<String> getEstancias(String planta) {
         ArrayList<String> listEstancias = new ArrayList<>();
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
            Element node2 = null;

            //recorremos el arbol
            for (Iterator i = node.elementIterator(); i.hasNext();) {
                node = (Element) i.next();

                //si es la planta que estoy buscando
                if (node.getName().equals("planta")  && node.valueOf("@alias").equals(planta)) {
                    node2 = node;

                    //recorro sus nodos hijos
                    for (Iterator ia = node2.elementIterator(); ia.hasNext();) {
                        node2 = (Element) ia.next();

                        if (node2.getName().equals("estancia") ) {
                            listEstancias.add(node2.valueOf("@nombre"));
                        }
                    }

                }// end if
            }// end for

        } catch (DocumentException e) {
            e.printStackTrace();
        }
         
         return listEstancias;
    }// end mostrarEstancias
    
    
    public static ArrayList<Dispositivo> getDispositivos(String planta,String estancia) {
         ArrayList<Dispositivo> listDispositivos = new ArrayList<>();
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
                                                   
                                                    Dispositivo d = new Dispositivo(
                                                        id,
                                                        icono[0],
                                                        icono[1], 
                                                        Integer.parseInt(x), 
                                                        Integer.parseInt(y),
                                                        Integer.parseInt(ancho),
                                                        Integer.parseInt(largo) 
                                                    );
                                                    listDispositivos.add(d);
                                                    break;
                                                }
                                            }
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
         
         return listDispositivos;
    }
    
}