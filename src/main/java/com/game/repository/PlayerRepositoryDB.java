package com.game.repository;

import com.game.config.SessionCreator;
import com.game.entity.Player;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Optional;

@Repository(value = "db")
public class PlayerRepositoryDB implements IPlayerRepository {

    private final SessionCreator creator;

    public PlayerRepositoryDB(SessionCreator creator) {
        this.creator = creator;
    }

    @Override
    public List<Player> getAll(int pageNumber, int pageSize) {
        int startEntry = (pageNumber - 1) * pageSize;
        Session session = creator.getSession();
        try {
            String sql = "SELECT * FROM rpg.player LIMIT :pageSize OFFSET :startEntry ";
            NativeQuery<Player> query = session.createNativeQuery(sql, Player.class);
            query.setParameter("pageSize", pageSize);
            query.setParameter("startEntry", startEntry);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public int getAllCount() {
        Session session = creator.getSession();
        try {
            Query<Integer> query = session.createNamedQuery("Player_GetAllCount", Integer.class);
            return query.getSingleResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Player save(Player player) {
        Session session = creator.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }
    }

    @Override
    public Player update(Player player) {
        Session session = creator.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(player);
            transaction.commit();
            return player;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }
    }

    @Override
    public Optional<Player> findById(long id) {
        Session session = creator.getSession();
        try {
            String hql = "SELECT p FROM Player p WHERE p.id = :id";

            return session.createQuery(hql, Player.class)
                    .setParameter("id", id)
                    .uniqueResultOptional();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void delete(Player player) {
        Session session = creator.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.remove(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            session.close();
        }
    }

    @PreDestroy
    public void beforeStop() {
        creator.close();
    }

}