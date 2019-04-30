package cz.aimtec.enviserver.controller;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.aimtec.enviserver.model.*;

public class MeasurementSpecification implements Specification<Measurement> {

	private SearchCriteria criteria;

	public SearchCriteria getSearchCriteria() {
		return criteria;
	}

	public void setSearchCriteria(SearchCriteria criteria1) {
		this.criteria = criteria1;
	}

	public MeasurementSpecification(SearchCriteria searchCriteria) {
		setSearchCriteria(searchCriteria);
	}

	@Override
	public Predicate toPredicate(Root<Measurement> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		if (criteria.getKey().equals("status")) {
			return builder.equal(root.get(criteria.getKey()),
					MeasurementStatus.valueOf(criteria.getValue().toString().toUpperCase()));
		} else if (criteria.getKey().equals("createdOn")) {
			if (criteria.getOperation().equalsIgnoreCase(">")) {
				return builder.greaterThanOrEqualTo(root.get(criteria.getKey()),
						Timestamp.valueOf(criteria.getValue().toString()));
			} else if (criteria.getOperation().equalsIgnoreCase("<")) {
				Timestamp measurementAfterTimestamp = Timestamp.valueOf(criteria.getValue().toString());
				return builder.greaterThanOrEqualTo(root.<Timestamp>get(criteria.getKey()), measurementAfterTimestamp);
			}
		} else if (criteria.getOperation().equalsIgnoreCase(">")) {
			return builder.greaterThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return builder.lessThanOrEqualTo(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase(":")) {
			if (root.get(criteria.getKey()).getJavaType() == String.class) {
				return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
			} else {
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		return null;
	}
}
