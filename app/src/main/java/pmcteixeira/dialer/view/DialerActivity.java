package pmcteixeira.dialer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import pmcteixeira.dialer.DialerContract;
import pmcteixeira.dialer.R;
import pmcteixeira.dialer.presenter.DialerPresenter;

public class DialerActivity extends AppCompatActivity implements DialerContract.View {

    private final static int CALL_PHONE_PERMISSIONS_REQUEST = 1;

    @BindView(R.id.tv_dialed_number)
    TextView dialedNumberTextView;

    @BindView(R.id.main_content)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.fab_call)
    FloatingActionButton callFloatingActionButton;

    @BindView(R.id.ib_delete_dialed_number)
    ImageButton deleteDialedNumberImageButton;

    @Nullable
    private BottomSheetBehavior<View> bottomSheetBehavior;

    @Nullable
    private DialerContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        ButterKnife.bind(this);
        initBottomSheet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onStop();
        }
    }

    @Override
    public void renderDialedNumber(@NonNull String dialedNumber) {
        dialedNumberTextView.setText(dialedNumber);
    }

    @Override
    public void showDialer(boolean show) {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(show ? BottomSheetBehavior.STATE_EXPANDED :
                    BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void callDialedNumber(@NonNull String dialedNumber) {
        if (isCallPhonePermission()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + dialedNumber));
            //noinspection MissingPermission
            startActivity(intent);
        }
    }

    @Override
    public void showDeleteButton(boolean show) {
        deleteDialedNumberImageButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableCallButton(boolean enable) {
        if (enable) {
            int enabled = ContextCompat.getColor(this, R.color.colorAccent);
            callFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(enabled));
            callFloatingActionButton.setImageResource(R.drawable.ic_phone_white_48dp);
        } else {
            int disabledColor = ContextCompat.getColor(this, R.color.light_gray);
            callFloatingActionButton.setBackgroundTintList(ColorStateList.valueOf(disabledColor));
            callFloatingActionButton.setImageResource(R.drawable.ic_close_white_48dp);
        }
    }

    @Override
    public void enableDialerCollapse(boolean enable) {
        int peekHeight = enable ? (int) getResources().getDimension(R.dimen.dialed_number_height) : 0;
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setPeekHeight(peekHeight);
        }
    }

    private boolean isCallPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PHONE_PERMISSIONS_REQUEST);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_PHONE_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (presenter != null) {
                        presenter.onCall();
                    }
                } else {
                    // permission denied
                }
            }
        }
    }

    @OnClick(R.id.tv_dialed_number)
    void onDialedNumberClick() {
        if (presenter != null) {
            presenter.onDialedNumberClick();
        }
    }

    @OnClick(R.id.fab_dialer)
    void onDialerClick() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @OnClick(R.id.ib_delete_dialed_number)
    void onDeleteDialedNumberClick() {
        if (presenter != null) {
            presenter.onRemoveLastDialNumber();
        }
    }

    @OnLongClick(R.id.ib_delete_dialed_number)
    boolean onRemoveDeleteDialedNumberClick() {
        if (presenter != null) {
            presenter.onClearDialNumber();
            return true;
        }
        return false;
    }

    @OnClick(R.id.fab_call)
    void onCall() {
        if (presenter != null) {
            presenter.onCall();
        }
    }

    @OnClick(R.id.tv_0)
    void onDialClick0() {
        if (presenter != null) {
            presenter.onDialNumberClick("0");
        }
    }

    @OnClick(R.id.tv_1)
    void onDialClick1() {
        if (presenter != null) {
            presenter.onDialNumberClick("1");
        }
    }

    @OnClick(R.id.tv_2)
    void onDialClick2() {
        if (presenter != null) {
            presenter.onDialNumberClick("2");
        }
    }

    @OnClick(R.id.tv_3)
    void onDialClick3() {
        if (presenter != null) {
            presenter.onDialNumberClick("3");
        }
    }

    @OnClick(R.id.tv_4)
    void onDialClick4() {
        if (presenter != null) {
            presenter.onDialNumberClick("4");
        }
    }

    @OnClick(R.id.tv_5)
    void onDialClick5() {
        if (presenter != null) {
            presenter.onDialNumberClick("5");
        }
    }

    @OnClick(R.id.tv_6)
    void onDialClick6() {
        if (presenter != null) {
            presenter.onDialNumberClick("6");
        }
    }

    @OnClick(R.id.tv_7)
    void onDialClick7() {
        if (presenter != null) {
            presenter.onDialNumberClick("7");
        }
    }

    @OnClick(R.id.tv_8)
    void onDialClick8() {
        if (presenter != null) {
            presenter.onDialNumberClick("8");
        }
    }

    @OnClick(R.id.tv_9)
    void onDialClick9() {
        if (presenter != null) {
            presenter.onDialNumberClick("9");
        }
    }

    @OnClick(R.id.tv_star)
    void onDialClickStar() {
        if (presenter != null) {
            presenter.onDialNumberClick("*");
        }
    }

    @OnClick(R.id.tv_hash)
    void onDialClickHash() {
        if (presenter != null) {
            presenter.onDialNumberClick("#");
        }
    }

    private void initPresenter() {
        presenter = new DialerPresenter();
        presenter.onStart(this);
    }

    private void initBottomSheet() {
        View bottomSheet = coordinatorLayout.findViewById(R.id.dialer_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (presenter != null) {
                            presenter.onDialerCollapsed();
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        // no-op
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (presenter != null) {
                            presenter.onDialerExpanded();
                        }
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        if (presenter != null) {
                            presenter.onDialerHidden();
                        }
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        // no-op
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // no-op
            }
        });
    }
}