package com.transactionHub.api;

import com.transactionHub.service.TagService;
import com.transactionHub.transactionCoreLibrary.domain.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/tag")
public class TagResource {

    @Inject
    TagService tagService;

    @GET
    @Path("{tag}")
    public Tag find(@PathParam("tag") String tag) {
        var result = tagService.read(tag);
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    @POST
    public void create(Tag tag) {
        tagService.create(tag);
    }

    @PUT
    public void update(Tag tag) {
        tagService.update(tag);
    }

    @Path("{tag}")
    @DELETE
    public void delete(@PathParam("tag") String tag) {
        tagService.delete(tag);
    }


}
