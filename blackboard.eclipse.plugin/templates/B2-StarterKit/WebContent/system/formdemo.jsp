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
  pageContext.setAttribute("thisUrl", "formdemo.jsp");
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.formdemo.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.formdemo.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.formdemo.title']}"}"/>
</bbNG:pageHeader>
<% 	if (!request.getMethod().equalsIgnoreCase("POST")) {  // show the form%>
<bbNG:form action="${r"${thisUrl}"}" method="POST" id="id_formdemo" name="demoForm" >
<bbNG:dataCollection markUnsavedChanges="true" showSubmitButtons="true" hasRequiredFields="true">
<bbNG:step title="${r"${bundle['page.system.formdemo.step.title']}"}" instructions="${r"${bundle['page.system.formdemo.step.title']}"}">

      <bbNG:dataElement isRequired="true" label="${r"${bundle['page.system.formdemo.label.demoTextelement']}"}">
        <bbNG:textElement name="demoTextelement" value=""  size="50" minLength="1" />
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoRadio']}"}">
      	<bbNG:radioElement name="demoRadio" value="Yes"  isSelected="true" >Yes</bbNG:radioElement>
      	<bbNG:radioElement name="demoRadio" value="No">No</bbNG:radioElement>
      	<bbNG:radioElement name="demoRadio" value="Maybe">Maybe</bbNG:radioElement>
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoCheckbox']}"}">
        <bbNG:checkboxElement name="demoCheckbox" value="Checked" />
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoSelect']}"}">
        <bbNG:selectElement name="demoSelect" size="1" >
        	<bbNG:selectOptionElement value="Blackjack" optionLabel="Blackjack"/>
        	<bbNG:selectOptionElement value="Craps" optionLabel="Craps"/>
        	<bbNG:selectOptionElement value="Poker" optionLabel="Poker"/>
        	<bbNG:selectOptionElement value="Slots" optionLabel="Slots"/>
        </bbNG:selectElement>
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoDatepicker']}"}">
        <bbNG:datePicker baseFieldName="demoDatepicker" />
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoTimepicker']}"}">
        <bbNG:datePicker baseFieldName="demoTimepicker" showTime="true" showDate="false"/>
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoFilepicker']}"}">
      	<bbNG:filePicker baseElementName="demoFilepicker" var="varFilepicker"></bbNG:filePicker>
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoColorpicker']}"}">
      	<bbNG:colorPicker name="demoColorpicker"/>
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.formdemo.label.demoTextbox']}"}">
      	<bbNG:textbox name="demoTextbox"></bbNG:textbox>
      </bbNG:dataElement>

</bbNG:step>
<bbNG:stepSubmit cancelUrl="${r"${cancelUrl}"}" showCancelButton="true">
<bbNG:stepSubmitButton label="Submit" />
</bbNG:stepSubmit>
</bbNG:dataCollection>
</bbNG:form>

<% } else {  //show the form output 
	List<String> requestParameterNames = Collections.list((Enumeration<String>)request.getParameterNames());
%>
<h2>Form Output</h2>
<table border="1" cellpadding="2" cellspacing="0">
	<tr><th><b>Property</b></th><th><b>Value</b></th></tr>
	<% for(String p : requestParameterNames ) { %>
	<tr><td><%=p %></td><td><%=request.getParameter(p) %></td></tr>
	<% } %>	
</table>
<p><a href="${r"${thisUrl}"}">Let's do that again!</a>
<% } //end if %>
</bbNG:genericPage>
