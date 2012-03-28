#!/usr/bin/env python
#coding=utf-8
import urllib
import lxml.html as HTML
import os
import socket
from mechanize import Browser

# A help class to instance a no history Browser
class NoHistory(object):
  def add(self, *a, **k):
    pass  
  def clear(self):
    pass

br = Browser(history=NoHistory())
br.set_handle_robots(False)
socket.setdefaulttimeout(10.0)

def parse_images(html):
    root = HTML.document_fromstring(html)
    lnodes = root.xpath("//li[@class='photo-item']/a/img/@src")
    urls = []
    for n in lnodes:
      urls.append(n.replace('abpic','pic'))
    return urls

def search(name):
  r1 = br.open("http://lvyou.baidu.com/search?word="+urllib.quote(name))
  root = HTML.document_fromstring(r1.read())
  tnodes = root.xpath("//div[@class='item scene-item clearfix']/h4/a/@href")
  urls = []
  if len(tnodes) > 0:
    r2 = br.open("http://lvyou.baidu.com"+tnodes[0])
    root = HTML.document_fromstring(r2.read())
    tnodes = root.xpath("//div[@class='scene-album-more']/a/@href")
    if len(tnodes) > 0:
      r3 = br.open("http://lvyou.baidu.com"+tnodes[0])
      r3_html = r3.read()
      urls.extend(parse_images(r3_html))
      r3_doc = HTML.document_fromstring(r3_html)
      tnodes = r3_doc.xpath("//div[@class='pagelist']/a/@href")
      if len(tnodes) > 0:
        r4 = br.open("http://lvyou.baidu.com" + tnodes[0])
        r4_html = r4.read()
        urls.extend(parse_images(r4_html))
  return urls
 
def save_image(scenic_id,image_urls):
  p = re.compile(r"http[\s\S]+\/")
  for url in image_urls:
    file_name = p.sub('',url)
    path = "scenic/images/" + file_name
    if not os.path.exists(path) or not os.path.isfile(path):
      image = urllib.urlopen(url).read()
      file = open("scenic/images/"+file_name,"wb")
      file.write(image)
      file.close()
      cursor.execute("insert into T_SCENIC_IMAGE(imageName,scenic_id) values(%s,%s)",[file_name,scenic_id])
      print file_name,' has finished'
    else:
      cursor.execute("update T_SCENIC set finished = 1 where id = %s",scenic_id)
      print file_name,' already exist'

if __name__=='__main__':
  import MySQLdb
  import sys
  import re
  passwd = sys.argv[1]
  limit_base = sys.argv[2]
  limit_length = sys.argv[3]
  conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')
  cursor = conn.cursor()
  cursor.execute("SET NAMES 'utf8'")
  cursor.execute("select id,name from T_SCENIC where id not in (select distinct scenic_id from T_SCENIC_IMAGE) and finished = 0 order by id limit "+limit_base+","+limit_length)
  result = cursor.fetchall()
  p = re.compile('\[[\s\S]+\]')
  for r in result:
    try:
      id = r[0]
      name = p.sub('',r[1])
      image_urls = search(name)
      print 'begin to parse images ',name
      if len(image_urls) > 0:
        print 'begin to download ',name
        save_image(id,image_urls)
      else:
        cursor.execute("update T_SCENIC set finished = 1 where id = %s",id)
      conn.commit()
    except:
      pass
  conn.commit()
  cursor.close()
  conn.close()
