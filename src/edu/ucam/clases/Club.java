package edu.ucam.clases;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * The Class Club.
 */
public class Club implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The nombre. */
	private String nombre;
	
	/** The jugadores. */
	private Hashtable<String, Jugador> jugadores = new Hashtable<String, Jugador>();
	
	/** The puntos. */
	private int puntos;

	/**
	 * Instantiates a new club.
	 */
	public Club() {
	}

	/**
	 * Instantiates a new club.
	 *
	 * @param id the id
	 * @param nombre the nombre
	 * @param puntos the puntos
	 */
	public Club(String id, String nombre, int puntos) {
		this.id = id;
		this.nombre = nombre;
		this.puntos = 0;
	}

	/**
	 * Instantiates a new club.
	 *
	 * @param nombre the nombre
	 */
	public Club(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Sets the jugadores.
	 *
	 * @param jugadores the jugadores
	 */
	public void setJugadores(Hashtable<String, Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	/**
	 * Sets the puntos.
	 *
	 * @param puntos the new puntos
	 */
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Gets the nombre.
	 *
	 * @param nombre the nombre
	 * @return the nombre
	 */
	public String getNombre(String nombre) {
		return this.nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Adds the jugador.
	 *
	 * @param id the id
	 * @param jugador the jugador
	 */
	public void addJugador(String id, Jugador jugador) {
		this.jugadores.put(id, jugador);
	}

	/**
	 * Update jugador.
	 *
	 * @param id the id
	 * @param jugador the jugador
	 */
	public void updateJugador(String id, Jugador jugador) {
		if (this.jugadores.get(id) != null) {
			this.jugadores.replace(id, jugador);
		}

	}

	/**
	 * Removes the jugador.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean removeJugador(String id) {
		if (this.jugadores.get(id) != null) {
			this.jugadores.remove(id);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Total jugadores.
	 *
	 * @return the int
	 */
	public int totalJugadores() {
		return this.jugadores.size();
	}

	/**
	 * Gets the jugadores.
	 *
	 * @return the jugadores
	 */
	public Hashtable<String, Jugador> getJugadores() {
		return jugadores;
	}

	/**
	 * Gets the puntos.
	 *
	 * @return the puntos
	 */
	public int getPuntos() {
		return this.puntos;
	}

	/**
	 * Actualizar puntos.
	 *
	 * @param resultado the resultado
	 */
	public void actualizarPuntos(String resultado) {
		if (resultado.equals("VICTORIA")) {
			this.puntos += 3;
		} else if (resultado.equals("EMPATE")) {
			this.puntos += 1;
		} else if (resultado.equals("DERROTA")) {
			this.puntos += 0;
		}
	}

	/**
	 * Adds the puntos.
	 *
	 * @param puntos the puntos
	 * @return the int
	 */
	public int addPuntos(int puntos) {
		return this.puntos;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Club [id=" + id + ", nombre=" + nombre + "]";
	}
}