package edu.ucam.app;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import edu.ucam.clases.*;

/**
 * The Class Cliente.
 */
public class Cliente {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Iniciando cliente");
			Socket socket = new Socket("127.0.0.1", 5000);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			Scanner scan = new Scanner(System.in);
			String msg = "";
			String msg2 = "";
			String partes[];
			String partes2[];
			String partesPuerto[];

			boolean userPass = false;
			boolean userOk = true;

			while (true) {
				System.out
						.println("Introduzca las credenciales que le acrediten con el formato siguiente: USER | NAME:");
				System.out.print(">> ");
				msg = scan.nextLine(); // Almacenamos en la variable msg lo introducido por teclado
				partes = msg.split(" "); // Separacion con espacios

				// Nos autenticamos como admin, si no se realiza correctamente no podremos
				// acceder al sistema
				// Introducimos lo siguiente: "USER" | "admin"
				// A continuacion introducimos lo siguiente: "PASS" | "admin"
				if (partes[0].equals("USER")) {
					if (partes[1].equals("admin")) {
						userPass = true; // Si introducimos correctamente el nombre pasaremos a la siguiente
											// funcion
						System.out.println("OK " + partes[0] + " 01A Send password");
						while (userPass) {
							try {

								System.out.print(">> ");
								msg = scan.nextLine(); // Guardamos lo introducido por teclado en la variable

								partes = msg.split(" "); // Separacion con espacios

								if (partes[0].equals("PASS")) {
									if (partes[1].equals("admin")) {
										System.out.println("OK " + partes[0] + " 02A Welcome admin"); // Muestra
																										// un
																										// mensaje
																										// de
																										// que
																										// nos
																										// hemos
																										// autenticado
										userOk = true; // Podemos acceder al sistema
										userPass = false; // Desactivamos el userPass

									} else {
										System.out.println("FAILED " + partes[1] + " 02B Password error");
									}

								} else {
									System.out.println("FAILED " + partes[0] + " Command error. ");
								}

							} catch (Exception e) {
								System.out.println("Ingrese: PASS | password");
							}
						}
					} else {
						System.out.println("FAILED " + partes[0] + " 01B Not user");
					}
					while (userOk) { // Ya autenticados como admin comenzamos con los comandos
						try {
							System.out.print(">> ");
							msg2 = scan.nextLine(); // Guardamos lo introducido por teclado en la variable msg2
							partes2 = msg2.split(" "); // Separacion con espacios
							String idClub, idJugador, idPartido, nombre, apellidos, nombreClubLocal,
									nombreClubVisitante, resultado;

							int goles, puntos = 0;

							if (partes2[0].equals("EXIT")) {
								pw.println("EXIT");
								pw.flush();
								userOk = false;
								System.out.println("Bye");

								/*
								 * msg = br.readLine(); partes = msg.split(" "); // Separamos la linea que ya se
								 * ha leido
								 * 
								 * if (partes[0].equals("OK")) {
								 * System.out.println("El numero de clientes conectados al servidor es: " +
								 * partes[1]); } else { System.out.
								 * println("Error al obtener el numero de clientes conectados al servidor"); }
								 */

							} else if (partes2[0].equals("SESIONES")) {
								pw.println("SESIONES");
								pw.flush();
								// Recibimos la respuesta del servidor
								msg = br.readLine();
								partes = msg.split(" "); // Separamos la linea que ya se ha leido
								System.out.println("El numero de clientes conectados al servidor es: " + partes[2]);

							} else if (partes2[0].equals("ADDCLUB") && partes2.length == 1) {
								pw.println("ADDCLUB " + partes2[0]); // Enviamos al hilo el comando ADDCLUB
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" "); // Recibimos ip y puerto
								System.out.println(aux);

								Socket socket1 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4])); // Nos
																													// //
																													// puerto

								System.out.println("Introduzca el id del club");
								System.out.print(">> ");
								idClub = scan.nextLine();

								System.out.println("Introduzca el nombre del club");
								System.out.print(">> ");
								nombre = scan.nextLine();

								// Creamos el objeto club
								Club club = new Club(idClub, nombre, puntos);
								// Mandamos el objeto por el socket nuevo
								ObjectOutputStream obj = new ObjectOutputStream(socket1.getOutputStream());
								obj.writeObject(club);
								obj.flush();

								// Leemos el socket nuevo
								BufferedReader brAddV = new BufferedReader(
										new InputStreamReader(socket1.getInputStream()));
								System.out.println(brAddV.readLine());
								partes2 = null;
								socket1.close(); // Finalmente cerramos el socket

							} else if (partes2[0].equals("UPDATECLUB") && partes2.length == 2) {

								pw.println("UPDATECLUB " + partes2[0]);
								pw.flush();

								BufferedReader brGetP = new BufferedReader(
										new InputStreamReader(socket.getInputStream()));

								String aux = brGetP.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket2 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								BufferedReader br1 = new BufferedReader(
										new InputStreamReader(socket2.getInputStream()));
								PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()));

								idClub = partes2[1];
								pw1.println(idClub);
								pw1.flush();

								// Leemos los datos que nos han enviado del club encontrado o no
								String aux1 = br1.readLine();
								System.out.println(aux1);

								// Modificamos el club
								if (!aux1.equals("No existe el club por lo que no se puede actualizar.")) {
									System.out.println("Introduzca la id: ");
									System.out.print(">> ");
									idClub = scan.nextLine();

									System.out.println("Introduzca el nombre: ");
									System.out.print(">> ");
									nombre = scan.nextLine();

									Club club = new Club(idClub, nombre, puntos);

									ObjectOutputStream oosi = new ObjectOutputStream(socket2.getOutputStream());
									oosi.writeObject(club);
									oosi.flush();

									System.out.println(br1.readLine());
									socket2.close();
								}

							} else if (partes2[0].equals("GETCLUB") && partes2.length == 2) {
								pw.println("GETCLUB " + partes2[0]);
								pw.flush();

								// Leemos el socket
								BufferedReader br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br2.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket3 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								// Leemos el socket nuevo
								BufferedReader br3 = new BufferedReader(
										new InputStreamReader(socket3.getInputStream()));
								// Lo imprimimos
								PrintWriter pw3 = new PrintWriter(new OutputStreamWriter(socket3.getOutputStream()));

								idClub = partes2[1];
								pw3.println(idClub);
								pw3.flush();

								System.out.println(br3.readLine());
								socket3.close(); // Finalmente cerramos el socket

							} else if (partes2[0].equals("REMOVECLUB") && partes2.length == 2) {

								pw.println("REMOVECLUB " + partes2[1]);
								pw.flush();

								System.out.println(br.readLine());

							} else if (partes2[0].equals("LISTCLUBES") && partes2.length == 1) {

								pw.println("LISTCLUBES " + partes2[0]);
								pw.flush();

								BufferedReader br4 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br4.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket4 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));
								ObjectInputStream ois1 = new ObjectInputStream(socket4.getInputStream());
								ArrayList<Club> clubes = new ArrayList<Club>();
								clubes = (ArrayList<Club>) ois1.readObject();

								System.out.println("LISTA DE CLUBES: ");
								for (int i = 0; i < clubes.size(); i++) {
									System.out.println("ID Club: " + clubes.get(i).getId() + ", Nombre: "
											+ clubes.get(i).getNombre());
								}

							} else if (partes2[0].equals("COUNTCLUBES") && partes2.length == 1) {
								pw.println("COUNTCLUBES ");
								pw.flush();

								System.out.println("OK. Total de clubes en el sistema: " + br.readLine());

							} else if (partes2[0].equals("ADDJUGADOR") && partes2.length == 1) {

								pw.println("ADDJUGADOR " + partes2[0]);
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" ");
								System.out.println(aux);

								Socket socket5 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4]));

								System.out.println("Introduzca el id del jugador: ");
								System.out.print(">> ");
								idJugador = scan.nextLine();

								System.out.println("Introduzca el nombre del jugador: ");
								System.out.print(">> ");
								nombre = scan.nextLine();

								System.out.println("Introduzca los apellidos del jugador: ");
								System.out.print(">> ");
								apellidos = scan.nextLine();

								System.out.println("Introduzca los goles del jugador: ");
								System.out.print(">> ");
								goles = scan.nextInt();

								Jugador jugador = new Jugador(idJugador, nombre, apellidos, goles, null);
								ObjectOutputStream oos1 = new ObjectOutputStream(socket5.getOutputStream());
								oos1.writeObject(jugador);
								oos1.flush();

								BufferedReader br5 = new BufferedReader(
										new InputStreamReader(socket5.getInputStream()));
								System.out.println(br5.readLine());
								socket5.close();

							} else if (partes2[0].equals("GETJUGADOR") && partes2.length == 2) {

								pw.println("GETJUGADOR " + partes2[0]);
								pw.flush();

								BufferedReader br6 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br6.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket6 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								BufferedReader br7 = new BufferedReader(
										new InputStreamReader(socket6.getInputStream()));
								PrintWriter pw4 = new PrintWriter(new OutputStreamWriter(socket6.getOutputStream()));

								idJugador = partes2[1];
								pw4.println(idJugador);
								pw4.flush();

								System.out.println(br7.readLine());
								socket6.close();

							} else if (partes2[0].equals("REMOVEJUGADOR") && partes2.length == 2) {

								pw.println("REMOVEJUGADOR " + partes2[1]);
								pw.flush();

								System.out.println(br.readLine());
							}

							else if (partes2[0].equals("LISTJUGADORES") && partes2.length == 1) {

								pw.println("LISTJUGADORES " + partes2[0]);
								pw.flush();

								BufferedReader br8 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br8.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket7 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
								ObjectInputStream ois2 = new ObjectInputStream(socket7.getInputStream());
								jugadores = (ArrayList<Jugador>) ois2.readObject();

								System.out.println("LISTA DE JUGADORES: ");

								for (int i = 0; i < jugadores.size(); i++) {

									System.out.println("ID del jugador: " + jugadores.get(i).getId()
											+ ", nombre del jugador: " + jugadores.get(i).getNombre()
											+ ", apellidos del jugador: " + jugadores.get(i).getApellidos()
											+ ", goles del jugador: " + jugadores.get(i).getGoles() + ", equipo:"
											+ jugadores.get(i).getClub());
								}
							}

							else if (partes2[0].equals("ADDJUGADOR2CLUB") && partes2.length == 3) {
								pw.println("ADDJUGADOR2CLUB " + partes2[0]);
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" ");
								System.out.println(aux);

								Socket socket8 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4]));

								PrintWriter pw5 = new PrintWriter(new OutputStreamWriter(socket8.getOutputStream()));
								BufferedReader br9 = new BufferedReader(
										new InputStreamReader(socket8.getInputStream()));

								idJugador = partes2[1];
								idClub = partes2[2];

								pw5.println(idJugador);
								pw5.flush();
								pw5.println(idClub);
								pw5.flush();

								System.out.println(br9.readLine());

							} else if (partes2[0].equals("REMOVEJUGADORFROMCLUB") && partes2.length == 3) {
								pw.println("REMOVEJUGADORFROMCLUB " + partes2[0]);
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" ");
								System.out.println(aux);

								Socket socket9 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4]));

								PrintWriter pw6 = new PrintWriter(new OutputStreamWriter(socket9.getOutputStream()));
								BufferedReader br10 = new BufferedReader(
										new InputStreamReader(socket9.getInputStream()));
								idJugador = partes2[1];
								idClub = partes2[2];

								pw6.println(idJugador);
								pw6.flush();
								pw6.println(idClub);
								pw6.flush();

								System.out.println(br10.readLine());
							} else if (partes2[0].equals("LISTJUGFROMCLUB") && partes2.length == 2) {
								pw.println("LISTJUGFROMCLUB " + partes2[1]);
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" ");
								System.out.println(aux);

								Socket socket9 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4]));
								PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socket9.getOutputStream()));
								pw1.println(partes2[1]);
								pw1.flush();
								ObjectInputStream ois = new ObjectInputStream(socket9.getInputStream());

								ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
								jugadores = (ArrayList<Jugador>) ois.readObject();

								System.out.println("Jugadores del club:");
								for (Jugador j : jugadores) {
									System.out.println(j.toString());
								}

							} else if (partes2[0].equals("ADDPARTIDO") && partes2.length == 1) {
								pw.println("ADDPARTIDO " + partes2[0]);
								pw.flush();

								String aux = br.readLine();
								partesPuerto = aux.split(" ");
								System.out.println(aux);

								Socket socket5 = new Socket(partesPuerto[3], Integer.parseInt(partesPuerto[4]));

								System.out.println("Introduzca el id del partido: ");
								System.out.print(">> ");
								idPartido = scan.nextLine();

								System.out.println("Introduzca el nombre del club local: ");
								System.out.print(">> ");
								nombreClubLocal = scan.nextLine();

								System.out.println("Introduzca el nombre del club visitante: ");
								System.out.print(">> ");
								nombreClubVisitante = scan.nextLine();

								System.out.println("Introduzca el resultado del partido: ");
								System.out.print(">> ");
								resultado = scan.nextLine();

								Club equipoLocal = null;
								Club equipoVisitante = null;

								// verificamos si los clubs local y visitante existen
								ObjectInputStream ois = new ObjectInputStream(socket5.getInputStream());
								ArrayList<Club> clubs = (ArrayList<Club>) ois.readObject();
								for (Club club : clubs) {
									if (club.getNombre().equals(nombreClubLocal)) {
										equipoLocal = club;
									}
									if (club.getNombre().equals(nombreClubVisitante)) {
										equipoVisitante = club;
									}
								}

								if (equipoLocal != null && equipoVisitante != null) {
									Partido partido = new Partido(idPartido, equipoLocal, equipoVisitante, resultado);
									ObjectOutputStream oos1 = new ObjectOutputStream(socket5.getOutputStream());
									oos1.writeObject(partido);
									oos1.flush();
								} else {
									System.out.println("Uno o ambos clubs no existen, no se puede agregar el partido");
									break;
								}
								socket5.close();

							} else if (partes2[0].equals("UPDATEPARTIDO") && partes2.length == 2) {

								pw.println("UPDATEPARTIDO ");
								pw.flush();

								BufferedReader brGetP = new BufferedReader(
										new InputStreamReader(socket.getInputStream()));

								String aux = brGetP.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket2 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								BufferedReader br1 = new BufferedReader(
										new InputStreamReader(socket2.getInputStream()));
								PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()));

								idPartido = partes2[1];
								pw1.println(idPartido);
								pw1.flush();

								// Leemos los datos que nos han enviado del club encontrado o no
								String aux1 = br1.readLine();
								System.out.println(aux1);

								// Modificamos el club
								System.out.println("Introduzca la id: ");
								idPartido = scan.nextLine();

								System.out.println("Introduzca el equipo local: ");
								System.out.print(">> ");
								nombreClubLocal = scan.nextLine();

								System.out.println("Introduzca el equipo visitante: ");
								System.out.print(">> ");
								nombreClubVisitante = scan.nextLine();

								System.out.println("Introduzca el resultado: ");
								System.out.print(">> ");
								resultado = scan.nextLine();

								Club equipoLocal = null;
								Club equipoVisitante = null;

								// verificamos si los clubs local y visitante existen
								ObjectInputStream ois = new ObjectInputStream(socket2.getInputStream());
								ArrayList<Club> clubs = (ArrayList<Club>) ois.readObject();
								for (Club club : clubs) {
									if (club.getNombre().equals(nombreClubLocal)) {
										equipoLocal = club;
									}
									if (club.getNombre().equals(nombreClubVisitante)) {
										equipoVisitante = club;
									}
								}

								if (equipoLocal != null && equipoVisitante != null) {
									Partido partido = new Partido(idPartido, equipoLocal, equipoVisitante, resultado);
									ObjectOutputStream oos1 = new ObjectOutputStream(socket2.getOutputStream());
									oos1.writeObject(partido);
									oos1.flush();
								} else {
									System.out.println("Uno o ambos clubs no existen, no se puede agregar el partido");
									break;
								}

								System.out.println(br1.readLine());
								socket2.close();

							} else if (partes2[0].equals("GETPARTIDO") && partes2.length == 2) {
								pw.println("GETPARTIDO " + partes2[0]);
								pw.flush();

								BufferedReader br6 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br6.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket6 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								BufferedReader br7 = new BufferedReader(
										new InputStreamReader(socket6.getInputStream()));
								PrintWriter pw4 = new PrintWriter(new OutputStreamWriter(socket6.getOutputStream()));

								idPartido = partes2[1];
								pw4.println(idPartido);
								pw4.flush();

								System.out.println(br7.readLine());
								socket6.close();

							} else if (partes2[0].equals("REMOVEPARTIDO") && partes2.length == 2) {
								pw.println("REMOVEPARTIDO " + partes2[1]);
								pw.flush();

								System.out.println(br.readLine());

							} else if (partes2[0].equals("LISTPARTIDOS") && partes2.length == 1) {
								pw.println("LISTPARTIDOS " + partes2[0]);
								pw.flush();

								BufferedReader br8 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

								String aux = br8.readLine();

								String[] partesPuertoG = aux.split(" ");
								System.out.println(aux);
								Socket socket7 = new Socket(partesPuertoG[3], Integer.parseInt(partesPuertoG[4]));

								ArrayList<Partido> partidos = new ArrayList<Partido>();
								ObjectInputStream ois2 = new ObjectInputStream(socket7.getInputStream());
								partidos = (ArrayList<Partido>) ois2.readObject();

								System.out.println("LISTA DE PARTIDOS: ");
								for (int i = 0; i < partidos.size(); i++) {
									System.out.println(partidos.toString());
								}

							} else if (partes2[0].equals("COUNTPARTIDOS") && partes2.length == 1) {
								pw.println("COUNTPARTIDOS ");
								pw.flush();

								System.out.println("OK. Total de partidos en el sistema: " + br.readLine());

							} /*
								 * else if (partes2[0].equals("CLASIFICACION") && partes2.length == 3) {
								 * pw.println("CLASIFICACION "); pw.flush(); String aux = br.readLine();
								 * partesPuerto = aux.split(" "); System.out.println(aux);
								 * 
								 * Socket socket7 = new Socket(partesPuerto[3],
								 * Integer.parseInt(partesPuerto[4]));
								 * 
								 * PrintWriter pw4 = new PrintWriter(new
								 * OutputStreamWriter(socket7.getOutputStream())); BufferedReader br8 = new
								 * BufferedReader( new InputStreamReader(socket7.getInputStream()));
								 * 
								 * int numClubes = Integer.parseInt(br8.readLine()); for (int i = 0; i <
								 * numClubes; i++) { String nombreDelClub = br8.readLine(); int puntos1 =
								 * Integer.parseInt(br8.readLine()); System.out.println(nombreDelClub + " " +
								 * puntos1); } }
								 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("FAILED " + partes[0] + "Command error. Mete bien el USER. ");
				}
			}
		} catch (

		UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
