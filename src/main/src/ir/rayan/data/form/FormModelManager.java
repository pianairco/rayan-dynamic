package ir.rayan.data.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.rayan.data.sql.SQLModelManager;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by mj.rahmati on 12/4/2019.
 */
public class FormModelManager {
    private Map<String, FormPersistDef> formInsertDefMap;
    private Map<String, FormSelectDef> formSelectDefMap;

    public FormDef getFormDef(String formName) {
        if(formInsertDefMap.containsKey(formName))
            return formInsertDefMap.get(formName);
        else if(formSelectDefMap.containsKey(formName))
            return formSelectDefMap.get(formName);
        return null;
    }

    public FormPersistDef getFormInsertDef(String formInsertName) {
        return formInsertDefMap.get(formInsertName);
    }

    FormSelectDef getFormSelectDef(String formSelectName) {
        return formSelectDefMap.get(formSelectName);
    }

    private FormModelManager(Map<String, FormPersistDef> formInsertDefMap,
                             Map<String, FormSelectDef> formSelectDefMap) {
        this.formInsertDefMap = formInsertDefMap;
        this.formSelectDefMap = formSelectDefMap;
    }

    private static List<ElementSelect> getSelects(Object list) {
        List<ElementSelect> elementSelects = new ArrayList<>();
        for(Map<String, Object> map : (List<Map<String, Object>>)list) {
            Object name = map.get("name");
            Object queryName = map.get("query-name");
            Object mapper = map.get("mapper");
            elementSelects.add(new ElementSelect((String) name, (String)queryName, mapper == null ? new ArrayList<>() : (List<String>)mapper));
        }
        return elementSelects;
//        return ((List<Map<String, String>>)list).stream()
//                .map(map -> new ElementSelect(map.get("name"), map.get("query-name")))
//                .collect(Collectors.toList());
    }

    private static List<ElementControl> getControls(Object list) {
        return ((List<Map<String, String>>)list).stream().map(
                map -> {
                    String disabledString = map.get("disabled");
                    boolean disabled = disabledString != null && disabledString.equals("true") ? true : false;
                    return new ElementControl(map.get("name"), map.get("title"), disabled, map.get("type"), map.get("items"),
                            map.get("query-name"), map.get("option-value"), map.get("option-title"));
                })
                .collect(Collectors.toList());
    }

    private static List<ElementButton> getButtons(Object list) {
        return ((List<Map<String, String>>)list).stream().map(
                map -> new ElementButton(map.get("name"), map.get("title"), map.get("type"), map.get("return-url")))
                .collect(Collectors.toList());
    }

    private static FormPersistDef getFormInsertDef(Map<String, Object> formInsertDefMap) throws RayanFormException {
        if(formInsertDefMap == null || formInsertDefMap.isEmpty())
            throw new RayanFormException("map is null or empty");
        FormPersistDef formPersistDef = new FormPersistDef();
        String name = (String) formInsertDefMap.get("name");
        String queryName = (String)formInsertDefMap.get("query-name");
        String title = (String)formInsertDefMap.get("title");
        String queryUpdateName = (String) formInsertDefMap.get("query-update-name");
        String queryInsertName = (String) formInsertDefMap.get("query-insert-name");
        String idParam = (String) formInsertDefMap.get("id-param");
        Object controlInRowObject = formInsertDefMap.get("control-in-row");
        int controlInRow = controlInRowObject == null ? 1 : (int) controlInRowObject;
        List<ElementSelect> selects = getSelects(formInsertDefMap.get("select"));
        List<ElementControl> controls = getControls(formInsertDefMap.get("control"));
        List<ElementButton> buttons = getButtons(formInsertDefMap.get("button"));
        formPersistDef.name = name;
        formPersistDef.queryName = queryName;
        formPersistDef.title = title;
        formPersistDef.queryUpdateName = queryUpdateName;
        formPersistDef.queryInsertName = queryInsertName;
        formPersistDef.idParam = idParam;
        formPersistDef.controlInRow = controlInRow;
        formPersistDef.selects = selects != null ? selects : new ArrayList<>();
        formPersistDef.controls = controls != null ? controls : new ArrayList<>();
        formPersistDef.buttons = buttons != null ? buttons : new ArrayList<>();
        return formPersistDef;
//        fillPersistenceColumns(formInsertDefMap, formPersistDef);
    }

    private static void fillFormSelectColumns(Map map, FormSelectDef formSelectDef) {
        List<Map<String, String>> columnDefs = (List<Map<String, String>>)map.get("columns");
        if(columnDefs == null || columnDefs.isEmpty()) {
            formSelectDef.formSelectColumnDefMap = new LinkedHashMap<>();
            formSelectDef.formSelectColumnDefs = new ArrayList<>();
            return;
        }
        List<FormSelectColumnDef> columns = new ArrayList<>();
        Map<String, FormSelectColumnDef> columnMap = new LinkedHashMap<>();
        for(Map<String, String> columnDef : columnDefs) {
            String property = columnDef.get("property");
            String titleKey = columnDef.get("title-key");
            String styleClass = columnDef.get("style-class");
            String sortable = columnDef.get("sortable");
            FormSelectColumnDef formSelectColumnDef = new FormSelectColumnDef(property, titleKey, styleClass, sortable != null && sortable.equalsIgnoreCase("true"));
            columnMap.put(property, formSelectColumnDef);
            columns.add(formSelectColumnDef);
        }
        formSelectDef.formSelectColumnDefMap = columnMap;
        formSelectDef.formSelectColumnDefs = columns;
    }

