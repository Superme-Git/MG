package com.locojoy.pager;

public interface Filter<T> {
	public boolean accept(T t);
}
