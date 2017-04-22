package com.yydcdut.rxmarkdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pratamalabs.emoji.EmojiPopup;
import com.pratamalabs.emoji.emoji.Emoji;
import com.pratamalabs.emoji.listeners.OnEmojiBackspaceClickListener;
import com.pratamalabs.emoji.listeners.OnEmojiClickedListener;
import com.pratamalabs.emoji.listeners.OnEmojiPopupDismissListener;
import com.pratamalabs.emoji.listeners.OnEmojiPopupShownListener;
import com.pratamalabs.emoji.listeners.OnSoftKeyboardCloseListener;
import com.pratamalabs.emoji.listeners.OnSoftKeyboardOpenListener;
import com.yydcdut.rxmarkdown.factory.EditFactory;
import com.yydcdut.rxmarkdown.view.HorizontalEditScrollView;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuyidong on 16/7/23.
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String RESULT = "MARKDOWN_RESULT";
    public static final String TEXT = "MARKDOWN_TEXT";
    public static final String PLACEHOLDER = "MARKDOWN_PLACEHOLDER";
    public static final String TITLE = "MARKDOWN_TITLE";
    private RxMDEditText mEditText;
    private EmojiPopup emojiPopup;
    private Observable<CharSequence> mObservable;
    private Subscription mSubscription;
    private HorizontalEditScrollView mHorizontalEditScrollView;
    private ViewGroup rootView;
    private ImageView emojiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String text = getIntent().getStringExtra(TEXT);

        if (text == null) {
            text = "Edit";
        }

        String title = getIntent().getStringExtra(TITLE);
        if (title == null) {
            title = "Edit";
        }
        toolbar.setTitle(title);
        setTitle(title);

        mEditText = (RxMDEditText) findViewById(R.id.edit_md);
        mHorizontalEditScrollView = (HorizontalEditScrollView) findViewById(R.id.scroll_edit);
        RxMDConfiguration rxMDConfiguration = new RxMDConfiguration.Builder(this)
                .setDefaultImageSize(50, 50)
                .setBlockQuotesColor(0xff33b5e5)
                .setHeader1RelativeSize(2.2f)
                .setHeader2RelativeSize(2.0f)
                .setHeader3RelativeSize(1.8f)
                .setHeader4RelativeSize(1.6f)
                .setHeader5RelativeSize(1.4f)
                .setHeader6RelativeSize(1.2f)
                .setHorizontalRulesColor(0xff99cc00)
                .setInlineCodeBgColor(0xffff4444)
                .setCodeBgColor(0x33999999)
                .setTodoColor(0xffaa66cc)
                .setTodoDoneColor(0xffff8800)
                .setUnOrderListColor(0xff00ddff)
                .build();
        mHorizontalEditScrollView.setEditTextAndConfig(mEditText, rxMDConfiguration);
        mEditText.setText(text);
        mObservable = RxMarkdown.live(mEditText)
                .config(rxMDConfiguration)
                .factory(EditFactory.create())
                .intoObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
        final long time = System.currentTimeMillis();
        mSubscription = mObservable
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        Snackbar.make(mFloatingActionButton, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
//                        Snackbar.make(mFloatingActionButton, (System.currentTimeMillis() - time) + "", Snackbar.LENGTH_SHORT).show();
                    }
                });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int after) {
//                Log.i("yuyidong", "beforeTextChanged  start-->" + start + "  before-->" + before + "  after-->" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int after) {
//                Log.i("yuyidong", "onTextChanged  start-->" + start + "  before-->" + before + "  after-->" + after);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rootView = (ViewGroup) findViewById(R.id.main_activity_root_view);

        setUpEmojiPopup();
    }

    private void setUpEmojiPopup() {
        emojiButton = (ImageView) mHorizontalEditScrollView.findViewById(R.id.img_emoji);
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emojiPopup.toggle();
            }
        });
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView).setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
            @Override
            public void onEmojiBackspaceClicked(final View v) {
                Log.d("MainActivity", "Clicked on Backspace");
            }
        }).setOnEmojiClickedListener(new OnEmojiClickedListener() {
            @Override
            public void onEmojiClicked(final Emoji emoji) {
                Log.d("MainActivity", "Clicked on emoji");
            }
        }).setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
            @Override
            public void onEmojiPopupShown() {
                emojiButton.setImageResource(R.drawable.ic_keyboard_grey_500_36dp);
            }
        }).setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
            @Override
            public void onKeyboardOpen(final int keyBoardHeight) {
                Log.d("MainActivity", "Opened soft keyboard");
            }
        }).setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
            @Override
            public void onEmojiPopupDismiss() {
                emojiButton.setImageResource(R.drawable.emoji_people);
            }
        }).setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
            @Override
            public void onKeyboardClose() {
                emojiPopup.dismiss();
            }
        }).build(mEditText);
    }

    @Override
    public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Intent intent = this.getIntent();
            intent.putExtra(mEditText.getText().toString(), RESULT);
            this.setResult(RESULT_OK, intent);
            this.finish();
            return true;
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_enable) {
//            final long time = System.currentTimeMillis();
//            mSubscription = mObservable
//                    .subscribe(new Subscriber<CharSequence>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
////                            Snackbar.make(mFloatingActionButton, e.getMessage(), Snackbar.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onNext(CharSequence charSequence) {
////                            Snackbar.make(mFloatingActionButton, (System.currentTimeMillis() - time) + "", Snackbar.LENGTH_SHORT).show();
//                        }
//                    });
//            return true;
//        } else if (id == R.id.action_disable) {
//            if (mSubscription != null) {
//                mSubscription.unsubscribe();
//                mSubscription = null;
//                mEditText.clear();
//            }
//            return true;
//        } else
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
//        ShowActivity.startShowActivity(this, mEditText.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
