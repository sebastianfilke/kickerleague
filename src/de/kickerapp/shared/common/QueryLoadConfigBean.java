package de.kickerapp.shared.common;

import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;

/**
 * A {@link PagingLoadConfigBean} with support for filters.
 */
public class QueryLoadConfigBean extends PagingLoadConfigBean implements QueryLoadConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2882594152365816146L;

	private String query;

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public void setQuery(String query) {
		this.query = query;
	}

}
