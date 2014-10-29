package de.kickerapp.server.persistence.queries;

/**
 * Container-Klasse zum Abfragen von Instanzen.
 * 
 * @author Sebastian Filke
 */
public class QueryContainer {

	/** Die Pläne für die Abfragen der Instanzen. */
	private String[] plans;
	/** Die Abfrage für die Instanzen. */
	private String query;
	/** Die Reihenfolge für die Instanzen. */
	private String ordering;
	/** Der zu verwendende Parameter. */
	private Object[] parameter;

	/**
	 * Erstellt einen leeren Container zum Abfragen von Instanzen.
	 */
	public QueryContainer() {
		super();

		plans = new String[] {};
		query = "";
		ordering = "";
		parameter = new Object[] {};
	}

	/**
	 * Liefert die Pläne für die Abfragen der Instanzen.
	 * 
	 * @return Die Pläne für die Abfragen der Instanzen als {@link String[]}.
	 */
	public String[] getPlans() {
		return plans;
	}

	/**
	 * Setzt die Pläne für die Abfragen der Instanzen.
	 * 
	 * @param plans Die Pläne für die Abfragen der Instanzen als {@link String[]}.
	 */
	public void setPlans(String... plans) {
		this.plans = plans;
	}

	/**
	 * Liefert die Abfrage für die Instanzen.
	 * 
	 * @return Die Abfrage für die Instanzen als {@link String}.
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Setzt die Abfrage für die Instanzen.
	 * 
	 * @param query Die Abfrage für die Instanzen als {@link String}.
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Liefert die Reihenfolge für die Instanzen.
	 * 
	 * @return Die Reihenfolge für die Instanzen als {@link String}.
	 */
	public String getOrdering() {
		return ordering;
	}

	/**
	 * Setzt die Reihenfolge für die Instanzen.
	 * 
	 * @param ordering Die Reihenfolge für die Instanzen als {@link String}.
	 */
	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	/**
	 * Liefert den zu verwendeten Parameter.
	 * 
	 * @return Der zu verwendete Parameter als {@link Object}.
	 */
	public Object[] getParameter() {
		return parameter;
	}

	/**
	 * Setzt den zu verwendeten Parameter.
	 * 
	 * @param parameter Der zu verwendete Parameter als {@link Object}.
	 */
	public void setParameter(Object[] parameter) {
		this.parameter = parameter;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append(" [");
		sb.append("query=").append(query).append(", ");
		sb.append("ordering=").append(ordering).append(", ");
		sb.append("]");

		return sb.toString();
	}

}
