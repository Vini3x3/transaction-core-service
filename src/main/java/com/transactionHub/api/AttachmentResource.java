package com.transactionHub.api;

import com.transactionHub.entity.Attachment;
import com.transactionHub.scenario.AttachmentScenario;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.web.UploadItemSchema;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Path("/attachment")
public class AttachmentResource {

    @Inject
    AttachmentScenario attachmentScenario;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveAttachment(@RestForm("date") Date date, @RestForm("offset") Integer offset, @RestForm("account") AccountEnum account, @RestForm("attachment") @Schema(implementation = UploadItemSchema.class) FileUpload file) throws IOException {

        var inputStream = Files.newInputStream(file.uploadedFile());
        String attachmentId = attachmentScenario.saveAttachment(date.toInstant(), offset, account, inputStream, file.fileName());

        return Response.accepted().entity(attachmentId).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Path("/download/{attachmentId}")
    public Response downloadAttachment(@PathParam("attachmentId") String attachmentId) {
        Attachment attachment = attachmentScenario.downloadAttachment(attachmentId);
        return Response.ok(attachment.getContent())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + attachment.getFilename())
                .build();
    }

    @DELETE
    @Path("/{attachmentId}")
    public Response deleteAttachment(@PathParam("attachmentId") String attachmentId) {
        attachmentScenario.deleteAttachment(attachmentId);
        return Response.accepted().build();
    }

}
