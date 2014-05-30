package de.kickerapp.shared.common;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Das Interface der Client-Basisklasse für Datenbankobjekte.
 * 
 * @author Sebastian Filke
 */
public interface IBase extends Serializable, IsSerializable {

	/**
	 * Setzt die DB-Id für das Objekt.
	 * 
	 * @param id Die DB-Id für das Objekt als {@link long}.
	 */
	public void setId(long id);

	/**
	 * Liefert die DB-Id für das Objekt.
	 * 
	 * @return Die DB-Id für das Objekt.
	 */
	public long getId();

}
