<%@ taglib prefix="bbNG" uri="/bbNG"%>

<bbNG:learningSystemPage>
	<bbNG:pageHeader instructions="This page is accessed from a 'course_tool' link in the course
	 	control panel menu. Note that the course navigation menus are automatically added
	 	on the left side of the page, and a breadcrumb is added to access the course homepage.">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb>An example &lt;bbNG:learningSystemPage&gt;</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar title="An example &lt;bbNG:learningSystemPage&gt;">
		</bbNG:pageTitleBar>
	</bbNG:pageHeader>
	<h2>Examples of page scaffolding tags</h2>
	<a href="genericPage.jsp">Generic Page</a><br>
<!-- 	<a href="adminConsolePage.jsp">Admin Console Page</a> -->
	<h2>Examples of page Types</h2>
	<a href="dataCollectionPage.action?course_id=${r"${r"${param['course_id']}"}"}"">Data Collection Page</a><br>
</bbNG:learningSystemPage>

