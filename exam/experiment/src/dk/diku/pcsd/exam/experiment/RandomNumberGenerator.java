package dk.diku.pcsd.exam.experiment;

import java.util.Random;

/**
 * Gets random numbers from either a zipf or a self similar distribution.
 * Algorithms taken from Cooper et al, http://research.yahoo.com/node/3202
 * 
 * @author marco
 * 
 */
public class RandomNumberGenerator {
	Random rnd = new Random();

	/**
	 * 
	 * @param N the number of objects to choose from
	 * @param h the skew
	 * @return the index of the chosen object
	 */
	public int selfSimilar(int N, double h) {
		int result = (1 + (int) (N * Math.pow(rnd.nextDouble(), Math.log(h)
				/ Math.log(1.0 - h))));
		return result;
	}

	public int zipf(int N, double theta) {
		double alpha = 1 / (1 - theta);
		double zetan = zeta(N, theta);
		double eta = (1 - Math.pow(2.0 / N, 1 - theta))
				/ (1 - this.zeta(2, theta) / zetan);

		double u = rnd.nextDouble();
		double uz = u * zetan;

		if (uz < 1)
			return 1;
		if (uz < 1 + Math.pow(0.5, theta))
			return 2;
		return (int) (1.0 + (N * Math.pow(eta * u - eta + 1, alpha)));
	}

	public double zeta(int N, double theta) {
		double zetan = 0;
		for (long i = 1; i < N; i++) {
			zetan += (1 / Math.pow(i, theta));
		}
		return zetan;
	}
}
