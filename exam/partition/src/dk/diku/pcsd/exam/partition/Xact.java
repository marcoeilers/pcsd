package dk.diku.pcsd.exam.partition;

import java.util.HashSet;
import java.util.Set;

/**
 * Transaction object for optimistic concurrency control.
 * Keeps track of the start and end of the relevant phases for this transaction.
 * Contains a future to signal when the validation thread is done.
 * @author marco
 *
 */
public class Xact implements Comparable<Xact> {
	// MAX_VALUE signals an event that lies in the future
	private long start = Long.MAX_VALUE;
	private long endRead = Long.MAX_VALUE;
	private long endWrite = Long.MAX_VALUE;

	private Set<Integer> readSet;
	private Set<Integer> writeSet;
	private Set<Account> changes = new HashSet<Account>();
	private FutureResult valid = null;

	/**
	 * Creates a Xact that has exactly one account in both its read and write set
	 */
	public Xact(int account) {
		// set the current time as start time
		start = System.nanoTime();
		readSet = new HashSet<Integer>();
		readSet.add(account);
		writeSet = new HashSet<Integer>();
		writeSet.add(account);
	}

	/**
	 * Creates an Xact with the given read and write sets
	 */
	public Xact(Set<Integer> read, Set<Integer> write) {
		start = System.nanoTime();
		readSet = read;
		writeSet = write;
	}

	public long getStart() {
		return start;
	}

	public long getEndRead() {
		return endRead;
	}

	public void setEndRead(long endRead) {
		this.endRead = endRead;
	}

	public long getEndWrite() {
		return endWrite;
	}

	public void setEndWrite(long endWrite) {
		this.endWrite = endWrite;
	}

	public void addChange(Account a) {
		changes.add(a);
	}

	public Set<Integer> getReadSet() {
		return readSet;
	}

	public void setReadSet(Set<Integer> readSet) {
		this.readSet = readSet;
	}

	public Set<Integer> getWriteSet() {
		return writeSet;
	}

	public void setWriteSet(Set<Integer> writeSet) {
		this.writeSet = writeSet;
	}

	public Set<Account> getChanges() {
		return changes;
	}

	@Override
	public int compareTo(Xact arg0) {
		return (int) (start - arg0.start);
	}

	public FutureResult getValid() {
		return valid;
	}

	public void setValid(FutureResult valid) {
		this.valid = valid;
	}

}
