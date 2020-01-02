package ir.rayan.dev.dynamic.web;

import ir.rayan.data.sql.SQLManager;
import ir.rayan.data.sql.SQLModelManager;

import java.io.InputStream;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class SQLManagerProvider {
    private String[] jsonResources;
    private SQLManager sqlManager;

    public void setJsonResources(String[] jsonResources) {
        this.jsonResources = jsonResources;
    }

    public void init() throws Exception {
        InputStream[] inputStreams = new InputStream[jsonResources.length];
        for (int i = 0; i < jsonResources.length; i++) {
            inputStreams[i] = SQLManagerProvider.class.getResourceAsStream(jsonResources[i]);
        }
        sqlManager = SQLManager.createSQLManager(SQLModelManager.getNewInstance(inputStreams));
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }
}
