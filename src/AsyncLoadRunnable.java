package com.withtrip.android.util;

import android.os.Handler;

public class AsyncLoadRunnable implements Runnable {

	private final AsyncHandlerInterface responseHandler;
	private  boolean  isCancelled = false;
	private boolean cancelIsNotified = false;
	private boolean isFinished = false;
	private Handler mHandler=new Handler();
	public AsyncLoadRunnable(AsyncHandlerInterface responseHandler) {
		this.responseHandler = responseHandler;
	}

	@Override
	public void run() {
		
		
		if (isCancelled()) {
			return;
		}

		responseHandler.loadingData();
		
		if (!isCancelled() && responseHandler != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					 responseHandler.sendFinishMessage();
				}
			});
           
        }

		isFinished = true;
	}

	public synchronized boolean isCancelled() {
		if (isCancelled) {
			sendCancelNotification();
		}
		return isCancelled;
	}

	private synchronized void sendCancelNotification() {
		System.out.println("sendCancelNotification");
		if (!isFinished && isCancelled && !cancelIsNotified) {
			cancelIsNotified = true;
			if (responseHandler != null)
				responseHandler.sendCancelMessage();
		}
	}

	public boolean isDone() {
		return isCancelled() || isFinished;
	}

	public synchronized boolean cancel(boolean mayInterruptIfRunning) {
		isCancelled = true;
		System.out.println("cancel");
		return isCancelled();
	}

	

	public interface AsyncHandlerInterface {
		
		/**
		 * 异步加载耗时动作
		 */
		void loadingData();
		/**
		 * 完成调用
		 */
		void sendFinishMessage();
		/**
		 * 取消调用
		 */
		void sendCancelMessage();

	}

}
