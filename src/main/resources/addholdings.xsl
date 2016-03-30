<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:mods="http://www.loc.gov/mods/v3" 
    xmlns:urlinfo="http://lib.harvard.edu/urlinfo" version="2.0"
    >
    
    <xsl:output indent="yes"/>
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
                    <xsl:if test="./DisplayCallNumber">
                        <xsl:element name="shelfLocator" namespace="http://www.loc.gov/mods/v3"><xsl:value-of select="./DisplayCallNumber"/></xsl:element>
                    </xsl:if>
                    <xsl:if test="./Marc852B">
                        <xsl:element name="physicalLocation" namespace="http://www.loc.gov/mods/v3"><xsl:value-of select="./Marc852B"/></xsl:element>
                    </xsl:if>
                    <xsl:apply-templates select="./Marc856U"/>
                 </xsl:element>
            </xsl:for-each>
         </xsl:copy>
    </xsl:template>

    <xsl:template match="Marc856U">
        <xsl:choose>
            <xsl:when test="contains(.,'nrs.harvard.edu') and not(contains(lower-case(.),'hul.e')and not(contains(lower-case(.),'hul.fig')))">
                <xsl:variable name="geturn"><xsl:value-of select="concat('',substring-after(.,'urn-3:'))"/></xsl:variable>
                <xsl:variable name="thumb">
                    <xsl:value-of select="document($geturn)/*/thumb"/>
                </xsl:variable>
                <xsl:variable name="pdsid">
                    <xsl:value-of select="document($geturn)/*/deliveryid"/>
                </xsl:variable>
                <xsl:variable name="mimetype">
                    <xsl:value-of select="document($geturn)/*/mimetype"/>
                </xsl:variable>
                <xsl:variable name="restrictflag">
                    <xsl:choose>
                        <xsl:when test="document($geturn)/*/restrictflag='P'">
                            <xsl:text>unrestricted</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:text>restricted</xsl:text>
                        </xsl:otherwise>
                    </xsl:choose>
                    <!--<xsl:value-of select="document($geturn)/*/restrictflag"/>-->
                </xsl:variable>
                <xsl:variable name="deliverytype">
                    <xsl:value-of select="document($geturn)/*/deliverytype"/>
                </xsl:variable>
                <xsl:if test="not($thumb='')">
                    <xsl:element name="url" namespace="http://www.loc.gov/mods/v3">
                        <xsl:attribute name="displayLabel"><xsl:text>thumb</xsl:text></xsl:attribute>
                        <xsl:value-of select="$thumb"/>
                        <!--
                        <xsl:element name="extension"  namespace="http://www.loc.gov/mods/v3">
                            <urlinfo:urlinfo>
                                <mimetype><xsl:value-of select="$mimetype"/></mimetype>
                                <restrictflag><xsl:value-of select="$restrictflag"/></restrictflag>
                                <deliverytype><xsl:value-of select="$deliverytype"/></deliverytype>
                            </urlinfo:urlinfo>
                        </xsl:element>
                        -->
                    </xsl:element>
                    <xsl:element name="url" namespace="http://www.loc.gov/mods/v3">
                        <xsl:attribute name="access"><xsl:text>raw object</xsl:text></xsl:attribute>
                        <xsl:attribute name="note"><xsl:value-of select="$restrictflag"/></xsl:attribute>
                        <xsl:value-of select="."/>
                    </xsl:element>    
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>   
                <xsl:element name="url" namespace="http://www.loc.gov/mods/v3">
                    <xsl:attribute name="access"><xsl:text>raw object</xsl:text></xsl:attribute>
                    <xsl:value-of select="."/>
                </xsl:element>
            </xsl:otherwise>   
        </xsl:choose>
    </xsl:template>


</xsl:stylesheet>    
