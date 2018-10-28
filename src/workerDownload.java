import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class workerDownload implements Runnable {
    private URL url;
    private int start,end;
    private String filename;
    private static int BUFFER_SIZE = 4096;


    public workerDownload(URL url, int start , int end, String filename) {
        this.url = url;
        this.start = start;
        this.end = end;
        this.filename = filename;
    }

    @Override
    public void run() {
        BufferedInputStream in = null;
        RandomAccessFile rfile = null;

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String byteRange = start + "-" + end;
            con.setRequestProperty("Range", "bytes=" + byteRange);
            System.out.println("bytes=" + byteRange );


            // connect to server
            con.connect();
            if(con.getResponseCode() / 100 != 2) throw new Error("not 200 code");

            in = new BufferedInputStream(con.getInputStream());

            rfile = new RandomAccessFile(filename,"rw");
            rfile.seek(start);

            byte data[] = new byte[BUFFER_SIZE];
            int numread;

            while((numread = in.read(data,0,BUFFER_SIZE)) != -1)
            {
                rfile.write(data,0,numread);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(rfile != null) rfile.close();
                if(in!= null) in.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

    }
}
