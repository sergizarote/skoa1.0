/**                   
 * 
 GNU GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.
 */

package skoa.views;

import javax.swing.Timer;

/**
 *
 * @author  David
 */
public class EstanciaGenerica extends javax.swing.JPanel {
    
    // Definimos los atributos que tendrá la clase
    public javax.swing.ButtonGroup bGConmLuc;
    public javax.swing.ButtonGroup bGRegLuc;
    public javax.swing.ButtonGroup bGValvula;
    public javax.swing.JButton jBBajar;
    public javax.swing.JButton jBSubir;
    
    // Para representar a los iconos (30 máximo, suponiendo 5 de cada tipo)
    public javax.swing.JLabel jLIconoReg1; 
    public javax.swing.JLabel jLIconoReg2;
    public javax.swing.JLabel jLIconoReg3;
    public javax.swing.JLabel jLIconoReg4;
    public javax.swing.JLabel jLIconoReg5;
    public javax.swing.JLabel jLIconoConm1;
    public javax.swing.JLabel jLIconoConm2;
    public javax.swing.JLabel jLIconoConm3; 
    public javax.swing.JLabel jLIconoConm4;
    public javax.swing.JLabel jLIconoConm5;
    public javax.swing.JLabel jLIconoPers1;
    public javax.swing.JLabel jLIconoPers2;
    public javax.swing.JLabel jLIconoPers3;
    public javax.swing.JLabel jLIconoPers4;
    public javax.swing.JLabel jLIconoPers5; 
    public javax.swing.JLabel jLIconoPuerta1;
    public javax.swing.JLabel jLIconoPuerta2; 
    public javax.swing.JLabel jLIconoPuerta3;
    public javax.swing.JLabel jLIconoPuerta4;
    public javax.swing.JLabel jLIconoPuerta5;
    public javax.swing.JLabel jLIconoMov1;
    public javax.swing.JLabel jLIconoMov2;
    public javax.swing.JLabel jLIconoMov3;
    public javax.swing.JLabel jLIconoMov4; 
    public javax.swing.JLabel jLIconoMov5;
    public javax.swing.JLabel jLIconoTemp1;
    public javax.swing.JLabel jLIconoTemp2;
    public javax.swing.JLabel jLIconoTemp3;
    public javax.swing.JLabel jLIconoTemp4;
    public javax.swing.JLabel jLIconoTemp5;
    public javax.swing.JLabel jLIconoComb1;
    public javax.swing.JLabel jLIconoComb2;
    public javax.swing.JLabel jLIconoComb3;
    public javax.swing.JLabel jLIconoComb4;
    public javax.swing.JLabel jLIconoComb5;
    public javax.swing.JLabel jLIconoInund1;
    public javax.swing.JLabel jLIconoInund2;
    public javax.swing.JLabel jLIconoInund3;
    public javax.swing.JLabel jLIconoInund4;
    public javax.swing.JLabel jLIconoInund5;
    public javax.swing.JLabel jLIconoCont1;
    public javax.swing.JLabel jLIconoCont2;
    public javax.swing.JLabel jLIconoCont3;
    public javax.swing.JLabel jLIconoCont4;
    public javax.swing.JLabel jLIconoCont5;
    public javax.swing.JLabel jLIconoElectr1;
    public javax.swing.JLabel jLIconoElectr2;
    public javax.swing.JLabel jLIconoElectr3;
    public javax.swing.JLabel jLIconoElectr4;
    public javax.swing.JLabel jLIconoElectr5;
    
    // Para representar a los iconos (30 máximo, suponiendo 5 de cada tipo)
    public javax.swing.JLabel jLIcono1; 
    public javax.swing.JLabel jLIcono2;
    public javax.swing.JLabel jLIcono3;
    public javax.swing.JLabel jLIcono4;
    public javax.swing.JLabel jLIcono5;
    public javax.swing.JLabel jLIcono6;
    public javax.swing.JLabel jLIcono7;
    public javax.swing.JLabel jLIcono8; 
    public javax.swing.JLabel jLIcono9;
    public javax.swing.JLabel jLIcono10;
    public javax.swing.JLabel jLIcono11;
    public javax.swing.JLabel jLIcono12;
    public javax.swing.JLabel jLIcono13;
    public javax.swing.JLabel jLIcono14;
    public javax.swing.JLabel jLIcono15; 
    public javax.swing.JLabel jLIcono16;
    public javax.swing.JLabel jLIcono17;
    public javax.swing.JLabel jLIcono18;
    public javax.swing.JLabel jLIcono19;
    public javax.swing.JLabel jLIcono20;
    public javax.swing.JLabel jLIcono21;
    public javax.swing.JLabel jLIcono22; 
    public javax.swing.JLabel jLIcono23;
    public javax.swing.JLabel jLIcono24;
    public javax.swing.JLabel jLIcono25;
    public javax.swing.JLabel jLIcono26;
    public javax.swing.JLabel jLIcono27;
    public javax.swing.JLabel jLIcono28;
    public javax.swing.JLabel jLIcono29; 
    public javax.swing.JLabel jLIcono30;
    
    // Los labels donde se mostrará cierta información
    public javax.swing.JLabel jLEstApertura;
    public javax.swing.JLabel jLEstEnergia;
    public javax.swing.JLabel jLEstInundacion;
    public javax.swing.JLabel jLEstLumComb;
    public javax.swing.JLabel jLEstMovimiento;
    public javax.swing.JLabel jLEstPotencia;
    public javax.swing.JLabel jLEstTemp;
    public javax.swing.JLabel jLEstTempComb;
    public javax.swing.JLayeredPane jLPEstancia;
    public javax.swing.JLabel jLPlano;
    public javax.swing.JLabel jLResApertura;
    public javax.swing.JLabel jLResEnergia;
    public javax.swing.JLabel jLResInundacion;
    public javax.swing.JLabel jLResLumComb;
    public javax.swing.JLabel jLResMovimiento;
    public javax.swing.JLabel jLResPotencia;
    public javax.swing.JLabel jLResTemp;
    public javax.swing.JLabel jLResTempComb;
    
    // Los paneles
    public javax.swing.JPanel jPActuadores;
    public javax.swing.JPanel jPApertura;
    public javax.swing.JPanel jPConmLuc;
    public javax.swing.JPanel jPContadores;
    public javax.swing.JPanel jPEstancia;
    public javax.swing.JPanel jPInundacion;
    public javax.swing.JPanel jPMovimiento;
    public javax.swing.JPanel jPPersianas;
    public javax.swing.JPanel jPRegLuc;
    public javax.swing.JPanel jPSenComb;
    public javax.swing.JPanel jPSensores;
    public javax.swing.JPanel jPTemp;
    public javax.swing.JPanel jPValvula;
    
    // Los botones para seleccionar la intensidad de las luces regulables
    public javax.swing.JRadioButton jRB100;
    public javax.swing.JRadioButton jRB25;
    public javax.swing.JRadioButton jRB50;
    public javax.swing.JRadioButton jRB75;
    public javax.swing.JRadioButton jRBAbrir;
    public javax.swing.JRadioButton jRBApagLuc;
    public javax.swing.JRadioButton jRBApagar;
    public javax.swing.JRadioButton jRBCerrar;
    public javax.swing.JRadioButton jRBEncender;
    
