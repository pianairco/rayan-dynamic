package ir.rayan.dev.dynamic.web;

import ir.rayan.dev.data.sql.RayanSQLException;
import ir.rayan.dev.data.sql.SQLManager;
import ir.rayan.dev.orm.GenericManager;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Created by mj.rahmati on 12/31/2019.
 */
public class CustomDispatchAction extends CommonDispatchAction {
    protected GenericManager manager = null;

//    public GenericManager getManager() {
//        return manager;
//    }

    public void setManager(GenericManager manager) {
        this.manager = manager;
    }

    private SQLManagerProvider sqlManagerProvider;
    private SQLManager sqlManager;
    private FormManagerProvider formManagerProvider;

    public void setSqlManagerProvider(SQLManagerProvider sqlManagerProvider) {
        this.sqlManagerProvider = sqlManagerProvider;
        this.sqlManager = sqlManagerProvider.getSqlManager();
    }

    public void setFormManagerProvider(FormManagerProvider formManagerProvider) {
        this.formManagerProvider = formManagerProvider;
    }

    @Override
    public ActionForward unnamedMethod(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response, String formName) throws SQLException, RayanSQLException {
        super.unnamedMethod(mapping, form, request, response, formName);
        return mapping.findForward("custom");
    }
}
