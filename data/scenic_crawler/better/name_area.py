#!/usr/bin/env python
#encoding:utf-8
import MySQLdb
import sys
import re

passwd = sys.argv[1]
conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")

cursor.execute("select id from T_SCENIC")
arr = cursor.fetchall()

for id in arr:
  cursor.execute("select name from T_SCENIC where id = %s",id[0])
  description = cursor.fetchone()[0]

  r12 = re.compile(r'<p align="center"><img alt="" src="[\s\S]+"></p>')

  description = r12.sub("",description)

  cursor.execute("update T_SCENIC set description = %s where id = %s",[description,id[0]])

conn.commit()
cursor.close()
conn.close()
