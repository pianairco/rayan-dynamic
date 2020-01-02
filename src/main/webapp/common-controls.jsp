<%@ page import="org.apache.struts.util.MessageResources"%>
<%@ page import="java.util.Map" %>
<%@ page import="ir.rayan.data.form.*" %>
<%@ page import="ir.rayan.dev.dynamic.Consts" %>
<%@ page import="ir.rayan.dev.util.SearchUtils" %>
<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp"%>

<html dir="rtl">
<head>
    <link href="../styles/global.css" type=text/css rel=stylesheet>
    <link href="styles/tab.css" type=text/css rel=stylesheet>
    <script language="JavaScript" src="scripts/tab.js"></script>
    <script language="JavaScript" src="scripts/checkDigitAndDate.js"></script>
    <script language="JavaScript" src="scripts/NumberFormat.js"></script>
    <link href="styles/tab.css" type=text/css rel=stylesheet>
    <link href="styles/global.css" type=text/css rel=stylesheet>
</head>
<body >

<script language="javascript">
    function resetSearchPanel()
    {
        <%="document.forms[0].elements['search.branch.branchCode'].value = '';"%>
    }
</script>

<%
    String MESSGAE_KEY = "org.apache.struts.action.MESSAGE";
    MessageResources messageResources = (MessageResources) request.getSession().getServletContext().getAttribute(MESSGAE_KEY);
    FormDef formDef = (FormDef) request.getAttribute("form-def");
    FormManager formManager = (FormManager) request.getAttribute("form-manager");
    String title = "";
    String[] split = formDef.getName().split("@");
    String formActionUrl = split[0] + "?method=" + split[1] + "&persist=true";
%>

<script language="javascript">
    function resetSearchPanel()
    {
        <%
        for(ElementControl SQLParamDef : formDef.getControls()) {
            String d = "document.forms[0].elements['" + SQLParamDef.getName() + "'].value = '';";
        %>
        <%=d%>
        <%
        }
        %>
    }
</script>
<table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
    <tr width="100%">
        <td>
            <html:form target="" action="<%=formActionUrl%>">
                <table width="100%" class="content" border="0" style="border-collapse:collapse">
                    <tr class="VNformheader">
                        <td width="100%" colspan="2">
                            &nbsp;
                        </td>
                    </tr>
                    <tr class=caption valign=top align=center>
                        <td width="100%" align="left" colspan="2">
                            <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
                                <tr>
                                    <td>
                                        <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
                                            <tr>
                                                <td>
                                                    <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
                                                        <%
                                                            int i = formDef.getControlInRow();
                                                            for(ElementControl elementControl : formDef.getControls()) {
                                                        %>
                                                        <%
                                                            if(elementControl.getType().equals("hidden")) {
                                                        %>

                                                        <%=SearchUtils.getSearchHidden(elementControl.getName(), request)%>

                                                        <%
                                                                continue;
                                                            }
                                                        %>

                                                        <%
                                                            if(i == formDef.getControlInRow()) {
                                                        %>
                                                        <tr >
                                                            <%
                                                                }
                                                            %>

                                                            <td width="<%=35/formDef.getControlInRow()%>%" class="fieldLabelArea"><bean:message key="<%=elementControl.getTitle()%>"/>:</td>
                                                            <td width="<%=65/formDef.getControlInRow()%>%" class="fieldValueArea">
                                                                <%
                                                                    if(elementControl.getType().equals("select")) {
                                                                        Map<String, String> listMap = formManager.getListMap(
                                                                                request.getAttribute(elementControl.getItems()),
                                                                                elementControl.getOptionTitle(), elementControl.getOptionValue());
                                                                %>
                                                                <%=SearchUtils.getSearchSelect(elementControl.getName(), listMap, false, true, false, request)%>
                                                                <%
                                                                } else if(elementControl.getType().equals("date")) {
                                                                %>

                                                                <%=SearchUtils.getSearchInput(elementControl.getName(), "10", "maxlength=\"10\" onblur=\"setEndDate()\" onkeypress=\"return autoMask(this,event,'"+ Consts.DATE_MASK_STRING + "');\"", elementControl.isDisabled(), request)%>

                                                                <%
                                                                } else if(elementControl.getType().equals("number")) {
                                                                %>

                                                                <%=SearchUtils.getSearchInput(elementControl.getName(), "10", "onblur=\"setRemainderTo()\" onkeypress=\"return autoMask(this,event,'###############');\"", elementControl.isDisabled(), request)%>

                                                                <%
                                                                } else if(elementControl.getType().equals("string")) {
                                                                %>

                                                                <%=SearchUtils.getSearchInput(elementControl.getName(), "12", elementControl.isDisabled(), request)%>

                                                                <%
                                                                } else if(elementControl.getType().equals("text")) {
                                                                %>

                                                                <%=SearchUtils.getSearchInput(elementControl.getName(), "30", elementControl.isDisabled(), request)%>

                                                                <%
                                                                    }
                                                                %>
                                                            </td>

                                                            <%
                                                                if(--i <= 0) {
                                                                    i = formDef.getControlInRow();
                                                            %>
                                                        </tr>
                                                        <%
                                                            }
                                                        %>

                                                        <%
                                                            }
                                                        %>

                                                        <%
                                                            if(i != formDef.getControlInRow() && i > 0) {
                                                        %>
                                                        <td colspan="<%=2 * i%>" width="<%=100/formDef.getControlInRow()*i%>">&nbsp;</td>
                                                        </tr>
                                                        <%
                                                            }
                                                        %>
                                                        <tr class="VNformheader">
                                                            <td colspan="<%=2 * formDef.getControlInRow()%>" align=left>
                                                                <%
                                                                    for(ElementButton elementButton : formDef.getButtons()) {
                                                                %>

                                                                <%
                                                                    if(elementButton.getType().equals("submit")) {
                                                                %>
                                                                <html:submit styleClass="button" ><bean:message key="<%=elementButton.getTitle()%>"/></html:submit>&nbsp;
                                                                <%
                                                                } else if(elementButton.getType().equals("reset")) {
                                                                %>
                                                                <button onclick="resetSearchPanel()" type="button"><bean:message key="<%=elementButton.getTitle()%>"/></button>&nbsp;
                                                                <%
                                                                } else if(elementButton.getType().equals("redirect")) {
                                                                %>
                                                                <button onclick="location = 'forward.jsp?forward=<%=elementButton.getReturnUrl()%>'" type="button" onblur="setNextFocus('return')"><bean:message key="<%=elementButton.getTitle()%>"/></button>&nbsp;
                                                                <%
                                                                    }
                                                                %>

                                                                <%
                                                                    }
                                                                %>
                                                                    <%--<button onclick="resetSearchPanel()" type="button"><bean:message key="button.reset"/></button>&nbsp;--%>
                                                                    <%--<html:submit styleClass="button" ><bean:message key="button.save"/></html:submit>&nbsp;--%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>
</body>
</html>
<%--<html:javascript formName="customerMessageForm" staticJavascript="false" cdata="false"/>--%>
<%--<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>--%>