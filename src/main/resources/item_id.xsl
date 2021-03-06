<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:lc="http://api.lib.harvard.edu/v2/collection/"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
    
    <xsl:template match="lc:item">
        <xsl:apply-templates select="lc:item_id"/>
    </xsl:template>
    
    <xsl:template match="lc:item_id">
        <xsl:value-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>