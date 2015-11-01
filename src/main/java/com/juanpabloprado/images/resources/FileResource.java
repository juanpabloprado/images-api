package com.juanpabloprado.images.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Juan on 25/02/2015.
 */
@Path("/v1/files")
@Produces(MediaType.APPLICATION_JSON)
public class FileResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileResource.class);
  private final String dest;

  public FileResource(String dest) {
    this.dest = dest;
  }

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(
      @FormDataParam("file") InputStream uploadedInputStream,
      @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {
    String uploadedFileLocation = dest + fileDetail.getFileName();
    LOGGER.info(uploadedFileLocation);
    // save it
    writeToFile(uploadedInputStream, uploadedFileLocation);
    String output = "File uploaded to : " + uploadedFileLocation;
    return Response.ok(output).build();
  }

  // save uploaded file to new location
  private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
    int read;
    final int BUFFER_LENGTH = 1024;
    final byte[] buffer = new byte[BUFFER_LENGTH];
    OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
    while ((read = uploadedInputStream.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
    out.flush();
    out.close();
  }
}
