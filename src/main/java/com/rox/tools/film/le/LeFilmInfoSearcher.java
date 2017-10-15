package com.rox.tools.film.le;

import com.rox.tools.film.FilmInfo;
import com.rox.tools.film.Searcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

/**
 * Created by Rox on 2017/7/3.
 */
public class LeFilmInfoSearcher implements Searcher {
    public FilmInfo search(String film) {
        try {
            String encode = URLEncoder.encode(film, "UTF-8");
            String url = "http://so.le.com/s?wd=" + encode + "&from=pc&ref=click&click_area=search_button&query=" + encode + "&is_default_query=0&module=suggest_list&eid=199499326957551507&experiment_id=104%3A0&is_trigger=1";
            Document doc = Jsoup.connect(url).timeout(300000).get();

            return parse(doc, film);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static FilmInfo parse(Document doc, String film) {
        FilmInfo info = null;
        Elements resultList = doc.getElementsByClass("So-detail");

        if(resultList == null || resultList.isEmpty()) {
            System.err.println("内容列表为空");
            return null;
        }

        for (Element row : resultList) {
            Elements aList = row.getElementsByClass("j-baidu-a");

            if(!aList.isEmpty()) {
                String tvname = aList.get(0).attr("title");
                if(tvname.equals(film)) {
                    try {
                        info = new FilmInfo();
                        info.name = tvname;
                        extractFilmInfo(info, row);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        return info;
    }

    protected static FilmInfo extractFilmInfo(FilmInfo info, Element element) {
        Elements infoList = element.getElementsByClass("info_list");
        if(infoList == null || infoList.isEmpty()) {
            System.err.println("未找详细介绍：info_list");
            return null;
        }

        Element eInfo = infoList.get(0);

        Elements spanList = eInfo.getElementsByTag("span");
        for(int i = 0; i < spanList.size(); i++) {
            Element span = spanList.get(i);

            String name = span.child(0).text();
            switch (name) {
                case "导演：":
                    info.director = join(span.getElementsByTag("a"));
                    break;
                case "主演：":
                    info.character = join(span.getElementsByTag("a"));
                    break;
                case "年份：":
                    info.age = join(span.getElementsByTag("a"));
                    break;
                case "地区：":
                    info.region = join(span.getElementsByTag("a"));
                    break;
            }
        }

        //详情
        infoList = element.getElementsByClass("info-cnt");
        if(infoList == null || infoList.isEmpty()) {
            System.err.println("找不到详情。");
        } else {
            Elements aList = infoList.get(0).getElementsByTag("a");
            //优先从“查看详情”链接中获取信息
            if(aList != null && !aList.isEmpty()) {
                info.brief = extractDetail(aList.get(0).attr("href"));
            }

            if(info.brief == null) {
                //其次从当前div中获取信息
                info.brief = infoList.get(0).ownText();
            }
        }

        return info;
    }

    protected static String extractDetail(String url) {
        try {
            Document doc = Jsoup.connect(url).timeout(300000).get();

            Elements desList = doc.getElementsByClass("des_con");
            if(!desList.isEmpty()) {
                return desList.get(0).text();
            }

            System.err.println("详情页面内找不到需要的信息。");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    protected static String join(Elements list) {
        String s = "";

        for(int i = 0; i < list.size(); i++) {
            if(i == 0) {
                s = list.get(i).text();
            } else {
                s = s + "," + list.get(i).text();
            }
        }

        return s;
    }

    public static void main(String[] args) {
        FilmInfo info = new LeFilmInfoSearcher().search("一粒红尘");

        System.out.println(info);
    }
}