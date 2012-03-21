from scrapy.contrib.spiders import CrawlSpider,Rule
from scrapy.contrib.linkextractors.sgml import SgmlLinkExtractor
from scrapy.selector import HtmlXPathSelector
from scenic_crawler.items import ScenicItem

#scrapy crawl scenic_spider -o items.json -t json
class ScenicSpider(CrawlSpider):
    name = 'scenic_spider'
    allowed_domains = ['51yala.com']
    start_urls = [
        'http://www.51yala.com/list/list_1494_1.Html',#bei jing
        'http://www.51yala.com/list/list_1695_1.Html',#tian jin
        'http://www.51yala.com/list/list_1967_1.Html',#he bei
        'http://www.51yala.com/list/list_1859_1.Html',#shan xi
        'http://www.51yala.com/list/list_1721_1.Html',#nei meng
        
        'http://www.51yala.com/list/list_1974_1.Html',#he nan
        'http://www.51yala.com/list/list_1785_1.Html',#hu bei
        'http://www.51yala.com/list/list_1472_1.Html',#hu nan
        'http://www.51yala.com/list/list_1818_1.Html',#jiang xi
        
        'http://www.51yala.com/list/list_1837_1.Html',#shan dong
        'http://www.51yala.com/list/list_1868_1.Html',#an hui
        'http://www.51yala.com/list/list_1404_1.Html',#jiang su
        'http://www.51yala.com/list/list_1790_1.Html',#shang hai
        'http://www.51yala.com/list/list_1537_1.Html',#zhe jiang
        'http://www.51yala.com/list/list_1630_1.Html',#fu jian
        
        'http://www.51yala.com/list/list_1513_1.Html',#guang dong
        'http://www.51yala.com/list/list_1902_1.Html',#guang xi
        'http://www.51yala.com/list/list_1085_1.Html',#hai nan
        
        'http://www.51yala.com/list/list_1837_1.Html',#si chuan
        'http://www.51yala.com/list/list_1365_1.Html',#yun nan
        'http://www.51yala.com/list/list_1457_1.Html',#chong qing
        'http://www.51yala.com/list/list_1677_1.Html',#gui zhou
        'http://www.51yala.com/list/list_1384_1.Html',#xi zang
        
        'http://www.51yala.com/list/list_1924_1.Html',#shan xi
        'http://www.51yala.com/list/list_2019_1.Html',#gan su
        'http://www.51yala.com/list/list_1945_1.Html',#ning xia
        'http://www.51yala.com/list/list_1958_1.Html',#qing hai
        'http://www.51yala.com/list/list_1610_1.Html',#xin jiang
        'http://www.51yala.com/list/list_1651_1.Html',#liao ning
        'http://www.51yala.com/list/list_1561_1.Html',#ji lin
        'http://www.51yala.com/list/list_1593_1.Html',#hei long jiang
        'http://www.51yala.com/list/list_1739_1.Html',#xiang gang
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
          #scenic['content'] = h.select('//span[@class="STYLE3"]').extract()[0]
          if scenic not in items:
              items.append(scenic)
        except:
          pass
      return items
