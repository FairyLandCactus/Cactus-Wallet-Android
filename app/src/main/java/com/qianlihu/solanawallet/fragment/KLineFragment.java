package com.qianlihu.solanawallet.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Transformer;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.chart.ChartFingerTouchListener;
import com.qianlihu.solanawallet.chart.CoupleChartGestureListener;
import com.qianlihu.solanawallet.chart.CoupleChartValueSelectedListener;
import com.qianlihu.solanawallet.chart.HighlightCombinedRenderer;
import com.qianlihu.solanawallet.chart.InBoundXAxisRenderer;
import com.qianlihu.solanawallet.chart.InBoundYAxisRenderer;
import com.qianlihu.solanawallet.chart.OffsetBarRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/215:24
 * desc   :
 * version: 1.0.0
 */
public class KLineFragment extends BaseFragment {

    @BindView(R.id.cc_kl)
    LineChart cc;
    @BindView(R.id.bc_kl)
    BarChart bc;

    private boolean toLeft;
    private int range = 52;//一屏显示Candle个数
    private int index = 2;//TabLayout选中下标
    private float highVisX;//切屏时X轴的最大值

    private List<List<String>> dataList;
    private Map<Integer, String> xValues;
    private LineDataSet lineSetMin;//分时线
    private LineDataSet lineSet5;
    private LineDataSet lineSet10;
    private CandleDataSet candleSet;
    private CombinedData combinedData;
    private BarDataSet barSet;
    private final float barOffset = -0.5F;//BarChart偏移量
    private CoupleChartGestureListener ccGesture;
    private CoupleChartGestureListener bcGesture;

