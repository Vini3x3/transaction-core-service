package com.transactionHub.api;

import com.transactionHub.scenario.TagScenario;
import com.transactionHub.service.TagService;
import com.transactionHub.transactionCoreLibrary.domain.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;

@Path("/tag")
public class TagResource {

    @Inject
    TagScenario tagScenario;

    @GET
    @Path("{tag}")
    public Tag find(@PathParam("tag") String tag) {
        var result = tagScenario.find(tag);
        if (result == null) {
            throw new NotFoundException();
        }
        return result;
    }

    @POST
    public void createOrUpdate(Tag tag) {
        tagScenario.createOrUpdate(tag);
    }

    @Path("{tag}")
    @DELETE
    public void delete(@PathParam("tag") String tag) {
        tagScenario.delete(tag);
    }


}
