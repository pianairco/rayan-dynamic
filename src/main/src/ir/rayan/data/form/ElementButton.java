package ir.rayan.data.form;

/**
 * Created by mj.rahmati on 12/26/2019.
 */
public class ElementButton {
    String name;
    String title;
    String type;
    String returnUrl;

    public ElementButton(String name, String title, String type, String returnUrl) {
        this.name = name;
        this.title = title;
        this.type = type;
        this.returnUrl = returnUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
