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
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import com.iut.gl.entity.Department;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Filiere implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String name;

	@Column
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	@Temporal(TemporalType.DATE)
	private Date updatedAt;

	@OneToMany
	private Set<Student> students = new HashSet<Student>();

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
		if (!(obj instanceof Filiere)) {
			return false;
		}
		Filiere other = (Filiere) obj;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		if (name != null && !name.trim().isEmpty())
			result += "name: " + name;
		return result;
	}

	public Set<Student> getStudents() {
		return this.students;
	}

	public void setStudents(final Set<Student> students) {
		this.students = students;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(final Department department) {
		this.department = department;
	}
}