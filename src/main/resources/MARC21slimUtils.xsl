<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:marc="http://www.loc.gov/MARC21/slim" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- 8/19/04: ntra added "marc:" prefix to datafield element -->
	<xsl:template name="datafield">
		<xsl:param name="tag"/>
		<xsl:param name="ind1"><xsl:text> </xsl:text></xsl:param>
		<xsl:param name="ind2"><xsl:text> </xsl:text></xsl:param>
		<xsl:param name="subfields"/>
		<xsl:element name="marc:datafield">
			<xsl:attribute name="tag">
				<xsl:value-of select="$tag"/>
			</xsl:attribute>
			<xsl:attribute name="ind1">
				<xsl:value-of select="$ind1"/>
			</xsl:attribute>
			<xsl:attribute name="ind2">
				<xsl:value-of select="$ind2"/>
			</xsl:attribute>
			<xsl:copy-of select="$subfields"/>
		</xsl:element>
	</xsl:template>

	<xsl:template name="subfieldSelect">
		<xsl:param name="codes">abcdefghijklmnopqrstuvwxyz</xsl:param>
		<xsl:param name="delimeter"><xsl:text> </xsl:text></xsl:param>
		<xsl:variable name="str">
			<xsl:for-each select="marc:subfield">
				<xsl:if test="contains($codes, @code)">
					<xsl:value-of select="text()"/><xsl:value-of select="$delimeter"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($str,1,string-length($str)-string-length($delimeter))"/>
	</xsl:template>

	<xsl:template name="subfieldSelect852">
		<xsl:param name="codes">abcdefghijklmnopqrstuvwxyz</xsl:param>
		<xsl:param name="delimeter"><xsl:text> </xsl:text></xsl:param>
		<xsl:variable name="str">
			<xsl:for-each select="marc:subfield">
				<xsl:if test="contains($codes, @code)">
					<!-- <xsl:value-of select="text()"/><xsl:value-of select="$delimeter"/> -->
<xsl:if test="@code='b'">
					<xsl:choose>
