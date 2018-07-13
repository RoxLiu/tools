/* 
 */
package com.rox.tools.film;

/************************************************************
 * @author Rox
 * @version 3.0.1
 * @Description: <pre>电影描述</pre>
 ************************************************************/
public class FilmInfo {
    public String name;
    public String subject;
    public String episode; //集数
    public String region; //地区
    public String label;
    public String language; //语言
    public String age; //年代
    public String director; //导演
    public String character;//主演
    public String actorList; //演员列表
    public String brief; //简介
    public String release; //上映时间


    public String toString() {
        return String.format("名称：%s\n集数：%s\n地区：%s\n年代：%s\n上映时间：%s\n导演：%s\n演员：%s\n简介：%s", name, episode, region, age, release, director, character, brief);
    }
}
