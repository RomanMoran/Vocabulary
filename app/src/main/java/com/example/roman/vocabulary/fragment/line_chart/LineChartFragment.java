package com.example.roman.vocabulary.fragment.line_chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.db_utility.DBHelper;
import com.example.roman.vocabulary.fragment.BaseFragment;
import com.example.roman.vocabulary.utilities.Constants;
import com.example.roman.vocabulary.utilities.Utility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by roman on 15.09.2017.
 */

@FragmentWithArgs
public class LineChartFragment extends BaseFragment implements OnChartValueSelectedListener {

    public static final String TAG = LineChartFragment.class.getName();
    @BindView(R.id.chartWordsDay)
    LineChart mChart;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.next)
    ImageView next;
    @BindView(R.id.previous)
    ImageView previous;

    private int mYear;
    private int mMonth;

    @Arg(bundler = ParcelerArgsBundler.class)
    public boolean learned;
    /*@Arg(bundler = ParcelerArgsBundler.class)
    public int day;
    @Arg(bundler = ParcelerArgsBundler.class)
    public int year;*/


    public static LineChartFragment newInstance(boolean learned) {
        LineChartFragment fragment = new LineChartFragmentBuilder(learned).build();
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_line_chart;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.previous,R.id.next})
    void flip(View view){
        int month;
        switch (view.getId()){
            case R.id.next:
                month = Utility.getMonth(mMonth+1);
                if (mMonth>=11) mYear++;
                setData(Utility.countDaysInMonth(month,mYear),month);
                break;
            case R.id.previous:
                month = Utility.getMonth(mMonth-1);
                if (mMonth<1) mYear--;
                setData(Utility.countDaysInMonth(month,mYear),month);
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        final Calendar c = Calendar.getInstance();
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);
        setData(Utility.countDaysInMonth(mMonth,mYear),mMonth);
        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setXEntrySpace(2f);
        l.setYEntrySpace(2f);
        l.setDrawInside(false);
    }

    private void setData(int daysInMonth,int month) {

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        // Количество слов i , значение value
        String newDate = "";
        for (int i = 1; i <= daysInMonth; i++) {
            float finalI = i;
            newDate = String.format("%02d/%02d/%04d",i,month+1,mYear);
            DBHelper.getWordsByDateOrLearned(newDate,learned)
                    .subscribe(words1 -> {
                yVals1.add(new Entry(finalI,words1.size()));
            });
        }

        this.year.setText(String.valueOf(mYear));
        newDate = String.format("%02d/%02d/%04d",1,month+1,mYear);
        this.month.setText(getViewTime(newDate));
        mMonth = month;
        LineDataSet set1 = null;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();

        }
            // create a dataset and give it a type
            createDataSet(set1,yVals1);



    }

    private void createDataSet(LineDataSet set1,ArrayList<Entry> yVals1) {
        set1 = new LineDataSet(yVals1, "Month "+yVals1.size());

        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisMaximum(yVals1.size());
        xAxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(30f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
    }

    public String getViewTime(String date) {
        SimpleDateFormat simpleDateFormatOld = new SimpleDateFormat(Constants.OUTPUT_DATE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MONTH);

        Date dateOld = null;
        try {
            dateOld = simpleDateFormatOld.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateOld == null)
            return "";

        return simpleDateFormat.format(dateOld);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
