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
  pageContext.setAttribute("thisUrl", "actionbar.jsp");
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.actionbar.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.actionbar.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.actionbar.title']}"}"/>
</bbNG:pageHeader>
<bbNG:actionControlBar>
	<bbNG:actionButton url="${r"${thisUrl}"}" title="Primary Action Button" primary="true"/>
	<bbNG:actionMenu title="Primary Action Menu" primary="true">
		<bbNG:actionMenuItem href="${r"${thisUrl}"}" title="P. Action MenuItem 1"/>
		<bbNG:actionMenuItem href="${r"${thisUrl}"}" title="P. Action MenuItem 2"/>
	</bbNG:actionMenu>
	<bbNG:actionButton url="${r"${thisUrl}"}" title="Secondary Action Button" primary="false"/>
	<bbNG:actionMenu title="Secondary Action Menu" primary="false">
		<bbNG:actionMenuItem href="${r"${thisUrl}"}" title="S. Action MenuItem 1"/>
		<bbNG:actionMenuItem href="${r"${thisUrl}"}" title="S. Action MenuItem 2"/>
	</bbNG:actionMenu>
	<bbNG:actionPanelButton type="SEARCH">Search Code Goes Here!</bbNG:actionPanelButton>
</bbNG:actionControlBar>
<h2>Markup:</h2>
<pre>
&lt;bbNG:actionControlBar&gt;
	&lt;bbNG:actionButton url="${r"${thisUrl}"}" title="Primary Action Button" primary="true"/&gt;
	&lt;bbNG:actionMenu title="Primary Action Menu" primary="true"&gt;
		&lt;bbNG:actionMenuItem href="${r"${thisUrl}"}" title="P. Action MenuItem 1"/&gt;
		&lt;bbNG:actionMenuItem href="${r"${thisUrl}"}" title="P. Action MenuItem 2"/&gt;
	&lt;/bbNG:actionMenu&gt;
	&lt;bbNG:actionButton url="${r"${thisUrl}"}" title="Secondary Action Button" primary="false"/&gt;
	&lt;bbNG:actionMenu title="Secondary Action Menu" primary="false"&gt;
		&lt;bbNG:actionMenuItem href="${r"${thisUrl}"}" title="S. Action MenuItem 1"/&gt;
		&lt;bbNG:actionMenuItem href="${r"${thisUrl}"}" title="S. Action MenuItem 2"/&gt;
	&lt;/bbNG:actionMenu&gt;
	&lt;bbNG:actionPanelButton type="SEARCH"&gt;Search Code Goes Here!&lt;/bbNG:actionPanelButton&gt;
&lt;/bbNG:actionControlBar&gt;
</pre>
</bbNG:genericPage>
