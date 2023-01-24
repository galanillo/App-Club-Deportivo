package edu.ucam.app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import edu.ucam.clases.*;

/**
 * The Class ServerThread.
 */
public class ServerThread implements Runnable {

	/** The socket cliente. */
	private Socket socketCliente;

	/** The resultado. */
	String str, nombreClubLocal, nombreClubVisitante, idPartido, resultado;

	/** The puerto. */
	int puerto;

	/** The valor 1. */
	int valor1;

	/** The num clientes. */
	int numClientes = 0;

	/** The clientes. */
	List<Socket> clientes = Server.getClientes();

	/**
	 * Instantiates a new server thread.
	 *
	 * @param socketCliente the socket cliente
	 */
	public ServerThread(Socket socketCliente) {
		this.socketCliente = socketCliente;
		// keepAlive();
	}

	/** The club. */
	private static Hashtable<String, Club> club = new Hashtable<String, Club>();

	/** The jugador. */
	private static Hashtable<String, Jugador> jugador = new Hashtable<String, Jugador>();

	/** The partido. */
	private static Hashtable<String, Partido> partido = new Hashtable<String, Partido>();

	/**
	 * Gets the tabla clubs.
	 *
	 * @return the tabla clubs
	 */
	public static Hashtable<String, Club> getTablaClubs() {
		return club;
	}

	/**
	 * Gets the tabla jugadores.
	 *
	 * @return the tabla jugadores
	 */
	public static Hashtable<String, Jugador> getTablaJugadores() {
		return jugador;
	}

	/**
	 * Gets the tabla partidos.
	 *
	 * @return the tabla partidos
	 */
	public static Hashtable<String, Partido> getTablaPartidos() {
		return partido;
	}

	/**
	 * Random number.
	 *
	 * @return the double
	 */
	public static double randomNumber() {
		double valor = Math.floor(Math.random() * 6 + 1);
		return valor;
	}

	/*
	 * public void keepAlive() { Timer timer = new Timer(); timer.schedule(new
	 * TimerTask() {
	 * 
	 * @Override public void run() { try { PrintWriter pw8 = new PrintWriter(new
	 * OutputStreamWriter(socketCliente.getOutputStream()));
	 * pw8.println("KEEPALIVE"); pw8.flush(); BufferedReader br8 = new
	 * BufferedReader(new InputStreamReader(socketCliente.getInputStream())); String
	 * msg = br8.readLine(); if (msg != null && !msg.equals("OK")) {
	 * Server.removeCliente(socketCliente); timer.cancel(); } } catch (IOException
	 * e) { e.printStackTrace(); Server.removeCliente(socketCliente);
	 * timer.cancel(); } } }, 0, 30000); }
	 */

