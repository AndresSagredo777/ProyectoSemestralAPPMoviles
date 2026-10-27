package cl.duoc.mastermartini.model;

public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;
    private String icono;

    public Categoria() {
    }

    public Categoria(
            Long id,
            String nombre,
            String descripcion,
            String icono
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.icono = icono;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getIcono() {
        return icono;
    }
}