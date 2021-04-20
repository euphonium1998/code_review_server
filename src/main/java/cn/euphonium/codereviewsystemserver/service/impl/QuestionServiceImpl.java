package cn.euphonium.codereviewsystemserver.service.impl;

import cn.euphonium.codereviewsystemserver.entity.*;
import cn.euphonium.codereviewsystemserver.mapper.QuestionMapper;
import cn.euphonium.codereviewsystemserver.mapper.UserMapper;
import cn.euphonium.codereviewsystemserver.service.QuestionService;
import cn.euphonium.codereviewsystemserver.utils.CodeUtils;
import cn.euphonium.codereviewsystemserver.utils.SandboxUtils;
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
    public SqlMsg insertOneQuestion(Question question) {
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
        SqlMsg sqlMsg = new SqlMsg();
        try {
            questionMapper.insertOneQuestion(question);
            int pid = questionMapper.selectQuestionByName(question.getName());
            for (Sample s : question.getSamples()) {
                s.setPid(pid);
                questionMapper.insertOneSample(s);
            }
        } catch (Exception e) {
            sqlMsg.setSqlError(e.toString());
            sqlMsg.setStatus(ConstInfo.SQL_ERROR);
        }
        return sqlMsg;

    }

    @Transactional
    @Override
    public Question selectOneQuestionById(int id) {
        Question question = questionMapper.selectOneQuestion(id);
        question.setAccountName(userMapper.getNameByAccount(question.getAccount()));
//        question.setSamples(questionMapper.getSampleByPid(question.getId()));
        return question;
    }

    @Transactional
    @Override
    public List<Question> selectAllQuestion() {
        return questionMapper.selectAllQuestion();
    }

    @Override
    public OJResponse onlineJudgement(OJRequest ojRequest) {
        String fileId = "";
        OJResponse ojRes = new OJResponse();
        ojRes.setStatus("Accepted");
//        System.out.println(ojRequest);
        try {
            Question question = questionMapper.selectOneQuestion(ojRequest.getQid());
            ojRes.setName(question.getName());
            question.setSamples(questionMapper.getSampleByPid(question.getId()));
//            System.out.println(question);
            fileId = SandboxUtils.compileCodeInSandbox(ojRequest.getCode());
            for (Sample sample : question.getSamples()) {
                //judge
//                System.out.println(sample);
                ojRes = SandboxUtils.onlineJudgeOneSample(sample, fileId, ojRes);
            }
        } catch (Exception e) {
            if (e.toString().equals("java.lang.Exception: compile error")) {
                ojRes.setStatus("Compile error");
            } else {
                ojRes.setStatus("Other error[" + e.toString() + "]");
            }
            return ojRes;
        } finally {
            if (!fileId.equals("")) {
                SandboxUtils.deleteFileByIdInSandbox(fileId);
            }
            return ojRes;
        }

    }
}
