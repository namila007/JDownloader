import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Download {
    private URL url ;
    private String filename;
    public Download (String url) {
        try {
            this.url = new URL(url);
            this.filename = url.substring(url.lastIndexOf("/")+1);
            System.out.println(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void downloadFile() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        HttpURLConnection con;
        try {
            con = (HttpURLConnection)url.openConnection();
            con.connect();

            int fileLength = con.getContentLength();

            executorService.submit(new workerDownload(url,0,fileLength/2,filename));
            executorService.submit(new workerDownload(url,(fileLength/2+1),fileLength,filename));
            executorService.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
}
