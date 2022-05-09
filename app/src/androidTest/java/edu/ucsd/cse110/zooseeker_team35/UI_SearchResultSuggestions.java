package edu.ucsd.cse110.zooseeker_team35;


import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UI_SearchResultSuggestions {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void partialStringSearch() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("mm"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Arctic Foxes")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(1, hasDescendant(withText("Elephant Odyssey")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(2, hasDescendant(withText("Gorillas")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(3, hasDescendant(withText("Lions")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(4)));
    }

    @Test
    public void tagSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("monkey"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Gorillas")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(1)));
    }

    @Test
    public void specificSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Lions"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(atPosition(0, hasDescendant(withText("Lions")))));

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(1)));
    }

    @Test
    public void invalidSearch(){
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_text),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("godzilla"), closeSoftKeyboard());

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.search_item_recycler))
                .check(matches(recyclerViewSize(0)));
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

    /*
    https://stackoverflow.com/questions/63562046/how-to-check-if-recyclerview-is-empty-espresso
    Matcher that checks that the recyclerView has a given number of elements
     */
    private static Matcher<View> recyclerViewSize(int size){
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description){
                description.appendText("with recycler size: " + size);
            }

            @Override
            public boolean matchesSafely(final RecyclerView view){
                return size == view.getChildCount();
            }
        };

    }

    /*
    https://stackoverflow.com/questions/31394569/how-to-assert-inside-a-recyclerview-in-espresso
    Matcher that checks that the recyclerView has a matching item at a given position
    * */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}
