/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.models;

import org.jdom.filter.Filter;
import org.jdom.Element;
/**
 *
 * @author 
 */
// Esta clase nos servirá para utilizar un filtro de búsqueda en el fichero XML
public class ElemNameFilter implements Filter {

    String name;

    public ElemNameFilter(String name) {
        this.name = name;
    }

    public boolean matches(Object o) {

        if (o instanceof Element) {
            Element e = (Element) o;
            if (e.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}