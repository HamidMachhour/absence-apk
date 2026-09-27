package com.absenceapp.teacher.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PendingAttendanceDao_Impl implements PendingAttendanceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RoomPendingAttendance> __insertionAdapterOfRoomPendingAttendance;

  private final EntityDeletionOrUpdateAdapter<RoomPendingAttendance> __updateAdapterOfRoomPendingAttendance;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public PendingAttendanceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRoomPendingAttendance = new EntityInsertionAdapter<RoomPendingAttendance>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pending_attendance` (`id`,`seanceId`,`absentStudentIds`,`timestamp`,`createdAt`,`retryCount`,`lastStatus`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoomPendingAttendance entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSeanceId());
        statement.bindString(3, entity.getAbsentStudentIds());
        statement.bindString(4, entity.getTimestamp());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getRetryCount());
        statement.bindString(7, entity.getLastStatus());
      }
    };
    this.__updateAdapterOfRoomPendingAttendance = new EntityDeletionOrUpdateAdapter<RoomPendingAttendance>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pending_attendance` SET `id` = ?,`seanceId` = ?,`absentStudentIds` = ?,`timestamp` = ?,`createdAt` = ?,`retryCount` = ?,`lastStatus` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoomPendingAttendance entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getSeanceId());
        statement.bindString(3, entity.getAbsentStudentIds());
        statement.bindString(4, entity.getTimestamp());
        statement.bindLong(5, entity.getCreatedAt());
        statement.bindLong(6, entity.getRetryCount());
        statement.bindString(7, entity.getLastStatus());
        statement.bindString(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM pending_attendance WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object save(final RoomPendingAttendance item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRoomPendingAttendance.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RoomPendingAttendance item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRoomPendingAttendance.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllPending(final Continuation<? super List<RoomPendingAttendance>> $completion) {
    final String _sql = "SELECT * FROM pending_attendance ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RoomPendingAttendance>>() {
      @Override
      @NonNull
      public List<RoomPendingAttendance> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSeanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "seanceId");
          final int _cursorIndexOfAbsentStudentIds = CursorUtil.getColumnIndexOrThrow(_cursor, "absentStudentIds");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retryCount");
          final int _cursorIndexOfLastStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStatus");
          final List<RoomPendingAttendance> _result = new ArrayList<RoomPendingAttendance>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RoomPendingAttendance _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSeanceId;
            _tmpSeanceId = _cursor.getString(_cursorIndexOfSeanceId);
            final String _tmpAbsentStudentIds;
            _tmpAbsentStudentIds = _cursor.getString(_cursorIndexOfAbsentStudentIds);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmpRetryCount;
            _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
            final String _tmpLastStatus;
            _tmpLastStatus = _cursor.getString(_cursorIndexOfLastStatus);
            _item = new RoomPendingAttendance(_tmpId,_tmpSeanceId,_tmpAbsentStudentIds,_tmpTimestamp,_tmpCreatedAt,_tmpRetryCount,_tmpLastStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final String id,
      final Continuation<? super RoomPendingAttendance> $completion) {
    final String _sql = "SELECT * FROM pending_attendance WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RoomPendingAttendance>() {
      @Override
      @Nullable
      public RoomPendingAttendance call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSeanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "seanceId");
          final int _cursorIndexOfAbsentStudentIds = CursorUtil.getColumnIndexOrThrow(_cursor, "absentStudentIds");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retryCount");
          final int _cursorIndexOfLastStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStatus");
          final RoomPendingAttendance _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpSeanceId;
            _tmpSeanceId = _cursor.getString(_cursorIndexOfSeanceId);
            final String _tmpAbsentStudentIds;
            _tmpAbsentStudentIds = _cursor.getString(_cursorIndexOfAbsentStudentIds);
            final String _tmpTimestamp;
            _tmpTimestamp = _cursor.getString(_cursorIndexOfTimestamp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final int _tmpRetryCount;
            _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
            final String _tmpLastStatus;
            _tmpLastStatus = _cursor.getString(_cursorIndexOfLastStatus);
            _result = new RoomPendingAttendance(_tmpId,_tmpSeanceId,_tmpAbsentStudentIds,_tmpTimestamp,_tmpCreatedAt,_tmpRetryCount,_tmpLastStatus);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
