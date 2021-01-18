package com.beertag.repositories.impl;

import com.beertag.exceptions.DatabaseItemNotFoundException;
import com.beertag.models.*;
import com.beertag.repositories.base.BeerRepository;
import com.beertag.repositories.base.UserRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class SqlUserRepository implements UserRepository {

    private SessionFactory sessionFactory;

    @Autowired
    public SqlUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User").list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public User getByName(String username) {
        User result = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User u where u.username like :name");
            query.setParameter("name", username);
            if (!query.list().isEmpty()) {
                result = query.getSingleResult();
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.toString());
        }
        return result;
    }

    @Override
    public boolean isPresent(String username) {
        boolean isInDatabase = true;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User u where u.username like :name");
            query.setParameter("name", username);
            if (query.list().isEmpty()) {
                isInDatabase = false;
            }
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.toString());
        }
        return isInDatabase;
    }

    @Override
    public void updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Authority authority = new Authority();
            authority.setAuthority("USER");
            authority.setUsername(user.getUsername());
            //session.save(user.getLoginUserInfo());
            session.save(user);
            session.save(authority);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void rateBeer(User user, Beer beer, int rating) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("from Rating r where r.user.id = :uID and r.beer.id = :bID");
            query.setParameter("uID", user.getId());
            query.setParameter("bID", beer.getId());

            Rating ratingObj = new Rating(rating, user, beer);
            if (query.list().isEmpty()) {
                beer.addRating(ratingObj);
                user.addRating(ratingObj);
                session.save(ratingObj);
            } else {
                Rating updateRating = (Rating) query.getSingleResult();
                beer.updateRating(ratingObj);
                user.updateRating(ratingObj);
                updateRating.setRating(rating);
                session.update(updateRating);
            }

            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public User getById(int id) {
        User user;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user = session.get(User.class, id);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }

        if (user == null) {
            throw new DatabaseItemNotFoundException(String.format("User with id %d can not be found", id));
        }

        return user;
    }

    @Override
    public List<Beer> getMostRankedBeersOfUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            int maxTopBeers = 3;

            Query query = session.createQuery("select r.beer from Beer as b join b.ratings as r " +
                    "where r.user.id = :id order by r.rating desc");
            query.setParameter("id", userId);
            query.setMaxResults(maxTopBeers);

            session.getTransaction().commit();
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> getWishedBeers(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("select b from Beer as b join b.bts as status " +
                    "where status.user.id = :id and status.beer.id = b.id and status.beerType.type like 'Wished'");
            query.setParameter("id", userId);

            session.getTransaction().commit();
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public List<Beer> getDrankBeers(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("select b from Beer as b join b.bts as status " +
                    "where status.user.id = :id and status.beer.id = b.id and status.beerType.type like 'Drank'");
            query.setParameter("id", userId);

            session.getTransaction().commit();
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }    }

    @Override
    public List<Beer> getUsersBeers(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("select b from Beer as b where b.addedBy.id = :id ");
            query.setParameter("id", userId);

            session.getTransaction().commit();
            return query.list();
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void setBeerType(Beer beer, User user, String type) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.createQuery("from BeerTypeStatus bts where bts.beer.id = :bID " +
                    "and bts.user.id = :uID");
            query.setParameter("bID", beer.getId());
            query.setParameter("uID", user.getId());

            Query idType = session.createQuery(
                    "select ubt.id from UserBeerType ubt where ubt.type like :type");
            idType.setParameter("type", type);

            int typeID = (Integer) idType.getSingleResult();
            UserBeerType ubt = session.get(UserBeerType.class, typeID);

            if (query.list().isEmpty()) {
                session.save(new BeerTypeStatus(user, beer, ubt));
            } else {
                BeerTypeStatus bts = (BeerTypeStatus) query.getSingleResult();
                bts.setBeer(beer);
                bts.setUser(user);
                bts.setBeerType(ubt);
                session.update(bts);
            }

            session.getTransaction().commit();
        }  catch (DatabaseItemNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (HibernateException he) {
            System.out.println(he.getMessage());
            throw he;
        }
    }

    @Override
    public void deleteUser(int userId, BeerRepository beerRepo) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = session.get(User.class, userId);

            for (Beer b : getUsersBeers(userId)) {
                beerRepo.deleteBeer(b.getId());
            }

            session.getTransaction().commit();
            session.beginTransaction();

            Query<BeerTypeStatus> statusQuery = session.createQuery("from BeerTypeStatus where user.id = :uID");
            statusQuery.setParameter("uID", userId);

            for (BeerTypeStatus bts : statusQuery.list()) {
                session.delete(bts);
            }

            for (Rating r : user.getRatings()) {
                session.delete(r);
            }

            session.getTransaction().commit();
            session.beginTransaction();

            session.delete(user);

            Query authQuery = session.createQuery("from Authority where username = :uName");
            authQuery.setParameter("uName", user.getUsername());
            session.delete(authQuery.getSingleResult());

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
    public int getBeerRating(int userId, int beerId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Rating> query = session.createQuery("from Rating r where r.user.id = :uID and r.beer.id = :bID");
            query.setParameter("uID", userId);
            query.setParameter("bID", beerId);
            session.getTransaction().commit();

            if(query.list().isEmpty()) {
                return 0;
            } else {
                return query.getSingleResult().getRating();
            }
        }
    }
}
