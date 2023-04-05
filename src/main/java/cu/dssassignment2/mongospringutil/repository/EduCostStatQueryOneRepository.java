package cu.dssassignment2.mongospringutil.repository;

import cu.dssassignment2.mongospringutil.model.EduCostStatQueryOne;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EduCostStatQueryOneRepository extends MongoRepository<EduCostStatQueryOne, String> {
}
