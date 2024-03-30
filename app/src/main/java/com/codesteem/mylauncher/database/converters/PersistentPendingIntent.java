package com.codesteem.mylauncher.database.converters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public final class PersistentPendingIntent implements Parcelable {
    private enum PendingIntentType {
        SERVICE,
        BROADCAST,
        ACTIVITY
    }

    @NonNull
    private final PendingIntentType pendingIntentType;
    private final int requestCode;
    private final int flags;
    @NonNull
    private final Intent intent;

    private PersistentPendingIntent(@NonNull Builder builder) {
        this.pendingIntentType = builder.pendingIntentType;
        this.requestCode = builder.requestCode;
        this.flags = builder.flags;
        this.intent = Objects.requireNonNull(builder.intent);
    }

    @Nullable
    public PendingIntent getPendingIntent(@NonNull Context context) {
        PendingIntent pendingIntent = null;
        switch (pendingIntentType) {
            case SERVICE:
                pendingIntent = PendingIntent.getService(context, requestCode, intent, flags);
                break;
            case BROADCAST:
                pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags);
                break;
            case ACTIVITY:
                pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags);
                break;
        }
        return pendingIntent;
    }

    @NonNull
    public static Builder builder(@NonNull PendingIntentType pendingIntentType, int requestCode, @NonNull Intent intent, int flags) {
        return new Builder(pendingIntentType, requestCode, intent, flags);
    }

    public static final class Builder {
        @NonNull
        private final PendingIntentType pendingIntentType;
        private final int requestCode;
        private final int flags;
        @NonNull
        private Intent intent;

        private Builder(@NonNull PendingIntentType pendingIntentType, int requestCode, @NonNull Intent intent, int flags) {
            this.pendingIntentType = pendingIntentType;
            this.requestCode = requestCode;
            this.intent = intent;
            this.flags = flags;
        }

        @NonNull
        public Builder setIntent(@NonNull Intent intent) {
            this.intent = Objects.requireNonNull(intent);
            return this;
        }

        @NonNull
        public PersistentPendingIntent build() {
            return new PersistentPendingIntent(this);
        }
    }

    private PersistentPendingIntent(Parcel in) {
        int tmpPendingIntentType = in.readInt();
        this.pendingIntentType = PendingIntentType.values()[tmpPendingIntentType];
        this.requestCode = in.readInt();
        this.flags = in.readInt();
        this.intent = in.readParcelable(Intent.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pendingIntentType.ordinal());
        dest.writeInt(this.requestCode);
        dest.writeInt(this.flags);
        dest.writeParcelable(this.intent, flags);
    }

    @SuppressWarnings("unused")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersistentPendingIntent)) return false;
        PersistentPendingIntent that = (PersistentPendingIntent) o;
        return getRequestCode() == that.getRequestCode() &&
                getFlags() == that.getFlags() &&
                pendingIntentType == that.pendingIntentType &&
                Objects.equals(getIntent(), that.getIntent());
    }

    @SuppressWarnings("unused")
    @Override
    public int hashCode() {
        return Objects.hash(pendingIntentType, getRequestCode(), getFlags(), getIntent());
    }

    @NonNull
    public PendingIntentType getPendingIntentType() {
        return pendingIntentType;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getFlags() {
        return flags;
    }

    @NonNull
    public Intent getIntent() {
        return intent;
    }

    public static final Creator<PersistentPendingIntent> CREATOR = new Creator<PersistentPendingIntent>() {
        @Override
        public PersistentPendingIntent createFromParcel(Parcel in) {
            return new PersistentPendingIntent(in
