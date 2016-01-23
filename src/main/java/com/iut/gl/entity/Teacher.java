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
import javax.persistence.Enumerated;
import com.iut.gl.entity.TeacherGender;
import javax.persistence.EnumType;
import com.iut.gl.entity.Department;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Teacher implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String fullName;

	@Column
	private String label;

	@Column
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	@Temporal(TemporalType.DATE)
	private Date updatedAt;

	@Enumerated(EnumType.STRING)
	private TeacherGender gender;

	@ManyToOne
	private Department department;

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
		if (!(obj instanceof Teacher)) {
			return false;
		}
		Teacher other = (Teacher) obj;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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

	public TeacherGender getGender() {
		return gender;
	}

	public void setGender(TeacherGender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (fullName != null && !fullName.trim().isEmpty())
			result += "fullName: " + fullName;
		if (label != null && !label.trim().isEmpty())
			result += ", label: " + label;
		return result;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}
}