import java.io.*;
import java.net.*;

public class Cliente {

  public static void main(String[] args) throws IOException{

    if (args.length != 2) { //si hay más de 1 parámetro
			System.out.println("Ingresar solo 2 argumentos, la IP del servidor y el PUERTO del servicio.");
			System.exit(-1);
		} else {
		    System.out.println("El cliente se va a conectar al servidor " + args[0] + " en el puerto " + args[1]+" ...");
		}

    Socket clienteSocket = null;
    DataOutputStream mensajeSalidaDelCliente = null;
    DataInputStream mensajeEntradaAlCliente = null;
    Integer servicioPuerto = Integer.parseInt(args[1]);
    String servidorIP = args[0];
    String entradaRemota="";
    String mensajeEscritoPorElUsuario="";
    boolean iterar = true;

    try {
      System.out.println("Conectando ...");
      clienteSocket = new Socket(servidorIP, servicioPuerto);
      System.out.println("Conectando al servidor con IP: " + servidorIP + ", al puerto: " + servicioPuerto);
    } catch (UnknownHostException e) {
      System.err.println("No conosco el servidor con IP: "+servidorIP);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("No puedo conectarme al servidor con IP:  "+servidorIP);
      System.exit(1);
    }

    BufferedReader entradaEstandar = new BufferedReader(new InputStreamReader(System.in));
    mensajeSalidaDelCliente = new DataOutputStream(clienteSocket.getOutputStream());
    mensajeEntradaAlCliente = new DataInputStream(clienteSocket.getInputStream());

    entradaRemota = mensajeEntradaAlCliente.readUTF();
    System.out.println("Mensaje del servidor --> " +entradaRemota);
     System.out.println("\nServicio de Horóscopo");
    System.out.println("Para conocer tu horóscopo para los siguientes tres días, ingresa tu signo zodiacal y presiona ENTER.\n");

    while (iterar) {

      try {
      mensajeEscritoPorElUsuario = entradaEstandar.readLine();
      if (mensajeEscritoPorElUsuario != null && !mensajeEscritoPorElUsuario.equals("desconectar")) {
        mensajeSalidaDelCliente.writeUTF(mensajeEscritoPorElUsuario);
        entradaRemota=mensajeEntradaAlCliente.readUTF();
        System.out.println(entradaRemota);
        System.out.println();
        System.out.println("Si quieres consultar el horóscopo para otro signo, ingresalo y presionar ENTER");
        System.out.println("Si no quieres realizar mas consultas, escribe 'desconectar'");
      } else {
        iterar=false;
      }
      
      
      }catch (IOException e) {
						System.out.println("Conexion con el servidor con IP: " + servidorIP + ", al puerto: " + servicioPuerto + " se ha perdido ya que el servidor se ha desconectado.");
						mensajeSalidaDelCliente.close();
						mensajeEntradaAlCliente.close();
						clienteSocket.close();
						//e.printStackTrace();
					}
    }
    System.out.println("Desconectandose del servidor ...");
    mensajeSalidaDelCliente.close();
    mensajeEntradaAlCliente.close();
    clienteSocket.close();
    System.out.println("Desconectado del servidor.");
  }
}
