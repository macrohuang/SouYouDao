#!/usr/bin/env python
#coding utf-8
import MySQLdb
conn = MySQLdb.connect(host='localhost', user='root',passwd='56906950',db='syd')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")
cursor.execute("delete from T_REGION")
cursor.execute("delete from T_CITY")
cursor.execute("delete from T_PROVINCE")

f = open('areas.json')
area = eval(f.read())

pid = 1;
cid = 1;
rid = 1;
for p in area:
  cursor.execute("insert into T_PROVINCE(id,name) values(%s,%s)",[pid,p['name']])
  for c in p['sub']:
    cursor.execute("insert into T_CITY(id,name,province_id) values(%s,%s,%s)",[cid,c['name'],pid])
    for r in c['sub']:
      cursor.execute("insert into T_REGION(id,name,city_id) values(%s,%s,%s)",[rid,r['name'],cid])
      rid = rid+1
    cid = cid+1
  pid = pid+1

conn.commit()
cursor.close()
conn.close()
