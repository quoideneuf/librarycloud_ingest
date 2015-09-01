<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mods="http://www.loc.gov/mods/v3" 
    xmlns:dcterms="http://purl.org/dc/terms/"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:col="http://api.lib.harvard.edu/v2/collection/"
    xmlns:lc="http://hul.harvard.edu/ois/xml/ns/libraryCloud"
    version="1.0">
    
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
 
    <xsl:param name="param1"/>
     
    <xsl:template match="@*|node()">
        <xsl:copy-of select="."/>
    </xsl:template>
    
    <xsl:template match="mods:modsCollection">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Remove existing collections -->
    <xsl:template match="mods:extension[lc:collections]"/>

    <xsl:template match="mods:mods">
        <xsl:copy>
            <xsl:apply-templates select="*[not(local-name()='recordInfo')]" />
            <xsl:variable name="collections" select="$param1"/> 
             <!--<xsl:variable name="holdings" select="document('')//xsl:param[@name='param1']//holdings"/>-->
            <xsl:variable name="recordid"><xsl:value-of select="./mods:recordInfo/mods:recordIdentifier"/></xsl:variable>
            <xsl:element name="extension" namespace="http://www.loc.gov/mods/v3">
                <xsl:element name="collections" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                    <xsl:for-each select="$collections//col:collections">
                        <xsl:element name="collection" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                            <xsl:element name="type" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="dc:type"/>
                            </xsl:element>
                            <xsl:element name="identifier" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="dc:identifier"/>
                            </xsl:element>
                            <xsl:element name="abstract" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="dcterms:abstract"/>
                            </xsl:element>
                            <xsl:element name="title" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="dc:title"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:for-each>
                </xsl:element>   
            </xsl:element> 
            <xsl:apply-templates select="mods:recordInfo" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>    

