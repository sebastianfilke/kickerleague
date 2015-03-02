package de.kickerapp.client.ui.token;

import java.util.HashMap;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

import de.kickerapp.client.ui.navigation.NavigationElement;

/**
 * Hilfsklasse zum Separieren der Zeichen einer URL.
 * 
 * @author Sebastian Filke
 */
public class Tokenizer {

	/** Die Zeichen der URL. */
	private String token;
	/** Das selektierte Element der Navigationsleiste. */
	private NavigationElement navigationElement;
	/** Die eingegebenen Parameter der URL. */
	private HashMap<String, String> parameter;

	/**
	 * Erstellt eine neue Hilfsklasse zum Separieren der Zeichen einer URL.
	 * 
	 * @param token Die Zeichen der URL.
	 */
	public Tokenizer(String token) {
		this.token = token;

		navigationElement = NavigationElement.UNKOWN;
		parameter = new HashMap<String, String>();

		parseToken();
	}

	/**
	 * Analysiert die URL.
	 */
	private void parseToken() {
		final RegExp urlRegExp = RegExp.compile("(?=[?&])");
		final RegExp parameterRegExp = RegExp.compile("(?=[=])");

		final SplitResult urlSplit = urlRegExp.split(token);
		for (int i = 0; i < urlSplit.length(); i++) {
			final SplitResult parameterSplit = parameterRegExp.split(urlSplit.get(i));
			if (parameterSplit.length() == 1) {
				getNavigationElement(parameterSplit.get(0));
			} else {
				addParameterIfNotEmpty(parameterSplit.get(0), parameterSplit.get(1));
			}
		}
	}

	/**
	 * Liefert das selektierte Element der Navigationsleiste.
	 * 
	 * @param identifier Der Schlüssel desNavigationselements.
	 */
	private void getNavigationElement(String identifier) {
		if (NavigationElement.TABLES.getIdentificator().equals(identifier) || "".equals(identifier)) {
			navigationElement = NavigationElement.TABLES;
		} else if (NavigationElement.SINGLETABLE.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.SINGLETABLE;
		} else if (NavigationElement.DOUBLETABLE.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.DOUBLETABLE;
		} else if (NavigationElement.TEAMTABLE.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.TEAMTABLE;
		} else if (NavigationElement.RESULTS.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.RESULTS;
		} else if (NavigationElement.INSERT.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.INSERT;
		} else if (NavigationElement.CHARTS.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.CHARTS;
		} else if (NavigationElement.SINGLECHART.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.SINGLECHART;
		} else if (NavigationElement.DOUBLECHART.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.DOUBLECHART;
		} else if (NavigationElement.TEAMCHART.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.TEAMCHART;
		} else if (NavigationElement.PLAYER.getIdentificator().equals(identifier)) {
			navigationElement = NavigationElement.PLAYER;
		}
	}

	/**
	 * Fügt einen URL-Parameter der Liste hinzu, falls ein Schlüssel und Wert exisitert.
	 * 
	 * @param key Der Schlüssel des Parameters.
	 * @param value Der Wert des Parameters.
	 */
	private void addParameterIfNotEmpty(String key, String value) {
		if (!key.isEmpty() && !value.isEmpty()) {
			parameter.put(key.substring(1), value.substring(1));
		}
	}

	/**
	 * Liefert das selektierte Element der Navigationsleiste.
	 * 
	 * @return Das selektierte Element der Navigationsleiste als {@link NavigationElement}.
	 */
	public NavigationElement getNavigationElement() {
		return navigationElement;
	}

	/**
	 * Liefert die eingegebenen Parameter der URL.
	 * 
	 * @return Die eingegebenen Parameter der URL als {@link HashMap}.
	 */
	public HashMap<String, String> getParameter() {
		return parameter;
	}

}
