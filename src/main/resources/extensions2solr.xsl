<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs"
    version="2.0">
    
    <xsl:output indent="yes"/>
    
    <xsl:template match="results">
        <xsl:element name="add">
            <xsl:apply-templates select="extensions"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="extensions">
        <xsl:element name="doc">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="urn">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>id</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>   
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>urn</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>        
    </xsl:template>
 
    <xsl:template match="thumbnailURL">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>thumbnailURL</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="fileDeliveryURL">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>fileDeliveryURL</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="contentModel">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>contentModel</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="accessFlag">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>accessFlag</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="uriType">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>uriType</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="ownerCode">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>ownerCode</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="ownerCodeDisplayName">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>ownerCodeDisplayName</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="metsLabel">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>metsLabel</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="lastModifiedDate">
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:text>lastModifiedDate</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="inDRS"/>

</xsl:stylesheet>