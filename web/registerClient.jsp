<%@page import="datashielder.DirectoryManager"%>
<%@page import="java.io.FileOutputStream"%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page errorPage="err.jsp" %>
<%@page import="java.io.*;" %>
<jsp:useBean class="datashielder.Client" id="clnt" scope="page" />


<%
    boolean flag = false;
    try
    {
        String tempFileName = datashielder.DirectoryManager.getTemporaryFileName();
        ServletInputStream sin = request.getInputStream();
        FileOutputStream fout = new FileOutputStream(tempFileName);
        int x;
        byte arr[] = new byte[512];

        while((x = sin.read(arr)) != -1)
        {
            fout.write(arr, 0, x);
        }
        fout.flush();
        fout.close();
        
        datashielder.FileParser fp = new datashielder.FileParser(tempFileName, "shared");
        String cname = fp.getFieldValue("cname");
        String cphone = fp.getFieldValue("cphone");
        String caddress = fp.getFieldValue("caddress");
        String username = fp.getFieldValue("username");
        String userpassword= fp.getFieldValue("userpass");
        
        flag =  (cname != null && cphone != null && caddress != null && username != null && userpassword != null);
        
        if(fp.extractFile() && flag)
        {
            File src = new File(fp.getAbsolutePathForOutputFile());
            File trgt = datashielder.ImageManager.setAsUserImage(username, src.getAbsolutePath());
            
            clnt.setCname(cname);
            clnt.setCphone(cphone);
            clnt.setCaddress(caddress);
            clnt.setCimage(trgt.getAbsolutePath());
            clnt.setUsername(username);
            clnt.setUserpass(userpassword);
            
            flag = clnt.addClient();
            
        }
        
    }
    catch(Exception ex)
    {
        flag = false;
    }
%>


<html>
    <head>
        <link rel="stylesheet" href="page.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Client Registration</title>

    </head>
    
    <body>
            <table width="100%">
                <tr>
                    <td colspan="2"> <jsp:include page="titlebar.jsp" />
                </tr>
                <tr align="right" bgcolor = "#9999FF">
                    <td > <jsp:include page="navbar.jsp"/> </td>
                </tr>
                <tr>
                    <td>
                    <%
                    if(flag)
                    {
                        out.println("<h2>Client Registered Successfully</h2>");
                        out.println("<h2><a href = login.jsp>login</a></h2>");
                    }
                    else
                    {
                        out.println("<h2>Client Registration Failed</h2>");
                    }
                %>

            </td>
        </tr>
    </table>
    </body>
</html>
