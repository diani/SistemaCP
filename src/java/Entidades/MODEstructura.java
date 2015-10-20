/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;

/**
 *
 * @author diani
 */

      
public class MODEstructura implements Serializable{

    private Usuario empleado;
    private Float valorHora;
    private Float CantHorasTrabajadas;
    private Float sueldoTotal;
    private Float valorHorasExtra;

    public MODEstructura(Usuario empleado, Float valorHora, Float CantHorasTrabajadas, Float sueldoTotal, Float valorHorasExtra) {
        this.empleado = empleado;
        this.valorHora = valorHora;
        this.CantHorasTrabajadas = CantHorasTrabajadas;
        this.sueldoTotal = sueldoTotal;
        this.valorHorasExtra = valorHorasExtra;
    }

    public MODEstructura() {
    }
    
    public Usuario getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
    }

    public Float getValorHora() {
        return valorHora;
    }

    public void setValorHora(Float valorHora) {
        this.valorHora = valorHora;
    }

    public Float getCantHorasTrabajadas() {
        return CantHorasTrabajadas;
    }

    public void setCantHorasTrabajadas(Float CantHorasTrabajadas) {
        this.CantHorasTrabajadas = CantHorasTrabajadas;
    }

    public Float getSueldoTotal() {
        return sueldoTotal;
    }

    public void setSueldoTotal(Float sueldoTotal) {
        this.sueldoTotal = sueldoTotal;
    }

    public Float getValorHorasExtra() {
        return valorHorasExtra;
    }

    public void setValorHorasExtra(Float valorHorasExtra) {
        this.valorHorasExtra = valorHorasExtra;
    }
    
    
    
}
