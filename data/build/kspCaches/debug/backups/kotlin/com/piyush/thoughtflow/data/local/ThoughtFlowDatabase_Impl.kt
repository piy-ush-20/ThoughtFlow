package com.piyush.thoughtflow.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ThoughtFlowDatabase_Impl : ThoughtFlowDatabase() {
  private val _documentDao: Lazy<DocumentDao> = lazy {
    DocumentDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "03d0b92da7377c00a0b30404bedfc182", "7624bf31d91a568e617fc8bce218976b") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `documents` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `bodyMarkdown` TEXT NOT NULL, `createdAtEpochMs` INTEGER NOT NULL, `updatedAtEpochMs` INTEGER NOT NULL, `status` TEXT NOT NULL, `wordCount` INTEGER NOT NULL, `lastExportAtEpochMs` INTEGER, `formatterUsed` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '03d0b92da7377c00a0b30404bedfc182')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `documents`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsDocuments: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDocuments.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("bodyMarkdown", TableInfo.Column("bodyMarkdown", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("createdAtEpochMs", TableInfo.Column("createdAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("updatedAtEpochMs", TableInfo.Column("updatedAtEpochMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("wordCount", TableInfo.Column("wordCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("lastExportAtEpochMs", TableInfo.Column("lastExportAtEpochMs", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDocuments.put("formatterUsed", TableInfo.Column("formatterUsed", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDocuments: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDocuments: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDocuments: TableInfo = TableInfo("documents", _columnsDocuments, _foreignKeysDocuments, _indicesDocuments)
        val _existingDocuments: TableInfo = read(connection, "documents")
        if (!_infoDocuments.equals(_existingDocuments)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |documents(com.piyush.thoughtflow.data.local.DocumentEntity).
              | Expected:
              |""".trimMargin() + _infoDocuments + """
              |
              | Found:
              |""".trimMargin() + _existingDocuments)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "documents")
  }

  public override fun clearAllTables() {
    super.performClear(false, "documents")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(DocumentDao::class, DocumentDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun documentDao(): DocumentDao = _documentDao.value
}