    // Los combobox
    public javax.swing.JComboBox jCBApertura;
    public javax.swing.JComboBox jCBConmLuc;
    public javax.swing.JComboBox jCBPersianas;
    public javax.swing.JComboBox jCBRegLuc;
    public javax.swing.JComboBox jCBValvula;
    public javax.swing.JComboBox jCBInund;
    public javax.swing.JComboBox jCBMovimiento;
    
    // Para conocer el último estado de las luces regulabes (desconocido por defecto)
    public String ultimoEstado_lr1 = "brd";
    public String ultimoEstado_lr2 = "brd";
    public String ultimoEstado_lr3 = "brd";
    public String ultimoEstado_lr4 = "brd";
    public String ultimoEstado_lr5 = "brd";
    
    // Los timers de los diferentes dispositivos
    public Timer timer_mov1;
    public Timer timer_mov2;
    public Timer timer_mov3;
    public Timer timer_mov4;
    public Timer timer_mov5;
    public Timer timer_puerta1;
    public Timer timer_puerta2;
    public Timer timer_puerta3;
    public Timer timer_puerta4;
    public Timer timer_puerta5;
    public Timer timer_inund1;
    public Timer timer_inund2;
    public Timer timer_inund3;
    public Timer timer_inund4;
    public Timer timer_inund5;
    public Timer timer_temp;
    public Timer timer_comb;
    public Timer timer_cont;
    //ESTEFANíA---------------
    public Timer timer_luzc1;
    public Timer timer_luzc2;
    public Timer timer_luzc3;
    public Timer timer_luzc4;
    public Timer timer_luzc5;
    public Timer timer_pers1;
    public Timer timer_pers2;
    public Timer timer_pers3;
    public Timer timer_pers4;
    public Timer timer_pers5;
    //------------------------
    
