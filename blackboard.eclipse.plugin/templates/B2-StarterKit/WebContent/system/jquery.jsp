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
  pageContext.setAttribute("thisUrl", "jquery.jsp");
  String jQueryPath = b2Context.getPath() + "js/jquery.min.js";
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.jquery.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.jquery.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.jquery.title']}"}"/>
</bbNG:pageHeader>
<bbNG:jsFile href="<%=jQueryPath %>"/>
<bbNG:button id="clickMe" label="Click Me!" url="#"/>
<bbNG:jsBlock>
	<script type="text/javascript">
		jQuery.noConflict();
		
		jQuery(document).ready(function(){
			jQuery("#clickMe").click(function() { 
				jQuery("#jqueryRocks").fadeToggle('slow');				
			});			
		});
	</script>
</bbNG:jsBlock>
<h1><span id="jqueryRocks">jQuery Rocks!</span>&nbsp;</h1>
<h2>Markup:</h2>
<pre>
&lt;bbNG:jsFile href="&lt;%=jQueryPath %&gt;"/&gt;
&lt;bbNG:button id="clickMe" label="Click Me!" url="#"/&gt;
&lt;bbNG:jsBlock&gt;
	&lt;script type="text/javascript"&gt;
		jQuery.noConflict();
		
		jQuery(document).ready(function(){
			jQuery("#clickMe").click(function() { 
				jQuery("#jqueryRocks").fadeToggle('slow');				
			});			
		});
	&lt;/script&gt;
&lt;/bbNG:jsBlock&gt;
&lt;h1&gt;&lt;span id="jqueryRocks"&gt;jQuery Rocks!&lt;/span&gt;&amp;nbsp;&lt;/h1&gt;
</pre>
</bbNG:genericPage>
