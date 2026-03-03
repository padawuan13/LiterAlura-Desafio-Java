package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsultaTraductor;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ---------------------------------------------------
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros más descargados
                    0 - Salir
                    ---------------------------------------------------
                    Elija una opción:
                    """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Opción inválida. Por favor, ingrese un número.");
                teclado.nextLine();
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroWeb();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> mostrarTop10();
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroWeb() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();

        System.out.println("Traduciendo búsqueda...");
        String nombreEnIngles = ConsultaTraductor.obtenerTraduccion(nombreLibro, "es%7Cen");
        System.out.println("Buscando en inglés como: " + nombreEnIngles);

        String nombreFormateado = URLEncoder.encode(nombreEnIngles, StandardCharsets.UTF_8);
        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreFormateado);
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        if (datosBusqueda.resultados() != null && !datosBusqueda.resultados().isEmpty()) {
            DatosLibro datosLibro = datosBusqueda.resultados().get(0);

            if (libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo()).isPresent()) {
                System.out.println("Nota: Este libro ya se encuentra registrado.");
                return;
            }

            if (datosLibro.autor().isEmpty()) {
                System.out.println("No se puede registrar un libro sin autor.");
                return;
            }

            DatosAutor datosAutor = datosLibro.autor().get(0);
            Autor autor = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> autorRepository.save(new Autor(datosAutor)));

            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            libroRepository.save(libro);

            System.out.println("\n--- LIBRO GUARDADO CON ÉXITO ---");
            System.out.println(libro);
        } else {
            System.out.println("No se encontraron resultados para: " + nombreLibro);
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("\n--- LIBROS REGISTRADOS ---");
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("\n--- AUTORES REGISTRADOS ---");
            autores.forEach(System.out::println);
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese el año para consultar autores vivos:");
        try {
            var anio = teclado.nextInt();
            teclado.nextLine();

            List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores registrados que vivieran en el año " + anio);
            } else {
                System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
                autoresVivos.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Año inválido. Ingrese un número.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Elija el idioma de búsqueda:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        var idioma = teclado.nextLine().toLowerCase();

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros registrados en ese idioma (" + idioma + ")");
        } else {
            System.out.println("\n--- LIBROS EN " + idioma.toUpperCase() + " ---");
            librosPorIdioma.forEach(System.out::println);
        }
    }

    private void mostrarTop10() {
        System.out.println("Consultando y traduciendo los libros más populares...");
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        System.out.println("\n--- TOP 10 LIBROS MÁS DESCARGADOS (Traducidos al Español) ---");

        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> {
                    String tituloTraducido = ConsultaTraductor.obtenerTraduccion(l.titulo(), "en%7Ces");
                    return "Título: " + tituloTraducido.toUpperCase() + " | Descargas: " + l.numeroDeDescargas();
                })
                .forEach(System.out::println);
    }
}