package cn.euphonium.codereviewsystemserver.controller;

import cn.euphonium.codereviewsystemserver.entity.Question;
import cn.euphonium.codereviewsystemserver.entity.SqlMsg;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping(value = "/insertOneQuestion")
    public SqlMsg insertOneQuestion(@RequestBody Question question) {
        return questionService.insertOneQuestion(question);
    }
}
