package pmcteixeira.dialer.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import pmcteixeira.dialer.DialerContract;

public class DialerPresenter implements DialerContract.Presenter {

    /**
     * The dialer is expanded.
     */
    private static final int STATE_EXPANDED = 1;

    /**
     * The dialer is collapsed.
     */
    private static final int STATE_COLLAPSED = 2;

    /**
     * The dialer is hidden.
     */
    private static final int STATE_HIDDEN = 3;

    @Nullable
    private DialerContract.View view;

    @NonNull
    private StringBuilder dialedNumberStringBuilder;

    private int dialerState;

    public DialerPresenter() {
        dialedNumberStringBuilder = new StringBuilder();
    }

    @Override
    public void onStart(@NonNull DialerContract.View view) {
        this.view = view;
        renderState();
    }

    @Override
    public void onStop() {
        this.view = null;
    }

    @Override
    public void onDialNumberClick(@NonNull String dialNumber) {
        dialedNumberStringBuilder.append(dialNumber);
        if (view != null) {
            renderState();
        }
    }

    @Override
    public void onRemoveLastDialNumber() {
        if (view != null) {
            if (dialedNumberStringBuilder.length() > 0) {
                dialedNumberStringBuilder.deleteCharAt(dialedNumberStringBuilder.length() - 1);
                renderState();
            }
        }
    }

    @Override
    public void onClearDialNumber() {
        if (view != null) {
            if (dialedNumberStringBuilder.length() > 0) {
                dialedNumberStringBuilder = new StringBuilder();
                renderState();
            }
        }
    }

    @Override
    public void onCall() {
        if (view != null) {
            if (dialedNumberStringBuilder.length() > 0) {
                view.callDialedNumber(dialedNumberStringBuilder.toString());
            } else {
                view.showDialer(false);
            }
        }
    }

    @Override
    public void onDialedNumberClick() {
        if (dialerState == STATE_COLLAPSED && view != null) {
            view.showDialer(true);
        }
    }

    @Override
    public void onDialerExpanded() {
        dialerState = STATE_EXPANDED;
    }

    @Override
    public void onDialerCollapsed() {
        dialerState = STATE_COLLAPSED;
    }

    @Override
    public void onDialerHidden() {
        dialerState = STATE_HIDDEN;
    }

    private void renderState() {
        if (view != null) {
            int length = dialedNumberStringBuilder.length();
            String dialedNumber = dialedNumberStringBuilder.toString();
            if (length > 9) {
                dialedNumber = dialedNumberStringBuilder.substring(length - 9, length);
            } else if (length == 1) {
                view.showDeleteButton(true);
                view.enableCallButton(true);
                view.enableDialerCollapse(true);
            } else if (length == 0) {
                view.showDeleteButton(false);
                view.enableCallButton(false);
                view.enableDialerCollapse(false);
            }
            view.renderDialedNumber(dialedNumber);
        }
    }
}