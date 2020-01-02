<%--Author : Mohsen Kashi--%>
<%--Date : 8 Esfand 1391 --%>

<%@ tag import="org.apache.struts.taglib.TagUtils" %>
<%@ tag import="org.apache.struts.taglib.html.Constants" %>

<%@tag description="This tag file used for autocomplete search , with usage of AJAX functionality <br/>
In this tag 2 textboxes exists,result text box means main text box that shows in page, search text box is the text box in popup <br/>
<br/>
<strong>REQUIMENT for using this tag</strong><br/>
<b>1.</b> a method in action class: You should passing it to this tag via base_url,
    in this method you should generate a JSON and send it to client.<br/>
    remember in result JSON one field of search_response is SEARCH_RESULT_STRING that will be showed as result.   <br/>
    searched phrase in text box is available in searched_phrase parameter, You can use it in your action class.
    <br/>
<b>2.</b>  one javascript method in jsp, that sets selected object values to your form. You should set it in setter_function attribute.<br/>
    This method has 2 parameter, in first jsp recieve id of selector, and in second parameter JavaScript Object of selected item"

       pageEncoding="UTF-8" %>

<%@attribute name="popup_id" required="true"
             description="id of selector tag, if there is more than one selector in one JSP page you should set every ID uniquely<br>
             <strong>IT SHOULD BE DIFFERENT WITH result_text_box_name</strong>" %>
<%@attribute name="result_text_box_size" required="false"
             description='size attribute of result textBox, default value is 30' %>
<%@attribute name="result_text_box_name" required="true"
             description="name and id of result text box, you can use it in your code, such as validation" %>
<%@attribute name="result_text_box_value" required="false" description='text box value. default value is : <br>
<b>session.getAttribute(result_text_box_name)</b>' %>
<%@attribute name="setter_function" required="true"
             description="the javascript function that will be call after selecting item" %>
<%@attribute name="ajax_type" required="false"
             description="'POST' or 'GET', remember that GET type caches data for reuse, default is POST" %>
<%@attribute name="base_url" required="true" rtexprvalue="true"
             description="which method in which action class generate response?!,this is a javascript var" %>
<%@attribute name="popup_title" required="false"
             description="default: جستجو" %>
<%@attribute name="disabled" required="false" %>
<%@attribute name="div_id" required="false" %>
<%@attribute name="style_class" required="false" %>


<script type='text/javascript' src='scripts/selector.js?version=4'></script>
<%
    //setting default values
    if (result_text_box_size == null) {
        result_text_box_size = "30";
    }
    if (ajax_type == null) {
        ajax_type = "POST";
    }
    if (popup_title == null) {
        popup_title = "جستجو";
    }
    if (result_text_box_value == null) {
        if (request.getAttribute(result_text_box_name) != null)
            result_text_box_value = request.getAttribute(result_text_box_name).toString();
        else if (session.getAttribute(result_text_box_name) != null)
            result_text_box_value = session.getAttribute(result_text_box_name).toString();
        else {
            try {
                result_text_box_value = TagUtils.getInstance().lookup((PageContext) jspContext, Constants.BEAN_KEY, result_text_box_name, null).toString();
            } catch (Exception e) {
                if (request.getParameter(result_text_box_name) != null)
                    result_text_box_value = request.getParameter(result_text_box_name);
                else
                    result_text_box_value = "";
            }
        }
    }
    boolean isDisable = "true".equalsIgnoreCase(disabled) || "".equals(disabled);

%>

<%--main text box--%>
<div <%=div_id == null ? "" : "id=" + div_id%>>
    <input type="text" readonly="true" class="<%=style_class==null?"readonlyValueArea":style_class%>"
           name="<%=result_text_box_name%>"
           value="<%=result_text_box_value%>" size="<%=result_text_box_size%>"
            <%if (!isDisable) {%>
           onfocus="showSearchPopUp('<%=popup_id%>');"
            <%}%>
           style="position:static;" id="<%=result_text_box_name%>"/>
    <%if (!isDisable) {%>
    <img border="0" src="images\ico-search.gif" alt='<%=popup_title%>' title='<%=popup_title%>' onclick="toggleSearchPopUp('<%=popup_id%>');"/>
    <img border="0" src="images\Clear.gif" alt="پاک کردن" title="پاک کردن"
         onclick="<%=setter_function%>('<%=popup_id%>',undefined);  this.parentNode.children[0].value='';  hideSearchPopUp('<%=popup_id%>');"/>
    <%}%>
</div>
<%if (!isDisable) {%>
<%--popup for searching--%>
<div id='<%=popup_id%>' style="display:none; " class="search_popup">
    <div id='banner_div' style="overflow: auto; height: 16px;">
        <strong style=" float:right;">
            <%=popup_title%>
        </strong>
        <img border="0" src="images\Cancel.gif" alt="بستن" title="بستن" onclick="hideSearchPopUp('<%=popup_id%>');" align="left"/>
        <img border="0" src="images\loading.gif" alt="در حال جستجو" title="در حال جستجو" style="display:none;  " align="left"
             name="loading"/>
    </div>
    <div id='search_div' style="clear: both;">
        <input type="text" autocomplete="off" size="<%=result_text_box_size%>"
               onkeypress="if(event.keyCode == 13) {return false;}" name="selector_search_text_box"
               onkeyup="onSearchTxtBxKeyUp(this,event,<%=base_url%>,'<%=ajax_type%>',<%=setter_function%>)"/>
    </div>
    <div id='result_div'>
        <%--result goes here--%>
    </div>
</div>
<%}%>
