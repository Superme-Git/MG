<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes"/>

<xsl:template match="//Manager[@name='GdeliveryClient']/Connector/@remotePort">
  <xsl:attribute name="remotePort">eval(STARTPORT+11)</xsl:attribute> 
  <xsl:attribute name="remoteIp">MANAGER_SERVER_IP</xsl:attribute>
  <xsl:attribute name="inputBufferSize">131072</xsl:attribute>
  <xsl:attribute name="outputBufferSize">2097152</xsl:attribute>
  <xsl:attribute name="keepOutputBuffer">true</xsl:attribute> 
</xsl:template>

<xsl:template match="//Manager[@name='Provider']/Connector">
forloop(`PORT', eval(STARTPORT+1), eval(STARTPORT+1+ LINK_NUMBER), CONNECTOR)
ifdef(`LINK2_INTERNAL_IP', forloop(`PORT', eval(STARTPORT+11), eval(STARTPORT+11+ LINK_NUMBER), CONNECTOR2))
</xsl:template>

<xsl:template match="//Manager[@name='GdeliveryClient']/@name">
  <xsl:attribute name="class">gnet.DeliveryManager</xsl:attribute> 
  <xsl:attribute name="name">GdeliveryClient</xsl:attribute> 
</xsl:template>

<xsl:template match="@*|*">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>

</xsl:stylesheet>
