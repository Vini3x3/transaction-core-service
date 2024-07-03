package com.transactionHub.repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.transactionHub.entity.Attachment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class AttachmentRepository {
    @Inject
    MongoClient mongoClient;

    public String save(Map<String, Object> meta, InputStream inputStream, String filename) {
        var database = mongoClient.getDatabase("transaction-db");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);


        var options = new GridFSUploadOptions()
                .metadata(new Document(meta));
        return gridFSBucket.uploadFromStream(filename, inputStream, options).toHexString();

    }

    public GridFSFile meta(String attachmentId) {
        var database = mongoClient.getDatabase("transaction-db");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);

//        var query = (Bson) (new BsonObjectId(new ObjectId(attachmentId)));
        var query = new BasicDBObject("_id", new ObjectId(attachmentId));

        return gridFSBucket.find(query).first();
    }


    public Attachment download(String attachmentId) {
        var database = mongoClient.getDatabase("transaction-db");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);

        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(attachmentId))) {
            GridFSFile file = downloadStream.getGridFSFile();
            int fileLength = (int) file.getLength();
            byte[] content = new byte[fileLength];
            downloadStream.read(content);
            assert file.getMetadata() != null;
            var meta = file.getMetadata().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return new Attachment(file.getFilename(), file.getUploadDate(), meta, content);
        }
    }

    public void delete(String attachmentId) {
        var database = mongoClient.getDatabase("transaction-db");
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        gridFSBucket.delete(new ObjectId(attachmentId));
    }

}
