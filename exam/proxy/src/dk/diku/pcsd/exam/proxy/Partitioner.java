package dk.diku.pcsd.exam.proxy;

import java.util.List;

import dk.diku.pcsd.exam.common.exceptions.InexistentBranchException;
import dk.diku.pcsd.exam.partition.AccountManagerPartitionImpl;

/**
 * A partitioner assigns a branchId to a partition based on some algorithm.
 * 
 * This interface that must be implemented by all partitioners. Only current
 * implementation is HashModPartitioner.
 * 
 * @author marco
 * 
 */
public interface Partitioner {

	/**
	 * Returns the web service stub of the partition for the specified branchId.
	 * 
	 * @param branchId
	 *            the ID of the branch
	 * @param partitions
	 *            a list of all partition services
	 * @return the partition service for this branch
	 * @throws InexistentBranchException
	 *             if the branch can be noted to be invalid, or if the list of
	 *             partitions is empty
	 */
	public AccountManagerPartitionImpl getPartition(int branchId,
			List<AccountManagerPartitionImpl> partitions)
			throws InexistentBranchException;

	/**
	 * Called when one partitions is detected to be unavailable.
	 * @param branchId the branch that is no longer available
	 * @param partitions a list of all partitions services
	 */
	public void setUnavailable(int branchId,
			List<AccountManagerPartitionImpl> partitions);
}
