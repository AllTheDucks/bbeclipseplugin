<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@ page import="edu.syr.ischool.b2sk.*" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<%@ taglib uri="/bbData" prefix="bbData"%> 
<bbData:context id="ctx">
<bbNG:learningSystemPage title="This is the bbNG:genericPage title" ctxId="ctx">
<bbNG:cssFile href="../css/b2sk.css" />
<%
	B2Context b2Context = new B2Context(request);
	pageContext.setAttribute("bundle", b2Context.getResourceStrings());
	String ref = request.getHeader("referer");
	String courseId = ctx.getCourseId().toString();  
	String contentId = ctx.getContentId().toString();
	String userName = ctx.getUser().getUserName();
	
	// on post, write content
	if (request.getMethod().equalsIgnoreCase("POST")) {

		courseId = request.getParameter("course_id");
		contentId =  request.getParameter("content_id");
		String url = request.getParameter("url");
		String returnUrl = request.getParameter("http_ref");
		String embedHtml = "<iframe src=\"" + url +"\"></iframe>";	

		ContentCreator.createContent(url, embedHtml,courseId, contentId);
	    response.sendRedirect(returnUrl + "?inline_receipt_message=Content%20Added.");

	}

	
%>
<bbNG:pageHeader instructions="${r"${bundle['page.course.content.demo.instructions']}"}">
    <bbNG:breadcrumbBar environment="COURSE" navItem="course_plugin_manage">
      <bbNG:breadcrumb title="${r"${bundle['plugin.name']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" 
	title="${r"${bundle['page.course.content.demo.title']}"}"/>
</bbNG:pageHeader>
<bbNG:form action="content_mashup_demo.jsp" method="POST" id="id_searchForm" name="searchForm">
<bbNG:dataCollection markUnsavedChanges="true" showSubmitButtons="true">
<bbNG:step title="${r"${bundle['page.course.content.demo.step.title']}"}" 
instructions="${r"${bundle['page.course.content.demo.step.instructions']}"}">
		<input type="hidden" value="<%=courseId %>" name="course_id" /> 
		<input type="hidden" value="<%=contentId %>" name="content_id" />
		<input type="hidden" value="<%=ref %>" name="http_ref" />
      <bbNG:dataElement isRequired="true" label="${r"${bundle['page.course.content.demo.step.label']}"}">
        <bbNG:textElement name="url" value=""  size="50" minLength="1" />
      </bbNG:dataElement>
</bbNG:step>
<bbNG:stepSubmit>
<bbNG:stepSubmitButton label="Submit"/>
</bbNG:stepSubmit>
</bbNG:dataCollection>
</bbNG:form>
<h2>bbtags used in this demo:"</h2>
<pre>
&lt;bbData:context&gt;
&lt;bbNG:learningSystemPage&gt;
&lt;bbNG:dataCollection&gt;
&lt;bbNG:step&gt;
&lt;bbNG:dataElement&gt;
&lt;bbNG:textElement&gt;
</pre>
</bbNG:learningSystemPage>
</bbData:context>
