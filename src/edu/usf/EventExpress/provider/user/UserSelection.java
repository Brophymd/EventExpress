package edu.usf.EventExpress.provider.user;

import java.util.Date;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import edu.usf.EventExpress.provider.base.AbstractSelection;

/**
 * Selection for the {@code user} table.
 */
public class UserSelection extends AbstractSelection<UserSelection> {
    @Override
    public Uri uri() {
        return UserColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @param sortOrder How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort
     *            order, which may be unordered.
     * @return A {@code UserCursor} object, which is positioned before the first entry, or null.
     */
    public UserCursor query(ContentResolver contentResolver, String[] projection, String sortOrder) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), sortOrder);
        if (cursor == null) return null;
        return new UserCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null}.
     */
    public UserCursor query(ContentResolver contentResolver, String[] projection) {
        return query(contentResolver, projection, null);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, projection, null, null}.
     */
    public UserCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null, null);
    }


    public UserSelection id(long... value) {
        addEquals(UserColumns._ID, toObjectArray(value));
        return this;
    }


    public UserSelection googleId(String... value) {
        addEquals(UserColumns.GOOGLE_ID, value);
        return this;
    }

    public UserSelection googleIdNot(String... value) {
        addNotEquals(UserColumns.GOOGLE_ID, value);
        return this;
    }

    public UserSelection googleIdLike(String... value) {
        addLike(UserColumns.GOOGLE_ID, value);
        return this;
    }

    public UserSelection userName(String... value) {
        addEquals(UserColumns.USER_NAME, value);
        return this;
    }

    public UserSelection userNameNot(String... value) {
        addNotEquals(UserColumns.USER_NAME, value);
        return this;
    }

    public UserSelection userNameLike(String... value) {
        addLike(UserColumns.USER_NAME, value);
        return this;
    }
}
