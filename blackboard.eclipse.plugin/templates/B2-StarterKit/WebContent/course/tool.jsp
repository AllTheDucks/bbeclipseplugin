<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@ page import="blackboard.platform.session.*,
				blackboard.data.user.*,
				blackboard.data.content.*,
				blackboard.data.course.*,
				blackboard.persist.*,
				java.text.*,
				java.util.*"
	errorPage="../error.jsp" %>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:learningSystemPage title="This is the bbNG:learningSystemPage title" ctxId="ctx">
<bbNG:cssFile href="../css/b2sk.css" />
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
  User currentUser = ctx.getUser();
  Course currentCourse = ctx.getCourse();
%> 
<bbNG:pageHeader instructions="${r"${bundle['page.course.tool.instructions']}"}">
    <bbNG:breadcrumbBar environment="COURSE" navItem="course_plugin_manage">
      <bbNG:breadcrumb title="${r"${bundle['plugin.name']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.course.tool.title']}"}"/>
</bbNG:pageHeader>
<h2>Information</h2>
<p>
	Tools appear as links from the "Tools" area of each course.<br/>
	They are available to students and instructors. <br />
	They are secured (enabled/disabled)by organization.
</p> 
<h2> Example (screenshot)</h2>
<img src="images/b2sk_tools.png" alt="Tools" />
<h2>Configuration</h2>
<p>Here's the required configuration section inside the <b>bb-manifest.xml</b> required for this section.</p>
<pre>
&lt;link&gt; &lt;!-- "Tools" Entry Point inside course --&gt; 
	&lt;type value="tool" /&gt;
	&lt;name value="B2SK: Tools" /&gt;
	&lt;url value="course/tool.jsp" /&gt;
	&lt;description value="B2SK Entry Point for a Building Block in the Tools Section" /&gt;
	&lt;icons&gt;
		&lt;listitem value="images/b2sk_logo_50x50.png"/&gt;
	&lt;/icons&gt;
&lt;/link&gt;
</pre>
<h2>Context Objects</h2>
<h3>User:</h3>
<table border="1" cellpadding="2" cellspacing="0">
	<tr><th><b>Property</b></th><th><b>Value</b></th></tr>
	<tr><td>UserName</td><td><%=currentUser.getUserName() %></td></tr>
	<tr><td>Email</td><td><%=currentUser.getEmailAddress() %></td></tr>
	<tr><td>GivenName</td><td><%=currentUser.getGivenName() %></td></tr>
	<tr><td>FamilyName</td><td><%=currentUser.getFamilyName() %></td></tr>
	<tr><td>Email</td><td><%=currentUser.getEmailAddress() %></td></tr>
	<tr><td>LastLoginDate</td><td><%=new SimpleDateFormat("MM/dd/yyyy").format(currentUser.getLastLoginDate().getTime()) %></td></tr>
	<tr><td>PortalRole</td><td><%=currentUser.getPortalRole() %></td></tr>
	<tr><td>Password</td><td><%=currentUser.getPassword() %></td></tr>
</table>
<h3>Course:</h3>
<table border="1" cellpadding="2" cellspacing="0">
	<tr><th><b>Property</b></th><th><b>Value</b></th></tr>
	<tr><td>CourseId</td><td><%=currentCourse.getCourseId() %></td></tr>
	<tr><td>Title</td><td><%=currentCourse.getTitle() %></td></tr>
	<tr><td>Description</td><td><%=currentCourse.getDescription() %></td></tr>
	<tr><td>CreatedDate</td><td><%=new SimpleDateFormat("MM/dd/yyyy").format(currentCourse.getCreatedDate().getTime()) %></td></tr>
	<tr><td>InstitutionName</td><td><%=currentCourse.getInstitutionName() %></td></tr>
</table>
<h3>Template Variables:</h3>
<table border="1" cellpadding="2" cellspacing="0">
	<tr><th><b>Property</b></th><th><b>Value</b></th></tr>
	<tr><td>course.course_id</td><td>@X@course.course_id@X@</td></tr>
	<tr><td>course.url</td><td>@X@course.url@X@</td></tr>
	<tr><td>course.pk_string</td><td>@X@course.pk_string@X@</td></tr>
	<tr><td>user.user_id</td><td>@X@user.user_id@X@</td></tr>
</table>
</bbNG:learningSystemPage>