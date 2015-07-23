/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


package skoa.views;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DragMouseAdapter extends MouseAdapter {

    JLabel insertar = new JLabel();
    int posx;//= EstanciaCaracteristicas.panel_plano.getX();
    int posy;//= EstanciaCaracteristicas.panel_plano.getY();
    int posYpanel;
    int derecha = 0;
    String labelPulsado = "";
    int anchoPanel;
    int altoPanel;
    static String nombreLabel = "";

    public void mousePressed(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();

        if (c.getName().equals("luzreg")) {
            nombreLabel = "regulacion";
            labelPulsado = "luzreg";
            //  System.out.println("luz regulable pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"bra.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_luz_reg.getY();
            anchoPanel = DispositivoNuevo.panel_luz_reg.getWidth();

        }//end if


        if (c.getName().equals("luzcon")) {
            nombreLabel = "conmutacion";
            labelPulsado = "luzcon";
            //  System.out.println("luz conmutable pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"bca.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));


            posYpanel = DispositivoNuevo.panel_luz_con.getY();
            anchoPanel = DispositivoNuevo.panel_luz_con.getWidth();
        } //end if

        if (c.getName().equals("persianas")) {
            nombreLabel = "persianas";
            labelPulsado = "persianas";
            //  System.out.println("persianas pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"pa.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_persianas.getY();
            anchoPanel = DispositivoNuevo.panel_persianas.getWidth();
        } //end if

        if (c.getName().equals("electrovalvula")) {
            nombreLabel = "electrovalvula";
            labelPulsado = "electrovalvula";
            //  System.out.println("electrovalvula pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"elv.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_electrovalvula.getY();
            anchoPanel = DispositivoNuevo.panel_electrovalvula.getWidth();
        } //end if

        //----------------Grupo de la derecha---------------
        if (c.getName().equals("apertura")) {
            nombreLabel = "puerta";
            labelPulsado = "apertura";
            // System.out.println("apertura pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"puc.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_apertura.getY();
            anchoPanel = DispositivoNuevo.panel_apertura.getWidth();
            derecha = 1;
        } //end if

        if (c.getName().equals("movimiento")) {
            nombreLabel = "movimiento";
            labelPulsado = "movimiento";
            // System.out.println("apertura pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"prdetec.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));


            posYpanel = DispositivoNuevo.panel_movimiento.getY();
            anchoPanel = DispositivoNuevo.panel_movimiento.getWidth();
            derecha = 1;
        } //end if

        if (c.getName().equals("temperatura")) {
            nombreLabel = "temperatura";
            labelPulsado = "temperatura";
            // System.out.println("apertura pulsada");


            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(SkoaMain.ROOT_ICONS_PATH + "term.png"));


            posYpanel = DispositivoNuevo.panel_temperatura.getY();
            anchoPanel = DispositivoNuevo.panel_temperatura.getWidth();
            derecha = 1;
        } //end if

        if (c.getName().equals("combinado")) {
            nombreLabel = "combinado";
            labelPulsado = "combinado";
            // System.out.println("apertura pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"comb.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_combinado.getY();
            anchoPanel = DispositivoNuevo.panel_combinado.getWidth();
            derecha = 1;
        } //end if

        if (c.getName().equals("inundacion")) {
            nombreLabel = "inundacion";
            labelPulsado = "inundacion";
            // System.out.println("apertura pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"inund.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_inundacion.getY();
            anchoPanel = DispositivoNuevo.panel_inundacion.getWidth();
            derecha = 1;
        } //end if

        if (c.getName().equals("contadores")) {
            nombreLabel = "contadores";
            labelPulsado = "contadores";
            // System.out.println("apertura pulsada");

            String path = SkoaMain.ROOT_ICONS_PATH+"cont.png";
            DispositivoNuevo.iconito = new ImageIcon(getClass().getResource(path));

            posYpanel = DispositivoNuevo.panel_contadores.getY();
            anchoPanel = DispositivoNuevo.panel_contadores.getWidth();
            derecha = 1;
        } //end if



    } //end mousepressed

    public void mouseReleased(MouseEvent e) {
        if (DispositivoNuevo.frame1 == 1) {

            JComponent c = (JComponent) e.getSource();
            posx = DispositivoNuevo.panel_plano.getX();
            posy = DispositivoNuevo.panel_plano.getY();
            int x = 0;
            int y = 0;
            int aux1 = DispositivoNuevo.panel_plano.getWidth();
            int aux2 = DispositivoNuevo.plano.getWidth();
            int plano_huecoAncho = (aux1 - aux2) / 2;
            aux1 = DispositivoNuevo.panel_plano.getHeight();
            aux2 = DispositivoNuevo.plano.getHeight();
            int plano_huecoAlto = (aux1 - aux2) / 2;
            String tipo = ""; // Nos indicará el tipo de icono que estamos insertando
            int opcion = 0; // Nos indicará el tipo de icono que salió elegido (para usar con el switch)
            int numero = 0; // El número que se pasará a la llamada InsertarActSen.main()

            if (derecha == 0) { //Caso grupo izquierda
                x = e.getX() - anchoPanel - (posx / 4);
                y = e.getY() + posYpanel; //+ posYpanel - posy ;
            } else //Caso grupo derecha
            if (derecha == 1) {
                x = e.getX() + DispositivoNuevo.panel_plano.getWidth() + 20;
                y = e.getY() + posYpanel - 5;
                derecha = 0;
            }

            if (DispositivoNuevo.DESPLEGABLE_PLANTA.getSelectedIndex() == 0 ||
                    DispositivoNuevo.DESPLEGABLE_ESTANCIA.getSelectedIndex() == 0 ) {
                String mostrar = "¡Seleccione planta y estancia!";
//		    	mostrar += "\n";
//		    	mostrar += "No se insertara";
                JOptionPane.showMessageDialog(null, mostrar, "alerta", 1);
            
            
            } else {
                if (x < 0 || x > DispositivoNuevo.panel_plano.getWidth() - 20
                        || y > 461 || y < 58) {
                    String mostrar = "¡Icono situado fuera del plano!";
                    mostrar += "\n";
                    mostrar += "No se insertará";
                    JOptionPane.showMessageDialog(null, mostrar, "alerta", 1);
                } else {
                    // Activamos el botón de "Borrar Todo" desde que se inserta algún actuador/sensor
                    DispositivoNuevo.botonBorrarTodo.setEnabled(true);

                    // A continuación se averigüará cual es el tipo del icono que se va a insertar
                    String ico = DispositivoNuevo.iconito.toString();
                    // Y dependiendo de cual sea, lo indicamos en la variable tipo
                    if (ico.indexOf("bra.png") != -1) {
                        tipo = "luz_reg";
                        opcion = 1;
                    }
                    if (ico.indexOf("bca.png") != -1) {
                        tipo = "luz_conm";
                        opcion = 2;
                    }
                    if (ico.indexOf("pa.png") != -1) {
                        tipo = "pers";
                        opcion = 3;
                    }
                    if (ico.indexOf("elv.png") != -1) {
                        tipo = "electrov";
                        opcion = 4;
                    }
                    if (ico.indexOf("puc.png") != -1) {
                        tipo = "puerta";
                        opcion = 5;
                    }
                    if (ico.indexOf("prdetec.png") != -1) {
                        tipo = "mov";
                        opcion = 6;
                    }
                    if (ico.indexOf("term.png") != -1) {
                        tipo = "temp";
                        opcion = 7;
                    }
                    if (ico.indexOf("comb.png") != -1) {
                        tipo = "comb";
                        opcion = 8;
                    }
                    if (ico.indexOf("inund.png") != -1) {
                        tipo = "inund";
                        opcion = 9;
                    }
                    if (ico.indexOf("cont.png") != -1) {
                        tipo = "cont";
                        opcion = 10;
                    }

                    // Ya conocemos los parámetros para la llamada a InsertarActSen: las posiciones x e y, 
                    // el nombre de la planta, de la estancia, el tipo y la variable opción (nos servirá para saber el número)
                    // Llamamos a InsertarActSen para que el usuario introduzca los parámetros del actuador/sensor insertado (direc. grupo, tipo, subtipo y nombre)
                    InsertarActSen.main(Integer.toString(x), Integer.toString(y),
                            DispositivoNuevo.planta_seleccionada, DispositivoNuevo.estancia_seleccionada, tipo, opcion);
                }
            }
        }
    }//end mouseReleased
}//end class

