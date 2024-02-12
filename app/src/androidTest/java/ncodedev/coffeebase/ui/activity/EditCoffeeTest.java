//package ncodedev.coffeebase.ui.activity;
//
//import androidx.test.ext.junit.rules.ActivityScenarioRule;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//import ncodedev.coffeebase.R;
//import ncodedev.coffeebase.model.security.User;
//import ncodedev.coffeebase.web.provider.CoffeeApiProvider;
//import okhttp3.MultipartBody;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.action.ViewActions.*;
//import static androidx.test.espresso.matcher.ViewMatchers.withId;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@RunWith(AndroidJUnit4.class)
//public class EditCoffeeTest {
//
//    static {
//        new User("testUserId", "testAccount", "testEmail", "testPhotoUrl", "testToken");
//    }
//
//    @Mock
//    private CoffeeApiProvider coffeeApiProvider;
//
//    @Rule
//    public ActivityScenarioRule<EditCoffee> activityRule = new ActivityScenarioRule<>(EditCoffee.class);
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
////        new User("testUserId", "testAccount", "testEmail", "testPhotoUrl", "testToken");
//    }
//
//    @Test
//    public void testNameInputAndSaveAction() {
//        onView(withId(R.id.inputCoffeeName))
//                .perform(typeText("Coffee name"), closeSoftKeyboard());
//        onView(withId(R.id.saveBtn)).perform(click());
//
//        verify(coffeeApiProvider, times(1)).save(any(), any(MultipartBody.Part.class), any());
//    }
//}