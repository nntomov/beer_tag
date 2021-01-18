package com.beertag.repositories.impl;

import com.beertag.models.Authority;
import com.beertag.repositories.base.AuthorityRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SqlAuthorityRepository implements AuthorityRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlAuthorityRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Authority> getAll() {
        List<Authority> authorities;

        try (Session session = sessionFactory.openSession()) {
            authorities = session.createQuery("from Authority ", Authority.class).list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        return authorities;
    }

    @Override
    public Authority getById(int id) {
        Authority authority;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            authority = session.get(Authority.class, id);
            session.getTransaction().commit();
        }

        return authority;
    }

    @Override
    public List<Authority> getByAuthority(String authoritory) {
        List<Authority> result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Authority> query = session.createQuery("from Authority bs where bs.authority like :authoritory");
            query.setParameter("authoritory", authoritory);
            if (!query.list().isEmpty()) {
                result = query.list();
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.toString());
            throw he;
        }

        return result;
    }

    @Override
    public Authority getByUsername(String username) {
        Authority result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Authority> query = session.createQuery("from Authority oc where oc.username like :username");
            query.setParameter("username", username);
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

    @Override
    public void updateAuthority(Authority authority) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(authority);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.toString());
            throw he;
        }
    }
}