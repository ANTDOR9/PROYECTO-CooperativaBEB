package com.cooperativabeb.model;

import java.util.Date;

public class Cliente {
    private int idCliente;
    private String dni;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String email;
    private Date fechaNacimiento;
    private String direccion;
    private String estado;
    private Date fechaRegistro;

    public Cliente() {}

    public Cliente(int idCliente, String dni, String nombres, String apellidos,
                   String telefono, String email, Date fechaNacimiento,
                   String direccion, String estado, Date fechaRegistro) {
        this.idCliente = idCliente;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return "Cliente{dni=" + dni + ", nombre=" + getNombreCompleto() + "}";
    }
}
