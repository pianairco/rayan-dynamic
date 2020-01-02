
// <input type=text name=ssn onkeypress="return autoMask(this,event, '###-##-####');">
// this will force #'s, not allowing alphas where the #'s are, and auto add -'s
  var _SARISSA_IS_IE = navigator.userAgent.toLowerCase().indexOf("msie") >-1 ?true : false ;
function sep3(vfield, hfield,event) {
    if (getKeyCode(event) != 9 && getKeyCode(event) != 16) {
        vfield.value = vfield.value.replace(/,/g, '');
        hfield.value = vfield.value;
        vfield.value = FormatNumberBy3(vfield.value);
    }
    return true;
}
                            
function sep3init(vfield) {
    vfield.value = FormatNumberBy3(vfield.value);
    //?>>
  var className = vfield.className;
  if (vfield.value == '0' && className != 'readonlyValueArea') {
    vfield.value = '';
  }
    //?<<
    return true;
}

function sep3initZeroRender(vfield) {
    vfield.value = FormatNumberBy3(vfield.value);
    //?>>
  var className = vfield.className;
		//?<<
    return true;
}

function sep3init2(vfield, hfield) {
    vfield.value = FormatNumberBy3(hfield.value);
    //?>>
    if (vfield.value == '0' && !vfield.readOnly) {
      vfield.value = '';
    }
    //?<<
    return true;
}

function sep3init3(vfield, hfield) {
    vfield.value = FormatNumberBy3(hfield.value);

    return true;
}

function autoMask(field, event, sMask, allowDot) {
    //var sMask = "**?##?####";

    if(getKeyCode(event) == 13)
        return true;

    var KeyTyped = String.fromCharCode(getKeyCode(event));
    var targ = getTarget(event);

    if(allowDot == 1 && getKeyCode(event) == 46 && targ.value.indexOf('.') == -1)
      return true;
    keyCount = targ.value.length;

		if (keyCount > sMask.length && ("####/##/##" == sMask || "##:##" == sMask))
		{
				return false;
		}
		else if (keyCount == sMask.length && "####/##/##" != sMask && "##:##" != sMask && getKeyCode(event) != 8 && getKeyCode(event) != 46)
		{
				return false;
		}

	if((sMask.charAt(keyCount+1) == ''))
    {
        return true;
    }

    if ((sMask.charAt(keyCount+1) != '#') && (sMask.charAt(keyCount+1) != 'A' ) && (sMask.charAt(keyCount+1) != '.' ) )
    {
        field.value = field.value + KeyTyped + sMask.charAt(keyCount+1);
        return false;
    }

    if (sMask.charAt(keyCount) == '*')
        return true;

    if (sMask.charAt(keyCount) == KeyTyped)
    {
        return true;
    }

    if ((sMask.charAt(keyCount) == '#') && isNumeric(KeyTyped))
        return true;

    if ((sMask.charAt(keyCount) == 'A') && isAlpha(KeyTyped))
       return true;

    if ((sMask.charAt(keyCount+1) == '?') )
    {
        field.value = field.value + KeyTyped + sMask.charAt(keyCount+1);
        return true;
    }
    if (KeyTyped.charCodeAt(0) < 32) return true;
        return false;

}
function getTarget(e) {
    // IE5
    if (e.srcElement) {
        return e.srcElement;
    }
    if (e.target) {
        return e.target;
    }
}

function getKeyCode(e) {
    //IE5
    // if (e.srcElement) {
    //     return e.keyCode
    // }
    // // NC5
    // if (e.target) {
    //     return e.which
    // }
    var x = e.which || e.keyCode;
    return x;
}

function isNumeric(c)
{
    var sNumbers = "01234567890";
    return sNumbers.indexOf(c) != -1;

}


function isFloat(c)
{
  var sNumbers = "..01234567890 ";
  for(var ic=0;ic<=c.length;ic++) {
    if (sNumbers.indexOf(c.substring(ic,ic+1)) == -1)
      return false;
  }
  return true;

}

function isAlpha(c)
{
    var lCode = c.charCodeAt(0);
    //        alert(lCode);
    //        if (lCode >= 65 && lCode <= 122 )
    if ((lCode >= 1575 && lCode <= 1740) || lCode == 32)
    {
        return true;
    }
    else
        return false;
}

function isPunct(c)
{
    var lCode = c.charCodeAt(0);
    if (lCode >= 32 && lCode <= 47 )
    {
        return true;
    }
    else
        return false;

}

function validateLong(val) {
    try{
    if (val == null)
        return false;
    var len = val.length;
    if (len > 0) {
        if (len <= 18) {
            for (i = 0; i < len; i++) {
                var ch = val.charAt(i);
                if (ch < '0' || ch > '9') {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
    }
    catch(e) {}
    return true;
}

function isValidJalaliDate(strValue) {
  var objRegExp = /1[2-4]\d\d\/(0[1-6]\/(0[1-9]|[12][0-9]|3[01]))|((0[7-9]|1[0-2])\/(0[1-9]|[12][0-9]|30))/;
  return objRegExp.test(strValue);
}

function isValidTime(strValue) {
  var objRegExp = /([01][0-9]|2[0-3])\:([0-5][0-9])/;
  return objRegExp.test(strValue);
}

    function checkNumericPlt()
    {

      var key_;
        if(_SARISSA_IS_IE)
        {
                  key_=event.keyCode;
                  if(key_ == 48){
                      event.keyCode=0;
                      return false;
                  }
                  if (key_<47 || ( key_ > (48+9) ))
                     {
                       event.keyCode=0;
                       return false;
                     }
                     else
                     {
                       return true;
                     }
        }
//        else
//        {
//                    key_=evt.which;
//                    if(key_ == 48){
//                      evt.preventDefault();
//                      return false;
//                    }else{
//                      if(key_==8 || key_ == 0 || key_==9 || key_==13)
//                      {
//                         return true;
//                      }
//                      if ( (key_<47) || (key_> ( 48 + 9 )) )
//                      {
//                         evt.preventDefault();
//                      }
//                    }
//        }
      }

function moneyCommaSep(ctrl) {
    var separator = ",";
    var int = ctrl.value.replace(new RegExp(separator, "g"), "");
    var regexp = new RegExp("\\B(\\d{3})(" + separator + "|$)");
    do {
        int = int.replace(regexp, separator + "$1");
    }
    while (int.search(regexp) >= 0)
    ctrl.value = int;
}

function mask(event, type) {
    var i = 0;
    var regexFarsi = new RegExp("[\u0600-\u06FF ]");
    var regexAccountNumber = new RegExp('[0-9-./]');
    var regexEnglish = new RegExp("[a-zA-Z ]");
    var regexNumber = new RegExp('[0-9]');
    var regex;

    if (event.shiftKey) {
        if (event.keyCode >= 33 && event.keyCode <= 40 || event.keyCode == 46)
            return false;
    }
    if (event.keyCode == 8 || event.keyCode == 9 || 32 <= event.keyCode && event.keyCode <= 38
        || 16 <= event.keyCode && event.keyCode <= 18 || event.keyCode == 27 || event.keyCode == 46)
        return true;
    for (; type[i];) {

        if (type[i] == "fa")
            regex = regexFarsi;
        if (type[i] == "en")
            regex = regexEnglish;
        if (type[i] == "an")
            regex = regexAccountNumber;
        if (type[i] == "num")
            regex = regexNumber;
        i++;
        var key = String.fromCharCode(event.charCode == event.which ? event.which : event.charCode);
        if (regex.test(key)) {
            return true;
        }
    }
    event.preventDefault();
    return false;
}
