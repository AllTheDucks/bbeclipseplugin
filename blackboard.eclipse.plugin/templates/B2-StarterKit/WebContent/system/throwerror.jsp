<%@ page language="java" contentType="text/html; charset=ISO-8859-1" errorPage="../error.jsp"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Throw an error!</title>
</head>
<body>
<%
	int x = 0;
	int y = 10;
	int z = y/x;   // This should barf!
%>
</body>
</html>