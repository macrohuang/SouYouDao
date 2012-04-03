#!/usr/bin/env python

import glob
import Image

count = 1;
for name in glob.glob('scenic/images/*'):
  if count <= 22062:
    print count
    count = count + 1
    continue
  try:
    i = Image.open(name)
    box = i.getbbox()
    new_box = (box[0],box[1],box[2],box[3]-50)
    new_img = i.crop(new_box)
    new_img.save(name)
    new_img.thumbnail((128,128))
    new_img.save(name.replace(r'scenic/images/','scenic/images/thumbs/'),'jpeg')
    print count,' ',name
  except Exception as e:
    print name,'--',e
  count = count + 1
