package edu.usf.EventExpress.provider.user;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code user} table.
 */
public class UserContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return UserColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, UserSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public UserContentValues putGoogleId(String value) {
        if (value == null) throw new IllegalArgumentException("value for googleId must not be null");
        mContentValues.put(UserColumns.GOOGLE_ID, value);
        return this;
    }



    public UserContentValues putUserName(String value) {
        if (value == null) throw new IllegalArgumentException("value for userName must not be null");
        mContentValues.put(UserColumns.USER_NAME, value);
        return this;
    }


}
