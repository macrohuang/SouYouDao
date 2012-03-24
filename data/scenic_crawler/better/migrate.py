#!/usr/bin/env python
#encoding:utf-8
import MySQLdb
import sys
import re

passwd = sys.argv[1]
conn_from = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd_bak')
conn_to = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')

cursor_from = conn_from.cursor()
cursor_to = conn_to.cursor()
cursor_from.execute("SET NAMES 'utf8'")
cursor_to.execute("SET NAMES 'utf8'")

cursor_from.execute("select id from T_SCENIC")
ids = cursor_from.fetchall()

for id in ids:
  cursor_from.execute("select name,description from T_SCENIC where id = %s",id[0])
  result = cursor_from.fetchone()
  name = result[0]
  description = result[1]
  cursor_to.execute("insert into T_SCENIC(name,description,authorized) values(%s,%s,false)",[name,description])
  
conn_to.commit()
cursor_from.close()
cursor_to.close()
conn_from.close()
conn_to.close()
