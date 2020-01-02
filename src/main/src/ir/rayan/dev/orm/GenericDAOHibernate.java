package ir.rayan.dev.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class GenericDAOHibernate implements GenericDAO {
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    GenericJdbcDAO genericJdbcDAO;

    public void setGenericJdbcDAO(GenericJdbcDAO genericJdbcDAO) {
        this.genericJdbcDAO = genericJdbcDAO;
    }
}
