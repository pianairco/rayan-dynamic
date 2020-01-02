/*
Author: Robert Hashemian
http://www.hashemian.com/

You can use this code in any manner so long as the author's
name, Web address and this disclaimer is kept intact.
********************************************************
Usage Sample:

<script language="JavaScript" src="http://www.hashemian.com/js/NumberFormat.js"></script>
<script language="JavaScript">
document.write(FormatNumberBy3("1234512345.12345", ".", ","));
</script>
*/

// function to format a number with separators. returns formatted number.
// num - the number to be formatted
// decpoint - the decimal point character. if skipped, "." is used
// sep - the separator character. if skipped, "," is used
function FormatNumberBy3(num, decpoint, sep) {
  // check for missing parameters and use defaults if so
  if (arguments.length == 2) {
    sep = ",";
  }
  if (arguments.length == 1) {
    sep = ",";
    decpoint = ".";
  }
  // need a string for operations
  num = num.toString();

  // negative numbers
  var sign = '+';
  if (num.charAt(0) == '-') {
      sign = '-';
      num = num.substring(1);
  }
  // separate the whole number and the fraction if possible
  a = num.split(decpoint);
  x = a[0]; // decimal
  y = a[1]; // fraction
  z = "";


  if (typeof(x) != "undefined") {
    // reverse the digits. regexp works from left to right.
    for (i=x.length-1;i>=0;i--)
      z += x.charAt(i);
    // add seperators. but undo the trailing one, if there
    z = z.replace(/(\d{3})/g, "$1" + sep);
    if (z.slice(-sep.length) == sep)
      z = z.slice(0, -sep.length);
    x = "";
    // reverse again to get back the number
    for (i=z.length-1;i>=0;i--)
      x += z.charAt(i);
    // add the fraction back in, if it was there
    if (typeof(y) != "undefined" && y.length > 0)
      x += decpoint + y;

    //sign
// Edited Mohammad
    if (sign == '-')
        x = '(' + x + ')';
//          x += sign;
  }
  return x;
}

var numbers = new Object();
var farsiNumbers = new Object();
numbers[0]=0;
numbers[1]=1;
numbers[2]=2;
numbers[3]=3;
numbers[4]=4;
numbers[5]=5;
numbers[6]=6;
numbers[7]=7;
numbers[8]=8;
numbers[9]=9;
numbers[10]=10;
numbers[11]=11;
numbers[12]=12;
numbers[13]=13;
numbers[14]=14;
numbers[15]=15;
numbers[16]=16;
numbers[17]=17;
numbers[18]=18;
numbers[19]=19;
numbers[20]=20;
numbers[21]=30;
numbers[22]=40;
numbers[23]=50;
numbers[24]=60;
numbers[25]=70;
numbers[26]=80;
numbers[27]=90;
numbers[28]=100;
numbers[29]=200;
numbers[30]=300;
numbers[31]=400;
numbers[32]=500;
numbers[33]=600;
numbers[34]=700;
numbers[35]=800;
numbers[36]=900;
numbers[37]=1000;
numbers[38]=1000000;
numbers[39]=1000000000;
numbers[40]=1000000000000;
numbers[41]=1000000000000000;
numbers[42]=1000000000000000000;

farsiNumbers[0]='صفر';
farsiNumbers[1]='یک';
farsiNumbers[2]='دو';
farsiNumbers[3]='سه';
farsiNumbers[4]='چهار';
farsiNumbers[5]='پنج';
farsiNumbers[6]='شش';
farsiNumbers[7]='هفت';
farsiNumbers[8]='هشت';
farsiNumbers[9]='نه';
farsiNumbers[10]='ده';
farsiNumbers[11]='يازده';
farsiNumbers[12]='دوازده';
farsiNumbers[13]='سيزده';
farsiNumbers[14]='چهارده';
farsiNumbers[15]='پانزده';
farsiNumbers[16]='شانزده';
farsiNumbers[17]='هفده';
farsiNumbers[18]='هيجده';
farsiNumbers[19]='نوزده';
farsiNumbers[20]='بيست';
farsiNumbers[21]='سي';
farsiNumbers[22]='چهل';
farsiNumbers[23]='پنجاه';
farsiNumbers[24]='شصت';
farsiNumbers[25]='هفتاد';
farsiNumbers[26]='هشتاد';
farsiNumbers[27]='نود';
farsiNumbers[28]='یکصد';
farsiNumbers[29]='دويست';
farsiNumbers[30]='سيصد';
farsiNumbers[31]='چهارصد';
farsiNumbers[32]='پانصد';
farsiNumbers[33]='ششصد';
farsiNumbers[34]='هفتصد';
farsiNumbers[35]='هشتصد';
farsiNumbers[36]='نهصد';
farsiNumbers[37]='هزار';
farsiNumbers[38]='ميليون';
farsiNumbers[39]='ميليارد';
farsiNumbers[40]='هزارميليارد';
farsiNumbers[41]='ميليون ميليارد';
farsiNumbers[42]='ميليارد ميليارد';

function getFarsiNumebr(number) {
    for (i=0;i<=42;i++) {
        if (numbers[i] == number) {
            return farsiNumbers[i];
        }
    }
}

function getX(x) {
    var c = 0;
    x = Math.trunc(x / 1000);
    while (x > 0) {
        c++;
        x = Math.trunc(x / 1000);
    }
    c = Math.pow(1000, c);
    return c;
}

function getNumberAsfarsi (number) {
    var fnstr;
    if (number >= 1000) {
        var s = getFarsiNumebr(number);
        if (s != null) {
            var sb = '';
            sb += "یک ";
            sb += s;
            return sb;
        }
    }
    return getNumberAsfarsiInternal(number);
}
function getNumberAsfarsiInternal (number) {
    var sb = '';
    var s = getFarsiNumebr(number);
    if (s != null) {
        sb += s;
        return sb;
    }
    if (number < 0) {
        sb += "-";
        sb += getNumberAsfarsiInternal(-number);
        return sb;
    } else if (number < 1000) {
        var n3 = (number < 100) ? 10 : 100;
        var n1 = number % n3;
        var n2 = number - n1;
        sb += getNumberAsfarsiInternal(n2);
        if (n1 > 0) {
            sb += ' و ' + getNumberAsfarsiInternal(n1);
        }
    } else {
        var n3 = getX(number);
        var n1 = number % n3;
        var n2 = Math.trunc(number / n3);
        sb += getNumberAsfarsiInternal(n2) + ' ' + getNumberAsfarsiInternal(n3);
        if (n1 > 0) {
            sb += ' و ' + getNumberAsfarsiInternal(n1);
        }
    }
    return sb;
}