<xsl:when test="text()='AFR'">Afro-American Studies</xsl:when>
<xsl:when test="text()='DIV'">Andover-Harv. Theol</xsl:when>
<xsl:when test="text()='BAK'">Baker Business</xsl:when>
<xsl:when test="text()='BER'">Biblioteca Berenson</xsl:when>
<xsl:when test="text()='BIO'">Biological Labs</xsl:when>
<xsl:when test="text()='BIR'">Birkhoff Math</xsl:when>
<xsl:when test="text()='BLH'">Blue Hill</xsl:when>
<xsl:when test="text()='ORC'">Botany Ames Orchid</xsl:when>
<xsl:when test="text()='AJP'">Botany Arboretum</xsl:when>
<xsl:when test="text()='ARN'">Botany Arnold (Cambr.)</xsl:when>
<xsl:when test="text()='ECB'">Botany Econ. Botany</xsl:when>
<xsl:when test="text()='FAR'">Botany Farlow Library</xsl:when>
<xsl:when test="text()='GRA'">Botany Gray Herbarium</xsl:when>
<xsl:when test="text()='ARG'">Botany Gray/Arnold</xsl:when>
<xsl:when test="text()='BRM'">Busch-Reisinger Mus</xsl:when>
<xsl:when test="text()='CAB'">Cabot Science</xsl:when>
<xsl:when test="text()='CAR'">Carpenter Center</xsl:when>
<xsl:when test="text()='WAR'">Charles Warren Ctr Lib</xsl:when>
<xsl:when test="text()='CHE'">Chemistry</xsl:when>
<xsl:when test="text()='CHI'">Child Memorial</xsl:when>
<xsl:when test="text()='MED'">Countway Medicine</xsl:when>
<xsl:when test="text()='EUR'">Ctr Eur Studies</xsl:when>
<xsl:when test="text()='HEL'">Ctr Hellen Studies</xsl:when>
<xsl:when test="text()='CFI'">Ctr Intl Affairs</xsl:when>
<xsl:when test="text()='DAN'">Derek Bok Center</xsl:when>
<xsl:when test="text()='DEV'">Development Office</xsl:when>
<xsl:when test="text()='DCJ'">Doc Ctr Japan</xsl:when>
<xsl:when test="text()='DOC'">Documents (Lamont)</xsl:when>
<xsl:when test="text()='DDO'">Dumbarton Oaks</xsl:when>
<xsl:when test="text()='CEA'">East Asian Res Ctr</xsl:when>
<xsl:when test="text()='ENV'">Environmental Information Ctr</xsl:when>
<xsl:when test="text()='FAL'">Fine Arts</xsl:when>
<xsl:when test="text()='FOG'">Fogg Museum</xsl:when>
<xsl:when test="text()='FUN'">Fung Library</xsl:when>
<xsl:when test="text()='GIB'">Gibb Islamic</xsl:when>
<xsl:when test="text()='GRO'">Grossman</xsl:when>
<xsl:when test="text()='GUT'">Gutman Education</xsl:when>
<xsl:when test="text()='HUA'">Harvard Archives</xsl:when>
<xsl:when test="text()='GDC'">Harvard Data Center</xsl:when>
<xsl:when test="text()='FOR'">Harvard Forest</xsl:when>
<xsl:when test="text()='HPO'">Harvard Planning &amp; Real Estate</xsl:when>
<xsl:when test="text()='ART'">Harvard University Art Museums</xsl:when>
<xsl:when test="text()='HYL'">Harvard-Yenching</xsl:when>
<xsl:when test="text()='HIL'">Hilles</xsl:when>
<xsl:when test="text()='HIS'">History Dept</xsl:when>
<xsl:when test="text()='HSL'">History of Science</xsl:when>
<xsl:when test="text()='HOU'">Houghton</xsl:when>
<xsl:when test="text()='KSG'">Kennedy Sch of Gov</xsl:when>
<xsl:when test="text()='KIR'">Kirkland House</xsl:when>
<xsl:when test="text()='GEO'">Kummel Geological Sci</xsl:when>
<xsl:when test="text()='GGL'">Networked Resource</xsl:when>
<xsl:when test="text()='LAM'">Lamont</xsl:when>
<xsl:when test="text()='LAW'">Law School</xsl:when>
<xsl:when test="text()='LIN'">Linguistics</xsl:when>
<xsl:when test="text()='LIT'">Littauer</xsl:when>
<xsl:when test="text()='DES'">Loeb Design</xsl:when>
<xsl:when test="text()='MUS'">Loeb Music</xsl:when>
<xsl:when test="text()='MAP'">Map Coll (Pusey)</xsl:when>
<xsl:when test="text()='MMF'">Master Microforms</xsl:when>
<xsl:when test="text()='MCK'">McKay Applied Sci</xsl:when>
<xsl:when test="text()='PAL'">Medieval Studies Lib</xsl:when>
<xsl:when test="text()='MIC'">Microforms (Lamont)</xsl:when>
<xsl:when test="text()='MUR'">Murray Research Ctr</xsl:when>
<xsl:when test="text()='MCZ'">Museum Comp Zoology</xsl:when>
<xsl:when test="text()='NMM'">National master micro</xsl:when>
<xsl:when test="text()='NEL'">Near Eastern Lib</xsl:when>
<xsl:when test="text()='NET'">Networked Resource</xsl:when>
<xsl:when test="text()='OPH'">Ophthalmology</xsl:when>
<xsl:when test="text()='PEA'">Peabody Museum</xsl:when>
<xsl:when test="text()='PHY'">Physics Research</xsl:when>
<xsl:when test="text()='POE'">Poetry Room (Lamont)</xsl:when>
<xsl:when test="text()='PRI'">Primate Center Lib</xsl:when>
<xsl:when test="text()='PSY'">Psychology Research</xsl:when>
<xsl:when test="text()='PUS'">Pusey</xsl:when>
<xsl:when test="text()='QUA'">Quad Library</xsl:when>
<xsl:when test="text()='RCA'">Radcliffe Archives</xsl:when>
<xsl:when test="text()='PHI'">Robbins Philosophy</xsl:when>
<xsl:when test="text()='CEL'">Robinson Celtic</xsl:when>
<xsl:when test="text()='RUB'">Rubel (Fine Arts)</xsl:when>
<xsl:when test="text()='RRC'">Russian Res Ctr</xsl:when>
<xsl:when test="text()='SAN'">Sanskrit Library</xsl:when>
<xsl:when test="text()='SFL'">Schering Health Care</xsl:when>
<xsl:when test="text()='SCH'">Schlesinger</xsl:when>
<xsl:when test="text()='SIA'">Sci &amp; Intl Affairs</xsl:when>
<xsl:when test="text()='HSI'">Sci Instruments</xsl:when>
<xsl:when test="text()='SMY'">Smyth Classical</xsl:when>
<xsl:when test="text()='SOC'">Social Rel-Sociol</xsl:when>
<xsl:when test="text()='SBC'">Solidarity Bibl Center</xsl:when>
<xsl:when test="text()='STA'">Statistics</xsl:when>
<xsl:when test="text()='SCC'">Straus Conservation</xsl:when>
<xsl:when test="text()='THE'">Theatre Collection</xsl:when>
<xsl:when test="text()='TOZ'">Tozzer</xsl:when>
<xsl:when test="text()='URI'">Ukrainian Res Inst</xsl:when>
<xsl:when test="text()='WAM'">Warren Anatomical</xsl:when>
<xsl:when test="text()='WEI'">Weissman Preservation Ctr</xsl:when>
<xsl:when test="text()='WID'">Widener</xsl:when>
<xsl:when test="text()='WOL'">Wolbach Library</xsl:when>
<xsl:when test="text()='ANT'">Andover Newton Theol</xsl:when>
<xsl:when test="text()='TBC'">Boston College</xsl:when>
<xsl:when test="text()='BUT'">BU School of Theol</xsl:when>
<xsl:when test="text()='EDS'">EDS/Weston</xsl:when>
<xsl:when test="text()='WES'">EDS/Weston</xsl:when>
<xsl:when test="text()='GCT'">Gordon-Conwell</xsl:when>
<xsl:when test="text()='HCR'">Holy Cross Orthodox</xsl:when>
<xsl:when test="text()='STJ'">St John's Seminary</xsl:when>
</xsl:choose>
</xsl:if>
<xsl:value-of select="$delimeter"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($str,1,string-length($str)-string-length($delimeter))"/>
	</xsl:template>

	<xsl:template name="buildSpaces">
		<xsl:param name="spaces"/>
		<xsl:param name="char"><xsl:text> </xsl:text></xsl:param>
		<xsl:if test="$spaces>0">
			<xsl:value-of select="$char"/>
			<xsl:call-template name="buildSpaces">
				<xsl:with-param name="spaces" select="$spaces - 1"/>
				<xsl:with-param name="char" select="$char"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="chopPunctuation">
		<xsl:param name="chopString"/>
		<xsl:param name="punctuation"><xsl:text>.:,;/ </xsl:text></xsl:param>
		<xsl:variable name="length" select="string-length($chopString)"/>
		<xsl:choose>
			<xsl:when test="$length=0"/>
			<xsl:when test="contains($punctuation, substring($chopString,$length,1))">
				<xsl:call-template name="chopPunctuation">
					<xsl:with-param name="chopString" select="substring($chopString,1,$length - 1)"/>
					<xsl:with-param name="punctuation" select="$punctuation"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="not($chopString)"/>
			<xsl:otherwise><xsl:value-of select="$chopString"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="chopPunctuationFront">
		<xsl:param name="chopString"/>
		<xsl:variable name="length" select="string-length($chopString)"/>
		<xsl:choose>
			<xsl:when test="$length=0"/>
			<!--xsl:when test="contains('.:,;/[ ', substring($chopString,1,1))"-->
			<xsl:when test="contains('.:,;/ ', substring($chopString,1,1))">
				<xsl:call-template name="chopPunctuationFront">
					<xsl:with-param name="chopString" select="substring($chopString,2,$length - 1)"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="not($chopString)"/>
			<xsl:otherwise><xsl:value-of select="$chopString"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
