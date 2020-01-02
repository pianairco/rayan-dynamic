<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>
<%@ include file="/taglibs.jsp" %>

<%
  String systemName = "";
  String subSystemName = "";
  String systemNameExtra = "";
  String s = (String) request.getAttribute("systemName");
  if(s != null){
    systemName = s;
  }
  s = (String) request.getAttribute("subSystemName");
  if (s != null) {
      subSystemName = s;
  }
  s = (String) request.getAttribute("systemNameExtra");
  if(s != null){
    systemNameExtra = s;
  }
%>
<table width="100%" border="0" cellspacing="0">
<tr width="100%" height="10">
  <td width="5%" vAlign=center align=middle>
    <a href="index.do">
      <img src="images/company.gif" border=0 height="60"> </a></td>
  <td width="50%" vAlign=top align="right" style="height: 95px;">
    <%--<img src="images/system_name.gif" border=0>--%>
    <style>
      @font-face { font-family: IranNastaliq; src: url('fonts/IranNastaliq.ttf');}
    </style>
    <div style="font-family: IranNastaliq;font-size: x-large">
      <%=systemName%>
    </div>
    <span style="font-size: larger;font-weight: bold;" >
      <%=subSystemName%>
    </span>
    <span style="font-size: x-small;margin-bottom: 2px" >
      <%=systemNameExtra%>
    </span>
  </td>
  <td width="40%" Valign="bottom" align="left">&nbsp;</td>
</tr>
<tr width="100%" class="navItem">
<td class="navItem" colspan="3" width="100%">
<table width="100%" border=0>
<tr width="100%">
<td width="1%">&nbsp;</td>
<td width="80%">
  &nbsp;
</td>
<td width="19%" align="left">&nbsp;</td>
</tr>
</table>
</td>
</tr>
</table>
<table border="1" align="right" width="100%">
  <%
      List appuserMessages = (List) request.getAttribute("appuserMessages");
      String cuurentUrl = (String) request.getAttribute("currentUrl");
      String params = new String("");
      Map parameters = (Map) request.getParameterMap();
      if (appuserMessages != null && appuserMessages.size() > 0) {
  %>
<tr>
    <td colspan="2">
        <table width="100%">
            <%
              for (Iterator iterator = appuserMessages.iterator(); iterator.hasNext();) {
                Map message = (Map) iterator.next();
                out.print("<tr><td align='right'>");
                out.print("<font color='#008000'>" + message.get("message_text")+ "</font>");
                out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
                out.print("<a href='index.do?" + "method=readMessage&messageId=" + message.get("message_id") + "&currentUrl=" + cuurentUrl + "'>مشاهده شد</a>");
                out.print("</td></tr>");
              }
            %>
        </table>
    </td>
</tr>
<%}%>
</table>