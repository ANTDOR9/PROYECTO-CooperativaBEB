package com.cooperativabeb.model;

import java.util.Date;

public class Transaccion {
    private int idTransaccion;
    private int idCuenta;
    private int idPlan;
    private String tipo;
    private double monto;
    private double saldoAnterior;
    private double saldoPosterior;
    private Date fechaTransaccion;
    private String descripcion;
    private String estado;

    public Transaccion() {}

    public Transaccion(int idTransaccion, int idCuenta, int idPlan,
                       String tipo, double monto, double saldoAnterior,
                       double saldoPosterior, Date fechaTransaccion,
                       String descripcion, String estado) {
        this.idTransaccion = idTransaccion;
        this.idCuenta = idCuenta;
        this.idPlan = idPlan;
        this.tipo = tipo;
        this.monto = monto;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.fechaTransaccion = fechaTransaccion;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(int idTransaccion) { this.idTransaccion = idTransaccion; }
    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }
    public int getIdPlan() { return idPlan; }
    public void setIdPlan(int idPlan) { this.idPlan = idPlan; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public double getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(double saldoAnterior) { this.saldoAnterior = saldoAnterior; }
    public double getSaldoPosterior() { return saldoPosterior; }
    public void setSaldoPosterior(double saldoPosterior) { this.saldoPosterior = saldoPosterior; }
    public Date getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(Date fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Transaccion{tipo=" + tipo + ", monto=" + monto + "}";
    }
}
