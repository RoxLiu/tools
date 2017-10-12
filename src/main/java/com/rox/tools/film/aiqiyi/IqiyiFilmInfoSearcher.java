package com.rox.tools.film.aiqiyi;

import com.rox.tools.film.FilmInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Rox on 2017/7/3.
 */
public class IqiyiFilmInfoSearcher {
    public static FilmInfo search(String film) {
        try {
            String url = "http://so.iqiyi.com/so/q_" + URLEncoder.encode(film, "UTF-8") + "?source=input&sr=637978315578";
            Document doc = Jsoup.connect(url).timeout(300000).get();

            return parse(doc, film);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static FilmInfo parse(Document doc, String film) {
        FilmInfo info = null;
        Elements resultList = doc.getElementsByClass("mod_result_list");

        if(resultList == null || resultList.isEmpty()) {
            System.err.println("找不到内容：albumpic-showall-wrap");
            return null;
        }

        Elements liList = resultList.get(0).getElementsByTag("li");

        if(liList == null || liList.isEmpty()) {
            System.err.println("内容列表为空");
            return null;
        }

        for (Element li : liList) {
//                String tvname = new String(li.attr("data-widget-searchlist-tvname").getBytes("iso-8859-1"), "UTF-8");
            String tvname = li.attr("data-widget-searchlist-tvname");
            if(tvname.equals(film)) {
                try {
                    info = extractFilmInfo(li);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        return info;
    }

    protected static FilmInfo extractFilmInfo(Element element) {
        FilmInfo info = new FilmInfo();
        info.name = element.attr("data-widget-searchlist-tvname");

        Elements items = element.getElementsByClass("info_item");
        if(items == null || items.isEmpty()) {
            System.err.println("未找详细介绍：info_item");
            return null;
        }

        Element item = items.get(0);

        if(!item.children().isEmpty()) {
            //导演
            info.director = items.get(0).child(0).child(1).text();

            //主演
            String s = "";
            Elements actors = items.get(0).child(1).children();
            for(int i = 1; i < actors.size(); i++) {
                s += actors.get(i).text();
                if(i < actors.size() - 1) {
                    s += ",";
                }
            }
            info.character = s;
        }

        //
        items = element.getElementsByClass("result_title");
        Element eDetail = items.get(0).child(0);
        if(eDetail == null || !eDetail.attr("title").equals(info.name)) {
            System.err.println("找不到详情链接。");
        } else {
            extractDetail(info, eDetail.attr("href"));
        }

        return info;
    }

    protected static void extractDetail(FilmInfo info, String url) {
        try {
            Document doc = Jsoup.connect(url).timeout(300000).get();

            Elements eIntros = doc.getElementsByClass("episodeIntro");
            if(!eIntros.isEmpty()) {
                extractEpisodeIntro(info, eIntros);
                return;
            }

            Elements detailList = doc.getElementsByClass("result_detail");
            if(!detailList.isEmpty()) {
                extractResultDetail(info, detailList);
                return;
            }

            System.err.println("详情页面内找不到需要的信息。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void extractEpisodeIntro(FilmInfo info, Elements eIntros) {

        Elements items = eIntros.get(0).getElementsByClass("episodeIntro-line");

        for(int i = 0; i < items.size(); i++) {
            Element line = items.get(i);

            Elements pList = line.getElementsByTag("p");
            for(int j = 0; j < pList.size(); j++) {
                Element p = pList.get(j);

                switch (p.child(0).text()) {
                    case "地区：":
                        info.region     = p.child(1).text();
                        break;
                    case "语言：":
                        info.language   = p.child(1).text();
                        break;
                    case "时间：":
                        info.age        = p.child(1).text();
                        break;
                }
            }

        }
        //简介
        Elements e = eIntros.get(1).getElementsByClass("episodeIntro-brief");
        Element brief = e.get(e.size() - 1);

        info.brief      = brief.child(0).text();
    }

    protected static void extractResultDetail(FilmInfo info, Elements detailList) {
        Elements itemList = detailList.get(0).getElementsByClass("topic_item");
        for(int i = 0; i < itemList.size(); i++) {
            Elements emList = itemList.get(i).getElementsByTag("em");

            for(int j = 0; j < emList.size(); j++) {
                Element em = emList.get(j);
                String type = em.childNode(0).outerHtml().trim();
                switch (type) {
                    case "地区：":
                        info.region     = em.child(0).text();
                        break;
                    case "语言：":
                        info.language   =  em.child(0).text();
                        break;
                    case "时间：":
                        info.age        = em.child(0).text();
                        break;
                    case "简介：":
                        info.brief        = em.parent().childNode(1).outerHtml();
                        break;
                }
            }
        }
    }

    public static void main(String[] args) {
        search("遵义行");
    }
}
