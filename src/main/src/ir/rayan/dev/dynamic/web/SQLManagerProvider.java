package ir.rayan.dev.dynamic.web;

import ir.rayan.dev.data.sql.SQLManager;
import ir.rayan.dev.data.sql.SQLModelManager;

import javax.sql.DataSource;
import java.io.InputStream;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class SQLManagerProvider {
    private String[] jsonResources;
    private SQLManager sqlManager;
    private DataSource dataSource;

    public void setJsonResources(String[] jsonResources) {
        this.jsonResources = jsonResources;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init() throws Exception {
        InputStream[] inputStreams = new InputStream[jsonResources.length];
        for (int i = 0; i < jsonResources.length; i++) {
            inputStreams[i] = SQLManagerProvider.class.getResourceAsStream(jsonResources[i]);
        }
        sqlManager = SQLManager.createSQLManager(SQLModelManager.getNewInstance(inputStreams), dataSource);
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }
}
