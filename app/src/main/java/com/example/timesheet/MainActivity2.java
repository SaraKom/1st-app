package com.example.timesheet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import static android.view.View.*;


public class MainActivity2 extends AppCompatActivity {


    public Long diffInMillis;
    private Date startDate;
    private Date stopDate;
    private String actT;
    private Integer status = null;
    private Long charged;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void main(String[] args) {
        LocalDate Day = LocalDate.now();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ParseUser currentUser = ParseUser.getCurrentUser();

//Kiírja a felghasználó nevet
        TextView tvName = this.findViewById(R.id.tvName);
        String name = getIntent().getStringExtra(MainActivity.KEY_NAME);
        tvName.setText(name);


//Kiírja az aznapi dátumot
        TextView Date = findViewById(R.id.Date);
        String Day = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Date.setText(Day);

        Button btnStart = this.findViewById(R.id.btnStart);
        Button btnStop = this.findViewById(R.id.btnStop);
        Button btnSave = findViewById(R.id.btnSave);

        DigitalClock etStartTime = (DigitalClock) findViewById(R.id.etStartTime);
        DigitalClock etStopTime = (DigitalClock) findViewById(R.id.etStopTime);

        CheckBox MN = this.<CheckBox>findViewById(R.id.MN);
        CheckBox _2x = this.<CheckBox>findViewById(R.id._2x);
        CheckBox _3x = this.<CheckBox>findViewById(R.id._3x);
        CheckBox PN = this.<CheckBox>findViewById(R.id.PN);
        CheckBox FSZ = this.<CheckBox>findViewById(R.id.FSZ);
        CheckBox TP = this.<CheckBox>findViewById(R.id.TP);


        final LinearLayout layoutContainer = this.findViewById(R.id.layoutContainer);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final LinearLayout layoutContainer_start = this.findViewById(R.id.layoutContainer_start);
        final LinearLayout layoutContainer_stop = this.findViewById(R.id.layoutContainer_stop);
        final LinearLayout layoutContainer_charged = this.<LinearLayout>findViewById(R.id.layoutContainer_charged);

// Státusz választó chackboxok.
        MN.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        if (MN.isChecked()) {
                            status = 0;
                            _2x.setEnabled(false);
                            _3x.setEnabled(false);
                            TP.setEnabled(false);
                            PN.setEnabled(false);
                            FSZ.setEnabled(false);
                            btnStart.setEnabled(true);


                        } else {
                            _2x.setEnabled(true);
                            _3x.setEnabled(true);
                            TP.setEnabled(true);
                            PN.setEnabled(true);
                            FSZ.setEnabled(true);

                            status = null;

                        }
                    }
                });

        _2x.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (_2x.isChecked()) {
                    status = 1;
                    MN.setEnabled(false);
                    _3x.setEnabled(false);
                    TP.setEnabled(false);
                    PN.setEnabled(false);
                    FSZ.setEnabled(false);
                    btnStart.setEnabled(true);

                } else {
                    MN.setEnabled(true);
                    _3x.setEnabled(true);
                    TP.setEnabled(true);
                    PN.setEnabled(true);
                    FSZ.setEnabled(true);

                    status = null;

                }
            }
        });


        _3x.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (_3x.isChecked()) {
                    status = 2;
                    MN.setEnabled(false);
                    _2x.setEnabled(false);
                    TP.setEnabled(false);
                    PN.setEnabled(false);
                    FSZ.setEnabled(false);
                    btnStart.setEnabled(true);


                } else {
                    MN.setEnabled(true);
                    _2x.setEnabled(true);
                    TP.setEnabled(true);
                    PN.setEnabled(true);
                    FSZ.setEnabled(true);

                    status = null;
                }
            }
        });

        PN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (PN.isChecked()) {
                    status = 3;

                    MN.setEnabled(false);
                    _2x.setEnabled(false);
                    _3x.setEnabled(false);
                    TP.setEnabled(false);
                    FSZ.setEnabled(false);


                    btnStop.setVisibility(GONE);
                    etStopTime.setVisibility(GONE);
                    btnStart.setVisibility(GONE);
                    etStartTime.setVisibility(GONE);
                    btnSave.setVisibility(View.VISIBLE);


                    diffInMillis = Long.valueOf(0);
                    charged = Long.valueOf(0);
                    View uniqTime = inflater.inflate(R.layout.uniq_time, null);
                    TextView tvTime = (TextView) uniqTime.findViewById(R.id.tvTime);
                    tvTime.setText(diffInMillis + " ");

                    View chdTime = inflater.inflate(R.layout.charged_time, null);
                    TextView chargedTime = (TextView) chdTime.findViewById(R.id.chargedTime);
                    chargedTime.setText(charged + " ");

                    layoutContainer_charged.addView(chdTime, 0);
                    layoutContainer.addView(uniqTime, 0);
                } else {
                    MN.setEnabled(true);
                    _2x.setEnabled(true);
                    _3x.setEnabled(true);
                    TP.setEnabled(true);
                    FSZ.setEnabled(true);


                    status = null;
                    btnStop.setVisibility(VISIBLE);
                    etStopTime.setVisibility(VISIBLE);
                    btnStart.setVisibility(VISIBLE);
                    etStartTime.setVisibility(VISIBLE);
                }
            }
        });

        FSZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (FSZ.isChecked()) {
                    status = 4;
                    MN.setEnabled(false);
                    _2x.setEnabled(false);
                    _3x.setEnabled(false);
                    PN.setEnabled(false);
                    TP.setEnabled(false);


                    btnStop.setVisibility(GONE);
                    etStopTime.setVisibility(GONE);
                    btnStart.setVisibility(GONE);
                    etStartTime.setVisibility(GONE);
                    btnSave.setVisibility(View.VISIBLE);

                    diffInMillis = Long.valueOf(0);
                    //8 órát számol el.
                    charged = Long.valueOf(60 * 60 * 8 * 1000);

                    View uniqTime = inflater.inflate(R.layout.uniq_time, null);
                    TextView tvTime = (TextView) uniqTime.findViewById(R.id.tvTime);
                    tvTime.setText(diffInMillis+ " ");

                    View chdTime = inflater.inflate(R.layout.charged_time, null);
                    TextView chargedTime = (TextView) chdTime.findViewById(R.id.chargedTime);
                    chargedTime.setText(R.string._8hours);

                    layoutContainer_charged.addView(chdTime, 0);
                    layoutContainer.addView(uniqTime, 0);
                } else {
                    MN.setEnabled(true);
                    _2x.setEnabled(true);
                    _3x.setEnabled(true);
                    PN.setEnabled(true);
                    TP.setEnabled(true);

                    status = null;
                    btnStop.setVisibility(VISIBLE);
                    etStopTime.setVisibility(VISIBLE);
                    btnStart.setVisibility(VISIBLE);
                    etStartTime.setVisibility(VISIBLE);
                }
            }
        });

        TP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (TP.isChecked()) {
                    status = 5;

                    MN.setEnabled(false);
                    _2x.setEnabled(false);
                    _3x.setEnabled(false);
                    PN.setEnabled(false);
                    FSZ.setEnabled(false);


                    btnStop.setVisibility(GONE);
                    etStopTime.setVisibility(GONE);
                    btnStart.setVisibility(GONE);
                    etStartTime.setVisibility(GONE);
                    btnSave.setVisibility(View.VISIBLE);

                    diffInMillis = Long.valueOf(0);
                    //8 órát számol el.
                    charged = Long.valueOf(60 * 60 * 8 * 1000);
                    View uniqTime = inflater.inflate(R.layout.uniq_time, null);
                    TextView tvTime = (TextView) uniqTime.findViewById(R.id.tvTime);
                    tvTime.setText(diffInMillis + " ");

                    View chdTime = inflater.inflate(R.layout.charged_time, null);
                    TextView chargedTime = (TextView) chdTime.findViewById(R.id.chargedTime);
                    chargedTime.setText(R.string._8hours);

                    layoutContainer_charged.addView(chdTime, 0);
                    layoutContainer.addView(uniqTime, 0);
                } else {
                    MN.setEnabled(true);
                    _2x.setEnabled(true);
                    _3x.setEnabled(true);
                    PN.setEnabled(true);
                    FSZ.setEnabled(true);

                    status = null;
                    btnStop.setVisibility(VISIBLE);
                    etStopTime.setVisibility(VISIBLE);
                    btnStart.setVisibility(VISIBLE);
                    etStartTime.setVisibility(VISIBLE);


                }
            }
        });

