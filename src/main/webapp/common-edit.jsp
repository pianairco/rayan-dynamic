<%@ page import="ir.rayan.data.form.ElementControl"%>
<%@ page import="ir.rayan.data.form.FormPersistDef"%>
<%@ page import="ir.rayan.data.form.FormManager"%>
<%@ page import="org.apache.struts.util.MessageResources"%>
<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp"%>

<html dir="rtl">
<head>
    <link href="../styles/global.css" type=text/css rel=stylesheet>
    <link href="styles/tab.css" type=text/css rel=stylesheet>
    <script language="JavaScript" src="scripts/tab.js"></script>
    <script language="JavaScript" src="scripts/checkDigitAndDate.js"></script>
    <script language="JavaScript" src="scripts/NumberFormat.js"></script>
    <script language="JavaScript" src="scripts/autoMask.js"></script>
    <script language="JavaScript" src="scripts/popup.js"></script>
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
    FormPersistDef formDef = (FormPersistDef) request.getAttribute("form-def");
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
<tr id="dwindow" style="position:absolute;background-color:#EBEBEB;cursor:auto;left:0px;top:0px;display:none" onSelectStart="return false">
    <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
        <tr>
            <td>
                    <table width="100%" class="content" border="0" style="border-collapse:collapse">
                        <tr class="VNformheader">
                            <td width="20%">
                                <%
                                    if(formDef.getTitle() != null) {
                                        String pageTitle = formDef.getTitle();
                                %>
                                <h4 ><bean:message key="<%=pageTitle%>"/></h4>
                                <%
                                    }
                                %>
                            </td>
                            <td width="80%" align=left >
                                    <%--<button type="button" name="newBranch" onclick="location =<%=formSelectDef.getActionURL().concat("?method=executeCommonInsert-edit")%>"><bean:message key="branch.newBranch"/></button>&nbsp;&nbsp;--%>
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
                                                        <jsp:include page="common-controls.jsp"></jsp:include>
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
</tr>
</table>
</body>
</html>
<%--<html:javascript formName="customerMessageForm" staticJavascript="false" cdata="false"/>--%>
<%--<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>--%>