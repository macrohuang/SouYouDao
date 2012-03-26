# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/topics/items.html

from scrapy.item import Item, Field

class ScenicItem(Item):
    # define the fields for your item here like:
    name = Field()
    content = Field()
    location = Field()
    pass
class ImageItem(Item):
  scenic_name = Field()
  image_url = Field()
  pass
