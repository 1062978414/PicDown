package SZx;
/**
 * Created by david on 2017-7-5.
 */
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtil {
    /**
     * ����http���󲢻�ȡ���
     * @param requestUrl �����ַ
     */
    public static JsonObject getXpath(String requestUrl){
        String res="";
        JsonObject object = null;
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection urlCon= (HttpURLConnection)url.openConnection();
            if(200==urlCon.getResponseCode()){
                InputStream is = urlCon.getInputStream();
                InputStreamReader isr = new InputStreamReader(is,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String str = null;
                while((str = br.readLine())!=null){
                    buffer.append(str);
                }
                br.close();
                isr.close();
                is.close();
                res = buffer.toString();
                JsonParser parse =new JsonParser();
                object = (JsonObject) parse.parse(res);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return object;
    }

   public static JsonObject postDownloadJson(String path,String post){
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// �ύģʽ
            // conn.setConnectTimeout(10000);//���ӳ�ʱ ��λ����
            // conn.setReadTimeout(2000);//��ȡ��ʱ ��λ����
            // ����POST�������������������
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // �����������
            printWriter.write(post);//post�Ĳ��� xx=xx&yy=yy
            // flush������Ļ���
            printWriter.flush();
            //��ʼ��ȡ����
            BufferedInputStream bis = new            BufferedInputStream(httpURLConnection.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            JsonParser parse = new JsonParser();
            return (JsonObject)parse.parse(bos.toString("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //����
    public static void main(String args [] ) {
        JsonObject res = null;
  //      res = getXpath("http://ip.taobao.com/service/getIpInfo.php?ip=63.223.108.42");
        res = postDownloadJson("http://localhost.ptlogin2.qq.com:4300/mc_get_uins","123");
        System.out.println(res);
        System.out.println(res.get("code"));
        System.out.println(res.get("data"));
    }
}