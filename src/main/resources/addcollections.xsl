<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs mods"
    xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mods="http://www.loc.gov/mods/v3"
    xmlns:dcterms="http://purl.org/dc/terms/" xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:col="http://api.lib.harvard.edu/v2/collection/"
    xmlns:lc="http://hul.harvard.edu/ois/xml/ns/libraryCloud" version="1.0">

    <xsl:output encoding="UTF-8" indent="yes"/>
    <xsl:strip-space elements="*"/>

    <xsl:param name="param1"></xsl:param>

    <xsl:template match="@* | node()">
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
            <xsl:apply-templates select="*[not(local-name() = 'recordInfo')]"/>
            <xsl:variable name="collections" select="$param1"/>
            <!--<xsl:variable name="holdings" select="document('')//xsl:param[@name='param1']//holdings"/>-->
            <xsl:variable name="recordid">
                <xsl:value-of select="./mods:recordInfo/mods:recordIdentifier"/>
            </xsl:variable>
            <xsl:element name="extension" namespace="http://www.loc.gov/mods/v3">
                <xsl:element name="sets"
                    namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                     <xsl:for-each
                        select="$collections//col:item[col:item_id = $recordid]/col:collections">
                        <xsl:element name="set"
                            namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                            <xsl:element name="recordType"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">set</xsl:element>
                            <xsl:element name="sytemId"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="col:systemId"/>
                            </xsl:element>
                            <xsl:element name="setDescription"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:setDescription"/>
                            </xsl:element>
                            <xsl:element name="setName"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                                <xsl:value-of select="col:setName"/>
                            </xsl:element>
                            <xsl:element name="dcp"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:dcp"/>
                            </xsl:element>
                            <xsl:element name="public"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:public"/>
                            </xsl:element>
                            <xsl:element name="collectionUrn"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:collectionUrn"/>
                            </xsl:element>
                            <xsl:element name="thumbnailUrn"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:thumbnailUrn"/>
                            </xsl:element>
                            <xsl:element name="created"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:created"/>
                            </xsl:element>
                            <xsl:element name="modified"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:modified"/>
                            </xsl:element>
                            <xsl:element name="contactName"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:contactName"/>
                            </xsl:element>
                            <xsl:element name="contactDepartment"
                                namespace="http://hul.harvard.edu/ois/xml/ns/libraryCloud">
                              <xsl:value-of select="col:contactDepartment"/>
                            </xsl:element>
                        </xsl:element>
                    </xsl:for-each>
                </xsl:element>
            </xsl:element>
            <xsl:apply-templates select="mods:recordInfo"/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>
