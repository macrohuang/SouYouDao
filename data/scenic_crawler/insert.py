#!/usr/bin/env python
#coding utf-8
import MySQLdb
import sys
import json

items = json.load(open('items.json'))

passwd = sys.argv[1]
conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd_bak')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")
count = 1
for i in items:
    #print i['name']
    cursor.execute("select count(*) from T_SCENIC where name = %s",i['name'].encode('utf-8'))
    exists = cursor.fetchone()[0]
    if exists == 0:
      cursor.execute("insert into T_SCENIC(name,description) values(%s,%s)",[i['name'].encode('utf-8'),i['content'].encode('utf-8')])
    count = count + 1
    if count % 1000 == 0:
      conn.commit()
    print count,'  ',i['name']

conn.commit()
cursor.close()
conn.close()
