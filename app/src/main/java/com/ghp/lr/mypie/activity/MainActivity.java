package com.ghp.lr.mypie.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.ghp.lr.mypie.R;
import com.ghp.lr.mypie.utils.CommonUtility;
import com.ghp.lr.mypie.view.FundPieChartLabel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity {
    @InjectView(R.id.pieViewLayout)RelativeLayout pieViewLayout;
    @InjectView(R.id.itemTipImg)View itemTipImg;
    private FundPieChartLabel pieView;
    private List<String> fundDataList=Arrays.asList("200000", "1800000", "1600000", "100000");
    private int ScrWidth;
    private int ScrHight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("给饼图标上百分比");
        ButterKnife.inject(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrWidth = dm.widthPixels;
        ScrHight=dm.heightPixels;
        initPieView();
    }
    private void initPieView(){
        pieViewLayout.removeAllViews();
        pieView=new FundPieChartLabel(this, pieViewLayout);
        pieView.setDataList(fundDataList);
        pieView.invalidate();
        pieViewLayout.addView(pieView);
        setItemTipImg();
    }
    //设置今日资金图片的位置
    private void setItemTipImg(){
        int imgSize = ScrWidth/6;
        CommonUtility.setLayout(itemTipImg, ScrWidth / 2, ScrWidth / 2 - ScrWidth / 20, -imgSize / 2, -imgSize / 2, imgSize, imgSize);
        itemTipImg.setVisibility(View.VISIBLE);
    }
}
