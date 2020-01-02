package ir.rayan.dev.orm;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class GenericManagerImpl implements GenericManager {
    public GenericJdbcDAO genericJdbcDAO;

    public void setGenericJdbcDAO(GenericJdbcDAO genericJdbcDAO) {
        this.genericJdbcDAO = genericJdbcDAO;
    }

    public GenericJdbcDAO getGenericJdbcDAO() {
        return genericJdbcDAO;
    }
}
