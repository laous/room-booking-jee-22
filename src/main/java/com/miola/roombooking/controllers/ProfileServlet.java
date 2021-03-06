package com.miola.roombooking.controllers;

import com.miola.roombooking.dao.AdminDao;
import com.miola.roombooking.dao.BookingDao;
import com.miola.roombooking.dao.ClientDao;
import com.miola.roombooking.dao.RoomDao;
import com.miola.roombooking.models.Admin;
import com.miola.roombooking.models.Booking;
import com.miola.roombooking.models.Client;
import com.miola.roombooking.models.Room;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.LinkedList;


@MultipartConfig
@WebServlet({"/profile", "/edit-profile"})

public class ProfileServlet extends HttpServlet{
    // This servlet is for crud operations on admins
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String Path = request.getServletPath();
        BookingDao bookingDao = new BookingDao();
        RoomDao roomDao= new RoomDao();
        ClientDao clientDao = new ClientDao();

        // set default return page
        String returnPath = "profile.jsp";


        if (Path.equalsIgnoreCase("/profile")) {
            // check if user is authenticated
            Client client =(Client) request.getSession().getAttribute("loggedIn");

            // if auth
            if(client == null){
                returnPath ="/main";
            }
            // if not
            else{
                // get all bookings
                LinkedList<Booking> bookings = bookingDao.getBookingsByCLientId(client.getClientId());

                // get all rooms
                LinkedList<Room> rooms = roomDao.getAllRooms();


                boolean bookingsExist = bookings != null;
                request.setAttribute("bookings" , bookings);
                request.setAttribute("rooms" , rooms);
                request.setAttribute("bookingsExist" , bookingsExist);

            }
            request.getRequestDispatcher(returnPath).forward(request, response);
        }
        // edit profile
        else if (Path.equalsIgnoreCase("/edit-profile")) {
            // check if user is authenticated
            Client client =(Client) request.getSession().getAttribute("loggedIn");

            // if not auth
            if(client == null){
                returnPath ="/main";
            }
            // if auth
            else{
                Client newClient = new Client();
                newClient.setClientId(client.getClientId());
                newClient.setEmail(client.getEmail());
                newClient.setPassword(client.getPassword());
                newClient.setAddress(request.getParameter("address"));
                newClient.setPhoneNumber(request.getParameter("phoneNumber"));
                newClient.setFirstName(request.getParameter("firstName"));
                newClient.setLastName(request.getParameter("lastName"));

                clientDao.updateClient(newClient);
                request.getSession().setAttribute("loggedIn" , newClient);
                returnPath="/profile";

            }



            request.getRequestDispatcher(returnPath).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }


}
