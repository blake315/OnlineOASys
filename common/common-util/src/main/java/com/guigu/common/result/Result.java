package com.guigu.common.result;

import lombok.Data;


/**
 * 定义统一的返回对象结果
 * @param <T>
 */
@Data
public class Result<T> {

    private Integer code; //状态码
    private String message; //返回信息
    private T data; //统一返回的结果数据


    //私有化，不能在外部new
    private Result() {}

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();

        // 封装数据
        if (body!=null){
            result.setData(body);
        }

        // 状态码
        result.setCode(resultCodeEnum.getCode());
        //返回信息
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    //成功返回方法
    public static<T> Result<T> success(){
        return build(null,ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data){

        return build(data, ResultCodeEnum.SUCCESS);
    }

    //失败返回方法
    public static <T> Result<T> fail(){
        return build(null, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data){
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }


}
