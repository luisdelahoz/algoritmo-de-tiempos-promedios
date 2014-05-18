/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.table.DefaultTableModel;

/**
 *
 * @author luis
 */
public class TablaNoEditable extends DefaultTableModel{
    
    public TablaNoEditable(Object[][] data, Object[] nombreColumnas){
        super(data, nombreColumnas);
    }

    @Override
    public boolean isCellEditable (int fila, int columna) {
        return false;
    }
}
