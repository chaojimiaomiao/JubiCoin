package com.zhaibingjie.jubicoin;

import java.io.Serializable;

/**
 * Created by zhaibingjie on 2017/6/17.
 *
 {"high":"299.990000",
 "low":"217.300000",
 "buy":"289.500000",
 "sell":"289.900000",
 "last":"289.480000",
 "vol":170590.3884,
 "volume":46366130.869488}
 */

public class CoinPrice implements Serializable {
    String name;
    String high;
    String low;
    String buy;
    String sell;
    String last;
    String vol;
    float volume;
}
