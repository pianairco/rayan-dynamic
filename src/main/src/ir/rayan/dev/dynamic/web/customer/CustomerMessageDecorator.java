package ir.rayan.dev.dynamic.web.customer;

import ir.rayan.data.form.FormDef;
import ir.rayan.data.form.FormManager;
import org.displaytag.decorator.TableDecorator;

import java.util.Map;

/**
 * Created by mj.rahmati on 12/31/2019.
 */
public class CustomerMessageDecorator extends TableDecorator {
    private static final String DISPLAY_NAME = "/customerMessage@list";

    public String getQuestionId() {
        Map currentRowObject = (Map) getCurrentRowObject();
        return String.valueOf(currentRowObject.get("questionId"));
    }

    public String getCellPhone() {
        Map currentRowObject = (Map) getCurrentRowObject();
        FormDef formDef = FormManager.getFormManager().getFormDef(DISPLAY_NAME);
        String questionId = String.valueOf(currentRowObject.get("questionId"));
        StringBuffer link = new StringBuffer();
        link.append("<a href='")
                .append(formDef.getName().split("@")[0].concat(".do"))
                .append("?method=persist&questionId=")
                .append(questionId)
                .append("'>")
                .append(currentRowObject.get("cellPhone"))
                .append("</a>");

        return link.toString()  ;
    }

    public String getQuestionDesc() {
        Map currentRowObject = (Map) getCurrentRowObject();
        return String.valueOf(currentRowObject.get("questionDesc"));
    }

    public String getAnswerDesc() {
        Map currentRowObject = (Map) getCurrentRowObject();
        return String.valueOf(currentRowObject.get("answerDesc"));
    }
}
