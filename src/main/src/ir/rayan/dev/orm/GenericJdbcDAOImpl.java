package ir.rayan.dev.orm;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class GenericJdbcDAOImpl extends JdbcDaoSupport implements GenericJdbcDAO {
    private JdbcTemplate jdbcTemplate;

}
