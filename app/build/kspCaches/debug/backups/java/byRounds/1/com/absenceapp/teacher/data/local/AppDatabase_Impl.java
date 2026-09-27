package com.absenceapp.teacher.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile RosterDao _rosterDao;

  private volatile PendingAttendanceDao _pendingAttendanceDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `rosters` (`seanceId` TEXT NOT NULL, `classeName` TEXT NOT NULL, `subject` TEXT NOT NULL, `salle` TEXT NOT NULL, `studentsJson` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`seanceId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pending_attendance` (`id` TEXT NOT NULL, `seanceId` TEXT NOT NULL, `absentStudentIds` TEXT NOT NULL, `timestamp` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `retryCount` INTEGER NOT NULL, `lastStatus` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '93231ac5081311eae69c63009b31d9a7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `rosters`");
        db.execSQL("DROP TABLE IF EXISTS `pending_attendance`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRosters = new HashMap<String, TableInfo.Column>(6);
        _columnsRosters.put("seanceId", new TableInfo.Column("seanceId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRosters.put("classeName", new TableInfo.Column("classeName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRosters.put("subject", new TableInfo.Column("subject", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRosters.put("salle", new TableInfo.Column("salle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRosters.put("studentsJson", new TableInfo.Column("studentsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRosters.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRosters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRosters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRosters = new TableInfo("rosters", _columnsRosters, _foreignKeysRosters, _indicesRosters);
        final TableInfo _existingRosters = TableInfo.read(db, "rosters");
        if (!_infoRosters.equals(_existingRosters)) {
          return new RoomOpenHelper.ValidationResult(false, "rosters(com.absenceapp.teacher.data.local.RoomSeanceRoster).\n"
                  + " Expected:\n" + _infoRosters + "\n"
                  + " Found:\n" + _existingRosters);
        }
        final HashMap<String, TableInfo.Column> _columnsPendingAttendance = new HashMap<String, TableInfo.Column>(7);
        _columnsPendingAttendance.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("seanceId", new TableInfo.Column("seanceId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("absentStudentIds", new TableInfo.Column("absentStudentIds", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("retryCount", new TableInfo.Column("retryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPendingAttendance.put("lastStatus", new TableInfo.Column("lastStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPendingAttendance = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPendingAttendance = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPendingAttendance = new TableInfo("pending_attendance", _columnsPendingAttendance, _foreignKeysPendingAttendance, _indicesPendingAttendance);
        final TableInfo _existingPendingAttendance = TableInfo.read(db, "pending_attendance");
        if (!_infoPendingAttendance.equals(_existingPendingAttendance)) {
          return new RoomOpenHelper.ValidationResult(false, "pending_attendance(com.absenceapp.teacher.data.local.RoomPendingAttendance).\n"
                  + " Expected:\n" + _infoPendingAttendance + "\n"
                  + " Found:\n" + _existingPendingAttendance);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "93231ac5081311eae69c63009b31d9a7", "fa897d85161bfa98d45b4811ac3b2bca");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "rosters","pending_attendance");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `rosters`");
      _db.execSQL("DELETE FROM `pending_attendance`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RosterDao.class, RosterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PendingAttendanceDao.class, PendingAttendanceDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RosterDao rosterDao() {
    if (_rosterDao != null) {
      return _rosterDao;
    } else {
      synchronized(this) {
        if(_rosterDao == null) {
          _rosterDao = new RosterDao_Impl(this);
        }
        return _rosterDao;
      }
    }
  }

  @Override
  public PendingAttendanceDao pendingAttendanceDao() {
    if (_pendingAttendanceDao != null) {
      return _pendingAttendanceDao;
    } else {
      synchronized(this) {
        if(_pendingAttendanceDao == null) {
          _pendingAttendanceDao = new PendingAttendanceDao_Impl(this);
        }
        return _pendingAttendanceDao;
      }
    }
  }
}
