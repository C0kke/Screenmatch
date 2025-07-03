package com.aluracursos.screenmatch.model;

import java.util.List;
import java.util.OptionalDouble;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;

    private Integer totalDeTemporadas;
    private Double evaluacion;
    private String poster;

    @Enumerated(EnumType.STRING)
    private Categoria genero; 
    private String actores;
    private String sinopsis;
    
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie(){}

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Serie(DatosSerie datos) {
        this.titulo = datos.titulo();
        this.totalDeTemporadas = datos.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.valueOf(datos.evaluacion())).orElse(0);
        this.poster = datos.poster();
        this.genero = Categoria.fromString(datos.genero().split(",")[0].trim());
        this.actores = datos.actores();
        this.sinopsis = datos.sinopsis();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }
    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }
    public Double getEvaluacion() {
        return evaluacion;
    }
    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }
    public Categoria getGenero() {
        return genero;
    }
    public void setGenero(Categoria genero) {
        this.genero = genero;
    }
    public String getActores() {
        return actores;
    }
    public void setActores(String actores) {
        this.actores = actores;
    }
    public String getSinopsis() {
        return sinopsis;
    }
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return "\n" + titulo + 
                "\nTotalDeTemporadas: " + totalDeTemporadas + 
                "\nEvaluacion: " + evaluacion + 
                "\nLink al Poster: " + poster + 
                "\nGenero: " + genero + 
                "\nReparto: " + actores + 
                "\nSinopsis: " + sinopsis + 
                "\nEpisodios: " + episodios;
    }    
}
