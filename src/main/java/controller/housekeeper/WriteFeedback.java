package controller.housekeeper;

import entity.Booking;
import entity.BookingUser;
import entity.Message;
import entity.User;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import session.BookingFacade;
import session.BookingUserFacade;
import session.MessageFacade;

@WebServlet(name = "WriteFeedback", urlPatterns = {"/housekeeper/WriteFeedback"})
public class WriteFeedback extends HttpServlet {
    
    @EJB
    private BookingFacade bookingFacade;
    @EJB
    private MessageFacade messageFacade;
    @EJB
    private BookingUserFacade bookingUserFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
        
        HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.HOUSEKEEPER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        }
        
        Booking selectedBooking = bookingFacade.find(Long.valueOf(request.getParameter("bookingID")));
        
        request.setAttribute("selectedBooking", selectedBooking);
        request.getRequestDispatcher("/housekeeper/writeFeedback.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
                
        String feedback = request.getParameter("feedback").trim();
        
        if(feedback.isEmpty()) {
            request.setAttribute("error", "No feedback given.");
            request.getRequestDispatcher("/housekeeper/writeFeedback.jsp").forward(request, response);
            return;
        }
        
        try {
            BookingUser bookingUser = bookingUserFacade.findByBookingAndRole(
                    Long.valueOf(request.getParameter("bookingId")), BookingUser.BookingUserRole.HOUSEKEEPER);

            Message message = new Message(bookingUser, Message.MessageType.FEEDBACK, feedback, null);
            messageFacade.create(message);
            
            request.setAttribute("success", "Feedback left successfully.");
            request.getRequestDispatcher("/housekeeper/ManageTask").forward(request, response);
            
        }
        catch(Exception e) {
            request.setAttribute("error", "Cannot generate feedback given.");
            request.getRequestDispatcher("/housekeeper/ManageTask").forward(request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
