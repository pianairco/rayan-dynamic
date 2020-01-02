package ir.rayan.dev.orm;

import javax.sql.DataSource;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public interface GenericJdbcDAO {
    DataSource getDataSource();
}
