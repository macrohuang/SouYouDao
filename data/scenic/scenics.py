#!/usr/bin/env python
import MySQLdb
import sys

passwd = sys.argv[1]
conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")

f = open('scenic4a.data')
scenics = f.xreadlines()

for l in scenics:
    arr = l.split(" ")
    cursor.execute("select count(*) from T_SCENIC where name = %s",arr[0])
    exists = cursor.fetchone()[0]
    if exists == 0:
      cursor.execute("insert into T_SCENIC(name,level,authorized) values(%s,%s,False)",[arr[0],arr[1]])

conn.commit()
cursor.close()
conn.close()
