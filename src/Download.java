import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Download {
    private URL url ;
    private String filename;
    private int threadsize;
    public Download (String url , int threadsize) {
        try {
            this.url = new URL(url);
            this.threadsize = threadsize;
            this.filename = threadsize+url.substring(url.lastIndexOf("/")+1);
            System.out.println(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void downloadFile() {
        ExecutorService executorService = Executors.newFixedThreadPool(threadsize);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection)url.openConnection();
            con.connect();

            int fileLength = con.getContentLength();
            System.out.println("File size: "+fileLength);
            Long start = System.nanoTime();

            for (int x = 0 ; x < threadsize ; x++) {
                executorService.submit(new workerDownload(url,(fileLength*x)/threadsize,(fileLength*(x+1))/threadsize,filename));
            }
            executorService.shutdown();
            executorService.awaitTermination(1,TimeUnit.HOURS);

            Long end = System.nanoTime();
            System.out.printf("Time Elapsed : %.4f ms\n",(end-start)/1e6);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
