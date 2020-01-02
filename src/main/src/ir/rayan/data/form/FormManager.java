package ir.rayan.data.form;

import ir.rayan.data.sql.SQLManager;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mj.rahmati on 12/25/2019.
 */
public class FormManager {
    private FormModelManager modelManager;
    private SQLManager sqlManager;

    private static FormManager formManager;

    private Object getValue(String key, HttpServletRequest request, Map<String, Object> paramMap) {
        Object obj = null;
        if(request != null) {
            obj = request.getParameter(key);
            if (obj == null)
                obj = request.getAttribute(key);
            if (obj == null)
                obj = request.getSession().getAttribute(key);
            request.getSession().setAttribute(key, obj);
        }
        if(paramMap != null && obj == null)
            obj = paramMap.get(key);
//        if(obj == null)
//            return "";
        return obj;
    }

    private Object getValue(String key, String type, HttpServletRequest request, Map<String, Object> paramMap) {
        Object obj = null;
        if(request != null) {
            obj = request.getParameter(key);
            if (obj == null)
                obj = request.getAttribute(key);
            if (obj == null)
                obj = request.getSession().getAttribute(key);
            request.getSession().setAttribute(key, obj);
        }
        if(paramMap != null && obj == null)
            obj = paramMap.get(key);
        if(obj == null) {
            if(type.equals("string"))
                return "";
            else
                return null;
        }
        return obj;
    }

    private FormManager(FormModelManager modelManager, SQLManager sqlManager) {
        this.modelManager = modelManager;
        this.sqlManager = sqlManager;
    }

    public static FormManager createFormManager(FormModelManager formModelManager, SQLManager sqlManager) {
        if(formManager == null && sqlManager != null) {
            formManager = new FormManager(formModelManager, sqlManager);
        }
        return formManager;
    }

    public static FormManager getFormManager() {
        if(formManager != null)
            return formManager;
        return null;
    }

    public FormPersistDef getFormInsertDef(String formName) {
        FormPersistDef formDef = modelManager.getFormInsertDef(formName);
        return formDef;
    }

    public FormDef getFormDef(String formName) {
        return modelManager.getFormDef(formName);
    }

    public Map<String, String> getListMap(Object listObject, String title, String value) {
        List<Map<String, String>> list = (List<Map<String, String>> )listObject;
        Map selectMap = new LinkedHashMap();
        for(Map<String, String> map : list) {
            selectMap.put(map.get(value), map.get(title));
        }
        return selectMap;
    }
}
