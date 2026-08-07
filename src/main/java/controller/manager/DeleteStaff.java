package controller.manager;

import entity.Staff;
import entity.User;
import jakarta.ejb.EJB;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import session.ManagerFacade;

@WebServlet(name = "DeleteStaff", urlPatterns = {"/manager/DeleteStaff"})
public class DeleteStaff extends HttpServlet {
    
    @EJB
    private ManagerFacade managerFacade;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
               
        String action = request.getParameter("action");
        String staffID = request.getParameter("staffID");
        
        if(!"delete".equals(action)) {
            request.setAttribute("error", "Invalid action.");
            request.getRequestDispatcher("/manager/ManageStaff").forward(request, response);
            return;
        }
        
        Staff staff = (Staff)managerFacade.getStaffByID(staffID);
        User user = new User(staff);
        
        managerFacade.remove(user);
        
        request.setAttribute("success", "Staff deleted successfully.");
        request.getRequestDispatcher("/manager/ManageStaff").forward(request, response);
        
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
