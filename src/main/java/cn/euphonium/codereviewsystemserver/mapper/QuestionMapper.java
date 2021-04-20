package cn.euphonium.codereviewsystemserver.mapper;


import cn.euphonium.codereviewsystemserver.entity.Question;
import cn.euphonium.codereviewsystemserver.entity.Sample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {
    int insertOneQuestion(Question question);

    List<Question> selectAllQuestion();

    int insertOneSample(Sample sample);

    Question selectOneQuestion(int id);

    int selectQuestionByName(String name);

    List<Sample> getSampleByPid(int pid);
}
