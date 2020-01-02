<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>

<%@ include file="/taglibs.jsp"%>
<html dir="rtl">

<%
boolean hasFundLicense = true;
    String title = "";
//    CommonUtils.getPageTitle( request);
%>

<head>
 <title>
    <%if (hasFundLicense) {%>
      <%=title%>
    <%} else {%>
    کارگزاری
    <%}%>
  </title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <link href="${ctx}/styles/global.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/images/favicon.ico" rel="SHORTCUT ICON"/>
    <script type="text/javascript" src="${ctx}/scripts/global.js"></script>
    <script type="text/javascript">
        function getIndexOf(_keys_, str) {
            try {
                for (i = 0; i < _keys_.length; i++) {
                    if (_keys_[i] == str) {
                        return i;
                    }
                }
            } catch (e) {}
            return -1;
        }

        function disableForm(theform) {
            if (document.all || document.getElementById) {
                for (i = 0; i < theform.length; i++) {
                    var tempobj = theform.elements[i];
                    if (tempobj.type.toLowerCase() == "submit" || tempobj.type.toLowerCase() == "reset") {
                        tempobj.disabled = true;
                    }
                }
                return true;
            }
            return false;
        }
        function enableForm(theform) {
						if (document.all || document.getElementById) {
                for (i = 0; i < theform.length; i++) {
                    var tempobj = theform.elements[i];
                    if (tempobj.type.toLowerCase() == "submit" || tempobj.type.toLowerCase() == "reset") {
                        tempobj.disabled = false;
                    }
                }
                return true;
            }
            return false;
        }

        function returnfalse() {
          return false;
        }
    </script>
</head>

<body>

<table border="0" style="border-collapse: collapse" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td><tiles:insert attribute="header"/></td>
	</tr>
	<tr>
		<td align="center">
            <%@ include file="/messages.jsp"%>
            <tiles:insert attribute="body"/>
        </td>
	</tr>
	<tr>
		<td><tiles:insert attribute="footer"/></td>
	</tr>
</table>

</body>

</html>
