
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
        } else {
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
                    fila[1] = hora.get(Calendar.HOUR) + ":" + "0"+ hora.get(Calendar.MINUTE);        
                }
                else{
                    fila[1] = hora.get(Calendar.HOUR) + ":" + hora.get(Calendar.MINUTE);
                }
            fila[2] = maquina.getVelocidadRelojMaquina();

            tablaModelo.addRow(fila);

            tablaMaquinas.setModel(tablaModelo);

            maquinas.add(maquina);

            //textoNumeroMaquina.setText(String.valueOf(Integer.parseInt(textoNumeroMaquina.getText()) + 1));
            textoNumeroMaquina.setText(String.valueOf(Maquina.getCantidadMaquinas() + 1));
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
            }   else {
              lista.addElement("Maquina Descartada: N° " + maquina.getNumeroMaquina());
            }
        }
        
        textoNuevaHora.setText(String.valueOf(Math.rint((acumuladorHora / cantidadMaquinasAceptadas + (Float.parseFloat(textoPropagacion.getText()))/100) * 100) / 100));
        textoIntervalo.setText(String.valueOf(Integer.parseInt(textoIntervalo.getText()) + 1));
        horaNueva = horaNueva + "0) / " + cantidadMaquinasAceptadas + " + " + textoPropagacion.getText() + " = " + textoNuevaHora.getText();
        lista.addElement(horaNueva);
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
    
    private void actualizaHoras(){
        
        float nuevaHora;
        int hora,minuto;
        Maquina maquina;
        String cambioHora;
        nuevaHora=Float.valueOf(textoNuevaHora.getText());
        hora=(int) nuevaHora;
        minuto=(int) Math.rint(((nuevaHora-hora)*100)*100)/100;
        
        for (int i=0; i<maquinas.size();i++){
            maquina=maquinas.get(i);
            cambioHora=("Hora de la Maquina N° " + maquina.getNumeroMaquina() + " = " + maquina.getHoraActual().get(Calendar.HOUR)+":"+ maquina.getHoraActual().get(Calendar.MINUTE));
            maquina.getHoraActual().set(Calendar.HOUR, hora);
            maquina.getHoraActual().set(Calendar.MINUTE, minuto+maquina.getVelocidadRelojMaquina());
            lista.addElement (cambioHora + " -> " + maquina.getHoraActual().get(Calendar.HOUR)+":"+ maquina.getHoraActual().get(Calendar.MINUTE));
            
            tablaMaquinas.setValueAt(maquina.getHoraActual().get(Calendar.HOUR)+ ":" + maquina.getHoraActual().get(Calendar.MINUTE), i, 1);
        }  
       
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
        jButton1 = new javax.swing.JButton();
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
        jButton4 = new javax.swing.JButton();
        Cancelar = new javax.swing.JButton();
        etiquetaNuevaHora = new javax.swing.JLabel();
        textoNuevaHora = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Registro de Maquinas"));

        jButton1.setText("Continuar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
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
        jScrollPane1.setViewportView(tablaMaquinas);

        jLabel1.setText("Tiempo Propagacion:");

        jLabel2.setText("Tiempo intervalo:");

        textoPropagacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textoPropagacionActionPerformed(evt);
            }
        });
        textoPropagacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textoPropagacionKeyTyped(evt);
            }
        });

        jLabel3.setText("N° Maquina:");

        textoNumeroMaquina.setEditable(false);

        jLabel4.setText("Hora Actual:");

        jLabel5.setText("Velocidad:");

        textoVelocidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                textoVelocidadKeyTyped(evt);
            }
        });

        botonNuevo.setText("Nueva");
        botonNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonNuevoActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(listaPasoAPaso);

        botonGuardar.setText("Guardar");

        jButton4.setText("Editar");

        Cancelar.setText("Cancelar");

        etiquetaNuevaHora.setText("Nueva hora");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(textoPropagacion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Cancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(textoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(etiquetaNuevaHora)
                    .addComponent(textoNuevaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
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
                    .addComponent(ComboHoras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textoVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonNuevo)
                    .addComponent(botonGuardar))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(Cancelar))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(textoIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(etiquetaNuevaHora))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoNuevaHora, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textoPropagacion)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
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

    private void textoPropagacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textoPropagacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textoPropagacionActionPerformed

    private void botonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonNuevoActionPerformed
        registrar();
        textoVelocidad.setText("");
        ComboHoras.setSelectedIndex(0);
        ComboMinutos.setSelectedIndex(0);       
    }//GEN-LAST:event_botonNuevoActionPerformed

    private void textoVelocidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoVelocidadKeyTyped
        validarSoloNumeros(evt);
    }//GEN-LAST:event_textoVelocidadKeyTyped

    private void textoPropagacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textoPropagacionKeyTyped
        validarSoloNumeros(evt);
    }//GEN-LAST:event_textoPropagacionKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        aplicarAlgoritmoTiempoPromedio();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancelar;
    private javax.swing.JComboBox ComboHoras;
    private javax.swing.JComboBox ComboMinutos;
    private javax.swing.JButton botonGuardar;
    private javax.swing.JButton botonNuevo;
    private javax.swing.JLabel etiquetaNuevaHora;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
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
