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
  pageContext.setAttribute("thisUrl", "tabs.jsp");
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.tabs.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.tabs.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.tabs.title']}"}"/>
</bbNG:pageHeader>
<bbNG:tabbedPanels collapsible="true">
	<bbNG:tabbedPanel title="Tab Panel 1">Content of <b>Tab Panel 1</b></bbNG:tabbedPanel>
	<bbNG:tabbedPanel title="Tab Panel 2">Content of Tab Panel 2</bbNG:tabbedPanel>
	<bbNG:tabbedPanel title="Tab Panel 3">Content of <i>Tab Panel 3</i></bbNG:tabbedPanel>
</bbNG:tabbedPanels>
<h2>Markup:</h2>
<pre>
&lt;bbNG:tabbedPanels collapsible="true"&gt;
	&lt;bbNG:tabbedPanel title="Tab Panel 1"&gt;Content of &lt;b&gt;Tab Panel 1&lt;/b&gt;&lt;/bbNG:tabbedPanel&gt;
	&lt;bbNG:tabbedPanel title="Tab Panel 2"&gt;Content of Tab Panel 2&lt;/bbNG:tabbedPanel&gt;
	&lt;bbNG:tabbedPanel title="Tab Panel 3"&gt;Content of &lt;i&gt;Tab Panel 3&lt;/i&gt;&lt;/bbNG:tabbedPanel&gt;
&lt;/bbNG:tabbedPanels&gt;
</pre>
</bbNG:genericPage>
