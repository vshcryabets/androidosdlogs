package com.v2soft.androidosdlogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.v2soft.osdlogs.LogReaderRunnable;

public class MainActivity extends AppCompatActivity {

    private Thread readThread;
    private TextView text;
    private StringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.text);
        builder = new StringBuilder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readThread = new LogReaderRunnable() {
            protected void handleLine(String line) {
                builder.append(line);
                builder.append('\n');
                int overLenggth = builder.length() - 2048;
                if (overLenggth > 0) {
                    builder.replace(0, overLenggth, "");
                }
                text.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(builder.toString());
                    }
                });
            }
        };
        readThread.start();
    }

    @Override
    protected void onPause() {
        readThread.interrupt();
        super.onPause();
    }
}
