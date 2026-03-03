package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Integer fechaDeNacimiento;

    private Integer fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento() != null ?
                Integer.valueOf(datosAutor.fechaDeNacimiento()) : null;
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento() != null ?
                Integer.valueOf(datosAutor.fechaDeFallecimiento()) : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getFechaDeNacimiento() { return fechaDeNacimiento; }
    public void setFechaDeNacimiento(Integer fechaDeNacimiento) { this.fechaDeNacimiento = fechaDeNacimiento; }

    public Integer getFechaDeFallecimiento() { return fechaDeFallecimiento; }
    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) { this.fechaDeFallecimiento = fechaDeFallecimiento; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this)); // Mantenemos la relación bidireccional
        this.libros = libros;
    }

    @Override
    public String toString() {
        String librosNombres = libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", "));

        return  "Autor: " + nombre + "\n" +
                "Fecha de nacimiento: " + (fechaDeNacimiento != null ? fechaDeNacimiento : "N/A") + "\n" +
                "Fecha de fallecimiento: " + (fechaDeFallecimiento != null ? fechaDeFallecimiento : "N/A") + "\n" +
                "Libros: [" + librosNombres + "]\n";
    }
}