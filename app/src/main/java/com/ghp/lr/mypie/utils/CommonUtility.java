package com.ghp.lr.mypie.utils;

import android.view.View;
import android.widget.RelativeLayout;

import java.text.DecimalFormat;

/**
 * Created  on 15/5/29.
 */
public class CommonUtility {

    public static String getFundMoneyFormatStr(String money){
        String formatStr="";
        float num=Float.parseFloat(money);
        DecimalFormat fmt=new DecimalFormat("0.#");
        if(num>=100000000){
            formatStr = fmt.format(num / 100000000)+"亿元";
        }
        else if(num>=10000){
            formatStr = fmt.format(num / 10000)+"万元";
        }
        else{
            if(num<0.01){
                formatStr=0+"元";
            }else{
                formatStr=fmt.format(num)+"元";
            }
        }
        return formatStr;
    }

    public static String getFundItemRate(float num){
        String rate=null;
        DecimalFormat fmt=new DecimalFormat("0.##");
        rate=Math.round(Float.parseFloat(fmt.format(num))*100)+"";
        return rate;
    }
    public static void setLayout(View view,int x,int y,int moveX,int moveY,int width,int hight)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, hight);
        layoutParams.setMargins(x+moveX,y+moveY,0,0);
        view.setLayoutParams(layoutParams);
    }
}
