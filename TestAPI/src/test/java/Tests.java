import TestRail.TestRail;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.util.regex.Pattern;

public class Tests  {
    private TestAPI API = new TestAPI();
    private Tools tools;
    String URI = "http://soft.it-hillel.com.ua:3000/api/users";
    String params = "";
    String ContentType = "application/json";
    String patternGetlist = ".+id.+";
    String patternContentType = ".+application/json.+";
    String status401 = "401";
    String status404 = "404";
    String patternIncorrectContentType = "text/html";
    String paramBody = "{\"name\": \"MyName\", \"phone\": \"7778855\",\"role\": \"Student\"}";
    String paramBodyRoleEmpty = "{\"name\":\"MyName\", \"phone\": \"7778855\",\"role\": \"\"}";
    String paramBodyRoleIncorrect = "{\"name\": \"MyName\", \"phone\": \"7778855\",\"role\": \"blabla\"}";
    String paramBodyAdmin = "{\"name\": \"MyNewAdmin\", \"phone\": \"7778899\",\"role\": \"Admin\"}";
    String paramBodySupport = "{\"name\": \"MyNewSupport\", \"phone\": \"1112211\",\"role\": \"Support\"}";
    String myUserId = "";
    String changeStudent = "{\"name\":\"MyNewChangedName\", \"phone\":\"1112233\",\"role\":\"Student\", " +
            "\"id\":\"";
    String newUsersId = "(?:\\{(?:.+)?\"id\":)(?:\\d+)(?:)?\\}";

    private TestRail myTestRail;
    public int testId = 0;

    @BeforeClass
    protected void prepareTestRail() throws Exception {
        myTestRail = new TestRail();
        myTestRail.start();
    }

    @AfterMethod
    protected void reportResult(ITestResult testResult) throws Exception {
        System.out.println(testId);
        myTestRail.sendResult(testId+123, testResult.getStatus());
    }

    @AfterClass
    protected void closeTestRailRun() throws Exception {
        myTestRail.closeRun();
        API.closeHttp();
    }


    @Test(description = "Get user list using method Get. Expected behavior : users list")
    void getUsersList() throws Exception {
        testId = 1;
        Assert.assertTrue(Pattern.matches(patternGetlist, API.sendGet(URI, params, ContentType)[0]));
    }

    @Test(description = "Check Content Type in Get response. Expected behavior : application/json ")
    void ContentTypeInGet() throws Exception {
        testId = 2;
        Assert.assertTrue(Pattern.matches(patternContentType, API.sendGet(URI, params, ContentType)[2]));
    }

    @Test(description = "Send empty Content Type in method Get. Expected behavior : 401")
    void ContentTypeEmpty() throws Exception {
        testId = 3;
        Assert.assertTrue(Pattern.matches(status401, API.sendGet(URI, params, "")[1]));
    }

    @Test(description = "Send incorrect Content Type in method Get. Expected behavior : 401")
    void ContentTypeIncorrectValue() throws Exception {
        testId = 4;
        Assert.assertTrue(Pattern.matches(status401, API.sendGet(URI, params, patternIncorrectContentType)[1]));
    }

    @Test(description = "Create new user (student) using method Post. Expected behavior : new user's id")
    void createNewStudent() throws Exception {
        testId = 5;
        tools = new Tools();
        myUserId = tools.getUserId(API.sendPost(URI, paramBody, ContentType)[0]);
       // Assert.assertTrue(Pattern.matches(newUsersId, response[0]));
    }

    @Test(description = "Check Content Type in Post response. Expected behavior : application/json ")
    void ContentTypeInPost() throws Exception {
        testId = 6;
        Assert.assertTrue(Pattern.matches(patternContentType, API.sendPost(URI, params, ContentType)[2]));
    }

    @Test(description = "Send empty Content Type in method Post. Expected behavior : 401")
    void ContentTypeEmptyPost() throws Exception {
        testId = 7;
        Assert.assertTrue(Pattern.matches(status401, API.sendPost(URI, params, "")[1]));
    }

    @Test(description = "Send incorrect Content Type in method Post. Expected behavior : 401")
    void ContentTypeIncorrectValuePost() throws Exception {
        testId = 8;
        Assert.assertTrue(Pattern.matches(status401, API.sendPost(URI, params, patternIncorrectContentType)[1]));
    }

    @Test(description = "Create new user (student) using method Post. Parametr role is empty. Expected behavior : new user's id")
    void createNewStudentRoleEmpty() throws Exception {
        testId = 9;
        Assert.assertTrue(Pattern.matches(newUsersId, API.sendPost(URI, paramBodyRoleEmpty, ContentType)[0]));
    }

    @Test(description = "Create new user (student) using method Post. Parametr role is incorrect. Expected behavior : new user's id")
    void createNewStudentRoleIncorrect() throws Exception {
        testId = 10;
        Assert.assertTrue(Pattern.matches(status401, API.sendPost(URI, paramBodyRoleIncorrect, ContentType)[1]));
    }

    @Test(description = "Create new administrator using method Post. Expected behavior : new administraotr's id")
    void createNewAdministrator() throws Exception {
        testId = 11;
        String[] response = API.sendPost(URI, paramBodyAdmin, ContentType);
        API.sendGet(URI+"//refreshAdmins", "", ContentType);
        Assert.assertTrue(Pattern.matches(newUsersId, response[0]));
    }

    @Test(description = "Create new support using method Post. Expected behavior : new support's id")
    void createNewSupport() throws Exception {
        testId = 12;
        String [] response = API.sendPost(URI, paramBodySupport, ContentType);
        Assert.assertTrue(Pattern.matches(newUsersId, response[0]));
    }

    @Test(description = "Change student using method Put. Expected behavior : new name user in list", dependsOnMethods = {"createNewStudent"})
    void changeSudent() throws Exception {
        testId = 13;
        tools = new Tools();
        API.sendPut(URI + "/" + myUserId, changeStudent + myUserId + "\"}", ContentType);
        tools.sleep(1);
        Assert.assertTrue(tools.findUserByName(API.sendGet(URI, params, ContentType)[0]));
    }

    @Test(description = "Change student using method Put. Send incorrect id. Expected behavior : 404")
    void changeSudentIncorrectId() throws Exception {
        testId = 14;
        tools = new Tools();
        API.sendPut(URI + "/" + "00000", changeStudent + "00000" + "\"}", ContentType);
        tools.sleep(1);
        Assert.assertTrue(Pattern.matches(status404, API.sendGet(URI, params, ContentType)[1]));
    }

    @Test(description = "Delete using method DELETE. Expected behavior : user has deleted",dependsOnMethods = {"createNewStudent"} )
    void deleteUser() throws Exception {
        testId = 15;
        API.sendDelete(URI + "/" + myUserId, params, ContentType);
        Assert.assertFalse(tools.findUserById(API.sendGet(URI, params, ContentType)[0],myUserId));
    }

    @Test(description = "Delete using method DELETE. Send incorrect id. Expected behavior : 404" )
    void deleteUserIncorrectId() throws Exception {
        testId = 16;
        API.sendDelete(URI + "/00", params, ContentType);
        tools.sleep(1);
        Assert.assertFalse(Pattern.matches(status404, API.sendGet(URI, params, ContentType)[1]));
    }
}