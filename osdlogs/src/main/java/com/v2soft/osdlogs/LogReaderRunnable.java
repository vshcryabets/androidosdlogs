package com.v2soft.osdlogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Thread that reads continuously log output.
 */
public class LogReaderRunnable extends Thread {

    @Override
    public void run() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"logcat"});
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            final String[] lines = new String[1];
            while (!isInterrupted()) {
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

    protected void handleLine(String line) {

    }
}
