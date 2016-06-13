<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="templates">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/blackboard-help.css" media="screen"/>
	<title>Project Templates for Blackboard Building Blocks</title>
	</head>
	<body>
	<h3>Project Templates</h3>
		<xsl:for-each select="template">
		<b><xsl:value-of select="name"/></b><br />
		<p><xsl:value-of select="description"/></p>
		
		</xsl:for-each>
	</body>	
	</html>
	</xsl:template>
</xsl:stylesheet>