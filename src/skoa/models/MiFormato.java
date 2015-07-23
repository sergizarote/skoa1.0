/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.models;

public class MiFormato {

    public String nombre;
    public String direccionGrupo;
    public String tipoEstructura;
    public String subtipo;
    public String x;
    public String y;

    @SuppressWarnings("deprecation")
    public MiFormato(String nombree, String dirgrup, String tipoestr, String subtipo, String xx, String yy) {
        this.nombre = nombree;
        this.direccionGrupo = dirgrup;
        this.tipoEstructura = tipoestr;
        this.subtipo = subtipo;
        this.x = xx;
        this.y = yy;
    }
}