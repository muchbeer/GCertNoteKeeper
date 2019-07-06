package muchbeer.raum.com.gcertnotekeeper;

import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;

import java.util.List;

import muchbeer.raum.com.gcertnotekeeper.data.DataManager;
import muchbeer.raum.com.gcertnotekeeper.data.NoteInfo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class NextThroughNoteTest {

    @Rule
    public ActivityTestRule<NavigatActivity> mActivityTestRule =
            new ActivityTestRule(NavigatActivity.class);


    @Test
    public void NextThroughNotes() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_note));

        onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
     //   int index = 0;
       for(int index = 0; index < notes.size(); index++) {
           NoteInfo note = notes.get(index);

           onView(withId(R.id.spinner_courses)).check(
                   matches(withSpinnerText(note.getCourse().getTitle())));
           onView(withId(R.id.text_note_title)).check(matches(withText(note.getTitle())));
           onView(withId(R.id.text_note_text)).check(matches(withText(note.getText())));

           if(index < notes.size() - 1)
               onView(allOf(withId(R.id.menu_next), isEnabled())).perform(click());
       }
        onView(withId(R.id.menu_next)).check(matches(not(isEnabled())));
        pressBack();
    }

}