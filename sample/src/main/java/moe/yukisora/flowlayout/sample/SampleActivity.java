package moe.yukisora.flowlayout.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        FlowLayout flowLayout = findViewById(R.id.flowLayout);

        for (int i = 0; i < 50; i++) {
            TextView textView = new TextView(this);
            String text = i + ": " + texts[i];
            textView.setText(text);
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            flowLayout.addView(textView);
        }
    }
}
