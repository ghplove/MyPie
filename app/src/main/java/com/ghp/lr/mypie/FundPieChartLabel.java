package com.ghp.lr.mypie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by ghp on 15/6/11.
 */
public class FundPieChartLabel extends View {

    private final float LINE_LENGTH=60f;
    public FundPieChartLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FundPieChartLabel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private int ScrWidth, ScrHeight;
    private float rx, ry;


    public FundPieChartLabel(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

        //解决4.1版本 以下canvas.drawTextOnPath()不显示问题
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //屏幕信息
        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScrHeight = dm.heightPixels;
        ScrWidth = dm.widthPixels;
    }

    public void onDraw(Canvas canvas) {
        //画布背景
        canvas.drawColor(Color.WHITE);

        //画笔初始化
        Paint PaintArc = new Paint();
        PaintArc.setColor(Color.RED);

        Paint PaintGree = new Paint();
        PaintGree.setColor(Color.GREEN);
        PaintGree.setStyle(Paint.Style.FILL);

        Paint PaintBlue = new Paint();
        PaintBlue.setColor(Color.BLUE);
        PaintBlue.setStyle(Paint.Style.STROKE);//设置非填充

        Paint PaintYellow = new Paint();
        PaintYellow.setColor(Color.YELLOW);
        PaintYellow.setStyle(Paint.Style.FILL);

        //抗锯齿
        PaintArc.setAntiAlias(true);
        PaintYellow.setAntiAlias(true);
        PaintGree.setAntiAlias(true);

        PaintBlue.setTextSize(12);

        float cirX = ScrWidth / 2;//圆心X
        float cirY = ScrWidth / 5*3;//圆心Y
        float radius = ScrWidth / 5;//半径
        //先画个圆确定下显示位置
        canvas.drawCircle(cirX, cirY, radius, PaintGree);

        float arcLeft = cirX - radius;
        float arcTop = cirY - radius;
        float arcRight = cirX + radius;
        float arcBottom = cirY + radius;
        RectF arcRF0 = new RectF(arcLeft, arcTop, arcRight, arcBottom);

        ////////////////////////////////////////////////////////////
        //饼图标题注释
        //canvas.drawText("author:xiongchuanliang", 60, ScrHeight - 270, PaintBlue);

        //位置计算类
        XChartCalc xcalc = new XChartCalc();

        //实际用于计算的半径
        //float calcRadius = radius / 2;//标写％居扇形中间
        float calcRadius = radius / 1;
        ////////////////////////////////////////////////////////////
        //初始角度
        float pAngle1 = 30f;
        float pAngle2 = 140f;
        float pAngle3 = 130f;
        float pAngle4 = 360f - pAngle1 - pAngle2-pAngle3;

        //填充扇形
        canvas.drawArc(arcRF0, 0, pAngle1, true, PaintArc);

        //计算并在扇形中心标注上百分比    30%
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 / 2);
        canvas.drawText(Float.toString(pAngle1) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
        float MinorOutAmountX=xcalc.getPosX();
        float MinorOutAmountY=xcalc.getPosY();
        gline(canvas,MinorOutAmountX,MinorOutAmountY,MinorOutAmountX+LINE_LENGTH*(float)Math.cos(30),MinorOutAmountY-LINE_LENGTH*(float)Math.sin(30),Color.RED);
        //////////////////////////////////////////////////////////////

        ////////////////////////////////////////////////////////////
        //填充扇形
        canvas.drawArc(arcRF0, pAngle1, pAngle2, true, PaintYellow);
        //计算并在扇形中心标注上百分比   140%
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 / 2);
        canvas.drawText(Float.toString(pAngle2) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
        float MainOutAmountX = xcalc.getPosX();
        float MainOutAmountY=xcalc.getPosY();
        gline(canvas,MainOutAmountX,MainOutAmountY,MainOutAmountX-LINE_LENGTH*(float)Math.sin(45),MainOutAmountY+LINE_LENGTH*(float)Math.cos(45),Color.BLUE);
        ////////////////////////////////////////////////////////////
        //计算并在扇形中心标注上百分比  130%
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 + pAngle3 / 2);
        canvas.drawText(Float.toString(pAngle3) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
        float MainInAmountX = xcalc.getPosX();
        float MainInAmountY=xcalc.getPosY();
        gline(canvas,MainInAmountX,MainInAmountY,MainInAmountX-LINE_LENGTH*(float)Math.sin(45),MainInAmountY-LINE_LENGTH*(float)Math.cos(45),Color.BLUE);
        ////////////////////////////////////////////////////////////
        //计算并在扇形中心标注上百分比  60%
        xcalc.CalcArcEndPointXY(cirX, cirY, calcRadius, pAngle1 + pAngle2 + pAngle3 + pAngle4 / 2);
        canvas.drawText(Float.toString(pAngle4) + "%", xcalc.getPosX(), xcalc.getPosY(), PaintBlue);
        float MinorInAmountX = xcalc.getPosX();
        float MinorInAmountY=xcalc.getPosY();
        gline(canvas,MinorInAmountX,MinorInAmountY,MinorInAmountX+LINE_LENGTH*(float)Math.cos(30),MinorInAmountY+LINE_LENGTH*(float)Math.sin(30),Color.BLUE);
    }
    private void gline(Canvas canvas,float startX, float startY, float stopX, float stopY,int color){
        Paint paint=new Paint(Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);//设置非填充
        paint.setStrokeWidth(2);//笔宽5像素
        paint.setColor(color);//设笔色
        paint.setAntiAlias(true);//锯齿不显示
        canvas.drawLine(startX, startY, stopX, stopY, paint);//画线
    }
}
