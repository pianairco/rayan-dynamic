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
public class CommonDispatchAction extends DispatchAction {
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

//    @Override
//    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if(this.isCancelled(request)) {
//            ActionForward af = this.cancelled(mapping, form, request, response);
//            if(af != null) {
//                return af;
//            }
//        }
//
//        String parameter = this.getParameter(mapping, form, request, response);
//        String name = this.getMethodName(mapping, form, request, response, parameter);
//        if("execute".equals(name) || "perform".equals(name)) {
//            String message = messages.getMessage("dispatch.recursive", mapping.getPath());
//            log.error(message);
//            throw new ServletException(message);
//        }
//        return this.dispatchMethod(mapping, form, request, response, name);
//    }

    public ActionForward dispatchUnnamedMethod(ActionMapping mapping,
                                               ActionForm form,
                                               HttpServletRequest request, HttpServletResponse response,
                                               String name) throws SQLException, RayanSQLException {
        ActionForward action = unnamedMethod(mapping, form, request, response, mapping.getPath() + "@" + name);
        return action;
    }

    public ActionForward dispatchMethod(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
            String name)
            throws Exception {
        if (name == null)
            return this.unspecified(mapping, form, request, response);

        Method method = null;
        String message = null;
        try {
            method = this.getMethod(name);
        } catch (NoSuchMethodException var13) {
            dispatchUnnamedMethod(mapping, form, request, response, name);
        }
        try {
            Object[] args = new Object[]{mapping, form, request, response};
            ActionForward forward = (ActionForward) method.invoke(this, args);
            return forward;
        } catch (ClassCastException var14) {
            message = messages.getMessage("dispatch.return", mapping.getPath(), name);
            log.error(message, var14);
            throw var14;
        } catch (IllegalAccessException var15) {
            message = messages.getMessage("dispatch.error", mapping.getPath(), name);
            log.error(message, var15);
            throw var15;
        } catch (InvocationTargetException var16) {
            Throwable t = var16.getTargetException();
            if (t instanceof Exception) {
                throw (Exception) t;
            } else {
                message = messages.getMessage("dispatch.error", mapping.getPath(), name);
                log.error(message, var16);
                throw new ServletException(t);
            }
        }
    }

    protected ActionForward unnamedMethod(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request,
                                          HttpServletResponse response, String formName) throws SQLException, RayanSQLException {
//         FormPersistDef formInsertDef = formManagerProvider.getFormManager().getFormInsertDef(formName);

        FormDef formDef = formManagerProvider.getFormManager().getFormDef(formName);

        request.setAttribute("form-def", formDef);
        request.setAttribute("form-manager", formManagerProvider.getFormManager());

        ActionMessages messages = new ActionMessages();

        if(!formDef.getSelects().isEmpty()) {
            for(ElementSelect elementSelect : formDef.getSelects()) {
                try {
                    sqlManager.query(elementSelect.getQueryName(),
                            manager.getGenericJdbcDAO().getDataSource(),
                            request, new LinkedHashMap<>(), elementSelect.getName());
                    if(elementSelect.getMapper() != null && !elementSelect.getMapper().isEmpty()) {
                        Object attribute = request.getAttribute(elementSelect.getName());
                        if(attribute != null && attribute instanceof List && ((List)attribute).size() == 1) {
                            Map map = (Map)((List) attribute).get(0);
                            for(String mapped : elementSelect.getMapper()) {
                                String[] split = mapped.split(":");
                                request.getSession().setAttribute(split[1], map.get(split[0]));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(formDef instanceof FormPersistDef) {
            executeCommonPersist(request, response, (FormPersistDef) formDef, messages);
        } else if(formDef instanceof FormSelectDef)
            executeCommonSelect(request, response, (FormSelectDef) formDef);

        return mapping.findForward("common");
//        return mapping.findForward("unnamedMethod-jsp");
    }

    protected String getValue(HttpServletRequest request, String key) {
        return getValue(request, key, false);
    }

    protected String getValue(HttpServletRequest request, String key, boolean saveToSession) {
        Object obj = null;
        if(request != null) {
            obj = request.getParameter(key);
            if (obj == null)
                obj = request.getAttribute(key);
            if (obj == null)
                obj = request.getSession().getAttribute(key);
            if(saveToSession)
                request.getSession().setAttribute(key, obj);
        }
        return (String)obj;
    }

    protected void executeCommonPersist(
            HttpServletRequest request, HttpServletResponse response,
            FormPersistDef formPersistDef, ActionMessages messages)
            throws SQLException, RayanSQLException {
        request.setAttribute("include", "edit");
        String persist = request.getParameter("persist");
        if(persist != null && persist.equals("true")) {
            if(request.getParameter(formPersistDef.getIdParam()) != null &&
                    !request.getParameter(formPersistDef.getIdParam()).isEmpty()) {
                executeCommonUpdate(request, response, formPersistDef);
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "message.saved", getValue(request, formPersistDef.getIdParam())));
                saveMessages(request.getSession(), messages);
            } else {
                executeCommonInsert(request, response, formPersistDef);
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "message.saved", getValue(request, formPersistDef.getIdParam())));
                saveMessages(request.getSession(), messages);
            }
        }
    }

    protected void executeCommonInsert(HttpServletRequest request, HttpServletResponse response, FormPersistDef formPersistDef)
            throws SQLException, RayanSQLException {
        sqlManager.query(formPersistDef.getQueryInsertName(), manager.getGenericJdbcDAO().getDataSource(), request, null);
    }

    protected void executeCommonUpdate(HttpServletRequest request, HttpServletResponse response, FormPersistDef formPersistDef)
            throws SQLException, RayanSQLException {
        sqlManager.query(formPersistDef.getQueryUpdateName(), manager.getGenericJdbcDAO().getDataSource(), request, null);
    }

    protected void executeCommonSelect(HttpServletRequest request, HttpServletResponse response, FormSelectDef formSelectDef)
            throws SQLException {
        request.setAttribute("include", "list");
        String query = sqlManager.createQuery(formSelectDef.getQueryName(), request, null);
        request.setAttribute(formSelectDef.getQueryName(), query);

//        sqlManager.query(formSelectDef.getQueryName(), getManager().getGenericJdbcDAO().getDataSource(), request, null);

//        if(!formSelectDef.getSelects().isEmpty()) {
//            for(ElementSelect elementSelect : formSelectDef.getSelects()) {
//                try {
//                    sqlManager.query(elementSelect.getQueryName(),
//                            getManager().getGenericJdbcDAO().getDataSource(),
//                            request, new LinkedHashMap<>(), elementSelect.getName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}
