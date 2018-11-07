package edu.temple.webbrowser;

import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CustomPagerAdapter mAdapter;
    ViewPager mPager;
    EditText mUrlEntry;
    Button mGoButton;
    Button mForwardButton;
    Button mBackButton;
    Button mClearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOnClickListeners();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(myToolbar);

        mUrlEntry = findViewById(R.id.url_edittext);
        mUrlEntry.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    mGoButton.callOnClick();
                }
                return false;
            }
        });
        WebPageFragment temp = new WebPageFragment();
        FragmentList.FRAGMENTS.add(temp);

        mAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewPager);
        mPager.addOnPageChangeListener(new CustomListener());
        mPager.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    String verifyUrl(String url) {
        StringBuilder ret = new StringBuilder(url);
        if (ret.indexOf("www") == -1) {
            ret.insert(0, "www.");
        }
        if (ret.indexOf("http") == -1) {
            ret.insert(0, "http://");
        }
        return ret.toString();
    }


    void setOnClickListeners() {
        mGoButton = findViewById(R.id.go_button);
        mGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebPageFragment current = FragmentList.FRAGMENTS.get(mPager.getCurrentItem());
                WebView wv = current.mWebView;
                String destination = verifyUrl(mUrlEntry.getText().toString());
                wv.loadUrl(destination);
            }
        });

        mForwardButton = findViewById(R.id.navigate_forward_button);
        mForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebPageFragment current = FragmentList.FRAGMENTS.get(mPager.getCurrentItem());
                current.mWebView.goForward();
            }
        });

        mBackButton = findViewById(R.id.navigate_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebPageFragment current = FragmentList.FRAGMENTS.get(mPager.getCurrentItem());
                current.mWebView.goBack();
            }
        });

        mClearButton = findViewById(R.id.clear);
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUrlEntry.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int currentIndex = mPager.getCurrentItem();
        String message;
        switch (item.getItemId()) {

            case R.id.action_back:
                if (currentIndex != 0)
                    currentIndex--;
                mPager.setCurrentItem(currentIndex);
                message = String.format("Tab %d of %d", currentIndex+1, mPager.getAdapter().getCount());
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_next:
                if (currentIndex != mPager.getAdapter().getCount()-1)
                    currentIndex++;
                mPager.setCurrentItem(currentIndex);
                message = String.format("Tab %d of %d", currentIndex+1, mPager.getAdapter().getCount());
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_add:
                FragmentList.FRAGMENTS.add(new WebPageFragment());
                mPager.getAdapter().notifyDataSetChanged();
                Toast.makeText(this, "New tab created", Toast.LENGTH_SHORT).show();
                mPager.setCurrentItem(mPager.getAdapter().getCount());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



    @Override
    public void onBackPressed() {
        mBackButton.callOnClick();
    }

    public class CustomListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            mUrlEntry.setText(FragmentList.FRAGMENTS.get(i).mWebView.getUrl());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
