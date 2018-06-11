<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:mods="http://www.loc.gov/mods/v3"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:param name="repository-map-file" select="'src/main/resources/repository-map.xml'" />
    <xsl:variable name="map" select="document($repository-map-file)" />

    <xsl:template match="mods:modsCollection">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="mods:mods">
        <!--
        <xsl:variable name="restrictedRec">
            <xsl:value-of select=".[not(contains(mods:recordInfo/mods:recordOrigin[starts-with(.,'Open Metadata')],'RES-C')) and not(contains(mods:recordInfo/mods:recordOrigin[starts-with(.,'Open Metadata')],'RES-D'))]"></xsl:value-of>
        </xsl:variable>
        -->
        <xsl:if test="not(mods:recordInfo/mods:recordOrigin='Open Metadata Status: RES-C') and not(mods:recordInfo/mods:recordOrigin='Open Metadata Status: RES-D')">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates />
            </xsl:copy>
        </xsl:if>

        <extension>
          <HarvardRepositories>
            <xsl:for-each select="mods:location/mods:physicalLocation[@type = 'repository']">
              <xsl:variable name="source" select="./text()" />
              <HarvardRepository>
                <xsl:choose>
                  <xsl:when test="string-length($map//mapping[source=$source]/replacement) &gt; 0">
                    <xsl:value-of select="$map//mapping[source=$source]/replacement" />
                  </xsl:when>
                  <xsl:otherwise><xsl:value-of select="text()" /></xsl:otherwise>
                </xsl:choose>
              </HarvardRepository>
            </xsl:for-each>
          </HarvardRepositories>
        </extension>
    </xsl:template>

    <xsl:template match="mods:location/mods:physicalLocation">
        <xsl:variable name="source" select="./text()" />
        <xsl:choose>
            <xsl:when test="@type = 'repository'">

                <xsl:copy>
                    <xsl:attribute name="displayLabel">Harvard repository</xsl:attribute>
                    <xsl:apply-templates select="@*"/>
                    <xsl:choose>
                        <xsl:when test="string-length($map//mapping[source=$source]/replacement) &gt; 0">
                            <xsl:value-of select="$map//mapping[source=$source]/replacement" />
                        </xsl:when>
                        <xsl:otherwise><xsl:value-of select="text()" /></xsl:otherwise>
                    </xsl:choose>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="." />
                <!-- <xsl:copy-of select="."/> -->
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

    <xsl:template match="@* | *">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
