package com.company.services.DAO;

import com.company.domain.Charity;
import lombok.NonNull;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CharityDAO extends BaseTemplateDAOImpl<Charity> {

    public CharityDAO() {
        super(Charity.class);
    }

    public Charity create(Charity obj, @NonNull EntityManager em) {
        return super.create(obj, em);
    }

    public Charity getById(Long id, @NonNull EntityManager em) {
        return super.getById(id, em);
    }

    public Boolean delete(Long id, @NonNull EntityManager em) {
        return super.deleteById(id, em);
    }
}

