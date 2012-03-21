# Scrapy settings for scenic_crawler project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/topics/settings.html
#

BOT_NAME = 'scenic_crawler'
BOT_VERSION = '1.0'

SPIDER_MODULES = ['scenic_crawler.spiders']
NEWSPIDER_MODULE = 'scenic_crawler.spiders'
USER_AGENT = '%s/%s' % (BOT_NAME, BOT_VERSION)

