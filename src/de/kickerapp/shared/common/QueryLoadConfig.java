package de.kickerapp.shared.common;

import com.sencha.gxt.data.shared.loader.PagingLoadConfig;

public interface QueryLoadConfig extends PagingLoadConfig {

	String getQuery();

	void setQuery(String query);
}