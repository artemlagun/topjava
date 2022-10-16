package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) throws NotFoundException {
        log.info("create {} with userId={}", meal, meal.getUserId());
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) throws NotFoundException {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) throws NotFoundException {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) throws NotFoundException {
        log.info("update {} with userId={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }
}