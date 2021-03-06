package com.aspenshore.relish.espresso;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import com.aspenshore.relish.core.Component;
import org.junit.Assert;

import static com.aspenshore.relish.core.TestUtils.attempt;
import static com.aspenshore.relish.espresso.AndroidUtils.getCurrentActivity;

public class Screen extends Component {
    private Class<? extends Activity> activity;

    public Screen(Class<? extends Activity> activity) {
        super(null);
        this.activity = activity;
    }

    @Override
    public void assertVisible() {
        checkCurrentActivity(activity);
    }

    @Override
    public String getStringValue() {
        throw new IllegalStateException("Cannot get string value for " + this);
    }

    public void pressBack() {
        Espresso.pressBack();
    }


    private void checkCurrentActivity(final Class<? extends Activity> activityClass) {
        attempt(new Runnable() {
            @Override
            public void run() {
                Class<? extends Activity> aClass = getCurrentActivity().getClass();
                Assert.assertEquals((Class) activityClass, aClass);
            }
        }, 50, 200);
    }
}
