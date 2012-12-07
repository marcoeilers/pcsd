package data;

public class ZipfGenerator {
	
	private long N;
	private double theta;

	public ZipfGenerator(long N, double theta) {
	 	this.N = N;
	 	this.theta = theta;
	 }
		
	//calculate as in paper
	public long calculateZipf(double u){
		double alpha = 1 / (1 - this.theta);
		double zetan = zeta(N, this.theta);
		double eta = (1 - Math.pow(2.0 / N, 1 - theta)) / (1 - this.zeta(2, theta) / zetan);
		
		double uz = u * zetan;
	  
		if (uz < 1) return 1;
		if (uz < 1 + Math.pow(0.5, theta)) return 2;
		return 1 + (long)(N * Math.pow(eta*u - eta + 1, alpha));
	}
	
	public double zeta(long N, double theta){
	  	double zetan = 0;
		for(long i=1;i < N; i++) {
	  		zetan += (1/Math.pow(i, theta));
	  	}
		return zetan;
	}
}
