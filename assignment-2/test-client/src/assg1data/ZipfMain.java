package assg1data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import assg1data.ZipfGenerator;


public class ZipfMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random rand = new Random();
		long COUNT = 10000;
		
        try {
        	PrintWriter out = new PrintWriter("test.txt");
            for (long i = 0; i < COUNT; i++) {
            	double u = rand.nextDouble();
        		ZipfGenerator zg = new ZipfGenerator(COUNT, 0.8);
        		long rank = zg.calculateZipf(u);
                out.println(rank);
            }
            out.close();
        } catch (IOException e) {}
	}

}
