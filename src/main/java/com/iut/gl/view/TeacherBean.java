package com.iut.gl.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.iut.gl.entity.Teacher;
import com.iut.gl.entity.Department;
import com.iut.gl.entity.TeacherGender;

/**
 * Backing bean for Teacher entities.
 * <p/>
 * This class provides CRUD functionality for all Teacher entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TeacherBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Teacher entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Teacher teacher;

	public Teacher getTeacher() {
		return this.teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "gesen-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.teacher = this.example;
		} else {
			this.teacher = findById(getId());
		}
	}

	public Teacher findById(Long id) {

		return this.entityManager.find(Teacher.class, id);
	}

	/*
	 * Support updating and deleting Teacher entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.teacher);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.teacher);
				return "view?faces-redirect=true&id=" + this.teacher.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Teacher deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Teacher entities with pagination
	 */

	private int page;
	private long count;
	private List<Teacher> pageItems;

	private Teacher example = new Teacher();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Teacher getExample() {
		return this.example;
	}

	public void setExample(Teacher example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Teacher> root = countCriteria.from(Teacher.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Teacher> criteria = builder.createQuery(Teacher.class);
		root = criteria.from(Teacher.class);
		TypedQuery<Teacher> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Teacher> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String fullName = this.example.getFullName();
		if (fullName != null && !"".equals(fullName)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("fullName")),
					'%' + fullName.toLowerCase() + '%'));
		}
		String label = this.example.getLabel();
		if (label != null && !"".equals(label)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("label")),
					'%' + label.toLowerCase() + '%'));
		}
		TeacherGender gender = this.example.getGender();
		if (gender != null) {
			predicatesList.add(builder.equal(root.get("gender"), gender));
		}
		Department department = this.example.getDepartment();
		if (department != null) {
			predicatesList
					.add(builder.equal(root.get("department"), department));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Teacher> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Teacher entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Teacher> getAll() {

		CriteriaQuery<Teacher> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Teacher.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Teacher.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final TeacherBean ejbProxy = this.sessionContext
				.getBusinessObject(TeacherBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Teacher) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Teacher add = new Teacher();

	public Teacher getAdd() {
		return this.add;
	}

	public Teacher getAdded() {
		Teacher added = this.add;
		this.add = new Teacher();
		return added;
	}
}
