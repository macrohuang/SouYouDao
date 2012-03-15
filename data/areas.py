#!/usr/bin/env python
f = open('areas.json')
area = eval(f.read())

for p in area:
  print p['name']
  for c in p['sub']:
    print '  ',c['name']
    for r in c['sub']:
      print '    ',r['name']
