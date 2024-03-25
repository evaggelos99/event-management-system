package org.com.ems.api.dao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * TODO can we transform this to record?
 * 
 * @author Evangelos Georgiou
 *
 */
public class SeatingInformation extends AbstractDAO {

	private static final long serialVersionUID = 1635646193965736631L;

	String seat;

	String section;

	protected SeatingInformation() {

	}

	public SeatingInformation(String seat, String section) {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE).append(seat).append(section).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(seat).append(section).build();
	}

	@Override
	public boolean equals(Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (getClass() != object.getClass()) {
			return false;
		}

		var rhs = (SeatingInformation) object;

		return new EqualsBuilder().append(this.seat, rhs.seat).append(this.section, rhs.section).build();
	}

	public String getSeat() {
		return seat;
	}

	public String getSection() {
		return section;
	}

}
