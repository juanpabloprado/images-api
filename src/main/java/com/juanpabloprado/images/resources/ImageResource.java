package com.juanpabloprado.images.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juanpabloprado.images.utils.FileIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.activation.MimetypesFileTypeMap;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
      @FormDataParam("file") FormDataContentDisposition fileDetail) throws URISyntaxException, UnsupportedEncodingException {
    String uploadedFileLocation = dest + fileDetail.getFileName();
    try {
      // save it
      FileIO.writeToFile(uploadedInputStream, uploadedFileLocation);
      LOGGER.info("Image uploaded to : " + uploadedFileLocation);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    String image = URLEncoder.encode(fileDetail.getFileName(), "UTF-8");
    return Response.created(new URI(image)).build();
  }

  @GET
  @Path("/{image}")
  @Produces("image/*")
  public Response getImage(@PathParam("image") String image) throws UnsupportedEncodingException {
    image = URLDecoder.decode(image, "UTF-8");
    File file = new File(dest + image);
    if (!file.exists()) {
      LOGGER.warn(image + " could not be found");
      return Response.status(Response.Status.NOT_FOUND).entity(new Error("No image found")).build();
    }
    String mimeType = new MimetypesFileTypeMap().getContentType(file);
    return Response.ok(file, mimeType).build();
  }

  @DELETE
  @Path("/{image}")
  public Response deleteImage(@PathParam("image") String image) throws UnsupportedEncodingException {
    image = URLDecoder.decode(image, "UTF-8");
    try {
      File file = new File(dest + image);
      if (!file.exists()) {
        LOGGER.warn(image + " could not be found");
        return Response.status(Response.Status.NOT_FOUND).entity(new Error("No image found")).build();
      }

      if (file.delete()) {
        LOGGER.info("** Deleted " + image + " **");
        return Response.noContent().build();
      } else {
        LOGGER.warn("Failed to delete " + image);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
      }
    } catch (SecurityException e) {
      LOGGER.error("Unable to delete " + image);
      LOGGER.error(e.getMessage());
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
  }

  private class Error {
    @JsonProperty
    public final String message;

    public Error(String message) {
      this.message = message;
    }

    public Error() {
      message = null;
    }

    public String getMessage() {
      return message;
    }
  }

}
