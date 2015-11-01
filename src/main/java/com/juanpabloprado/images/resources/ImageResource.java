package com.juanpabloprado.images.resources;

import com.juanpabloprado.images.utils.FileIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/v1/images")
@Produces(MediaType.APPLICATION_JSON)
public class ImageResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(ImageResource.class);
  private final String dest;

  public ImageResource(String dest) {
    this.dest = dest;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response createImage(
      @FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException, URISyntaxException {
    String uploadedFileLocation = dest + fileDetail.getFileName();
    // save it
    FileIO.writeToFile(uploadedInputStream, uploadedFileLocation);
    LOGGER.info("Image uploaded to : " + uploadedFileLocation);

    return Response.created(new URI(fileDetail.getFileName())).build();
  }

  @GET
  @Path("/{image}")
  @Produces("image/*")
  public Response getImage(@PathParam("image") String image) {
    File file = new File(dest + image);
    if(!file.exists()) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return Response.ok(file, mimeType).build();
  }

}
