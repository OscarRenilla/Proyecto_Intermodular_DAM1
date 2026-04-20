package dam.code.models.utils;

public enum Rol {
    ADMIN("Administrador"),
    USER("Usuario");

    private final String nombre;

    Rol(final String nombre) {
        this.nombre = nombre;
    }


    @Override
    public String toString() {
        return nombre;
    }
}
