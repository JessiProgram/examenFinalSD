package py.una.entidad;

public class Paquete{

    public int opcion; // Opciones del programa del 1 al 3
    
    public int id_estacion;
    public String ciudad;
    public int porcentaje_humedad;
    public int temperatura;
    public int velocidad_viento;
    public String fecha;
    public int hora;
    
    public Paquete(){}
    
    // Para las opciones que requieran una cama
    
    public Paquete (int opc, 
        int id_estacion,
        String ciudad,
        int porcentaje_humedad,
        int temperatura,
        int velocidad_viento,
        String fecha,
        int hora)
    {
        this.opcion = opc;
        this.id_estacion = id_estacion;
        this.ciudad = ciudad;
        this.porcentaje_humedad = porcentaje_humedad;
        this.temperatura = temperatura;
        this.velocidad_viento = velocidad_viento;
        this.fecha = fecha;
        this.hora = hora;

    }
    
    public Paquete (int opc)
    {
        this.opcion = opc;
    }

}
