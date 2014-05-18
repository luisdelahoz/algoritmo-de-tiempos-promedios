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
    
    
    private int NumeroMaquina;
    private Calendar HoraActual;
    private int VelocidadRelojMaquina;
    
    private static int cantidadMaquinas = 0;

    public Maquina() {
        cantidadMaquinas++;
        this.NumeroMaquina = cantidadMaquinas;
        this.HoraActual = null;
        this.VelocidadRelojMaquina = 0;
    }
    
    public int getNumeroMaquina() {
            return NumeroMaquina;
    }

    public void setNumeroMaquina(int NumeroMaquina) {
        this.NumeroMaquina = NumeroMaquina;
    }

        public Calendar getHoraActual() {
            return HoraActual;
        }

        public void setHoraActual(Calendar HoraActual) {
            this.HoraActual = HoraActual;
        }

        public int getVelocidadRelojMaquina() {
            return VelocidadRelojMaquina;
        }

        public void setVelocidadRelojMaquina(int VelocidadRelojMaquina) {
            this.VelocidadRelojMaquina = VelocidadRelojMaquina;
        }
}
