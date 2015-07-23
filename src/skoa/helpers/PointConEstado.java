/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */


/*
 * PointConEstado.java
 * Fichero empleado también en el proyecto de Mario
 */
package skoa.helpers;

/**
 * @author David Monné Chávez
 */
public class PointConEstado {

    /** Creates a new instance of PointConEstado */
    public PointConEstado() {
        this.punto = null;
        this.estado = "indefinido";
    }
    private String estado; // Conserva el valor de la propiedad estado
    private tuwien.auto.eibpoints.Point punto; // Conserva el valor de la propiedad tipo.

    /**
     * Getter para la propiedad estado
     * Devuelve el valor guardado, no lo solicita al bus
     * @return Valor de la propiedad estado.
     */
    synchronized public String getEstado() {
        return this.estado;
    }

    /**
     * Setter para la propiedad estado
     * @param estado Nuevo valor de la propiedad estado
     */
    synchronized public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Getter para la propiedad punto
     * @return Valor de la propiedad punto
     */
    public tuwien.auto.eibpoints.Point getPunto() {
        return this.punto;
    }

    /**
     * Setter para la propiedad punto
     * @param punto Nuevo valor de la propiedad punto
     */
    public void setPunto(tuwien.auto.eibpoints.Point punto) {
        this.punto = punto;
    }

    /**
     * Setter para la propiedad punto
     * @param nombre Valor de la propiedad _PointName del nuevo punto
     * @param direccionEIB Valor de la propiedad _Address del nuevo punto
     * @param tipoPrincipal Valor de la propiedad _PointTypeMajor del nuevo punto
     * @param tipoSecundario Valor de la propiedad _PointTypeMinor del nuevo punto
     */
    public void setPunto(String nombre,
            String direccionEIB,
            String tipoPrincipal, String tipoSecundario) {
        try {
            this.punto = new tuwien.auto.eibpoints.Point(nombre,
                    new tuwien.auto.eicl.struct.cemi.EIB_Address(direccionEIB),
                    tipoPrincipal, tipoSecundario);
        } catch (tuwien.auto.eicl.util.EICLException ex) {
            //System.out.println("SETPUNTO: error al crear el punto EIB");
            //System.out.println("SETPUNTO: " + ex.getMessage());
            //ex.printStackTrace();
        }
    }

    public void actualizarEstado(tuwien.auto.eicl.CEMI_Connection tunel) {
        this.setEstado("indefinido"); //si no se actualiza, queda indefinido

        if (tunel != null)//se envía un mensaje de lectura
        {
            try {
                //Se crea el encapsulador de mensajes con los major_type y minor_type del punto
                tuwien.auto.eibxlator.PointPDUXlator xlator;
                xlator = tuwien.auto.eibxlator.PDUXlatorList.getPointPDUXlator(punto.getMajorType(), punto.getMinorType());

                //Se indica que se va a encapsular un mensaje de lectura
                xlator.setServiceType(tuwien.auto.eibxlator.PointPDUXlator.A_GROUPVALUE_READ);

                //Se crea el mensaje de lectura, indicándole el tipo, dirección origen, dirección del punto y el encapsulado
                tuwien.auto.eicl.struct.cemi.CEMI_L_DATA data = new tuwien.auto.eicl.struct.cemi.CEMI_L_DATA(
                        (byte) tuwien.auto.eicl.struct.cemi.CEMI_L_DATA.MC_L_DATAREQ,
                        new tuwien.auto.eicl.struct.cemi.EIB_Address(), punto.getDeviceAddress()[0], xlator.getAPDUByteArray());
                //tuwien.auto.eicl.struct.cemi.CEMI_L_DATA.

                //Se envía la solicitud de respuesta y se espera por la confirmación
                tunel.sendFrame(data, tuwien.auto.eicl.CEMI_Connection.WAIT_FOR_CONFIRM);
                //El mensaje se recibe por el listener de tunel y actualiza el estado

            } // fin del try
            catch (tuwien.auto.eicl.util.EICLException ex) {
                //ex.printStackTrace();
                //A ocurrido alg�n eror en el env�o, no se sabe el estado en el que est�.
                //System.out.println("ACTUALIZARESTADO: " + this.punto.getDeviceName());
                //System.out.println("ACTUALIZARESTADO: error al enviar mensaje de pregunta de estado");
                //System.out.println("ACTUALIZARESTADO: " + ex.getMessage());
            }
        } // fin del if
    }

    /* Metodo para cambiar el estado del punto enviando un mensaje al bus EIB
     * El estado sólo se actualiza a indefinido en caso de que ocurra algún error
     * en el envío del mensaje o que no exista la conexión pasada por parámetro
     * @param tunel, conexión por la que se envía el mensaje
     * @param valor, dato que se quiere escribir en el point
     */
    public void cambiarEstado(tuwien.auto.eicl.CEMI_Connection tunel, String valor) {
        this.setEstado("indefinido"); // Si ocurre un error, queda en indefinido
        if (tunel != null)//se envía un mensaje de escritura
        {
            try {
                //Se crea el encapsulador de mensajes con los major_type y minor_type del punto
                tuwien.auto.eibxlator.PointPDUXlator xlator;
                xlator = tuwien.auto.eibxlator.PDUXlatorList.getPointPDUXlator(punto.getMajorType(), punto.getMinorType());

                //Se indica que se va a encapsular un mensaje de lectura
                xlator.setServiceType(tuwien.auto.eibxlator.PointPDUXlator.A_GROUPVALUE_WRITE);

                //Se añade el nuevo valor al encapsulado (codifica el valor)
                xlator.setASDUfromString(valor);

                //Se crea el mensaje de lectura, indicándole el tipo, dirección origen, dirección del punto y el encapsulado
                tuwien.auto.eicl.struct.cemi.CEMI_L_DATA data = new tuwien.auto.eicl.struct.cemi.CEMI_L_DATA(
                        (byte) tuwien.auto.eicl.struct.cemi.CEMI_L_DATA.MC_L_DATAREQ,
                        new tuwien.auto.eicl.struct.cemi.EIB_Address(), punto.getDeviceAddress()[0], xlator.getAPDUByteArray());

                //Se envía la solicitud de respuesta y se espera por la confirmación
                tunel.sendFrame(data, tuwien.auto.eicl.CEMI_Connection.WAIT_FOR_CONFIRM);
                //El mensaje se recibe por el listener de tunel y actualiza el estado.

            } // fin del try
            catch (tuwien.auto.eicl.util.EICLException ex) {
                //A ocurrido alg�n eror en el env�o, no se sabe el estado en el que est�.
                //System.out.println("CAMBIARESTADO: error al enviar mensaje de cambiar estado");
                //System.out.println("CAMBIARESTADO: " + ex.getMessage());
            }
        } // fin del if
    }
}
