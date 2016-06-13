<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:learningSystemPage title="This is the bbNG:genericPage title" ctxId="ctx">
<bbNG:cssFile href="../css/b2sk.css" />
<bbNG:pageHeader instructions="This is an example of a Course Tool Building block.">
	<bbNG:pageTitleBar iconUrl="../images/b2sk_logo_92x92.png" showIcon="true" showTitleBar="true" 
	title="B2SK - Course Tool Building block Entry Point"/>
</bbNG:pageHeader>
<h2>Information</h2>
<p>
	When accessing an individual course, you see this entry under "Course Tools" in the left hand column.<br/>
	Course tools are available to non-students only. <br/>
</p> 
<h2> Example (screenshot)</h2>
<img src="images/b2sk_course_tool.png" alt="Course Tool" />
<h2>Configuration</h2>
<p>Here's the required configuration section inside the <b>bb-manifest.xml</b> required for this section.</p>
<pre>
&lt;link&gt; &lt;!-- "Course Tools" Entry Point inside course --&gt;
	&lt;type value="course_tool" /&gt;
	&lt;name value="B2SK: Course Tool" /&gt;
	&lt;url value="course/course_tool.jsp" /&gt;
	&lt;description value="B2SK Entry Point for a Course Tools Building Block" /&gt;
&lt;/link&gt;
</pre>
</bbNG:learningSystemPage>
