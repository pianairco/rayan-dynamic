package ir.rayan.dev.dynamic.web;

import ir.rayan.data.form.ElementSelect;
import ir.rayan.data.form.FormDef;
import ir.rayan.data.form.FormPersistDef;
import ir.rayan.data.form.FormSelectDef;
import ir.rayan.data.sql.RayanSQLException;
import ir.rayan.data.sql.SQLManager;
import ir.rayan.dev.orm.GenericManager;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
