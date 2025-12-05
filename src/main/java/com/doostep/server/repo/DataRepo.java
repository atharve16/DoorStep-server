package com.doostep.server.repo;

import com.doostep.server.model.DataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRepo extends MongoRepository<DataEntity, String> {
}
