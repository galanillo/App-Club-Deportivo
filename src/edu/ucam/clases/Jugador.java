package edu.ucam.clases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class Jugador.
 */
public class Jugador implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The nombre. */
	private String nombre;
	
	/** The apellidos. */
	private String apellidos;
	
	/** The goles. */
	private int goles;
	
	/** The club. */
	private Club club;
	
	/** The clubs jug. */
	private ArrayList<Club> clubsJug;

	/**
	 * Instantiates a new jugador.
	 */
	public Jugador() {
	}

	/**
	 * Instantiates a new jugador.
	 *
	 * @param id the id
	 * @param nombre the nombre
	 * @param apellidos the apellidos
	 * @param goles the goles
	 * @param club the club
	 */
	public Jugador(String id, String nombre, String apellidos, int goles, Club club) {
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.goles = goles;
		this.club = club;
		this.clubsJug = new ArrayList<Club>();
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Jugador [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", goles=" + goles + ", club="
				+ club + "]";
	}

	/**
	 * Gets the club.
	 *
	 * @return the club
	 */
	public Club getClub() {
		return club;
	}

	/**
	 * Sets the club.
	 *
	 * @param club the new club
	 */
	public void setClub(Club club) {
		this.club = club;
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
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the apellidos.
	 *
	 * @return the apellidos
	 */
	public String getApellidos() {
		return this.apellidos;
	}

	/**
	 * Sets the apellidos.
	 *
	 * @param apellidos the new apellidos
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * Gets the goles.
	 *
	 * @return the goles
	 */
	public int getGoles() {
		return this.goles;
	}

	/**
	 * Sets the goles.
	 *
	 * @param goles the new goles
	 */
	public void setGoles(int goles) {
		this.goles = goles;
	}

	/**
	 * Adds the gol.
	 */
	public void addGol() {
		++this.goles;
	}

	/**
	 * Removes the gol.
	 */
	public void removeGol() {
		if (this.goles > 0) {
			--this.goles;
		}

	}

	/**
	 * Adds the jugador club.
	 *
	 * @param club the club
	 */
	public void addJugadorClub(Club club) {
		this.clubsJug.add(club);
	}
	
	/**
	 * Adds the jugador 2 club.
	 *
	 * @param club the club
	 */
	public void addJugador2Club(Club club) {
		this.club = club;
	}

	/**
	 * Removes the club jugador.
	 *
	 * @param club the club
	 */
	public void removeClubJugador(Club club) {
		this.club= null;
	}
	
	/**
	 * Removes the jugador from club.
	 *
	 * @param club the club
	 */
	public void removeJugadorFromClub(Club club) {
		this.club = club;
	}

	/**
	 * List club jugador.
	 *
	 * @param jugador the jugador
	 * @return the array list
	 */
	public ArrayList<Club> listClubJugador(Jugador jugador) {
		return jugador.clubsJug;
	}
}