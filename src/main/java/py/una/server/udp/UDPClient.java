package py.una.server.udp;


import java.io.*;
import java.net.*;

import py.una.entidad.Paquete;
import py.una.entidad.PaqueteEnvioJSON;


class UDPClient {

    public static void main(String a[]) throws Exception {

        // Datos necesario
        String direccionServidor = "127.0.0.1";

        if (a.length > 0) {
            direccionServidor = a[0];
        }

        int puertoServidor = 9876;
        
        try {

            BufferedReader inFromUser =
                    new BufferedReader(new InputStreamReader(System.in));

            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName(direccionServidor);
            System.out.println("Intentando conectar a = " + IPAddress + ":" + puertoServidor +  " via UDP...");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

          
            
            System.out.print("Ingrese la opcion 1, 2 o 3: ");
            int opcion = Integer.parseInt(inFromUser.readLine());
            System.out.print("Ingrese el id_estacion (Entero): ");
            int id_estacion = Integer.parseInt(inFromUser.readLine());
            System.out.print("Ingrese la ciudad: ");
            String ciudad = inFromUser.readLine();
            System.out.print("Ingrese el porcentaje_humedad (100 para 100%): ");
            int porcentaje_humedad = Integer.parseInt(inFromUser.readLine());
            System.out.print("Ingrese la temperatura (Entero): ");
            int temperatura = Integer.parseInt(inFromUser.readLine());
            System.out.print("Ingrese la velocidad_viento (Entero): ");
            int velocidad_viento = Integer.parseInt(inFromUser.readLine());
            System.out.print("Ingrese la fecha (dd-mm-aaaa): ");
            String fecha = inFromUser.readLine();
            System.out.print("Ingrese la hora (Entero): ");
            int hora = Integer.parseInt(inFromUser.readLine());
            
            Paquete p = new Paquete( opcion, id_estacion, ciudad,porcentaje_humedad,temperatura,velocidad_viento,fecha,hora);
        
            
            String datoPaquete = PaqueteEnvioJSON.objetoString(p); 
            sendData = datoPaquete.getBytes();

            System.out.println("Enviar " + datoPaquete + " al servidor. ("+ sendData.length + " bytes)");
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, puertoServidor);

            clientSocket.send(sendPacket);

            DatagramPacket receivePacket =
                    new DatagramPacket(receiveData, receiveData.length);

            System.out.println("Esperamos si viene la respuesta.");

            //Vamos a hacer una llamada BLOQUEANTE entonces establecemos un timeout maximo de espera
            clientSocket.setSoTimeout(10000);

            try {
                // ESPERAMOS LA RESPUESTA, BLOQUENTE
                clientSocket.receive(receivePacket);

                String respuesta = new String(receivePacket.getData());
                
                
                InetAddress returnIPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                System.out.println("Respuesta desde =  " + returnIPAddress + ":" + port);
                System.out.println("Respuesta: "+respuesta);
                
                
                

            } catch (SocketTimeoutException ste) {

                System.out.println("TimeOut: El paquete udp se asume perdido.");
            }
            clientSocket.close();
        } catch (UnknownHostException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
} 

