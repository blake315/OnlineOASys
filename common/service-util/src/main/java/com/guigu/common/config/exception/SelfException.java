package com.guigu.common.config.exception;


import com.guigu.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
public class SelfException extends RuntimeException{
    private Integer code;
    private String message;

    public SelfException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public SelfException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "SelfException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
