import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class TestAPI {

   private static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String[] sendGet(String URI, String params, String ContentType) throws Exception {
            try {
                HttpGet httpget = new HttpGet(URI + params);
                httpget.addHeader("Content-Type",ContentType);
                HttpResponse respons = httpclient.execute(httpget);
                return returnResponse(respons);
            } finally {

            }
    }

    public static String[] sendPost(String URI, String paramBody, String ContentType) throws Exception {
        try {
            HttpPost httppost = new HttpPost(URI);
            HttpEntity json = new ByteArrayEntity(paramBody.getBytes("UTF-8"));
            httppost.addHeader("Content-Type",ContentType);
            httppost.setEntity(json);
            HttpResponse respons = httpclient.execute(httppost);
            return returnResponse(respons);
        } finally {

        }
    }

    public static String[] sendDelete(String URI, String params, String ContentType) throws Exception {
        try  {
            HttpDelete httpDelete = new HttpDelete(URI);
            httpDelete.addHeader("Content-Type",ContentType);
            HttpResponse respons = httpclient.execute(httpDelete);
            return returnResponse(respons);
        } finally {

        }
    }

    public static String[] sendPut(String URI, String paramBody, String ContentType) throws Exception {
        try {
            HttpPut httpput = new HttpPut(URI);
            HttpEntity json = new ByteArrayEntity(paramBody.getBytes("UTF-8"));
            httpput.addHeader("Content-Type",ContentType);
            httpput.setEntity(json);
            HttpResponse respons = httpclient.execute(httpput);
            return returnResponse(respons);
        } finally {

        }
    }

    public static String[] returnResponse(HttpResponse respons) throws Exception{
        String[] responseArr = new String[3];
        responseArr[0]= respons.getEntity() != null ? EntityUtils.toString(respons.getEntity()):"Respons is empty";   //response
        responseArr[1] = respons.getStatusLine() != null ?
                Integer.toString(respons.getStatusLine().getStatusCode()): "Status is empty";                          //status code
        responseArr[2] = respons.getEntity() != null ? respons.getEntity().toString(): "Headers are empty";             //headers
        return responseArr;
    }

    public void closeHttp () throws Exception{
        httpclient.close();
    }

}