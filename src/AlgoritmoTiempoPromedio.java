
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author luis
 */
public class AlgoritmoTiempoPromedio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Panel panel = new Panel();
        JFrame ventana = new JFrame("Algoritmo Tiempo Promedio");
        ventana.add(panel);
        ventana.pack();
        ventana.setDefaultCloseOperation(ventana.EXIT_ON_CLOSE);
        ventana.setVisible(true);
    }
    
}
