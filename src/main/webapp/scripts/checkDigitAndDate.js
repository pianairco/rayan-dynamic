function isDigit(digit) {
    var v = "0123456789";
    for (i_=0; i_ < digit.length; i_++)
        if (v.indexOf(digit.charAt(i_),0) == -1)
            return false
    return true;
}

function isDouble(digit) {
		var v = "0123456789.";
    for (i_=0; i_ < digit.length; i_++)
        if (v.indexOf(digit.charAt(i_),0) == -1)
            return false
    return true;
}

function valDate(DateObj,message)
{
    var inputDate = DateObj.value;

    //alert(inputDate);

    x = inputDate;
    while (x.substring(0,1) == ' ') x = x.substring(1);
    while (x.substring(x.length-1,x.length) == ' ') x = x.substring(0,x.length-1);

    inputDate = x;

    // TAKE THIS LINE OUT LATER

    NULLABLE = true;

    if( isEmpty(inputDate) && NULLABLE ) return true;
    if( isEmpty(inputDate) && !NULLABLE )
    {
        alert("لطفا "+ message +" را وارد كنيد");
        DateObj.focus();
        return false;
    }

    inputDate = inputDate.replace(/-/g, "/")

    var delimCount = 0;
    var startIndex = 0;
    while( (startIndex=inputDate.indexOf("/", startIndex))!=-1 )
    {	delimCount++;
        startIndex++;
    }
    if( delimCount != 0 && delimCount != 2 )
    {
        //        alert("مقدار وارد شده قابل قبول نيست\n.بايد تاريخ را به صورت روز/ماه/سال  وارد كنيد");
        alert(message + " صحیح نیست");
        DateObj.focus();
        return false;
    }

    var delim1 = inputDate.indexOf("/")
    var delim2 = inputDate.lastIndexOf("/")
    DateObj.value = inputDate;

    if( delim1!=-1 )
    {
        var yyyy = parseInt(inputDate.substring(0, delim1), 10);
        var mm = parseInt(inputDate.substring(delim1+1, delim2), 10);
        var dd = parseInt(inputDate.substring(delim2+1, inputDate.length), 10);

        var stryyyy = inputDate.substring(0, delim1);
        var strmm = inputDate.substring(delim1+1, delim2);
        var strdd = inputDate.substring(delim2+1, inputDate.length);

    }
    else
    {
//        alert("مقدار وارد شده قابل قبول نيست\n.بايد تاريخ را به صورت روز/ماه/سال  وارد كنيد");
        alert(message + " صحیح نیست");
        DateObj.focus();
        return false;
    }

    if(isNaN(mm))   { alert("ماه بايد يك عدد صحيح باشد"); DateObj.focus(); return false; }
    if(isNaN(dd))   { alert("روز بايد يك عدد صحيح باشد"); DateObj.focus(); return false; }
    if(isNaN(yyyy))	{ alert("سال بايد يك عدد صحيح باشد"); DateObj.focus(); return false;	}

    if( mm < 1 || mm > 12 )
    {
        alert(message + " صحیح نیست. " + " ماه بايد در محدوده 1 (فروردين) تا 12 (اسفند) وارد شود");
        DateObj.focus();
        return false;
    }
    if( dd < 1 || dd > 31 )
    {
        //alert("x= " + x + ",  y= " + y);
        alert(message + " صحیح نیست. " + " (روز بايد در محدوده 1 تا 31 وارد شود (با توجه به ماه مربوطه");
        DateObj.focus();
        return false;
    }
    //	if( yyyy < 100 )
    //	{
    //		yyyy += 1300;
    //	}
    //    if( yyyy < 10 || yyyy > 99 )
   	if( yyyy < 1000 || yyyy > 9999 )
    {
        alert(message + " صحیح نیست. " + " سال بايد يك عدد چهار رقمي باشد");
        DateObj.focus();
        return false;
    }

    var months = new Array("", "فروردين", "ارديبهشت", "خرداد", "تير", "مرداد", "شهريور", "مهر", "آبان", "آذر", "دي", "بهمن", "اسفند");

    if( (mm==1 || mm==2 || mm==3 || mm==4 || mm==5 || mm==6) && dd > 31)
    {
        alert(message + " صحیح نیست. " + months[mm] + " داراي 31 روز مي باشد");
        DateObj.focus();
        return false;
    }
    if( (mm==7 || mm==8 || mm==9 || mm==10 || mm==11 || mm==12) && dd > 30)
    {
        alert(message + " صحیح نیست. " + months[mm] + " داراي 30 روز مي باشد");
        DateObj.focus();
        return false;
    }

    if ( strmm.length < 2) strmm = "0" + strmm
    if ( strdd.length < 2) strdd = "0" + strdd

    DateObj.value = yyyy + "/" + strmm + "/" + strdd;
    return true;
}

function isEmpty(inputStr)
{
	if (inputStr == "" || inputStr == null)
		return true;
	return false;
}

function checkFiscalYear(fromDateName , toDateName , fiscalYearStart , fiscalYearEnd ,formNumber){
    var dateFrom = document.forms[formNumber].elements[fromDateName].value;
    var dateTo = document.forms[formNumber].elements[toDateName].value;
//    if (dateFrom==""){
//        document.forms[formNumber].elements[fromDateName].value = fiscalYearStart;
//    }
//    if (dateTo==""){
//        document.forms[formNumber].elements[toDateName].value = fiscalYearEnd;
//    }
    if(dateFrom!="" && dateTo!="" && dateTo<dateFrom){
        alert('"تاريخ از" بايد کوچکتر از "تاريخ تا" باشد');
        document.forms[formNumber].elements[fromDateName].focus();
        return false;
    };
     if(dateFrom!="" && dateFrom<fiscalYearStart){
         alert('"تاريخ از" بايد در محدوده سال مالی باشد');
         document.forms[formNumber].elements[fromDateName].focus();
         return false;
     };
     if(dateTo!="" && dateTo>fiscalYearEnd){
         alert('"تاريخ تا" بايد در محدوده سال مالی باشد');
         document.forms[formNumber].elements[toDateName].focus();
         return false;
     };
    return true;
}

function checkFiscalYear2(toDateName , fiscalYearStart , fiscalYearEnd ,formNumber){
    var dateTo = document.forms[formNumber].elements[toDateName].value;
     if(dateTo!="" && (dateTo>fiscalYearEnd || dateTo<fiscalYearStart)){
         alert('"تاريخ تا" بايد در محدوده سال مالی باشد');
         document.forms[formNumber].elements[toDateName].focus();
         return false;
     };
    return true;

}

function setThousandSeperator(number) {
    return number.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
}
