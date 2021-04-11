package cn.euphonium.codereviewsystemserver.service.impl;

import cn.euphonium.codereviewsystemserver.entity.Question;
import cn.euphonium.codereviewsystemserver.entity.Sample;
import cn.euphonium.codereviewsystemserver.mapper.QuestionMapper;
import cn.euphonium.codereviewsystemserver.mapper.UserMapper;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    @Transactional
    public void insertOneQuestion(Question question) {
//        questionMapper.
//        Question q = new Question();
//        q.setName("test test");
//        q.setAccount("170110314");
//        q.setQuestionDescription("alielie");
//        q.setInputDescription("bilibili");
//        q.setOutputDescription("acfun");
//        q.setSampleInput("1 2 3\n" +
//                "4 5 6 7\n" +
//                "2 3");
//        q.setSampleOutput("2 3 4");
//        questionMapper.insertOneQuestion(q);
//        q.setAccount("170110324");
//        questionMapper.insertOneQuestion(q);
        questionMapper.insertOneQuestion(question);
        int pid = question.getId();
        for (Sample s : question.getSamples()) {
            s.setPid(pid);
            questionMapper.insertOneSample(s);
        }
    }

    @Transactional
    @Override
    public Question selectOneQuestionById(int id) {
        Question question = questionMapper.selectOneQuestion(id);
        question.setAccountName(userMapper.getNameByAccount(question.getAccount()));
        return question;
    }

    @Transactional
    @Override
    public List<Question> selectAllQuestion() {
        return questionMapper.selectAllQuestion();
    }
}
