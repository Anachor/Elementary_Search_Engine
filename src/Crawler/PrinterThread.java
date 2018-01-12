package Crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class PrinterThread implements Runnable{
    String fileName;
    String text;
    String url;

    public PrinterThread(String fileName, String text, String url) {
        this.fileName = fileName;
        this.text = text;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            File file = new File(fileName + ".crawldata");
            file.getParentFile().mkdirs();
            PrintWriter pw = new PrintWriter(file);
            pw.println(url);
            pw.print(text);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}