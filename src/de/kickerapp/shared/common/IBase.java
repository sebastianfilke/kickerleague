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

}
