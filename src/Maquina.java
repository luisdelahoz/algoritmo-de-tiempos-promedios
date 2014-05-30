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
    private int horaActualMinutos;
    private int velocidadRelojMaquina;
    
    private static int cantidadMaquinas = 0;

    public Maquina() {
        cantidadMaquinas++;
        this.numeroMaquina = cantidadMaquinas;
        this.horaActual = null;
        this.horaActualMinutos = 0;
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

    public void setHoraActual(Calendar horaActual) {
        this.horaActual = horaActual;
        this.horaActualMinutos = horaActual.get(Calendar.HOUR) * 60 + horaActual.get(Calendar.MINUTE);
    }

    public int getVelocidadRelojMaquina() {
        return velocidadRelojMaquina;
    }

    public void setVelocidadRelojMaquina(int velocidadRelojMaquina) {
        this.velocidadRelojMaquina = velocidadRelojMaquina;
    }

    public int getHoraActualMinutos() {
        return horaActualMinutos;
    }

    public void setHoraActualMinutos(int horaActualMinutos) {
        this.horaActualMinutos = horaActualMinutos;
    }

    public static int getCantidadMaquinas() {
        return cantidadMaquinas;
    }

    public static void setCantidadMaquinas(int cantidadMaquinas) {
        Maquina.cantidadMaquinas = cantidadMaquinas;
    }
}
