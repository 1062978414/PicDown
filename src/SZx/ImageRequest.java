package SZx;



import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageRequest {
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        System.out.print("输入要下载的图片数量：\n");
        int num;
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            System.out.println("输入的不是数字，默认设置为1");
            num = 1;
            //System.exit(0);
        }else{
            num = new Integer(str);
    }
        int i = 0;
        System.out.print("下载中，请稍候...\n");
       while(i<num){
           System.out.print("正在下载第"+(i+1)+"张\n");
          // String picName = String.valueOf(System.currentTimeMillis() + new Random().nextInt())+".jpg";
           getPic();
           i++;
       }
        System.out.print("下载完成。\n");
    }


    public static void getPic() throws Exception {
        //new一个URL对象
        URL url = new URL("https://api.mtyqx.cn/api/random.php");
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        conn.setInstanceFollowRedirects(false);

        String redirectUrl = conn.getHeaderField("Location");
        //获取重定向地址后
        url = new URL(redirectUrl);
        //打开链接
        conn = (HttpURLConnection)url.openConnection();
        //通过输入流获取图片数据

        InputStream inStream = conn.getInputStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        String picName = url.getFile().toString().substring(7, url.getFile().length());

        //new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(picName);

        //创建输出流
        FileOutputStream outStream = new FileOutputStream("F:/pic/"+imageFile);

        //写入数据
        outStream.write(data);
        //关闭输出流
        outStream.close();
    }
    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }
}