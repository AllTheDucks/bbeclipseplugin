<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context,
				edu.syr.ischool.b2sk.*, 
				java.text.*,
				java.util.*"
				errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the &lt;bbNG:genericPage&gt; title property" entitlement="system.admin.VIEW">
<bbNG:cssFile href="../css/b2sk.css" />
<%
	String ITEMLIST = "listdemo-item-list";
	String ITEMTEXT = "listdemo-item-text";
	String delimiter = ";";
	B2Context b2Context = new B2Context(request);
	String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
	pageContext.setAttribute("bundle", b2Context.getResourceStrings());
	pageContext.setAttribute("cancelUrl", "index.jsp");
	pageContext.setAttribute("thisUrl", "listdemo.jsp");
	pageContext.setAttribute("addUrl","listdemo.jsp?ADD=ADD");
	List<String> itemList = ListDemoLib.DeSerialize(b2Context.getSetting(ITEMLIST), delimiter);
	String addString = request.getParameter("ADD");
	Boolean addMode = (addString != null);
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.listdemo.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.listdemo.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.listdemo.title']}"}"/>
</bbNG:pageHeader>
<h1>Shopping List:</h1>
<% 
	// Show Form
	if (addMode) {

		if (request.getMethod().equalsIgnoreCase("POST")) {
			// Persist the item you added
			String newItem = request.getParameter(ITEMTEXT);
			itemList.add(newItem);		
			b2Context.setSetting(ITEMLIST,ListDemoLib.Serialize(itemList, delimiter));
			b2Context.persistSettings();
		    response.sendRedirect(pageContext.getAttribute("thisUrl") + "?inline_receipt_message=" + b2Context.getResourceString("page.system.listdemo.addsuccess"));
		}			
%>
<bbNG:form action="${r"${addUrl}"}" method="POST" id="id_addItemForm" name="addItemForm" >
<bbNG:dataCollection markUnsavedChanges="true" showSubmitButtons="true" hasRequiredFields="true">
<bbNG:step title="${r"${bundle['page.system.listdemo.step.title']}"}" instructions="${r"${bundle['page.system.listdemo.step.title']}"}">
      <bbNG:dataElement isRequired="true" label="${r"${bundle['page.system.listdemo.label.itemText']}"}">
        <bbNG:textElement name="<%=ITEMTEXT %>" value=""  size="50" minLength="1" />
      </bbNG:dataElement>
</bbNG:step>
<bbNG:stepSubmit cancelUrl="${r"${thisUrl}"}" showCancelButton="true">
<bbNG:stepSubmitButton label="Submit" />
</bbNG:stepSubmit>
</bbNG:dataCollection>
</bbNG:form>
<% } else { // Show inventoryList 
	 if (request.getMethod().equalsIgnoreCase("POST")) {
		   String[] itemsToDelete = request.getParameterValues("ckbox");
		   boolean success = false;
		   if (itemsToDelete!= null) 
		   {
		      for (int i = 0; i < itemsToDelete.length; i++) 
		      {
		    	 itemList.remove(itemsToDelete[i]);
		      }
				b2Context.setSetting(ITEMLIST,ListDemoLib.Serialize(itemList, delimiter));
				b2Context.persistSettings();
		   }
		    response.sendRedirect(pageContext.getAttribute("thisUrl") + "?inline_receipt_message=" + b2Context.getResourceString("page.system.listdemo.deletesuccess"));
		 }
%>
<p><bbNG:button url="${r"${addUrl}"}" label="Add..."></bbNG:button></p>
<bbNG:form action="" id="id_listForm" name="listForm" method="post" onsubmit="return validateForm();">
<bbNG:inventoryList collection="<%=itemList %>" objectVar="ix" className="String"> 
	<bbNG:listActionBar> 
	<bbNG:listActionItem title="Delete" id="removeListItem" url="javascript:deleteSelectedItem()" />
	</bbNG:listActionBar>           
        <bbNG:listCheckboxElement name="ckbox" value="<%=ix%>" />             
        <bbNG:listElement label="Item Name" name="item" isRowHeader="true"> 
          <%=ix%>
        </bbNG:listElement>           
</bbNG:inventoryList>
</bbNG:form>   
<bbNG:jsBlock>
<script language="javascript" type="text/javascript">
function deleteSelectedItem()
{
  var submitForDelete= confirm( "${r"${bundle['page.system.listdemo.delete']}"}" );
  if ( submitForDelete)
  {
    document.listForm.action.value='delete';
    document.listForm.submit();
  } 
}
</script>
</bbNG:jsBlock>
<% } // end if %>
</bbNG:genericPage>
