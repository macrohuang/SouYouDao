from scrapy.contrib.spiders import CrawlSpider,Rule
from scrapy.contrib.linkextractors.sgml import SgmlLinkExtractor
from scrapy.selector import HtmlXPathSelector
from scenic_crawler.items import ScenicItem

#scrapy crawl scenic_spider -o items.json -t json
class ImageSpider(CrawlSpider):
    name = 'image_spider'
    allowed_domains = ['51yala.com']
    start_urls = [
        'http://www.51yala.com/list/list_1753_1.Html',#ao men
    ]
    rules = (
        Rule(SgmlLinkExtractor(allow='www.51yala.com/Html/\\d+-*\\d+.html$'),
                callback='parse_item',follow=True,),
        )
    def parse_item(self, response):
      hxs = HtmlXPathSelector(response)
      html_items = hxs.select('//table[@class="tab7"]')
      items = []
      for h in html_items:
        try:
          scenic = ScenicItem()
          scenic['name'] = h.select('//span[@class="STYLE39"]/text()').extract()[0]
          scenic['content'] = h.select('//span[@class="STYLE3"]').extract()[0]
          if scenic not in items:
              items.append(scenic)
        except:
          pass
      return items
