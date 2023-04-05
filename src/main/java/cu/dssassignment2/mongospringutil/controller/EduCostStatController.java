package cu.dssassignment2.mongospringutil.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import cu.dssassignment2.mongospringutil.model.EduCostStat;
import cu.dssassignment2.mongospringutil.service.EduCostStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@RequestMapping("/")
public class EduCostStatController {

    @Autowired
    EduCostStatService eduCostStatService;

    @PostMapping("/upload")
    public String uploadCsvFile(@RequestBody String request) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("nces330_20.csv")))) {

            CsvToBean<EduCostStat> csvToBean = new CsvToBeanBuilder<EduCostStat>(reader).withType(EduCostStat.class)
                    .withIgnoreLeadingWhiteSpace(true).build();

            List<EduCostStat> eduCostStats = csvToBean.parse();

            for (EduCostStat eduCostStat : eduCostStats) {
                eduCostStatService.uploadDataset(eduCostStat);
            }

            return "File uploaded successfully.";

        } catch (IOException e) {
            return "File upload failed: " + e.getMessage();
        }

    }
}
