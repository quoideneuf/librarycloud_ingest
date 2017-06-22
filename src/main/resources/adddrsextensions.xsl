<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:mods="http://www.loc.gov/mods/v3"
    xmlns:urlinfo="http://lib.harvard.edu/urlinfo"
    xmlns:HarvardDRS="http://hul.harvard.edu/ois/xml/ns/HarvardDRS" version="2.0">
    
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
 
    <xsl:param name="param1"><results/></xsl:param>
    <!--<xsl:param name="param1"><drsextensions>
        <docs>
            <doc>
                <urn>urn-3:HUL.OIS:527001</urn>
                <inDRS>true</inDRS>
                <accessFlag>P</accessFlag>
                <contentModel>STILL IMAGE</contentModel>
                <uriType>IDS</uriType>
                <fileDeliveryURL>http://nrs.harvard.edu/urn-3:DIV.LIB:15895461</fileDeliveryURL>
                <thumbnailURL>http://wwww.thumb.harvard.edu</thumbnailURL>
                <ownerCode>HUL.SS</ownerCode>
                <ownerCodeDisplayName>Library Techonolgy Services, Harvard University Information
                    Technology</ownerCodeDisplayName>
                <metsLabel/>
                <lastModifiedDate>2017-04-13T21:05:42.74Z</lastModifiedDate>
            </doc>
        </docs>
    </drsextensions></xsl:param>-->
 
     
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
            <xsl:variable name="results" select="$param1"/>
             <!--<xsl:variable name="holdings" select="document('')//xsl:param[@name='param1']//holdings"/>-->
            <!--<xsl:variable name="urn"><xsl:value-of select="substring-after(.//mods:url[@access='raw object'],'urn-3')"/></xsl:variable>-->
            <xsl:variable name="urn">
                <xsl:choose>
                    <xsl:when test="contains(.//mods:url[@access='raw object'],'?')">
                        <xsl:value-of select="substring-before(substring-after(.//mods:url[@access='raw object'],'urn-3'),'?')"/>                   
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="substring-after(.//mods:url[@access='raw object'],'urn-3')"/>                   
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:apply-templates select="$results//extensions[substring-after(urn,'urn-3')=$urn and inDRS = true()] "/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="extensions">
        <xsl:element name="extension" namespace="http://www.loc.gov/mods/v3">
            <xsl:element name="HarvardDRS:DRSMetadata" xmlns:HarvardDRS="http://hul.harvard.edu/ois/xml/ns/HarvardDRS">
                <!--<xsl:apply-templates select="inDRS"/>-->
                <xsl:apply-templates select="accessFlag"/>
                <xsl:apply-templates select="contentModel"/>
                <xsl:apply-templates select="uriType"/>
                <xsl:apply-templates select="ownerCode"/>
                <xsl:apply-templates select="ownerCodeDisplayName[not(.='')]"/>
                <xsl:apply-templates select="metsLabel[not(.='')]"/>
                <xsl:apply-templates select="lastModifiedDate[not(.='')]"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <xsl:template match="mods:location[mods:url/@access='raw object']">
        <xsl:copy>
            <xsl:apply-templates select="*"/>
            <xsl:variable name="results" select="$param1"/>
            <!--<xsl:variable name="urn">
                <xsl:value-of select="substring-after(mods:url[@access='raw object'],'urn-3')"/>
            </xsl:variable>-->
            <xsl:variable name="urn">
                <xsl:choose>
                    <xsl:when test="contains(mods:url[@access='raw object'],'?')">
                        <xsl:value-of select="substring-before(substring-after(mods:url[@access='raw object'],'urn-3'),'?')"/>                   
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="substring-after(mods:url[@access='raw object'],'urn-3')"/>                   
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <xsl:choose>
                <xsl:when test="not(mods:url[@access='preview'])">
                    <xsl:apply-templates select="$results//extensions[substring-after(urn,'urn-3')=$urn]/thumbnailURL[not(.='') and not(.='null')] "/>
                </xsl:when>
                <xsl:otherwise/>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="thumbnailURL">
        <xsl:element name="url" namespace="http://www.loc.gov/mods/v3">
            <xsl:attribute name="access"><xsl:text>preview</xsl:text></xsl:attribute>
            <xsl:value-of select="."/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="inDRS">
        <xsl:element name="inDRS" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>

    <xsl:template match="accessFlag">
        <xsl:element name="accessFlag" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>

    <xsl:template match="uriType">
        <xsl:element name="uriType" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>
    
    <xsl:template match="contentModel">
        <xsl:element name="contentModel" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>

    <xsl:template match="ownerCode">
        <xsl:element name="ownerCode" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>


    <xsl:template match="ownerCodeDisplayName">
        <xsl:element name="ownerCodeDisplayName" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>
    
    <xsl:template match="metsLabel">
        <xsl:element name="metsLabel" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>
    <xsl:template match="lastModifiedDate">
        <xsl:element name="lastModifiedDate" namespace="http://hul.harvard.edu/ois/xml/ns/HarvardDRS"><xsl:value-of select="."/></xsl:element>
    </xsl:template>
    
</xsl:stylesheet>    