#!/usr/bin/env python
#coding utf-8
import MySQLdb
import sys

passwd = sys.argv[1]

conn = MySQLdb.connect(host='localhost', user='root',passwd=passwd,db='syd')

cursor = conn.cursor()
cursor.execute("SET NAMES 'utf8'")

cursor.execute("delete from T_ADMIN")
cursor.execute("insert into T_ADMIN values(1,'xjx','xjx')")
cursor.execute("insert into T_ADMIN values(2,'gkk','gkk')")
cursor.execute("insert into T_ADMIN values(3,'admin','admin')")

conn.commit()
cursor.close()
conn.close()
