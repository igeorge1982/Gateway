package com.jeet.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeet.api.Devices;
import com.jeet.api.Logins;
import com.jeet.api.Movie;
import com.jeet.api.Tokens;
import com.jeet.service.BookingHandlerImpl;
import com.jeet.utils.AesUtil;
import com.jeet.utils.MyApplicationException;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

@Path("/")
public class BookController {
	
    private static final String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
    private static final String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
    private static volatile List<String> ciphertext;
    
    private static volatile String plaintext;
    private static final String ORIGINPLAINTEXT = "G";
    
    private static final String PASSPHRASE = "SecretPassphrase";
    private static final int KEYSIZE = 128;
    private static final int ITERATIONCOUNT = 1000;
    
    private static AesUtil aesUtil = new AesUtil(KEYSIZE, ITERATIONCOUNT);

	
	/*
	@PUT
	@Path("/book/{movieName}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response getMovieTicket(@PathParam(value = "movieName") String movieName) {
		
		Ticket ticket = new BookingHandlerImpl().bookTicket(movieName);
		
		if (ticket != null) {
			return Response.ok().entity(ticket).build();
		
		} else {
		
			return Response.status(404).build();
		}
	}*/
	
	@GET
	@Path("/book/{detail}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response movieGenre(@PathParam(value = "detail") String detail) {
		
		Movie movie = new BookingHandlerImpl().movieGenre(detail);
		
		if (movie != null) {
			return Response.ok().entity(movie).build();
		
		} else {
		
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/device/{uuid}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getDevice(@PathParam(value = "uuid") String uuid) throws MyApplicationException {
		
		Devices device = new BookingHandlerImpl().getDevice(uuid);
		
		if (uuid == null) {
            throw new MyApplicationException("uuid is not present in request !!");
		}
		
		if (device != null) {
			return Response.ok().status(200).entity(device).header("Device", device.getDevice()).build();
		
		}
		
		else {
			
			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.BAD_REQUEST);
			builder.entity("The requested resource could not be served.");
			Response response = builder.build();
			  
			throw new WebApplicationException(response);		}
	}
	
	@GET
	@Path("/user/{user}/{token1}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response getUser(@Context HttpHeaders headers, @PathParam(value = "user") String user, @PathParam(value = "token1") String token1) {
		
		Logins user_ = new BookingHandlerImpl().getUser(user);
		Tokens token = new BookingHandlerImpl().getToken(token1);
		
		for(String header : headers.getRequestHeaders().keySet()){
			System.out.println(header);
		}
		
		ciphertext = headers.getRequestHeaders().get("Ciphertext");
        plaintext = aesUtil.decrypt(SALT, IV, PASSPHRASE, ciphertext.toString());
		
        if (plaintext.equals(ORIGINPLAINTEXT)) {
        
			if (user_ != null && token != null) {
				
					return Response.ok().status(200).entity(user_).header("User", user_.getUuid()).build();		
				
				} else {
			
					return Response.status(404).build();
		
				}		
        
        } else {
			
        	return Response.status(403).build();
        	
        }
	}
	
	/*
	@DELETE
	@Path("/book/{ticketId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response cancelTicket(@PathParam(value = "ticketId") int ticketId) {
		
		new BookingHandlerImpl().deleteTicket(ticketId);
		
		return Response.ok().build();
	}*/
}
