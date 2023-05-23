package ru.pad;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class PsychologistAssignZeroTestYellowMarkNotExistsInSelectedDayViewTest {

    @Rule
    public ActivityScenarioRule<AuthorizationActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(AuthorizationActivity.class);

    @Test
    public void psychologistAssignZeroTestYellowMarkNotExistsInSelectedDayViewTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editTextEmail),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputLayoutEmail),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("testPsy@mail.ru"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editTextPassword),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.inputLayoutPassword),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("123456d"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonAuthorization), withText("�����"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.constraintLayoutActivityAuthorization),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonSportsmanNameSurnameEmail), withText("Vlad Shkut (testSpo@mail.ru)"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                0)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.buttonSportsmanCalendar), withText("���������"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerViewCalendar),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)));
        recyclerView.perform(actionOnItemAtPosition(21, click()));

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.buttonAssignTest), withText("��������� ����"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.buttonAssignTests), withText("���������"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton5.perform(click());

        pressBack();

        ViewInteraction textView = onView(
                allOf(withId(R.id.textViewAvailableTests), withText("��������� ��� ����������� �����"),
                        withParent(allOf(withId(R.id.linearLayoutAvailableTests),
                                withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView.check(doesNotExist());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
