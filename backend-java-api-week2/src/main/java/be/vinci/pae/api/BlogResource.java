package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.Blog;
import be.vinci.pae.services.DataServiceBlogCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/blog")
public class BlogResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorize
    public Blog create(Blog blog) {
        if (blog == null || blog.getTitle() == null || blog.getTitle().isEmpty())
            throw new WebApplicationException(
                    Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
        DataServiceBlogCollection.addBlog(blog);

        return blog;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize
    public Blog getBlog(@PathParam("id") int id) {
        if (id == 0)
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
                    .type("text/plain").build());
        Blog filmFound = DataServiceBlogCollection.getBlog(id);

        if (filmFound == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND)
                    .entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

        return filmFound;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Authorize
    public Blog updateBlog(Blog blog, @PathParam("id") int id) {

        if (blog == null || blog.getTitle() == null || blog.getTitle().isEmpty())
            throw new WebApplicationException(
                    Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());

        blog.setId(id);
        Blog updatedFilm = DataServiceBlogCollection.updateBlog(blog);

        if (updatedFilm == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND)
                    .entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

        return updatedFilm;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authorize
    public Blog deleteFilm(@PathParam("id") int id) {
        if (id == 0)
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
                    .type("text/plain").build());

        Blog deletedFilm = DataServiceBlogCollection.deleteBlog(id);

        if (deletedFilm == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND)
                    .entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

        return deletedFilm;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> getAllFilms(@DefaultValue("-1") @QueryParam("minimum-duration") int minimumDuration) {
        return DataServiceBlogCollection.getBlogs();
    }

}
