/* 
 * To change this template, choose Tools | Templates 
 * and open the template in the editor. 
 */
package com.meihuayishu.vone.utils.bazi;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 完整八字排盘系统，接口入口
 */
public class PaipanClass {

    private BaZi lunar;
    public String[] bazi;
    public String[] DayunArray;
    private int shengsibiaoshunxu, shishengbiaoshunxu;
    private String[] zhanggan;
    private Calendar cal;
    private String ganziyear, ganzimonth, ganziday, ganzitime;
    private Lvhehun.sex isman;

    private ShenShaHehun myShenShaHehun = new ShenShaHehun();
    private Paipandayun mPaipandayun = new Paipandayun();
    private ShenSha myShenSha = new ShenSha();
    private PaipanShiShen mPaipanshisheng = new PaipanShiShen();
    private DiZang mDizhang = new DiZang();

    public PaipanClass(String time, Lvhehun.sex isman) {
        this.cal = getCalendar(time);
        this.isman = isman;
        lunar = new BaZi(cal);
        Lunar lunaryue = new Lunar(cal.getTime());
        String GanZhi = lunar.getYearGanZhi(cal.get(Calendar.HOUR_OF_DAY) / 2);//取八字
        this.ganziyear = lunaryue.getCyclicaYear();//年柱
        this.ganzimonth = lunaryue.getCyclicaMonth();//月柱
        this.ganziday = lunaryue.getCyclicaDay();//日柱
        this.ganzitime = GanZhi.split(",")[3];//时柱
        bazi = new String[]{"", ganziyear.substring(0, 1), ganziyear.substring(1, 2),
                ganzimonth.substring(0, 1), ganzimonth.substring(1, 2),
                ganziday.substring(0, 1), ganziday.substring(1, 2),
                ganzitime.substring(0, 1), ganzitime.substring(1, 2)};


        this.zhanggan = CommonClass.dizhuang(ganziyear.substring(1, 2));
        this.shishengbiaoshunxu = mPaipandayun.getyuezhuStart(BaZi.Gan, ganziday.substring(0, 1));
        this.shishengbiaoshunxu++;
        this.shengsibiaoshunxu = mPaipandayun.getyuezhuStart(BaZi.shengsibiao, ganziday.substring(0, 1));
        this.shengsibiaoshunxu++;

        Log.i("MSL", "PaipanClass: " + ganzimonth);
        DayunArray = mPaipandayun.Dayun(ganziyear, ganzimonth, isman);
    }

    /**
     * @param man 生日 yyyy-MM-dd HH
     * @return
     * @throws ParseException
     */
    private String paipan(String man, Lvhehun.sex isman) throws ParseException {

        Calendar mancal;

        try {
            mancal = myShenShaHehun.getCalendarfromString(man, "yyyy-MM-dd HH");
            //原来的private 方法改了下  
        } catch (ParseException ex) {
            return "输入不正确" + ex.getMessage();
        }

        return paipan(mancal, isman);

    }

