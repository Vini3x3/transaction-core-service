package com.transactionHub.repository;

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
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class AttachmentRepository {
    @Inject
    MongoClient mongoClient;

    @ConfigProperty(name = "quarkus.mongodb.database", defaultValue = "")
    String databaseName;

    public String save(Map<String, Object> meta, InputStream inputStream, String filename) {
        var gridFSBucket = getBucket();
        var options = new GridFSUploadOptions()
                .metadata(new Document(meta));
        return gridFSBucket.uploadFromStream(filename, inputStream, options).toHexString();
    }


    public Attachment download(String attachmentId) {
        var gridFSBucket = getBucket();
        try (GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(attachmentId))) {
            GridFSFile file = downloadStream.getGridFSFile();
            int fileLength = (int) file.getLength();
            byte[] content = new byte[fileLength];
            downloadStream.read(content);

            Map<String, Object> meta;
            if (file.getMetadata() != null) {
                meta = file.getMetadata().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            } else {
                meta = new HashMap<>();
            }
            
            return new Attachment(file.getFilename(), file.getUploadDate().toInstant().atZone(ZoneId.of("UTC")), meta, content);
        }
    }

    public void delete(String attachmentId) {
        var gridFSBucket = getBucket();
        gridFSBucket.delete(new ObjectId(attachmentId));
    }

    protected GridFSBucket getBucket() {
        var database = mongoClient.getDatabase(databaseName);
        return GridFSBuckets.create(database);
    }

}
