package cn.euphonium.codereviewsystemserver.controller;

import cn.euphonium.codereviewsystemserver.entity.OJRequest;
import cn.euphonium.codereviewsystemserver.entity.OJResponse;
import cn.euphonium.codereviewsystemserver.entity.Question;
import cn.euphonium.codereviewsystemserver.entity.SqlMsg;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/getQuestion")
    public Question selectOneQuestionById(@RequestParam Integer id) {
        return questionService.selectOneQuestionById(id);
    }

    @GetMapping(value = "/getQuestionList")
    public List<Question> selectAllQuestion() {
        return questionService.selectAllQuestion();
    }

    @PostMapping(value = "/oj")
    public OJResponse onlineJudgement(@RequestBody OJRequest ojRequest) {
        return questionService.onlineJudgement(ojRequest);
    }

}
