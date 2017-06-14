<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"  
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xlink="http://www.w3.org/TR/xlink"  
    xmlns:mods="http://www.loc.gov/mods/v3" 
>
<xsl:output method="xml" omit-xml-declaration="yes" version="1.0" encoding="UTF-8" indent="yes"/>
	<!--<xsl:param name="url">http://nrs.harvard.edu/urn-3:HBS.Baker.GEN:5028297-2011</xsl:param>-->
	<!--<xsl:param name="url"></xsl:param>-->
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>	
	</xsl:template>
	
	<xsl:template match="mods:recordIdentifier">
		<xsl:choose>
			<xsl:when test="../../mods:location/mods:url[@access='raw object']=$url">
				<xsl:copy>
					<xsl:copy-of select="@*"/>
					<xsl:variable name="urn">
						<xsl:choose>
							<xsl:when test="contains($url,'?')">
								<xsl:value-of select="substring-before(substring-after($url,'urn-3:'),'?')"/>                   
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="substring-after($url,'urn-3:')"/>                   
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:value-of select="."/><xsl:text>_</xsl:text><xsl:value-of select="$urn"/>
				</xsl:copy>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="mods:location[mods:url]">
		<xsl:copy>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="mods:url[@access='raw object']">
		<xsl:if test=".=$url">
			<xsl:copy-of select="."/>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>