//Start gomb figyelő, elindítja a számlálót, elmenti az adott időt
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                startDate = new Date(System.currentTimeMillis());
                actT = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                View startTime = inflater.inflate(R.layout.start_time, null);
                TextView tvStartTime = (TextView) startTime.findViewById(R.id.tvStartTime);
                tvStartTime.setText(getString(R.string.init) + actT);


                layoutContainer_start.addView(startTime);
                btnStart.setVisibility(GONE);
                btnStop.setEnabled(true);
                etStartTime.setVisibility(GONE);

            }
        });


//Stop gomb figyelője, elmenti az adott időt, amiből, kivonja a kezdeti időt és meg lesz a különbözet, azaz a ledolgozott idő, A switchben a kétszeres, háromszoros időt is kezteli.

        btnStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                stopDate = new Date(System.currentTimeMillis());
                actT = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                View stopTime = inflater.inflate(R.layout.stop_time, null);
                TextView tvStopTime = (TextView) stopTime.findViewById(R.id.tvStopTime);
                tvStopTime.setText(getString(R.string.End) + actT);


                long diffInMillis = stopDate.getTime() - startDate.getTime();
                long dateDiffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
                long dateDiffInHours = TimeUnit.HOURS.convert(diffInMillis - (dateDiffInDays * 24 * 60 * 60 * 1000), TimeUnit.MILLISECONDS);
                long dateDiffInMinutes = TimeUnit.MINUTES.convert(diffInMillis - (dateDiffInDays * 24 * 60 * 60 * 1000) -(dateDiffInHours * 60 * 60 * 1000), TimeUnit.MILLISECONDS);
                long dateDiffInSeconds = TimeUnit.SECONDS.convert(diffInMillis - (dateDiffInDays * 24 * 60 * 60 * 1000) - (dateDiffInHours * 60 * 60 * 1000) - (dateDiffInMinutes * 60 * 1000), TimeUnit.MILLISECONDS);

                View uniqTime = inflater.inflate(R.layout.uniq_time, null);
                TextView tvTime = (TextView) uniqTime.findViewById(R.id.tvTime);
                tvTime.setText(dateDiffInHours+":"+dateDiffInMinutes);

                switch (status) {
                    case 0:
                        if (diffInMillis <= 21600000) {
                            charged = diffInMillis;
                        } else {
                            charged = diffInMillis - 1800000;
                        }
                        break;
                    case 1:
                        charged = 2 * diffInMillis;
                        break;
                    case 2:
                        charged = 3 * diffInMillis;
                        break;
                }


                long chDateDiffInDays = TimeUnit.DAYS.convert(charged, TimeUnit.MILLISECONDS);
                long chDateDiffInHours = TimeUnit.HOURS.convert(charged - (chDateDiffInDays * 24 * 60 * 60 * 1000), TimeUnit.MILLISECONDS);
                long chDateDiffInMinutes = TimeUnit.MINUTES.convert(charged - (chDateDiffInDays * 24 * 60 * 60 * 1000) -(chDateDiffInHours * 60 * 60 * 1000), TimeUnit.MILLISECONDS);
                long chDdateDiffInSeconds = TimeUnit.SECONDS.convert(charged - (chDateDiffInDays * 24 * 60 * 60 * 1000) - (chDateDiffInHours * 60 * 60 * 1000) - (chDateDiffInMinutes * 60 * 1000), TimeUnit.MILLISECONDS);
                View chdTime = inflater.inflate(R.layout.charged_time, null);
                TextView chargedTime = (TextView) chdTime.findViewById(R.id.chargedTime);
                chargedTime.setText(chDateDiffInHours + ":" + chDateDiffInMinutes);


                layoutContainer_charged.addView(chdTime, 0);
                layoutContainer_stop.addView(stopTime);
                layoutContainer.addView(uniqTime, 0);
                btnStop.setVisibility(GONE);
                etStopTime.setVisibility(GONE);

                Date date = new Date(System.currentTimeMillis());
                Long work = diffInMillis;
                Long chargedT = charged;
                Timestamp startT;
                Timestamp stopT;

                if (startDate != null) {
                    startT = new Timestamp(startDate.getTime());
                } else {
                    startT = null;
                }

                if (stopDate != null) {
                    stopT = new Timestamp(stopDate.getTime());
                } else {
                    stopT = null;
                }

                ParseObject logger = new ParseObject("logger");
                logger.put("name", name);
                logger.put("date", date);
                logger.put("status", status);
                logger.put("start", startT);
                logger.put("stop", stopT);
                logger.put("workedTime", work);
                logger.put("chargedTime", chargedT);
                logger.saveInBackground();

                logger.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Success
                        } else {
                            // Error
                        }
                    }
                });


            }
        });


        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date(System.currentTimeMillis());


                Long work = diffInMillis;
                Long chargedT = charged;
                ParseObject logger = new ParseObject("logger");
                logger.put("name", name);
                logger.put("date", date);
                logger.put("status", status);
                logger.put("workedTime", work);
                logger.put("chargedTime", chargedT);
                logger.saveInBackground();

                logger.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Success
                        } else {
                            // Error
                        }
                    }
                });

            }
        });

    }
}

