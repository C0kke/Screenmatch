package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsolaService {

    @Autowired
    private SerieRepository repositorio;
    
    @Autowired
    private ConsumoAPI consumoApi;
    @Autowired
    private ConvierteDatos conversor;

    private final String URL_BASE_BUSQUEDA = "https://www.omdbapi.com/?s=";
    private final String API_KEY = "&apikey=4acc98d3";

    public List<Map<String, String>> buscarSeriesPorNombre(String nombreSerie) {
        String url = URL_BASE_BUSQUEDA + nombreSerie.replace(" ", "+") + API_KEY;
        String json = consumoApi.obtenerDatos(url);

        Map<?,?> response = conversor.obtenerDatos(json, Map.class);

        List<Map<String, String>> resultados = new ArrayList<>();
        if ("True".equals(response.get("Response"))) {
            List<Map<String, String>> busqueda = (List<Map<String, String>>) response.get("Search");
            for (Map<String, String> item : busqueda) {
                Map<String, String> resumen = new HashMap<>();
                resumen.put("titulo", item.get("Title"));
                resumen.put("año", item.get("Year"));
                resumen.put("imdbID", item.get("imdbID"));
                resumen.put("poster", item.get("Poster"));
                resultados.add(resumen);
            }
        }
        return resultados;
    }

    public String importarSeriePorNombre(String nombreSerie) {
        String urlBusqueda = URL_BASE_BUSQUEDA + nombreSerie.replace(" ", "+") + API_KEY;
        String jsonBusqueda = consumoApi.obtenerDatos(urlBusqueda);

        Map<?, ?> response = conversor.obtenerDatos(jsonBusqueda, Map.class);

        if (!"True".equals(response.get("Response"))) {
            return "No se encontró ninguna serie con ese nombre.";
        }

        List<Map<String, String>> busqueda = (List<Map<String, String>>) response.get("Search");
        if (busqueda == null || busqueda.isEmpty()) {
            return "No se encontró ninguna serie con ese nombre.";
        }

        String imdbID = busqueda.get(0).get("imdbID");

        return importarSeriePorImdbID(imdbID);
    }

    public String importarSeriePorImdbID(String imdbID) {
        String url = "https://www.omdbapi.com/?i=" + imdbID + API_KEY;
        String json = consumoApi.obtenerDatos(url);

        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);

        if (datos == null || datos.titulo() == null) {
            return "No se encontró la serie con ese imdbID.";
        }

        Serie serie = new Serie(datos);

        List<DatosTemporadas> temporadas = new ArrayList<>();
        for (int i = 1; i <= serie.getTotalDeTemporadas(); i++) {
            String temporadaUrl = "https://www.omdbapi.com/?i=" + imdbID + "&Season=" + i + API_KEY;
            String jsonTemporada = consumoApi.obtenerDatos(temporadaUrl);
            DatosTemporadas datosTemporada = conversor.obtenerDatos(jsonTemporada, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        
        List<Episodio> episodios = temporadas.stream()
                .flatMap(d -> d.episodios().stream()
                    .map(e -> new Episodio(d.numero(), e)))
                .collect(Collectors.toList());
        serie.setEpisodios(episodios);

        repositorio.save(serie);
        return "La serie \"" + datos.titulo() + "\" y todas sus temporadas han sido agregadas a la base de datos.";
    }
}