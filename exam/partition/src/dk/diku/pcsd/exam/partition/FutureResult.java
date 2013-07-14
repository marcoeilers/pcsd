package dk.diku.pcsd.exam.partition;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * Implementation of a future containing a boolean.
 * Mostly copied from the skeleton classes of assignment 2.
 * Used by OptimisticBranch.
 */
public class FutureResult implements Future<Boolean> {
		
		private String lock;
		private Boolean ret;
		private boolean done = false;
		
		public FutureResult(long id){
			// create individual lock object for each transaction
			lock = "" + id;
		}

		@Override
		public Boolean get(long arg0, TimeUnit arg1) throws InterruptedException,
				ExecutionException, TimeoutException {
			
			synchronized(this.lock) {
				if (!this.done)
					this.lock.wait(arg1.toMillis(arg0));
			}
			return ret;
		}

		@Override
		public Boolean get() throws InterruptedException, ExecutionException {
			synchronized(this.lock) {
				if (!this.done)
					this.lock.wait();
			}
			return ret;
		}
		
		public void signalAll(Boolean ret) {
			synchronized(this.lock) {
				this.ret = ret;
				this.done = true;
				this.lock.notifyAll();
			}
		}

		@Override
		public boolean cancel(boolean arg0) throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return this.done;
		}


	
}
