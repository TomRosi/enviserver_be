package cz.aimtec.enviserver.controller;

import cz.aimtec.enviserver.model.SensorTable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SensorTableSpecification implements Specification<SensorTable> {

	private SearchCriteria criteria;

	public SearchCriteria getSearchCriteria() {
		return criteria;
	}

	public void setSearchCriteria(SearchCriteria criteria1) {
		this.criteria = criteria1;
	}

	public SensorTableSpecification(SearchCriteria searchCriteria) {
		setSearchCriteria(searchCriteria);
	}

	@Override
	public Predicate toPredicate(Root<SensorTable> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		if (criteria.getOperation().equalsIgnoreCase(":")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
			} else {
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		return null;
	}
}
