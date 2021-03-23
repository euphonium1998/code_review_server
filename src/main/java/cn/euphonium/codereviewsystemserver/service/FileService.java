package cn.euphonium.codereviewsystemserver.service;

import cn.euphonium.codereviewsystemserver.entity.CodeMsg;

import java.io.IOException;

public interface FileService {
    String codeReview(String code) throws IOException;
    CodeMsg codeFormat(CodeMsg codeMsg);
}
