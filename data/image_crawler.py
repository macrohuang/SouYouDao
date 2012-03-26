#!/usr/bin/env python
#coding=utf-8
import urllib
import lxml.html as HTML
from mechanize import Browser

br = Browser()
br.set_handle_robots(False)

def parseImages(html):
    root = HTML.document_fromstring(html)
    lnodes = root.xpath("//li[@class='photo-item']/a/img/@src")
    for n in lnodes:
      print n.replace('abpic','pic')

def search(name):
  r1 = br.open("http://lvyou.baidu.com/search?word="+urllib.quote(name))
  root = HTML.document_fromstring(r1.read())
  tnodes = root.xpath("//div[@class='item scene-item clearfix']/h4/a/@href")
  if len(tnodes) > 0:
    r2 = br.open("http://lvyou.baidu.com"+tnodes[0])
    root = HTML.document_fromstring(r2.read())
    tnodes = root.xpath("//div[@class='scene-album-more']/a/@href")
    if len(tnodes) > 0:
      r3 = br.open("http://lvyou.baidu.com"+tnodes[0])
      r3_html = r3.read()
      parseImages(r3_html)
      r3_doc = HTML.document_fromstring(r3_html)
      tnodes = r3_doc.xpath("//div[@class='pagelist']/a/@href")
      if len(tnodes) > 0:
        r4 = br.open("http://lvyou.baidu.com" + tnodes[0])
        r4_html = r4.read()
        parseImages(r4_html)
      
if __name__=='__main__':
  import MySQLdb
  import sys
  passwd = sys.argv[1]
  conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')
  cursor = conn.cursor()
  cursor.execute("SET NAMES 'utf8'")
  cursor.execute("select id,name from T_SCENIC")
  result = cursor.fetchone()
  id = result[0]
  name = result[1]
