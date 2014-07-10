package de.kickerapp.client.properties;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.kickerapp.client.ui.resources.IconProvider;
import de.kickerapp.shared.common.MatchType;
import de.kickerapp.shared.dto.MatchDto;
import de.kickerapp.shared.dto.PlayerDto;
import de.kickerapp.shared.dto.TeamDto;

public interface MatchProperty extends PropertyAccess<MatchDto> {

	@Path("id")
	public ModelKeyProvider<MatchDto> id();

	public ValueProvider<MatchDto, Integer> matchNumber();

	public ValueProvider<MatchDto, Date> matchDate();

	public ValueProvider<MatchDto, String> groupDate();

	public ValueProvider<MatchDto, String> matchType = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			String matchType = "";
			if (object.getMatchType() == MatchType.SINGLE) {
				matchType = "Einzel";
			} else {
				matchType = "Doppel";
			}
			return matchType;
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "matchType";
		}
	};

	public ValueProvider<MatchDto, String> team1 = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			return getTeam(object, object.getTeam1Dto());
		}

		private String getTeam(MatchDto object, TeamDto teamDto) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDto player1 = teamDto.getPlayer1();
			sb.append(player1.getLastName()).append(", ");
			sb.append(player1.getFirstName());

			if (object.getMatchType() == MatchType.DOUBLE) {
				sb.append(" | ");

				final PlayerDto player2 = teamDto.getPlayer2();
				sb.append(player2.getLastName()).append(", ");
				sb.append(player2.getFirstName());
			}
			return sb.toString();
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "team1";
		}
	};

	public ValueProvider<MatchDto, String> team2 = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			return getTeam(object, object.getTeam2Dto());
		}

		private String getTeam(MatchDto object, TeamDto teamDto) {
			final StringBuilder sb = new StringBuilder();

			final PlayerDto player1 = teamDto.getPlayer1();
			sb.append(player1.getLastName()).append(", ");
			sb.append(player1.getFirstName());

			if (object.getMatchType() == MatchType.DOUBLE) {
				sb.append(" | ");

				final PlayerDto player2 = teamDto.getPlayer2();
				sb.append(player2.getLastName()).append(", ");
				sb.append(player2.getFirstName());
			}
			return sb.toString();
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "team2";
		}
	};

	public ValueProvider<MatchDto, String> matchResult = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			final StringBuilder sb = new StringBuilder();

			int setWinsTeam1 = 0;
			for (Integer set : object.getMatchSetsDto().getMatchSetsTeam1()) {
				if (set == 6) {
					setWinsTeam1++;
				}
			}
			sb.append(Integer.toString(setWinsTeam1));
			sb.append(":");

			int setWinsTeam2 = 0;
			for (Integer set : object.getMatchSetsDto().getMatchSetsTeam2()) {
				if (set == 6) {
					setWinsTeam2++;
				}
			}
			sb.append(Integer.toString(setWinsTeam2));

			return sb.toString();
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "matchResult";
		}
	};

	public ValueProvider<MatchDto, String> matchSets = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			final StringBuilder sb = new StringBuilder();

			final int size = object.getMatchSetsDto().getMatchSetsTeam1().size();
			for (int i = 0; i < size; i++) {
				final Integer setTeam1 = object.getMatchSetsDto().getMatchSetsTeam1().get(i);
				final Integer setTeam2 = object.getMatchSetsDto().getMatchSetsTeam2().get(i);
				if (setTeam1 != null && setTeam2 != null) {
					sb.append(setTeam1);
					sb.append(":");
					sb.append(setTeam2);

					if (i < size - 1) {
						final Integer nextSetTeam1 = object.getMatchSetsDto().getMatchSetsTeam1().get(i + 1);
						final Integer nextSetTeam2 = object.getMatchSetsDto().getMatchSetsTeam2().get(i + 1);
						if (nextSetTeam1 != null && nextSetTeam2 != null) {
							sb.append(", ");
						}
					}
				}
			}
			return sb.toString();
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "matchSets";
		}
	};

	public ValueProvider<MatchDto, String> matchPointsTeam1 = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			final StringBuilder sb = new StringBuilder();

			final ArrayList<Integer> pointsTeam1 = object.getMatchPointsDto().getMatchPointsTeam1();

			Integer point = pointsTeam1.get(0);
			appendPoint(sb, point);
			if (object.getMatchType() == MatchType.DOUBLE) {
				sb.append(" | ");

				point = pointsTeam1.get(1);
				appendPoint(sb, point);
				sb.append(" (");
				point = pointsTeam1.get(2);
				appendPoint(sb, point);
				sb.append(")");
			}
			return sb.toString();
		}

		private void appendPoint(final StringBuilder sb, Integer point) {
			if (point >= 0) {
				sb.append("+" + point);
			} else {
				sb.append(point);
			}
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "matchPointsTeam1";
		}
	};

	public ValueProvider<MatchDto, String> matchPointsTeam2 = new ValueProvider<MatchDto, String>() {
		@Override
		public String getValue(MatchDto object) {
			final StringBuilder sb = new StringBuilder();

			final ArrayList<Integer> pointsTeam2 = object.getMatchPointsDto().getMatchPointsTeam2();

			Integer point = pointsTeam2.get(0);
			appendPoint(sb, point);
			if (object.getMatchType() == MatchType.DOUBLE) {
				sb.append(" | ");

				point = pointsTeam2.get(1);
				appendPoint(sb, point);
				sb.append(" (");
				point = pointsTeam2.get(2);
				appendPoint(sb, point);
				sb.append(")");
			}
			return sb.toString();
		}

		private void appendPoint(final StringBuilder sb, Integer point) {
			if (point >= 0) {
				sb.append("+" + point);
			} else {
				sb.append(point);
			}
		}

		@Override
		public void setValue(MatchDto object, String value) {
		}

		@Override
		public String getPath() {
			return "matchPointsTeam2";
		}
	};

	public ValueProvider<MatchDto, ImageResource> matchComment = new ValueProvider<MatchDto, ImageResource>() {
		@Override
		public ImageResource getValue(MatchDto object) {
			return IconProvider.get().comments10();
		}

		@Override
		public void setValue(MatchDto object, ImageResource value) {
		}

		@Override
		public String getPath() {
			return "matchComment";
		}
	};

}
