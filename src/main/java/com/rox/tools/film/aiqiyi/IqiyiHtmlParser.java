package com.rox.tools.film.aiqiyi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rox on 2017/7/3.
 */
public class IqiyiHtmlParser {
    public static void main(String[] args) {
        try {
            File input = new File("xsqt.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            Element albumpic = doc.getElementById("albumpic-showall-wrap");

            if(albumpic == null) {
                System.err.println("找不到内容：albumpic-showall-wrap");
                return;
            }

            Elements liList = albumpic.getElementsByTag("li");

            if(liList == null || liList.isEmpty()) {
                System.err.println("内容列表为空");
                return;
            }

            System.out.printf("发现%d期内容.\r\n", liList.size());

            for (Element li : liList) {
                Elements ts = li.getElementsByClass("mod-listTitle_right");
                if(ts == null || ts.isEmpty()) {
                    System.err.println("未找到期号");
                    continue;
                }

                String no = ts.get(0).text().substring(0, 10).replace("-", "");

                Elements rtList = li.getElementsByClass("graphics-type-rt");
                if(rtList == null || rtList.isEmpty()) {
                    System.err.println("未找到片名(rt)");
                    continue;
                }

                Elements aList = rtList.get(0).getElementsByTag("a");
                if(aList == null || aList.isEmpty()) {
                    System.err.println("未找到片名(a)");
                    continue;
                }

                String film = aList.get(0).text();
//                System.out.printf("%s  %s\n", no, film);

//                System.out.printf("rename %s*.ts 晓松奇谈%s.ts\n", film, no);
                System.out.printf("cache.put(\"%s\", \"%s\");\n", no, film);
/*
                String linkHref = li.attr("href");
                String linkText = li.text();

                System.out.printf("%s: %s\n", linkHref, linkText);
*/

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
