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
import com.iut.gl.entity.Teacher;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import com.iut.gl.entity.Filiere;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Department implements Serializable {

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
	private Set<Teacher> teachers = new HashSet<Teacher>();

	@OneToMany
	private Set<Filiere> filieres = new HashSet<Filiere>();

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
		if (!(obj instanceof Department)) {
			return false;
		}
		Department other = (Department) obj;
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

	public Set<Teacher> getTeachers() {
		return this.teachers;
	}

	public void setTeachers(final Set<Teacher> teachers) {
		this.teachers = teachers;
	}

	public Set<Filiere> getFilieres() {
		return this.filieres;
	}

	public void setFilieres(final Set<Filiere> filieres) {
		this.filieres = filieres;
	}
}