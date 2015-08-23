package com.jeet.rest;

import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.jeet.api.Devices;
import com.jeet.api.Logins;
import com.jeet.api.Movie;
import com.jeet.api.Tokens;
import com.jeet.service.BookingHandlerImpl;

@Path("/")
public class BookController {
	
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
	
	public Response getDevice(@PathParam(value = "uuid") String uuid) {
		
		Devices device = new BookingHandlerImpl().getDevice(uuid);
		
		if (device != null) {
			return Response.ok().status(200).entity(device).header("Device", device.getDevice()).build();
		
		} else {
		
			return Response.status(404).build();
		}
	}
	
	@GET
	@Path("/user/{user}/{token1}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	
	public Response getUser(@PathParam(value = "user") String user, @PathParam(value = "token1") String token1) {
		
		Logins user_ = new BookingHandlerImpl().getUser(user);
		Tokens token = new BookingHandlerImpl().getToken(token1);
		
		if (user_ != null && token != null) {
			return Response.ok().status(200).entity(user_).header("User", user_.getUuid()).build();
		
		} else {
		
			return Response.status(404).build();
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
