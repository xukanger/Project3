<%--
  Created by IntelliJ IDEA.
  User: yt
  Date: 2016/12/13
  Time: 21:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

</head>
<body>
<%=request.getAttribute("user")%>
<p>${user.name}</p>
</body>
</html>
