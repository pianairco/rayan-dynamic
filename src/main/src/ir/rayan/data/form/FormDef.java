package ir.rayan.data.form;

import java.util.List;

/**
 * Created by mj.rahmati on 12/28/2019.
 */
public interface FormDef {
    String getName();
    String getQueryName();
    String getTitle();
    List<ElementSelect> getSelects();
    int getControlInRow();
    List<ElementControl> getControls();
    List<ElementButton> getButtons();
}
