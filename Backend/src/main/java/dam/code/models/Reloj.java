package dam.code.models;

public class Reloj {
    private int id;
    private String nombre;
    private String modelo;
    private String descripcion;
    private int stock;
    private int precio;
    private int compras;

    public Reloj(String nombre, String modelo, String descripcion, int stock, int precio) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.compras = 0;
    }

    public Reloj(int id, String nombre, String modelo, String descripcion, int stock, int precio) {
        this.id = id;
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.compras = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCompras() {
        return compras;
    }

    public void setCompras(int compras) {
        this.compras = compras;
    }
}