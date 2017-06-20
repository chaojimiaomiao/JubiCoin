package com.zhaibingjie.jubicoin;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zhaibingjie on 2017/6/18.
 */
class MyAdapter extends BaseAdapter {
    final static float ERROR_CODE = -404;

    private Activity mActivity;
    private ArrayList<Data> fuccList = new ArrayList<>();

    public MyAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setData(ArrayList<CoinPrice> coins, ArrayList<CoinPrice> lastcoins) {
        ArrayList<Data> list = new ArrayList<Data>();
        for (int i = 0; i < 43; i++) {
            CoinPrice coin = coins.get(i);
            CoinPrice coinLast = lastcoins.get(i);
            Data tmpData = new Data();
            tmpData.name = coin.name;
            if (coin == null || coin.last == null || coinLast == null || coinLast.last == null || coin.last == "0"||coinLast.last =="0") {
                tmpData.price = "...";
                tmpData.vibrate = ERROR_CODE;
            } else {
                tmpData.price = coin.last;
                if (!coin.last.isEmpty() && !coinLast.last.isEmpty()) {
                    if (!coin.name.equals(coinLast.name)) {
                        tmpData.vibrate = ERROR_CODE;
                    } else {
                        Float price = Float.parseFloat(coin.last);
                        Float lstPrc = Float.parseFloat(coinLast.last);
                        tmpData.vibrate = (price - lstPrc) / lstPrc;
                    }
                }
            }
            list.add(tmpData);
        }
        fuccList.clear();
        fuccList.addAll(list);
    }

    @Override
    public int getCount() {
        return 43;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            /*
             * convertView为一个item条目对象，每个item滑过之后，创建的item对象有被重新销毁，为此安卓提供一种机制，
             * 创建的条目不被销毁而是供后来展现的item重复使用，大大提高了效率
             */
        View view = null;
        if (convertView == null) {  //如果没有可供重复使用的item View对象
            view = mActivity.getLayoutInflater().inflate(R.layout.coin_column, null);
            //如果view的布局很复杂，可以将内部的控件保存下来
            TextView textView1 = (TextView) view.findViewById(R.id.coin_name);
            TextView textView2 = (TextView) view.findViewById(R.id.coin_price);
            TextView textView3 = (TextView) view.findViewById(R.id.coin_vibrate);

            ViewSet set = new ViewSet();
            set.nameView = textView1;
            set.priceView = textView2;
            set.vibrateView = textView3;
            view.setTag(set);
        } else {
            view = convertView; //如果已经加载将重复使用
        }
        //不用重复的查找控件
        Data data = fuccList.get(position);
        ViewSet views = (ViewSet) view.getTag();
        views.nameView.setText(data.name);
        views.priceView.setText(data.price);
        if (data.vibrate == ERROR_CODE) {
            view.setBackgroundColor(Color.WHITE);
            views.vibrateView.setText(data.vibrate + "");
        } else if (Math.abs(data.vibrate) >= 0.02) {
            view.setBackgroundColor(mActivity.getResources().getColor(R.color.colorRed));
            views.vibrateView.setText(data.vibrate *100 + "%");
        } else {
            view.setBackgroundColor(mActivity.getResources().getColor(R.color.colorGreen));
            views.vibrateView.setText(data.vibrate *100 + "%");
        }
        return view;
    }

    //保存item控件
    static class ViewSet {
        TextView nameView;
        TextView priceView;
        TextView vibrateView;
    }

    private class Data {
        String name;
        String price;
        float vibrate;
    }
}
