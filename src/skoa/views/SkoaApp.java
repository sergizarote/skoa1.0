/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */
package skoa.views;

import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class SkoaApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    

    @Override 
    protected void startup() {

      SelectLanguage dialog = new SelectLanguage(new javax.swing.JFrame(), true);
        dialog.display();
       switch(dialog.getReturnStatus()){
            case 0:
                 Locale.setDefault(new Locale("ES"));
                break;
            case 1:
                 Locale.setDefault(Locale.ENGLISH);
                break;
            default:
                 System.exit(0);
        }
        dialog.dispose();
        
        
        
        System.out.println("Default locale:" + Locale.getDefault().toString());
        
        
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        show(new SkoaMain(this));
       // show(new SelectLanguage(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override 
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MaipezApp
     */
    public static SkoaApp getApplication() {
        return Application.getInstance(SkoaApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(SkoaApp.class, args);
    }
    
    
  
}
