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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.main_rv),
                        childAtPosition(
                                withId(R.id.activity_main_relativeLayout),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.recipe_detail_steps_rv),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                2)));
        recyclerView2.perform(actionOnItemAtPosition(5, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_pause), withContentDescription("Pause"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                5),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.exo_play), withContentDescription("Play"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.recipe_detail_steps_rv),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                2)));
        recyclerView3.perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.step_detail_next_imgbtn), withContentDescription("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_fragment_container),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1096);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.step_detail_next_imgbtn), withContentDescription("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_fragment_container),
                                        0),
                                4),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4957);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.action_bar),
                                        childAtPosition(
                                                withId(R.id.action_bar_container),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                allOf(withId(R.id.item_ingredient_tv), withText("6.0 UNIT egg whites"),
                        childAtPosition(
                                allOf(withId(R.id.item_ingredient_cl),
                                        childAtPosition(
                                                withId(R.id.recipe_detail_ingredientsItems_rv),
                                                8)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("6.0 UNIT egg whites")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.item_step_tv), withText("Combine dry ingredients."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_step_cv),
                                        0),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Combine dry ingredients.")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.item_step_tv), withText("Combine dry ingredients."),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.item_step_cv),
                                        0),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Combine dry ingredients.")));

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.recipe_detail_steps_rv),
                        childAtPosition(
                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                2)));
        recyclerView4.perform(actionOnItemAtPosition(2, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(4953);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.step_detail_info_tv), withText("Step 2 out of 15"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.step_fragment_container),
                                        0),
                                3),
                        isDisplayed()));
        textView4.check(matches(withText("Step 2 out of 15")));

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
