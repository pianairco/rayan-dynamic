<%@ page language="java" errorPage="/error.jsp" contentType="text/html;charset=UTF-8" %>

<%@ include file="/taglibs.jsp"%>
<%
  boolean flagMenu = ((Boolean)request.getSession().getAttribute("flagMenu")).booleanValue();
%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta http-equiv="Content-Language" content="fa">
<title>Menu</title>
<style type="text/css">

    #dropmenudiv{
        position:absolute;
        border:1px #6495ed; /* taghire rang be kamrang */
        border-bottom-width: 0;
        font:normal 12px Tahoma;
        line-height:18px;
        z-index:100;
    }

    #dropmenudiv a{
        width: 100%;
        display: block;
        text-indent: 3px;
    /*border-bottom: 1px solid white;*/
        border-bottom: 1px ;
        padding: 1px 0;
        text-decoration: none;
    /*font-weight: bold;*/
        font-weight: normal;
    }

    #dropmenudiv a:hover{ /*hover background color*/
        background-color: #6495ed; /* taghire rang be kamrang */
    }

</style>


<script type="text/javascript">

/***********************************************
* AnyLink Drop Down Menu- © Dynamic Drive (www.dynamicdrive.com)
* This notice MUST stay intact for legal use
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

//Contents for menu 1
var menu1=new Array()

//------------------------------------------------------------------------
var menuwidth='165px' //default menu width
//var menubgcolor='blue'  //menu bgcolor
var menubgcolor='#004a95'  //menu bgcolor   taghir rang be porange
var disappeardelay=500  //menu disappear speed onMouseout (in miliseconds)
var hidemenu_onclick="yes" //hide menu when user clicks within menu?

/////No further editting needed

var ie4=document.all
var ns6=document.getElementById&&!document.all

if (ie4||ns6)
    document.write('<div id="dropmenudiv" style="visibility:hidden;width:'+menuwidth+';background-color:'+menubgcolor+'" onMouseover="clearhidemenu()" onMouseout="dynamichide(event)"></div>')

function getposOffset(what, offsettype){
    var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
    var parentEl=what.offsetParent;
    while (parentEl!=null){
        totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
        parentEl=parentEl.offsetParent;
    }
    return totaloffset;
}


function showhide(obj, e, visible, hidden, menuwidth){
    if (ie4||ns6)
        dropmenuobj.style.left=dropmenuobj.style.top=-500
    if (menuwidth!=""){
        dropmenuobj.widthobj=dropmenuobj.style
        dropmenuobj.widthobj.width=menuwidth
    }
    if (e.type=="click" && obj.visibility==hidden || e.type=="mouseover")
        obj.visibility=visible
    else if (e.type=="click")
        obj.visibility=hidden
}

function iecompattest(){
    return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function clearbrowseredge(obj, whichedge){
    var edgeoffset=0
    if (whichedge=="rightedge"){
        var windowedge=ie4 && !window.opera? iecompattest().scrollLeft+iecompattest().clientWidth-15 : window.pageXOffset+window.innerWidth-15
        dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
        if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
            edgeoffset=dropmenuobj.contentmeasure-obj.offsetWidth
    }
    else{
        var topedge=ie4 && !window.opera? iecompattest().scrollTop : window.pageYOffset
        var windowedge=ie4 && !window.opera? iecompattest().scrollTop+iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
        dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
        if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure){ //move up?
            edgeoffset=dropmenuobj.contentmeasure+obj.offsetHeight
            if ((dropmenuobj.y-topedge)<dropmenuobj.contentmeasure) //up no good either?
                edgeoffset=dropmenuobj.y+obj.offsetHeight-topedge
        }
    }
    return edgeoffset
}

function populatemenu(what){
    if (ie4||ns6)
        dropmenuobj.innerHTML=what.join("")
}


function dropdownmenu(obj, e, menucontents, menuwidth, xOffset){

    hideSelect();

    if (window.event) event.cancelBubble=true
    else if (e.stopPropagation) e.stopPropagation()
    clearhidemenu()
    dropmenuobj=document.getElementById? document.getElementById("dropmenudiv") : dropmenudiv
    populatemenu(menucontents)

    if (ie4||ns6){
        showhide(dropmenuobj.style, e, "visible", "hidden", menuwidth, xOffset)

        dropmenuobj.x=getposOffset(obj.parentNode, "left") - xOffset  //15
        dropmenuobj.y=getposOffset(obj, "top") + 6
        dropmenuobj.style.left=dropmenuobj.x-clearbrowseredge(obj, "rightedge")+"px"
        dropmenuobj.style.top=dropmenuobj.y-clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+"px"
    }

    return clickreturnvalue()
}

function clickreturnvalue(){
    if (ie4||ns6) return false
    else return true
}

function contains_ns6(a, b) {
    while (b.parentNode)
        if ((b = b.parentNode) == a)
            return true;
    return false;
}

function dynamichide(e){
    if (ie4&&!dropmenuobj.contains(e.toElement))
        delayhidemenu()
    else if (ns6&&e.currentTarget!= e.relatedTarget&& !contains_ns6(e.currentTarget, e.relatedTarget))
        delayhidemenu()
}

function hidemenu(e){
    if (typeof dropmenuobj!="undefined"){
        if (ie4||ns6)
            dropmenuobj.style.visibility="hidden"
        unhideSelect()
    }
}

function delayhidemenu(){
    if (ie4||ns6)
        delayhide=setTimeout("hidemenu()",disappeardelay)
}

function clearhidemenu(){
    if (typeof delayhide!="undefined")
        clearTimeout(delayhide)
}

if (hidemenu_onclick=="yes")
    document.onclick=hidemenu


function hideSelect(){
    for (j=0; j<document.forms.length; j++) {
        var theForm = document.forms[j]
        for(i=0; i<theForm.elements.length; i++){
            if(theForm.elements[i].type == "select-one" || theForm.elements[i].type == "select-multiple") {
                theForm.elements[i].style.visibility = "hidden";
            }
        }
    }
}
function unhideSelect(){
    for (j=0; j<document.forms.length; j++) {
        var theForm = document.forms[j]
        for(i=0; i<theForm.elements.length; i++){
            if(theForm.elements[i].type == "select-one" || theForm.elements[i].type == "select-multiple") {
                theForm.elements[i].style.visibility = "visible";
            }
        }
    }
}


</script>
</head>

<body dir=rtl>
<table border="0" cellpadding="2" cellspacing="0">
    <tr>
        <td align=right width="95" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'" >
            <a href="#" onClick="return dropdownmenu(this, event, menu1, '140px', 40)" onMouseout="delayhidemenu()">
                راهبري سيستم
            </a>
        </td>
        <td align=right width="80" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, menu2, '230px', 55)" onMouseout="delayhidemenu()">
                تلگرام
            </a>
        </td>
        <td align=right width="80" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu3, '140px', 55)" onMouseout="delayhidemenu()">
                اطلاعات پایه
            </a>
        </td>
        <td align=right width="70" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu4, '140px', 65)" onMouseout="delayhidemenu()">
                درخواست
            </a>
        </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu5, '150px', 50)" onMouseout="delayhidemenu()">
                دریافت و پرداخت
            </a>
        </td>
        <td align=right width="65" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu6, '210px', 100)" onMouseout="delayhidemenu()">
                گزارشات
            </a>
        </td>
        <td align=right width="65" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu7, '160px', 90)" onMouseout="delayhidemenu()">
                حسابداری
            </a>
        </td>
        <td align=right width="125" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu8, '140px',20)" onMouseout="delayhidemenu()">
                گزارشات حسابداری
            </a>
        </td>
        <td align=right width="75" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <%--<a href="#" onClick="return dropdownmenu(this, event, Menu9, '150px', 70)" onMouseout="delayhidemenu()">--%>
            <a href="#" onClick="return dropdownmenu(this, event, Menu9, '160px', 70)" onMouseout="delayhidemenu()">
                سبد سهام
            </a>
        </td>
        <td align=right width="75" onmouseover="this.style.backgroundColor='#6495ed'"
            onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu21, '160px', 70)" onMouseout="delayhidemenu()">
                سبد کالا
            </a>
        </td>
        <td align=right width="95" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
          <a href="#" onClick="return dropdownmenu(this, event, Menu10, '220px', 60)" onMouseout="delayhidemenu()">
              اوراق مشاركت
          </a>
      </td>
      <td align=right width="170" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
          <a href="#" onClick="return dropdownmenu(this, event, Menu11, '250px', 55)" onMouseout="delayhidemenu()">
              صندوق سرمایه گذاری مشترک
          </a>
      </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu12, '150px', 55)" onMouseout="delayhidemenu()">
                بورس كالا
            </a>
        </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu14, '150px', 55)" onMouseout="delayhidemenu()">
                قرارداد آتی
            </a>
        </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu15, '150px', 55)" onMouseout="delayhidemenu()">
تسهيلات
            </a>
        </td>
      <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
          <a href="#" onClick="return dropdownmenu(this, event, Menu13, '150px', 55)" onMouseout="delayhidemenu()">
كنترل صندوقها
          </a>
      </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
          <a href="#" onClick="return dropdownmenu(this, event, Menu19, '150px', 55)" onMouseout="delayhidemenu()">
            بانكداري اختصاصي
          </a>
        </td>
        <td align=right width="100" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu20, '150px', 55)" onMouseout="delayhidemenu()">
                صندوق های ETF
            </a>
        </td>
        <td align=right width="80" onmouseover="this.style.backgroundColor='#6495ed'" onmouseout="this.style.backgroundColor='#004a95'">
            <a href="#" onClick="return dropdownmenu(this, event, Menu22, '230px', 55)" onMouseout="delayhidemenu()">
                ثبت گروهی
            </a>
        </td>
    </tr>
</table>
</body>

</html>
