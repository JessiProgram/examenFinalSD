package py.una.server.udp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import py.una.entidad.Paquete;
import py.una.entidad.PaqueteEnvioJSON;

public class UDPServer {

    public static void main(String[] a) {

        // Variables
        int puertoServidor = 9876;

        try {
            // 1) Creamos el socket Servidor de Datagramas (UDP)
            DatagramSocket serverSocket = new DatagramSocket(puertoServidor);
            System.out.println("Servidor Sistemas Distribuidos - UDP ");

            // 2) buffer de datos a enviar y recibir
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            int asd = 0;

            // Creamos el almacen de datos
            LinkedList<Paquete> datos = new LinkedList<>();

            // 3) Servidor siempre esperando
            while (true) {

                receiveData = new byte[1024];

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                asd++;
                System.out.println("Esperando a algun cliente... " + asd);

                // 4) Receive LLAMADA BLOQUEANTE
                serverSocket.receive(receivePacket);

                System.out.println("________________________________________________");
                System.out.println("Aceptamos un paquete");
                InetAddress direccionIP = receivePacket.getAddress(); // Obtener IP del cliente
                int puerto = receivePacket.getPort();

                // Datos recibidos e Identificamos quien nos envio
                String datoRecibido = new String(receivePacket.getData());
                datoRecibido = datoRecibido.trim();
                System.out.println("DatoRecibido: " + datoRecibido);

                Paquete p = PaqueteEnvioJSON.stringObjeto(datoRecibido);
                int opcion = p.opcion;

                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                System.out.println("De : " + IPAddress + ":" + port);

                JSONObject respuesta = new JSONObject();

                switch (opcion) {
                    case 1: // Ver estado de todos los hospitales
                        DatagramPacket paqueteEnviar;
                        try {
                            datos.add(p);

                            // formamos el json de respuesta
                            respuesta.put("estado", 0);
                            respuesta.put("mensaje", "ok");
                            respuesta.put("tipo_operacion", 1);
                            respuesta.put("cuerpo", "Se añadio los elementos con exito");

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);

                        } catch (Exception e) { // Se notifica el error al cliente

                            // formamos el json de respuesta
                            respuesta.put("estado", -1);
                            respuesta.put("mensaje", e.getMessage());
                            respuesta.put("tipo_operacion", 1);
                            respuesta.put("cuerpo", "Se pordujo un error");

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);
                        }

                        break;
                    case 2:
                        try {
                            
                            boolean listo = false;
                            // Consultar la temperatura en una ciudad especificada por el usuario en la
                            // consulta.
                            for (Paquete paquete : datos) {
                                if (paquete.ciudad.equals(p.ciudad)) {
                                    // formamos el json de respuesta
                                    respuesta.put("estado", 0);
                                    respuesta.put("mensaje", "ok");
                                    respuesta.put("tipo_operacion", 2);
                                    respuesta.put("cuerpo", paquete.temperatura);

                                    listo = true;
                                }
                            }
                            if(!listo){
                                respuesta.put("estado", -1);
                                respuesta.put("mensaje", "Error");
                                respuesta.put("tipo_operacion", 2);
                                respuesta.put("cuerpo", "No se encontro la cuidad");
                            }

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);
                        } catch (Exception e) {
                            // formamos el json de respuesta
                            respuesta.put("estado", -1);
                            respuesta.put("mensaje", e.getMessage());
                            respuesta.put("tipo_operacion", 2);
                            respuesta.put("cuerpo", "Se pordujo un error");

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);
                        }

                        break;
                    case 3:
                        try {
                            // Consultar la temperatura promedio en un dia especificado por el usuario en la
                            // consulta.
                            int sum = 0;
                            int cant = 0;
                            for (Paquete paquete : datos) {
                                if (paquete.fecha.equals(p.fecha)) {
                                    sum += paquete.temperatura;
                                    cant++;
                                }
                            }
                            if (cant == 0) {
                                respuesta.put("estado", -1);
                                respuesta.put("mensaje", "Error");
                                respuesta.put("tipo_operacion", 3);
                                respuesta.put("cuerpo", "No hay datos en esa fecha");

                                sendData = (respuesta.toJSONString()).getBytes();
                                paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                                serverSocket.send(paqueteEnviar);

                                break;
                            }
                            int prom = sum / cant;

                            respuesta.put("estado", 0);
                            respuesta.put("mensaje", "ok");
                            respuesta.put("tipo_operacion", 3);
                            respuesta.put("cuerpo", prom);

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);
                        } catch (Exception e) {
                            // formamos el json de respuesta
                            respuesta.put("estado", -1);
                            respuesta.put("mensaje", e.getMessage());
                            respuesta.put("tipo_operacion", 3);
                            respuesta.put("cuerpo", "Se pordujo un error");

                            sendData = (respuesta.toJSONString()).getBytes();
                            paqueteEnviar = new DatagramPacket(sendData, sendData.length, direccionIP, puerto);
                            serverSocket.send(paqueteEnviar);
                        }

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }

    }

}
