package dk.diku.pcsd.exam.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads an init file and turns it into a map of branch ids to lists, which
 * contain the branche's account IDs. With getBranches(), the init file is
 * expected to be account.init in user.home, otherwise use getBranches(String)
 * 
 * @author marco
 * 
 */
public class InitDataReader {
	private String initFile;
	private Map<Integer, List<Integer>> branches;

	public InitDataReader() {
		branches = new HashMap<Integer, List<Integer>>();
	}

	public Map<Integer, List<Integer>> getBranches() {
		return getBranches("account.init");
	}

	public Map<Integer, List<Integer>> getBranches(String fileName) {

		String home = System.getProperty("user.home");
		if (!home.endsWith(File.separator))
			home += File.separator;
		initFile = home + fileName;

		try {
			BufferedReader initReader = new BufferedReader(new FileReader(
					initFile));
			String next = initReader.readLine();

			int branch = -1;
			List<Integer> toBeAdded = null;

			while (next != null) {
				if (!next.startsWith("%")) {
					String[] separated = next.split("\t", 2);

					if (separated.length == 2) {
						switch (separated[0]) {
						case "branch":
							try {
								if (branch != -1)
									branches.put(branch, toBeAdded);
								branch = Integer.parseInt(separated[1]);
								toBeAdded = new ArrayList<Integer>();
							} catch (NumberFormatException e) {
							}
							break;
						default:
							if (branch != -1) {
								try {
									int accountId = Integer
											.parseInt(separated[0]);
									// parse balance as well, only add it if
									// balance is valid
									Double.parseDouble(separated[1]);
									toBeAdded.add(accountId);
								} catch (NumberFormatException e) {
								}
							}
							break;
						}
					}
				}
				next = initReader.readLine();
			}
			if (branch != -1)
				branches.put(branch, toBeAdded);

			initReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return branches;
	}
}
