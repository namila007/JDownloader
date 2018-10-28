public class main {
    public static void main(String[] args) {
        String url = "http://tour.lk/assets/images/srilanka-logo.png";
        Download dl1 = new Download(url,1);
        Download dl2 = new Download(url,2);
        Download dl3 = new Download(url,4);
        dl1.downloadFile();
        dl2.downloadFile();
        dl3.downloadFile();
    }
}
