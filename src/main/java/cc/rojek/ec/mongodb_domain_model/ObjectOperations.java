package cc.rojek.ec.mongodb_domain_model;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

public class ObjectOperations {

    MongoOperations mongoOperation;

    public ObjectOperations(MongoOperations mongoOperation) {
        this.mongoOperation = mongoOperation;
    }

    public List<Object> getListOfObjectAndConnectedNodes() {
        Query query = new Query();
        List<Object> objectList = mongoOperation.find(query, Object.class);
        return objectList;
    }

}
