<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:HarvardDRS="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mods="http://www.loc.gov/mods/v3" version="1.0">
    
    <xsl:output indent="yes" method="text"/>
    
    <xsl:template match="mods:modsCollection">
        <xsl:element name="add">
            <xsl:apply-templates select="mods:mods"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="mods:mods">
        <xsl:if test="not(mods:typeOfResource[@collection='yes'])">
            <xsl:apply-templates select=".//mods:location[mods:url/@access='raw object' and not(mods:url/@access='preview') and not(mods:extension/HarvardDRS:DRSMetadata/HarvardDRS:accessFlag='R')]"/>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="mods:location[./mods:url/@access='raw object' and not(./mods:url/@access='preview')]">
        <xsl:if test="contains(mods:url[@access='raw object'],'urn-3') and not(contains(mods:url[@access='raw object'],'HUL.FIG')) and not(contains(mods:url[@access='raw object'],'ebookbatch')) and not(contains(mods:url[@access='raw object'],'ejournals'))">
            <xsl:value-of select="mods:url[@access='raw object']"/>
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>

   
    <xsl:template match="*"/>
    
</xsl:stylesheet>