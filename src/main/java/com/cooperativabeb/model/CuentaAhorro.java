package com.cooperativabeb.model;

import java.util.Date;

public class CuentaAhorro {
    private int idCuenta;
    private String nroCuenta;
    private int idCliente;
    private int idAsesor;
    private double saldo;
    private double tasaInteres;
    private String tipoCuenta;
    private String estado;
    private Date fechaApertura;
    private Date fechaCierre;

    public CuentaAhorro() {}

    public CuentaAhorro(int idCuenta, String nroCuenta, int idCliente,
                        int idAsesor, double saldo, double tasaInteres,
                        String tipoCuenta, String estado,
                        Date fechaApertura, Date fechaCierre) {
        this.idCuenta = idCuenta;
        this.nroCuenta = nroCuenta;
        this.idCliente = idCliente;
        this.idAsesor = idAsesor;
        this.saldo = saldo;
        this.tasaInteres = tasaInteres;
        this.tipoCuenta = tipoCuenta;
        this.estado = estado;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
    }

    public int getIdCuenta() { return idCuenta; }
    public void setIdCuenta(int idCuenta) { this.idCuenta = idCuenta; }
    public String getNroCuenta() { return nroCuenta; }
    public void setNroCuenta(String nroCuenta) { this.nroCuenta = nroCuenta; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdAsesor() { return idAsesor; }
    public void setIdAsesor(int idAsesor) { this.idAsesor = idAsesor; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(double tasaInteres) { this.tasaInteres = tasaInteres; }
    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(Date fechaApertura) { this.fechaApertura = fechaApertura; }
    public Date getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Date fechaCierre) { this.fechaCierre = fechaCierre; }

    @Override
    public String toString() {
        return "CuentaAhorro{nro=" + nroCuenta + ", saldo=" + saldo + "}";
    }
}
