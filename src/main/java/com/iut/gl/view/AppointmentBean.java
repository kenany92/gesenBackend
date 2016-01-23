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

import com.iut.gl.entity.Appointment;
import com.iut.gl.entity.Management;

/**
 * Backing bean for Appointment entities.
 * <p/>
 * This class provides CRUD functionality for all Appointment entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AppointmentBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Appointment entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Appointment appointment;

	public Appointment getAppointment() {
		return this.appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
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
			this.appointment = this.example;
		} else {
			this.appointment = findById(getId());
		}
	}

	public Appointment findById(Long id) {

		return this.entityManager.find(Appointment.class, id);
	}

	/*
	 * Support updating and deleting Appointment entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.appointment);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.appointment);
				return "view?faces-redirect=true&id="
						+ this.appointment.getId();
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
			Appointment deletableEntity = findById(getId());

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
	 * Support searching Appointment entities with pagination
	 */

	private int page;
	private long count;
	private List<Appointment> pageItems;

	private Appointment example = new Appointment();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Appointment getExample() {
		return this.example;
	}

	public void setExample(Appointment example) {
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
		Root<Appointment> root = countCriteria.from(Appointment.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Appointment> criteria = builder
				.createQuery(Appointment.class);
		root = criteria.from(Appointment.class);
		TypedQuery<Appointment> query = this.entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Appointment> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String title = this.example.getTitle();
		if (title != null && !"".equals(title)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("title")),
					'%' + title.toLowerCase() + '%'));
		}
		String works = this.example.getWorks();
		if (works != null && !"".equals(works)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("works")),
					'%' + works.toLowerCase() + '%'));
		}
		String results = this.example.getResults();
		if (results != null && !"".equals(results)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("results")),
					'%' + results.toLowerCase() + '%'));
		}
		Management management = this.example.getManagement();
		if (management != null) {
			predicatesList
					.add(builder.equal(root.get("management"), management));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Appointment> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Appointment entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<Appointment> getAll() {

		CriteriaQuery<Appointment> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(Appointment.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(Appointment.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final AppointmentBean ejbProxy = this.sessionContext
				.getBusinessObject(AppointmentBean.class);

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

				return String.valueOf(((Appointment) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Appointment add = new Appointment();

	public Appointment getAdd() {
		return this.add;
	}

	public Appointment getAdded() {
		Appointment added = this.add;
		this.add = new Appointment();
		return added;
	}
}
