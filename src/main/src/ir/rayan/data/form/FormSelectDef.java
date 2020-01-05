package ir.rayan.data.form;

import ir.rayan.dev.data.sql.SQLParamDef;
import ir.rayan.dev.data.sql.SelectDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by mj.rahmati on 12/14/2019.
 */
public class FormSelectDef implements FormDef {
    String name;
    String queryName;
    String title;
    SelectDef selectDef;
    String decorator;
    String sortProperty;
    String sortOrder;
    String actionURL;
    String actionMethod;
    String rowPerPage;
    List<String> searchParams;
    List<SQLParamDef> searchSQLParamDefs;
    Map<String, FormSelectColumnDef> formSelectColumnDefMap;
    List<FormSelectColumnDef> formSelectColumnDefs;
    int controlInRow;
    List<ElementSelect> selects;
    List<ElementControl> controls;
    List<ElementButton> buttons;

    public FormSelectDef() {
    }

    public FormSelectDef(String queryName) {
        this.queryName = queryName;
    }

    public FormSelectDef(SelectDef selectDef) {
        this.queryName = selectDef.getName();
        this.selectDef = selectDef;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getQueryName() {
        return queryName;
    }

    @Override
    public String getTitle() {
        return title;
    }

    void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDecorator() {
        return decorator;
    }

    void setDecorator(String decorator) {
        this.decorator = decorator;
    }

    public Map<String, FormSelectColumnDef> getFormSelectColumnDefMap() {
        return Collections.unmodifiableMap(formSelectColumnDefMap);
    }

    void setFormSelectColumnDefMap(Map<String, FormSelectColumnDef> formSelectColumnDefMap) {
        this.formSelectColumnDefMap = formSelectColumnDefMap;
    }

    public List<FormSelectColumnDef> getFormSelectColumnDefs() {
        return Collections.unmodifiableList(formSelectColumnDefs);
    }

    void setFormSelectColumnDefs(List<FormSelectColumnDef> formSelectColumnDefs) {
        this.formSelectColumnDefs = formSelectColumnDefs;
    }

    public String getSortProperty() {
        return this.selectDef.getParamMap().get(sortProperty).getKey();
    }

    void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    public String getSortOrder() {
        return this.selectDef.getParamMap().get(sortOrder).getKey();
    }

    void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getActionURL() {
        return actionURL;
    }

    void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }

    public String getActionMethod() {
        return actionMethod;
    }

    void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getRowPerPage() {
        return this.selectDef.getParamMap().get(rowPerPage).getKey();
    }

    void setRowPerPage(String rowPerPage) {
        this.rowPerPage = rowPerPage;
    }

    public List<SQLParamDef> getSearchParams() {
        if(searchSQLParamDefs == null) {
            List<SQLParamDef> searchParams = new ArrayList<>();
            for (String searchParam : this.searchParams) {
                if (selectDef.getParamMap().containsKey(searchParam))
                    searchParams.add(selectDef.getParamMap().get(searchParam));
            }
            searchSQLParamDefs = Collections.unmodifiableList(searchParams);
        }
        return searchSQLParamDefs;
    }

    public int getControlInRow() {
        return controlInRow;
    }

    public void setControlInRow(int controlInRow) {
        this.controlInRow = controlInRow;
    }

    public List<ElementSelect> getSelects() {
        return selects;
    }

    void setSelects(List<ElementSelect> selects) {
        this.selects = selects;
    }

    public List<ElementControl> getControls() {
        return controls;
    }

    void setControls(List<ElementControl> controls) {
        this.controls = controls;
    }

    public List<ElementButton> getButtons() {
        return buttons;
    }

    void setButtons(List<ElementButton> buttons) {
        this.buttons = buttons;
    }
}
