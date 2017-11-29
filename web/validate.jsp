<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<jsp:useBean id="lgn"  class= "datashielder.Validate" scope="page" />
<jsp:setProperty name="lgn" property="*" />   


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="page.css"> 
    </head>
    <body>
        <table width = "100%" border = 0>
            <tr>
                <td> <jsp:include page="titlebar.jsp" />
            </tr>
            
            <tr bgcolor="#9999FF" align="right">
                <td >
                    &nbsp;
                </td>
            </tr>
            
            <%
            int id = lgn.isValid();
            if(id != -1)
            {
                session.setAttribute("uid", lgn.getUid());
                response.sendRedirect("userhome.jsp");
            }
            else
            {
            %>
            <tr>
                <td>
                    <h2>Login Failed</h2>
                    <a href="login.jsp">Click</a> here to Retry.
                </td>
            </tr>
            <%
            }
            %>
        </table>
    </body>
</html>
