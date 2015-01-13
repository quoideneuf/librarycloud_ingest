<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mods="http://www.loc.gov/mods/v3" version="1.0"
    >
    
    <xsl:output indent="no"/>
    <xsl:strip-space elements="*"/>
 
    <xsl:param name="param1"><holdings/></xsl:param> 
 
     
    <xsl:template match="@*|node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="mods:modsCollection">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="mods:mods">
        <xsl:copy>
            <xsl:apply-templates select="*" />
            <xsl:variable name="holdings" select="$param1"/> 
             <!--<xsl:variable name="holdings" select="document('')//xsl:param[@name='param1']//holdings"/>-->
            <xsl:variable name="hollisid"><xsl:value-of select="./mods:recordInfo/mods:recordIdentifier"/></xsl:variable>
            
            <xsl:for-each select="$holdings//docs[MarcLKRB=$hollisid]">
                <xsl:element name="location" namespace="http://www.loc.gov/mods/v3">
                    <xsl:if test="$holdings//docs[MarcLKRB=$hollisid]/DisplayCallNumber">
                        <xsl:element name="shelfLocator" namespace="http://www.loc.gov/mods/v3"><xsl:value-of select="$holdings//docs[MarcLKRB=$hollisid]/DisplayCallNumber"/></xsl:element>
                    </xsl:if>
                    <xsl:if test="$holdings//docs[MarcLKRB=$hollisid]/Marc852B">
                        <xsl:element name="physicalLocation" namespace="http://www.loc.gov/mods/v3"><xsl:value-of select="$holdings//docs[MarcLKRB=$hollisid]/Marc852B"/></xsl:element>
                    </xsl:if>
                    <xsl:if test="$holdings//docs[MarcLKRB=$hollisid]/Marc856U">
                        <xsl:element name="url" namespace="http://www.loc.gov/mods/v3"><xsl:value-of select="$holdings//docs[MarcLKRB=$hollisid]/Marc856U"/></xsl:element>
                    </xsl:if>
                </xsl:element>
            </xsl:for-each>
            
        </xsl:copy>
    </xsl:template>


</xsl:stylesheet>    