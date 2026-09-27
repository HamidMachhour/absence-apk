package com.absenceapp.teacher.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RosterDao_Impl implements RosterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RoomSeanceRoster> __insertionAdapterOfRoomSeanceRoster;

  public RosterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRoomSeanceRoster = new EntityInsertionAdapter<RoomSeanceRoster>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `rosters` (`seanceId`,`classeName`,`subject`,`salle`,`studentsJson`,`updatedAt`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RoomSeanceRoster entity) {
        statement.bindString(1, entity.getSeanceId());
        statement.bindString(2, entity.getClasseName());
        statement.bindString(3, entity.getSubject());
        statement.bindString(4, entity.getSalle());
        statement.bindString(5, entity.getStudentsJson());
        statement.bindLong(6, entity.getUpdatedAt());
      }
    };
  }

  @Override
  public Object saveRoster(final RoomSeanceRoster roster,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRoomSeanceRoster.insert(roster);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRosterBySeance(final String seanceId,
      final Continuation<? super RoomSeanceRoster> $completion) {
    final String _sql = "SELECT * FROM rosters WHERE seanceId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, seanceId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RoomSeanceRoster>() {
      @Override
      @Nullable
      public RoomSeanceRoster call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSeanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "seanceId");
          final int _cursorIndexOfClasseName = CursorUtil.getColumnIndexOrThrow(_cursor, "classeName");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfSalle = CursorUtil.getColumnIndexOrThrow(_cursor, "salle");
          final int _cursorIndexOfStudentsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studentsJson");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final RoomSeanceRoster _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSeanceId;
            _tmpSeanceId = _cursor.getString(_cursorIndexOfSeanceId);
            final String _tmpClasseName;
            _tmpClasseName = _cursor.getString(_cursorIndexOfClasseName);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpSalle;
            _tmpSalle = _cursor.getString(_cursorIndexOfSalle);
            final String _tmpStudentsJson;
            _tmpStudentsJson = _cursor.getString(_cursorIndexOfStudentsJson);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RoomSeanceRoster(_tmpSeanceId,_tmpClasseName,_tmpSubject,_tmpSalle,_tmpStudentsJson,_tmpUpdatedAt);
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

  @Override
  public Object getLastRoster(final Continuation<? super RoomSeanceRoster> $completion) {
    final String _sql = "SELECT * FROM rosters ORDER BY updatedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RoomSeanceRoster>() {
      @Override
      @Nullable
      public RoomSeanceRoster call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSeanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "seanceId");
          final int _cursorIndexOfClasseName = CursorUtil.getColumnIndexOrThrow(_cursor, "classeName");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfSalle = CursorUtil.getColumnIndexOrThrow(_cursor, "salle");
          final int _cursorIndexOfStudentsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studentsJson");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final RoomSeanceRoster _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSeanceId;
            _tmpSeanceId = _cursor.getString(_cursorIndexOfSeanceId);
            final String _tmpClasseName;
            _tmpClasseName = _cursor.getString(_cursorIndexOfClasseName);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpSalle;
            _tmpSalle = _cursor.getString(_cursorIndexOfSalle);
            final String _tmpStudentsJson;
            _tmpStudentsJson = _cursor.getString(_cursorIndexOfStudentsJson);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RoomSeanceRoster(_tmpSeanceId,_tmpClasseName,_tmpSubject,_tmpSalle,_tmpStudentsJson,_tmpUpdatedAt);
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

  @Override
  public Flow<List<RoomSeanceRoster>> observeAllRosters() {
    final String _sql = "SELECT * FROM rosters ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"rosters"}, new Callable<List<RoomSeanceRoster>>() {
      @Override
      @NonNull
      public List<RoomSeanceRoster> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSeanceId = CursorUtil.getColumnIndexOrThrow(_cursor, "seanceId");
          final int _cursorIndexOfClasseName = CursorUtil.getColumnIndexOrThrow(_cursor, "classeName");
          final int _cursorIndexOfSubject = CursorUtil.getColumnIndexOrThrow(_cursor, "subject");
          final int _cursorIndexOfSalle = CursorUtil.getColumnIndexOrThrow(_cursor, "salle");
          final int _cursorIndexOfStudentsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studentsJson");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RoomSeanceRoster> _result = new ArrayList<RoomSeanceRoster>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RoomSeanceRoster _item;
            final String _tmpSeanceId;
            _tmpSeanceId = _cursor.getString(_cursorIndexOfSeanceId);
            final String _tmpClasseName;
            _tmpClasseName = _cursor.getString(_cursorIndexOfClasseName);
            final String _tmpSubject;
            _tmpSubject = _cursor.getString(_cursorIndexOfSubject);
            final String _tmpSalle;
            _tmpSalle = _cursor.getString(_cursorIndexOfSalle);
            final String _tmpStudentsJson;
            _tmpStudentsJson = _cursor.getString(_cursorIndexOfStudentsJson);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RoomSeanceRoster(_tmpSeanceId,_tmpClasseName,_tmpSubject,_tmpSalle,_tmpStudentsJson,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
