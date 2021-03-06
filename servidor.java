import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Servidor {

	static ServerSocket servidorSocket = null;

	public String leerTxt(String directorio, Integer linea, String signo){
		
		String texto= "";
		try {
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			FileReader f = new FileReader(directorio);
			BufferedReader b = new BufferedReader(f);
			String temp = "";
			String bf = "";
			for(int i=0; i<linea-1; i++)
				b.readLine();
			texto = "Horóscopo para "+signo+" "+dateFormat.format(date)+"\n\n"+"Hoy : "+b.readLine()+"\n\n";
			texto = texto+"Mañana: "+b.readLine()+"\n\n";
			texto = texto+"Pasado mañana: "+b.readLine();
		} catch (Exception e) {
			System.err.println("No se logró leer el archivo");
		}

		return texto;

	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1) { //si hay más de 1 parámetro
			System.out.println("Ingresar solo 1 argumento, el número del puerto donde el servicio está escuchando.");
			System.exit(-1);
		} else {
		    System.out.println("El servicio se va a configurar en el puerto " + args[0]+" ...");
		}
		Socket clienteSocket=null;
		DataOutputStream mensajeSalidaDelServidor = null;
		DataInputStream mensajeEntradaAlServidor = null;
		Integer servicioPuerto = Integer.parseInt(args[0]);
		String entradaRemota="";
		boolean escuchando = true;

		try {
			System.out.println("Creando el servicio en el puerto "+ servicioPuerto+" ...");
			servidorSocket = new ServerSocket(servicioPuerto);
			System.out.println("Servicio en el puerto "+ servicioPuerto+" creado exitosamente ...");
		} catch (IOException e) {
			System.err.println("No se puede utilizar el puerto: "+servicioPuerto);
			System.exit(-1);
		}

		System.out.println("Servidor listo para aceptar requerimiento de clientes ....");


		Runtime.getRuntime().addShutdownHook(new Thread() {
		        public void run() {
							System.out.println("Cerrando servicio ....");
							try{
								servidorSocket.close();
								System.out.println("Servicio cerrado.");
							} catch (IOException e) {
								e.printStackTrace();
							}
		        }
		    });


				while (escuchando) {
					System.out.println("Escuchando ....");
					clienteSocket=servidorSocket.accept();
					System.out.println("se conecto un cliente desde: "+ clienteSocket.getInetAddress());
					try {
						mensajeSalidaDelServidor = new DataOutputStream(clienteSocket.getOutputStream());
						mensajeEntradaAlServidor = new DataInputStream(clienteSocket.getInputStream());

						mensajeSalidaDelServidor.writeUTF("El servidor dice que esta OK para recibir mensajes...");
						System.out.println("Esperando mensajes de desde el cliente ...");
						
						Servidor s = new Servidor();
						//System.out.println(s.leerTxt("/mnt/f/Carrasco-Vejar-Grupo5/horoscopo.txt"));
						String Direcciontxt="/mnt/f/Carrasco-Vejar-Grupo5/horoscopo.txt";
						//Direccion del txt kevin "/mnt/c/Users/kvn_9/Documents/Carrasco-Vejar-Grupo5/horoscopo.txt"
						//Direccion del txt alejandro "/mnt/f/Carrasco-Vejar-Grupo5/horoscopo.txt"
						while ((entradaRemota = mensajeEntradaAlServidor.readUTF()) != null) {
							System.out.println("Llego desde el cliente el mensaje --> " +entradaRemota);
							entradaRemota= entradaRemota.substring(0, 1).toUpperCase()+entradaRemota.substring(1,entradaRemota.length()).toLowerCase();
							//System.out.println("asi quedo --> " +entradaRemota);
							
							switch (entradaRemota) {
								case "Aries":
									entradaRemota=s.leerTxt(Direcciontxt, 2, entradaRemota);
									break;

								case "Tauro":
									entradaRemota=s.leerTxt(Direcciontxt, 6, entradaRemota);
									break;	

								case "Geminis":
									entradaRemota=s.leerTxt(Direcciontxt, 10, entradaRemota);
									break;	
								
								case "Cancer":
									entradaRemota=s.leerTxt(Direcciontxt, 14, entradaRemota);
									break;

								case "Leo":
									entradaRemota=s.leerTxt(Direcciontxt, 18, entradaRemota);
									break;	

								case "Virgo":
									entradaRemota=s.leerTxt(Direcciontxt, 22, entradaRemota);
									break;	

								case "Libra":
									entradaRemota=s.leerTxt(Direcciontxt, 26, entradaRemota);
									break;

								case "Escorpio":
									entradaRemota=s.leerTxt(Direcciontxt, 30, entradaRemota);
									break;	

								case "Sagitario":
									entradaRemota=s.leerTxt(Direcciontxt, 34, entradaRemota);
									break;	
								
								case "Capricornio":
									entradaRemota=s.leerTxt(Direcciontxt, 38, entradaRemota);
									break;

								case "Acuario":
									entradaRemota=s.leerTxt(Direcciontxt, 42, entradaRemota);
									break;	

								case "Piscis":
									entradaRemota=s.leerTxt(Direcciontxt, 46, entradaRemota);
									break;
									
								default:
									System.out.println("Signo inválido: "+entradaRemota);
									entradaRemota = "Signo inválido: "+entradaRemota;
									break;
							}

							mensajeSalidaDelServidor.writeUTF(entradaRemota);
							


						}
					} catch (IOException e) {
						System.out.println("Cliente " +clienteSocket.getInetAddress()+ " se desconectó.");
						mensajeSalidaDelServidor.close();
						mensajeEntradaAlServidor.close();
						clienteSocket.close();
						//e.printStackTrace();
					}
				}
	}
}
