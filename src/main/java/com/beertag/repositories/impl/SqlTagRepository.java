package com.beertag.repositories.impl;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.Tag;
import com.beertag.repositories.base.TagRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SqlTagRepository implements TagRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlTagRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Tag> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Tag").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public Tag getTagById(int id) {

        Tag tag;

        try (Session session = sessionFactory.openSession()) {
            tag = session.get(Tag.class, id);
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        if (tag == null) {
            throw new DatabaseItemNotFoundException("This tag does not exist in the database");
        }

        return tag;
    }

    @Override
    public Tag getByName(String name){
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Tag where name = :tagName");
            query.setParameter("tagName", name);
            if (query.list().isEmpty()) {
               // throw new DatabaseItemNotFoundException(String.format("tag with name %s does not exist", name));
                return null;
            }

            return (Tag)query.list().get(0);
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }
}
