/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Calendar;
/**
 *
 * @author luis
 */
public class Maquina {
    
    
    private int numeroMaquina;
    private Calendar horaActual;
    private int velocidadRelojMaquina;
    
    private static int cantidadMaquinas = 0;

    public Maquina() {
        cantidadMaquinas++;
        this.numeroMaquina = cantidadMaquinas;
        this.horaActual = null;
        this.velocidadRelojMaquina = 0;
    }
    
    public int getNumeroMaquina() {
            return numeroMaquina;
    }

    public void setNumeroMaquina(int NumeroMaquina) {
        this.numeroMaquina = NumeroMaquina;
    }


    public Calendar getHoraActual() {
        return horaActual;
    }

    public void setHoraActual(Calendar HoraActual) {
        this.horaActual = HoraActual;
    }

    public int getVelocidadRelojMaquina() {
        return velocidadRelojMaquina;
    }

    public void setVelocidadRelojMaquina(int VelocidadRelojMaquina) {
        this.velocidadRelojMaquina = VelocidadRelojMaquina;
    }
}
