
package com.example.android.justjavatea;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private int mQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This is triggered by clicking order button.
     *
     * @param view view something
     */
    public void submitOrder(View view) {
        final int mDuration = 200;
        EditText editText = (EditText) findViewById(R.id.edit_name_text);
        CheckBox whipCreamCheck = (CheckBox) findViewById(R.id.whip_cream_check);
        CheckBox chocolateCheck = (CheckBox) findViewById(R.id.chocolate_check);
        displayPrice(calculatePrice(mQuantity >= 0 ? mQuantity : 0, 5,
                        whipCreamCheck.isChecked(), chocolateCheck.isChecked()),
                whipCreamCheck.isChecked(), chocolateCheck.isChecked(), editText.getText()
                        .toString());
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(mDuration);
        }
    }

    /**
     * This is to displays the given quantity value on the screen.
     *
     * @param number number of something
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(number));
    }

    /**
     * This is to displays the given price value on the screen.
     *
     * @param number           number of something
     * @param whipCreamChecked check box
     * @param chocolateChecked check box
     * @param name             of something
     */
    private void displayPrice(int number, boolean whipCreamChecked, boolean chocolateChecked,
                              String name) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(buildOrderSummary(number, whipCreamChecked, chocolateChecked, name));
    }

    /**
     * This is to build the given price order summary.
     *
     * @param number           number of something
     * @param whipCreamChecked check box
     * @param chocolateChecked check box
     * @param name             of something
     */
    private String buildOrderSummary(int number, boolean whipCreamChecked,
                                     boolean chocolateChecked, String name) {
        StringBuilder sb = new StringBuilder().append(getString(R.string.order_summary_name, name))
                .append("\n").append(getString(R.string.order_summary_whipped_cream, whipCreamChecked))
                .append("\n").append(getString(R.string.order_summary_chocolate, chocolateChecked))
                .append("\n").append(getString(R.string.order_summary_quantity, mQuantity))
                .append("\n").append(getString(R.string.order_summary_price,
                        NumberFormat.getCurrencyInstance().format(number)));
        if (mQuantity > 0) {
            sb.append("\n").append(getString(R.string.thanks));
        }
        return sb.toString();
    }

    /**
     * Increment quantity.
     *
     * @param view something
     */
    public void increment(View view) {
        if (100 > mQuantity) {
            mQuantity = mQuantity + 1;
            displayQuantity(mQuantity);
        } else {
            Toast.makeText(this, getString(R.string.order_summary_limit, mQuantity),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Decrement quantity.
     *
     * @param view something
     */
    public void decrement(View view) {
        mQuantity = mQuantity > 0 ? mQuantity - 1 : 0;
        displayQuantity(mQuantity);
    }

    /**
     * This method displays the given text on the screen.
     *
     * @param message message of something
     */
    @SuppressWarnings("unused")
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity         is the number of cups of coffee ordered
     * @param pricePerCup      price per cup
     * @param whipCreamChecked check box
     * @param chocolateChecked check box
     */
    private int calculatePrice(int quantity, int pricePerCup, boolean whipCreamChecked,
                               boolean chocolateChecked) {
        return quantity * (pricePerCup + (whipCreamChecked ? 1 : 0) + (chocolateChecked ? 2 : 0));
    }

    /**
     * This is triggered by clicking send button.
     *
     * @param view view something
     */
    public void sendOrder(View view) {
        final int mDuration = 200;
        EditText editText = (EditText) findViewById(R.id.edit_name_text);
        CheckBox whipCreamCheck = (CheckBox) findViewById(R.id.whip_cream_check);
        CheckBox chocolateCheck = (CheckBox) findViewById(R.id.chocolate_check);
        String emailBody = buildOrderSummary(calculatePrice(mQuantity >= 0 ? mQuantity : 0, 5,
                        whipCreamCheck.isChecked(), chocolateCheck.isChecked()),
                whipCreamCheck.isChecked(), chocolateCheck.isChecked(), editText.getText()
                        .toString());
        composeEmail(emailBody, editText.getText().toString());
    }

    /**
     * Send email.
     *
     * @param emailBody email text
     * @param name      name of something
     */
    private void composeEmail(String emailBody, String name) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        // intent.setData(Uri.parse("mailto:"));
        // intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_line, name));
        intent.putExtra(Intent.EXTRA_TEXT, emailBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.i(LOG_TAG, "No email client is found " + e.getMessage());
            }
        }
    }

    /**
     * @param item menu item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_motion_shake) {
            Intent intent = new Intent(this, MotionActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
