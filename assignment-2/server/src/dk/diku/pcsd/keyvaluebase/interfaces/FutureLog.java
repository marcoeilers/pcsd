package dk.diku.pcsd.keyvaluebase.interfaces;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureLog<T> implements Future<T> {
	
	private String lock = "";
	private T ret;
	private boolean done = false;

	@Override
	public T get(long arg0, TimeUnit arg1) throws InterruptedException,
			ExecutionException, TimeoutException {
		
		synchronized(this.lock) {
			if (!this.done)
				this.lock.wait(arg1.toMillis(arg0));
		}
		return ret;
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		synchronized(this.lock) {
			if (!this.done)
				this.lock.wait();
		}
		return ret;
	}
	
	public void signalAll(T ret) {
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
