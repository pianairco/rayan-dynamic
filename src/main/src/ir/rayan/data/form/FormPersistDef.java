package ir.rayan.data.form;

import java.util.List;

/**
 * Created by mj.rahmati on 12/4/2019.
 */
public class FormPersistDef implements FormDef {
    String name;
    String queryName;
    String title;
    String queryUpdateName;
    String queryInsertName;
    String idParam;
    int controlInRow;
    List<ElementControl> controls;
    List<ElementButton> buttons;
    List<ElementSelect> selects;

    public FormPersistDef() {
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

    public String getQueryUpdateName() {
        return queryUpdateName;
    }

    void setQueryUpdateName(String queryUpdateName) {
        this.queryUpdateName = queryUpdateName;
    }

    public String getQueryInsertName() {
        return queryInsertName;
    }

    void setQueryInsertName(String queryInsertName) {
        this.queryInsertName = queryInsertName;
    }

    public String getIdParam() {
        return idParam;
    }

    void setIdParam(String idParam) {
        this.idParam = idParam;
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
