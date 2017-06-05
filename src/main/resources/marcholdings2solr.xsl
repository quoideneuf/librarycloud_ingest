<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs"
    version="2.0"
    xmlns:marc="http://www.loc.gov/MARC21/slim">
    
    <xsl:output indent="yes"/>
    
    <xsl:template match="marc:collection">
        <xsl:element name="add">
            <xsl:apply-templates select="marc:record"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="marc:record">
        <xsl:element name="doc">
            <xsl:apply-templates select="marc:controlfield"/>
            <!-- change tp 004 once the marc holdings export changes -->
            <xsl:apply-templates select="marc:datafield[@tag='999']"/>
            <!--
            <xsl:element name="field">
                <xsl:attribute name="name">
                    <xsl:text>originalMarc</xsl:text>
                </xsl:attribute>
                <xsl:text disable-output-escaping="yes">
                    &lt;![CDATA[
                </xsl:text>
                <xsl:copy-of select="."/>
                <xsl:text disable-output-escaping="yes">
                    ]]&gt;
                </xsl:text>
            </xsl:element>
            -->
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="marc:controlfield">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:value-of select="@tag"/>
            </xsl:attribute>       
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>
    
    <!-- change to 004 when marc holdings export changes -->
    <xsl:template match="marc:datafield[@tag='999']">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>004</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="marc:subfield[@code='b']"/>
        </xsl:element>
                
    </xsl:template>
    
</xsl:stylesheet>