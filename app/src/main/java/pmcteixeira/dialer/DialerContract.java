package pmcteixeira.dialer;

import android.support.annotation.NonNull;

public interface DialerContract {

    interface View {
        void renderDialedNumber(@NonNull String dialNumber);

        void showDialer(boolean show);

        void callDialedNumber(@NonNull String dialedNumber);

        void showDeleteButton(boolean show);

        void enableCallButton(boolean enable);

        void enableDialerCollapse(boolean enable);
    }

    interface Presenter {
        void onStart(@NonNull DialerContract.View view);

        void onStop();

        void onDialNumberClick(@NonNull String dialNumber);

        void onRemoveLastDialNumber();

        void onClearDialNumber();

        void onCall();

        void onDialedNumberClick();

        void onDialerExpanded();

        void onDialerCollapsed();

        void onDialerHidden();
    }
}