package com.quoctrungdhqn.shiportalandroid.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.bluelinelabs.conductor.Controller;

public abstract class BaseController extends Controller {
    protected BaseController() {
    }

    protected BaseController(Bundle args) {
        super(args);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Set the default behavior to be back navigation.
                getRouter().handleBack();
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
    }
}
