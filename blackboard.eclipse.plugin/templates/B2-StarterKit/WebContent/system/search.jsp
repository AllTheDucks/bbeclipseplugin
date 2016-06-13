<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context, 
				java.text.*,
				java.util.*"
				errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the &lt;bbNG:genericPage&gt; title property" entitlement="system.admin.VIEW">
<bbNG:cssFile href="../css/b2sk.css" />
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", "index.jsp");
  pageContext.setAttribute("thisUrl", "search.jsp");
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.search.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.search.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.search.title']}"}"/>
</bbNG:pageHeader>
<bbNG:searchBar>
	<bbNG:searchOption label="Basement" value="Basement"></bbNG:searchOption>
	<bbNG:searchOption label="Closet" value="Closet"></bbNG:searchOption>
	<bbNG:searchOption label="Shed" value="Shed"></bbNG:searchOption>
</bbNG:searchBar>
<h2>Markup:</h2>
<pre>
&lt;bbNG:searchBar&gt;
	&lt;bbNG:searchOption label="Basement" value="Basement"&gt;&lt;/bbNG:searchOption&gt;
	&lt;bbNG:searchOption label="Closet" value="Closet"&gt;&lt;/bbNG:searchOption&gt;
	&lt;bbNG:searchOption label="Shed" value="Shed"&gt;&lt;/bbNG:searchOption&gt;
&lt;/bbNG:searchBar&gt;
</pre>
</bbNG:genericPage>
