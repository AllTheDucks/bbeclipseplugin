<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.spvsoftwareproducts.blackboard.utils.B2Context" errorPage="../error.jsp"%>
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the bbNG:genericPage title" entitlement="system.admin.VIEW">
<bbNG:cssFile href="../css/b2sk.css" />
<%
  B2Context b2Context = new B2Context(request);
  String cancelUrl = b2Context.getNavigationItem("admin_plugin_manage").getHref();
  pageContext.setAttribute("bundle", b2Context.getResourceStrings());
  pageContext.setAttribute("cancelUrl", cancelUrl);
%>
<bbNG:pageHeader instructions="${r"${bundle['page.system.setup.instructions']}"}">
    <bbNG:breadcrumbBar environment="SYS_ADMIN_PANEL" navItem="admin_plugin_manage">
      <bbNG:breadcrumb href="index.jsp" title="${r"${bundle['plugin.name']}"}" />
      <bbNG:breadcrumb title="${r"${bundle['page.system.setup.breadcrumb']}"}" />
    </bbNG:breadcrumbBar>
	<bbNG:pageTitleBar iconUrl="${r"${bundle['plugin.logo']}"}" showIcon="true" showTitleBar="true" title="${r"${bundle['page.system.setup.title']}"}"/>
</bbNG:pageHeader>
<h3>Step 1: Enable the BYOB2 Organization Tool</h3>
<ol>
	<li>Starting in the<b> Administrator Panel</b> Click on <b>Organization Settings</b> 
	then <strong>Organization Tools</strong></li>
	<li>From Inside <b>Organization </b><strong>Tools</strong> click the<strong>&nbsp; 
	Filter By </strong>button and select <strong>Building Block Tools</strong><br>
	Make sure the <strong>BYOB2 - StarterKit </strong>Tool is <strong>
	On</strong> (make sure it's checked) and click <strong>Submit.</strong></li>
</ol>
<h3>Step 2: Enable the BYOB2 Course Tool</h3>
<ol>
	<li>Starting in the <b>Administrator Panel</b> Click on <b>Course Settings</b> 
	then <strong>Course Tools</strong></li>
	<li>From Inside <b>Course </b><strong>Tools</strong> click the <strong>
	Filter By </strong>button and select <strong>Building Block Tools</strong></li>
	<li>Make sure the <strong>BYOB2 - StarterKit </strong>Tool is <strong>On</strong> 
	(make sure it's checked) and click <strong>Submit.</strong></li>
</ol>
<h3>Step 3: Enable the BYOB2 User Tool</h3>
<p><b>NOTE:</b> This step must be repeated each time the Building Block is re-installed.</p> 
<ol>
	<li>Starting in the <b>Administrator Panel</b> Click on <b>Tabs and Modules </b>
	(under Communities)<b> </b>then select <strong>Tool Panel</strong></li>
	<li>From the tool panel, click <strong>Add Tool. </strong>Fill out the form 
	as follows and click <strong>Submit</strong>.<br>
	<img alt="Add tool" src="setup-images/add_tool.gif"></li>
</ol>
<h3>Step 4: Create a Course, enroll yourself.</h3>
<ol>
	<li>From the <strong>Administrator Panel</strong>, click <strong>Courses</strong>, then click 
	<strong>Create Course =&gt; New.</strong></li>
	<li>Fill in the course information screen. At minimum you need a <strong>
	Course ID </strong>and <strong>Course Name</strong>. Click <strong>Submit</strong> 
	when ready.</li>
	<li>Back at the <strong>Courses</strong> page, search for your course. 
	Choose <strong>Enrollments</strong> from the context menu, and click <strong>
	Enroll Users</strong>.</li>
	<li>Enroll yourself into the course with the <strong>Course Builder</strong> 
	role. Click <strong>Submit</strong>.<br>
	<img alt="enroll user" src="setup-images/enroll_user.gif"><br></li>
</ol>
<h3>All Done!</h3>
<p>After you complete these step you're ready to being exploring all that the BYOB2 Starter Kit has to offer! Enjoy. </p>
</bbNG:genericPage>
