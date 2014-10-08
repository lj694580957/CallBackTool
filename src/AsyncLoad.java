package com.withtrip.android.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Looper;

public class AsyncLoad {
	private final Map<Context, List<AsyncHandle>> requestMap;
	private ExecutorService threadPool;
	
	public AsyncLoad() {
		threadPool = Executors.newCachedThreadPool();
		requestMap = new WeakHashMap<Context, List<AsyncHandle>>();

	}

	public AsyncHandle submitTask(Context context, AsyncLoadRunnable run) {
		threadPool.submit(run);
		
		AsyncHandle requestHandle = new AsyncHandle(run);
		if (context != null) {
			List<AsyncHandle> requestList = requestMap.get(context);
			if (requestList == null) {
				requestList = new LinkedList();
				requestMap.put(context, requestList);
			}

			requestList.add(requestHandle);

			Iterator<AsyncHandle> iterator = requestList.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().shouldBeGarbageCollected()) {
					iterator.remove();
				}
			}
		}

		return requestHandle;
	}

	public void cancelRequests(final Context context,
			final boolean mayInterruptIfRunning) {
		if (context == null) {
			return;
		}
		Runnable r = new Runnable() {
			@Override
			public void run() {
				List<AsyncHandle> requestList = requestMap.get(context);
				if (requestList != null) {
					for (AsyncHandle requestHandle : requestList) {
						requestHandle.cancel(mayInterruptIfRunning);
					}
					requestMap.remove(context);
				}
			}
		};
		if (Looper.myLooper() == Looper.getMainLooper()) {
			new Thread(r).start();
		} else {
			r.run();
		}
	}

	public void cancelAllRequests(boolean mayInterruptIfRunning) {

		for (List<AsyncHandle> requestList : requestMap.values()) {
			if (requestList != null) {
				for (AsyncHandle requestHandle : requestList) {
					requestHandle.cancel(mayInterruptIfRunning);
				}
			}
		}
		requestMap.clear();
	}

	public class AsyncHandle {
		private final WeakReference<AsyncLoadRunnable> request;

		public AsyncHandle(AsyncLoadRunnable request) {
			this.request = new WeakReference(request);
		}

		public boolean cancel(boolean mayInterruptIfRunning) {
			AsyncLoadRunnable _request = request.get();
			
			return _request == null || _request.cancel(mayInterruptIfRunning);
		}

		public boolean isFinished() {
			AsyncLoadRunnable _request = request.get();
			return _request == null || _request.isDone();
		}

		public boolean isCancelled() {
			AsyncLoadRunnable _request = request.get();
			return _request == null || _request.isCancelled();
		}

		public boolean shouldBeGarbageCollected() {
			boolean should = isCancelled() || isFinished();
			if (should)
				request.clear();
			return should;
		}
	}

}
