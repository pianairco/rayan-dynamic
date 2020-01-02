package ir.rayan.dev.util;

import ir.rayan.dev.dynamic.Consts;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mj.rahmati on 12/31/2019.
 */
public class SearchUtils {
    public static String getSearchHidden(String name , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='hidden' name='" + name + "' value='" + value + "' />";
        return input;
    }

    public static String getSearchHidden(String name , HttpSession session) {
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='hidden' name='" + name + "' value='" + value + "' />";
        return input;
    }

    public static String getSearchHidden(String name ,String id , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='hidden' name='" + name + "' id ='"+ id +"' value='" + value + "' />";
        return input;
    }

    public static String getSearchInput(String name, String size , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='text' name='" + name +"' size='" + size +  "' value='" + value + "' />";
        return input;
    }

    public static String getSearchInput(String name, String size , String Extended, boolean isDisable, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name) != null ? session.getAttribute(name) + "" : "";
        String disabled = isDisable ? "disabled='true'" : "";
        String input = "<input type='text' name='" + name + "' id='" + name.replace(".", "_") + "' size='" + size + "' value='" + value + "' " + Extended + disabled + " />";
        return input;
    }

    public static String getSearchInput(String name, String size , String Extended , HttpServletRequest request) {
        return getSearchInput(name, size , Extended, false , request) ;
    }

    public static String getSearchHidden(String name, String size , String Extended , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='hidden' name='" + name +"' size='" + size +  "' value='" + value + "' " +Extended+ " />";
        return input;
    }

    public static String getSearchInput(String name, String size  , String styleclass , String readOnly , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='text' name='" + name +"' size='" + size +  "' value='" + value +"' class=\""+ styleclass +"\" readonly=\"" +readOnly +"\" />";
        return input;
    }

    public static String getSearchInput(String name,String id, String size  , String styleclass , String readOnly , HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) + "" : "";
        String input = "<input type='text' name='" + name +"' id='" + id +"' size='" + size +  "' value='" + value +"'  />";
        return input;
    }

    public static String getSearchInput(String name, String size, boolean disabled, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name)!=null ? session.getAttribute(name) +"" : "";
        String input = "<input type='text' name='" + name +"' size='" + size +  "' value='" + value + "'" ;
        if (disabled)
            input += " disabled='true'" ;
        input += " />";
        return input;
    }

    public static String getSearchSelect(String name,
                                         String collectionName ,
                                         String propertyName,
                                         String lablePropertyName,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request,
                                         String size) {
        return getSearchSelect(name, collectionName ,
                propertyName,
                lablePropertyName,
                hasNothing,
                hasAll,
                multiSelect,
                request,
                size,"","");
    }

    public static String getSearchSelect(String name,
                                         String collectionName ,
                                         String propertyName,
                                         String lablePropertyName,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request,
                                         String size, String otherOptions, String extended) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name) !=null ? session.getAttribute(name) + "" : "";
        StringBuffer select = new StringBuffer();
        if (multiSelect)
            select.append("<select multiple=\"true\" name=\"").append(name).append("\"") ;
        else
            select.append("<select name=\"").append(name).append("\"") ;
        if (!size.equals("")){
            select.append(" STYLE=\"width:" );
            select.append(size );
            select.append(";\"");
        }

        select.append(" ").append(extended).append(" ") ;
        select.append(">");
        if (hasAll)
            select.append("<option value=''>همه</option>") ;


        if (hasNothing){
            select.append("<option ");
            if (Consts.COMBO_NO_VALUE.equals(value)) {
                select.append(" selected ");
            }
            select.append(" value='").append(Consts.COMBO_NO_VALUE).append("'>هیچکدام</option>") ;
        }

        Collection items = (Collection) request.getAttribute(collectionName);
        if (items != null)
            for (Iterator iterator = items.iterator(); iterator.hasNext();) {
                Object o =  iterator.next();
                try {
                    String propertyValue = BeanUtils.getProperty(o , propertyName);
                    String lablePropertyValue = BeanUtils.getProperty(o , lablePropertyName);
                    String selected = "";
                    if (propertyValue!=null && propertyValue.equals(value)) {
                        selected = "selected";
                    }
                    select.append("<option value=\"").
                            append(propertyValue).
                            append("\" ").
                            append(selected).
                            append(">").
                            append(lablePropertyValue).
                            append("</option>");

                } catch (Exception e) {
                }
            }
        select.append(" ").append(otherOptions).append(" ") ;

        select.append("</select>") ;

        return select.toString() ;
    }

    public static String getSearchSelect(String name,
                                         String collectionName ,
                                         String propertyName,
                                         String lablePropertyName,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request) {
        return getSearchSelect(name,collectionName ,propertyName,lablePropertyName,hasNothing,hasAll,multiSelect,request,"");
    }

    public static String getSearchSelect(String name,
                                         Map items,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request) {
        return getSearchSelect(name,items,hasNothing,hasAll,multiSelect,request,"","");

    }

    public static String getSearchSelect(String name,
                                         Map items,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request,String size) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name) !=null ? session.getAttribute(name) + "" : "";

        StringBuffer select = new StringBuffer();
        if (multiSelect)
            select.append("<select multiple=\"true\" name=\"").append(name).append("\"") ;
        else
            select.append("<select name=\"").append(name).append("\"") ;
        if (!size.equals("")){
            select.append(" STYLE=\"width:" );
            select.append(size );
            select.append(";\"");
        }
        select.append(">");
        if (hasAll)
            select.append("<option value=''>همه</option>") ;
        if (hasNothing)
            select.append("<option value=''>هیچکدام</option>") ;

        for (Iterator iterator = items.keySet().iterator(); iterator.hasNext();) {
            String propertyValue = (String) iterator.next();
            try {
                String lablePropertyValue = (String) items.get(propertyValue);
                String selected = "";
                if (propertyValue!=null && propertyValue.equals(value)) {
                    selected = "selected";
                }
                select. append("<option value=\"").
                        append(propertyValue).
                        append("\" ").
                        append(selected).
                        append(">").
                        append(lablePropertyValue).
                        append("</option>");

            } catch (Exception e) {
            }
        }

        select.append("</select>") ;


        return select.toString() ;
    }

    public static String getSearchSelect(String name,
                                         Map items,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request,String size,String size2){
        return getSearchSelect(name, items, hasNothing, hasAll, multiSelect, request, size, size2,"");
    }


    public static String getSearchSelect(String name,
                                         Map items,
                                         boolean hasNothing,
                                         boolean hasAll,
                                         boolean multiSelect,
                                         HttpServletRequest request,String size,String size2, String extended) {
        HttpSession session = request.getSession();
        String value = session.getAttribute(name) !=null ? session.getAttribute(name) + "" : "";

        StringBuffer select = new StringBuffer();
        if (multiSelect)
            select.append("<select multiple=\"true\" name=\"").append(name).append("\"") ;
        else
            select.append("<select name=\"").append(name).append("\"") ;
        if (!size.equals("")){
            select.append(" STYLE=\"width:" );
            select.append(size );
            select.append(";\"");
        }
        if (!size2.equals("")){
            select.append(" STYLE=\"height:" );
            select.append(size2 );
            select.append(";\"");
        }
        select.append(" " + extended + " ");
        select.append(">");
        if (hasAll)
            select.append("<option value=''>همه</option>") ;
        if (hasNothing)
            select.append("<option value=''>هیچکدام</option>") ;

        for (Iterator iterator = items.keySet().iterator(); iterator.hasNext();) {
            String propertyValue = (String) iterator.next();
            try {
                String lablePropertyValue = (String) items.get(propertyValue);
                String selected = "";
                if (propertyValue!=null && propertyValue.equals(value)) {
                    selected = "selected";
                }
                select. append("<option value=\"").
                        append(propertyValue).
                        append("\" ").
                        append(selected).
                        append(">").
                        append(lablePropertyValue).
                        append("</option>");

            } catch (Exception e) {
            }
        }
        select.append("</select>") ;
        return select.toString() ;
    }
}
