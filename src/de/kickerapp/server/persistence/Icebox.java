package de.kickerapp.server.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Serialisiert Datenobjekte in ein Byte-Array um es vom Server zum Clienten und
 * wieder zurück schicken zu können.
 * 
 * @author Sebastian Filke
 */
public final class Icebox {

	/** Der Logger der Klasse. */
	private static transient final Logger LOGGER = Logger.getLogger(Icebox.class.getName());

	/**
	 * Privater Konstruktor soll nicht von außen aufgerufen werden.
	 */
	private Icebox() {
	}

	/**
	 * Serialisiert das Datenobjekt in ein Byte-Array.
	 * 
	 * @param <T> Der Typ der Daten.
	 * @param data Die Daten welche zu Serialisieren sind.
	 * @return Serialisierte Daten als Byte-Array.
	 */
	public static <T extends Serializable> byte[] freeze(T data) {
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objectOut;
		DeflaterOutputStream zipStream;
		try {
			zipStream = new DeflaterOutputStream(byteOut);

			objectOut = new ObjectOutputStream(zipStream);
			objectOut.writeObject(data);
			objectOut.flush();
			objectOut.close();

			zipStream.flush();
			zipStream.close();

			final byte[] result = byteOut.toByteArray();

			return result;
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage());
		}
		return null;
	}

	/**
	 * Deserialisiert die Daten aus dem Byte-Array in das Datenobjekt.
	 * 
	 * @param <T> Der Typ der Daten.
	 * @param stream Das Byte-Array welches zu Deserialisieren sind.
	 * @return Das Datenobjekt aus dem Byte-Array.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T melt(byte[] stream) {
		final ByteArrayInputStream byteIn = new ByteArrayInputStream(stream);
		ObjectInputStream objectIn;
		InflaterInputStream zipStream;
		try {
			zipStream = new InflaterInputStream(byteIn);
			objectIn = new ObjectInputStream(zipStream);
			T data = null;
			try {
				data = (T) objectIn.readObject();
			} catch (final ClassNotFoundException e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
			objectIn.close();
			zipStream.close();

			return data;
		} catch (IOException ioe) {
			LOGGER.log(Level.SEVERE, ioe.getMessage());
		}
		return null;
	}

}
