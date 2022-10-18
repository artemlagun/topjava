package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
    MealRestController mealRestController = appCtx.getBean(MealRestController.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("filter".equals(action)) {
            log.info("Filter");

            String reqStartDate = request.getParameter("startDate");
            String reqEndDate = request.getParameter("endDate");
            String reqStartTime = request.getParameter("startTime");
            String reqEndTime = request.getParameter("endTime");

            LocalDate startDate = reqStartDate.isEmpty() ? null : LocalDate.parse(reqStartDate);
            LocalDate endDate = reqEndDate.isEmpty() ? null : LocalDate.parse(reqEndDate);
            LocalTime startTime = reqStartTime.isEmpty() ? null : LocalTime.parse(reqStartTime);
            LocalTime endTime = reqEndTime.isEmpty() ? null : LocalTime.parse(reqEndTime);

            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("startTime", startTime);
            request.setAttribute("endTime", endTime);
            request.setAttribute("meals", mealRestController.getAll(startDate, startTime, endDate, endTime));

            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id), SecurityUtil.authUserId(),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                mealRestController.create(meal);
                response.sendRedirect("meals");
            } else {
                mealRestController.update(meal, meal.getId());
                response.sendRedirect("meals");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(SecurityUtil.authUserId(), LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }
}
