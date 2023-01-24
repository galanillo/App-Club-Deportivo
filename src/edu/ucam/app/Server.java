package edu.ucam.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Server.
 */
public class Server {

	/** The puerto. */
	private static int puerto = 5001;
	
	/** The clientes. */
	private static List<Socket> clientes = new ArrayList<>();
	/**
	 * Puerto.
	 *
	 * @return the int
	 */
	public static int puerto() {
		return puerto++;
	}

	/**
	 * Adds the cliente.
	 *
	 * @param cliente the cliente
	 */
	public static void addCliente(Socket cliente) {
		// Añade un cliente a la lista de clientes conectados
		clientes.add(cliente);
	}

	/**
	 * Removes the cliente.
	 *
	 * @param cliente the cliente
	 */
	public static void removeCliente(Socket cliente) {
		// Elimina un cliente de la lista de clientes conectados
		if(clientes.contains(cliente)) {
	        clientes.remove(cliente);
	        try {
	            cliente.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

	/**
	 * Gets the clientes.
	 *
	 * @return the clientes
	 */
	public static List<Socket> getClientes() {
		// Devuelve la lista de clientes conectados
		return clientes;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Iniciando servidor");
			try (ServerSocket ss = new ServerSocket(5000)) {
				while (true) {
					Socket socketCliente = ss.accept();
					Server.addCliente(socketCliente);
					ServerThread serverThread = new ServerThread(socketCliente);
					Thread hilo = new Thread(serverThread);
					hilo.start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
	//INTENTO DE IMPLEMENTAR SISTEMA DE CONTROL DE USUARIO CONECTADOS

	/*public static void addClienteActivo(String cliente) {
		clientesActivos.add(cliente);
	}

	public static void removeClienteActivo(String cliente) {
		clientesActivos.remove(cliente);
	}

	public static List<String> getClientesActivos() {
		return clientesActivos;
	}

	public static void main(String[] args) {
        try {
            System.out.println("Iniciando servidor");
            try (ServerSocket ss = new ServerSocket(5000)) {
                keepAliveTimer.scheduleAtFixedRate(new TimerTask() {
                    public void run() {
                        for (Socket cliente : clientes) {
                            PrintWriter pw = null;
                            BufferedReader br = null;
							try {
								pw = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream()));
								br = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
								pw.println("KEEPALIVE");
								pw.flush();
								String keepAliveResponse = br.readLine();
								if (!keepAliveResponse.equals("OK")) {
									removeCliente(cliente);
								}
							} catch (IOException e) {
								e.printStackTrace();
								removeCliente(cliente);
							}
                        }
                    }
                }, 30000, 30000);
                while (true) {
                    Socket socketCliente = ss.accept();
                    addCliente(socketCliente);
                    clientesActivos.add(socketCliente.getInetAddress().getHostName());
                    ServerThread serverThread = new ServerThread(socketCliente);
                    Thread hilo = new Thread(serverThread);
                    hilo.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
