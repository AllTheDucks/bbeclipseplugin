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
  pageContext.setAttribute("thisUrl", "formtags.jsp");
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.formdemo.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.formtags.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.formtags.title']}"}"/>
</bbNG:pageHeader>

<bbNG:form action="${r"${cancelUrl}"}" method="GET" id="id_formdemo" name="demoForm" >
<bbNG:dataCollection markUnsavedChanges="true" showSubmitButtons="true" hasRequiredFields="true">
<bbNG:step title="${r"${bundle['page.system.formtags.breadcrumb']}"}" instructions="">

      <bbNG:dataElement label="bbNG:textElement">
        <bbNG:textElement name="demoTextelement" value=""  size="50" minLength="1" />
      	<pre>
&lt;bbNG:textElement name="demoTextelement" value=""  size="50" minLength="1" /&gt;
      	</pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:radioElement">
      	<bbNG:radioElement name="demoRadio" value="Yes"  isSelected="true" >Yes</bbNG:radioElement>
      	<bbNG:radioElement name="demoRadio" value="No">No</bbNG:radioElement>
      	<bbNG:radioElement name="demoRadio" value="Maybe">Maybe</bbNG:radioElement>
	    <pre>
&lt;bbNG:radioElement name="demoRadio" value="Yes"  isSelected="true" &gt;Yes&lt;/bbNG:radioElement&gt;
&lt;bbNG:radioElement name="demoRadio" value="No"&gt;No&lt;/bbNG:radioElement&gt;
&lt;bbNG:radioElement name="demoRadio" value="Maybe"&gt;Maybe&lt;/bbNG:radioElement&gt;
        </pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:checkboxElement">
        <bbNG:checkboxElement name="demoCheckbox" value="Checked" />
		<pre>
&lt;bbNG:checkboxElement name="demoCheckbox" value="Checked" /&gt;
      	</pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:selectElement">
        <bbNG:selectElement name="demoSelect" size="1" >
        	<bbNG:selectOptionElement value="Blackjack" optionLabel="Blackjack"/>
        	<bbNG:selectOptionElement value="Craps" optionLabel="Craps"/>
        	<bbNG:selectOptionElement value="Poker" optionLabel="Poker"/>
        	<bbNG:selectOptionElement value="Slots" optionLabel="Slots"/>
        </bbNG:selectElement>
        <pre>
&lt;bbNG:selectElement name="demoSelect" size="1" &gt;
	&lt;bbNG:selectOptionElement value="Blackjack" optionLabel="Blackjack"/&gt;
	&lt;bbNG:selectOptionElement value="Craps" optionLabel="Craps"/&gt;
	&lt;bbNG:selectOptionElement value="Poker" optionLabel="Poker"/&gt;
	&lt;bbNG:selectOptionElement value="Slots" optionLabel="Slots"/&gt;
&lt;/bbNG:selectElement&gt;
        </pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:datePicker">
        <bbNG:datePicker baseFieldName="demoDatepicker" />
        <pre>
&lt;bbNG:datePicker baseFieldName="demoDatepicker" /&gt;
        </pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:datePicker (timePicker)">
        <bbNG:datePicker baseFieldName="demoTimepicker" showTime="true" showDate="false"/>
        <pre>
&lt;bbNG:datePicker baseFieldName="demoTimepicker" showTime="true" showDate="false"/&gt;
        </pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:filePicker">
      	<bbNG:filePicker baseElementName="demoFilepicker" var="varFilepicker"></bbNG:filePicker>
      	<pre>
&lt;bbNG:filePicker baseElementName="demoFilepicker" var="varFilepicker"&gt;&lt;/bbNG:filePicker&gt;
      	</pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:colorPicker">
      	<bbNG:colorPicker name="demoColorpicker"/>
      	<pre>
&lt;bbNG:colorPicker name="demoColorpicker"/&gt;
      	</pre>
      </bbNG:dataElement>

      <bbNG:dataElement label="bbNG:textbox">
      	<bbNG:textbox name="demoTextbox"></bbNG:textbox>
      	<pre>
&lt;bbNG:textbox name="demoTextbox"&gt;&lt;/bbNG:textbox&gt;
      	</pre>
      </bbNG:dataElement>

</bbNG:step>
<bbNG:stepSubmit cancelUrl="${r"${cancelUrl}"}" showCancelButton="true">
<bbNG:stepSubmitButton label="Submit" />
</bbNG:stepSubmit>
</bbNG:dataCollection>
</bbNG:form>
</bbNG:genericPage>
