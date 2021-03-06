package cl.figonzal.lastquakechile.views.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import cl.figonzal.lastquakechile.R;
import cl.figonzal.lastquakechile.views.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class ViewPagerTest {

	@Rule
	public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

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

	private Context mContext;

	@Before
	public void setup() {
		mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
	}

	@Test
	public void test1_click_tab_map() {
		//Checkear el texto mapa
		ViewInteraction tabView = onView(
				allOf(withContentDescription(mContext.getString(R.string.tab_map)),
						childAtPosition(
								childAtPosition(
										withId(R.id.tabs),
										0),
								1),
						isDisplayed()));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		tabView.perform(click());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2_click_tab_listado() {
		//Checkear texto Listado
		ViewInteraction tabView2 = onView(
				allOf(withContentDescription(mContext.getString(R.string.tab_list)),
						childAtPosition(
								childAtPosition(
										withId(R.id.tabs),
										0),
								0),
						isDisplayed()));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		tabView2.perform(click());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
