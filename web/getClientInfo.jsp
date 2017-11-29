<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page errorPage="err.jsp" %>

<html>
    <head>
        <link rel="stylesheet" href="page.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Get Client Information</title>
        <script language="javascript">
            
        function showError(divID, msg)
        {
            document.getElementById(divID).innerHTML= msg;
            return false;
        }
        
        function clearDiv(divID)
        {
            document.getElementById(divID).innerHTML="";
        }
        
        function chk()
        {
            var a,b,c,d,e,f,g, h;
            
            a = document.f.cname.value;
            b = document.f.cphone.value;
            c = document.f.caddress.value;
            d = document.f.cimage.value;
                
            e = document.f.username.value;
            f = document.f.userpass.value;
            g = document.f.repass.value;

            h = eval(document.f.flag.value);
            
            if(a.length === 0)
            {//clientName missing
                return showError("divName", "Client Name Missing");
            }
            else if(b.length === 0)
            {//clientPhone missing
                return showError("divUphone", "Client Phone Number Missing");
            }
            else if(b.length === 0)
            {//clientPhone missing
                return showError("divUphone", "Client Phone Number Missing");
            }
            else if(c.length === 0)
            {//clientAddress missing
                return showError("divUaddress", "Client Address Missing");
            }
            else if(d.length === 0)
            {//clientImage missing
                return showError("divUimage", "Client Image Missing");
            }
            else if(e.length === 0)
            {//username missing
                return showError("divUname", "User Number Missing");
            }
            else if(f.length === 0)
            {//password missing
                return showError("divPass", "Password Missing");
            }
            else if(g.length === 0)
            {//rentered password missing
                return showError("divRepass", "ReEnter Password");
            }
            else if(g.length < 6)
            {
                return showError("divRepass", "Password length should be min 6 characters");
            }
            else if(f !== g)
            {
                return showError("divRepass", "Password and ReEntered Password Mismatch ");
            }
            else if(b.length !== 10)
            {
                return showError("divUphone","Phone Number Should be 10 Digits in Length");
            }
            else if(isNaN(b))
            {
                return showError("divUphone","Phone Number Should be Numeric Value");
            }
            else if(h === 0)
            {
                return showError("divUname","User Name Not Available");
            }
            else
            {
                return true;
            }
        }
        
        var req;
        function checkAvailablity()
        {
            var un, url;
            un= document.getElementById("username").value;
            if(un.trim().length === 0)
                return showError("divUname", "UserName Missing");
            req = new XMLHttpRequest();
            url = "checkUserNameAvailability.jsp?username=" +un;
            req.open("GET", url, true);//synchronous
            req.onreadystatechange = serverResponse;
            req.send();
        }
        
        function serverResponse()
        {
            if(req.readyState === 4)
            {
                if(req.status === 200)
                {
                    var res = req.responseText;                    
                    var flag = eval(res);
                    if(flag === 1)
                    {//username available
                        document.getElementById("flag").value = "1";
                        document.getElementById("divUname").innerHTML= "UserName Available";
                    }
                    else
                    {//username not available
                        document.getElementById("flag").value = "0";
                        document.getElementById("divUname").innerHTML= "UserName Not Available";
                    }
                }//if(req.status == 200)
            }//if(req.readyState == 4)
        }//serverResponse()
            
        </script>
    </head>
    <body>
  <table width = 100%>
    <tr>
        <td colspan="2"> <jsp:include page="titlebar.jsp" /> </td>
    </tr>
    <tr>
        <td align="right" colspan="2" bgcolor = "#9999FF"> <a href ="index.jsp" > Home </a> </td>
    </tr>
    <tr>
        <td colspan="2" > <h2>Register Client</h2></td>
    </tr>

    <tr>
        <td align="center"><img src = register.jpg width="250" height="150"></td>
        <td width="60%" align = "center"> 
            
            <form name = f action="registerClient.jsp"  enctype="multipart/form-data" method = "post" onsubmit="return chk()">
                <table border="0" width="100%">
                    <tr>
                        <td>Name</td>
                        <td><input id = "name" type="text" name="cname" value="" onfocus="clearDiv('divName')" /></td>
                        <td><div id="divName"></div> </td>
                    </tr>
                    <tr>
                        <td>Phone</td>
                        <td><input id = "uphone" type="text" name="cphone" value="" onfocus="clearDiv('divUphone')"/></td>
                        <td><div id="divUphone"></div> </td>
                    </tr>                    
                    <tr>
                        <td>Address</td>
                        <td><input id ="uaddress" type="text" name="caddress" value="" onfocus="clearDiv('divUaddress')"/></td>
                        <td><div id="divUaddress"></div> </td>
                    </tr>
                    
                    <tr>
                        <td>Client Image</td>
                        <td><input id= "uimage" type="file" name="cimage" value="" onfocus="clearDiv('divUimage')"/></td>
                        <td><div id="divUimage"></div> </td>
                    </tr>

                    <tr>
                        <td>User Name</td>
                        <td><input id = "username" type="text" name="username" value="" onblur="checkAvailablity()" onfocus="clearDiv('divUname')"/></td>
                        <td><div id="divUname"></div> </td>
                    </tr>            
                    <tr>
                        <td>Password</td>
                        <td><input id = "userpass" type="password" name="userpass" value="" onfocus="clearDiv('divPass')"/></td>
                        <td><div id="divPass"></div> </td>
                    </tr>
                    <tr>
                        <td>ReEnter Password</td>
                        <td><input id = "repass" type="password" name="repass" value="" onfocus="clearDiv('divRepass')"/></td>
                        <td><div id="divRepass"></div> </td>
                        
                    </tr>
                    
                    <tr>
                        <td >&nbsp;</td>
                        <td><input type = "submit" value = "Register" name = "register" /></td>
                        <td>&nbsp;<input id= "flag" type = "hidden" value = "0" name = "flag" /></td>

                    </tr>
                </table>

            </form>
            </td>
        </tr>
    </table>
    </body>
</html>
