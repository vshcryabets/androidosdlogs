package com.v2soft.androidosdlogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        readThread = new Thread(readRunnable);
        readThread.start();
    }

    @Override
    protected void onPause() {
        readThread.interrupt();
        super.onPause();
    }

    private Runnable readRunnable = new Runnable() {
        @Override
        public void run() {
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"logcat"});
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                final String[] lines = new String[1];
                while (!readThread.isInterrupted()) {
                    lines[0] = bufferedReader.readLine();
                    System.out.print(lines[0]);
                    //if ((prevLine != null) && (prevLine.equals(lines[0])))
                    if (lines[0] != null) {
                        handleLine(lines[0]);
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private void handleLine(String line) {
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
}
