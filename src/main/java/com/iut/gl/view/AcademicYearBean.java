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

import com.iut.gl.entity.AcademicYear;

/**
 * Backing bean for AcademicYear entities.
 * <p/>
 * This class provides CRUD functionality for all AcademicYear entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AcademicYearBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving AcademicYear entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private AcademicYear academicYear;

	public AcademicYear getAcademicYear() {
		return this.academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
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
			this.academicYear = this.example;
		} else {
			this.academicYear = findById(getId());
		}
	}

	public AcademicYear findById(Long id) {

		return this.entityManager.find(AcademicYear.class, id);
	}

	/*
	 * Support updating and deleting AcademicYear entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.academicYear);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.academicYear);
				return "view?faces-redirect=true&id="
						+ this.academicYear.getId();
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
			AcademicYear deletableEntity = findById(getId());

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
	 * Support searching AcademicYear entities with pagination
	 */

	private int page;
	private long count;
	private List<AcademicYear> pageItems;

	private AcademicYear example = new AcademicYear();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public AcademicYear getExample() {
		return this.example;
	}

	public void setExample(AcademicYear example) {
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
		Root<AcademicYear> root = countCriteria.from(AcademicYear.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<AcademicYear> criteria = builder
				.createQuery(AcademicYear.class);
		root = criteria.from(AcademicYear.class);
		TypedQuery<AcademicYear> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<AcademicYear> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}
		String startYear = this.example.getStartYear();
		if (startYear != null && !"".equals(startYear)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("startYear")),
					'%' + startYear.toLowerCase() + '%'));
		}
		String endYear = this.example.getEndYear();
		if (endYear != null && !"".equals(endYear)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("endYear")),
					'%' + endYear.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<AcademicYear> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back AcademicYear entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<AcademicYear> getAll() {

		CriteriaQuery<AcademicYear> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(AcademicYear.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(AcademicYear.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final AcademicYearBean ejbProxy = this.sessionContext
				.getBusinessObject(AcademicYearBean.class);

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

				return String.valueOf(((AcademicYear) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private AcademicYear add = new AcademicYear();

	public AcademicYear getAdd() {
		return this.add;
	}

	public AcademicYear getAdded() {
		AcademicYear added = this.add;
		this.add = new AcademicYear();
		return added;
	}
}
