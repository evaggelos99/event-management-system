package org.com.ems.api.dao;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public final class Organizer extends AbstractDAO {

	private static final long serialVersionUID = -43707763031653832L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID UUID;

	protected Organizer() {

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

}
