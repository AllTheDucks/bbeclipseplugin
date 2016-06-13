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
  pageContext.setAttribute("thisUrl", "list.jsp");
  List<String> exampleList = new ArrayList<String>();
  exampleList.add("Apples");
  exampleList.add("Banannas");
  exampleList.add("Cherries");
  exampleList.add("Dates");
  exampleList.add("Eggplant");
  exampleList.add("Figs");
  exampleList.add("Grapes");
  
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.list.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.list.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.list.title']}"}"/>
</bbNG:pageHeader>
 	<bbNG:inventoryList collection="<%=exampleList %>" objectVar="el" className="String"> 
 		<bbNG:listActionBar> 
			<bbNG:listActionItem title="Delete" id="removeListItem" url="javascript:deleteSelectedItem()" /> 
		</bbNG:listActionBar>           
          <bbNG:listCheckboxElement name="ckbox" value="<%=el%>" />             
          <bbNG:listElement label="Fruit Name" name="el" isRowHeader="true"> 
            <%=el%>
          </bbNG:listElement>           
          <bbNG:listElement label="Google Search" name="elsearch"> 
            <a target="_blank" href="http://www.google.com/search?q=<%=el%>"><%=el %></a>
          </bbNG:listElement>           
    </bbNG:inventoryList> 
<h2>Markup:</h2>
<pre>
&lt;bbNG:inventoryList collection="&lt;%=exampleList %&gt;" objectVar="el" className="String"&gt; 
	&lt;bbNG:listActionBar&gt; 
		&lt;bbNG:listActionItem title="Delete" id="removeListItem" url="javascript:deleteSelectedItem()" /&gt; 
	&lt;/bbNG:listActionBar&gt;           
	&lt;bbNG:listCheckboxElement name="ckbox" value="&lt;%=el%&gt;" /&gt;             
	&lt;bbNG:listElement label="Fruit Name" name="el" isRowHeader="true"&gt; 
		&lt;%=el%&gt;
	&lt;/bbNG:listElement&gt;           
	&lt;bbNG:listElement label="Google Search" name="elsearch"&gt; 
		&lt;a target="_blank" href="http://www.google.com/search?q=&lt;%=el%&gt;"&gt;&lt;%=el %&gt;&lt;/a&gt;
	&lt;/bbNG:listElement&gt;           
&lt;/bbNG:inventoryList&gt; 
</pre>
</bbNG:genericPage>
