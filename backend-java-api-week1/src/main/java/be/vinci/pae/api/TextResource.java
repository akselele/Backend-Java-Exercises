package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.domain.Text;
import be.vinci.pae.services.DataServiceTextCollection;

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
@Path("/texts")
public class TextResource {	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text create(Text text) {
		if (text == null || text.getContent() == null || text.getContent().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		DataServiceTextCollection.addText(text);

		return text;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text getText(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());
		Text textFound = DataServiceTextCollection.getText(id);

		if (textFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return textFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Text updateText(Text text, @PathParam("id") int id) {

		if (text == null || text.getContent() == null || text.getContent().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());

		text.setId(id);
		Text updatedText = DataServiceTextCollection.updateText(text);

		if (updatedText == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updatedText;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Text deleteFilm(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		Text deletedText = DataServiceTextCollection.deleteText(id);

		if (deletedText == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedText;
	}

	@GET     
	@Produces(MediaType.APPLICATION_JSON)     
	public List<Text> getAllTexts(@DefaultValue("") @QueryParam("level") String level) {   
		if (!level.equals("")) 
			return DataServiceTextCollection.getTexts(level);         
		return DataServiceTextCollection.getTexts();      
		}


}