	/**
	 * Run.
	 */
	@Override
	public void run() {
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(this.socketCliente.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(this.socketCliente.getInputStream()));
			ObjectInputStream ios;
			LocalDateTime localDate = LocalDateTime.now();
			String formattedTime = localDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

			Club club = null; // Creamos un objeto de la clase Club para anadirlo despues
			Partido partido = null;
			Jugador jugador = null;

			// keepAlive();

			while (true) {
				str = br.readLine();
				String[] linea = str.split(" "); // Separamos la linea que ya se ha leido

				/*
				 * if (linea[0].equals("KO")) { Server.removeClienteActivo(linea[1]); }
				 * 
				 * if (linea[0].equals("CONECTADOS")) { // preguntamos si el cliente ha enviado
				 * el mensaje CONECTADOS pw.println("Hay " + Server.getClientesActivos().size()
				 * + " usuarios conectados."); //enviamos el numero de usuarios conectados
				 * pw.flush(); }
				 */

				switch (linea[0].toString()) {

				case "EXIT":
					// Eliminamos el cliente de la lista de clientes conectados
					Server.removeCliente(socketCliente);
					// Cerramos la conexion con el cliente
					socketCliente.close();
					// Finalizamos la ejecucion del hilo
					return;

				case "SESIONES":
					pw.println("Clientes conectados: " + clientes.size());
					pw.flush();
					break;

				/*
				 * case "KEEPALIVE": try { PrintWriter pw1 = new PrintWriter(new
				 * OutputStreamWriter(socketCliente.getOutputStream())); pw1.println("OK");
				 * pw1.flush(); } catch (IOException e) { e.printStackTrace();
				 * Server.removeClienteActivo(linea[1]); Server.removeCliente(socketCliente);
				 * socketCliente.close(); } break;
				 */

				case "ADDCLUB":
					valor1 = (int) randomNumber();
					// Sumamos 1 al puerto que tenemos ya definido en la clase Server para que no
					// choquen los clientes a la hora de realizar comandos
					puerto = Server.puerto();
					// Mensaje para el cliente con el socket del canal de datos
					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto); // Socket para el canal de datos
						Socket socketCliente = ss.accept(); // Aceptamos conexion en el canal de datos

						ios = new ObjectInputStream(socketCliente.getInputStream()); // Leemos el objeto que viene
																						// por
																						// el
																						// canal de datos
						club = (Club) ios.readObject();

						System.out.println("(" + formattedTime + ") " + ">> Se ha agregado el siguiente club: "
								+ club.getId() + " " + club.getNombre()); // Mostramos el objeto

						Club e;
						ArrayList<Club> club2 = new ArrayList<Club>(); // Creamos la lista de los clubs

						// Comprobamos que el club no esta en la lista de los clubs ya registrados
						Enumeration<Club> enumeration = getTablaClubs().elements();
						while (enumeration.hasMoreElements()) {
							e = (Club) enumeration.nextElement();
							club2.add(e);
						}

						boolean encontrado = false;
						for (Club clubb : club2) {
							if (clubb.getId().equals(club.getId())) {
								encontrado = true; // Existe un club con esa id.
								break;
							}
						}

						if (encontrado) {
							str = "Ya existe un club con ese id";
						}

						else {
							getTablaClubs().put(club.getId(), club); // Adjuntamos el objeto club a la tabla de
																		// clubs
							str = "Club adjuntado";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));

						pw1.println(str);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					break;
				case "GETCLUB":
					valor1 = (int) randomNumber();
					// Sumamos 1 al puerto que tenemos definido en la clase servidor para que no
					// choquen los clientes a la hora de realizar comandos
					puerto = Server.puerto();
					// Mensaje para el cliente con el socket del canal de datos
					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteGet = ss.accept();
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(socketClienteGet.getInputStream()));// Obtenemos
																							// el
																							// club
																							// para
																							// imprimirlo
																							// en
																							// Cliente
						club = getTablaClubs().get(br1.readLine());
						if (club != null) {
							str = "OK" + " " + club.getId() + " " + club.getNombre();
						} else {
							str = "No existe";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketClienteGet.getOutputStream()));
						pw1.println(str);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				case "UPDATECLUB":

					valor1 = (int) randomNumber();
					puerto = Server.puerto();
					String msg = "OK " + linea[0] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;

					pw.println(msg);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteUpdate = ss.accept();

						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(socketClienteUpdate.getInputStream()));
						PrintWriter pw1 = new PrintWriter(
								new OutputStreamWriter(socketClienteUpdate.getOutputStream()));

						club = getTablaClubs().get(br1.readLine());
						if (club != null) {
							msg = "Club encontrado; " + club.getId() + " " + club.getNombre() + " "
									+ "- Inserte los nuevos datos.";
							getTablaClubs().remove(club.getId(), club);
							pw1.println(msg);
							pw1.flush();

							ios = new ObjectInputStream(socketClienteUpdate.getInputStream());
							club = (Club) ios.readObject();
							getTablaClubs().put(club.getId(), club);

							PrintWriter pw11 = new PrintWriter(
									new OutputStreamWriter(socketClienteUpdate.getOutputStream()));
							msg = "Club modificado.";
							System.out.println(
									"(" + formattedTime + ") " + ">> Se ha modificado el club: " + club.getNombre());
							pw11.println(msg);
							pw11.flush();

						} else {
							msg = "No existe el club por lo que no se puede actualizar.";
							pw1.println(msg);
							pw1.flush();
						}

					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					break;

				case "REMOVECLUB":
					// Comprobamos si existe el club para poder eliminarlo y ser� eliminado si se
					// encuentra.

					club = getTablaClubs().get(linea[1]);
					if (club != null) {
						getTablaClubs().remove(linea[1]);
						msg = "OK El club con el ID " + linea[1] + " ha sido eliminado.";
						System.out
								.println("(" + formattedTime + ") " + ">> Se ha eliminado el club con id: " + linea[1]);

					} else {
						msg = "FAILED. No se ha encontrado el club.";
					}
					pw.println(msg);
					pw.flush();

					break;

				case "LISTCLUBES":
					// Listado de CLUBES existentes
					valor1 = (int) randomNumber();

					puerto = Server.puerto();
					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketCliente = ss.accept();
						Club c;
						ArrayList<Club> clubs = new ArrayList<Club>();
						Enumeration<Club> enumeration = getTablaClubs().elements();
						while (enumeration.hasMoreElements()) {
							c = (Club) enumeration.nextElement();
							clubs.add(c);
						}

						ObjectOutputStream out = new ObjectOutputStream(socketCliente.getOutputStream());
						out.writeObject(clubs);
						out.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				case "COUNTCLUBES":
					int i = 0;
					Enumeration<Club> enumeration = getTablaClubs().elements();
					while (enumeration.hasMoreElements()) {
						enumeration.nextElement();
						i++;
					}
					pw.println(i);
					pw.flush();

					break;
				case "ADDJUGADOR":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto); // Socket para canal de datos
						Socket socketCliente = ss.accept(); // Aceptamos conexion en el canal de datos

						ios = new ObjectInputStream(socketCliente.getInputStream()); // Leemos el objeto que viene
																						// por
																						// el canal de datos
						jugador = (Jugador) ios.readObject();

						Jugador j;
						ArrayList<Jugador> jugador2 = new ArrayList<Jugador>(); // CREAMOS LISTA DE LAS
																				// jugadores
						// Comprobamos que el jugador no estuviera ya insertado en la lista.
						Enumeration<Jugador> enumerationPar = getTablaJugadores().elements();
						while (enumerationPar.hasMoreElements()) {
							j = (Jugador) enumerationPar.nextElement();
							jugador2.add(j);
						}

						boolean encontrado = false;
						for (Jugador jugadores : jugador2) {
							if (jugadores.getId().equals(jugador.getId())) {
								encontrado = true;
								break;
							}
						}

						if (encontrado) {
							str = "Ya existe ese Jugador.";
						}

						else {
							getTablaJugadores().put(jugador.getId(), jugador); // ANADIMOS EL OBJETO club
																				// A LA TABLA HASH
																				// jugadores
							str = "Jugador adjuntado";
						}

						PrintWriter pw12L = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));

						pw12L.println(str);
						pw12L.flush();

					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}

					break;
				case "GETJUGADOR":

					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteGet = ss.accept();
						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(socketClienteGet.getInputStream()));
						jugador = getTablaJugadores().get(br1.readLine());
						if (jugador != null) {
							str = "OK  " + " " + jugador.getId() + " " + jugador.getNombre() + " "
									+ jugador.getApellidos() + " " + jugador.getGoles() + " " + jugador.getClub();

						} else {
							str = "No existe";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketClienteGet.getOutputStream()));
						pw1.println(str);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;

				case "REMOVEJUGADOR":
					// Comprobamos si existe el Jugador para poder eliminarlo y lo eliminamos si
					// se encuentra.

					jugador = getTablaJugadores().get(linea[1]);
					if (jugador != null) {
						getTablaJugadores().remove(linea[1]);
						msg = "OK El Jugador con el ID " + linea[1] + " Ha sido eliminado";
						System.out.println(
								"(" + formattedTime + ") " + ">> El jugador con id: " + linea + " ha sido eliminado.");
					} else {
						msg = "FAILED. No autorizado";
					}
					pw.println(msg);
					pw.flush();

					break;

				case "LISTJUGADORES":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteList = ss.accept();
						Jugador j;
						ArrayList<Jugador> jugadores2 = new ArrayList<Jugador>();
						Enumeration<Jugador> enumeration3 = getTablaJugadores().elements();
						while (enumeration3.hasMoreElements()) {
							j = (Jugador) enumeration3.nextElement();
							jugadores2.add(j);
						}

						ObjectOutputStream oos = new ObjectOutputStream(socketClienteList.getOutputStream());
						oos.writeObject(jugadores2);
						oos.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				case "ADDJUGADOR2CLUB":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteA = ss.accept();
						BufferedReader br1 = new BufferedReader(new InputStreamReader(socketClienteA.getInputStream()));
						// Realizamos varias comprobaciones antes de insertarlos por si no existe el
						// club
						club = getTablaClubs().get(br1.readLine());
						if (club != null) {
							jugador = getTablaJugadores().get(br1.readLine());
							if (jugador != null) {
								jugador.addJugadorClub(club);
								jugador.addJugador2Club(club);
								msg = "OK. Se unio el jugador al club";
								System.out.println("(" + formattedTime + ") " + ">> El jugador con el id: "
										+ jugador.getId() + " se ha unido al club: " + club.getNombre());
							} else {
								msg = "No se encuentra el jugador";
							}
						}

						else {
							msg = "No se encuentra el club";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketClienteA.getOutputStream()));
						pw1.println(msg);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;

				case "REMOVEJUGADORFROMCLUB":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteR = ss.accept();
						BufferedReader br1 = new BufferedReader(new InputStreamReader(socketClienteR.getInputStream()));

						String aux = br1.readLine();
						// Realizamos varias comprobaciones antes de borrarlos, por si no existe la
						// club y/o el Jugador.
						club = getTablaClubs().get(aux);
						if (club != null) {

							jugador = getTablaJugadores().get(br1.readLine());
							if (jugador != null) {
								jugador.removeClubJugador(club);
								jugador.removeJugadorFromClub(club);
								msg = "OK. Se ha eliminado al jugador del club";
								System.out.println("(" + formattedTime + ") " + ">> El jugador con el id: "
										+ jugador.getId() + " se ha eliminado del club: " + club.getNombre());
							} else {
								msg = "FAILED. No se encuentra el Jugador";
							}
						}

						else {
							msg = "FAILED - No se encuentra el club";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketClienteR.getOutputStream()));
						pw1.println(msg);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

				case "LISTJUGFROMCLUB":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteR = ss.accept();
						BufferedReader br1 = new BufferedReader(new InputStreamReader(socketClienteR.getInputStream()));
						String idJugador2 = br1.readLine();
						ArrayList<Jugador> jugadoresDelClub = new ArrayList<Jugador>();
						Club club2 = null;
						for (Jugador jug : getTablaJugadores().values()) {
							if (jug.getId().equals(idJugador2)) {
								club2 = jug.getClub();
							}
						}
						if (club2 != null) {
							for (Jugador jug : getTablaJugadores().values()) {
								if (jug.getClub() == club2)
									jugadoresDelClub.add(jug);
							}
							ObjectOutputStream oos = new ObjectOutputStream(socketClienteR.getOutputStream());
							oos.writeObject(jugadoresDelClub);
							oos.flush();
						} else {
							pw.println("No se encontro el jugador solicitado");
							pw.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				case "ADDPARTIDO":
					valor1 = (int) randomNumber();
					puerto = Server.puerto();
					str = "OK " + linea[1] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();
					try {
						ServerSocket ss = new ServerSocket(puerto); // Socket para canal de datos
						Socket socketCliente = ss.accept(); // Aceptamos conexion en el canal de datos
						ios = new ObjectInputStream(socketCliente.getInputStream()); // Leemos el objeto que viene
																						// por
																						// el canal de datos
						partido = (Partido) ios.readObject();

						boolean encontradoLocal = false;
						boolean encontradoVisitante = false;

						Club c1;
						ArrayList<Club> clubs21 = new ArrayList<Club>();
						Enumeration<Club> enumeration311 = getTablaClubs().elements();
						while (enumeration311.hasMoreElements()) {
							c1 = (Club) enumeration311.nextElement();
							clubs21.add(c1);
						}

						// iteramos para verificar que el club local y visitante existen
						for (Club club1 : clubs21) {
							if (club1.getNombre().equals(partido.getEquipoLocal().getNombre())) {
								encontradoLocal = true;
								break;
							}
							if (club1.getNombre().equals(partido.getEquipoVisitante().getNombre())) {
								encontradoVisitante = true;
								break;
							}
						}

						// si los dos equipos existen los agregamos al HashTable
						if (encontradoLocal && encontradoVisitante) {
							getTablaPartidos().put(partido.getId(), partido);
							str = "El partido ha sido agregado exitosamente";
							System.out.println("(" + formattedTime + ") "
									+ ">> Se ha agregado un nuevo partido con las siguientes caracteristicas: "
									+ partido.getId() + " " + partido.getEquipoLocal() + " "
									+ partido.getEquipoVisitante() + " " + partido.getResultado());
						} else {
							str = "Uno o ambos clubs no existen, no se puede agregar el partido";
						}
						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
						pw1.println(str);
						pw1.flush();
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					}
					break;

				case "UPDATEPARTIDO":
					valor1 = (int) randomNumber();
					puerto = Server.puerto();

					str = "OK " + linea[0] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss = new ServerSocket(puerto);
						Socket socketClienteUpdate = ss.accept();

						BufferedReader br1 = new BufferedReader(
								new InputStreamReader(socketClienteUpdate.getInputStream()));
						PrintWriter pw1 = new PrintWriter(
								new OutputStreamWriter(socketClienteUpdate.getOutputStream()));

						partido = getTablaPartidos().get(br1.readLine());
						if (partido != null) {
							msg = "Partido encontrado; " + partido.getId() + " " + partido.getEquipoLocal() + " " + " "
									+ partido.getEquipoVisitante() + " " + partido.getResultado()
									+ "- Inserte los nuevos datos.";
							getTablaPartidos().remove(partido.getId(), partido);
							pw1.println(msg);
							pw1.flush();

							ios = new ObjectInputStream(socketClienteUpdate.getInputStream());
							partido = (Partido) ios.readObject();
							getTablaPartidos().put(partido.getId(), partido);

							PrintWriter pw11 = new PrintWriter(
									new OutputStreamWriter(socketClienteUpdate.getOutputStream()));
							msg = "Partido modificado.";
							System.out.println(
									"(" + formattedTime + ") " + ">> Se ha modificado el partido: " + partido.getId());
							pw11.println(msg);
							pw11.flush();

						} else {
							msg = "No existe el partido por lo que no se puede actualizar.";
							pw1.println(msg);
							pw1.flush();
						}
					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
					}
					break;

				case "GETPARTIDO":

					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[0] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss1 = new ServerSocket(puerto);
						Socket socketClienteGet = ss1.accept();
						BufferedReader br11 = new BufferedReader(
								new InputStreamReader(socketClienteGet.getInputStream()));
						partido = getTablaPartidos().get(br11.readLine());
						if (partido != null) {
							str = "OK  " + " " + partido.toString();

						} else {
							str = "No existe";
						}

						PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(socketClienteGet.getOutputStream()));
						pw1.println(str);
						pw1.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;

				case "REMOVEPARTIDO":
					// Comprobamos si existe el Jugador para poder eliminarlo y lo eliminamos si
					// se encuentra.

					partido = getTablaPartidos().get(linea[1]);
					if (partido != null) {
						getTablaPartidos().remove(linea[1]);
						msg = "OK El partido con el ID " + linea[1] + " ha sido eliminado";

					} else {
						msg = "FAILED. No autorizado";
					}
					pw.println(msg);
					pw.flush();

					break;

				case "LISTPARTIDOS":
					valor1 = (int) randomNumber();

					puerto = Server.puerto();

					str = "OK " + linea[0] + " " + valor1 + " "
							+ socketCliente.getLocalAddress().toString().substring(1) + " " + puerto;
					pw.println(str);
					pw.flush();

					try {
						ServerSocket ss1 = new ServerSocket(puerto);
						Socket socketClienteList = ss1.accept();
						Partido p;
						ArrayList<Partido> partidos2 = new ArrayList<Partido>();
						Enumeration<Partido> enumeration3 = getTablaPartidos().elements();
						while (enumeration3.hasMoreElements()) {
							p = (Partido) enumeration3.nextElement();
							partidos2.add(p);
						}
						ObjectOutputStream oos = new ObjectOutputStream(socketClienteList.getOutputStream());
						oos.writeObject(partidos2);
						oos.flush();

					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				case "COUNTPARTIDOS":
					int i1 = 0;
					Enumeration<Partido> enumeration1 = getTablaPartidos().elements();
					while (enumeration1.hasMoreElements()) {
						enumeration1.nextElement();
						i1++;
					}
					pw.println(i1);
					pw.flush();

					break;
				/*
				 * case "CLASIFICACION": Hashtable<String, Club> clubes = new Hashtable<String,
				 * Club>(); // Ordena la lista de clubes por n�mero de puntos y muestra la
				 * clasificacion List<Club> clubsOrdenados = new
				 * ArrayList<Club>(clubes.values()); clubsOrdenados.sort(new Comparator<Club>()
				 * {
				 * 
				 * @Override public int compare(Club o1, Club o2) { return o2.getPuntos() -
				 * o1.getPuntos(); } });
				 * 
				 * StringBuilder sb = new StringBuilder(); sb.append("CLASIFICACION\n"); for
				 * (Club c : clubsOrdenados) { sb.append(c.getNombre() + " " + c.getPuntos() +
				 * "\n"); } pw.println(sb.toString()); pw.flush(); break;
				 */
				default: {
					pw.println("Comando no encontrado ");
					pw.flush();
				}
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}
}
