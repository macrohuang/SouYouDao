package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import utils.WebSearchResult;

public class WebSearch extends Controller {
  /**
   * 搜索全网(原是计划搜索互联网，但是基于数据可靠和准确性考虑，放弃互联网，改为自身提供数据)
   * @param keywords
   * @param page
   * @param template 由于前端要使用scrollPagination插件，需要一个template专门对页面进行css控制
   */
  public static void search(String keywords, int page, String template) {
    if (template == null || template.equals("")) {
      template = "Application/page.html";
    }
    List<WebSearchResult> searchResults = new ArrayList<WebSearchResult>();
    // http://souyoudao.eicp.net
    /*
     * String solrHost = Play.configuration.get("solr.host").toString(); String
     * searchUrl = solrHost + "/solr/select/?q=" + keywords + "&start=" + (page
     * - 1) Constants.WEB_SEARCH_PAGE_SIZE + "&rows=" +
     * Constants.WEB_SEARCH_PAGE_SIZE + "&fl=title%2Curl%2Ctstamp&hl=on";
     * HttpResponse resp = WS.url(searchUrl).post(); Document document =
     * resp.getXml(); // 获取title、url等基本信息 NodeList docs =
     * document.getElementsByTagName("doc"); // 获取填充highlighting的信息 NodeList
     * highlightings = document.getElementsByTagName("arr"); if (docs == null ||
     * docs.getLength() == 0) return; for (int i = 0; i < docs.getLength(); i++)
     * { Node doc = docs.item(i); WebSearchResult result = new
     * WebSearchResult(); result.title =
     * doc.getChildNodes().item(0).getTextContent(); result.date =
     * doc.getChildNodes().item(1).getTextContent(); result.url =
     * doc.getChildNodes().item(2).getTextContent(); Node highlight =
     * highlightings.item(i); result.highlighting =
     * highlight.getChildNodes().item(0).getTextContent();
     * searchResults.add(result); }
     */
    renderTemplate(template, searchResults);
  }
}
