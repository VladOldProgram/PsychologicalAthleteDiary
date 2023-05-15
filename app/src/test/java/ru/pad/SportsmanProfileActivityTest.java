package ru.pad;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static org.junit.Assert.*;

public class SportsmanProfileActivityTest {
    @Rule
    public ActivityScenarioRule<SportsmanProfileActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SportsmanProfileActivity.class);

    @Test
    public void ActivitySwitchedToAuthorizationOnProfileExit() {
        ActivityScenario<SportsmanProfileActivity> activityScenario = activityScenarioRule.getScenario();
        ActivityScenario.ActivityAction<SportsmanProfileActivity> activityAction = activity -> {
            activity.buttonSportsmanProfileExit.performClick();
        };
        activityScenario.onActivity(activityAction);
    }
}