package edu.ucam.clases;

import java.io.Serializable;

/**
 * The Class Partido.
 */
public class Partido implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The equipo local. */
	private Club equipoLocal;
	
	/** The equipo visitante. */
	private Club equipoVisitante;
	
	/** The resultado. */
	private String resultado;

	/**
	 * Instantiates a new partido.
	 *
	 * @param id the id
	 * @param equipoLocal the equipo local
	 * @param equipoVisitante the equipo visitante
	 * @param resultado the resultado
	 */
	public Partido(String id, Club equipoLocal, Club equipoVisitante, String resultado) {
		this.id = id;
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.resultado = resultado;
	}

	/**
	 * Sets the resultado.
	 *
	 * @param resultado the new resultado
	 */
	public void setResultado(String resultado) {
		this.resultado = resultado;
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
	 * Gets the equipo local.
	 *
	 * @return the equipo local
	 */
	public Club getEquipoLocal() {
		return this.equipoLocal;
	}

	/**
	 * Sets the equipo local.
	 *
	 * @param equipoLocal the new equipo local
	 */
	public void setEquipoLocal(Club equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	/**
	 * Gets the equipo visitante.
	 *
	 * @return the equipo visitante
	 */
	public Club getEquipoVisitante() {
		return this.equipoVisitante;
	}

	/**
	 * Sets the equipo visitante.
	 *
	 * @param equipoVisitante the new equipo visitante
	 */
	public void setEquipoVisitante(Club equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	/**
	 * Gets the resultado.
	 *
	 * @return the resultado
	 */
	public String getResultado() {
		return this.resultado;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Partido [id=" + id + ", equipoLocal=" + equipoLocal + ", equipoVisitante=" + equipoVisitante
				+ ", resultado=" + resultado + "]";
	}

}