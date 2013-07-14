package dk.diku.pcsd.exam.proxy;

import java.util.List;

import dk.diku.pcsd.exam.common.exceptions.InexistentBranchException;
import dk.diku.pcsd.exam.partition.AccountManagerPartitionImpl;

/**
 * Assigns branches to partitions by taking the branchId modulo the number of
 * partitions and returning the partition with the resulting index.
 * 
 * @author marco
 * 
 */
public class HashModPartitioner implements Partitioner {

	@Override
	public AccountManagerPartitionImpl getPartition(int branchId,
			List<AccountManagerPartitionImpl> partitions)
			throws InexistentBranchException {
		if (partitions.size() <= 0)
			throw new InexistentBranchException("No partitions available",
					branchId);
		// hash by taking modulo number of partitions
		AccountManagerPartitionImpl result = partitions.get(branchId
				% partitions.size());
		
		// if the partition has not yet been detected to be unavailable
		if (result != null)
			return result;
		else
			throw new InexistentBranchException("Branch " + branchId
					+ " cannot be reached.", branchId);
	}

	@Override
	public void setUnavailable(int branchId,
			List<AccountManagerPartitionImpl> partitions) {
		// null indicates that the service is not reachable
		partitions.set(branchId % partitions.size(), null);
	}

}
