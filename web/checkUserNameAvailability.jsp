<jsp:useBean id="clnt" scope="page" class="datashielder.Client"/>
<jsp:setProperty name="clnt" property="username" param="username"/>            
<%
    if(clnt.isUserNameAvailable())
    {
        System.out.println("qwerty");
        out.println("1");
    }
    else
    {
        System.out.println("qwerty false");
        out.println("0");
    }

%>