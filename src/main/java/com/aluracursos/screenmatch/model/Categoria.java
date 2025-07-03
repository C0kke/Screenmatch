package com.aluracursos.screenmatch.model;

public enum Categoria {
    ANIMACION("Animation", "Animacion"),
    COMEDIA("Comedy", "Comedia"),
    ROMANCE("Romance", "Romance"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen"),
    ACCION("Action", "Accion"),
    AVENTURA("Adventure", "Aventura");

    private String categoriaOmdb;
    private String categoriaEspanol;

    Categoria(String categoriaOmdb, String categoriaEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria c : Categoria.values()) {
            if (c.categoriaOmdb.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Ninguna Categoria Ha Sido Encontrada" + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria c : Categoria.values()) {
            if (c.categoriaEspanol.equalsIgnoreCase(text)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Ninguna Categoria Ha Sido Encontrada" + text);
    }
}