    /** Creates new form EstanciaGnerica */
    public EstanciaGenerica() {
        //initComponents();
        // Utilizamos un procedimiento auxiliar para inicializar los componentes y permitir
        // que puedan ser modificados más adelante
        initMio();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGRegLuc = new javax.swing.ButtonGroup();
        bGValvula = new javax.swing.ButtonGroup();
        bGConmLuc = new javax.swing.ButtonGroup();
        jPEstancia = new javax.swing.JPanel();
        jPActuadores = new javax.swing.JPanel();
        jPRegLuc = new javax.swing.JPanel();
        jRBApagar = new javax.swing.JRadioButton();
        jRB25 = new javax.swing.JRadioButton();
        jRB50 = new javax.swing.JRadioButton();
        jRB75 = new javax.swing.JRadioButton();
        jRB100 = new javax.swing.JRadioButton();
        jCBRegLuc = new javax.swing.JComboBox();
        jPConmLuc = new javax.swing.JPanel();
        jRBEncender = new javax.swing.JRadioButton();
        jRBApagLuc = new javax.swing.JRadioButton();
        jCBConmLuc = new javax.swing.JComboBox();
        jPPersianas = new javax.swing.JPanel();
        jBSubir = new javax.swing.JButton();
        jBBajar = new javax.swing.JButton();
        jCBPersianas = new javax.swing.JComboBox();
        jPValvula = new javax.swing.JPanel();
        jRBAbrir = new javax.swing.JRadioButton();
        jRBCerrar = new javax.swing.JRadioButton();
        jCBValvula = new javax.swing.JComboBox();
        jPSensores = new javax.swing.JPanel();
        jPApertura = new javax.swing.JPanel();
        jLEstApertura = new javax.swing.JLabel();
        jLResApertura = new javax.swing.JLabel();
        jCBApertura = new javax.swing.JComboBox();
        jPMovimiento = new javax.swing.JPanel();
        jLEstMovimiento = new javax.swing.JLabel();
        jLResMovimiento = new javax.swing.JLabel();
        jCBMovimiento = new javax.swing.JComboBox();
        jPTemp = new javax.swing.JPanel();
        jLEstTemp = new javax.swing.JLabel();
        jLResTemp = new javax.swing.JLabel();
        jPSenComb = new javax.swing.JPanel();
        jLEstTempComb = new javax.swing.JLabel();
        jLResTempComb = new javax.swing.JLabel();
        jLEstLumComb = new javax.swing.JLabel();
        jLResLumComb = new javax.swing.JLabel();
        jPInundacion = new javax.swing.JPanel();
        jLEstInundacion = new javax.swing.JLabel();
        jLResInundacion = new javax.swing.JLabel();
        jCBInund = new javax.swing.JComboBox();
        jPContadores = new javax.swing.JPanel();
        jLEstPotencia = new javax.swing.JLabel();
        jLResPotencia = new javax.swing.JLabel();
        jLPEstancia = new javax.swing.JLayeredPane();
        jLPlano = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        jPEstancia.setName("jPEstancia"); // NOI18N

        jPActuadores.setName("jPActuadores"); // NOI18N

        jPRegLuc.setName("jPRegLuc"); // NOI18N

        bGRegLuc.add(jRBApagar);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(EstanciaGenerica.class);
        jRBApagar.setText(resourceMap.getString("jRBApagar.text")); // NOI18N
        jRBApagar.setName("jRBApagar"); // NOI18N
        jRBApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBApagarActionPerformed(evt);
            }
        });
        jRBApagar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jRBApagarKeyPressed(evt);
            }
        });

        bGRegLuc.add(jRB25);
        jRB25.setText(resourceMap.getString("jRB25.text")); // NOI18N
        jRB25.setName("jRB25"); // NOI18N

        bGRegLuc.add(jRB50);
        jRB50.setText(resourceMap.getString("jRB50.text")); // NOI18N
        jRB50.setName("jRB50"); // NOI18N

        bGRegLuc.add(jRB75);
        jRB75.setText(resourceMap.getString("jRB75.text")); // NOI18N
        jRB75.setName("jRB75"); // NOI18N

        bGRegLuc.add(jRB100);
        jRB100.setText(resourceMap.getString("jRB100.text")); // NOI18N
        jRB100.setName("jRB100"); // NOI18N

        jCBRegLuc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Luz Regulable 1" }));
        jCBRegLuc.setName("jCBRegLuc"); // NOI18N
        jCBRegLuc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBRegLucActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPRegLucLayout = new javax.swing.GroupLayout(jPRegLuc);
        jPRegLuc.setLayout(jPRegLucLayout);
        jPRegLucLayout.setHorizontalGroup(
            jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPRegLucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRBApagar)
                        .addGap(47, 47, 47))
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRB25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRB75))
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRB50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRB100))
                    .addComponent(jCBRegLuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(83, Short.MAX_VALUE))
        );
        jPRegLucLayout.setVerticalGroup(
            jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPRegLucLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBRegLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBApagar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRB25)
                    .addComponent(jRB75))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRB50)
                    .addComponent(jRB100)))
        );

        jPConmLuc.setName("jPConmLuc"); // NOI18N

        bGConmLuc.add(jRBEncender);
        jRBEncender.setText(resourceMap.getString("jRBEncender.text")); // NOI18N
        jRBEncender.setName("jRBEncender"); // NOI18N

        bGConmLuc.add(jRBApagLuc);
        jRBApagLuc.setText(resourceMap.getString("jRBApagLuc.text")); // NOI18N
        jRBApagLuc.setName("jRBApagLuc"); // NOI18N

        jCBConmLuc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Luz Conmutable 1" }));
        jCBConmLuc.setName("jCBConmLuc"); // NOI18N

        javax.swing.GroupLayout jPConmLucLayout = new javax.swing.GroupLayout(jPConmLuc);
        jPConmLuc.setLayout(jPConmLucLayout);
        jPConmLucLayout.setHorizontalGroup(
            jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPConmLucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBEncender)
                    .addComponent(jRBApagLuc)
                    .addComponent(jCBConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPConmLucLayout.setVerticalGroup(
            jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPConmLucLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jRBEncender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBApagLuc))
        );

        jPPersianas.setName("jPPersianas"); // NOI18N

        jBSubir.setText(resourceMap.getString("jBSubir.text")); // NOI18N
        jBSubir.setName("jBSubir"); // NOI18N
        jBSubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSubirActionPerformed(evt);
            }
        });

        jBBajar.setText(resourceMap.getString("jBBajar.text")); // NOI18N
        jBBajar.setName("jBBajar"); // NOI18N

        jCBPersianas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Persiana 1" }));
        jCBPersianas.setName("jCBPersianas"); // NOI18N

        javax.swing.GroupLayout jPPersianasLayout = new javax.swing.GroupLayout(jPPersianas);
        jPPersianas.setLayout(jPPersianasLayout);
        jPPersianasLayout.setHorizontalGroup(
            jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPersianasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jBBajar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBSubir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPPersianasLayout.setVerticalGroup(
            jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPersianasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBSubir)
                .addGap(4, 4, 4)
                .addComponent(jBBajar)
                .addGap(7, 7, 7))
        );

        jPValvula.setName("jPValvula"); // NOI18N

        bGValvula.add(jRBAbrir);
        jRBAbrir.setText(resourceMap.getString("jRBAbrir.text")); // NOI18N
        jRBAbrir.setName("jRBAbrir"); // NOI18N

        bGValvula.add(jRBCerrar);
        jRBCerrar.setText(resourceMap.getString("jRBCerrar.text")); // NOI18N
        jRBCerrar.setName("jRBCerrar"); // NOI18N

        jCBValvula.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Valve 1" }));
        jCBValvula.setName("jCBValvula"); // NOI18N

        javax.swing.GroupLayout jPValvulaLayout = new javax.swing.GroupLayout(jPValvula);
        jPValvula.setLayout(jPValvulaLayout);
        jPValvulaLayout.setHorizontalGroup(
            jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPValvulaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBAbrir)
                    .addComponent(jRBCerrar)
                    .addComponent(jCBValvula, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPValvulaLayout.setVerticalGroup(
            jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPValvulaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRBAbrir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBCerrar)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPActuadoresLayout = new javax.swing.GroupLayout(jPActuadores);
        jPActuadores.setLayout(jPActuadoresLayout);
        jPActuadoresLayout.setHorizontalGroup(
            jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPRegLuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPConmLuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPPersianas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPValvula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82))
        );
        jPActuadoresLayout.setVerticalGroup(
            jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPRegLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPRegLuc.getAccessibleContext().setAccessibleName(resourceMap.getString("jPRegLuc.AccessibleContext.accessibleName")); // NOI18N

        jPSensores.setName("jPSensores"); // NOI18N

        jPApertura.setName("jPApertura"); // NOI18N

        jLEstApertura.setText(resourceMap.getString("jLEstApertura.text")); // NOI18N
        jLEstApertura.setName("jLEstApertura"); // NOI18N

        jLResApertura.setText(resourceMap.getString("jLResApertura.text")); // NOI18N
        jLResApertura.setName("jLResApertura"); // NOI18N

        jCBApertura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Puerta 1" }));
        jCBApertura.setName("jCBApertura"); // NOI18N
        jCBApertura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBAperturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPAperturaLayout = new javax.swing.GroupLayout(jPApertura);
        jPApertura.setLayout(jPAperturaLayout);
        jPAperturaLayout.setHorizontalGroup(
            jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAperturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPAperturaLayout.createSequentialGroup()
                        .addComponent(jLEstApertura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResApertura)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPAperturaLayout.setVerticalGroup(
            jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAperturaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstApertura)
                    .addComponent(jLResApertura))
                .addContainerGap())
        );

        jPMovimiento.setName("jPMovimiento"); // NOI18N

        jLEstMovimiento.setText(resourceMap.getString("jLEstMovimiento.text")); // NOI18N
        jLEstMovimiento.setName("jLEstMovimiento"); // NOI18N

        jLResMovimiento.setText(resourceMap.getString("jLResMovimiento.text")); // NOI18N
        jLResMovimiento.setName("jLResMovimiento"); // NOI18N

        jCBMovimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Detector 1" }));
        jCBMovimiento.setName("jCBMovimiento"); // NOI18N
        jCBMovimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBMovimientoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPMovimientoLayout = new javax.swing.GroupLayout(jPMovimiento);
        jPMovimiento.setLayout(jPMovimientoLayout);
        jPMovimientoLayout.setHorizontalGroup(
            jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMovimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jCBMovimiento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLEstMovimiento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLResMovimiento)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPMovimientoLayout.setVerticalGroup(
            jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPMovimientoLayout.createSequentialGroup()
                .addComponent(jCBMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstMovimiento)
                    .addComponent(jLResMovimiento))
                .addContainerGap())
        );

        jPTemp.setName("jPTemp"); // NOI18N

        jLEstTemp.setText(resourceMap.getString("jLEstTemp.text")); // NOI18N
        jLEstTemp.setName("jLEstTemp"); // NOI18N

        jLResTemp.setText(resourceMap.getString("jLResTemp.text")); // NOI18N
        jLResTemp.setName("jLResTemp"); // NOI18N

        javax.swing.GroupLayout jPTempLayout = new javax.swing.GroupLayout(jPTemp);
        jPTemp.setLayout(jPTempLayout);
        jPTempLayout.setHorizontalGroup(
            jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTempLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLEstTemp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLResTemp)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPTempLayout.setVerticalGroup(
            jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTempLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstTemp)
                    .addComponent(jLResTemp))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPSenComb.setName("jPSenComb"); // NOI18N

        jLEstTempComb.setText(resourceMap.getString("jLEstTempComb.text")); // NOI18N
        jLEstTempComb.setName("jLEstTempComb"); // NOI18N

        jLResTempComb.setText(resourceMap.getString("jLResTempComb.text")); // NOI18N
        jLResTempComb.setName("jLResTempComb"); // NOI18N

        jLEstLumComb.setText(resourceMap.getString("jLEstLumComb.text")); // NOI18N
        jLEstLumComb.setName("jLEstLumComb"); // NOI18N

        jLResLumComb.setText(resourceMap.getString("jLResLumComb.text")); // NOI18N
        jLResLumComb.setName("jLResLumComb"); // NOI18N

        javax.swing.GroupLayout jPSenCombLayout = new javax.swing.GroupLayout(jPSenComb);
        jPSenComb.setLayout(jPSenCombLayout);
        jPSenCombLayout.setHorizontalGroup(
            jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSenCombLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPSenCombLayout.createSequentialGroup()
                        .addComponent(jLEstTempComb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResTempComb))
                    .addGroup(jPSenCombLayout.createSequentialGroup()
                        .addComponent(jLEstLumComb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLResLumComb)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPSenCombLayout.setVerticalGroup(
            jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSenCombLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstTempComb)
                    .addComponent(jLResTempComb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstLumComb)
                    .addComponent(jLResLumComb))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPInundacion.setName("jPInundacion"); // NOI18N

        jLEstInundacion.setText(resourceMap.getString("jLEstInundacion.text")); // NOI18N
        jLEstInundacion.setName("jLEstInundacion"); // NOI18N

        jLResInundacion.setText(resourceMap.getString("jLResInundacion.text")); // NOI18N
        jLResInundacion.setName("jLResInundacion"); // NOI18N

        jCBInund.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sensor 1" }));
        jCBInund.setName("jCBInund"); // NOI18N

        javax.swing.GroupLayout jPInundacionLayout = new javax.swing.GroupLayout(jPInundacion);
        jPInundacion.setLayout(jPInundacionLayout);
        jPInundacionLayout.setHorizontalGroup(
            jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInundacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPInundacionLayout.createSequentialGroup()
                        .addComponent(jLEstInundacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResInundacion))
                    .addComponent(jCBInund, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPInundacionLayout.setVerticalGroup(
            jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInundacionLayout.createSequentialGroup()
                .addComponent(jCBInund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstInundacion)
                    .addComponent(jLResInundacion))
                .addContainerGap())
        );

        jPContadores.setName("jPContadores"); // NOI18N

        jLEstPotencia.setText(resourceMap.getString("jLEstPotencia.text")); // NOI18N
        jLEstPotencia.setName("jLEstPotencia"); // NOI18N

        jLResPotencia.setText(resourceMap.getString("jLResPotencia.text")); // NOI18N
        jLResPotencia.setName("jLResPotencia"); // NOI18N

        javax.swing.GroupLayout jPContadoresLayout = new javax.swing.GroupLayout(jPContadores);
        jPContadores.setLayout(jPContadoresLayout);
        jPContadoresLayout.setHorizontalGroup(
            jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLEstPotencia)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLResPotencia)
                .addContainerGap(146, Short.MAX_VALUE))
        );
        jPContadoresLayout.setVerticalGroup(
            jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstPotencia)
                    .addComponent(jLResPotencia))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPSensoresLayout = new javax.swing.GroupLayout(jPSensores);
        jPSensores.setLayout(jPSensoresLayout);
        jPSensoresLayout.setHorizontalGroup(
            jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPSensoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPContadores, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPInundacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPApertura, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPSenComb, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPTemp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPMovimiento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPSensoresLayout.setVerticalGroup(
            jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSensoresLayout.createSequentialGroup()
                .addComponent(jPApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPMovimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addComponent(jPTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPSenComb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPInundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPContadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLPEstancia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLPEstancia.setName("jLPEstancia"); // NOI18N

        jLPlano.setIcon(new javax.swing.ImageIcon(getClass().getResource("/skoa/views/resources/01dormitorio.png"))); // NOI18N
        jLPlano.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLPlano.setMaximumSize(new java.awt.Dimension(390, 400));
        jLPlano.setMinimumSize(new java.awt.Dimension(390, 400));
        jLPlano.setName("jLPlano"); // NOI18N
        jLPlano.setPreferredSize(new java.awt.Dimension(390, 400));
        jLPlano.setBounds(5, 82, 390, 400);
        jLPEstancia.add(jLPlano, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jPEstanciaLayout = new javax.swing.GroupLayout(jPEstancia);
        jPEstancia.setLayout(jPEstanciaLayout);
        jPEstanciaLayout.setHorizontalGroup(
            jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLPEstancia, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addComponent(jPSensores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPEstanciaLayout.setVerticalGroup(
            jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPSensores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPActuadores.getAccessibleContext().setAccessibleName(resourceMap.getString("jPActuadores.AccessibleContext.accessibleName")); // NOI18N
        jPActuadores.getAccessibleContext().setAccessibleDescription(resourceMap.getString("jPActuadores.AccessibleContext.accessibleDescription")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1249, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(113, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

private void jCBRegLucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBRegLucActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCBRegLucActionPerformed

private void jRBApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBApagarActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRBApagarActionPerformed

private void jRBApagarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jRBApagarKeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_jRBApagarKeyPressed

private void jBSubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSubirActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jBSubirActionPerformed

private void jCBAperturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBAperturaActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jCBAperturaActionPerformed

private void jCBMovimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBMovimientoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCBMovimientoActionPerformed

    public void initMio()
    {
        bGRegLuc = new javax.swing.ButtonGroup();
        bGValvula = new javax.swing.ButtonGroup();
        bGConmLuc = new javax.swing.ButtonGroup();
        jPEstancia = new javax.swing.JPanel();
        jPActuadores = new javax.swing.JPanel();
        jPRegLuc = new javax.swing.JPanel();
        jRBApagar = new javax.swing.JRadioButton();
        jRB25 = new javax.swing.JRadioButton();
        jRB50 = new javax.swing.JRadioButton();
        jRB75 = new javax.swing.JRadioButton();
        jRB100 = new javax.swing.JRadioButton();
        jCBRegLuc = new javax.swing.JComboBox();
        jPConmLuc = new javax.swing.JPanel();
        jRBEncender = new javax.swing.JRadioButton();
        jRBApagLuc = new javax.swing.JRadioButton();
        jCBConmLuc = new javax.swing.JComboBox();
        jPPersianas = new javax.swing.JPanel();
        jBSubir = new javax.swing.JButton();
        jBBajar = new javax.swing.JButton();
        jCBPersianas = new javax.swing.JComboBox();
        jPValvula = new javax.swing.JPanel();
        jRBAbrir = new javax.swing.JRadioButton();
        jRBCerrar = new javax.swing.JRadioButton();
        jCBValvula = new javax.swing.JComboBox();
        jPSensores = new javax.swing.JPanel();
        jPApertura = new javax.swing.JPanel();
        jLEstApertura = new javax.swing.JLabel();
        jLResApertura = new javax.swing.JLabel();
        jCBApertura = new javax.swing.JComboBox();
        jPMovimiento = new javax.swing.JPanel();
        jLEstMovimiento = new javax.swing.JLabel();
        jLResMovimiento = new javax.swing.JLabel();
        jCBMovimiento = new javax.swing.JComboBox();
        jPTemp = new javax.swing.JPanel();
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        jLIcono1 = new javax.swing.JLabel();
        jLIcono2 = new javax.swing.JLabel();
        jLIcono3 = new javax.swing.JLabel();
        jLIcono4 = new javax.swing.JLabel();
        jLIcono5 = new javax.swing.JLabel();
        jLIcono6 = new javax.swing.JLabel();
        jLIcono7 = new javax.swing.JLabel();
        jLIcono8 = new javax.swing.JLabel();
        jLIcono9 = new javax.swing.JLabel();
        jLIcono10 = new javax.swing.JLabel();
        jLIcono11 = new javax.swing.JLabel();
        jLIcono12 = new javax.swing.JLabel();
        jLIcono13 = new javax.swing.JLabel();
        jLIcono14 = new javax.swing.JLabel();
        jLIcono15 = new javax.swing.JLabel();
        jLIcono16 = new javax.swing.JLabel();
        jLIcono17 = new javax.swing.JLabel();
        jLIcono18 = new javax.swing.JLabel();
        jLIcono19 = new javax.swing.JLabel();
        jLIcono20 = new javax.swing.JLabel();
        jLIcono21 = new javax.swing.JLabel();
        jLIcono22 = new javax.swing.JLabel();
        jLIcono23 = new javax.swing.JLabel();
        jLIcono24 = new javax.swing.JLabel();
        jLIcono25 = new javax.swing.JLabel();
        jLIcono26 = new javax.swing.JLabel();
        jLIcono27 = new javax.swing.JLabel();
        jLIcono28 = new javax.swing.JLabel();
        jLIcono29 = new javax.swing.JLabel();
        jLIcono30 = new javax.swing.JLabel();
        // AQUIIII
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        jLIconoReg1 = new javax.swing.JLabel();
        jLIconoReg2 = new javax.swing.JLabel();
        jLIconoReg3 = new javax.swing.JLabel();
        jLIconoReg4 = new javax.swing.JLabel();
        jLIconoReg5 = new javax.swing.JLabel();
        jLIconoConm1 = new javax.swing.JLabel();
        jLIconoConm2 = new javax.swing.JLabel();
        jLIconoConm3 = new javax.swing.JLabel();
        jLIconoConm4 = new javax.swing.JLabel();
        jLIconoConm5 = new javax.swing.JLabel();
        jLIconoPers1 = new javax.swing.JLabel();
        jLIconoPers2 = new javax.swing.JLabel();
        jLIconoPers3 = new javax.swing.JLabel();
        jLIconoPers4 = new javax.swing.JLabel();
        jLIconoPers5 = new javax.swing.JLabel();
        jLIconoPuerta1 = new javax.swing.JLabel();
        jLIconoPuerta2 = new javax.swing.JLabel();
        jLIconoPuerta3 = new javax.swing.JLabel();
        jLIconoPuerta4 = new javax.swing.JLabel();
        jLIconoPuerta5 = new javax.swing.JLabel();
        jLIconoMov1 = new javax.swing.JLabel();
        jLIconoMov2 = new javax.swing.JLabel();
        jLIconoMov3 = new javax.swing.JLabel();
        jLIconoMov4 = new javax.swing.JLabel();
        jLIconoMov5 = new javax.swing.JLabel();
        jLIconoTemp1 = new javax.swing.JLabel();
        jLIconoTemp2 = new javax.swing.JLabel();
        jLIconoTemp3 = new javax.swing.JLabel();
        jLIconoTemp4 = new javax.swing.JLabel();
        jLIconoTemp5 = new javax.swing.JLabel();
        jLIconoComb1 = new javax.swing.JLabel();
        jLIconoComb2 = new javax.swing.JLabel();
        jLIconoComb3 = new javax.swing.JLabel();
        jLIconoComb4 = new javax.swing.JLabel();
        jLIconoComb5 = new javax.swing.JLabel();
        jLIconoInund1 = new javax.swing.JLabel();
        jLIconoInund2 = new javax.swing.JLabel();
        jLIconoInund3 = new javax.swing.JLabel();
        jLIconoInund4 = new javax.swing.JLabel();
        jLIconoInund5 = new javax.swing.JLabel();
        jLIconoCont1 = new javax.swing.JLabel();
        jLIconoCont2 = new javax.swing.JLabel();
        jLIconoCont3 = new javax.swing.JLabel();
        jLIconoCont4 = new javax.swing.JLabel();
        jLIconoCont5 = new javax.swing.JLabel();
        jLIconoElectr1 = new javax.swing.JLabel();
        jLIconoElectr2 = new javax.swing.JLabel();
        jLIconoElectr3 = new javax.swing.JLabel();
        jLIconoElectr4 = new javax.swing.JLabel();
        jLIconoElectr5 = new javax.swing.JLabel();
        
        jLEstTemp = new javax.swing.JLabel();
        jLResTemp = new javax.swing.JLabel();
        jPSenComb = new javax.swing.JPanel();
        jLEstTempComb = new javax.swing.JLabel();
        jLResTempComb = new javax.swing.JLabel();
        jLEstLumComb = new javax.swing.JLabel();
        jLResLumComb = new javax.swing.JLabel();
        jPInundacion = new javax.swing.JPanel();
        jLEstInundacion = new javax.swing.JLabel();
        jLResInundacion = new javax.swing.JLabel();
        jCBInund = new javax.swing.JComboBox();
        jPContadores = new javax.swing.JPanel();
        jLEstPotencia = new javax.swing.JLabel();
        jLResPotencia = new javax.swing.JLabel();
        jLEstEnergia = new javax.swing.JLabel();
        jLResEnergia = new javax.swing.JLabel();
        jLPEstancia = new javax.swing.JLayeredPane();
        jLPlano = new javax.swing.JLabel();

        setName("Form"); // NOI18N
        
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        jLIcono1.setName("jLIcono1");
        jLIcono2.setName("jLIcono2");
        jLIcono3.setName("jLIcono3");
        jLIcono4.setName("jLIcono4");
        jLIcono5.setName("jLIcono5");
        jLIcono6.setName("jLIcono6");
        jLIcono7.setName("jLIcono7");
        jLIcono8.setName("jLIcono8");
        jLIcono9.setName("jLIcono9");
        jLIcono10.setName("jLIcono10");
        jLIcono11.setName("jLIcono11");
        jLIcono12.setName("jLIcono12");
        jLIcono13.setName("jLIcono13");
        jLIcono14.setName("jLIcono14");
        jLIcono15.setName("jLIcono15");
        jLIcono16.setName("jLIcono16");
        jLIcono17.setName("jLIcono17");
        jLIcono18.setName("jLIcono18");
        jLIcono19.setName("jLIcono19");
        jLIcono20.setName("jLIcono20");
        jLIcono21.setName("jLIcono21");
        jLIcono22.setName("jLIcono22");
        jLIcono23.setName("jLIcono23");
        jLIcono24.setName("jLIcono24");
        jLIcono25.setName("jLIcono25");
        jLIcono26.setName("jLIcono26");
        jLIcono27.setName("jLIcono27");
        jLIcono28.setName("jLIcono28");
        jLIcono29.setName("jLIcono29");
        jLIcono30.setName("jLIcono30");
        // -------------------------------------------------------------------
        
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        jLIconoReg1.setName("jLIconoReg1");
        jLIconoReg2.setName("jLIconoReg2");
        jLIconoReg3.setName("jLIconoReg3");
        jLIconoReg4.setName("jLIconoReg4");
        jLIconoReg5.setName("jLIconoReg5");
        jLIconoConm1.setName("jLIconoConm1");
        jLIconoConm2.setName("jLIconoConm2");
        jLIconoConm3.setName("jLIconoConm3");
        jLIconoConm4.setName("jLIconoConm4");
        jLIconoConm5.setName("jLIconoConm5");
        jLIconoPers1.setName("jLIconoPers1");
        jLIconoPers2.setName("jLIconoPers2");
        jLIconoPers3.setName("jLIconoPers3");
        jLIconoPers4.setName("jLIconoPers4");
        jLIconoPers5.setName("jLIconoPers5");
        jLIconoPuerta1.setName("jLIconoPuerta1");
        jLIconoPuerta2.setName("jLIconoPuerta2");
        jLIconoPuerta3.setName("jLIconoPuerta3");
        jLIconoPuerta4.setName("jLIconoPuerta4");
        jLIconoPuerta5.setName("jLIconoPuerta5");
        jLIconoMov1.setName("jLIconoMov1");
        jLIconoMov2.setName("jLIconoMov2");
        jLIconoMov3.setName("jLIconoMov3");
        jLIconoMov4.setName("jLIconoMov4");
        jLIconoMov5.setName("jLIconoMov5");
        jLIconoTemp1.setName("jLIconoTemp1");
        jLIconoTemp2.setName("jLIconoTemp2");
        jLIconoTemp3.setName("jLIconoTemp3");
        jLIconoTemp4.setName("jLIconoTemp4");
        jLIconoTemp5.setName("jLIconoTemp5");
        jLIconoComb1.setName("jLIconoComb1");
        jLIconoComb2.setName("jLIconoComb2");
        jLIconoComb3.setName("jLIconoComb3");
        jLIconoComb4.setName("jLIconoComb4");
        jLIconoComb5.setName("jLIconoComb5");
        jLIconoInund1.setName("jLIconoInund1");
        jLIconoInund2.setName("jLIconoInund2");
        jLIconoInund3.setName("jLIconoInund3");
        jLIconoInund4.setName("jLIconoInund4");
        jLIconoInund5.setName("jLIconoInund5");
        jLIconoCont1.setName("jLIconoCont1");
        jLIconoCont2.setName("jLIconoCont2");
        jLIconoCont3.setName("jLIconoCont3");
        jLIconoCont4.setName("jLIconoCont4");
        jLIconoCont5.setName("jLIconoCont5");
        jLIconoElectr1.setName("jLIconoElectr1");
        jLIconoElectr2.setName("jLIconoElectr2");
        jLIconoElectr3.setName("jLIconoElectr3");
        jLIconoElectr4.setName("jLIconoElectr4");
        jLIconoElectr5.setName("jLIconoElectr5");
        
        // -------------------------------------------------------------------

         org.jdesktop.application.ResourceMap resourceMap 
          = org.jdesktop.application.Application.getInstance(skoa.views.SkoaApp.class)
                .getContext().getResourceMap(EstanciaGenerica.class);
        
        jPEstancia.setName("jPEstancia"); // NOI18N

        jPActuadores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPActuadores.AccessibleContext.accessibleDescription") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPActuadores.setName("jPActuadores"); // NOI18N

        jPRegLuc.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPRegLuc.AccessibleContext.accessibleName"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPRegLuc.setName("jPRegLuc"); // NOI18N

        bGRegLuc.add(jRBApagar);
       
        
        
        jRBApagar.setText(resourceMap.getString("jRBApagar.text")); // NOI18N
        jRBApagar.setName("jRBApagar"); // NOI18N

        bGRegLuc.add(jRB25);
        jRB25.setText(resourceMap.getString("jRB25.text")); // NOI18N
        jRB25.setName("jRB25"); // NOI18N

        bGRegLuc.add(jRB50);
        jRB50.setText(resourceMap.getString("jRB50.text")); // NOI18N
        jRB50.setName("jRB50"); // NOI18N

        bGRegLuc.add(jRB75);
        jRB75.setText(resourceMap.getString("jRB75.text")); // NOI18N
        jRB75.setName("jRB75"); // NOI18N

        bGRegLuc.add(jRB100);
        jRB100.setText(resourceMap.getString("jRB100.text")); // NOI18N
        jRB100.setName("jRB100"); // NOI18N

        jCBRegLuc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Luz Regulable 1" }));
        jCBRegLuc.setName("jCBRegLuc"); // NOI18N

        javax.swing.GroupLayout jPRegLucLayout = new javax.swing.GroupLayout(jPRegLuc);
        jPRegLuc.setLayout(jPRegLucLayout);
        jPRegLucLayout.setHorizontalGroup(
            jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPRegLucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRBApagar)
                        .addGap(47, 47, 47))
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRB25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRB75))
                    .addGroup(jPRegLucLayout.createSequentialGroup()
                        .addComponent(jRB50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRB100))
                    .addComponent(jCBRegLuc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPRegLucLayout.setVerticalGroup(
            jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPRegLucLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBRegLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBApagar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRB25)
                    .addComponent(jRB75))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPRegLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRB50)
                    .addComponent(jRB100)))
        );

        jPConmLuc.setBorder(
                javax.swing.BorderFactory.createTitledBorder(null, 
                resourceMap.getString("jPConmLuc.AccessibleContext.accessibleName"), 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPConmLuc.setName("jPConmLuc"); // NOI18N

        bGConmLuc.add(jRBEncender);
        jRBEncender.setText(resourceMap.getString("jRBEncender.text")); // NOI18N
        jRBEncender.setName("jRBEncender"); // NOI18N

        bGConmLuc.add(jRBApagLuc);
        jRBApagLuc.setText(resourceMap.getString("jRBApagLuc.text")); // NOI18N
        jRBApagLuc.setName("jRBApagLuc"); // NOI18N

        jCBConmLuc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Luz Conmutable 1" }));
        jCBConmLuc.setName("jCBConmLuc"); // NOI18N
        
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        // Establecemos algunos parámetros del jLabel jLPlano, como su nombre
        jLPlano.setName("jLPlano");
        // Y lo añadimos al jLayeredPane (antes era necesario, ahora si se pone, el programa peta)
        //jLPEstancia.add(jLPlano, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        // Indicando que tanto la posición horizontal como vertical de la imagen sea centrada
        jLPlano.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLPlano.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        // ----------------------------------------------------------------------------

        javax.swing.GroupLayout jPConmLucLayout = new javax.swing.GroupLayout(jPConmLuc);
        jPConmLuc.setLayout(jPConmLucLayout);
        jPConmLucLayout.setHorizontalGroup(
            jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPConmLucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBEncender)
                    .addComponent(jRBApagLuc)
                    .addComponent(jCBConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPConmLucLayout.setVerticalGroup(
            jPConmLucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPConmLucLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCBConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jRBEncender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBApagLuc))
        );
        
        

        jPPersianas.setBorder(javax.swing.BorderFactory.createTitledBorder(null,  resourceMap.getString("jPPersianas.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPPersianas.setName("jPPersianas"); // NOI18N

        jBSubir.setText(resourceMap.getString("jBSubir.text")); // NOI18N
        jBSubir.setName("jBSubir"); // NOI18N

        jBBajar.setText(resourceMap.getString("jBBajar.text")); // NOI18N
        jBBajar.setName("jBBajar"); // NOI18N

        jCBPersianas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Persiana 1" }));
        jCBPersianas.setName("jCBPersianas"); // NOI18N

        javax.swing.GroupLayout jPPersianasLayout = new javax.swing.GroupLayout(jPPersianas);
        jPPersianas.setLayout(jPPersianasLayout);
        jPPersianasLayout.setHorizontalGroup(
            jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPPersianasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jBBajar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBSubir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPPersianasLayout.setVerticalGroup(
            jPPersianasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPPersianasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBSubir)
                .addGap(4, 4, 4)
                .addComponent(jBBajar)
                .addGap(7, 7, 7))
        );

        jPValvula.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPValvula.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPValvula.setName("jPValvula"); // NOI18N

        bGValvula.add(jRBAbrir);
        jRBAbrir.setText(resourceMap.getString("jRBAbrir.text")); // NOI18N
        jRBAbrir.setName("jRBAbrir"); // NOI18N

        bGValvula.add(jRBCerrar);
        jRBCerrar.setText(resourceMap.getString("jRBCerrar.text")); // NOI18N
        jRBCerrar.setName("jRBCerrar"); // NOI18N

        jCBValvula.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Válvula 1" }));
        jCBValvula.setName("jCBValvula"); // NOI18N

        javax.swing.GroupLayout jPValvulaLayout = new javax.swing.GroupLayout(jPValvula);
        jPValvula.setLayout(jPValvulaLayout);
        jPValvulaLayout.setHorizontalGroup(
            jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPValvulaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBAbrir)
                    .addComponent(jRBCerrar)
                    .addComponent(jCBValvula, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPValvulaLayout.setVerticalGroup(
            jPValvulaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPValvulaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jRBAbrir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBCerrar)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPActuadoresLayout = new javax.swing.GroupLayout(jPActuadores);
        jPActuadores.setLayout(jPActuadoresLayout);
        jPActuadoresLayout.setHorizontalGroup(
            jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPRegLuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPConmLuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPPersianas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPValvula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82))
        );
        jPActuadoresLayout.setVerticalGroup(
            jPActuadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPActuadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPRegLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPConmLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPPersianas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPValvula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPSensores.setBorder(javax.swing.BorderFactory.createTitledBorder(null,resourceMap.getString("jPSensores.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPSensores.setName("jPSensores"); // NOI18N

        jPApertura.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPApertura.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N
        jPApertura.setName("jPApertura"); // NOI18N

        jLEstApertura.setText(resourceMap.getString("jLEstApertura.text")); // NOI18N
        jLEstApertura.setName("jLEstApertura"); // NOI18N

        jLResApertura.setText(resourceMap.getString("jLResApertura.text")); // NOI18N
        jLResApertura.setName("jLResApertura"); // NOI18N

        jCBApertura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Puerta 1" }));
        jCBApertura.setName("jCBApertura"); // NOI18N

        javax.swing.GroupLayout jPAperturaLayout = new javax.swing.GroupLayout(jPApertura);
        jPApertura.setLayout(jPAperturaLayout);
        jPAperturaLayout.setHorizontalGroup(
            jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPAperturaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCBApertura, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPAperturaLayout.createSequentialGroup()
                        .addComponent(jLEstApertura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResApertura)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPAperturaLayout.setVerticalGroup(
            jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPAperturaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jCBApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPAperturaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstApertura)
                    .addComponent(jLResApertura))
                .addContainerGap())
        );

        jPMovimiento.setBorder(javax.swing.BorderFactory.createTitledBorder(null,resourceMap.getString("jPMovimiento.AccessibleContext.accessibleName")  , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma 11", 1, 12))); // NOI18N
        jPMovimiento.setName("jPMovimiento"); // NOI18N

        jLEstMovimiento.setText(resourceMap.getString("jLEstMovimiento.text")); // NOI18N
        jLEstMovimiento.setName("jLEstMovimiento"); // NOI18N

        jLResMovimiento.setText(resourceMap.getString("jLResMovimiento.text")); // NOI18N
        jLResMovimiento.setName("jLResMovimiento"); // NOI18N

        jCBMovimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Detector 1" }));
        jCBMovimiento.setName("jCBMovimiento"); // NOI18N

        javax.swing.GroupLayout jPMovimientoLayout = new javax.swing.GroupLayout(jPMovimiento);
        jPMovimiento.setLayout(jPMovimientoLayout);
        jPMovimientoLayout.setHorizontalGroup(
            jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPMovimientoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jCBMovimiento, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLEstMovimiento, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLResMovimiento)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPMovimientoLayout.setVerticalGroup(
            jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPMovimientoLayout.createSequentialGroup()
                .addComponent(jCBMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPMovimientoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstMovimiento)
                    .addComponent(jLResMovimiento))
                .addContainerGap())
        );

        jPTemp.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPTemp.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma 11 12", 1, 12))); // NOI18N
        jPTemp.setName("jPTemp"); // NOI18N

        jLEstTemp.setText(resourceMap.getString("jLEstTemp.text")); // NOI18N
        jLEstTemp.setName("jLEstTemp"); // NOI18N

        jLResTemp.setText(resourceMap.getString("jLResTemp.text")); // NOI18N
        jLResTemp.setName("jLResTemp"); // NOI18N

        javax.swing.GroupLayout jPTempLayout = new javax.swing.GroupLayout(jPTemp);
        jPTemp.setLayout(jPTempLayout);
        jPTempLayout.setHorizontalGroup(
            jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTempLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLEstTemp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLResTemp)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPTempLayout.setVerticalGroup(
            jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPTempLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPTempLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstTemp)
                    .addComponent(jLResTemp))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPSenComb.setBorder(javax.swing.BorderFactory.createTitledBorder(null,resourceMap.getString("jPSenComb.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma 11 12 12", 1, 12))); // NOI18N
        jPSenComb.setName("jPSenComb"); // NOI18N

        jLEstTempComb.setText(resourceMap.getString("jLEstTempComb.text")); // NOI18N
        jLEstTempComb.setName("jLEstTempComb"); // NOI18N

        jLResTempComb.setText(resourceMap.getString("jLResTempComb.text")); // NOI18N
        jLResTempComb.setName("jLResTempComb"); // NOI18N

        jLEstLumComb.setText(resourceMap.getString("jLEstLumComb.text")); // NOI18N
        jLEstLumComb.setName("jLEstLumComb"); // NOI18N

        jLResLumComb.setText(resourceMap.getString("jLResLumComb.text")); // NOI18N
        jLResLumComb.setName("jLResLumComb"); // NOI18N

        javax.swing.GroupLayout jPSenCombLayout = new javax.swing.GroupLayout(jPSenComb);
        jPSenComb.setLayout(jPSenCombLayout);
        jPSenCombLayout.setHorizontalGroup(
            jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSenCombLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPSenCombLayout.createSequentialGroup()
                        .addComponent(jLEstTempComb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResTempComb))
                    .addGroup(jPSenCombLayout.createSequentialGroup()
                        .addComponent(jLEstLumComb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLResLumComb)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPSenCombLayout.setVerticalGroup(
            jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSenCombLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstTempComb)
                    .addComponent(jLResTempComb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPSenCombLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstLumComb)
                    .addComponent(jLResLumComb))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPInundacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPInundacion.AccessibleContext.accessibleName") , javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma 11 12 12", 1, 12))); // NOI18N
        jPInundacion.setName("jPInundacion"); // NOI18N

        jLEstInundacion.setText(resourceMap.getString("jLEstInundacion.text")); // NOI18N
        jLEstInundacion.setName("jLEstInundacion"); // NOI18N

        jLResInundacion.setText(resourceMap.getString("jLResInundacion.text")); // NOI18N
        jLResInundacion.setName("jLResInundacion"); // NOI18N

        jCBInund.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sensor 1" }));
        jCBInund.setName("jCBInund"); // NOI18N

        javax.swing.GroupLayout jPInundacionLayout = new javax.swing.GroupLayout(jPInundacion);
        jPInundacion.setLayout(jPInundacionLayout);
        jPInundacionLayout.setHorizontalGroup(
            jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPInundacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPInundacionLayout.createSequentialGroup()
                        .addComponent(jLEstInundacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResInundacion))
                    .addComponent(jCBInund, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPInundacionLayout.setVerticalGroup(
            jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPInundacionLayout.createSequentialGroup()
                .addComponent(jCBInund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(jPInundacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstInundacion)
                    .addComponent(jLResInundacion))
                .addContainerGap())
        );

        jPContadores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPContadores.AccessibleContext.accessibleName"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma 11 12 12 12", 1, 12))); // NOI18N
        jPContadores.setName("jPContadores"); // NOI18N

        jLEstPotencia.setText(resourceMap.getString("jLEstPotencia.text")); // NOI18N
        jLEstPotencia.setName("jLEstPotencia"); // NOI18N

        jLResPotencia.setText(resourceMap.getString("jLResPotencia.text")); // NOI18N
        jLResPotencia.setName("jLResPotencia"); // NOI18N

        jLEstEnergia.setText(resourceMap.getString("jLEstEnergia.text")); // NOI18N
        jLEstEnergia.setName("jLEstEnergia"); // NOI18N

        jLResEnergia.setText(resourceMap.getString("jLResEnergia.text")); // NOI18N
        jLResEnergia.setName("jLResEnergia"); // NOI18N

        javax.swing.GroupLayout jPContadoresLayout = new javax.swing.GroupLayout(jPContadores);
        jPContadores.setLayout(jPContadoresLayout);
        jPContadoresLayout.setHorizontalGroup(
            jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPContadoresLayout.createSequentialGroup()
                        .addComponent(jLEstPotencia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLResPotencia))
                    .addGroup(jPContadoresLayout.createSequentialGroup()
                        .addComponent(jLEstEnergia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLResEnergia)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jPContadoresLayout.setVerticalGroup(
            jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPContadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstPotencia)
                    .addComponent(jLResPotencia))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPContadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstEnergia)
                    .addComponent(jLResEnergia))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPSensoresLayout = new javax.swing.GroupLayout(jPSensores);
        jPSensores.setLayout(jPSensoresLayout);
        jPSensoresLayout.setHorizontalGroup(
            jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSensoresLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPInundacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPContadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPSenComb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPSensoresLayout.setVerticalGroup(
            jPSensoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPSensoresLayout.createSequentialGroup()
                .addComponent(jPApertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPMovimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11)
                .addComponent(jPTemp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPSenComb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPInundacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPContadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        
        // ---- Añadir en caso de modificaciones futuras al GUI de EstanciaGenerica ----
        jLPEstancia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLPEstancia.setName("jLPEstancia"); // NOI18N
        
        jLPlano.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLPlano.setMaximumSize(new java.awt.Dimension(390, 400));
        jLPlano.setMinimumSize(new java.awt.Dimension(390, 400));
        jLPlano.setName("jLPlano"); // NOI18N
        jLPlano.setPreferredSize(new java.awt.Dimension(390, 400));
        // ------------------------------------------------------------------------------
        
        javax.swing.GroupLayout jPEstanciaLayout = new javax.swing.GroupLayout(jPEstancia);
        jPEstancia.setLayout(jPEstanciaLayout);
        jPEstanciaLayout.setHorizontalGroup(
            jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLPEstancia, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addComponent(jPSensores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPEstanciaLayout.setVerticalGroup(
            jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPEstanciaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPEstanciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPSensores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPActuadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 953, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPEstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );
    }
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGConmLuc;
    private javax.swing.ButtonGroup bGRegLuc;
    private javax.swing.ButtonGroup bGValvula;
    private javax.swing.JButton jBBajar;
    private javax.swing.JButton jBSubir;
    private javax.swing.JComboBox jCBApertura;
    private javax.swing.JComboBox jCBConmLuc;
    private javax.swing.JComboBox jCBInund;
    private javax.swing.JComboBox jCBMovimiento;
    private javax.swing.JComboBox jCBPersianas;
    private javax.swing.JComboBox jCBRegLuc;
    private javax.swing.JComboBox jCBValvula;
    private javax.swing.JLabel jLEstApertura;
    private javax.swing.JLabel jLEstInundacion;
    private javax.swing.JLabel jLEstLumComb;
    private javax.swing.JLabel jLEstMovimiento;
    private javax.swing.JLabel jLEstPotencia;
    private javax.swing.JLabel jLEstTemp;
    private javax.swing.JLabel jLEstTempComb;
    private javax.swing.JLayeredPane jLPEstancia;
    private javax.swing.JLabel jLPlano;
    private javax.swing.JLabel jLResApertura;
    private javax.swing.JLabel jLResInundacion;
    private javax.swing.JLabel jLResLumComb;
    private javax.swing.JLabel jLResMovimiento;
    private javax.swing.JLabel jLResPotencia;
    private javax.swing.JLabel jLResTemp;
    private javax.swing.JLabel jLResTempComb;
    private javax.swing.JPanel jPActuadores;
    private javax.swing.JPanel jPApertura;
    private javax.swing.JPanel jPConmLuc;
    private javax.swing.JPanel jPContadores;
    private javax.swing.JPanel jPEstancia;
    private javax.swing.JPanel jPInundacion;
    private javax.swing.JPanel jPMovimiento;
    private javax.swing.JPanel jPPersianas;
    private javax.swing.JPanel jPRegLuc;
    private javax.swing.JPanel jPSenComb;
    private javax.swing.JPanel jPSensores;
    private javax.swing.JPanel jPTemp;
    private javax.swing.JPanel jPValvula;
    private javax.swing.JRadioButton jRB100;
    private javax.swing.JRadioButton jRB25;
    private javax.swing.JRadioButton jRB50;
    private javax.swing.JRadioButton jRB75;
    private javax.swing.JRadioButton jRBAbrir;
    private javax.swing.JRadioButton jRBApagLuc;
    private javax.swing.JRadioButton jRBApagar;
    private javax.swing.JRadioButton jRBCerrar;
    private javax.swing.JRadioButton jRBEncender;
    // End of variables declaration//GEN-END:variables
*/
}
