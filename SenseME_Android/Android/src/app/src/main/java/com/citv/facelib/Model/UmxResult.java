package com.citv.facelib.Model;

/**
 * Created by kay on 2016/7/14.
 */
public enum UmxResult {
    SUCCESS(0),
    ERR_UNITY(1),
    ERR_UNKOWN(2),
    ERR_BAD_ARG(3),
    ERR_FILE_NOT_FOUND(4);

    private final int value;

    int getValue(){
        return value;
    }

    UmxResult(int value){
        this.value=value;
    }
}
