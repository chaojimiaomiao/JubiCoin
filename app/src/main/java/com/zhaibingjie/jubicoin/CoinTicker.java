package com.zhaibingjie.jubicoin;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhaibingjie on 2017/6/19.
 */

public class CoinTicker implements Serializable {

    TickerPrice btc;
    TickerPrice eth;
    TickerPrice ltc;
    TickerPrice etc;
    TickerPrice xsgs;
    TickerPrice game;
    TickerPrice ans;
    TickerPrice lsk;
    TickerPrice ppc;
    TickerPrice vrc;
    TickerPrice rss;
    TickerPrice vtc;
    TickerPrice ktc;
    TickerPrice xpm;
    TickerPrice blk;
    TickerPrice fz;
    TickerPrice bts;
    TickerPrice tfc;
    TickerPrice xrp;
    TickerPrice xas;
    TickerPrice rio;
    TickerPrice pgc;
    TickerPrice nxt;
    TickerPrice wdc;
    TickerPrice max;
    TickerPrice zcc;
    TickerPrice mryc;
    TickerPrice qec;
    TickerPrice plc;
    TickerPrice lkc;
    TickerPrice zet;
    TickerPrice ytc;
    TickerPrice peb;
    TickerPrice gooc;
    TickerPrice skt;
    TickerPrice mtc;
    TickerPrice jbc;
    TickerPrice hlb;
    TickerPrice met;
    TickerPrice dnc;
    TickerPrice doge;
    TickerPrice eac;
    TickerPrice ifc;

    public CoinTicker() {
        initKeys();
    }

    public ArrayList<String> nameMaps = new ArrayList<>();
    private void initKeys() {
        nameMaps.add("比特");
        nameMaps.add("以太");
        nameMaps.add("莱特");
        nameMaps.add("经典");
        nameMaps.add("雪山");
        nameMaps.add("游戏点");
        nameMaps.add("小蚁股");
        nameMaps.add("lsk");
        nameMaps.add("点点");
        nameMaps.add("维理");
        nameMaps.add("贝壳");
        nameMaps.add("绿币");
        nameMaps.add("肯特");
        nameMaps.add("质数");
        nameMaps.add("黑币");
        nameMaps.add("冰河");
        nameMaps.add("bts");
        nameMaps.add("传送");
        nameMaps.add("瑞波");
        nameMaps.add("阿希");
        nameMaps.add("里约");
        nameMaps.add("乐园");
        nameMaps.add("未来");
        nameMaps.add("世界");
        nameMaps.add("最大");
        nameMaps.add("招财");
        nameMaps.add("美人鱼");
        nameMaps.add("企鹅");
        nameMaps.add("保罗");
        nameMaps.add("幸运");
        nameMaps.add("泽塔");
        nameMaps.add("一号");
        nameMaps.add("普银");
        nameMaps.add("谷壳");
        nameMaps.add("鲨鱼");
        nameMaps.add("美通");
        nameMaps.add("聚宝");
        nameMaps.add("活力");
        nameMaps.add("美通");
        nameMaps.add("暗网");
        nameMaps.add("狗狗");
        nameMaps.add("地球");
        nameMaps.add("无限");
    }

}
