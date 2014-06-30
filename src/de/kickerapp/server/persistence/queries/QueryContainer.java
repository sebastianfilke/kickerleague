package de.kickerapp.server.persistence.queries;

/**
 * Container-Klasse zum Abfragen von Instanzen.
 * 
 * @author Sebastian Filke
 */
public class QueryContainer {

	private String[] plans;

	private String query;

	private String ordering;

	private Object parameter;

	public QueryContainer() {
		super();

		plans = new String[] {};
		query = "";
		ordering = "";
		parameter = null;
	}

	public String[] getPlans() {
		return plans;
	}

	public void setPlans(String... plans) {
		this.plans = plans;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	public Object getParameter() {
		return parameter;
	}

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

}
