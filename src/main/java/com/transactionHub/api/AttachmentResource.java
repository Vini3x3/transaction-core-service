package com.transactionHub.api;

import com.transactionHub.service.AttachmentService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.util.helper.WebHelper;
import com.transactionHub.web.UploadItemSchema;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Path("/attachment")
public class AttachmentResource {

    @Inject
    AttachmentService attachmentService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importAttachment(@QueryParam("date") Date date, @QueryParam("offset") Integer offset, @QueryParam("account") String account, @QueryParam("statement") @Schema(implementation = UploadItemSchema.class) FileUpload file) throws IOException {

        var inputStream = Files.newInputStream(file.uploadedFile());
        AccountEnum accountEnum = WebHelper.parseAccountEnum(account);

        String attachmentId = attachmentService.save(date, offset, accountEnum, inputStream, file.fileName());

        return Response.accepted().entity(attachmentId).build();
    }

    @DELETE
    @Path("/{attachmentId}")
    public Response deleteAttachment(@PathParam("attachmentId") String attachmentId) {
        return Response.accepted().build();
    }

}
