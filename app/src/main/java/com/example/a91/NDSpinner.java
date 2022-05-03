

package com.example.a91;

import android.content.Context;
import android.util.AttributeSet;


/** Spinner extension that calls onItemSelected even when the selection is the same as its previous value */

//Adapted from https://stackoverflow.com/questions/5335306/how-can-i-get-an-event-in-android-spinner-when-the-current-selected-item-is-sele
//Makes spinner update on same item selected


public class NDSpinner extends androidx.appcompat.widget.AppCompatSpinner {

    public NDSpinner(Context context)
    { super(context); }

    public NDSpinner(Context context, AttributeSet attrs)
    { super(context, attrs); }

    public NDSpinner(Context context, AttributeSet attrs, int defStyle)
    { super(context, attrs, defStyle); }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (position == 0){

        }
        else if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (position == 0){

        }
        else if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

}