    private static FormSelectDef getFormSelectDef(SQLModelManager sqlModelManager, Map<String, Object> formSelectDefMap) {
        FormSelectDef formSelectDef = new FormSelectDef();
        String name = (String) formSelectDefMap.get("name");
        String queryName = (String) formSelectDefMap.get("query-name");
        String title = (String) formSelectDefMap.get("title");
        String decorator = (String) formSelectDefMap.get("decorator");
        String sortProperty = (String) formSelectDefMap.get("sortPropertyParamName");
        String sortOrder = (String) formSelectDefMap.get("sortOrderParamName");
        String rowPerPage = (String) formSelectDefMap.get("rowPerPageParamName");
        String actionURL = (String) formSelectDefMap.get("actionURL");
        String actionMethod = (String) formSelectDefMap.get("actionMethod");
        Object searchParams = formSelectDefMap.get("search-params");
        List<ElementSelect> selects = getSelects(formSelectDefMap.get("select"));
        Object controlInRowObject = formSelectDefMap.get("control-in-row");
        int controlInRow = controlInRowObject == null ? 1 : (int) controlInRowObject;
        List<ElementControl> controls = getControls(formSelectDefMap.get("control"));
        List<ElementButton> buttons = getButtons(formSelectDefMap.get("button"));
        if(searchParams == null || !(searchParams instanceof List))
            formSelectDef.searchParams = new ArrayList<>();
        else
            formSelectDef.searchParams = (List<String>) searchParams;
        formSelectDef.name = name;
        formSelectDef.queryName = queryName;
        formSelectDef.title = title;
        formSelectDef.decorator = decorator;
        formSelectDef.sortProperty = sortProperty;
        formSelectDef.sortOrder = sortOrder;
        formSelectDef.actionURL = actionURL;
        formSelectDef.actionMethod = actionMethod;
        formSelectDef.rowPerPage = rowPerPage;
        formSelectDef.controlInRow = controlInRow;
        formSelectDef.selects = selects;
        formSelectDef.controls = controls;
        formSelectDef.buttons = buttons;
        formSelectDef.selectDef = sqlModelManager.getSelectDef(queryName);
        fillFormSelectColumns(formSelectDefMap, formSelectDef);
        return formSelectDef;
    }

//    private static void fillPersistenceColumns(Map map, PersistenceDef persistenceDef) {
//        List<Map<String, String>> columnDefs = (List<Map<String, String>>)map.get("columns");
//        if(columnDefs == null || columnDefs.isEmpty()) {
//            persistenceDef.persistenceColumnDefMap = new LinkedHashMap<>();
//            persistenceDef.persistenceColumnDefs = new ArrayList<>();
//            return;
//        }
//        List<PersistenceColumnDef> columns = new ArrayList<>();
//        Map<String, PersistenceColumnDef> columnMap = new LinkedHashMap<>();
//        for(Map<String, String> columnDef : columnDefs) {
//            String property = columnDef.get("property");
//            String titleKey = columnDef.get("title-key");
//            String styleClass = columnDef.get("style-class");
//            String sortable = columnDef.get("sortable");
//            PersistenceColumnDef persistenceColumnDef = new PersistenceColumnDef(property, titleKey, styleClass, sortable != null && sortable.equalsIgnoreCase("true"));
//            columnMap.put(property, persistenceColumnDef);
//            columns.add(persistenceColumnDef);
//        }
//        persistenceDef.persistenceColumnDefMap = columnMap;
//        persistenceDef.persistenceColumnDefs = columns;
//    }

    public static FormModelManager getNewInstance(SQLModelManager sqlModelManager, InputStream ...formInputStreams) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, FormPersistDef> formInsertDefMap = new LinkedHashMap<>();
        Map<String, FormSelectDef> formSelectDefMap = new LinkedHashMap<>();
        for(InputStream is : formInputStreams) {
            String string = IOUtils.toString(is);
            List<Map> modelList = objectMapper.readValue(string, List.class);
            for(Map map : modelList) {
                if(sqlModelManager.hasInsertDef((String)map.get("query-name"))) {
                    FormPersistDef formPersistDef = getFormInsertDef(map);
                    formInsertDefMap.put(formPersistDef.name, formPersistDef);
                } else if(sqlModelManager.hasSelectDef((String)map.get("query-name"))) {
                    FormSelectDef formSelectDef = getFormSelectDef(sqlModelManager, map);
                    formSelectDefMap.put(formSelectDef.name, formSelectDef);
                } else if(sqlModelManager.hasUpdateDef((String)map.get("query-name"))) {

                }
            }
        }
        FormModelManager formModelManager = new FormModelManager(formInsertDefMap, formSelectDefMap);
        return formModelManager;
    }

    public static void main(String[] args) throws Exception {
        FormModelManager modelManager = FormModelManager.getNewInstance(null,
                FormModelManager.class.getResourceAsStream("/queries/fund-question-form.json"));
        FormManager formManager = FormManager.createFormManager(modelManager, null);
    }
}
