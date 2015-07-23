/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

import java.io.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class ElectorFicheros extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	public JButton openButton, saveButton;
	public JTextField log,logruta;
	JFileChooser fc;
	String name="", route="";
	File fseleccionado;	//fichero seleccionado.
	static String ruta;
	 
	public ElectorFicheros() {
            File dir_inicial = new File ("./");
            String x =dir_inicial.getAbsolutePath();
            String os=System.getProperty("os.name");
            //En Windows las barras son \ y en Linux /.
            if (os.indexOf("Win")>=0) ruta=x+"\\Consultas\\";//Windows
            else ruta=x+"/Consultas/";                       //Linux
            log = new JTextField();
            log.setEditable(false);
            logruta = new JTextField();
            logruta.setEditable(false);
            //Crea el seleccionador de fichero (file chooser), abriendolo desde el directorio consultas(=ruta).
            fc = new JFileChooser(ruta);
            
            URL imgURL = getClass().getResource(SkoaMain.ROOT_ICONS_PATH + "abrir.png");
            openButton = new JButton("Abrir un fichero...", new ImageIcon(imgURL));
            openButton.addActionListener(this);
	}
 
	public void actionPerformed(ActionEvent e) {
		//Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(ElectorFicheros.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				fseleccionado=file;
				log.setText(" " + file.getName()); 	//Coge el nombre del fichero para abrir.
				logruta.setText(""+file.getAbsolutePath()); 	//Coge la ruta del fichero para abrir.
			} else {
				log.setText("");
				logruta.setText("");
			}
			log.setCaretPosition(log.getDocument().getLength());
			logruta.setCaretPosition(logruta.getDocument().getLength());
		} 
	}
}
