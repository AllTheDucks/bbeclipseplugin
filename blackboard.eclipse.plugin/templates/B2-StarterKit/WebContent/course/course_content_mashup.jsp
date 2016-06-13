<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:learningSystemPage title="This is the bbNG:genericPage title" ctxId="ctx">
<bbNG:cssFile href="../css/b2sk.css" />
<bbNG:jspBlock>
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
%>
</bbNG:jspBlock>
<bbNG:pageHeader instructions="${r"${bundle['page.course.content.instructions']}"}">
    <bbNG:breadcrumbBar environment="COURSE" navItem="course_plugin_manage">
      <bbNG:breadcrumb title="${r"${bundle['plugin.name']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" 
	title="${r"${bundle['page.course.content.title']}"}"/>
</bbNG:pageHeader>
<h2>Information</h2>
<p>
	When accessing an individual course, you see this entry under the "Build Content" menu.<br/>
	This allows instructors and course builders to add content to a content area. <br/>
</p> 
<h2> Example (screenshot)</h2>
<img src="images/b2sk_course_content_mashup.png" alt="Course Tool" />
<h2>Configuration</h2>
<p>Here's the required configuration section inside the <b>bb-manifest.xml</b> required for this section.</p>
<pre>
&lt;!-- Ensemble Content Type Handler. --&gt;
&lt;content-handler&gt; 
	&lt;name value="B2SK - Course Content Mashup"/&gt;
	&lt;handle value= "resource/x-bb-b2sk-content-mashup"/&gt;
	&lt;http-actions&gt;
		&lt;create value="course/course_content_mashup.jsp"/&gt;
	&lt;/http-actions&gt;
	&lt;icons&gt;
		&lt;toolbar value="/images/b2sk_logo_12x12.png"/&gt;
		&lt;listitem value="images/b2sk_logo_12x12.png"/&gt;
	&lt;/icons&gt;
	&lt;types&gt;
		&lt;type&gt;
			&lt;action-type value="mashup"/&gt;
		&lt;/type&gt;
		&lt;type&gt;
			&lt;action-type value="image"/&gt;
		&lt;/type&gt;            
       &lt;/types&gt;             
&lt;/content-handler&gt;
</pre>
</bbNG:learningSystemPage>
