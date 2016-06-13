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
	String COLOR = "persistdemo-color";
	String FONTSIZE = "persistdemo-font-size";
	String SAMPLETEXT = "persistdemo-sample-text";	
	B2Context b2Context = new B2Context(request);
	String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
	pageContext.setAttribute("bundle", b2Context.getResourceStrings());
	pageContext.setAttribute("cancelUrl", "index.jsp");
	pageContext.setAttribute("thisUrl", "persistdemo.jsp");
	String fontSize = b2Context.getSetting(FONTSIZE);
	String sampleText = b2Context.getSetting(SAMPLETEXT);
	String color =  b2Context.getSetting(COLOR);
	color = color.isEmpty() ? "#000000" : color;
	fontSize = fontSize.isEmpty() ? "medium" : fontSize;
	sampleText = sampleText.isEmpty() ? "Enter Some Text" : sampleText;
	if (request.getMethod().equalsIgnoreCase("POST")) {
		color = request.getParameter(COLOR);
		fontSize = request.getParameter(FONTSIZE);
		sampleText = request.getParameter(SAMPLETEXT);
		b2Context.setSetting(COLOR,color);
		b2Context.setSetting(FONTSIZE,fontSize);
		b2Context.setSetting(SAMPLETEXT,sampleText);
		b2Context.persistSettings();			
	}
	Boolean isSmall = fontSize.compareTo("small")==0;
	Boolean isMedium  = fontSize.compareTo("medium")==0;
	Boolean isLarge  = fontSize.compareTo("large")==0;
			
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.persistdemo.instructions']}"}">
		<bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      		<bbNG:breadcrumb href="${r"${cancelUrl}"}" title="${r"${bundle['plugin.name']}"}" />
      		<bbNG:breadcrumb title="${r"${bundle['page.system.persistdemo.breadcrumb']}"}" />
    	</bbNG:breadcrumbBar>	    
	    <bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.persistdemo.title']}"}"/>
</bbNG:pageHeader>
<h1>Current Setting:</h1>
<p><span style="font-size: <%=fontSize %>; color: <%=color %>;"><%=sampleText %></span></p>

<bbNG:form action="${r"${thisUrl}"}" method="POST" id="id_persistdemo" name="demoForm" >
<bbNG:dataCollection markUnsavedChanges="true" showSubmitButtons="true" hasRequiredFields="true">
<bbNG:step title="${r"${bundle['page.system.persistdemo.step.title']}"}" instructions="${r"${bundle['page.system.persistdemo.step.title']}"}">

      <bbNG:dataElement isRequired="true" label="${r"${bundle['page.system.persistdemo.label.sampleText']}"}">
        <bbNG:textElement name="<%=SAMPLETEXT %>" value="<%=sampleText %>"  size="50" minLength="1" />
      </bbNG:dataElement>

      <bbNG:dataElement label="${r"${bundle['page.system.persistdemo.label.fontSize']}"}">
        <bbNG:selectElement name="<%=FONTSIZE %>" size="1" >
        	<bbNG:selectOptionElement value="small" optionLabel="small" isSelected="<%=isSmall %>"/>
        	<bbNG:selectOptionElement value="medium" optionLabel="medium" isSelected="<%=isMedium%>"/>
        	<bbNG:selectOptionElement value="large" optionLabel="large" isSelected="<%=isLarge%>"/>
        </bbNG:selectElement>
      </bbNG:dataElement>


      <bbNG:dataElement label="${r"${bundle['page.system.persistdemo.label.color']}"}">
      	<bbNG:colorPicker name="<%=COLOR %>" initialColor="<%=color %>"/>
      </bbNG:dataElement>

</bbNG:step>
<bbNG:stepSubmit cancelUrl="${r"${cancelUrl}"}" showCancelButton="true">
<bbNG:stepSubmitButton label="Submit" />
</bbNG:stepSubmit>
</bbNG:dataCollection>
</bbNG:form>
</bbNG:genericPage>
