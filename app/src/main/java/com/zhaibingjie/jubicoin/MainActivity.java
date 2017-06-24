package com.zhaibingjie.jubicoin;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 共三种模式
 * 追星模式，只看涨幅和累计
 * 震荡模式，看短时间内跌幅和涨幅（若有波段可从中获利）
 * 后台模式，只看累计涨幅
 */
public class MainActivity extends Activity implements View.OnClickListener {
    final static int INTERVAL = 1500;
    final static int INTERVAL_ONEMINUTE = 1000 * 60 * 1;//一分钟
    final static int INTERVAL_TWOMINUTE = 1500 * 60;//震荡
    final static int STAR_INTERVAL = 1000 * 10 *1;//10s
    Button startBtn, stopBtn, backBtn, starBtn, vibrateBtn;
    ListView listView;
    //SimpleAdapter simpleAdapter;
    MyAdapter myAdapter;
    boolean isStop, isStop2;
    int nowInterval = 1000;

    LinearLayout backHint;
    TextView diefu, zhangfu;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            if (!isStop) {
                getAllPrices();
                handler.postDelayed(this, INTERVAL);
            }
        }
    };

    Handler handler2 = new Handler();
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            if (!isStop2) {
                getAllTicker();
                handler2.postDelayed(this, nowInterval);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        starBtn = (Button) findViewById(R.id.main_star);
        startBtn = (Button) findViewById(R.id.main_start);
        stopBtn = (Button) findViewById(R.id.main_stop);
        backBtn = (Button) findViewById(R.id.main_background);
        vibrateBtn = (Button) findViewById(R.id.main_vibrate);
        listView = (ListView) findViewById(R.id.coin_list);
        backHint = (LinearLayout) findViewById(R.id.back_hint);
        diefu = (TextView) findViewById(R.id.main_diefu);
        zhangfu = (TextView) findViewById(R.id.main_zhangfu);

        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        starBtn.setOnClickListener(this);
        vibrateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_start:
                initLastList();
                initList();
                isStop = false;
                //handler.postDelayed(runnable, INTERVAL);//每两秒执行一次runnable.
                break;
            case R.id.main_stop:
                isStop = true;
                isStop2 = true;
                //handler.removeCallbacks(runnable);
                handler2.removeCallbacks(runnable2);
                break;
            case R.id.main_background:
                nowInterval = INTERVAL_ONEMINUTE;
                initLeiji();
                handler2.post(runnable2);
                break;
            case R.id.main_star:
                nowInterval = STAR_INTERVAL;
                initLeiji();
                handler2.post(runnable2);
                break;
            case R.id.main_vibrate:
                nowInterval = INTERVAL_TWOMINUTE;
                initLeiji();
                handler2.post(runnable2);
                break;
        }
    }

    private ArrayList<CoinPrice> coins = new ArrayList<>();
    private ArrayList<CoinPrice> lastcoins = new ArrayList<>();
    int cishu;
    int coinNum;

    private void refreshList() {
        myAdapter.setData(coins, lastcoins);
        myAdapter.notifyDataSetChanged();
    }

    private void getAllTickerInBac() {

    }

    private ArrayList<Float> lstPrice = new ArrayList<>();
    private ArrayList<Float> nowPrice = new ArrayList<>();
    private ArrayList<Float> leijiPrice = new ArrayList<>();
    int lstNum = 0;

    private void getAllTicker() {//后台调用，不显示到主界面，仅显示提示
        String url = "http://www.jubi.com/api/v1/allticker/";
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        if (response != null) {
                            Gson gson = new Gson();
                            CoinTicker ticker = gson.fromJson(response.toString(), CoinTicker.class);
                            if (lstNum == 0) {
                                lstNum ++ ;
                                addLstPrice(ticker);
                            } else if (lstNum ==1 ){
                                lstNum ++;
                                addNowPrice(ticker);
                            } else {
                                lstPrice.clear();
                                lstPrice.addAll(nowPrice);
                                nowPrice.clear();
                                addNowPrice(ticker);
                                for (int i=0; i < nowPrice.size(); i++) {
                                    float wt = (nowPrice.get(i) - lstPrice.get(i)) /lstPrice.get(i);
                                    leijiPrice.set(i, leijiPrice.get(i) + wt);
                                    if (nowInterval == STAR_INTERVAL) {
                                        if (wt > 0.01) {//百分之一
                                            zhangfu.setText(zhangfu.getText() + ticker.nameMaps.get(i) + "：涨幅为"+ wt*100 +
                                                    "%，累计：" + leijiPrice.get(i)*100 +"%\n");
                                        } else if (wt < -0.01) {
                                            //diefu.setText(diefu.getText() + ticker.nameMaps.get(i) + "：跌幅为"+ wt*100 + "%\n");
                                        }
                                    } else if (nowInterval == INTERVAL_ONEMINUTE)  {
                                        if (leijiPrice.get(i) > 0.05) {
                                            zhangfu.setText(zhangfu.getText() + ticker.nameMaps.get(i)+
                                                    "  累计：" + leijiPrice.get(i)*100 +"%\n");
                                        } else if (leijiPrice.get(i) < -0.05)  {
                                            diefu.setText(diefu.getText() + ticker.nameMaps.get(i) +
                                                    "  累计：" + leijiPrice.get(i)*100 +"%\n");
                                        }
                                    } else if (nowInterval == INTERVAL_TWOMINUTE)  {
                                        if (wt > 0.015) {//百分之一
                                            zhangfu.setText(zhangfu.getText() + ticker.nameMaps.get(i) + "：涨幅为"+ wt*100 +
                                                    "%，累计：" + leijiPrice.get(i)*100 +"%\n");
                                        } else if (wt < -0.015) {
                                            diefu.setText(diefu.getText() + ticker.nameMaps.get(i) + "：跌幅为"+ wt*100 + "%\n");
                                        }
                                    }

                                }
                            }
                            Log.e("MainActivity", "" + ticker);

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        ParamFactory.getVolley(this).add(jsonRequest);
    }

    private void getAllPrices() {//按价格排序,43个
        getTicker("btc", 0);
        getTicker("eth", 1);
        getTicker("ltc", 2);
        getTicker("etc", 3);
        getTicker("xsgs", 4);
        getTicker("game", 5);
        getTicker("ans", 6);
        getTicker("lsk", 7);
        getTicker("ppc", 8);
        getTicker("vrc", 9);
        getTicker("rss", 10);
        getTicker("vtc", 11);
        getTicker("ktc", 12);
        getTicker("xpm", 13);
        getTicker("blk", 14);
        getTicker("fz", 15);
        getTicker("bts", 16);
        getTicker("tfc", 17);
        getTicker("xrp", 18);
        getTicker("xas", 19);
        getTicker("rio", 20);
        getTicker("pgc", 21);
        getTicker("nxt", 22);
        getTicker("wdc", 23);
        getTicker("max", 24);
        getTicker("zcc", 25);
        getTicker("mryc", 26);
        getTicker("qec", 27);
        getTicker("plc", 28);
        getTicker("lkc", 29);
        getTicker("zet", 30);
        getTicker("ytc", 31);
        getTicker("peb", 32);
        getTicker("gooc", 33);
        getTicker("skt", 34);
        getTicker("mtc", 35);
        getTicker("jbc", 36);
        getTicker("hlb", 37);
        getTicker("met", 38);
        getTicker("dnc", 39);
        getTicker("doge", 40);
        getTicker("eac", 41);
        getTicker("ifc", 42);
    }

    private void initList() {
        coins.clear();
        for (int i =0; i < 43; i++) {
            coins.add(new CoinPrice());
        }
    }
    private void initLastList() {
        lastcoins.clear();
        for (int i =0; i < 43; i++) {
            lastcoins.add(new CoinPrice());
        }
    }

    private void getTicker(final String coinName, final int seq) {
        String url = ParamFactory.getPrice(coinName);
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        if (response != null) {
                            Gson gson = new Gson();
                            CoinPrice price = gson.fromJson(response.toString(), CoinPrice.class);
                            price.name = coinName;
                            if (cishu == 0) {
                                if (coinNum < 43) {
                                    lastcoins.set(seq, price);
                                    coinNum ++;
                                } else {
                                    coinNum = 1;
                                    cishu ++;
                                    coins.set(seq, price);
                                }
                            } else {
                                if (coinNum < 43) {
                                    coins.set(seq, price);
                                    coinNum ++;
                                } else {
                                    if (cishu == 1) {
                                        cishu ++;
                                        myAdapter = new MyAdapter(MainActivity.this);
                                        listView.setAdapter(myAdapter);//绑定适配器
                                    }
                                    refreshList();
                                    coinNum = 1;
                                    lastcoins.clear();
                                    lastcoins.addAll(coins);
                                    initList();
                                    coins.set(seq, price);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        ParamFactory.getVolley(this).add(jsonRequest);
    }

    private void addNowPrice(CoinTicker ticker) {
        nowPrice.add(ticker.btc.last);
        nowPrice.add(ticker.eth.last);
        nowPrice.add(ticker.ltc.last);
        nowPrice.add(ticker.etc.last);
        nowPrice.add(ticker.xsgs.last);
        nowPrice.add(ticker.game.last);
        nowPrice.add(ticker.ans.last);
        nowPrice.add(ticker.lsk.last);
        nowPrice.add(ticker.ppc.last);
        nowPrice.add(ticker.vrc.last);
        nowPrice.add(ticker.rss.last);
        nowPrice.add(ticker.vtc.last);
        nowPrice.add(ticker.ktc.last);
        nowPrice.add(ticker.xpm.last);
        nowPrice.add(ticker.blk.last);
        nowPrice.add(ticker.fz.last);
        nowPrice.add(ticker.bts.last);
        nowPrice.add(ticker.tfc.last);
        nowPrice.add(ticker.xrp.last);
        nowPrice.add(ticker.xas.last);
        nowPrice.add(ticker.rio.last);
        nowPrice.add(ticker.pgc.last);
        nowPrice.add(ticker.nxt.last);
        nowPrice.add(ticker.wdc.last);
        nowPrice.add(ticker.max.last);
        nowPrice.add(ticker.zcc.last);
        nowPrice.add(ticker.mryc.last);
        nowPrice.add(ticker.qec.last);
        nowPrice.add(ticker.plc.last);
        nowPrice.add(ticker.lkc.last);
        nowPrice.add(ticker.zet.last);
        nowPrice.add(ticker.ytc.last);
        nowPrice.add(ticker.peb.last);
        nowPrice.add(ticker.gooc.last);
        nowPrice.add(ticker.skt.last);
        nowPrice.add(ticker.mtc.last);
        nowPrice.add(ticker.jbc.last);
        nowPrice.add(ticker.hlb.last);
        nowPrice.add(ticker.met.last);
        nowPrice.add(ticker.dnc.last);
        nowPrice.add(ticker.doge.last);
        nowPrice.add(ticker.eac.last);
        nowPrice.add(ticker.ifc.last);
    }

    private void addLstPrice(CoinTicker ticker) {
        lstPrice.add(ticker.btc.last);
        lstPrice.add(ticker.eth.last);
        lstPrice.add(ticker.ltc.last);
        lstPrice.add(ticker.etc.last);
        lstPrice.add(ticker.xsgs.last);
        lstPrice.add(ticker.game.last);
        lstPrice.add(ticker.ans.last);
        lstPrice.add(ticker.lsk.last);
        lstPrice.add(ticker.ppc.last);
        lstPrice.add(ticker.vrc.last);
        lstPrice.add(ticker.rss.last);
        lstPrice.add(ticker.vtc.last);
        lstPrice.add(ticker.ktc.last);
        lstPrice.add(ticker.xpm.last);
        lstPrice.add(ticker.blk.last);
        lstPrice.add(ticker.fz.last);
        lstPrice.add(ticker.bts.last);
        lstPrice.add(ticker.tfc.last);
        lstPrice.add(ticker.xrp.last);
        lstPrice.add(ticker.xas.last);
        lstPrice.add(ticker.rio.last);
        lstPrice.add(ticker.pgc.last);
        lstPrice.add(ticker.nxt.last);
        lstPrice.add(ticker.wdc.last);
        lstPrice.add(ticker.max.last);
        lstPrice.add(ticker.zcc.last);
        lstPrice.add(ticker.mryc.last);
        lstPrice.add(ticker.qec.last);
        lstPrice.add(ticker.plc.last);
        lstPrice.add(ticker.lkc.last);
        lstPrice.add(ticker.zet.last);
        lstPrice.add(ticker.ytc.last);
        lstPrice.add(ticker.peb.last);
        lstPrice.add(ticker.gooc.last);
        lstPrice.add(ticker.skt.last);
        lstPrice.add(ticker.mtc.last);
        lstPrice.add(ticker.jbc.last);
        lstPrice.add(ticker.hlb.last);
        lstPrice.add(ticker.met.last);
        lstPrice.add(ticker.dnc.last);
        lstPrice.add(ticker.doge.last);
        lstPrice.add(ticker.eac.last);
        lstPrice.add(ticker.ifc.last);
    }

    private void initLeiji() {
        for (int i = 0; i < 43; i++) {
            leijiPrice.add(0f);
        }
    }
}
