import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

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
        
        JFrame ventana = new JFrame("Algoritmo de Tiempo Promedio");
        ventana.add(panel);
        ventana.pack();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setVisible(true);
        ventana.setIconImage(new javax.swing.ImageIcon(ventana.getClass().getResource("/imagenes/reloj48.png")).getImage());


    }
    
}
