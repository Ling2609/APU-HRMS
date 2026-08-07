package controller.manager;

import entity.RoomType;
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
import session.ManagerFacade;

@WebServlet(name = "ManageRoomPrice", urlPatterns = {"/manager/ManageRoomPrice"})
public class ManageRoomPrice extends HttpServlet {

    @EJB
    private ManagerFacade managerFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        
        User user = (User) session.getAttribute("user");
        if (user.getRole() != User.Role.MANAGER) {
            response.sendRedirect(request.getContextPath() + "/common/login.jsp");
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
        
        ArrayList<RoomType> roomTypeList = managerFacade.findAllRoomTypes();
        
        request.setAttribute("roomTypeList", roomTypeList);
        request.getRequestDispatcher("/manager/manageRoomPrice.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        processRequest(request, response);
        
        try {
            
            double singleStandardPrice = Double.parseDouble(request.getParameter("SINGLE_STANDARD"));
            double twinStandardPrice = Double.parseDouble(request.getParameter("TWIN_STANDARD"));
            double doubleStandardPrice = Double.parseDouble(request.getParameter("DOUBLE_STANDARD"));
            double quadStandardPrice = Double.parseDouble(request.getParameter("QUAD_STANDARD"));
            double deluxeSuitePrice = Double.parseDouble(request.getParameter("DELUXE_SUITE"));
            double vipSuitePrice = Double.parseDouble(request.getParameter("VIP_SUITE"));
            
            if(singleStandardPrice <= 0 || twinStandardPrice <= 0 || doubleStandardPrice <= 0 || 
                    quadStandardPrice <= 0 || deluxeSuitePrice <= 0 || vipSuitePrice <= 0 ) {
                
                request.setAttribute("error", "Invalid price range.");
                request.getRequestDispatcher("/manager/manageRoomPrice.jsp").forward(request, response);
                return;
            
            }
            
            ArrayList<RoomType> updatedRoomTypes = new ArrayList<>();
            
            RoomType simpleStandard = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.SINGLE_STANDARD);
            simpleStandard.setRoomTypePrice(singleStandardPrice);
            updatedRoomTypes.add(simpleStandard);
            
            RoomType twinStandard = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.TWIN_STANDARD);
            twinStandard.setRoomTypePrice(twinStandardPrice);
            updatedRoomTypes.add(twinStandard);
            
            RoomType doubleStandard = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.DOUBLE_STANDARD);
            doubleStandard.setRoomTypePrice(doubleStandardPrice);
            updatedRoomTypes.add(doubleStandard);
            
            RoomType quadStandard = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.QUAD_STANDARD);
            quadStandard.setRoomTypePrice(quadStandardPrice);
            updatedRoomTypes.add(quadStandard);
            
            RoomType deluxeSuite = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.DELUXE_SUITE);
            deluxeSuite.setRoomTypePrice(deluxeSuitePrice);
            updatedRoomTypes.add(deluxeSuite);
            
            RoomType vipSuite = managerFacade.getRoomTypeByName(RoomType.RoomTypeName.VIP_SUITE);
            vipSuite.setRoomTypePrice(vipSuitePrice);
            updatedRoomTypes.add(vipSuite);
            
            managerFacade.updateRoomPrices(updatedRoomTypes);
            
            ArrayList<RoomType> roomTypeList = managerFacade.findAllRoomTypes();
            request.setAttribute("roomTypeList", roomTypeList);

            request.setAttribute("success", "Prices updated successfully.");
            doGet(request, response);
            
        }
        catch (Exception e) {
            
            request.setAttribute("error", "Invalid price value.");
            doGet(request, response);
            
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
