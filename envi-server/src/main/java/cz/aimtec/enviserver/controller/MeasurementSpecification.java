package cz.aimtec.enviserver.controller;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import cz.aimtec.enviserver.model.*;

public class MeasurementSpecification implements Specification<Measurement> {

	private SearchCriteria criteria;




	public MeasurementSpecification(SearchCriteria searchCriteria) {
	
	}




	@Override
	public Predicate toPredicate(Root<Measurement> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		if (criteria.getOperation().equalsIgnoreCase(">")) {
			return builder.greaterThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperation().equalsIgnoreCase("<")) {
			return builder.lessThan(root.<String>get(criteria.getKey()), criteria.getValue().toString());
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
