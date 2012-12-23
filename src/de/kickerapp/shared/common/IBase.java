package de.kickerapp.shared.common;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Das Interface der Basisklasse für Datenbankobjekte im Client.
 * 
 * @author Sebastian Filke
 */
public interface IBase extends Serializable, IsSerializable {

	/**
	 * Setzt die DB-ID für das Objekt.
	 * 
	 * @param id Die DB-ID für das Objekt als {@link int}.
	 */
	public void setId(int id);

	/**
	 * Liefert die DB-ID für das Objekt.
	 * 
	 * @return Die DB-ID für das Objekt.
	 */
	public int getId();

	/**
	 * Setzt den Anzeigetext für ein ausgewähltes Objekt.
	 * 
	 * @param label Der Anzeigetext für ein ausgewähltes Objekta als
	 *            {@link String}.
	 */
	public void setLabel(String label);

	/**
	 * Liefert den Anzeigetext für ein ausgewähltes Objekt.
	 * 
	 * @return Der Anzeigetext für ein ausgewähltes Objekt.
	 */
	public String getLabel();

}
