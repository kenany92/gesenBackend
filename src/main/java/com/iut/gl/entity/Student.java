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
import com.iut.gl.entity.AcademicYear;
import javax.persistence.Enumerated;
import com.iut.gl.entity.StudentGender;
import javax.persistence.EnumType;
import com.iut.gl.entity.Filiere;
import javax.persistence.ManyToOne;
import com.iut.gl.entity.Management;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@XmlRootElement
public class Student implements Serializable {

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
	private String phone;

	@Column
	private String email;

	@Column
	@Temporal(TemporalType.DATE)
	private Date createdAt;

	@Column
	@Temporal(TemporalType.DATE)
	private Date updatedAt;

	@Enumerated(EnumType.STRING)
	private StudentGender gender;

	@ManyToOne
	private Filiere filiere;

	@OneToMany
	private Set<Management> management = new HashSet<Management>();

	@ManyToOne
	private AcademicYear academicYear;

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
		if (!(obj instanceof Student)) {
			return false;
		}
		Student other = (Student) obj;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public StudentGender getGender() {
		return gender;
	}

	public void setGender(StudentGender gender) {
		this.gender = gender;
	}

	public Filiere getFiliere() {
		return this.filiere;
	}

	public void setFiliere(final Filiere filiere) {
		this.filiere = filiere;
	}

	public Set<Management> getManagement() {
		return this.management;
	}

	public void setManagement(final Set<Management> management) {
		this.management = management;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (fullName != null && !fullName.trim().isEmpty())
			result += "fullName: " + fullName;
		if (phone != null && !phone.trim().isEmpty())
			result += ", phone: " + phone;
		if (email != null && !email.trim().isEmpty())
			result += ", email: " + email;
		return result;
	}

	public AcademicYear getAcademicYear() {
		return this.academicYear;
	}

	public void setAcademicYear(final AcademicYear academicYear) {
		this.academicYear = academicYear;
	}
}