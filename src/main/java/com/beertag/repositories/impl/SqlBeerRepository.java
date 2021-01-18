package com.beertag.repositories.impl;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.*;
import com.beertag.repositories.base.BeerRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SqlBeerRepository implements BeerRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlBeerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void add(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            BeerStyle style = session.get(BeerStyle.class, beer.getStyle().getId());
            beer.setStyle(style);

            OriginCountry country = session.get(OriginCountry.class, beer.getOriginCountry().getId());
            beer.setOriginCountry(country);

            User user = session.get(User.class, beer.getAddedBy().getId());
            beer.setAddedBy(user);

            session.save(beer);
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void updateBeer(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(beer);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void addTagToBeer(Beer beer, Tag tag, boolean unknownTag) {
        try (Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
            if (unknownTag) {
                session.save(tag);
            }
            beer.addTag(tag);
            session.update(beer);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Beer").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> sortByABV() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Beer b order by b.ABV asc").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> sortByBeerName() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Beer b order by b.name asc").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> sortByRating() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Beer b order by b.avgRating asc").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> filterByStyle(String beerStyle) {
        try (Session session = sessionFactory.openSession()) {
            Query<Beer> query = session.createQuery("from Beer where style.name = :style");
            query.setParameter("style", beerStyle);

            if (query.list().isEmpty()) {
                return new ArrayList<>();
//                throw new DatabaseItemNotFoundException(String.format("Beers with style %s do not exist", beerStyle));
            }
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> filterByCountry(String beerCountry) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Beer where originCountry.name = :country");
            query.setParameter("country", beerCountry);

            if (query.list().isEmpty()) {
                return new ArrayList<>();

//                throw new DatabaseItemNotFoundException(String.format("Beers from country %s do not exist", beerCountry));
            }
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> filterByTag(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from Beer b join b.tags tag where tag.name like :name");
            query.setParameter("name", tagName);

            if (query.list().isEmpty()) {
                return new ArrayList<>();
//                throw new DatabaseItemNotFoundException(String.format("Beers with tag %s do not exist", tagName));
            }
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Rating> getBeerRating(int beerId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rating> query = session.createQuery("from Rating r where r.beer.id = :bID");
            query.setParameter("bID", beerId);

            return query.list();
        }
    }

    @Override
    public void deleteBeer(int beerId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Beer beer = session.get(Beer.class, beerId);
            for (Rating r : beer.getRatings()) {
                session.delete(r);
            }

            for (Tag t : beer.getTags()) {
                session.delete(t);
            }

            Query<BeerTypeStatus> query = session.createQuery("from BeerTypeStatus where beer.id = :bID");
            query.setParameter("bID", beerId);

            for (BeerTypeStatus bts : query.list()) {
                session.delete(bts);
            }
            session.getTransaction().commit();

            session.beginTransaction();
            session.delete(beer);
            session.getTransaction().commit();
        } catch (DatabaseItemNotFoundException e) {
            e.getMessage();
            throw e;
        } catch (HibernateException he) {
            he.printStackTrace();
            throw he;
        }
    }

    @Override
    public Beer getById(int id) {
        Beer beer;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            beer = session.get(Beer.class, id);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        if (beer == null) {
            throw new DatabaseItemNotFoundException(String.format("Beer with id %d does not exist", id));
        }
        return beer;
    }

    @Override
    public void addBeer(Beer beer) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(beer);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }
}
