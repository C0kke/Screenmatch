package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
        private Scanner teclado = new Scanner(System.in);
        private ConsumoAPI consumoApi = new ConsumoAPI();
        private final String URL_BASE = "https://www.omdbapi.com/?t=";
        private final String API_KEY = "&apikey=4acc98d3";
        private ConvierteDatos conversor = new ConvierteDatos();
        private SerieRepository repositorio;

        private Optional<Serie> serieBuscada;

        public Principal(SerieRepository repository) {
                this.repositorio = repository;
        }

        public void muestraElMenu() {
                var opcion = -1;
                while (opcion != 0) {
                        var menu = """
                                1 - Buscar Series
                                2 - Buscar Series Por Título
                                3 - Top 5 Series
                                4 - Buscar Series Por Genero
                                5 - Buscar Series Por Temporadas Máximas y Evaluación Mínima
                                0 - Salir
                                """;
                        System.out.println("\n" + menu);
                        opcion = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcion) {
                                case 1:
                                        buscarSerieWeb();
                                        break;
                                case 2:
                                        buscarSeriesPorTitulo();
                                        break;
                                case 3:
                                        buscarTop5Series();
                                        break;
                                case 4:
                                        buscarSeriesPorCategoria();
                                        break;
                                case 5:
                                        buscarSeriesPorTemporadaMaximaYEvaluacionMinima();
                                        break;
                                // case 8:
                                //         buscarEpisodiosPorTitulo();
                                //         break;
                                // case 9:
                                //         buscarTop5Episodios();
                                //         break;
                                case 0:
                                        System.out.println("Cerrando la aplicación...");
                                        break;
                        default:
                                System.out.println("Opción Inválida");
                                break;
                        }
                }
        }

        private DatosSerie getDatosSerie() {
                System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
                var nombreSerie = teclado.nextLine();
                var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
                System.out.println(json);
                DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
                return datos;
        }

        private void buscarSerieWeb() {
                DatosSerie datos = getDatosSerie();
                Serie serie = new Serie(datos);

                if (serie != null) {
                        System.out.println(serie.getTitulo());

                        List<DatosTemporadas> temporadas = new ArrayList<>();
        
                        for (int i = 1; i <= serie.getTotalDeTemporadas(); i++) {
                                var json = consumoApi.obtenerDatos(URL_BASE + serie.getTitulo().replace(" ", "+") + "&Season=" + i + API_KEY);
                                DatosTemporadas datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
                                temporadas.add(datosTemporadas);
                        }
                        temporadas.forEach(System.out::println);
                        
                        List<Episodio> episodios = temporadas.stream()
                                                .flatMap(d -> d.episodios().stream()
                                                        .map(e -> new Episodio(d.numero(), e)))
                                                .collect(Collectors.toList());
                        
                        serie.setEpisodios(episodios);
                        repositorio.save(serie);
                        System.out.println("La serie " + datos.titulo() + " ha sido agregada a la lista\n");
                }
                
        }

        private void buscarSeriesPorTitulo() {
                System.out.println("Escribe el nombre de la serie: ");
                var idSerie = teclado.nextInt();
                serieBuscada = repositorio.findById(idSerie);

                if (serieBuscada.isPresent()) {
                        System.out.println("La serie buscada es: " + serieBuscada.get());
                } else {
                        System.out.println("La serie no se encuentra en la lista");
                }
        }

        private void buscarTop5Series() {
                List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
                topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + ", Evaluacion: " + s.getEvaluacion()));
        }

        private void buscarSeriesPorCategoria() {
                System.out.println("Escriba el Genero/Categoria de la serie");
                var genero = teclado.nextLine();
                var categoria = Categoria.fromEspanol(genero);
                List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
                if (seriesPorCategoria.isEmpty()) {
                        System.out.println("No hay series de esta categoria");
                } else {
                        System.out.println("Las series de la categoria " + genero);
                        seriesPorCategoria.forEach(System.out::println);
                }
        }

        private void buscarSeriesPorTemporadaMaximaYEvaluacionMinima() {
                System.out.println("Ingrese cantidad máxima de temporadas: ");
                var cantidadMaxima = teclado.nextInt();
                System.out.println("Ingrese evaluación mínima: ");
                var evaluacionMinima = teclado.nextDouble();
                List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(cantidadMaxima, evaluacionMinima);
                
                if (filtroSeries.isEmpty()) {
                        System.out.println("No hay series con menos de " + cantidadMaxima + " temporadas y con una calificación mayor a " + evaluacionMinima);
                } else {
                        System.out.println("Las series con más de " + cantidadMaxima + " temporadas son y con evaluacion mayor a " + evaluacionMinima + " son:");
                        filtroSeries.forEach(s -> System.out.println("\nSerie: " + s.getTitulo() + "\nCantidad de Temporadas: " + s.getTotalDeTemporadas() +"\nEvaluación: " + s.getEvaluacion()));
                }
        }

        // private void buscarEpisodiosPorTitulo() {
        //         System.out.println("Escribe el nombre del episodio a buscar: ");
        //         var nombreEpisodio = teclado.nextLine();
        //         List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        //         episodiosEncontrados.forEach(e -> 
        //                 System.out.printf("Serie: %s: Temporada %s, Episodio %s '%s' Evaluacion: %s\n",
        //                         e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(),e.getEvaluacion()));
        // }

        // private void buscarTop5Episodios() {
        //         buscarSeriesPorTitulo();
        //         if (serieBuscada.isPresent()) {
        //                 Serie serie = serieBuscada.get();
        //                 List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
        //                 topEpisodios.forEach(e ->
        //                         System.out.printf("Serie: %s - Temporada %s - Episodio %s '%s' - Evaluacion %s\n", 
        //                                 e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(), e.getEvaluacion()));
        //         }
        // }
}
