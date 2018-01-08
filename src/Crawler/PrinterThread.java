package Crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class PrinterThread implements Runnable{
    String fileName;
    String text;

    public PrinterThread(String fileName, String text) {
        this.fileName = fileName;
        this.text = text;
    }

    @Override
    public void run() {
        try {
            File file = new File(fileName);
            file.getParentFile().mkdirs();
            PrintWriter pw = new PrintWriter(file);
            pw.print(text);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}