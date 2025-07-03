package com.aluracursos.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    Optional<Serie> findById(int idSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria c);

    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas >= :maxTemporadas AND s.evaluacion >= :minEvaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(int maxTemporadas, Double minEvaluacion);

    @Query("Select s FROM Serie s " + "Join s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> lanzamientosMasRecientes();

    @Query("Select e FROM Serie s Join s.episodios e WHERE  s.id = :id AND e.temporada = :numeroTemporada order by e.temporada")
    List<Episodio> obtenerTemporadasPorNumero(Long id, Long numeroTemporada);

}
