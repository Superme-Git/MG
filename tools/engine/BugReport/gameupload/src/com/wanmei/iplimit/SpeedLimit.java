package com.locojoy.iplimit;

import java.util.*;

/**
 * 
 * @author zengpan
 *
 * @param <K>
 */
public class SpeedLimit<K>
{
	private class Limit
	{
		private int HIGH;
		private int WINDOW;

		private class Record
		{
			K key;
			int count = 1;
			int deadline = WINDOW + (int) ( System.currentTimeMillis() / 1000 );
			Record( K key ) { this.key = key; }
			boolean isTimeout(int deadline) { return this.deadline <= deadline; }
			boolean update() { return count++ < HIGH; }
			boolean isLimit() { return count < HIGH; }
		}

		public Limit( int WINDOW, int HIGH )
		{
			this.HIGH   = HIGH;
			this.WINDOW = WINDOW;
		}

		private final PriorityQueue<Record> rq = new PriorityQueue<Record>( 256, new Comparator<Record>()
			{
				public int compare( Record r1, Record r2 ) { return r1.deadline - r2.deadline; }
			}
		);

		private final HashMap<K, Record> rm = new HashMap<K, Record>();

		public synchronized boolean add( K key )
		{
			int now = (int) ( System.currentTimeMillis() / 1000 );
			Record r;
			for ( ; ( r = rq.peek() ) != null && r.isTimeout(now); rq.poll() )
				rm.remove( r.key );
			r = rm.get( key );
			if ( r != null )
				return r.update();
			r = new Record( key );
			rq.add( r );
			rm.put( key, r );
			return true;
		}
		
		public synchronized boolean ask( K key )
		{
			int now = (int) ( System.currentTimeMillis() / 1000 );
			Record r;
			for ( ; ( r = rq.peek() ) != null && r.isTimeout(now); rq.poll() )
				rm.remove( r.key );
			r = rm.get( key );
			if ( r != null )
				return r.isLimit();
			/*
			r = new Record( key );
			rq.add( r );
			rm.put( key, r );
			*/
			return true;
		}
	}

	private List<Limit> limit_list = new ArrayList<Limit>();

	public void config( int WINDOW, int HIGH ) { limit_list.add( new Limit(WINDOW, HIGH) ); }

	public boolean add( K key )
	{
		boolean r = true;
		for ( Limit limit : limit_list ) r = r && limit.add(key);
		return r;
	}
	
	public boolean ask( K key )
	{
		boolean r = true;
		for ( Limit limit : limit_list ) r = r && limit.ask(key);
		return r;
	}

	/**
	 * 2秒窗口允许3个并且5秒窗口允许5个
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception
	{
		SpeedLimit<Integer> sl = new SpeedLimit<Integer>();
		sl.config( 2, 3 );
		sl.config( 5, 5 );

		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );

		Thread.sleep(3000);
		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );
		System.out.println( sl.add( 1 ) );
	}
}
