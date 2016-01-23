package com.iut.gl.entity;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.iut.gl.entity.Management;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Appointment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;

	@Column
	private String title;

	@Column
	private String works;

	@Column
	private String results;

	@Column
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	@Temporal(TemporalType.DATE)
	private Date updatedAt;

	@ManyToOne
	private Management management;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Appointment)) {
			return false;
		}
		Appointment other = (Appointment) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWorks() {
		return works;
	}

	public void setWorks(String works) {
		this.works = works;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (title != null && !title.trim().isEmpty())
			result += "title: " + title;
		if (works != null && !works.trim().isEmpty())
			result += ", work: " + works;
		if (results != null && !results.trim().isEmpty())
			result += ", results: " + results;
		return result;
	}

	public Management getManagement() {
		return this.management;
	}

	public void setManagement(final Management management) {
		this.management = management;
	}
}