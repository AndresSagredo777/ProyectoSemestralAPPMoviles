package cl.duoc.mastermartini.model;

public class Video {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String urlVideo;

    public Video() {
    }

    public Video(
            Long id,
            String titulo,
            String descripcion,
            String categoria,
            String urlVideo
    ) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.urlVideo = urlVideo;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getUrlVideo() {
        return urlVideo;
    }
}