
/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.models;

public class NuevoContador {

    public String direccionFisica;
    public float medicion;
    public String medida;//Wh, ºC, W, ....


    public NuevoContador(String dirFis, float med, String medid) {
        this.direccionFisica = dirFis;
        this.medicion = med;
        this.medida = medid;
    }

}
