package com.beertag.repositories.impl;

import com.beertag.models.BeerStyle;
import com.beertag.repositories.base.BeerStyleRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SqlBeerStyleRepository implements BeerStyleRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlBeerStyleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<BeerStyle> getAll() {
        List<BeerStyle> beerStyles;

        try (Session session = sessionFactory.openSession()) {
            beerStyles = session.createQuery("from BeerStyle", BeerStyle.class).list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        return beerStyles;
    }

    @Override
    public BeerStyle getById(int id) {
        BeerStyle country;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            country = session.get(BeerStyle.class, id);
            session.getTransaction().commit();
        }

        return country;
    }

    @Override
    public BeerStyle getByName(String style) {
        BeerStyle result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<BeerStyle> query = session.createQuery("from BeerStyle bs where bs.name like :styleName");
            query.setParameter("styleName", style);
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
