package com.howky.mike.bakingapp;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void detailActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.main_rv),
                        childAtPosition(
                                withId(R.id.activity_main_relativeLayout),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_ingredient_tv), withText("1.0 K Nutella or other chocolate-hazelnut spread"),
                        childAtPosition(
                                allOf(withId(R.id.item_ingredient_cl),
                                        childAtPosition(
                                                withId(R.id.recipe_detail_ingredientsItems_rv),
                                                5)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("1.0 K Nutella or other chocolate-hazelnut spread")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.item_step_tv), withText("Starting prep"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_step_cv),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Starting prep")));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_detail_steps_rv),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                2)));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

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