    public static Fragment newInstance(int id, int status) {
        KLineFragment fragment = new KLineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_k_line;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    private void initChart() {
        int black = getColorById(R.color.black3B);
        int gray = getColorById(R.color.gray8B);
        int red = getColorById(R.color.redEB);
        int green = getColorById(R.color.green4C);
        int highlightColor = getColorById(R.color.brown);
        float highlightWidth = 0.5F;//高亮线的线宽
        float sp8 = sp2px(8);
        //K线
        cc.setNoDataTextColor(gray);//无数据时提示文字的颜色
        cc.setDescription(null);//取消描述
        cc.getLegend().setEnabled(false);//取消图例
        cc.setDragDecelerationEnabled(false);//不允许甩动惯性滑动  和moveView方法有冲突 设置为false
        cc.setMinOffset(0);//设置外边缘偏移量
        cc.setExtraBottomOffset(6);//设置底部外边缘偏移量 便于显示X轴

        cc.setScaleEnabled(false);//不可缩放
        cc.setAutoScaleMinMaxEnabled(true);//自适应最大最小值
//        cc.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.CANDLE,
//                CombinedChart.DrawOrder.LINE});
        Transformer trans = cc.getTransformer(YAxis.AxisDependency.LEFT);
        //自定义X轴标签位置
        cc.setXAxisRenderer(new InBoundXAxisRenderer(cc.getViewPortHandler(), cc.getXAxis(), trans, 10));
        //自定义Y轴标签位置
        cc.setRendererLeftYAxis(new InBoundYAxisRenderer(cc.getViewPortHandler(), cc.getAxisLeft(), trans));
        //自定义渲染器 重绘高亮
//        cc.setRenderer(new HighlightCombinedRenderer(cc, cc.getAnimator(), cc.getViewPortHandler(), sp8));

        //X轴
        XAxis xac = cc.getXAxis();
        xac.setPosition(XAxis.XAxisPosition.BOTTOM);
        xac.setGridColor(black);//网格线颜色
        xac.setTextColor(gray);//标签颜色
        xac.setTextSize(8);//标签字体大小
        xac.setAxisLineColor(black);//轴线颜色
        xac.disableAxisLineDashedLine();//取消轴线虚线设置
        xac.setAvoidFirstLastClipping(true);//避免首尾端标签被裁剪
        xac.setLabelCount(2, true);//强制显示2个标签
        xac.setValueFormatter(new IAxisValueFormatter() {//转换X轴的数字为文字
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int v = (int) value;
                if (!xValues.containsKey(v) && xValues.containsKey(v - 1)) {
                    v = v - 1;
                }
                String x = xValues.get(v);
                return TextUtils.isEmpty(x) ? "" : x;
            }
        });

        //左Y轴
        YAxis yac = cc.getAxisLeft();
        yac.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//标签显示在内侧
        yac.setGridColor(black);
        yac.setTextColor(gray);
        yac.setTextSize(8);
        yac.setLabelCount(5, true);
        yac.enableGridDashedLine(5, 4, 0);//横向网格线设置为虚线
//        yac.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {//只显示部分标签
//                int index = getIndexY(value, axis.getAxisMinimum(), axis.getAxisMaximum());
//                return index == 0 || index == 2 ? format4p.format(value) : "";//不显示的标签不能返回null
//            }
//        });
        //右Y轴
        cc.getAxisRight().setEnabled(false);

        //蜡烛图
        candleSet = new CandleDataSet(new ArrayList<CandleEntry>(), "Kline");
        candleSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleSet.setDrawHorizontalHighlightIndicator(false);
        candleSet.setHighlightLineWidth(highlightWidth);
        candleSet.setHighLightColor(highlightColor);
        candleSet.setShadowWidth(0.7f);
        candleSet.setIncreasingColor(red);//上涨为红色
        candleSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleSet.setDecreasingColor(green);//下跌为绿色
        candleSet.setDecreasingPaintStyle(Paint.Style.STROKE);
        candleSet.setNeutralColor(red);
        candleSet.setShadowColorSameAsCandle(true);
        candleSet.setDrawValues(false);
        candleSet.setHighlightEnabled(false);
        //均线
        lineSet5 = new LineDataSet(new ArrayList<Entry>(), "MA5");
        lineSet5.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet5.setColor(getColorById(R.color.purple));
        lineSet5.setDrawCircles(false);
        lineSet5.setDrawValues(false);
        lineSet5.setHighlightEnabled(false);
        lineSet10 = new LineDataSet(new ArrayList<Entry>(), "MA10");
        lineSet10.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSet10.setColor(getColorById(R.color.yellow));
        lineSet10.setDrawCircles(false);
        lineSet10.setDrawValues(false);
        lineSet10.setHighlightEnabled(false);
        //分时线
        lineSetMin = new LineDataSet(new ArrayList<Entry>(), "Minutes");
        lineSetMin.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineSetMin.setColor(Color.WHITE);
        lineSetMin.setDrawCircles(false);
        lineSetMin.setDrawValues(false);
        lineSetMin.setDrawFilled(true);
        lineSetMin.setHighlightEnabled(false);
        lineSetMin.setFillColor(gray);
        lineSetMin.setFillAlpha(60);


        //成交量
        bc.setNoDataTextColor(gray);
        bc.setDescription(null);
        bc.getLegend().setEnabled(false);
        bc.setDragDecelerationEnabled(false);//不允许甩动惯性滑动
        bc.setMinOffset(0);//设置外边缘偏移量

        bc.setScaleEnabled(false);//不可缩放
        bc.setAutoScaleMinMaxEnabled(true);//自适应最大最小值
        //自定义Y轴标签位置
        bc.setRendererLeftYAxis(new InBoundYAxisRenderer(bc.getViewPortHandler(), bc.getAxisLeft(),
                bc.getTransformer(YAxis.AxisDependency.LEFT)));
        //设置渲染器控制颜色、偏移，以及高亮
        bc.setRenderer(new OffsetBarRenderer(bc, bc.getAnimator(), bc.getViewPortHandler(), barOffset)
                .setHighlightWidthSize(highlightWidth, sp8));

        bc.getXAxis().setEnabled(false);
        YAxis yab = bc.getAxisLeft();
        yab.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//标签显示在内侧
        yab.setDrawAxisLine(false);
        yab.setGridColor(black);
        yab.setTextColor(gray);
        yab.setTextSize(8);
        yab.setLabelCount(2, true);
        yab.setAxisMinimum(0);
        yab.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? "" : value + "";
            }
        });
        bc.getAxisRight().setEnabled(false);

        barSet = new BarDataSet(new ArrayList<BarEntry>(), "VOL");
        barSet.setHighLightColor(highlightColor);
        barSet.setColors(red, green);
        barSet.setDrawValues(false);
        barSet.setHighlightEnabled(false);

        ccGesture = new CoupleChartGestureListener((CoupleChartGestureListener.OnEdgeListener) this, cc, bc) {//设置成全局变量，后续要用到
            @Override
            public void chartDoubleTapped(MotionEvent me) {
//                doubleTapped();
            }
        };
        cc.setOnChartGestureListener(ccGesture);//设置手势联动监听
        bcGesture = new CoupleChartGestureListener((CoupleChartGestureListener.OnEdgeListener) getContext(), bc, cc) {
            @Override
            public void chartDoubleTapped(MotionEvent me) {
//                doubleTapped();
            }
        };
        bc.setOnChartGestureListener(bcGesture);

        cc.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener((CoupleChartValueSelectedListener.ValueSelectedListener) getActivity(), cc, bc));//设置高亮联动监听
        bc.setOnChartValueSelectedListener(new CoupleChartValueSelectedListener((CoupleChartValueSelectedListener.ValueSelectedListener) getContext(), bc, cc));
        cc.setOnTouchListener(new ChartFingerTouchListener(cc, (ChartFingerTouchListener.HighlightListener) getContext()));//手指长按滑动高亮
        bc.setOnTouchListener(new ChartFingerTouchListener(bc, (ChartFingerTouchListener.HighlightListener) getContext()));
    }

    public int getColorById(int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }

    public int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                getResources().getDisplayMetrics());
    }

}
