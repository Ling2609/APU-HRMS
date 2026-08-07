package controller.housekeeper;

import entity.Booking;
import entity.Housekeeper;
import entity.Message;
import entity.Staff;
import entity.User;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import session.HousekeeperFacade;

@WebServlet(name = "CompleteTask", urlPatterns = {"/housekeeper/CompleteTask"})
public class CompleteTask extends HttpServlet {

    @EJB
    private HousekeeperFacade housekeeperFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.HOUSEKEEPER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
            return;
        }
        
        Housekeeper housekeeper = Housekeeper.isHouseKeeper(Staff.isStaff(user));
        housekeeperFacade.completeTask(request.getParameter("bookingID"));
        
        ArrayList<Booking> bookingList = housekeeperFacade.getAllHousekeeperBookings(housekeeper);
        ArrayList<Message> feedbackList = housekeeperFacade.getAllHousekeeperFeedback(housekeeper);
        
        request.setAttribute("bookingList", bookingList);
        request.setAttribute("feedbackList", feedbackList);
        request.getRequestDispatcher("/housekeeper/manageTask.jsp").forward(request, response);

        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
