package com.withtrip.android.util;


/**
 * �첽���ع���
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
