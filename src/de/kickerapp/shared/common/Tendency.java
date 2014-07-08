package de.kickerapp.shared.common;

/**
 * Aufzählung für die unterschiedlichen Typen von Tendenzen.
 * 
 * @author Sebastian Filke
 */
public enum Tendency implements BaseSerializable {

	/**
	 * Aufwärtstrend.
	 */
	UPWARD,

	/**
	 * Abwärtstrend.
	 */
	DOWNWARD,

	/**
	 * Konstanter-Trend.
	 */
	CONSTANT;

}
