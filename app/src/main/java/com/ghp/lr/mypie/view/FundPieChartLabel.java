package com.ghp.lr.mypie.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ghp.lr.mypie.utils.CommonUtility;
import com.ghp.lr.mypie.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ghp on 15/6/11.
 */
public class FundPieChartLabel extends View {

    private float LINE_LENGTH;//斜线的长度
    private float PIE_R;//圆的半径
    private float RING_R;//圆环的宽度
    private float LEDGEMENT_LENGHT;//横线的长度
    private float AMALL_CIRCLE_R=5f;//小圆球半径
    private Context context;
    private static View fundItemLayout;
    private TextView fund_percent;
    private TextView percent_tip;
    private TextView fund_num;
    private TextView fund_item_name;
    private RelativeLayout pieViewLayout;
    public float fundAcount = 0;
    private boolean fundSize=false;
    public FundPieChartLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public FundPieChartLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }
    private int ScrWidth, ScrHeight;
    private float rx, ry;
    private List<Integer> colorList= Arrays.asList(getResources().getColor(R.color.pie_retail_outflow), getResources().getColor(R.color.f10_taking_color), getResources().getColor(R.color.f10_retained_profits_color), getResources().getColor(R.color.pie_retail_inflow));;
    private List<String> dataList=new ArrayList<String>();
    public FundPieChartLabel(Context context,RelativeLayout pieViewLayout) {
        super(context);
        this.context=context;
        this.pieViewLayout=pieViewLayout;
        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;
        LINE_LENGTH=ScrWidth/10;
        PIE_R=ScrWidth/5;
        RING_R=ScrWidth/36;
        LEDGEMENT_LENGHT=ScrWidth/30;

    }


    public void onDraw(Canvas canvas) {
        //画布背景
        //canvas.drawColor(Color.WHITE);
        //画笔初始化
        Paint paintBg=new Paint();
        //paintBg.setColor(R.attr.pie_bg_color);
        paintBg.setColor(getResources().getColor(R.color.fund_barchart_bg));

        Paint PaintMinorOutAmount = new Paint();
        PaintMinorOutAmount.setColor(colorList.get(0));

        Paint PaintMainOutAmount = new Paint();
        PaintMainOutAmount.setColor(colorList.get(1));
        PaintMainOutAmount.setStyle(Paint.Style.FILL);

        Paint PaintMainInAmount = new Paint();
        PaintMainInAmount.setColor(colorList.get(2));
        PaintMainInAmount.setStyle(Paint.Style.FILL);

        Paint PaintMinorInAmount = new Paint();
        PaintMinorInAmount.setColor(colorList.get(3));
        PaintMinorInAmount.setStyle(Paint.Style.FILL);

        Paint PaintBlue = new Paint();
        PaintBlue.setColor(Color.BLUE);
        PaintBlue.setStyle(Paint.Style.STROKE);//设置非填充



        //抗锯齿
        paintBg.setAntiAlias(true);
        PaintMinorOutAmount.setAntiAlias(true);
        PaintMainOutAmount.setAntiAlias(true);
        PaintMainInAmount.setAntiAlias(true);
        PaintMinorInAmount.setAntiAlias(true);

        PaintBlue.setTextSize(12);

        float cirX = ScrWidth / 2;//圆心X
        float cirY = ScrWidth / 2-ScrWidth/20;//圆心Y
        float radius = PIE_R;//半径
        //先画个圆作为饼图背景
        canvas.drawCircle(cirX, cirY, radius + RING_R, paintBg);
        //先画个圆确定下显示位置
        canvas.drawCircle(cirX, cirY, radius, PaintMinorInAmount);

        float arcLeft = cirX - radius;
        float arcTop = cirY - radius;
        float arcRight = cirX + radius;
        float arcBottom = cirY + radius;
        RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);
        RectF ringRectf=new RectF(arcLeft-RING_R, arcTop-RING_R, arcRight+RING_R, arcBottom+RING_R);
        ////////////////////////////////////////////////////////////
        //饼图标题注释
        //canvas.drawText("author:xiongchuanliang", 60, ScrHeight - 270, PaintBlue);

        //位置计算类
        XChartCalc xcalc = new XChartCalc();

        //实际用于计算的半径
        //float calcRadius = radius / 2;//标写％居扇形中间
        float calcRadius = radius + RING_R;
        ////////////////////////////////////////////////////////////
        if(dataList!=null){
            //初始角度
            for(int i=0;i<dataList.size();i++){
                fundAcount+=Float.parseFloat(dataList.get(i));
            }
            float pAngle1 = Float.parseFloat(dataList.get(0))/fundAcount*360;
            float pAngle2 = Float.parseFloat(dataList.get(1))/fundAcount*360;
            float pAngle3 = Float.parseFloat(dataList.get(2))/fundAcount*360;
            //float pAngle4 = 360f - pAngle1 - pAngle2 - pAngle3;
            float pAngle4=Float.parseFloat(dataList.get(3))/fundAcount*360;
            if((pAngle2+pAngle3)<(pAngle1+pAngle4)){
                fundSize=true;
            }
            //填充扇形
            canvas.drawArc(arcRF0, 0, pAngle1, true, PaintMinorOutAmount);

            //计算并在扇形中心标注上百分比    散户流出
            xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 / 2);
            //canvas.drawText(Float.toString(pAngle1) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
            float MinorOutAmountX=xcalc.getPosX();
            float MinorOutAmountY=xcalc.getPosY();
            float MinorOutAmountStopX;
            float MinorOutAmountStopY;
            if(!fundSize){
                MinorOutAmountStopX=MinorOutAmountX+LINE_LENGTH*(float)Math.cos(30);
                MinorOutAmountStopY=MinorOutAmountY-LINE_LENGTH*(float)Math.sin(30);
            }else{
                MinorOutAmountStopX=MinorOutAmountX+LINE_LENGTH*(float)Math.cos(45);
                MinorOutAmountStopY=MinorOutAmountY+LINE_LENGTH*(float)Math.sin(45);
            }

            gline(canvas,MinorOutAmountX,MinorOutAmountY,MinorOutAmountStopX,MinorOutAmountStopY,colorList.get(0));
            gline(canvas, MinorOutAmountStopX, MinorOutAmountStopY, MinorOutAmountStopX + LEDGEMENT_LENGHT, MinorOutAmountStopY, colorList.get(0));
            canvas.drawCircle(MinorOutAmountStopX + LEDGEMENT_LENGHT, MinorOutAmountStopY, AMALL_CIRCLE_R, PaintMinorOutAmount);
            setfundItem(0,"散户流出");

            setLayout(MinorOutAmountStopX - LEDGEMENT_LENGHT, MinorOutAmountStopY,ScrWidth/9,-ScrWidth/9);

            ////////////////////////////////////////////////////////////
            //填充扇形
            canvas.drawArc(arcRF0, pAngle1, pAngle2, true, PaintMainOutAmount);
            //计算并在扇形中心标注上百分比   主力流出
            xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 / 2);
            //canvas.drawText(Float.toString(pAngle2) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
            float MainOutAmountX = xcalc.getPosX();
            float MainOutAmountY = xcalc.getPosY();
            float MainOutAmountStopX;
            float MainOutAmountStopY;
            if(!fundSize){
                MainOutAmountStopX=MainOutAmountX - LINE_LENGTH * (float) Math.sin(45);
                MainOutAmountStopY=MainOutAmountY+LINE_LENGTH*(float)Math.cos(45);
            }else{
                MainOutAmountStopX=MainOutAmountX - LINE_LENGTH * (float) Math.cos(30);
                MainOutAmountStopY=MainOutAmountY-LINE_LENGTH*(float)Math.sin(30);
            }
            gline(canvas, MainOutAmountX, MainOutAmountY,MainOutAmountStopX,MainOutAmountStopY,colorList.get(1));
            gline(canvas, MainOutAmountStopX, MainOutAmountStopY, MainOutAmountStopX - LEDGEMENT_LENGHT, MainOutAmountStopY, colorList.get(1));
            canvas.drawCircle(MainOutAmountStopX - LEDGEMENT_LENGHT, MainOutAmountStopY, AMALL_CIRCLE_R, PaintMainOutAmount);
            setfundItem(1,"主力流出");
            setLayout(MainOutAmountStopX - LEDGEMENT_LENGHT, MainOutAmountStopY,-ScrWidth/5,-ScrWidth/9);

            ////////////////////////////////////////////////////////////
            //填充扇形
            canvas.drawArc(arcRF0, pAngle1+pAngle2, pAngle3, true, PaintMainInAmount);
            //计算并在扇形中心标注上百分比  主力流入
            xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 + pAngle3 / 2);
            //canvas.drawText(Float.toString(pAngle3) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
            float MainInAmountX = xcalc.getPosX();
            float MainInAmountY=xcalc.getPosY();
            float MainInAmountStopX;
            float MainInAmountStopY;
            if(!fundSize){
                MainInAmountStopX=MainInAmountX-LINE_LENGTH*(float)Math.sin(45);
                MainInAmountStopY=MainInAmountY-LINE_LENGTH*(float)Math.cos(45);
            }else{
                MainInAmountStopX=MainInAmountX-LINE_LENGTH*(float)Math.cos(30);
                MainInAmountStopY=MainInAmountY+LINE_LENGTH*(float)Math.sin(30);
            }
            gline(canvas,MainInAmountX, MainInAmountY,MainInAmountStopX,MainInAmountStopY,colorList.get(2));
            gline(canvas,MainInAmountStopX,MainInAmountStopY,MainInAmountStopX-LEDGEMENT_LENGHT,MainInAmountStopY,colorList.get(2));
            canvas.drawCircle(MainInAmountStopX - LEDGEMENT_LENGHT, MainInAmountStopY, AMALL_CIRCLE_R, PaintMainInAmount);
            setfundItem(2,"主力流入");
            setLayout(MainInAmountStopX - LEDGEMENT_LENGHT, MainInAmountStopY,-ScrWidth/5,-ScrWidth/9);

            ////////////////////////////////////////////////////////////
            //填充扇形
            canvas.drawArc(arcRF0, pAngle1+pAngle2+pAngle3, pAngle4, true, PaintMinorInAmount);
            //计算并在扇形中心标注上百分比  散户流入
            xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 + pAngle3 + pAngle4 / 2);
            //canvas.drawText(Float.toString(pAngle4) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
            float MinorInAmountX = xcalc.getPosX();
            float MinorInAmountY=xcalc.getPosY();
            float MinorInAmountStopX;
            float MinorInAmountStopY;
            if(!fundSize){
                MinorInAmountStopX=MinorInAmountX+LINE_LENGTH*(float)Math.cos(30);
                MinorInAmountStopY=MinorInAmountY+LINE_LENGTH*(float)Math.sin(30);
            }else{
                MinorInAmountStopX=MinorInAmountX+LINE_LENGTH*(float)Math.cos(45);
                MinorInAmountStopY=MinorInAmountY-LINE_LENGTH*(float)Math.sin(45);
            }
            gline(canvas,MinorInAmountX, MinorInAmountY,MinorInAmountStopX,MinorInAmountStopY,colorList.get(3));
            gline(canvas,MinorInAmountStopX,MinorInAmountStopY,MinorInAmountStopX+LEDGEMENT_LENGHT,MinorInAmountStopY,colorList.get(3));
            canvas.drawCircle(MinorInAmountStopX + LEDGEMENT_LENGHT, MinorInAmountStopY, AMALL_CIRCLE_R, PaintMinorInAmount);
            setfundItem(3,"散户流入");
            setLayout(MinorInAmountStopX + LEDGEMENT_LENGHT, MinorInAmountStopY,ScrWidth/36,-ScrWidth/9);
        }
    }
    private void gline(Canvas canvas,float startX, float startY, float stopX, float stopY,int color){
        Paint paint=new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);//设置非填充
        paint.setStrokeWidth(1);//笔宽像素
        paint.setColor(color);//设笔色
        paint.setAntiAlias(true);//锯齿不显示
        canvas.drawLine(startX, startY, stopX, stopY, paint);//画线
    }
    private void setfundItem(int index,String itemName){
        fundItemLayout= LayoutInflater.from(context).inflate(R.layout.individual_share_fund_iten_layout,null);
        LinearLayout.LayoutParams layoutParamsWW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fund_percent= (TextView) fundItemLayout.findViewById(R.id.fund_percent);
        percent_tip= (TextView) fundItemLayout.findViewById(R.id.percent_tip);
        fund_num= (TextView) fundItemLayout.findViewById(R.id.fund_num);
        fund_item_name= (TextView) fundItemLayout.findViewById(R.id.fund_item_name);
        fund_item_name.setText(itemName);
        fund_percent.setTextColor(colorList.get(index));
        percent_tip.setTextColor(colorList.get(index));
        fund_percent.setText(CommonUtility.getFundItemRate(Float.parseFloat(dataList.get(index)) / fundAcount));
        fund_num.setText(CommonUtility.getFundMoneyFormatStr(dataList.get(index)));
        if(fundSize){
            if(index==1||index==2){
                fund_percent.setTextSize(17);
                fund_num.setTextSize(13);
            }
        }else{
            if(index==0||index==3){
                fund_percent.setTextSize(17);
                fund_num.setTextSize(13);
            }
        }
        pieViewLayout.addView(fundItemLayout, layoutParamsWW);
    }
    public static void setLayout(float x,float y,int moveX,int moveY)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) x+moveX,(int) y+moveY,0,0);
        fundItemLayout.setLayoutParams(layoutParams);
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }
}
