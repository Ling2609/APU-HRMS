package controller.common;

import entity.User;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Random;
import session.UserFacade;
import util.EmailUtil;

@WebServlet(name = "ForgotPassword", urlPatterns = {"/common/ForgotPassword"})
public class ForgotPassword extends HttpServlet {

    @EJB
    private UserFacade userFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/common/forgotPassword.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Please enter your registered email.");
            request.getRequestDispatcher("/common/forgotPassword.jsp").forward(request, response);
            return;
        }

        User user = userFacade.findByEmail(email.trim());

        if (user == null) {
            request.setAttribute("error", "No account was found with this email address.");
            request.getRequestDispatcher("/common/forgotPassword.jsp").forward(request, response);
            return;
        }

        String code = String.format("%06d", new Random().nextInt(1000000));

        HttpSession session = request.getSession();
        session.setAttribute("resetCode", code);
        session.setAttribute("resetEmail", user.getEmail());
        session.setAttribute("resetCodeExpiry",
                System.currentTimeMillis() + (10 * 60 * 1000));

        try {
            EmailUtil.sendVerificationCode(user.getEmail(), code);

            response.sendRedirect(
                    request.getContextPath() + "/common/VerifyResetCode"
            );

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Unable to send verification email. Please try again."
            );

            request.getRequestDispatcher(
                    "/common/forgotPassword.jsp"
            ).forward(request, response);
        }
    }
}