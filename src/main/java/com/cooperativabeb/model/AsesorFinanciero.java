package com.cooperativabeb.model;

public class AsesorFinanciero {
    private int idAsesor;
    private String dni;
    private String nombres;
    private String apellidos;
    private String especialidad;
    private String telefono;
    private String email;
    private String estado;

    public AsesorFinanciero() {}

    public AsesorFinanciero(int idAsesor, String dni, String nombres,
                            String apellidos, String especialidad,
                            String telefono, String email, String estado) {
        this.idAsesor = idAsesor;
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
    }

    public String getNombreCompleto() { return nombres + " " + apellidos; }

    public int getIdAsesor() { return idAsesor; }
    public void setIdAsesor(int idAsesor) { this.idAsesor = idAsesor; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Asesor{" + getNombreCompleto() + ", " + especialidad + "}";
    }
}
