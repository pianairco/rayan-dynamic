<%@ page import="org.apache.struts.util.MessageResources"%>
<%@ page import="ir.rayan.data.form.*" %>
<%@ page import="ir.rayan.data.sql.SQLParamDef" %>
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
    FormManager formManager = (FormManager) request.getAttribute("form-manager");
    String title = "";
    FormSelectDef formDef = (FormSelectDef) request.getAttribute("form-def");
    String[] split = formDef.getName().split("@");
    String formActionUrl = split[0] + "?method=" + split[1] + "&persist=true";

    //>>edit start
    String sortPropertyParam = formDef.getSortProperty();
    String sortOrderParam = formDef.getSortOrder();
//    String sortHref = formDef.getActionURL().concat("?").concat("sort_mode=true&method=")
//            .concat(displayDef.getActionMethod()).concat("&");

    String[] splited = formDef.getName().split("@");
    String sortHref = split[0].concat(".do?").concat("method=").concat(splited[1]).concat("&sort_mode=true&");
    //<<edit end
    //--dont edit-->>
    String sortOrder = "asc";
    String sortedStyleClass = "";
    String columnStyleClass = "";
    String sortableStyleClass = "sortable";
    if(session.getAttribute(sortOrderParam) != null) {
        sortOrder = (String) session.getAttribute(sortOrderParam);
        if(sortOrder == null)
            sortOrder = "asc";
        if(sortOrder.equals("asc")) {
            sortedStyleClass = "order1 sortable sorted";
        } else {
            sortedStyleClass = "order2 sortable sorted";
        }
    }
    String sortProperty = "";
    if (session.getAttribute(sortPropertyParam) != null)
        sortProperty=(String) session.getAttribute(sortPropertyParam);
    String href = "";
    String commonHref = "<a href='"+sortHref+sortPropertyParam+"=";
    //--dont edit--<<
%>

<script language="javascript">
    function resetSearchPanel()
    {
        <%
        for(SQLParamDef SQLParamDef : formDef.getSearchParams()) {
            String d = "document.forms[0].elements['" + SQLParamDef.getKey() + "'].value = '';";
        %>
        <%=d%>
        <%
        }
        %>
    }
</script>

<table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
    <tr >
        <td  >
            <table width="100%" class="content" border="0" style="border-collapse:collapse">
                <tr class="VNformheader" >
                    <td width="20%" >
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
                        <%--<button type="button" name="newBranch" onclick="location =<%=formDef.getActionURL().concat("?method=executeCommonInsert-edit")%>"><bean:message key="branch.newBranch"/></button>&nbsp;&nbsp;--%>
                    </td>
                </tr>
                <tr class=caption valign=top align=center>
                    <td width="20%" class="vnformheader"  >
                        <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%" >
                            <tr>
                                <td>
                                    <input type="hidden" name="method" value="commonList"/>
                                    <input type="hidden" name="search" value="true"/>
                                    <table bgcolor="bbbbbb" border="0" cellpadding="1" cellspacing="0" width="100%">
                                        <tr>
                                            <td>
                                                <jsp:include page="common-controls.jsp"></jsp:include>
                                            </td>
                                        </tr>
                                    </table>
                                </td></tr>
                        </table>
                    </td>
                    <td width="80%" colspan=4 valign=top>
                        <%
                            if(formDef != null) { //#if formDef
                                Object pageSizeObject = request.getSession().getAttribute(formDef.getRowPerPage());
                                String pageSize = "30";
                                if(pageSizeObject != null)
                                    pageSize = (String) pageSizeObject;
                                String tableName = formDef.getQueryName() + "List";
                                String tableQuery = formDef.getQueryName();
//                                String tableQuery = formDef.getQueryName() + "Query";
                                String tableSize = formDef.getQueryName() + "Size";
                                String tableId = formDef.getQueryName() + "_table";
                                String decorator = formDef.getDecorator();
                        %>

                        <%
                            if(request.getAttribute(tableQuery) != null && request.getAttribute(tableName) == null) {
                        %>

                        <dtsource:jdbc pagesize="<%=new Long(pageSize)%>" id="<%=tableId%>" list="<%=tableName%>" sizelist="<%=tableSize%>" defaultsortName="" alias="" table="">
                            <%=request.getAttribute(tableQuery)%>
                        </dtsource:jdbc>

                        <%
                            }
                        %>

                        <display:table name="<%=tableName%>" requestURI="" class="list" id="<%=tableId%>" export="false"
                                       pagesize="30"  decorator="<%=decorator%>"
                                       cellspacing="1"  offset="0" cellpadding="2">

                            <%
                                for(FormSelectColumnDef displayColumnDef : formDef.getFormSelectColumnDefs())  { //#for displayColumnDef
                            %>

                            <%
                                if(displayColumnDef.isSortable()) {//#if displayColumnDef.isSortable()
                                    String newOrder = sortOrder == null ? "asc" : sortOrder.equalsIgnoreCase("asc") ? "desc" : "asc";
                                    href = commonHref + displayColumnDef.getProperty() + "&" + sortOrderParam + "=" + newOrder +  "'>" + messageResources.getMessage(displayColumnDef.getTitleKey()) + "</a>";
                                    columnStyleClass = sortProperty.equals(displayColumnDef.getProperty()) ? sortedStyleClass : sortableStyleClass;

                            %>

                            <display:column style="width:8%" property="<%=displayColumnDef.getProperty()%>" headerClass="<%=columnStyleClass%>" title="<%=href%>" />

                            <%
                            } //#if displayColumnDef.isSortable()
                            else {//#if !displayColumnDef.isSortable()
                            %>

                            <display:column style="width:8%" property="<%=displayColumnDef.getProperty()%>" title="<%=messageResources.getMessage(displayColumnDef.getTitleKey())%>" />

                            <%
                                }//#if !displayColumnDef.isSortable()
                            %>

                            <%
                                }//#for displayColumnDef
                            %>

                        </display:table>
                        <%
                            }//#if formDef
                        %>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

</body>
</html>
<%--<html:javascript formName="customerMessageForm" staticJavascript="false" cdata="false"/>--%>
<%--<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>--%>