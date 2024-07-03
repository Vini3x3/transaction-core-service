package com.transactionHub.repository;

import com.transactionHub.entity.Tag;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TagRepository  implements PanacheMongoRepository<Tag>  {
}
