package controllers;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jobs.ScenicImageDownloader;
import models.scenic.Scenic;
import models.scenic.ScenicAlias;
import models.scenic.ScenicImage;
import models.scenic.ScenicUpdateHelper;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.lucene.IKQueryParser;
import org.wltea.analyzer.lucene.IKSimilarity;

import play.Logger;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;
import utils.Constants;
import utils.FileUtil;
import utils.WebResponse;
import utils.WebResponse.ResponseStatus;

public class Scenics extends Controller {
    private static Map<String, Field> fieldMap = new HashMap<String, Field>();
    static {
        for (Field field : Scenic.class.getFields()) {
            fieldMap.put(field.getName(), field);
        }
    }

    public static void index() {
        List<Scenic> scenics = Scenic.all().fetch(4);
        render(scenics);
    }

    public static void detail(Long id) {
        Scenic scenic = Scenic.findById(id);
        List<ScenicImage> images = ScenicImage.find("scenic.id = ? order by id desc", id).fetch(21);
        ScenicImage firstImage = images.size() > 0 ? images.get(0) : null;
        render(scenic, images, firstImage);
    }

    /**
     * Search后进入列表页面
     * @param keywords
     * @param page start at 1
     */
    public static void search(String keywords, int page) {
        int total = 0;
        if (page < 1) page = 1;
        List<Scenic> scenics = new ArrayList<Scenic>();
        Logger.info("搜索景区：" + keywords);
        Directory directory = null;
        IndexSearcher searcher = null;
        try {
            directory = Constants.OPEN_SCENIC_INDEX_DIR();
            searcher = Constants.CREATE_INDEX_SEARCHER(directory);
            Query query =
                    IKQueryParser.parseMultiField(new String[] {"name", "description", "address",
                            "tel", "province.name", "city.name"}, keywords);
            searcher.setSimilarity(new IKSimilarity());
            // 执行检索操作，根据每次查询的页数取topN
            TopDocs results = searcher.search(query, page * Constants.SCENIC_SEARCH_PAGE_SIZE);
            total = results.totalHits;
            // 当前页面起点
            int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
            for (int i = 0; i < Constants.SCENIC_SEARCH_PAGE_SIZE; i++) {
                // 超过总结果集数目跳出循环
                if (index + i >= results.scoreDocs.length) {
                    break;
                }
                ScoreDoc scoreDoc = results.scoreDocs[index + i];
                Document document = searcher.doc(scoreDoc.doc);
                Scenic scenic = Scenic.findById(Long.parseLong(document.get("id")));
                if (scenic != null) {
                    scenics.add(scenic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (searcher != null) searcher.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        render("Scenics/list.html", scenics, keywords, total, page);
    }

    /**
     * 删除景区内的图片一张
     * @param imageId
     */
    public static void deleteImage(Long imageId) {
        ScenicImage image = ScenicImage.findById(imageId);
        new File(FileUtil.getApplicationPath("data", "scenic", "images") + image.imageName)
                .delete();
        image.delete();
    }

    public static void deleteRoadmap(Long scenicId) {
        Scenic scenic = Scenic.findById(scenicId);
        new File(FileUtil.getApplicationPath("data", "scenic", "roadmaps") + scenic.roadmapImage)
                .delete();
        scenic.roadmapImage = null;
        scenic.save();
    }

    /**
     * AJAX分页使用的query. <<<<<<< HEAD
     * @param keywords
     * @param page start at 1
     * @param template (default = "Scenics/page.html") =======
     * @param keywords
     * @param page start at 1
     * @param template (default = "Scenics/page.html") >>>>>>>
     *            0419e5a87f1970bd3c61179fd42a8eb5fdd95d44
     */
    public static void query(String keywords, int page, String template) {
        Logger.info("Query Scenics : " + keywords + ", page : " + page);
        if (template == null || template.equals("")) {
            template = "Scenics/page.html";
        }
        if (page < 1) page = 1;
        List<Scenic> scenics = new ArrayList<Scenic>();
        Directory directory = null;
        IndexSearcher searcher = null;
        try {
            directory = Constants.OPEN_SCENIC_INDEX_DIR();
            searcher = Constants.CREATE_INDEX_SEARCHER(directory);
            Query query =
                    IKQueryParser.parseMultiField(new String[] {"name", "description", "address",
                            "tel", "province.name", "city.name"}, keywords);
            searcher.setSimilarity(new IKSimilarity());
            // 执行检索操作，根据每次查询的页数取topN
            TopDocs results = searcher.search(query, page * Constants.SCENIC_SEARCH_PAGE_SIZE);
            // 当前页面起点
            int index = (page - 1) * Constants.SCENIC_SEARCH_PAGE_SIZE;
            for (int i = 0; i < Constants.SCENIC_SEARCH_PAGE_SIZE; i++) {
                // 超过总结果集数目跳出循环
                if (index + i >= results.scoreDocs.length) {
                    break;
                }
                ScoreDoc scoreDoc = results.scoreDocs[index + i];
                Document document = searcher.doc(scoreDoc.doc);
                Scenic scenic = Scenic.findById(Long.parseLong(document.get("id")));
                if (scenic != null) {
                    scenics.add(scenic);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (searcher != null) searcher.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        renderTemplate(template, scenics);
    }

    public static void saveName(long id, String name) {
        Scenic s = Scenic.findById(id);
        s.name = name;
        s.save();
    }

    /**
     * 从网络下载图片
     * @param urls
     */
    public static void saveImages(long scenicId, String webImg, File localImg, String pasteImg,
            String imgSrc) {
        if ("web".equals(imgSrc)) {
            new ScenicImageDownloader(webImg, scenicId).now();
        } else if ("local".equals(imgSrc)) {
            ScenicImage image = new ScenicImage();
            File file =
                    new File(Constants.SCENIC_IMAGE_DIR + Codec.UUID() + "."
                            + FileUtil.getExtension(localImg));
            // TODO renameTo only works on the same File System.
            localImg.renameTo(file);
            image.imageName = file.getName();
            image.scenic = Scenic.findById(scenicId);
            image.save();
            // 生成缩略图
            File thumb = new File(Constants.SCENIC_IMAGE_THUMB_DIR + file.getName());
            Images.resize(file, thumb, 160, -1);
        }
        detail(scenicId);
    }

    /**
     * 删除景区图片
     * @param scenicImageId
     */
    public static void deleteImage(long scenicImageId) {
        ScenicImage image = ScenicImage.findById(scenicImageId);
        File file = new File(Constants.SCENIC_IMAGE_DIR + image.imageName);
        File thumb = new File(Constants.SCENIC_IMAGE_THUMB_DIR + image.imageName);
        if (file.exists()) file.delete();
        if (thumb.exists()) thumb.delete();
        image.delete();
        // 获得下一张图片
        ScenicImage next = ScenicImage.find("id < ? order by id desc", scenicImageId).first();
        long nextId = 0;
        if (next != null) nextId = next.id;
        String json = "{\"nextId\":\"" + nextId + "\"}";
        // TODO 增加图片评论数据
        renderJSON(json);
    }

    /**
     * 获取下一张图片
     * @param scenicImageId
     */
    public static void nextImage(long scenicImageId) {
        ScenicImage next = ScenicImage.find("id < ? order by id desc", scenicImageId).first();
        long nextId = 0;
        if (next != null) nextId = next.id;
        String json = "{\"nextId\":\"" + nextId + "\"}";
        // TODO 增加图片评论数据
        renderJSON(json);
    }

    /**
     * 获取上一张图片
     * @param scenicImageId
     */
    public static void prevImage(long scenicImageId) {
        ScenicImage next = ScenicImage.find("id > ? order by id desc", scenicImageId).first();
        long prevId = 0;
        if (next != null) prevId = next.id;
        String json = "{\"prevId\":\"" + prevId + "\"}";
        // TODO 增加图片评论数据
        renderJSON(json);
    }

    /**
     * 获得图片在Gallery中显示
     * @param imageId
     */
    public static void getImage(long imageId) {
        ScenicImage image = ScenicImage.findById(imageId);
        if (image != null) {
            String json = "{\"imageName\":\"" + image.imageName + "\",\"id\":\"" + image.id + "\"}";
            // TODO 增加图片评论数据
            renderJSON(json);
        }
    }

    public static void updateByField(long scenicId, String field, String value) {
        WebResponse response = new WebResponse();
        if (!fieldMap.containsKey(field)) {
            response.status = ResponseStatus.WEB_RESPONSE_PARAM_ERROR;
            response.msg = "所要更新的字段为非法字段";
        } else {
            Scenic scenic = Scenic.findById(scenicId);
            ScenicUpdateHelper.updateByField(response, scenic, fieldMap.get(field), value);
        }
        renderJSON(response.toJsonString());
    }

    public static void newScenic(String name) {
        WebResponse response = new WebResponse();
        Scenic scenic = Scenic.find("name=?", name).first();
        if (scenic != null) {
            response.status = ResponseStatus.WEB_RESPONSE_PARAM_ERROR;
            response.msg = "该景点已经存在！";
        } else {
            scenic = new Scenic();
            scenic.name = name;
            scenic.save();
            processOK(response, scenic.id);
        }
        renderJSON(response.toJsonString());
    }

    public static void addAlias(long scenicId, String alias) {
        System.out.println(alias);
        WebResponse response = new WebResponse();
        Scenic scenic = Scenic.findById(scenicId);
        ScenicAlias scenicAlias = new ScenicAlias();
        scenicAlias.alias = alias;
        if (scenic.alias == null)
            scenic.alias = new ArrayList<ScenicAlias>();
        if (scenic.alias.contains(scenicAlias)) {
            response.status = ResponseStatus.WEB_RESPONSE_PARAM_ERROR;
            response.msg = "该别名已经存在！";
        } else {
            scenicAlias.save();
            scenic.alias.add(scenicAlias);
            scenic.save();
            processOK(response, alias);
        }
        renderJSON(response.toJsonString());
    }

    private static void processOK(WebResponse response, Object data) {
        response.status = ResponseStatus.WEB_RESPONSE_OK;
        response.data = data;
    }
}