    /**
     * 将1990-1-1 18 这样的String转化为Calendar格式的时间戳
     *
     * @param man "1990-1-1 18" String类型
     * @return
     */
    private Calendar getCalendar(String man) {

        try {
            return myShenShaHehun.getCalendarfromString(man, "yyyy-MM-dd HH");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }




    /**获取农历日期*/
    public String getLunarDate() {
        return lunar.toString();
    }

    /**获取生肖*/
    public String getAnimal() {

        return lunar.animalsYear();
    }

    /**获取四柱五行纳音*/
    public String[] getSiZhuSound(){
        return new String[]{ShenShaHehun.getnumsix(ganziyear),ShenShaHehun.getnumsix(ganzimonth),
        ShenShaHehun.getnumsix(ganziday),ShenShaHehun.getnumsix(ganzitime)};
    }

    /**获取四柱对应的神煞，
     * 显示时需要根据"年柱：" "\n"  "月柱：" "\n" "日柱：" "\n" "时柱："等拆分，
     * 再根据" " 拆分，得到每一个神煞，然后显示在控件上
     * @return String类型
     */
    public String getShenSha() {
        return myShenSha.shengsha(bazi, isman);
    }

    /**
     * 获取干支十神
     * @return 3个元素
     */
    public String[] getGanZhiShiShen() {
        //此人四柱干支十神
        return new String[]{mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun
                .getyuezhuStart(mPaipanshisheng.shisheng[0], ganziyear.substring(0, 1))],//年柱
                mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun
                        .getyuezhuStart(mPaipanshisheng.shisheng[0], ganzimonth.substring(0, 1))],//月柱
                //日柱不计算！
                mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun
                        .getyuezhuStart(mPaipanshisheng.shisheng[0], ganzitime.substring(0, 1))]//时柱
        };
    }

    /**获取年藏干
     * @return 年藏干，key：地支 ，value：藏干
     */
    public Map<String, String> getYearZangGan() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < zhanggan.length; i++) {

            if (zhanggan[i] == null) {
                continue;
            }
            map.put(zhanggan[i], mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]);
        }
        return map;
    }
    /**获取月藏干
     * @return 月藏干，key：地支 ，value：藏干
     */
    public Map<String,String> getMonthZanggan(){
        zhanggan = CommonClass.dizhuang(ganzimonth.substring(1, 2));
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            map.put(zhanggan[i], mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]);
        }
        return map;
    }
    /**获取日藏干
     * @return 日藏干，key：地支 ，value：藏干
     */
    public Map<String,String> getDayZanggan(){
        Map<String,String> map = new HashMap<>();
        zhanggan = CommonClass.dizhuang(ganziday.substring(1, 2));
        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            map.put(zhanggan[i], mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]);
        }
        return map;
    }
    /**获取时藏干
     * @return 时藏干，key：地支 ，value：藏干
     */
    public Map<String,String> getHourZanggan(){
        Map<String,String> map = new HashMap<>();
        zhanggan = CommonClass.dizhuang(ganzitime.substring(1, 2));
        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            map.put(zhanggan[i], mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]);

        }
        return map;
    }

    /**起大运周岁*/
    public long getDayunStartAge () {
        return mDizhang.dayunkaishi(cal, ganziyear.substring(0, 1), isman);
    }

    /**获取大运生旺死绝*/
    public String[] getDayunShengsi(){
        String[] DayunArrayshengsi = new String[DayunArray.length];//大运十天干 生旺死绝表
        for (int i = 0; i < DayunArray.length; i++) {
            DayunArrayshengsi[i] = mPaipanshisheng.shengwang[0][mPaipandayun.getyuezhuStart(mPaipanshisheng.shengwang[shengsibiaoshunxu], DayunArray[i].substring(1, 2))]; //十天干生旺死绝表 用干查表
        }
        return DayunArrayshengsi;
    }
    /**获取大运十神*/
    public String[] getDayunShishen() {
        String[] DayunArrayshisheng = new String[DayunArray.length];//大运十神表
        for (int i = 0; i < DayunArray.length; i++) {
            DayunArrayshisheng[i] = mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], DayunArray[i].substring(0, 1))]; //十神表 用支查表
        }
        return DayunArrayshisheng;
    }

    /**获取大运神煞 每一个元素需要根据" "再次拆分 */
    public String[] getDayunShensha() {
        String[] DayunArrayshengsha= new String[DayunArray.length];
        for (int i = 0; i < DayunArray.length; i++) {
            DayunArrayshengsha[i] = myShenSha.shengshadayun(DayunArray[i], bazi, isman);
        }
        return DayunArrayshengsha;
    }

    /**
     * 获取流年
     * @return 每一个元素的类型：1995|乙亥|　　神煞：|文昌 驿马 丧门，可根据"|"拆分开
     */
    public String[] getLiunian(){
        int[] liunianarray = new int[92];
        int start = lunar.getnumberYear();
        start++;
        for (int i = 0; i < liunianarray.length; i++) {

            liunianarray[i] = start + i;
        }

        return myShenSha.liunianshensha(liunianarray, bazi, isman);
    }

    /**
     * 获取神煞合婚
     * @return
     */
    public String getHunyinShenSha(){
        String month = lunar.getMonth();//取表格中的中国农历月份
        int number = lunar.getYearindex() + 1;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 14; i++) {
            if (month.equalsIgnoreCase(ShenShaHehun.shensha1[number][i])) {
                result.append("犯" + ShenShaHehun.shensha1[0][i]);
                result.append(" ");
            }
            if (month.equalsIgnoreCase(ShenShaHehun.shensha2[number][
                    i])) {
                result.append("犯" + ShenShaHehun.shensha2[0][i]);
                result.append(" ");
            }
        }
        return result.toString();
    }

    /**桃花位*/
    public String getTaohua(){
        String returnString = "";
        if ((bazi[2].equals("寅")) || (bazi[2].equals("午")) || (bazi[2].equals("戌"))) {
            returnString += "卯";
        }
        if ((bazi[2].equals("申")) || (bazi[2].equals("子")) || (bazi[2].equals("辰"))) {
            returnString += "酉";
        }
        if ((bazi[2].equals("亥")) || (bazi[2].equals("卯")) || (bazi[2].equals("未"))) {
            returnString += "子";
        }
        if ((bazi[2].equals("巳")) || (bazi[2].equals("酉")) || (bazi[2].equals("丑"))) {
            returnString += "午";
        }
        return returnString;
    }

    /**
     * 甲子纳音
     * @param paramString 年份干支
     * @return 纳音
     */
    public String getGanZhiSound(String paramString){
        if ((paramString.equals("甲子")) || (paramString.equals("乙丑"))) {
            return "海中金";
        }
        if ((paramString.equals("丙寅")) || (paramString.equals("丁卯"))) {
            return "炉中火";
        }
        if ((paramString.equals("戊辰")) || (paramString.equals("己巳"))) {
            return "大林木";
        }
        if ((paramString.equals("庚午")) || (paramString.equals("辛未"))) {
            return "路旁土";
        }
        if ((paramString.equals("壬申")) || (paramString.equals("癸酉"))) {
            return "剑锋金";
        }
        if ((paramString.equals("甲戌")) || (paramString.equals("乙亥"))) {
            return "山头火";
        }
        if ((paramString.equals("丙子")) || (paramString.equals("丁丑"))) {
            return "涧下水";
        }
        if ((paramString.equals("戊寅")) || (paramString.equals("己卯"))) {
            return "城头土";
        }
        if ((paramString.equals("庚辰")) || (paramString.equals("辛巳"))) {
            return "白蜡金";
        }
        if ((paramString.equals("壬午")) || (paramString.equals("癸未"))) {
            return "杨柳木";
        }
        if ((paramString.equals("甲申")) || (paramString.equals("乙酉"))) {
            return "泉中水";
        }
        if ((paramString.equals("丙戌")) || (paramString.equals("丁亥"))) {
            return "屋上土";
        }
        if ((paramString.equals("戊子")) || (paramString.equals("己丑"))) {
            return "霹雳火";
        }
        if ((paramString.equals("庚寅")) || (paramString.equals("辛卯"))) {
            return "松柏木";
        }
        if ((paramString.equals("壬辰")) || (paramString.equals("癸巳"))) {
            return "长流水";
        }
        if ((paramString.equals("甲午")) || (paramString.equals("乙未"))) {
            return "沙中金";
        }
        if ((paramString.equals("丙申")) || (paramString.equals("丁酉"))) {
            return "山下火";
        }
        if ((paramString.equals("戊戌")) || (paramString.equals("己亥"))) {
            return "平地木";
        }
        if ((paramString.equals("庚子")) || (paramString.equals("辛丑"))) {
            return "壁上土";
        }
        if ((paramString.equals("壬寅")) || (paramString.equals("癸卯"))) {
            return "金箔金";
        }
        if ((paramString.equals("甲辰")) || (paramString.equals("乙巳"))) {
            return "佛灯火";
        }
        if ((paramString.equals("丙午")) || (paramString.equals("丁未"))) {
            return "天河水";
        }
        if ((paramString.equals("戊申")) || (paramString.equals("己酉"))) {
            return "大驿土";
        }
        if ((paramString.equals("庚戌")) || (paramString.equals("辛亥"))) {
            return "钗钏金";
        }
        if ((paramString.equals("壬子")) || (paramString.equals("癸丑"))) {
            return "桑柘木";
        }
        if ((paramString.equals("甲寅")) || (paramString.equals("乙卯"))) {
            return "大溪水";
        }
        if ((paramString.equals("丙辰")) || (paramString.equals("丁巳"))) {
            return "沙中土";
        }
        if ((paramString.equals("戊午")) || (paramString.equals("己未"))) {
            return "天上火";
        }
        if ((paramString.equals("庚申")) || (paramString.equals("辛酉"))) {
            return "石榴木";
        }
        if ((paramString.equals("壬戌")) || (paramString.equals("癸亥"))) {
            return "大海水";
        }
        return "";
    }





    private String paipan(Calendar cal, Lvhehun.sex isman) throws ParseException {


        BaZi lunar = new BaZi(cal);
        Lunar lunaryue = new Lunar(cal.getTime());
        System.out.println("此人农历的日期【" + lunar.toString() + "】");
        /**
         * 很多地方都是按照23：00-1：00为子时这是不对的。 
         * 子时24.00－2.00,丑时2.00－4.00,寅时4.00－6.00,卯时6.00－8.00, 
         * 辰时8.00－10.00,巳时10.00－12.00,午时12.00－14.00,未时14.00－16.00 
         * 申时16.00－18.00,酉时18.00－20.00,戌时20.00－22.00,亥时22.00－24.00 
         *
         */
        int time = cal.get(Calendar.HOUR_OF_DAY) / 2;

        //获取生肖  
        System.out.println("此人的农历生肖【" + lunar.animalsYear() + "】");


        String GanZhi = lunar.getYearGanZhi(time);//取八字
        String[] tempchar = GanZhi.split(",");
        //我修改原来的，用,分割  
        String ganziyear = lunaryue.getCyclicaYear();//年柱  
        String ganzimonth = lunaryue.getCyclicaMonth();//月柱  
        String ganziday = lunaryue.getCyclicaDay();//日柱  
        String ganzitime = tempchar[3];//时柱  

        System.out.println("此人八字【" + ganziyear + " " + ganzimonth + " " + ganziday + " " + ganzitime + "】");

        String[] arrayOfString = new String[9];

        arrayOfString[0] = "";
        arrayOfString[1] = ganziyear.substring(0, 1);//年干  
        arrayOfString[2] = ganziyear.substring(1, 2);//年支  
        arrayOfString[3] = ganzimonth.substring(0, 1);//月干  
        arrayOfString[4] = ganzimonth.substring(1, 2);//月支  
        arrayOfString[5] = ganziday.substring(0, 1);//日干  
        arrayOfString[6] = ganziday.substring(1, 2);//日支  
        arrayOfString[7] = ganzitime.substring(0, 1);//时干  
        arrayOfString[8] = ganzitime.substring(1, 2);//时支  
        System.out.println(myShenSha.shengsha(arrayOfString, isman));


        //排食神生旺死绝  
        //用日干 日支分别查找位于表的对应值  
        //修改原文的类方法字段属性为public  
        //我的表格均是按照顺序比如  
        //十天干生旺死绝表 十神概念按顺序排列  
        //查找某一行就可以查到对应值  

        //十天干生旺死绝表 用日干查表  

        int shengsibiaoshunxu = mPaipandayun.getyuezhuStart(BaZi.shengsibiao, ganziday.substring(0, 1));
        //十神表按顺序  
        int shishengbiaoshunxu = mPaipandayun.getyuezhuStart(BaZi.Gan, ganziday.substring(0, 1));

        shengsibiaoshunxu++;
        shishengbiaoshunxu++;


        String[] DayunArray = mPaipandayun.Dayun(ganziyear, ganzimonth, isman);

        //排此人四柱十神生旺死绝  

        //       比　　　　印　　　　日元　　　劫　　　<- 这里直接用四柱干支计算　  
        //乾造　　庚　　　　己　　　　庚　　　　辛  
        //　　　　辰　　　　卯　　　　午　　　　巳　　　　  
        //藏干　　乙戊癸　　乙　　　　己丁　　　庚丙戊　 <- 这里直接用藏干计算  
        //　　　　才枭伤　　才　　　　印官　　　比杀枭　 <- 这里直接用藏干计算  
        //地势　　养　　　　胎　　　　沐浴　　　长生　　　<- 藏干不算生旺死绝  
        //纳音　　白蜡金　　城墙土　　路旁土　　白蜡金  

        System.out.println("此人四柱干支十神");
        System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], ganziyear.substring(0, 1))]); //十神表 用支查表
