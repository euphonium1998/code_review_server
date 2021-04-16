package cn.euphonium.codereviewsystemserver.service;

import cn.euphonium.codereviewsystemserver.entity.OJResponse;
import cn.euphonium.codereviewsystemserver.entity.Question;

import java.util.List;

public interface QuestionService {

    void insertOneQuestion(Question question);

    Question selectOneQuestionById(int id);

    List<Question> selectAllQuestion();

    OJResponse onlineJudgement(Question question, String code);


}
