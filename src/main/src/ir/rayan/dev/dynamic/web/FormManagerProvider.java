package ir.rayan.dev.dynamic.web;

import ir.rayan.data.form.FormManager;
import ir.rayan.data.form.FormModelManager;

import java.io.InputStream;

/**
 * Created by mj.rahmati on 12/15/2019.
 */
public class FormManagerProvider {
    private String[] jsonResources;
    private SQLManagerProvider sqlManagerProvider;
    private FormManager formManager;

    public void setJsonResources(String[] jsonResources) {
        this.jsonResources = jsonResources;
    }

    public void setSqlManagerProvider(SQLManagerProvider sqlManagerProvider) {
        this.sqlManagerProvider = sqlManagerProvider;
    }

    public void init() throws Exception {
        InputStream[] inputStreams = new InputStream[jsonResources.length];
        for (int i = 0; i < jsonResources.length; i++) {
            inputStreams[i] = FormManagerProvider.class.getResourceAsStream(jsonResources[i]);
        }
        formManager = FormManager.createFormManager(
                FormModelManager.getNewInstance(
                        sqlManagerProvider.getSqlManager().getSQLModelManager(), inputStreams),
                sqlManagerProvider.getSqlManager());
    }

    public FormManager getFormManager() {
        return formManager;
    }
}
