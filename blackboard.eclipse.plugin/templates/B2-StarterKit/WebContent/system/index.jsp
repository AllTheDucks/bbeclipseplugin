<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the bbNG:genericPage title" entitlement="system.admin.VIEW">
<bbNG:cssFile href="../css/b2sk.css" />
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.index.instructions']}"}">
    <bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      <bbNG:breadcrumb title="${r"${bundle['plugin.name']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.index.title']}"}"/>
</bbNG:pageHeader>

<h1>Table Of Contents:</h1>
<bbNG:tabbedPanels collapsible="false">
	<bbNG:tabbedPanel title="Getting Started">
<h2>Getting Started</h2>
	<ul>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="setup.jsp">Setup</a> First Time? Please Read!</li>	
		<li style="list-style-type:disc;list-style-position:inside;"><a href="about.jsp">About</a> Information about this building block</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="helloblackboard.jsp">Hello World!</a> (Without BB Taglibs)</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="helloblackboard-bbtags.jsp">Hello World!</a> (With BB Taglibs)</li>
	</ul>
	</bbNG:tabbedPanel>

	<bbNG:tabbedPanel title="Entry Point Examples">
<h2><a name="entryPoints" id="entryPoints"></a>Building Block Entry Point Examples</h2>
<p>Instructions for how to active each type of entry point included in the starter kit is below. When you activate the entry point you will see a page demostrating sample use and see the <b>bb-manifest.xml</b> markup required for activation.</p>
	<ul>
		<li style="list-style-type:disc;list-style-position:inside;"><b>System Tool:</b> (This page)</li>	
		<li style="list-style-type:disc;list-style-position:inside;"><b>Tool:</b> Open Any course =&gt; Tools =&gt; B2SK: Tool</li>	
		<li style="list-style-type:disc;list-style-position:inside;"><b>Course Tool:</b> Open Any Course =&gt; Control Panel =&gt; Course Tools =&gt; B2SK: Course Tool</li>
		<li style="list-style-type:disc;list-style-position:inside;"><b>User Tool:</b> Go To My Institution =&gt; Tools =&gt; B2SK: User Tool</li>
		<li style="list-style-type:disc;list-style-position:inside;"><b>Content Mashup:</b> Open Any Course =&gt; Content =&gt; Build Content =&gt; B2SK: Content Mashup</li>
		<li style="list-style-type:disc;list-style-position:inside;"><b>VBTE Mashup:</b> Open Any Course =&gt; Content =&gt; Build Content =&gt; Item =&gt; Turn Visual Editor on =&gt; Add Mashup =&gt; B2SK: VBTE Mashup</li>
	</ul>
	</bbNG:tabbedPanel>

	<bbNG:tabbedPanel title="Tag Library Examples">
<h2>Tag Library Examples (with Markup only - no functionality)</h2>
	<ul>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="formtags.jsp">Form tags</a> (bbNG tags for data collection)</li>	
		<li style="list-style-type:disc;list-style-position:inside;"><a href="tabs.jsp">Tabs</a> (bbNG:tabbedPanel)<li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="actionbar.jsp">Action Bars / Menus</a> (bbNG:actionControlBar)</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="search.jsp">Search</a> (bbNG:searchBar)<li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="list.jsp">Inventory List</a> (bbNG:inventoryList)</li>
	</ul>
	</bbNG:tabbedPanel>

	<bbNG:tabbedPanel title="Other Examples">
<h2>Other Examples</h2>
	<ul>
		<li style="list-style-type:disc;list-style-position:inside;"><b>User / Course Context / Template variables</b>: See <b>Tool</b> building Block Entry Point.</li>	
		<li style="list-style-type:disc;list-style-position:inside;"><a href="jquery.jsp">jQuery</a> Getting the jQuery javaScript library working in Blackboard.</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="throwerror.jsp">Error Handling</a> Making better pages for unhandled exceptions.</li>
	</ul>
	</bbNG:tabbedPanel>

	<bbNG:tabbedPanel title="End-to-End Demos">
<h2>End-to-End Working Demos</h2>
	<ul>
		<li style="list-style-type:disc;list-style-position:inside;"><b><a name="contentMashup" id="contentMashup"></a>Content Mashup Demo:</b> Open Any Course =&gt; Content =&gt; Build Content =&gt; B2SK: Content Mashup Demo</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="formdemo.jsp">Form Demo</a>: See how to do data collection using the BBtaglibs.</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="persistdemo.jsp">Data Persistence</a> (Using b2Context library)</li>
		<li style="list-style-type:disc;list-style-position:inside;"><a href="listdemo.jsp">Inventory List Demo</a>: A working bbNG:inventoryList example with Data Persistence</li>
		<li style="list-style-type:disc;list-style-position:inside;">TODO <a href=""></a>Search Demo: A working bbNG:searchBar demo</li>
	</ul>
	</bbNG:tabbedPanel>
</bbNG:tabbedPanels>
 
</bbNG:genericPage>
