package de.kickerapp;

import org.junit.Test;

import de.kickerapp.server.entity.Stats;

public class TestCase {

	private final int[] K_FAKTOR = new int[] { 1, 34, 31, 28, 25, 22, 19, 16, 13, 10 };

	@Test
	public void testPointCalculation() {
		final Stats db1Stats = new Stats();
		db1Stats.setPoints(2223);
		final Stats db2Stats = new Stats();
		db2Stats.setPoints(2120);

		for (int i = 0; i < 300; i++) {
			System.out.println("Spiel " + (i + 1));

			final int points1 = calculateEloPoints(true, db1Stats, db2Stats);
			final int points2 = calculateEloPoints(false, db2Stats, db1Stats);

			db1Stats.setPoints(db1Stats.getPoints() + points1);
			db1Stats.setWins(db1Stats.getWins() + 1);
			db2Stats.setPoints(db2Stats.getPoints() + points2);
			db2Stats.setDefeats(db2Stats.getDefeats() + 1);

			System.out.println("Elo-Punkte Spieler 1: " + points1);
			System.out.println("Elo-Punkte Spieler 1 gesamt: " + db1Stats.getPoints());
			System.out.println("Elo-Punkte Spieler 2: " + points2);
			System.out.println("Elo-Punkte Spieler 2 gesamt: " + db2Stats.getPoints());
		}
		for (int i = 0; i < 300; i++) {
			System.out.println("Spiel " + (i + 1));

			final int points1 = calculateEloPoints(false, db1Stats, db2Stats);
			final int points2 = calculateEloPoints(true, db2Stats, db1Stats);

			db1Stats.setPoints(db1Stats.getPoints() + points1);
			db1Stats.setWins(db1Stats.getWins() + 1);
			db2Stats.setPoints(db2Stats.getPoints() + points2);
			db2Stats.setDefeats(db2Stats.getDefeats() + 1);

			System.out.println("Elo-Punkte Spieler 1: " + points1);
			System.out.println("Elo-Punkte Spieler 1 gesamt: " + db1Stats.getPoints());
			System.out.println("Elo-Punkte Spieler 2: " + points2);
			System.out.println("Elo-Punkte Spieler 2 gesamt: " + db2Stats.getPoints());
		}
	}

	private int calculateEloPoints(boolean winner, Stats db1Stats, Stats db2Stats) {
		final int points1 = db1Stats.getPoints();
		final int points2 = db2Stats.getPoints();

		final int kFaktor = getKFaktor(points1);
		final int score = getScore(winner);
		final double expectancy = calculateExpectancy(points1, points2);

		final double points = kFaktor * (score - expectancy);
		final int roundedPoints = (int) Math.round(points);

		return roundedPoints;
	}

	private int getKFaktor(int points1) {
		int k = 0;
		if (points1 < 10) {
			k = K_FAKTOR[0];
		} else if (points1 < 1000) {
			k = K_FAKTOR[1];
		} else if (points1 >= 1000 && points1 < 1300) {
			k = K_FAKTOR[2];
		} else if (points1 >= 1300 && points1 < 1600) {
			k = K_FAKTOR[3];
		} else if (points1 >= 1600 && points1 < 1900) {
			k = K_FAKTOR[4];
		} else if (points1 >= 1900 && points1 < 2100) {
			k = K_FAKTOR[5];
		} else if (points1 >= 2100 && points1 < 2300) {
			k = K_FAKTOR[6];
		} else if (points1 >= 2300 && points1 < 2500) {
			k = K_FAKTOR[7];
		} else if (points1 >= 2500 && points1 < 2700) {
			k = K_FAKTOR[8];
		} else if (points1 >= 2700) {
			k = K_FAKTOR[9];
		}
		return k;
	}

	private int getScore(boolean winner) {
		int score = 0;
		if (winner) {
			score = 1;
		}
		return score;
	}

	private double calculateExpectancy(int points1, int points2) {
		return 1 / (1 + Math.pow(10, calculateExponent(points1, points2)));
	}

	private double calculateExponent(int points1, int points2) {
		double exponent = 0;
		if (points2 - points1 > 400) {
			exponent = 1;
		} else if (points2 - points1 < -400) {
			exponent = -1;
		} else {
			exponent = (double) (points2 - points1) / 400;
		}
		return exponent;
	}

}
