package com.transactionHub.api;

import com.transactionHub.service.ImportService;
import com.transactionHub.transactionCoreLibrary.constant.AccountEnum;
import com.transactionHub.util.helper.WebHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;


/*
Technical Reference: https://stackoverflow.com/a/74466372
 */
@Path("/import")
public class ImportResource {

    @Inject
    protected ImportService importService;

    @Path("statement/{type}/{account}")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importStatement(@RestPath String type, @RestPath String account, @RestForm("statement") @Schema(implementation = UploadItemSchema.class) FileUpload file) throws IOException {

        var inputStream = Files.newInputStream(file.uploadedFile());
        AccountEnum accountEnum = WebHelper.parseAccountEnum(account);

        switch (type) {
            case "csv":
                importService.importCsv(accountEnum, inputStream, file.fileName());
                break;
            case "excel":
                importService.importExcel(accountEnum, inputStream, file.fileName());
            default:
                throw new WebApplicationException(String.format("Invalid file type %s", type));
        }
        return Response.accepted().build();
    }

    @Schema(type = SchemaType.STRING, format = "binary")
    public static class UploadItemSchema {

    }


}