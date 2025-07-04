package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.service.ConsolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consola")
public class ObtenerDatosController {

    @Autowired
    private ConsolaService consolaService;

    @GetMapping("/search")
    public List<Map<String, String>> buscarSeriesPorNombre(@RequestParam String nombre) {
        return consolaService.buscarSeriesPorNombre(nombre);
    }

    @PostMapping("/import")
    public String importarSeriePorTitulo(@RequestParam String nombre) {
        return consolaService.importarSeriePorNombre(nombre);
    }

    @PostMapping("/importById")
    public String importarSeriePorImdbID(@RequestParam String imdbID) {
        return consolaService.importarSeriePorImdbID(imdbID);
    }
}
