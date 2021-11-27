package py.una.entidad;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class PaqueteEnvioJSON {

    // Convierte una cadena a formato JSON
    public static String objetoString(Paquete p) {	
		JSONObject obj = new JSONObject();
		
		
        obj.put("id_estacion", p.id_estacion);
        obj.put("ciudad", p.ciudad);
        obj.put("porcentaje_humedad", p.porcentaje_humedad);
        obj.put("temperatura", p.temperatura);
        obj.put("velocidad_viento", p.velocidad_viento);
        obj.put("fecha", p.fecha);
        obj.put("hora", p.hora);
        obj.put("opcion", p.opcion);
        

        return obj.toJSONString();
    }
    
    // Convierte una cadena a un objeto
    public static Paquete stringObjeto(String str) throws Exception {
    	Paquete p;
        p = new Paquete();
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(str.trim());
        JSONObject jsonObject = (JSONObject) obj;
		
       
        p.id_estacion=((Long) (jsonObject.get("id_estacion"))).intValue();
        p.ciudad=(String) (jsonObject.get("ciudad"));
        p.porcentaje_humedad=((Long) (jsonObject.get("porcentaje_humedad"))).intValue();
        p.temperatura=((Long) (jsonObject.get("temperatura"))).intValue();
        p.velocidad_viento=((Long) (jsonObject.get("velocidad_viento"))).intValue();
        p.fecha=(String) (jsonObject.get("fecha"));
        p.hora=((Long) (jsonObject.get("hora"))).intValue();
        p.opcion=((Long) (jsonObject.get("opcion"))).intValue();
        
        return p;
	}

}
