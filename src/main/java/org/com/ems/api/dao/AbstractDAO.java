package org.com.ems.api.dao;

import java.io.Serializable;

public abstract class AbstractDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public abstract String toString();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object object);
}
