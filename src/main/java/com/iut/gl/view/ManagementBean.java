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

import com.iut.gl.entity.Management;
import com.iut.gl.entity.ManagementStatus;
import com.iut.gl.entity.ManagementType;
import com.iut.gl.entity.Student;

/**
 * Backing bean for Management entities.
 * <p/>
 * This class provides CRUD functionality for all Management entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Management entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Management management;

	public Management getManagement() {
		return this.management;
	}

	public void setManagement(Management management) {
		this.management = management;
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
			this.management = this.example;
		} else {
			this.management = findById(getId());
		}
	}

	public Management findById(Long id) {

		return this.entityManager.find(Management.class, id);
	}

	/*
	 * Support updating and deleting Management entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.management);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.management);
				return "view?faces-redirect=true&id=" + this.management.getId();
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
			Management deletableEntity = findById(getId());

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
	 * Support searching Management entities with pagination
	 */

	private int page;
	private long count;
	private List<Management> pageItems;

	private Management example = new Management();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Management getExample() {
		return this.example;
	}

	public void setExample(Management example) {
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
		Root<Management> root = countCriteria.from(Management.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Management> criteria = builder
				.createQuery(Management.class);
		root = criteria.from(Management.class);
		TypedQuery<Management> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Management> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String topic = this.example.getTopic();
		if (topic != null && !"".equals(topic)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("topic")),
					'%' + topic.toLowerCase() + '%'));
		}
		String observation = this.example.getObservation();
		if (observation != null && !"".equals(observation)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("observation")),
					'%' + observation.toLowerCase() + '%'));
		}
		ManagementStatus status = this.example.getStatus();
		if (status != null) {
			predicatesList.add(builder.equal(root.get("status"), status));
		}
		ManagementType type = this.example.getType();
		if (type != null) {
			predicatesList.add(builder.equal(root.get("type"), type));
		}
		Student student = this.example.getStudent();
		if (student != null) {
			predicatesList.add(builder.equal(root.get("student"), student));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Management> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Management entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Management> getAll() {

		CriteriaQuery<Management> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Management.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Management.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ManagementBean ejbProxy = this.sessionContext
				.getBusinessObject(ManagementBean.class);

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

				return String.valueOf(((Management) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Management add = new Management();

	public Management getAdd() {
		return this.add;
	}

	public Management getAdded() {
		Management added = this.add;
		this.add = new Management();
		return added;
	}
}
