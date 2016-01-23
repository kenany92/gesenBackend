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
import com.iut.gl.entity.Student;
import javax.persistence.Enumerated;
import com.iut.gl.entity.ManagementStatus;
import javax.persistence.EnumType;
import com.iut.gl.entity.ManagementType;
import com.iut.gl.entity.Appointment;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Management implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String topic;

	@Column
	private double note;

	@Column
	private String observation;

	@Column
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	@Temporal(TemporalType.DATE)
	private Date updatedAt;

	@Enumerated(EnumType.STRING)
	private ManagementStatus status;

	@Enumerated(EnumType.STRING)
	private ManagementType type;

	@OneToMany
	private Set<Appointment> appointments = new HashSet<Appointment>();

	@ManyToOne
	private Student student;

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
		if (!(obj instanceof Management)) {
			return false;
		}
		Management other = (Management) obj;
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public double getNote() {
		return note;
	}

	public void setNote(double note) {
		this.note = note;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
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

	public ManagementStatus getStatus() {
		return status;
	}

	public void setStatus(ManagementStatus status) {
		this.status = status;
	}

	public ManagementType getType() {
		return type;
	}

	public void setType(ManagementType type) {
		this.type = type;
	}

	public Set<Appointment> getAppointments() {
		return this.appointments;
	}

	public void setAppointments(final Set<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (topic != null && !topic.trim().isEmpty())
			result += "topic: " + topic;
		result += ", note: " + note;
		if (observation != null && !observation.trim().isEmpty())
			result += ", observation: " + observation;
		return result;
	}

	public Student getStudent() {
		return this.student;
	}

	public void setStudent(final Student student) {
		this.student = student;
	}
}