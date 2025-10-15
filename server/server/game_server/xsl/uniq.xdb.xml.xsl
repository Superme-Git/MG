<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes"/>
<xsl:template match="@localIp">
  <xsl:attribute name="localIp">UNIQNAME_SERVER_IP</xsl:attribute> 
</xsl:template>

<xsl:template match="@localPort">
  <xsl:attribute name="localPort">eval(STARTPORT+1004)</xsl:attribute> 
</xsl:template>

<xsl:template match="@*|*">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
