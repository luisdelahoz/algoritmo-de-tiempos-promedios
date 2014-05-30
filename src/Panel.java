
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author luis
 */
public class Panel extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    DefaultListModel lista;
    
    LinkedList<Maquina> maquinas = new LinkedList<>();
    
    public Panel() {
        initComponents();
        cargarHoras();
        cargarMinutos();
        textoVelocidad.setEditable(false);
        textoNuevaHora.setEditable(false);
        ComboHoras.setEnabled(false);
        ComboMinutos.setEnabled(false);
        textoIntervalo.setEditable(false);
        textoNumeroMaquina.setText("1");
        textoIntervalo.setText("0");
        lista = new DefaultListModel();
        listaPasoAPaso.setModel(lista);
        
    }
    
    private void cargarHoras(){
        DefaultComboBoxModel lista = new DefaultComboBoxModel();
        for(int i = 0; i < 24; i++){
            lista.addElement(i);
        }
        
        ComboHoras.setModel(lista);
    }
    
    private void cargarMinutos(){
        DefaultComboBoxModel lista = new DefaultComboBoxModel();
        for(int i= 0; i < 60; i++){
            lista.addElement(i);
        }
        ComboMinutos.setModel(lista);
    }
    
    private void registrar(){

        if("".equals(textoVelocidad.getText().trim())){
            JOptionPane.showMessageDialog(null, "Falta ingresar la velocidad del reloj");
        } else if("Guardar".equals(botonGuardar.getText())){
            Calendar hora = new GregorianCalendar();
            Maquina maquina = new Maquina();

            hora.set(Calendar.HOUR, Integer.parseInt(String.valueOf(ComboHoras.getSelectedItem())));
            hora.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(ComboMinutos.getSelectedItem())));

            maquina.setVelocidadRelojMaquina(Integer.parseInt(textoVelocidad.getText()));
            maquina.setHoraActual(hora);

            DefaultTableModel tablaModelo = (DefaultTableModel) tablaMaquinas.getModel();

            Object fila[] = new Object[3];
            fila[0] = maquina.getNumeroMaquina();
                if(hora.get(Calendar.MINUTE)< 10){
                    fila[1] = hora.get(Calendar.HOUR) + ":0"+ hora.get(Calendar.MINUTE);        
                }
                else{
                    fila[1] = hora.get(Calendar.HOUR) + ":" + hora.get(Calendar.MINUTE);
                }
            fila[2] = maquina.getVelocidadRelojMaquina();

            tablaModelo.addRow(fila);

            tablaMaquinas.setModel(tablaModelo);
            botonGuardar.setEnabled(false);

            maquinas.add(maquina);

            //textoNumeroMaquina.setText(String.valueOf(Integer.parseInt(textoNumeroMaquina.getText()) + 1));
            textoNumeroMaquina.setText(String.valueOf(Maquina.getCantidadMaquinas() + 1));
            
            arranque();
            
        } else {
            
 
            if(Integer.parseInt(String.valueOf(ComboMinutos.getSelectedItem())) < 10){
                tablaMaquinas.setValueAt(ComboHoras.getSelectedItem() + ":0" + ComboMinutos.getSelectedItem(), tablaMaquinas.getSelectedRow(), 1);
            } else {
                tablaMaquinas.setValueAt(ComboHoras.getSelectedItem() + ":" + ComboMinutos.getSelectedItem(), tablaMaquinas.getSelectedRow(), 1);
            }
            
            tablaMaquinas.setValueAt(textoVelocidad.getText(), tablaMaquinas.getSelectedRow(), 2);
            
            for(Maquina maquina: maquinas){
                if(maquina.getNumeroMaquina() == Integer.parseInt(String.valueOf(tablaMaquinas.getValueAt(tablaMaquinas.getSelectedRow(), 0)))){
                    maquina.getHoraActual().set(Calendar.HOUR, Integer.parseInt(String.valueOf(ComboHoras.getSelectedItem())));
                    maquina.getHoraActual().set(Calendar.MINUTE, Integer.parseInt(String.valueOf(ComboMinutos.getSelectedItem())));
                    maquina.setVelocidadRelojMaquina(Integer.parseInt(textoVelocidad.getText()));
                    break;
                }
            }
            
            arranque();
            
        }
        
        if(tablaMaquinas.getRowCount() > 1){
            botonContinuar.setEnabled(true);
            textoPropagacion.setEnabled(true);
        }
        
    }
    
    private int obtenerTiempoMaquinaSeleccionada(int numeroMaquina)
    {
        for(Maquina maquina: maquinas){
            if(maquina.getNumeroMaquina() == numeroMaquina){
                return maquina.getHoraActualMinutos();
            }
        }
        return 0;
        
    }
    
    private void aplicarAlgoritmoTiempoPromedio(){
          
        int numeroMaquina = Integer.valueOf(String.valueOf(tablaMaquinas.getValueAt(tablaMaquinas.getSelectedRow(), 0)));
        int tiempoMaquinaSeleccionada = obtenerTiempoMaquinaSeleccionada(numeroMaquina);
        float acumuladorHora = 0;
        int cantidadMaquinasAceptadas = 0;
        String horaNueva = "Nueva Hora = ("; 
        lista.addElement("Intervalo: " + textoIntervalo.getText() + " / Maquina N°: " + numeroMaquina);
        
        if(!"0".equals(textoIntervalo.getText())){
            actualizaHoras();
        }
        
        for(Maquina maquina: maquinas){
            if(Math.abs(tiempoMaquinaSeleccionada - maquina.getHoraActualMinutos()) < 60){
                cantidadMaquinasAceptadas++;
                acumuladorHora = acumuladorHora + (maquina.getHoraActual().get(Calendar.HOUR) + (float) (maquina.getHoraActual().get(Calendar.MINUTE)) / 100);
                horaNueva = horaNueva +(maquina.getHoraActual().get(Calendar.HOUR) + (float) (maquina.getHoraActual().get(Calendar.MINUTE))/ 100) + " + ";
            } else {
              lista.addElement("Maquina Descartada: N° " + maquina.getNumeroMaquina());
            }
        }
        
        textoNuevaHora.setText(String.valueOf(Math.rint((acumuladorHora / cantidadMaquinasAceptadas + (Float.parseFloat(textoPropagacion.getText()))/100) * 100) / 100));
        textoIntervalo.setText(String.valueOf(Integer.parseInt(textoIntervalo.getText()) + 1));
        horaNueva = horaNueva + "0) / " + cantidadMaquinasAceptadas + " + " + textoPropagacion.getText() + " = " + textoNuevaHora.getText();
        lista.addElement(horaNueva);
    }
    
    
    private void actualizaHoras(){
        
        float nuevaHora;
        int hora,minuto;
        Maquina maquina;
        String cambioHora;
        nuevaHora = Float.valueOf(textoNuevaHora.getText());
        hora = (int) nuevaHora;
        minuto = (int) Math.rint(((nuevaHora-hora)*100)*100)/100;
        
        for (int i = 0; i < maquinas.size(); i++){
            maquina = maquinas.get(i);
            cambioHora = ("Hora de la Maquina N° " + maquina.getNumeroMaquina() + " = " + maquina.getHoraActual().get(Calendar.HOUR)+":"+ maquina.getHoraActual().get(Calendar.MINUTE));
            maquina.getHoraActual().set(Calendar.HOUR, hora);
            maquina.getHoraActual().set(Calendar.MINUTE, minuto+maquina.getVelocidadRelojMaquina());
            lista.addElement(cambioHora + " -> " + maquina.getHoraActual().get(Calendar.HOUR)+":"+ maquina.getHoraActual().get(Calendar.MINUTE));
            
            tablaMaquinas.setValueAt(maquina.getHoraActual().get(Calendar.HOUR)+ ":" + maquina.getHoraActual().get(Calendar.MINUTE), i, 1);
        }  
       
    }
    
    private void validarSoloNumeros(java.awt.event.KeyEvent evt){
        char caracter = evt.getKeyChar();
        int numeroCaracter = (int) caracter; // Se obtiene el entero que representa cual tecla se ha presionado, necesario para validar backspace, enter
        // Extraido de http://stackoverflow.com/questions/15693904/java-keylistener-keytyped-backspace-esc-as-input
//        System.out.println(numeroCaracter + " " + caracter);
        if((caracter < '0' || caracter > '9') && (numeroCaracter != 8) && (numeroCaracter != 10) && (numeroCaracter != 32)){
//            System.out.println(numeroCaracter != 8);
            JOptionPane.showMessageDialog(this, "Solo puede ingresar numeros", "Aviso!", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
    }
    
    private void limpiarCampos(){
        textoVelocidad.setText("");
        //textoNuevaHora.setText("");
        //textoPropagacion.setText("");
        ComboHoras.setSelectedIndex(0);
        ComboMinutos.setSelectedIndex(0);        
    }
   
    private void cargarCampos(){
        
        String hora = String.valueOf(tablaMaquinas.getValueAt(tablaMaquinas.getSelectedRow(), 1));
        
        textoNumeroMaquina.setText(String.valueOf(tablaMaquinas.getValueAt(tablaMaquinas.getSelectedRow(), 0)));
        textoVelocidad.setText(String.valueOf(tablaMaquinas.getValueAt(tablaMaquinas.getSelectedRow(), 2)));
        
        ComboHoras.setSelectedItem(Integer.parseInt(hora.substring(0, hora.indexOf(":")).trim()));
        ComboMinutos.setSelectedItem(Integer.parseInt(hora.substring(hora.indexOf(":") + 1, hora.length()).trim()));
        
    }
    
    private void habilitarCampos(){
        ComboHoras.setEnabled(true);
        ComboMinutos.setEnabled(true);
        textoVelocidad.setEditable(true);
    }
    
    private void deshabilitarCampos(){
        ComboHoras.setEnabled(false);
        ComboMinutos.setEnabled(false);
        textoVelocidad.setEditable(false);
    }
    
    public final void arranque(){
        limpiarCampos();
        deshabilitarCampos();
        botonGuardar.setText("Guardar");
        tablaMaquinas.setEnabled(true);
        botonNuevo.setEnabled(true);
        botonGuardar.setEnabled(false);
//        botonEliminar.setEnabled(false);
        botonEditar.setEnabled(false);
    }
    
    private void nuevo(){
        limpiarCampos();
        habilitarCampos();
        tablaMaquinas.setEnabled(false);
        botonGuardar.setEnabled(true);   
    }  
    
    private void editar(){
        habilitarCampos();
        botonGuardar.setText("Actualizar");
        botonGuardar.setEnabled(true);
        tablaMaquinas.setEnabled(false);
    }
    
    public void deshabilitarBotones(){
        botonCancelar.setEnabled(false);
        botonEditar.setEnabled(false);
        botonGuardar.setEnabled(false);
        botonNuevo.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        botonContinuar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMaquinas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textoPropagacion = new javax.swing.JTextField();
        textoIntervalo = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        textoNumeroMaquina = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textoVelocidad = new javax.swing.JTextField();
        botonNuevo = new javax.swing.JButton();
        ComboHoras = new javax.swing.JComboBox();
        ComboMinutos = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaPasoAPaso = new javax.swing.JList();
        botonGuardar = new javax.swing.JButton();
        botonEditar = new javax.swing.JButton();
        botonCancelar = new javax.swing.JButton();
        etiquetaNuevaHora = new javax.swing.JLabel();
        textoNuevaHora = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro de Maquinas"));

        botonContinuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/siguiente.png"))); // NOI18N
        botonContinuar.setText("Continuar");
        botonContinuar.setToolTipText("RECUERDA necesitas al menos 2 máquinas. Presionar para aplicar el método");
        botonContinuar.setEnabled(false);
        botonContinuar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonContinuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonContinuarActionPerformed(evt);
            }
        });

        tablaMaquinas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Maquina", "Hora Actual", "Velocidad del Reloj"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaMaquinas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMaquinasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMaquinas);

        jLabel1.setText("Tiempo Propagacion:");

        jLabel2.setText("Tiempo intervalo:");

        textoPropagacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textoPropagacionKeyTyped(evt);
            }
        });

        textoIntervalo.setEditable(false);
        textoIntervalo.setBackground(new java.awt.Color(238, 238, 238));

        jLabel3.setText("N° Maquina:");

        textoNumeroMaquina.setEditable(false);

        jLabel4.setText("Hora Actual:");

        jLabel5.setText("Velocidad:");

        textoVelocidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textoVelocidadKeyTyped(evt);
            }
        });

        botonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        botonNuevo.setText("Nueva");
        botonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNuevoActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(listaPasoAPaso);

        botonGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        botonGuardar.setText("Guardar");
        botonGuardar.setEnabled(false);
        botonGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGuardarActionPerformed(evt);
            }
        });

        botonEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        botonEditar.setText("Editar");
        botonEditar.setEnabled(false);
        botonEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEditarActionPerformed(evt);
            }
        });

        botonCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        botonCancelar.setText("Cancelar");
        botonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCancelarActionPerformed(evt);
            }
        });

        etiquetaNuevaHora.setText("Nueva hora");

        textoNuevaHora.setEditable(false);
        textoNuevaHora.setBackground(new java.awt.Color(238, 238, 238));
        textoNuevaHora.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(textoPropagacion, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(botonContinuar, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                        .addComponent(textoNuevaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addGap(57, 57, 57)
                                                .addComponent(textoNumeroMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addComponent(jLabel5))
                                                .addGap(54, 54, 54)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(textoVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(ComboHoras, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(ComboMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(botonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(botonEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(botonCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                    .addComponent(botonNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(29, 29, 29))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(etiquetaNuevaHora)
                        .addGap(54, 54, 54))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textoNumeroMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(ComboMinutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ComboHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textoVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonGuardar)
                    .addComponent(botonEditar)
                    .addComponent(botonCancelar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etiquetaNuevaHora))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoNuevaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoPropagacion)
                    .addComponent(botonContinuar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void botonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNuevoActionPerformed
      nuevo();
      /*botonGuardar.setEnabled(true);
      //botonContinuar.setEnabled(true);
      botonCancelar.setEnabled(true);
      botonNuevo.setEnabled(false);
      textoVelocidad.setEditable(true);
      //textoPropagacion.setEditable(true);
      ComboHoras.setEnabled(true);
      ComboMinutos.setEnabled(true);*/
    }//GEN-LAST:event_botonNuevoActionPerformed

    private void textoVelocidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoVelocidadKeyTyped
        validarSoloNumeros(evt);
    }//GEN-LAST:event_textoVelocidadKeyTyped

    private void textoPropagacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoPropagacionKeyTyped
        validarSoloNumeros(evt);
    }//GEN-LAST:event_textoPropagacionKeyTyped

    private void botonContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonContinuarActionPerformed
        //boolean aplicar=true;
        if(textoPropagacion.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Ingrese Tiempo de Propagacion");
            //aplicar=false;
        } else if(tablaMaquinas.getSelectedRow() == -1){
            JOptionPane.showMessageDialog(null, "Seleccione la Maquina");
            //aplicar=false;
        } else {
            deshabilitarBotones();
            textoPropagacion.setEditable(false);
            aplicarAlgoritmoTiempoPromedio();
        }
            
        
        /*if (tablaMaquinas.getRowCount()==0){
            JOptionPane.showMessageDialog(null, "Ingrese Maquinas");
            aplicar=false;
        }
        if(tablaMaquinas.getSelectedRow()==-1){
            JOptionPane.showMessageDialog(null, "Seleccione la Maquina");
            aplicar=false;
        }
        if (aplicar)
            aplicarAlgoritmoTiempoPromedio();
        }*/
    }//GEN-LAST:event_botonContinuarActionPerformed

    private void botonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGuardarActionPerformed
      registrar();
      /*textoVelocidad.setText("");
      ComboHoras.setSelectedIndex(0);
      ComboMinutos.setSelectedIndex(0); 
      botonEditar.setText("Editar");
      botonNuevo.setEnabled(true);
      textoVelocidad.setEditable(false);
      //textoPropagacion.setEditable(false);
      ComboHoras.setEnabled(false);
      ComboMinutos.setEnabled(false);*/
    }//GEN-LAST:event_botonGuardarActionPerformed

    private void botonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCancelarActionPerformed
        arranque();
        /*limpiarCampos();
        botonGuardar.setEnabled(false);
        botonEditar.setEnabled(false);
        //botonContinuar.setEnabled(false);
        botonNuevo.setEnabled(true);
        tablaMaquinas.clearSelection();
        botonEditar.setText("Editar");
        textoVelocidad.setEditable(false);
        //textoPropagacion.setEditable(false);
        ComboHoras.setEnabled(false);
        ComboMinutos.setEnabled(false);*/
    }//GEN-LAST:event_botonCancelarActionPerformed

    private void botonEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEditarActionPerformed
        editar();
    }//GEN-LAST:event_botonEditarActionPerformed

    private void tablaMaquinasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMaquinasMouseClicked
        if(tablaMaquinas.isEnabled()){
            if(tablaMaquinas.getSelectedRow() != -1){
                botonEditar.setEnabled(true);
                botonNuevo.setEnabled(false);
                cargarCampos();
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaMaquinasMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox ComboHoras;
    private javax.swing.JComboBox ComboMinutos;
    private javax.swing.JButton botonCancelar;
    private javax.swing.JButton botonContinuar;
    private javax.swing.JButton botonEditar;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton botonNuevo;
    private javax.swing.JLabel etiquetaNuevaHora;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList listaPasoAPaso;
    private javax.swing.JTable tablaMaquinas;
    private javax.swing.JTextField textoIntervalo;
    private javax.swing.JTextField textoNuevaHora;
    private javax.swing.JTextField textoNumeroMaquina;
    private javax.swing.JTextField textoPropagacion;
    private javax.swing.JTextField textoVelocidad;
    // End of variables declaration//GEN-END:variables
}
