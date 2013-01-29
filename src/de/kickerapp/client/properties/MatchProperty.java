package de.kickerapp.client.properties;

import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.shared.dto.IMatch;
import de.kickerapp.shared.dto.PlayerDto;

public interface MatchProperty extends PropertyAccess<IMatch> {

	@Path("id")
	public ModelKeyProvider<IMatch> id();

	public ValueProvider<IMatch, String> matchNumber();

	public ValueProvider<IMatch, Date> matchDate();

	public ValueProvider<IMatch, String> team1 = new ValueProvider<IMatch, String>() {
		@Override
		public String getValue(IMatch object) {
			final StringBuilder builder = new StringBuilder();

			final PlayerDto player1 = object.getTeam1().getPlayer1();
			builder.append(player1.getLastName()).append(" ");
			builder.append(player1.getFirstName());

			return builder.toString();
		}

		@Override
		public void setValue(IMatch object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IMatch, String> team2 = new ValueProvider<IMatch, String>() {
		@Override
		public String getValue(IMatch object) {
			final StringBuilder builder = new StringBuilder();

			final PlayerDto player1 = object.getTeam2().getPlayer1();
			builder.append(player1.getLastName()).append(" ");
			builder.append(player1.getFirstName());

			return builder.toString();
		}

		@Override
		public void setValue(IMatch object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IMatch, String> matchResult = new ValueProvider<IMatch, String>() {
		@Override
		public String getValue(IMatch object) {
			final StringBuilder builder = new StringBuilder();

			int setWinsTeam1 = 0;
			for (Integer set : object.getSets().getSetsTeam1()) {
				if (set == 6) {
					setWinsTeam1++;
				}
			}
			builder.append(Integer.toString(setWinsTeam1));
			builder.append(":");

			int setWinsTeam2 = 0;
			for (Integer set : object.getSets().getSetsTeam2()) {
				if (set == 6) {
					setWinsTeam2++;
				}
			}
			builder.append(Integer.toString(setWinsTeam2));

			return builder.toString();
		}

		@Override
		public void setValue(IMatch object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

	public ValueProvider<IMatch, String> matchSets = new ValueProvider<IMatch, String>() {
		@Override
		public String getValue(IMatch object) {
			final StringBuilder builder = new StringBuilder();

			int setWinsTeam1 = 0;
			for (Integer set : object.getSets().getSetsTeam1()) {
				if (set == 6) {
					setWinsTeam1++;
				}
			}

			int size = 0;
			if (setWinsTeam1 == 2 || setWinsTeam1 == 0) {
				size = 2;
			} else {
				size = 3;
			}
			for (int i = 0; i < size; i++) {
				final Integer setTeam1 = object.getSets().getSetsTeam1().get(i);
				final Integer setTeam2 = object.getSets().getSetsTeam2().get(i);
				if (setTeam1 != null && setTeam2 != null) {
					builder.append(setTeam1);
					builder.append(":");
					builder.append(setTeam2);

					if (i < size - 1) {
						final Integer nextSetTeam1 = object.getSets().getSetsTeam1().get(i + 1);
						final Integer nextSetTeam2 = object.getSets().getSetsTeam2().get(i + 1);
						if (nextSetTeam1 != null && nextSetTeam2 != null) {
							builder.append(", ");
						}
					}
				}
			}
			return builder.toString();
		}

		@Override
		public void setValue(IMatch object, String value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	};

}
