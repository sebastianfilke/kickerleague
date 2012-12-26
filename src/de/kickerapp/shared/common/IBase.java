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
	 * @param id Die DB-ID für das Objekt als {@link long}.
	 */
	public void setId(long id);

	/**
	 * Liefert die DB-ID für das Objekt.
	 * 
	 * @return Die DB-ID für das Objekt.
	 */
	public long getId();

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

	/**
	 * Setzt das Serviceobjekt für den Datenspeicher.
	 * 
	 * @param serviceObject Das Serviceobjekt für den Datenspeicher.
	 */
	public void setServiceObject(byte[] serviceObject);

	/**
	 * Liefert das Serviceobjekt für den Datenspeicher.
	 * 
	 * @return Das Serviceobjekt für den Datenspeicher.
	 */
	public byte[] getServiceObject();

}
