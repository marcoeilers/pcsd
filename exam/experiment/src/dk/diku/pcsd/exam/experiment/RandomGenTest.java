package dk.diku.pcsd.exam.experiment;

/**
 * Class for testing if RandomNumberGenerator generated numbers according to my
 * wishes.
 * 
 * @author marco
 * 
 */
public class RandomGenTest {

	public static void main(String[] args) {
		RandomNumberGenerator gen = new RandomNumberGenerator();

		int slots = 460;
		int draws = 10000;

		int[] hits = new int[slots];

		for (int i = 0; i < draws; i++) {
			hits[gen.selfSimilar(slots, 0.2) - 1]++;
		}

		for (int i = 0; i < slots; i++) {
			System.out.print(hits[i] + ",");
		}

	}

}
