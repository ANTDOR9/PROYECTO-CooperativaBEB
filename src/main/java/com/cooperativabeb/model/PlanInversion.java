package com.cooperativabeb.model;

import java.util.Date;

public class PlanInversion {
    private int idPlan;
    private int idCliente;
    private int idProducto;
    private int idAsesor;
    private double montoInvertido;
    private double tasaPactada;
    private int plazoMeses;
    private Date fechaInicio;
    private Date fechaVencimiento;
    private String estado;

    public PlanInversion() {}

    public PlanInversion(int idPlan, int idCliente, int idProducto,
                         int idAsesor, double montoInvertido, double tasaPactada,
                         int plazoMeses, Date fechaInicio,
                         Date fechaVencimiento, String estado) {
        this.idPlan = idPlan;
        this.idCliente = idCliente;
        this.idProducto = idProducto;
        this.idAsesor = idAsesor;
        this.montoInvertido = montoInvertido;
        this.tasaPactada = tasaPactada;
        this.plazoMeses = plazoMeses;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }

    public double calcularGanancia() {
        return montoInvertido * (tasaPactada / 100) * (plazoMeses / 12.0);
    }

    public double calcularMontoFinal() {
        return montoInvertido + calcularGanancia();
    }

    public int getIdPlan() { return idPlan; }
    public void setIdPlan(int idPlan) { this.idPlan = idPlan; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getIdAsesor() { return idAsesor; }
    public void setIdAsesor(int idAsesor) { this.idAsesor = idAsesor; }
    public double getMontoInvertido() { return montoInvertido; }
    public void setMontoInvertido(double montoInvertido) { this.montoInvertido = montoInvertido; }
    public double getTasaPactada() { return tasaPactada; }
    public void setTasaPactada(double tasaPactada) { this.tasaPactada = tasaPactada; }
    public int getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "PlanInversion{monto=" + montoInvertido + ", tasa=" + tasaPactada + "}";
    }
}
