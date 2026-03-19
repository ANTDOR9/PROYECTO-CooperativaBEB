package com.cooperativabeb.model;

public class ProductoFinanciero {
    private int idProducto;
    private String nombre;
    private String tipo;
    private String descripcion;
    private double tasaBase;
    private double montoMinimo;
    private double montoMaximo;
    private int plazoMeses;
    private String estado;

    public ProductoFinanciero() {}

    public ProductoFinanciero(int idProducto, String nombre, String tipo,
                              String descripcion, double tasaBase,
                              double montoMinimo, double montoMaximo,
                              int plazoMeses, String estado) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.tasaBase = tasaBase;
        this.montoMinimo = montoMinimo;
        this.montoMaximo = montoMaximo;
        this.plazoMeses = plazoMeses;
        this.estado = estado;
    }

    public double calcularGanancia(double monto) {
        return monto * (tasaBase / 100) * (plazoMeses / 12.0);
    }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getTasaBase() { return tasaBase; }
    public void setTasaBase(double tasaBase) { this.tasaBase = tasaBase; }
    public double getMontoMinimo() { return montoMinimo; }
    public void setMontoMinimo(double montoMinimo) { this.montoMinimo = montoMinimo; }
    public double getMontoMaximo() { return montoMaximo; }
    public void setMontoMaximo(double montoMaximo) { this.montoMaximo = montoMaximo; }
    public int getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(int plazoMeses) { this.plazoMeses = plazoMeses; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() { return nombre + " (" + tasaBase + "% — " + plazoMeses + " meses)"; }
}
