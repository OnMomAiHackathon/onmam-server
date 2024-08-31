package mapper.util;

import dto.diary.question.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessageMapper {
    public static Map<String,String> ResponseMessage(String message){
        Map<String,String> map = new HashMap<>();
        map.put("message",message);
        return map;
    }
}
