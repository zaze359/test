package com.zaze.demo.debug;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.zaze.demo.R;
import com.zaze.utils.ToastUtil;
import com.zaze.utils.ZStringUtil;
import com.zaze.utils.date.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Description :
 *
 * @author : ZAZE
 * @version : 2018-07-16 - 10:09
 */
public class TimePickerWidget extends Dialog {
    private static final int START = 0;
    private static final int END = 1;

    private ValueSelectView hourPicker;
    private ValueSelectView minutePicker;
    private View timePickerStartLl;
    private View timePickerEndLl;
    private TextView timePickerStartTv;
    private TextView timePickerEndTv;
    private TextView timePickerCancelTv;
    private TextView timePickerSureTv;
    private TextView timePickerTitle;

    private TimePickerListener timePickerListener;
    private final String[] startHours;
    private final String[] startMinutes;
    private final String[] endHours;
    private final String[] endMinutes;
    private final String title;
    private final String beginAt;
    private final String endAt;
    private final boolean checkTimeRange;

    /**
     * 默认
     */
    private final String[] defaultMinutes = buildArrayAndFull(60);

    /**
     * 当前状态
     */
    private int curStatus = START;

    private TimePickerWidget(@NonNull Context context, Builder builder) {
        super(context);
        this.title = builder.title;
        this.beginAt = builder.beginAt;
        this.endAt = builder.endAt;
        this.startHours = builder.startHours;
        this.startMinutes = builder.startMinutes;
        this.endHours = builder.endHours;
        this.endMinutes = builder.endMinutes;
        this.timePickerListener = builder.timePickerListener;
        this.checkTimeRange = builder.checkTimeRange;
        setCancelable(builder.cancelable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
        final View view = getLayoutInflater().inflate(R.layout.time_picker_layout, null);
        hourPicker = view.findViewById(R.id.time_picker_hour_sv);
        minutePicker = view.findViewById(R.id.time_picker_minute_sv);
        timePickerStartTv = view.findViewById(R.id.time_picker_start_tv);
        timePickerEndTv = view.findViewById(R.id.time_picker_end_tv);
        timePickerSureTv = view.findViewById(R.id.time_picker_sure_tv);
        timePickerCancelTv = view.findViewById(R.id.time_picker_cancel_tv);
        timePickerStartLl = view.findViewById(R.id.time_picker_start_ll);
        timePickerEndLl = view.findViewById(R.id.time_picker_end_ll);
        timePickerTitle = view.findViewById(R.id.time_picker_title);
        // --------------------------------------------------
        timePickerStartTv.setText(beginAt);
        timePickerEndTv.setText(endAt);
        timePickerTitle.setText(title);
        // --------------------------------------------------
        reset();
        // --------------------------------------------------
        hourPicker.setOnValueChangedListener(new ValueSelectView.OnValueChangedListener() {
            @Override
            public void onValueChanged(@NotNull String value, int index) {
                updateTimeStr();
                reset();
            }
        });
        // --------------------------------------------------
        minutePicker.setOnValueChangedListener(new ValueSelectView.OnValueChangedListener() {
            @Override
            public void onValueChanged(@NotNull String value, int index) {
                updateTimeStr();
            }
        });
        // --------------------------------------------------
        timePickerStartLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curStatus != START) {
                    curStatus = START;
                    resetStart();
                }
            }
        });
        timePickerEndLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curStatus != END) {
                    curStatus = END;
                    resetEnt();
                }
            }
        });
        timePickerCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerListener != null) {
                    timePickerListener.onCancel();
                }
                dismiss();
            }
        });
        timePickerSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePickerListener != null) {
                    long beginTime = calculateTime(timePickerStartTv.getText().toString().trim());
                    long endTime = calculateTime(timePickerEndTv.getText().toString().trim());
                    if (checkTimeRange && beginTime >= endTime) {
                        ToastUtil.toast(view.getContext(), "结束时间不可小于开始时间！");
                        return;
                    }
                    timePickerListener.onSure(TimePickerWidget.this,
                            beginTime, endTime);
                }
            }
        });
        setContentView(view);
    }

    /**
     * reset
     */
    private void reset() {
        if (curStatus == START) {
            resetStart();
        } else {
            resetEnt();
        }
    }

    /**
     * resetStart
     */
    private void resetStart() {
        updateStatus(timePickerEndLl, timePickerStartLl);
        String[] hm = timePickerStartTv.getText().toString().trim().split(":");
        String[] hours = startHours;
        String[] minutes = startMinutes;
        int hourIndex = 0;
        int minuteIndex = 0;
        if (hm.length == 2) {
            hourIndex = getIndex(hours, hm[0]);
            // 若是第一个小时 则minute的范围应该是 : 起始时间的分钟值 ~ 59
            if (hourIndex != 0) {
                minutes = defaultMinutes;
            }
            minuteIndex = getIndex(minutes, hm[1]);
        }
        initPicker(hourPicker, hours, hourIndex);
        initPicker(minutePicker, minutes, minuteIndex);
    }

    /**
     * resetEnt
     */
    private void resetEnt() {
        updateStatus(timePickerStartLl, timePickerEndLl);
        String[] hm = timePickerEndTv.getText().toString().trim().split(":");
        String[] hours = endHours;
        String[] minutes = endMinutes;
        int hourIndex = 0;
        int minuteIndex = 0;
        if (hm.length == 2) {
            hourIndex = getIndex(hours, hm[0]);
            // 若是最后一个小时 则minute的范围应该是 : 00 ~ 结束时间的分钟值
            if (hourIndex < endHours.length - 1) {
                minutes = defaultMinutes;
            }
            minuteIndex = getIndex(minutes, hm[1]);
        }
        initPicker(hourPicker, hours, hourIndex);
        initPicker(minutePicker, minutes, minuteIndex);
    }

    /**
     * initPicker
     *
     * @param numberPicker numberPicker
     * @param arrays       arrays
     * @param position     position
     */
    private void initPicker(ValueSelectView numberPicker, String[] arrays, int position) {
        numberPicker.setValues(Arrays.asList(arrays));
        numberPicker.setNeedLooper(true);
        numberPicker.setSelect(position);
    }


    /**
     * 获取位置
     *
     * @param arrays arrays
     * @param value  value
     * @return getIndex
     */
    private int getIndex(String[] arrays, String value) {
        for (int i = 0; i < arrays.length; i++) {
            if (TextUtils.equals(arrays[i], value)) {
                return i;
            }
        }
        return 0;
    }


    /**
     * updateTimeStr
     */
    private void updateTimeStr() {
        switch (curStatus) {
            case START:
                timePickerStartTv.setText(ZStringUtil.format("%s:%s", hourPicker.getCenterValue(), minutePicker.getCenterValue()));
                break;
            default:
                timePickerEndTv.setText(ZStringUtil.format("%s:%s", hourPicker.getCenterValue(), minutePicker.getCenterValue()));
                break;
        }
    }


    /**
     * updateStatus
     *
     * @param preView      preView
     * @param selectedView selectedView
     */
    private void updateStatus(View preView, View selectedView) {
        preView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        selectedView.setBackgroundColor(Color.parseColor("#EBEBEB"));
    }

    /**
     * 将选择的时段转换为当天时间
     *
     * @param timeStr HH:mm
     * @return 当天时间
     */
    public static long calculateTime(String timeStr) {
        return DateUtil.getDayStart(System.currentTimeMillis()) + calculateTimeHM(timeStr);
    }

    /**
     * calculateTimeHM
     *
     * @param timeStr timeStr
     * @return long
     */
    public static long calculateTimeHM(String timeStr) {
        return DateUtil.getTimeMillisByHM(timeStr);
    }

    /**
     * 生成数组并填充
     *
     * @param length 数据长度
     * @return String[]
     */
    private String[] buildArrayAndFull(int length) {
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            array[i] = String.format(Locale.getDefault(), "%02d", i);
        }
        return array;
    }


    public static class Builder {
        private String[] startHours;
        private String[] startMinutes;
        private String[] endHours;
        private String[] endMinutes;
        private String title;
        private String beginAt;
        private String endAt;
        private TimePickerListener timePickerListener;
        private PickerTime startPickerTime;
        private PickerTime endPickerTime;
        private boolean cancelable = false;
        private boolean checkTimeRange = true;

        /**
         * build
         *
         * @param context            context
         * @param timePickerListener timePickerListener
         * @return TimePickerWidget
         */
        public TimePickerWidget build(Context context, TimePickerListener timePickerListener) {
            if (startPickerTime == null) {
                startPickerTime = new PickerTime();
            }
            startHours = startPickerTime.getHours();
            startMinutes = startPickerTime.getStartMinutes();
            //
            if (endPickerTime == null) {
                endPickerTime = new PickerTime();
            }
            endHours = endPickerTime.getHours();
            endMinutes = endPickerTime.getEndMinutes();
            //
            if (TextUtils.isEmpty(beginAt)) {
                beginAt = startPickerTime.minTimeStr;
            }
            if (TextUtils.isEmpty(endAt)) {
                endAt = endPickerTime.maxTimeStr;
            }
            if (TextUtils.isEmpty(title)) {
                title = "时间选择";
            }
            this.timePickerListener = timePickerListener;
            return new TimePickerWidget(context, this);
        }

        /**
         * title
         *
         * @param title title
         * @return Builder
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置dialog是否可以取消
         *
         * @param cancelable 能否取消
         * @return this
         */
        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * startPicker
         *
         * @param minTimeStr HH:mm
         * @param maxTimeStr HH:mm
         * @return Builder
         */
        public Builder startPicker(String minTimeStr, String maxTimeStr) {
//            minTimeStr = "00:00";
//            maxTimeStr = "23:59";
            startPickerTime = new PickerTime(minTimeStr, maxTimeStr);
            return this;
        }

        /**
         * endPicker
         *
         * @param minTimeStr HH:mm
         * @param maxTimeStr HH:mm
         * @return Builder
         */
        public Builder endPicker(String minTimeStr, String maxTimeStr) {
//            minTimeStr = "00:00";
//            maxTimeStr = "23:59";
            endPickerTime = new PickerTime(minTimeStr, maxTimeStr);
            return this;
        }

        /**
         * beginAt
         *
         * @param beginAt beginAt
         * @return Builder
         */
        public Builder beginAt(long beginAt) {
            this.beginAt = DateUtil.timeMillisToString(beginAt, "HH:mm", TimeZone.getDefault());
            return this;
        }

        /**
         * beginAt
         *
         * @param beginAt HH:mm
         * @return Builder
         */
        public Builder beginAt(String beginAt) {
            this.beginAt = beginAt;
            return this;
        }

        /**
         * endAt
         *
         * @param endAt endAt
         * @return Builder
         */
        public Builder endAt(long endAt) {
            this.endAt = DateUtil.timeMillisToString(endAt, "HH:mm", TimeZone.getDefault());
            return this;
        }

        /**
         * endAt
         *
         * @param endAt hm
         * @return Builder
         */
        public Builder endAt(String endAt) {
            this.endAt = endAt;
            return this;
        }

        /**
         * 是否检测时间 默认开启(结束 时间 不可小于开始时间)
         *
         * @param checkTimeRange checkTimeRange
         * @return Builder
         */
        public Builder checkTimeRange(boolean checkTimeRange) {
            this.checkTimeRange = checkTimeRange;
            return this;
        }

        // --------------------------------------------------

        /**
         * 边界容错
         *
         * @param maxHour maxHour
         * @return int
         */
        private int getMaxHour(int maxHour) {
            if (maxHour < 0 || maxHour > 23) {
                return 23;
            }
            return maxHour;
        }

        /**
         * 边界容错
         *
         * @param maxMinute maxMinute
         * @return int
         */
        private int getMaxMinute(int maxMinute) {
            if (maxMinute < 0 || maxMinute > 59) {
                return 59;
            }
            return maxMinute;
        }
    }

    public static class PickerTime {

        private int minHour;
        private int minMinute;
        private int maxHour;
        private int maxMinute;
        private String minTimeStr;
        private String maxTimeStr;

        public PickerTime() {
            this.minHour = 0;
            this.minMinute = 0;
            this.maxHour = 23;
            this.maxMinute = 59;
        }

        public PickerTime(String minTimeStr, String maxTimeStr) {
            this.minTimeStr = minTimeStr;
            this.maxTimeStr = maxTimeStr;
            long minTime = calculateTimeHM(minTimeStr);
            minHour = (int) (minTime / DateUtil.HOUR);
            minMinute = (int) ((minTime % DateUtil.HOUR) / DateUtil.MINUTE);
            long maxTime = calculateTimeHM(maxTimeStr);
            maxHour = (int) (maxTime / DateUtil.HOUR);
            maxMinute = (int) ((maxTime % DateUtil.HOUR) / DateUtil.MINUTE);
        }

        public PickerTime(int minHour, int minMinute, int maxHour, int maxMinute) {
            this.minHour = minHour;
            this.minMinute = minMinute;
            this.maxHour = maxHour;
            this.maxMinute = maxMinute;
        }

        public int getMinHour() {
            return minHour;
        }

        public void setMinHour(int minHour) {
            this.minHour = minHour;
        }

        public int getMaxHour() {
            return maxHour;
        }

        public void setMaxHour(int maxHour) {
            this.maxHour = maxHour;
        }

        public int getMinMinute() {
            return minMinute;
        }

        public void setMinMinute(int minMinute) {
            this.minMinute = minMinute;
        }

        public int getMaxMinute() {
            return maxMinute;
        }

        public void setMaxMinute(int maxMinute) {
            this.maxMinute = maxMinute;
        }

        /**
         * 获取小时范围 xx~xx
         *
         * @return 小时范围 0~23
         */
        public String[] getHours() {
            return createArray(minHour, maxHour);
        }

        /**
         * 获取分钟范围 minMinute ~ 59
         *
         * @return 分钟范围 0~59
         */
        public String[] getStartMinutes() {
            return createArray(minMinute, 59);
        }

        /**
         * 获取分钟范围 0 ~ maxMinute
         *
         * @return 分钟范围 0~59
         */
        public String[] getEndMinutes() {
            return createArray(0, maxMinute);
        }

        /**
         * 填充数组
         *
         * @param start start
         * @param end   end
         * @return String[]
         */
        private String[] createArray(int start, int end) {
            String[] array;
            if (end >= start) {
                array = new String[end - start + 1];
                for (int i = 0; i < array.length; i++) {
                    array[i] = String.format(Locale.getDefault(), "%02d", start + i);
                }
            } else {
                array = new String[0];
            }
            return array;
        }
    }

    public interface TimePickerListener {
        /**
         * 取消时
         */
        void onCancel();

        /**
         * 确认
         *
         * @param dialog    dialog
         * @param beginTime beginTime
         * @param endTime   endTime
         */
        void onSure(@NonNull Dialog dialog, long beginTime, long endTime);
    }
}
