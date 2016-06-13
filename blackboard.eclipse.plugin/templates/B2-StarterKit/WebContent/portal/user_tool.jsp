<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/bbNG" prefix="bbNG" %>
<bbNG:genericPage title="This is the bbNG:genericPage title">
<bbNG:cssFile href="../css/b2sk.css" />
<bbNG:pageHeader instructions="This is an example of a User Tool Building block.">
	<bbNG:pageTitleBar iconUrl="../images/b2sk_logo_92x92.png" showIcon="true" showTitleBar="true" 
	title="B2SK - User Tool Building block Entry Point"/>
</bbNG:pageHeader>
<h2>Information</h2>
<p>
	User Tools appear on the institution page and are independent of any given course. 
	They are available to logged in users.<br />
</p> 
<h2> Example (screenshot)</h2>
<img src="images/b2sk_user_tool.png" alt="User Tool" />
<h2>Configuration</h2>
<p>Here's the required configuration section inside the <b>bb-manifest.xml</b> required for this section.</p>
<pre>
&lt;link&gt; &lt;!-- "User Tools" Entry Point institution page --&gt;
	&lt;type value="user_tool" /&gt;
	&lt;name value="B2SK: User Tool" /&gt;
	&lt;url value="portal/user_tool.jsp" /&gt;
	&lt;description value="B2SK Entry Point for a User Tools Building Block" /&gt;
	&lt;icons&gt;
       	&lt;listitem value="images/b2sk_logo_12x12.png"/&gt;
	&lt;/icons&gt;
&lt;/link&gt;			
</pre>
</bbNG:genericPage>