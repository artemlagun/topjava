package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import ru.javawebinar.topjava.Profiles;

@Profile(Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
}