//        System.out.print(" ");
        System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], ganzimonth.substring(0, 1))]); //十神表 用支查表
//        System.out.print(" ");
        System.out.print("日主"); //日柱不计算！  
//        System.out.print(" ");
        System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], ganzitime.substring(0, 1))]); //十神表 用支查表

        System.out.println("");

        System.out.println("此人年藏干");
        String[] zhanggan = CommonClass.dizhuang(ganziyear.substring(1, 2));
        for (int i = 0; i < zhanggan.length; i++) {

            if (zhanggan[i] == null) {
                continue;
            }
            System.out.print(zhanggan[i]);
//            System.out.print(" ");
            System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]); //十神表 用支查表
//            System.out.print(" ");


        }
        System.out.println("");
        zhanggan = CommonClass.dizhuang(ganzimonth.substring(1, 2));
        System.out.println("此人月藏干");

        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            System.out.print(zhanggan[i]);
//            System.out.print(" ");
            System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]); //十神表 用支查表
//            System.out.print(" ");


        }
        System.out.println("");
        System.out.println("此人日藏干");
        zhanggan = CommonClass.dizhuang(ganziday.substring(1, 2));
        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            System.out.print(zhanggan[i]);
//            System.out.print(" ");
            System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]); //十神表 用支查表
//            System.out.print(" ");


        }

        System.out.println("");
        System.out.println("此人时藏干");
        zhanggan = CommonClass.dizhuang(ganzitime.substring(1, 2));
        for (int i = 0; i < zhanggan.length; i++) {
            if (zhanggan[i] == null) {
                continue;
            }
            System.out.print(zhanggan[i]);
//            System.out.print(" ");
            System.out.print(mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], zhanggan[i])]); //十神表 用支查表
