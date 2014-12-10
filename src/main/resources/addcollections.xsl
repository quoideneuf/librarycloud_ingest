<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mods="http://www.loc.gov/mods/v3" 
    xmlns:ns2="http://purl.org/dc/terms" xmlns:ns4="http://purl.org/dc/terms/"
    xmlns:ns3="http://purl.org/dc/elements/1.1" xmlns:ns5="http://purl.org/dc/elements/1.1/"
    xmlns:ns6="http://api.lib.harvard.edu/v2/collection/"
    xmlns:lc="http://hul.harvard.edu/ois/xml/ns/libraryCloud"
    version="1.0">
    
    <xsl:output indent="yes"/>
    <xsl:strip-space elements="*"/>
 
    <!-- try the commented out param statements when actually passing param via java - dont forget to remove the hardcoded sample param1 xml -->
    <xsl:param name="param1"/>
    <!--<xsl:param name="param1"><ns6:item/></xsl:param>-->
    <!--<xsl:param name="param1"><ns6:item xmlns:ns6="http://api.lib.harvard.edu/v2/collection/"/></xsl:param>-->
    <!-- remove below when actully passing param in java -->
    <!--<xsl:param name="param1">
        <ns6:item xmlns:ns2="http://purl.org/dc/terms" xmlns:ns4="http://purl.org/dc/terms/"
            xmlns:ns3="http://purl.org/dc/elements/1.1" xmlns:ns5="http://purl.org/dc/elements/1.1/"
            xmlns:ns6="http://api.lib.harvard.edu/v2/collection/">
            <item_id>013634709</item_id>
            <collections>
                <ns3:type>collection</ns3:type>
                <ns2:extent>0</ns2:extent>
                <ns3:identifier>6</ns3:identifier>
                <ns4:abstract>Lorem upson</ns4:abstract>
                <ns5:title>6th collection</ns5:title>
            </collections>
            <collections>
                <ns3:type>collection</ns3:type>
                <ns2:extent>0</ns2:extent>
                <ns3:identifier>9</ns3:identifier>
                <ns4:abstract/>
                <ns5:title>User 1 collection</ns5:title>
            </collections>
        </ns6:item>
    </xsl:param> -->
 
     
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
                    <xsl:for-each select="$collections//collections">
                        <xsl:element name="collection" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                            <xsl:element name="type" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="ns3:type"/>
                            </xsl:element>
                            <xsl:element name="identifier" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="ns3:identifier"/>
                            </xsl:element>
                            <xsl:element name="abstract" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="ns4:abstract"/>
                            </xsl:element>
                            <xsl:element name="title" namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="ns5:title"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:for-each>
                </xsl:element>   
            </xsl:element> 
            <xsl:apply-templates select="mods:recordInfo" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>    

