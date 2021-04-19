package cn.euphonium.codereviewsystemserver.entity;

public class ConstInfo {
    public static final int ERROR = 1;
    public static final int SUCCESS = 0;
    public static final int FILE_ERROR = 2;         //文件上传失败
    public static final int C_COMPILE_ERROR = 3;    //C语言gcc编译失败
    public static final int SQL_ERROR = 4;          //数据库操作失误
}
