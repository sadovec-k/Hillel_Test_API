import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
        String findUserId = "(?:\"id\":)(?:\\d+)";
        String findId = "\\d+";
        String userData = "(?:\\{.+\"id\":\\d+.+\"MyNewChangedName\".+\\})";

    public String getUserId(String inPutString){
        Pattern stringPattern = Pattern.compile(findUserId);
        Matcher rezalt = stringPattern.matcher(inPutString);
        String rezaltString = (rezalt.find()? inPutString.substring(rezalt.start(), rezalt.end()): " ");
        Pattern newPattern = Pattern.compile(findId);
        Matcher newrezalt = newPattern.matcher(rezaltString);
        String userId = (newrezalt.find()? rezaltString.substring(newrezalt.start(), newrezalt.end()): " ");
        return userId;
    }

    public Boolean findUserByName(String inPutString){
        Pattern newPattern = Pattern.compile(userData);
        Matcher newrezalt = newPattern.matcher(inPutString);
        return newrezalt.find();
    }
    public Boolean findUserById(String inPutString, String myUserId){
        String userData = "(?:\\.+\"id\":)" + myUserId;
        Pattern newPattern = Pattern.compile(userData);
        Matcher newrezalt = newPattern.matcher(inPutString);
        return newrezalt.find();
    }

    public void sleep(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
        }
    }
}