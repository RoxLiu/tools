package com.rox.tools.film.searcher;

import com.rox.tools.film.FilmInfo;
import com.rox.tools.film.Searcher;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

/**
 * Created by Rox on 2017/7/3.
 */
public class YoukuFilmInfoSearcher implements Searcher {
    public FilmInfo search(String film, String category) {
        try {
            String url = "http://so.youku.com/search_video/q_" + URLEncoder.encode(film, "UTF-8") + "?spm=a2hww.11359951.#qheader_search~10";
            Document doc = Jsoup.connect(url).timeout(300000).get();

            //统一类型
            if(category != null && category.equals("电视剧")) {
                category = "剧集";
            }

            url = extractDetailUrlFromSearchResult(doc, film, category);
            if(url != null) {
                doc = Jsoup.connect(url).timeout(300000).get();
                return parseInDetailPage(doc, film);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static FilmInfo parseInSearchResult(Document doc, final String film) {
        final FilmInfo info = new FilmInfo();

        Elements eScriptList = doc.getElementsByTag("script");
        for(Element e : eScriptList) {
            String html = e.html();
            if(html.startsWith("bigview.view({\"domid\":\"bpmodule-main\"")) {
                html = html.substring(13, html.lastIndexOf(')'));

                JSONObject json = new JSONObject(html);
                html = json.getString("html");
//                System.out.println(html);

                doc = Jsoup.parse(html);
                Elements es1 = doc.select(".sk-result-list .mod-main");
                for(Element e1 : es1) {
                    if(info.name == null) {
                        Elements es2 = e1.select(".mod-header .spc-lv-1 a");
                        if(!es2.isEmpty()) {
                            String name = es2.get(0).attr("title");
                            //found the result.
                            if(film.equals(name)) {
                                info.name = film;

                                Elements es3 = e1.select(".mod-info span");
                                for(Element e3 : es3) {
                                    String text = e3.text();
                                    if(text.startsWith("上映时间:")) {
                                        info.release = text.substring(5).trim();
                                    } else if(text.startsWith("导演:")) {
                                        info.director = text.substring(3).trim();
                                    } else if(text.startsWith("主演:")) {
                                        info.character = text.substring(3).trim();
                                    } else if(text.startsWith("简介:")) {
                                        info.brief = text.substring(3).trim();
                                    }
                                }

                                return info;
                            }
                        }
                    }
                }
            }
        }

        return info;
    }


    protected static String extractDetailUrlFromSearchResult(Document doc, final String film, String category) {
        Elements eScriptList = doc.getElementsByTag("script");
        for(Element e : eScriptList) {
            String html = e.html();
            if(html.startsWith("bigview.view({\"domid\":\"bpmodule-main\"")) {
                html = html.substring(13, html.lastIndexOf(')'));

                JSONObject json = new JSONObject(html);
                html = json.getString("html");
//                System.out.println(html);

                doc = Jsoup.parse(html);
                Elements es1 = doc.select(".sk-result-list .mod-main");
                for(Element e1 : es1) {
                    if(category != null) {
                        Elements es2 = e1.select(".mod-header .base-type");
                        if(!es2.isEmpty()) {
                            //搜索时指定了类型，排除类型不一致的结果
                            if(!es2.get(0).text().equals(category)) {
                                continue;
                            }
                        }
                    }

                    Elements es3 = e1.select(".mod-header .spc-lv-1 a");
                    if(!es3.isEmpty()) {
                        String name = es3.get(0).attr("title");

                        //found the result.
                        //找到完全匹配的记录
                        if(film.equals(name)) {
                            Elements es4 = e1.select(".mod-info .row-ellipsis a");
                            if(es4.size() > 0) {
                                for (Element e4 : es4) {
                                    if(e4.text().equals("节目详情 >")) {
//                                        System.out.println(e4.attr("href"));
                                        return e4.attr("href");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }


    protected static FilmInfo parseInDetailPage(Document doc, final String film) {
        final FilmInfo info = new FilmInfo();

        Elements eLiList = doc.select(".s-body .yk-content .p-base li");
        for(Element eLi : eLiList) {
            String text = eLi.text();
            if(eLi.classNames().contains("p-title")) {
                info.name = film;
                Elements es = eLi.select(".sub-title");
                if(es.size() > 0) {
                    info.age = es.get(0).text();//年代
                }
            } else if(eLi.classNames().contains("p-renew")) {
                info.episode = text;//集数
            } else if(text.startsWith("上映：")) {
                info.release = text.substring(3);
            } else if(text.startsWith("地区：")) {
                info.region = text.substring(3);
            } else if(text.startsWith("主演：")) {
                info.character = text.substring(3);
            } else if(text.startsWith("导演：")) {
                info.director = text.substring(3);
            } else if(text.startsWith("简介：")) {
                info.brief = text.substring(3);
            }
        }

        return info;
    }

    public static void main(String[] args) {
        FilmInfo info = new YoukuFilmInfoSearcher().search("暗战", "电视剧");
        System.out.println(info.name);
        System.out.println(info.episode);
        System.out.println(info.age);
        System.out.println(info.release);
        System.out.println(info.region);
        System.out.println(info.director);
        System.out.println(info.character);
        System.out.println(info.brief);
    }
}
