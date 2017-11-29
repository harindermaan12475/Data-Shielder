<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.io.*" %>
<%@page import="java.util.*" %>


<html>
    <head>
        <link rel="stylesheet" href="page.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Image</title>
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
    String uid = "";
    try
    {
        uid = session.getAttribute("uid").toString();
    }
    catch(Exception e)
    {}
    if(uid.length() == 0)
    {
         response.sendRedirect("login.jsp");
    }
    else
    {
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

            datashielder.FileParser fp = new datashielder.FileParser(tempFileName, uid);
      
            datashielder.ImageManager im = new datashielder.ImageManager();
            if(fp.extractFile())
            {
                File src = new File(fp.getAbsolutePathForOutputFile());
                datashielder.ImageManager.setAsUserImage(uid, src.getAbsolutePath());
      
                out.println("<h2> User Image Uplaoded SUCCESSFULLY </h2>");
            }
            else
             {
                out.println("<h2> User Image Uplaod FAILED </h2>");
            }  
        }
        catch(Exception ex)
        {
            out.println("\n File Upload Failed");
        }
        finally
        {
            datashielder.DirectoryManager.clearUploads(uid);
        }
       
    }
    %>
            </td>
        </tr>
    </table>
    </body>
</html>
