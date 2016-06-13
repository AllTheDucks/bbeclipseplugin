<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="bbNG" uri="/bbNG"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<bbNG:genericPage ctxId="ctx">
	<bbNG:pageHeader instructions="Put help information for the page here.">
		<bbNG:breadcrumbBar>
			<bbNG:breadcrumb>Example Config Page</bbNG:breadcrumb>
		</bbNG:breadcrumbBar>
		<bbNG:pageTitleBar>Example Config Page</bbNG:pageTitleBar>
	</bbNG:pageHeader>

	<stripes:form beanclass="${r"${basePackage}"}.stripes.ConfigAction">
		<bbNG:dataCollection>
			<bbNG:step title="Step One">
				<bbNG:dataElement label="Example Configuration Property">
					<stripes:text name="exampleProperty" />
				</bbNG:dataElement>
				<bbNG:stepInstructions text="This Property will be put in the 'settings.properties' file in the config directory."/>
			</bbNG:step>
			<bbNG:stepSubmit />
		</bbNG:dataCollection>
	</stripes:form>
	
</bbNG:genericPage>
