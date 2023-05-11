package com.guigu.common.config.exception;


import com.guigu.common.result.ResultCodeEnum;
import lombok.Data;
import lombok.ToString;

@Data
public class SelfException extends RuntimeException{
    private Integer code;
    private String msg;

    public SelfException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SelfException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "SelfException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
