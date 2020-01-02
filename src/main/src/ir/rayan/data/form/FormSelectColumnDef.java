package ir.rayan.data.form;

/**
 * Created by mj.rahmati on 12/14/2019.
 */
public class FormSelectColumnDef {
    String property;
    String titleKey;
    String styleClass;
    boolean sortable;

    public FormSelectColumnDef(String property, String titleKey, String styleClass, boolean sortable) {
        this.property = property;
        this.titleKey = titleKey;
        this.styleClass = styleClass;
        this.sortable = sortable;
    }

    public String getProperty() {
        return property;
    }

    void setProperty(String property) {
        this.property = property;
    }

    public String getTitleKey() {
        return titleKey;
    }

    void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getStyleClass() {
        return styleClass;
    }

    void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isSortable() {
        return sortable;
    }

    void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
}
