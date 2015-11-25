package com.Beendo.Utils;

public interface ICache<T> {

	public Integer getCacheId();
	public T getObject(Integer id);
}
