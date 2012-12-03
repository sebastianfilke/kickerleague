package de.kickerapp.shared.match;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import de.kickerapp.shared.common.BaseEntity;

/**
 * Datenklasse zum Halten der Informationen für ein Spiel.
 * 
 * @author Sebastian Filke
 */
@PersistenceCapable
public class Match extends BaseEntity implements MatchData {

	private int id;

	private String name;

	/** Konstante für die SerialVersionUID. */
	private static final long serialVersionUID = -77744879325097514L;

	@Persistent
	private String set1;
	@Persistent
	private String player1;
	@Persistent
	private String player2;

	public Match() {
	}

	public String getSet1() {
		return set1;
	}

	public void setSet1(String set1) {
		this.set1 = set1;
	}

	public String getPlayer1() {
		return player1;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
