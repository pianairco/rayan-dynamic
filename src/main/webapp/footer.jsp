<%@ page import="java.sql.Date"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp"%>
<%
    String today = (String) session.getAttribute("today");
    String loginTime = (String) session.getAttribute("loginTime");
    if (today == null || loginTime == null) {
        Date d = new Date(session.getCreationTime());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        loginTime = sdf.format(d);
        session.setAttribute("today", today);
        session.setAttribute("loginTime", loginTime);
    }
    String yearName = "";
%>
<table width="100%" border="0" cellspacing="0">
<%--
    <tr>
        <td colspan="3">
            <table width="100%" border="1" cellspacing="0" cellpadding="0">
                <tr>
                    <%for (int i=0; i<10; i++){%>
                        <td height="10">
                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                <tr>
                                    <td height="10" bgcolor="E18888" align="center">&nbsp;</td>
                                    <td height="10" bgcolor="B9DC9A" align="center"><%=i%></td>
                                    <td height="10" bgcolor="B9DC9A" align="center">&nbsp;</td>
                                </tr>
                            </table>
                        </td>
                    <%}%>
                </tr>
            </table>
        </td>
    </tr>
--%>
    <tr width="100%" class="navItem">
        <td width="30%" class="navItem" dir="ltr" align="right">
            <font color="#afeeee">نسخه:</font>&nbsp;<span dir="rtl"></span>&nbsp;&nbsp;
            <font color="#afeeee"><bean:message key="loginDate"/>:</font>&nbsp;<%=today%>&nbsp;&nbsp;
            <font color="#afeeee"><bean:message key="loginTime"/>:</font>&nbsp;<%=loginTime%></td>
        <td width="40%" class="navItem" align="center">
            <font color="#afeeee">شركت رايان هم افزا</font>&nbsp;&nbsp;
            <!--<img height=14 width=16 src="images/VN.bmp" border=0> -->
        </td>
        <td width="30%" class="navItem" align="left">
            <font color="#afeeee"><bean:message key="activeFiscalYear"/>:</font>&nbsp;<%=yearName%>
        </td>
    </tr>
</table>
<%--#eeee00--%>