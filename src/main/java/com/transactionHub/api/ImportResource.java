package com.transactionHub.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;


/*
Technical Reference: https://stackoverflow.com/a/74466372
 */
@Path("/import")
public class ImportResource {

    @Path("statement")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importStatement(@RestForm("statement") @Schema(implementation = UploadItemSchema.class) FileUpload file) throws IOException {

        // print filename
        System.out.println(file.fileName());

        // get file inputStream
        var inputStream = Files.newInputStream(file.uploadedFile());
        System.out.println(new String(inputStream.readAllBytes()));

        return Response.accepted().build();
    }

    @Schema(type = SchemaType.STRING, format = "binary")
    public static class UploadItemSchema {

    }


}