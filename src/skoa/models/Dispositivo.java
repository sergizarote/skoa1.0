/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.models;

/**
 *
 * @author jorge
 */
public class Dispositivo {
    public String id ;
    public String tipo;
    public String icon;
    public int X;
    public int Y;
    public int ancho;
    public int largo ;
    

    public Dispositivo(String id,String tipo, String icon, int X, int Y, int ancho, int largo) {
        this.tipo = tipo;
        this.icon = icon;
        this.X = X;
        this.Y = Y;
        this.ancho = ancho;
        this.largo = largo;
        this.id = id;
    }

   
    
    
    
    

   
    
    
    
    
}
