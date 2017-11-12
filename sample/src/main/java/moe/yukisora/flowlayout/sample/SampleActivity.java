package moe.yukisora.flowlayout.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import moe.yukisora.flowlayout.FlowLayout;

public class SampleActivity extends Activity {
    public static final String[] texts = new String[]{
            "Java",
            "C",
            "Objective-C",
            "C++",
            "C#",
            "PHP",
            "Python",
            "(Visual) Basic",
            "Perl",
            "Ruby",
            "JavaScript",
            "Visual Basic .NET",
            "Lisp",
            "Pascal",
            "Delphi/Object Pascal",
            "Transact-SQL",
            "Bash",
            "MATLAB",
            "Assembly",
            "Ada",
            "Lua",
            "PL/SQL",
            "SAS",
            "COBOL",
            "Fortran",
            "R",
            "LabVIEW",
            "Scheme",
            "Scratch",
            "ABAP",
            "Logo",
            "Prolog",
            "Haskell",
            "Erlang",
            "Scala",
            "D",
            "Smalltalk",
            "RPG (OS/400)",
            "OpenCL",
            "Forth",
            "NXT-G",
            "APL",
            "ML",
            "ActionScript",
            "Common Lisp",
            "Awk",
            "F#",
            "Tcl",
            "PL/I",
            "CFML"
    };
    public static final int[] textSize = new int[]{
            10,
            11,
            12,
            13,
            14,
            15,
            16,
            17,
            18,
            19,
    };
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final FlowLayout flowLayout = findViewById(R.id.flowLayout);
        RadioGroup align = findViewById(R.id.align);
        RadioGroup verticalAlign = findViewById(R.id.verticalAlign);
        RadioGroup direction = findViewById(R.id.direction);
        RadioGroup lastRowAlign = findViewById(R.id.lastRowAlign);
        Button add = findViewById(R.id.add);
        Button removeAll = findViewById(R.id.removeAll);

        for (int i = 0; i < 50; i++) {
            String text = getText();
            TextView textView = new TextView(SampleActivity.this);
            textView.setText(index + ": " + text);
//            textView.setTextSize(textSize[text.length() > 10 ? 9 : text.length() - 1]);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    flowLayout.removeView(view);
                }
            });

            flowLayout.addView(textView);
        }

        // align
        align.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.left:
                        flowLayout.setAlign(FlowLayout.ALIGN_LEFT);
                        break;
                    case R.id.right:
                        flowLayout.setAlign(FlowLayout.ALIGN_RIGHT);
                        break;
                    case R.id.center:
                        flowLayout.setAlign(FlowLayout.ALIGN_CENTER);
                        break;
                    case R.id.justify:
                        flowLayout.setAlign(FlowLayout.ALIGN_JUSTIFY);
                        break;
                }
            }
        });

        // vertical align
        verticalAlign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.top:
                        flowLayout.setVerticalAlign(FlowLayout.VERTICAL_ALIGN_TOP);
                        break;
                    case R.id.middle:
                        flowLayout.setVerticalAlign(FlowLayout.VERTICAL_ALIGN_MIDDLE);
                        break;
                    case R.id.bottom:
                        flowLayout.setVerticalAlign(FlowLayout.VERTICAL_ALIGN_BOTTOM);
                        break;
                }
            }
        });

        // direction
        direction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.ltr:
                        flowLayout.setDirection(FlowLayout.DIRECTION_LTR);
                        break;
                    case R.id.rtl:
                        flowLayout.setDirection(FlowLayout.DIRECTION_RTL);
                        break;
                }
            }
        });

        // last row align
        lastRowAlign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.lastRowLeft:
                        flowLayout.setLastRowAlign(FlowLayout.ALIGN_LEFT);
                        break;
                    case R.id.lastRowRight:
                        flowLayout.setLastRowAlign(FlowLayout.ALIGN_RIGHT);
                        break;
                    case R.id.lastRowCenter:
                        flowLayout.setLastRowAlign(FlowLayout.ALIGN_CENTER);
                        break;
                    case R.id.lastRowJustify:
                        flowLayout.setLastRowAlign(FlowLayout.ALIGN_JUSTIFY);
                        break;
                    case R.id.lastRowInherit:
                        flowLayout.setLastRowAlign(FlowLayout.ALIGN_INHERIT);
                        break;
                }
            }
        });

        // add
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = getText();
                TextView textView = new TextView(SampleActivity.this);
                textView.setText(index + ": " + text);
                textView.setTextSize(textSize[text.length() > 10 ? 9 : text.length() - 1]);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(layoutParams);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flowLayout.removeView(view);
                    }
                });

                flowLayout.addView(textView);
            }
        });

        //remove all
        removeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowLayout.removeAllViews();
            }
        });
    }

    private String getText() {
        String text = texts[index];
        index++;
        if (index >= 50) {
            index = 0;
        }

        return text;
    }
}
