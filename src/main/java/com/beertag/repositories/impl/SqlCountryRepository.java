package com.beertag.repositories.impl;

import com.beertag.models.OriginCountry;
import com.beertag.repositories.base.CountryRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlCountryRepository implements CountryRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlCountryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<OriginCountry> getAll() {
        List<OriginCountry> countries;

        try (Session session = sessionFactory.openSession()) {
            countries = session.createQuery("FROM OriginCountry", OriginCountry.class).list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        return countries;
    }

    @Override
    public OriginCountry getById(int id) {
        OriginCountry country;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            country = session.get(OriginCountry.class, id);
            session.getTransaction().commit();
        }

        return country;
    }

    @Override
    public OriginCountry getByName(String country) {
        OriginCountry result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<OriginCountry> query = session.createQuery("from OriginCountry oc where oc.name like :countryName");
            query.setParameter("countryName", country);
            if (!query.list().isEmpty()) {
                result = query.getSingleResult();
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.toString());
            throw he;
        }

        return result;
    }
}
