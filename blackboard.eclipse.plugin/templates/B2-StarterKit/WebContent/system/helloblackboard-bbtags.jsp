<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the &lt;bbNG:genericPage&gt; title property" entitlement="system.admin.VIEW">
<bbNG:cssFile href="../css/b2sk.css" />
<bbNG:jspBlock>
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
%>
</bbNG:jspBlock>
<bbNG:pageHeader instructions="This is the &lt;bbNG:pageHeader&gt; instructions property">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="index.jsp" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="&lt;bbNG:breadcrumb/&gt; title property" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="This is the &lt;bbNG:pageTitleBar&gt; title property"/>
</bbNG:pageHeader>
<h2>Page Content</h2>
	<p>Hello Blackboard World!</p> 
	<p><a href="helloblackboard.jsp">Without BB Taglibs</a></p>
</bbNG:genericPage>
