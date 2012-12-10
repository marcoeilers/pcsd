package dk.diku.pcsd.assignment1.impl;

import java.io.Serializable;

/**
 * Specifies an area of memory in the store.
 * Contains an offset and a length.
 */
public class SpaceIdent implements Comparable<SpaceIdent>, Serializable {
	private long pos;

	private int length;

	private long next;

	public long getPos() {
		return pos;
	}

	public int getLength() {
		return length;
	}

	public long getNext() {
		return next;
	}

	public void setLength(int length) {
		this.length = length;
		this.next = pos + length;
	}

	public SpaceIdent(long pos, int length) {
		this.pos = pos;
		this.length = length;
		this.next = pos + length;
	}

	@Override
	public int compareTo(SpaceIdent other) {
		long otherP = other.getPos();
		if (pos < otherP)
			return -1;
		else if (pos == otherP)
			return 0;
		else
			return 1;

	}
}
