#!/usr/bin/env python

import glob
import Image

count = 1;
for name in glob.glob('scenic/images/*'):
  '''
  if count <= 22062:
    print count
    count = count + 1
    continue
  '''
  try:
    i = Image.open(name)
    i.thumbnail((180,135))
    i.save(name.replace(r'scenic/images/','scenic/images/thumbs/'),'jpeg')
    print count,' ',name
  except Exception as e:
    print name,'--',e
  count = count + 1
