package cu.dssassignment2.mongospringutil.service;

import cu.dssassignment2.mongospringutil.model.EduCostStat;
import cu.dssassignment2.mongospringutil.repository.EduCostStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EduCostStatService {

    EduCostStatRepository eduCostStatRepository;

    public List<cu.assignment2.proto.EduCostStat> getEduCostStatByYear(String year) {
        return eduCostStatRepository.findByYear(year);
    }

    public EduCostStat uploadDataset(EduCostStat eduCostStat) {
        return eduCostStatRepository.save(eduCostStat);
    }

    public List<EduCostStat> getAllStat() {
        return eduCostStatRepository.findAll();
    }

    public List<cu.assignment2.proto.EduCostStat> getAllStatByYear(String year) {
        return eduCostStatRepository.findByYear(year);
    }

    public List<EduCostStat> getEduCostStatByQ1Request(String year, String state, String type, String length, String expense) {
        return eduCostStatRepository.findByYearAndStateAndTypeAndLengthAndExpense(year, state, type, length, expense);
    }
}
