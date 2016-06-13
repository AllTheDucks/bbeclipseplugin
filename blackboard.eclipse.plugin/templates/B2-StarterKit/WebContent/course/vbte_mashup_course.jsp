<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the bbNG:genericPage title" ctxId="ctx">
<bbNG:cssFile href="../css/b2sk.css" />
<bbNG:jspBlock>
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
%>
</bbNG:jspBlock>
<bbNG:pageHeader instructions="${r"${bundle['page.course.vbte.instructions']}"}">
    <bbNG:breadcrumbBar environment="COURSE" navItem="course_plugin_manage">
      <bbNG:breadcrumb title="${r"${bundle['plugin.name']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" 
	title="${r"${bundle['page.course.vbte.title']}"}"/>
</bbNG:pageHeader>
<h2>Information</h2>
<p>
	When accessing the Visual Textbox Editor  (VBTE) inside content of an individual course, you see this entry under the "Add Mashup" icon in the editor toolbar.<br/>
	This is avaiable to anyone from inside the content editor.. <br/>
</p> 
<h2> Example (screenshot)</h2>
<img src="images/b2sk_course_vbte_mashup.png" alt="VBTE Mashup Course" />
<h2>Configuration</h2>
<p>Here's the required configuration section inside the <b>bb-manifest.xml</b> required for this section.</p>
<pre>
&lt;link&gt; &lt;!-- "VBTE Course Mashup" Entry Point page --&gt;
	&lt;type value="vtbe_mashup_course" /&gt;
	&lt;name value="B2SK VBTE Course Mashup" /&gt;
	&lt;url value="course/vbte_mashup_course.jsp?vtbe=true&amp;course_id=@X@course.pk_string@X@" /&gt;
	&lt;description value="B2SK Entry Point for a VBTE Course Mashup Building Block" /&gt;
&lt;/link&gt;       			
</pre>
</bbNG:genericPage>
