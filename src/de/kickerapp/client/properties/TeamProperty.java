package de.kickerapp.client.properties;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.common.Tendency;
import de.kickerapp.shared.dto.ITeam;
import de.kickerapp.shared.dto.PlayerDto;

public interface TeamProperty extends PropertyAccess<ITeam> {

	@Path("id")
	public ModelKeyProvider<ITeam> id();

	public ValueProvider<ITeam, String> teamLabel = new ValueProvider<ITeam, String>() {
		@Override
		public String getValue(ITeam object) {
			final StringBuilder builder = new StringBuilder();

			final PlayerDto player1 = object.getPlayer1();
			final PlayerDto player2 = object.getPlayer2();

			builder.append(player1.getLastName()).append(", ");
			builder.append(player1.getFirstName());
			builder.append(" | ");
			builder.append(player2.getLastName()).append(", ");
			builder.append(player2.getFirstName());

			return builder.toString();
		}

		@Override
		public void setValue(ITeam object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<ITeam, PlayerDto> player1();

	public ValueProvider<ITeam, PlayerDto> player2();

	public ValueProvider<ITeam, Date> lastMatchDate();

	public ValueProvider<ITeam, Integer> matches = new ValueProvider<ITeam, Integer>() {
		@Override
		public Integer getValue(ITeam object) {
			return object.getWins() + object.getLosses();
		}

		@Override
		public void setValue(ITeam object, Integer value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<ITeam, Integer> wins();

	public ValueProvider<ITeam, Integer> losses();

	public ValueProvider<ITeam, Integer> shotGoals();

	public ValueProvider<ITeam, Integer> getGoals();

	public ValueProvider<ITeam, String> goals = new ValueProvider<ITeam, String>() {
		@Override
		public String getValue(ITeam object) {
			final StringBuilder sb = new StringBuilder();

			sb.append(object.getShotGoals());
			sb.append(":");
			sb.append(object.getGetGoals());

			return sb.toString();
		}

		@Override
		public void setValue(ITeam object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<ITeam, String> goalDifference = new ValueProvider<ITeam, String>() {
		@Override
		public String getValue(ITeam object) {
			final StringBuilder sb = new StringBuilder();

			final int goalDifference = object.getShotGoals() - object.getGetGoals();
			if (goalDifference >= 0) {
				sb.append("+" + Integer.toString(goalDifference));
			} else {
				sb.append(Integer.toString(goalDifference));
			}
			return sb.toString();
		}

		@Override
		public void setValue(ITeam object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<ITeam, Integer> prevTablePlace();

	public ValueProvider<ITeam, Integer> curTablePlace();

	public ValueProvider<ITeam, Integer> points();

	public ValueProvider<ITeam, Tendency> tendency();

}
