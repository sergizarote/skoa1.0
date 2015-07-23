/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

// Necesarios para poder centrar la ventana
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;

/**
 *
 * @author  David
 */
public class Guardando extends javax.swing.JDialog {

    /** Creates new form Guardando */
    public Guardando(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLInfo = new javax.swing.JLabel();
        jLGif = new javax.swing.JLabel();
        jBAceptar = new javax.swing.JButton();
        jLInfo2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class).getContext().getResourceMap(Guardando.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLInfo.setFont(resourceMap.getFont("jLInfo.font")); // NOI18N
        jLInfo.setText(resourceMap.getString("jLInfo.text")); // NOI18N
        jLInfo.setName("jLInfo"); // NOI18N

        jLGif.setIcon(resourceMap.getIcon("jLGif.icon")); // NOI18N
        jLGif.setText(resourceMap.getString("jLGif.text")); // NOI18N
        jLGif.setName("jLGif"); // NOI18N

        jBAceptar.setText(resourceMap.getString("jBAceptar.text")); // NOI18N
        jBAceptar.setEnabled(false);
        jBAceptar.setName("jBAceptar"); // NOI18N
        jBAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAceptarActionPerformed(evt);
            }
        });

        jLInfo2.setFont(resourceMap.getFont("jLInfo2.font")); // NOI18N
        jLInfo2.setText(resourceMap.getString("jLInfo2.text")); // NOI18N
        jLInfo2.setName("jLInfo2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLInfo))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLInfo2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLGif))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jBAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLInfo2)
                .addGap(28, 28, 28)
                .addComponent(jLGif, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jBAceptar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
private void jBAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAceptarActionPerformed
    this.dispose();
}//GEN-LAST:event_jBAceptarActionPerformed

private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    // Desde que aparece la ventana de guardado, se realiza una llamada a la función guarda_info()
    // Que guarda el contenido de los vectores vecDispUsados y vecDispDomoticos, así como sus índices
    // en el fichero info.txt
    SkoaMain.guarda_info(); //******************************************************************************************************************
        
    // Tras el proceso de guardado, se activa el botón de aceptar
    jBAceptar.setEnabled(true);
    // Y se cambia la información mostrada en pantalla
    jLInfo.setText("Información guardada con éxito");
    jLInfo2.setText("");
}//GEN-LAST:event_formWindowOpened

    /**
    * @param args the command line arguments
    */
    public static void main() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Guardando dialog = new Guardando(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        //System.exit(0);
                    }
                });
                
                // Para centrar la ventana
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = dialog.getSize();
                if (frameSize.height > screenSize.height)
                    frameSize.height = screenSize.height;
                if (frameSize.width > screenSize.width)
                    frameSize.width = screenSize.width;
        
                dialog.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);        
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton jBAceptar;
    private javax.swing.JLabel jLGif;
    public static javax.swing.JLabel jLInfo;
    public static javax.swing.JLabel jLInfo2;
    // End of variables declaration//GEN-END:variables

}
