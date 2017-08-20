package TestRail;

import org.json.simple.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestRail {

    APIClient testRailClient;
    Long runID;
    Map data = new HashMap();
    private String Base_URL = "https://gdrive.testrail.net/";
    private String userName = "sadovec.k@gmail.com";
    private String PASS = "sadovets";
    private String runName = "APITest ";

    public void start () throws Exception{
        testRailClient = new APIClient(Base_URL);
        testRailClient.setUser(userName);
        testRailClient.setPassword(PASS);
        Map data = new HashMap();
        data.put("name" , runName + new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date()));
        JSONObject c = (JSONObject) testRailClient.sendPost("add_run/2", data);
        runID = (Long) c.get("id");
    }

    public void sendResult(int testId, Integer testNGResult )throws Exception{
        Map result = new HashMap();
        result.put("status_id", convertResult(testNGResult));
        testRailClient.sendPost("add_result_for_case/" + runID + "/" + testId, result);
    }

    public void closeRun() throws Exception {
        testRailClient.sendPost("close_run/" + runID, data);
    }

    private Integer convertResult(Integer testNGResult) {
        switch (testNGResult) {
            case 1:
                return 1;   // Success
            case 2:
                return 5;   // Failure
            case 3:
                return 2;   // Skip/Blocked
            default:
                return 4;  //Retest
        }
    }
}
