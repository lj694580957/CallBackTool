package com.withtrip.android.util;


/**
 * 异步加载工具
 * @author daohen
 *
 */
public class AsyncLoaderUtil {
	
	private static AsyncLoad asynloader = new AsyncLoad(); 
	
	public static AsyncLoad getAsynLoader() {
		if(asynloader==null)
			asynloader = new AsyncLoad();
		
		return asynloader;
	}
}
