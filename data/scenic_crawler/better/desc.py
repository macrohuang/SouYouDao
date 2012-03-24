#!/usr/bin/env python
#encoding:utf-8
import MySQLdb
import sys
import re

passwd = sys.argv[1]
conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd_bak')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")

cursor.execute("select id from T_SCENIC")
arr = cursor.fetchall()

for id in arr:
  cursor.execute("select description from T_SCENIC where id = %s",id[0])
  description = cursor.fetchone()[0]

  #r1 = re.compile(r'<img src=".+">')
  #r2 = re.compile(r'<news:news_info>')
  #r3 = re.compile(r'</news:news_info>')
  #r5 = re.compile(r'<span class="STYLE3">')
  #r6 = re.compile(r'</span>')
  #r7 = re.compile(r'class=".+"')
  #r8 = re.compile(r'style=".+"')
  #r9 = re.compile(r'<table >[\s\S]+</table>')
  #r10 = re.compile(r'<p></p>')
  #r11 = re.compile(r'<div align="left">[\s]+</div>')
  r12 = re.compile(r'<p align="center"><img alt="" src="[\s\S]+"></p>')

  #description = r1.sub("",description)
  #description = r2.sub("",description)
  #description = r3.sub("",description)
  #description = r5.sub("",description)
  #description = r6.sub("",description)
  #description = r7.sub("",description)
  #description = r8.sub("",description)
  #description = r9.sub("",description)
  #description = r10.sub("",description)
  #description = r11.sub("",description)
  description = r12.sub("",description)

  cursor.execute("update T_SCENIC set description = %s where id = %s",[description,id[0]])

conn.commit()
cursor.close()
conn.close()
