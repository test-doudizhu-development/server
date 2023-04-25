package ch.uzh.ifi.hase.soprafs23.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result<T> implements Serializable  {


    /**
     * 200 成功
     */
    public Integer status;

    /***
     * 返回数据
     */
    private T data;

    /**
     * 消息内容
     */
    private String msg;

    private String token;


    /**
     * 得到返回结果
     */
    public static Result success(){
        return Result.builder().status(200).build();
    }
    public static Result success(String msg){
        return Result.builder().status(200).msg(msg).build();
    }

    public static Result successToken(String token){
        return Result.builder().status(200).token(token).build();
    }
    public  static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setData(data);
        result.setStatus(200);
        return result;
    }

    public  static  Result<Object> error(){
        return Result.builder().status(500).build();
    }

    public  static  Result<Object> error(String msg){
        return Result.builder().status(500).msg(msg).build();
    }
    public  static  Result<Object> error(String msg,Integer status){
        return Result.builder().status(status).msg(msg).build();
    }
}
