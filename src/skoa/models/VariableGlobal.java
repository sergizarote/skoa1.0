
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.models;

// Para el uso de PointList()
import skoa.views.EstanciaGenerica;
import tuwien.auto.eibpoints.*;

/**
 *
 * @author
 */

/* Para poder simular las variables globales de C++, por ej (ya que Java no dispone de variables
 * globales, nos declaramos esta clase y declaramos en ellas las variables de tipo public y
 * static. De esta forma, otras clases pueden acceder a estas variables como 
 * "VariableGlobal.nombredevariable"
 */
public class VariableGlobal {

    private String Variable;
    public static int obteneratributo = 0; // alias de plantas
    //ESTEFANÍA
    //Índice para usarlo en los vectores vecDGS, vecDGEstancia y vecDGTipo
    //Índices para saber que opc_sensor sería. Estos son los valores de cada case de monitoriza_sensor()
    public static int indVDG = 0; // posicion de la DG, en todos los vectores.
    public static int cmov = 1;     // Los sensores de movimiento empiezan en 1, y acaban en 5
    public static int cpuertas = 6; // Las puertas empiezan en 6, y acaban en 10
    public static int cinun = 11;   // Los sensores de inundacion empiezan en 11, y acaban en 15
    public static int ctemp = 16;   // El sensor de temperatura es el 16
    public static int ccomb = 17;   // El sensor combinado es el 17
    public static int ccont = 18;   // El sensor contador es el 18
    public static int cluzcon = 19; // Las luces conmutables empiezan en 19 y acaban en 23.
    public static int cpers = 24; // Las persiandas empiezan en 24, y acaban en 28.
    //---------
    // Índices para utilizarlos con los vectores:
    public static int indVA = 0; // alias de plantas
    public static int indVN = 0; // nombres de estancias
    public static int indVI = 0; // imágenes de los planos
    public static int indVReg = 0; // actuadores: regulación
    public static int indVConm = 0; // actuadores: conmutación
    public static int indVPer = 0; // actuadores: persianas
    public static int indVElec = 0; // actuadores: electroválvulas
    public static int indVPuert = 0; // sensores: puertas
    public static int indVMov = 0; // sensores: movimiento
    public static int indVTemp = 0; // sensores: temperatura
    public static int indVComb = 0; // sensores: combinado
    public static int indVInund = 0; // sensores: inundación
    public static int indVCont = 0; // sensores: contadores   
    public static int num_estancias_p0 = 0; // número de estancias de la planta 0 (baja)
    public static int num_estancias_p1 = 0; // número de estancias de la planta 1 (alta)
    public static int indVNLR = 0; // Luces regulabes de cada estancia
    public static int indVNLC = 0; // Luces conmutables de cada estancia
    public static int indVNPers = 0; // Persianas de cada estancia
    public static int indVNPuer = 0; // Puertas de cada estancia
    public static int indVNE = 0; // Electroválvulas de cada estancia
    public static int indVNPres = 0; // Sensores de presencia de cada estancia
    public static int indVEje_x = 0; // Coordenada del eje x de los iconos
    public static int indVEje_y = 0; // Coordenada del eje y de los iconos
    public static int indVAncho = 0; // Anchura de los iconos
    public static int indVAlto = 0; // Altura de los iconos
    public static int indVIcono = 0; // Ruta de la imagen de los iconos
    public static int indDibujaIconos = 0; // Índice para utilizar con el procedimiento dibuja_iconos
    public static int numTotalIconos = 0; // Para saber el número total de iconos a dibujar
    public static int AuxVIcono = 0; // Misma forma de proceder hasta esta variable
    public static int cont_est = 0; // Variable encargada de llevar la cuenta del número de estancias de cada planta
    public static int indVecNumEst = 0; // Índice para el vector del número de estancias
    public static int comodin = 0; // Variable auxiliar para ser utilizada en la función "obten_atributo"
    public static int opcion = 0; // Variable para saber qué opción se elige en los ComboBox
    public static String tipo_actual = ""; // Variable que nos servirá para saber cuál es el tipo que se está tratanto a la hora de asignar los valores de las coordenadas de los iconos al vector    
    public static int ind_reg = 0; // Índices para ubicar las coordenadas del eje x,y, anchura y altura en los vectores correspondientes
    public static int ind_conm = 5;
    public static int ind_pers = 10;
    public static int ind_puert = 15;
    public static int ind_mov = 20;
    public static int ind_temp = 25;
    public static int ind_comb = 30;
    public static int ind_inund = 35;
    public static int ind_cont = 40;
    public static int ind_electr = 45;
    public static int ind_reg_aux = 5; // Nos servirán como variables auxiliares de las variables anteriores
    public static int ind_conm_aux = 10;
    public static int ind_pers_aux = 15;
    public static int ind_puert_aux = 20;
    public static int ind_mov_aux = 25;
    public static int ind_temp_aux = 30;
    public static int ind_comb_aux = 35;
    public static int ind_inund_aux = 40;
    public static int ind_cont_aux = 45;
    public static int ind_electr_aux = 50;
    public static int indVNIE = 0; // Para el índice del vector de nº de iconos por estancia
    public static int contador = 0; // Para saber el nº de iconos en cada estancia
    //public static int retardo = 0; // Utilizada en "LecturaPeriodica.java"
    public static int lectura_manual = 0; // Para saber si se está realizando una lectura manual de los contadores(0) o una lectura periódica(1)
    public static int desconexion = 0; // Nos servirá para detener la lectura periódica de los contadores en caso de que el usuario
    // pulse "desconectar bus KNX" sin haber desactivado previamente la lectura periódica
    // Ya que de lo contrario se producen fallos a través del bus KNX. Valor utilizado para ello= 8
    public static int primera_vez = 1; // Esta variable evitará que se escriban 2 espacios cuando se muestran las medidas de todos los contadores,
    // tanto en el log de los contadores como en el fichero de mediciones (1: activado, 0:desactivado)
    public static int codificacion = 1; // Variable que servirá para saber si es necesario cambiar la codificaición 
    // al fichero .xml antes de abrirlo (0: no es necesario, 1: sí es necesario)
    public static PointList Lcontadores = new PointList();
    public static int indVDispDom = 0; // Índice del vector que guarda diversa información de los dispositivos domóticos (planta,estancia,tipo,número,nombre)
    public static int indVDispUsad = 0; // Índice del vector que guarda información del número de dispositivos usados en cada estancia
    public static EstanciaGenerica estancia; // Estas 3 variables nos servirán para poder cambiar el estado de los iconos a la hora de monitorizar los sensores
    public static int coord = 0;            // Para la monitorización de los sensores
    public static int opc_sens = 0;
    public static int indVEstTim = 0;

    // Constructor por defecto. Inicializa la variable global
    public VariableGlobal() {
        Variable = new String();
        initVariable("off");
    }

    // Constructor que inicializa la variable global a un valor determinado
    public VariableGlobal(String _valor) {
        Variable = new String(_valor);
    }

    // Devuelve el valor del contenido de la variable global.
    public String getVariable() {
        return Variable;
    }

    // función que inicializa la variable global 
    // @param _valor El valor que se le asignará a la variable
    public void initVariable(String _valor) {
        Variable = _valor;
    }
}