//            System.out.print(" ");


        }




        String[] DayunArrayshengsi = new String[DayunArray.length];//大运十天干生旺死绝表
        String[] DayunArrayshisheng = new String[DayunArray.length];//大运十神表  


        for (int i = 0; i < DayunArray.length; i++) {
            DayunArrayshengsi[i] = mPaipanshisheng.shengwang[0][mPaipandayun.getyuezhuStart(mPaipanshisheng.shengwang[shengsibiaoshunxu], DayunArray[i].substring(1, 2))]; //十天干生旺死绝表 用干查表

            DayunArrayshisheng[i] = mPaipanshisheng.shisheng[shishengbiaoshunxu][mPaipandayun.getyuezhuStart(mPaipanshisheng.shisheng[0], DayunArray[i].substring(0, 1))]; //十神表 用支查表


        }
        System.out.println("此人大运");
        mPaipandayun.pringst(DayunArray);

        System.out.println("此人起大运周岁：");
        System.out.println(mDizhang.dayunkaishi(cal, ganziyear.substring(0, 1), isman) + "岁");

        System.out.println("此人大运生旺死绝");
        mPaipandayun.pringst(DayunArrayshengsi);
        System.out.println("此人大运十神");
        mPaipandayun.pringst(DayunArrayshisheng);


        String[] DayunArrayshengsha = new String[DayunArray.length];

        for (int i = 0; i < DayunArray.length; i++) {

            DayunArrayshengsha[i] = myShenSha.shengshadayun(DayunArray[i], arrayOfString, isman);
        }


        System.out.println("此人大运神煞");
        mPaipandayun.pringst(DayunArrayshengsha);


        System.out.println("此人流年");

        int[] liunianarray = new int[92];
        int start = lunar.getnumberYear();
        start++;
        for (int i = 0; i < liunianarray.length; i++) {

            liunianarray[i] = start + i;
        }
        mPaipandayun.pringst(myShenSha.liunianshensha(liunianarray, arrayOfString, isman));

        System.out.println("此人婚姻神煞:");
        ShenShaHehun myShenShaHehun = new ShenShaHehun();
        System.out.println(myShenShaHehun.shensha(cal));

        return null;


    }

    public PaipanClass() {}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, IOException {
        PaipanClass my = new PaipanClass();
        System.out.println("请输入你的年月日时间类似 2013-3-14 20");
        BufferedReader strin = new BufferedReader(new InputStreamReader(System.in));
        String a = strin.readLine();
        System.out.println("输入的是：" + a);
        my.paipan(a, Lvhehun.sex.man);
//        my.paipan(a, Lvhehun.sex.woman);
    }
}  